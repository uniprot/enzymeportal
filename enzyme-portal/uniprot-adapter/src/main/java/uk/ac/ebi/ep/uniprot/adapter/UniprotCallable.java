package uk.ac.ebi.ep.uniprot.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import uk.ac.ebi.ep.enzyme.model.ChemicalEntity;
import uk.ac.ebi.ep.enzyme.model.Entity;
import uk.ac.ebi.ep.enzyme.model.Enzyme;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.enzyme.model.Molecule;
import uk.ac.ebi.ep.enzyme.model.Pathway;
import uk.ac.ebi.ep.enzyme.model.ReactionPathway;
import uk.ac.ebi.ep.enzyme.model.Sequence;
import uk.ac.ebi.ep.search.model.Compound;
import uk.ac.ebi.ep.search.model.EnzymeAccession;
import uk.ac.ebi.ep.search.model.EnzymeSummary;
import uk.ac.ebi.ep.search.model.Species;
import uk.ac.ebi.kraken.interfaces.uniprot.DatabaseType;
import uk.ac.ebi.kraken.interfaces.uniprot.Organism;
import uk.ac.ebi.kraken.interfaces.uniprot.ProteinDescription;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.Comment;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.CommentType;
import uk.ac.ebi.kraken.interfaces.uniprot.dbx.drugbank.DrugBank;
import uk.ac.ebi.kraken.interfaces.uniprot.dbx.reactome.Reactome;
import uk.ac.ebi.kraken.interfaces.uniprot.description.Name;
import uk.ac.ebi.kraken.model.uniprot.dbx.pdb.PdbImpl;
import uk.ac.ebi.kraken.uuw.services.remoting.Attribute;
import uk.ac.ebi.kraken.uuw.services.remoting.AttributeIterator;
import uk.ac.ebi.kraken.uuw.services.remoting.EntryIterator;
import uk.ac.ebi.kraken.uuw.services.remoting.EntryRetrievalService;
import uk.ac.ebi.kraken.uuw.services.remoting.Query;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtJAPI;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtQueryBuilder;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtQueryService;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class UniprotCallable {

//********************************* VARIABLES ********************************//
   protected  static EntryRetrievalService entryRetrievalService = UniProtJAPI
           .factory.getEntryRetrievalService();
   protected  static UniProtQueryService queryService = UniProtJAPI
           .factory.getUniProtQueryService();

//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

//******************************** INNER CLASS *******************************//

    public static class GetEntriesCaller implements Callable<EnzymeSummary> {
        protected String accession;
        //protected boolean entryPage;
        public GetEntriesCaller() {
        }

        public GetEntriesCaller(String accession) {
            this.accession = accession;
        }



        public EnzymeSummary call() throws Exception {
            return getEnzymeSummaryByAccession();
        }

        public String getAccession() {
            return accession;
        }

        public void setAccession(String accession) {
            this.accession = accession;
        }

        public EnzymeSummary getEnzymeSummaryByAccession()  {
            //Retrieve UniProt entry by its accession number
            UniProtEntry entry = (UniProtEntry) entryRetrievalService
                    .getUniProtEntry(accession);
            return setEzymeResult(entry);
        }

    public EnzymeSummary getEnzymeWithSequenceByAccession() {
        UniProtEntry entry = (UniProtEntry) entryRetrievalService
                .getUniProtEntry(accession);
        EnzymeModel enzymeModel =  (EnzymeModel)setEzymeResult(entry);
        int seqLength = entry.getSequence().getLength();
        int weight = entry.getSequence().getMolecularWeight();
        String seqUrl = IUniprotAdapter.SEQUENCE_URL_BASE
                +this.accession
                +IUniprotAdapter.SEQUENCE_URL_SUFFIX;

        Sequence seq = new Sequence();
        seq.setSequence(String.valueOf(seqLength));
        seq.setWeight(String.valueOf(weight));
        seq.setSequenceurl(seqUrl);
        Enzyme enzyme = new Enzyme();
        enzyme.setSequence(seq);
        enzymeModel.setEnzyme(enzyme);
        return enzymeModel;
    }

    public EnzymeSummary getEnzymePathwayByAccession() {
        UniProtEntry entry = (UniProtEntry) entryRetrievalService
                .getUniProtEntry(accession);
        EnzymeModel enzymeModel = (EnzymeModel)setEnzymeCommonProperties(entry);
        ReactionPathway reactionpathway = this.getReactomePathways(entry);
        enzymeModel.getReactionpathway().add(reactionpathway);
        return enzymeModel;
    }

    public EnzymeSummary getEnzymeDrugByAccession() {
        UniProtEntry entry = (UniProtEntry) entryRetrievalService
                .getUniProtEntry(accession);
        EnzymeModel enzymeModel = (EnzymeModel)setEnzymeCommonProperties(entry);
        //ReactionPathway reactionpathway = this.setReactomePathways(entry);
        List<Molecule>  molecules = this.getDrugBankAccessions(entry);
        ChemicalEntity chemicalEntity = new ChemicalEntity();
        chemicalEntity.setDrugs(molecules);
        enzymeModel.setMolecules(chemicalEntity);
        return enzymeModel;
    }

    public List<String> getPdbeAccessions(UniProtEntry entry) {
        List<PdbImpl> refList = entry.getDatabaseCrossReferences(DatabaseType.PDB);
        List<String> results = new ArrayList<String>();
        if (refList.size() > 0) {
            for (PdbImpl ref:refList) {
                String dbAccession = ref.getDbAccession();
                //PdbResolution resolution = ref.getPdbResolution();
                //String resolutionValue = resolution.getValue();
                if (dbAccession != null) {
                    //The url of pdbe images use lowercase
                    results.add(dbAccession.toLowerCase());
                }
            }
        }
        return results;
    }

   
    public ReactionPathway getReactomePathways(UniProtEntry entry) {
        List<Reactome> refList = entry.getDatabaseCrossReferences(DatabaseType.REACTOME);
        List<String> results = new ArrayList<String>();
        ReactionPathway reactionpathway = new ReactionPathway();
        if (refList.size() > 0) {
            for (Reactome ref:refList) {
                String dbAccession = ref.getReactomeAccessionNumber().getValue();
                if (dbAccession != null) {
                    ref.getReactomeDescription();
                    Pathway pathway = new Pathway();
                    pathway.setId(dbAccession);
                    pathway.setName(ref.getReactomeDescription().getValue());
                    reactionpathway.getPathways().add(pathway);
                }
            }
        }
        return reactionpathway;
    }

    public List<Molecule> getDrugBankAccessions(UniProtEntry entry) {
        List<DrugBank> refList = entry.getDatabaseCrossReferences(DatabaseType.DRUGBANK);        
        List<Molecule> molecules = new ArrayList<Molecule>();
        if (refList.size() > 0) {
            for (DrugBank ref:refList) {
                String dbAccession = ref.getDrugBankAccessionNumber().getValue();
                if (dbAccession != null) {
                    String name = ref.getDrugBankDescription().getValue();
                    Molecule molecule = new Molecule();
                    molecule.getXrefs().add(ref.getDrugBankAccessionNumber().getValue());
                    molecules.add(molecule);
                }
            }
        }
        return molecules;
    }

    public EnzymeSummary setEnzymeCommonProperties(UniProtEntry entry) {
        EnzymeModel enzymeSummary = new EnzymeModel();
        enzymeSummary.getUniprotaccessions().add(entry.getPrimaryUniProtAccession().getValue());
        enzymeSummary.setUniprotid(entry.getUniProtId().getValue());
        if (entry != null) {
            ProteinDescription desc = entry.getProteinDescription();
            String name = null;
            if (desc.hasSubNames()){
                 List<Name> names  = desc.getSubNames();
                name = Transformer.getFullName(names.get(0));
            }
            else {
                Name recName = desc.getRecommendedName();
                name = Transformer.getFullName(recName);
            }

            enzymeSummary.setName(name);

            enzymeSummary.getEc().addAll(desc.getEcNumbers());
            String speciesCommonName = entry.getOrganism().getCommonName().getValue();
            String scientificName = entry.getOrganism().getScientificName().getValue();
            Species species = new Species();
            species.setCommonname(speciesCommonName);
            species.setScientificname(scientificName);
            enzymeSummary.setSpecies(species);
        }
        return enzymeSummary;
    }
    
    public EnzymeSummary setEzymeResult(UniProtEntry entry) {
        //EnzymeSummary enzymeSummary = new EnzymeSummary();
        EnzymeSummary enzymeSummary = setEnzymeCommonProperties(entry);
        if (entry != null) {            
            List<Comment> commentList = entry.getComments(CommentType.FUNCTION);
            String function = Transformer.getFunction(commentList);
            enzymeSummary.setFunction(function);

            ProteinDescription desc = entry.getProteinDescription();            
            if (desc.hasAlternativeNames()){
                List<Name> names = desc.getAlternativeNames();
                enzymeSummary.getSynonym().addAll(Transformer.getAltNames(names));
            }
            enzymeSummary.setPdbeaccession(this.getPdbeAccessions(entry));                     
        }
        return enzymeSummary;
    }

  }
//******************************** INNER CLASS *******************************//
    /**
     * A caller that implements the {@link Callable} interface that can be called
     * to perform concurrent queries to Uniprot API to get Uniprot entries grouped by species.
     */
    public static class QueryEntryByIdCaller implements Callable<EnzymeSummary> {    
        protected UniProtEntry mainEntry;
        protected String defaultSpecies;
        protected Query uniprotQuery;

        public QueryEntryByIdCaller(String query, String defaultSpecies) {
            uniprotQuery = UniProtQueryBuilder.buildQuery(query);
            this.defaultSpecies = defaultSpecies;
        }


        public EnzymeSummary call() throws Exception {
            return queryforEnzymeSummaryEntry();
        }

        public EnzymeSummary queryforEnzymeSummaryEntry()  {
            List<EnzymeAccession> speciesList = getSpecies(0);
            EnzymeSummary enzymeSummary = null;
            if (speciesList.size() > 0) {
                EnzymeAccession topSpecies = speciesList.get(0);
                GetEntriesCaller caller = new GetEntriesCaller(
                topSpecies.getUniprotaccessions().get(0));
                //Retieve the main entry
                enzymeSummary = caller.getEnzymeSummaryByAccession();

                //Add related species
                if (speciesList.size() > 1) {
                    speciesList.remove(topSpecies);
                    enzymeSummary.getRelatedspecies().addAll(speciesList);
                }
                
            }
            return enzymeSummary;
        }

        public EnzymeSummary queryforEnzymeSequenceEntry()  {
            List<EnzymeAccession> speciesList = getSpecies(IUniprotAdapter.SPECIES_BRIEF_MAX_SIZE);
            EnzymeSummary enzymeSummary = null;
            if (speciesList.size() > 0) {
                EnzymeAccession topSpecies = speciesList.get(0);
                GetEntriesCaller caller = new GetEntriesCaller(
                topSpecies.getUniprotaccessions().get(0));
                //Retieve the main entry
                enzymeSummary = caller.getEnzymeWithSequenceByAccession();
                //Add related species
                if (speciesList.size() > 1) {
                    speciesList.remove(topSpecies);
                    enzymeSummary.getRelatedspecies().addAll(speciesList);
                }

            }
            return enzymeSummary;
        }

        public EnzymeSummary queryforEnzymePathwayEntry()  {
            List<EnzymeAccession> speciesList = getSpecies(IUniprotAdapter.SPECIES_BRIEF_MAX_SIZE);
            EnzymeSummary enzymeSummary = null;
            if (speciesList.size() > 0) {
                EnzymeAccession topSpecies = speciesList.get(0);
                GetEntriesCaller caller = new GetEntriesCaller(
                topSpecies.getUniprotaccessions().get(0));
                //Retieve the main entry
                enzymeSummary = caller.getEnzymePathwayByAccession();
                //Add related species
                if (speciesList.size() > 1) {
                    //The top species is not set in getEnzymePathwaysByAccession method
                    speciesList.remove(topSpecies);
                    enzymeSummary.getRelatedspecies().addAll(speciesList);
                }

            }
            return enzymeSummary;
        }

        public List<EnzymeAccession> getSpecies(int size) {
             AttributeIterator<UniProtEntry> attributes  = queryService
                     .getAttributes(uniprotQuery, "ognl:organism");
             List<EnzymeAccession> accSpeciesList = new ArrayList<EnzymeAccession>();
             for (Attribute att : attributes) {
                Organism organism = (Organism)att.getValue();
                String commonName = organism.getCommonName().getValue();
                String scientificName = organism.getScientificName().getValue();
                EnzymeAccession enzymeAccession = new EnzymeAccession();
                enzymeAccession.getUniprotaccessions().add(att.getAccession());
                Species species = new Species();
                species.setCommonname(commonName);
                species.setScientificname(scientificName);
                enzymeAccession.setSpecies(species);
                 //if (commonName.equalsIgnoreCase(this.defaultSpecies)) {
                if (scientificName.equalsIgnoreCase(this.defaultSpecies)) {
                     accSpeciesList.add(0,enzymeAccession);
                 } else {
                    accSpeciesList.add(enzymeAccession);
                 }

                if (accSpeciesList.size() == size) {
                    break;
                }
             }
             return accSpeciesList;

        }

        public List<UniProtEntry> separateEntries(EntryIterator<UniProtEntry> entries) {
            List<UniProtEntry> secEntries = new ArrayList<UniProtEntry>();
            boolean hasMainEntry = false;
            for (UniProtEntry uniProtEntry: entries) {
                String species = uniProtEntry.getOrganism().getCommonName().getValue();
                if (species.equalsIgnoreCase(this.defaultSpecies)) {
                    this.mainEntry = uniProtEntry;
                    hasMainEntry = true;
                } else {
                    secEntries.add(uniProtEntry);
                }

                String accesion = uniProtEntry.getPrimaryUniProtAccession().getValue();
            }
            if (!hasMainEntry) {
                this.mainEntry = secEntries.get(0);
                secEntries.remove(this.mainEntry);
            }
            return secEntries;
        }

  }

//******************************** INNER CLASS *******************************//

    public static class GetSpeciesCaller implements Callable<Map<String,String>> {
        protected String query;

        public GetSpeciesCaller(String query) {
            this.query = query;
        }


        public Map<String,String> call() throws Exception {
            return getSpecies();
        }
        public Map<String,String> getSpecies() {
            Query uniprotQuery = UniProtQueryBuilder.buildQuery(query);
            return getSpecies(uniprotQuery);
        }
        public Map<String,String> getSpecies(Query uniprotQuery) {
             AttributeIterator<UniProtEntry> attributes  = queryService
                     .getAttributes(uniprotQuery, "ognl:organism");
             Map<String,String> speciesMap = new HashMap<String, String>();
             for (Attribute att : attributes) {
                Organism organism = (Organism)att.getValue();
                String commonName = organism.getCommonName().getValue();
                String scientificName = organism.getScientificName().getValue();
                speciesMap.put(scientificName, commonName);

             }
             return speciesMap;
        }

        public Map<String,String> getIds(Query uniprotQuery) {
             AttributeIterator<UniProtEntry> attributes  = queryService
                     .getAttributes(uniprotQuery, "ognl:organism");
             Map<String,String> speciesMap = new HashMap<String, String>();
             for (Attribute att : attributes) {
                Organism organism = (Organism)att.getValue();
                String commonName = organism.getCommonName().getValue();
                String scientificName = organism.getScientificName().getValue();
                speciesMap.put(scientificName, commonName);

             }
             return speciesMap;
        }

  }

}
