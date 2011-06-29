package uk.ac.ebi.ep.uniprot.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import uk.ac.ebi.ep.search.model.EnzymeAccession;
import uk.ac.ebi.ep.search.model.EnzymeSummary;
import uk.ac.ebi.ep.search.model.Species;
import uk.ac.ebi.kraken.interfaces.uniprot.Organism;
import uk.ac.ebi.kraken.interfaces.uniprot.ProteinDescription;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.Comment;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.CommentType;
import uk.ac.ebi.kraken.interfaces.uniprot.description.Name;
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
        public GetEntriesCaller() {
        }
        public GetEntriesCaller(String accession) {
            this.accession = accession;
        }


        public EnzymeSummary call() throws Exception {
            return getEnzymeEntry();
        }

        public EnzymeSummary getEnzymeEntry()  {
            //Retrieve UniProt entry by its accession number
            UniProtEntry entry = (UniProtEntry) entryRetrievalService
                    .getUniProtEntry(accession);
            return setEnzymeProperties(entry);
        }

    public EnzymeSummary setEnzymeProperties(UniProtEntry entry) {
        EnzymeSummary enzymeSummary = new EnzymeSummary();
        enzymeSummary.getUniprotaccessions().add(entry.getPrimaryUniProtAccession().getValue());
        enzymeSummary.setUniprotid(entry.getUniProtId().getValue());
        if (entry != null) {            
            List<Comment> commentList = entry.getComments(CommentType.FUNCTION);
            String function = Transformer.getFunction(commentList);
            enzymeSummary.setFunction(function);

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

            if (desc.hasAlternativeNames()){
                List<Name> names = desc.getAlternativeNames();
                enzymeSummary.getSynonym().addAll(Transformer.getAltNames(names));
            }
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

  }
//******************************** INNER CLASS *******************************//

    public static class QueryEntryByIdCaller implements Callable<EnzymeSummary> {
        protected String query;

        protected UniProtEntry mainEntry;

        public QueryEntryByIdCaller(String query) {
            this.query = query;
        }


        public EnzymeSummary call() throws Exception {
            return queryEntry();
        }
/*
        public EnzymeSummary queryEntry()  {
            //Retrieve UniProt entry by its accession number
            Query uniprotQuery = UniProtQueryBuilder.buildQuery(query);
            List<EnzymeAccession> speciesList = getSpecies(uniprotQuery);
            EntryIterator<UniProtEntry> entries = queryService.getEntryIterator(uniprotQuery);
            int resultSize = entries.getResultSize();
            EnzymeSummary enzymeSummary = new EnzymeSummary();
            if (resultSize > 0) {
                List<UniProtEntry> secEntries = separateEntries (entries);
                enzymeSummary = setMainEntry(this.mainEntry);
                if (secEntries.size() > 0) {
                    List<EnzymeAccession>  relSpecies = getRelatedSpecies(secEntries);
                    enzymeSummary.getRelatedspecies().addAll(relSpecies);
                }
            }
            else {
                int resSize = resultSize;
            }
            return enzymeSummary;
        }
*/
        public EnzymeSummary queryEntry()  {
            //Retrieve UniProt entry by its accession number
            Query uniprotQuery = UniProtQueryBuilder.buildQuery(query);
            List<EnzymeAccession> speciesList = getSpecies(uniprotQuery);
            EnzymeSummary enzymeSummary = null;
            if (speciesList.size() > 0) {
                EnzymeAccession defaultSpecies = speciesList.get(0);               
                GetEntriesCaller caller = new GetEntriesCaller(
                defaultSpecies.getUniprotaccessions().get(0));
                enzymeSummary = caller.getEnzymeEntry();
                if (speciesList.size() > 1) {
                    speciesList.remove(defaultSpecies);
                    enzymeSummary.getRelatedspecies().addAll(speciesList);
                }
                
            }
            return enzymeSummary;
        }

        public EnzymeSummary setMainEntry(UniProtEntry mainEntry) {
            EnzymeSummary enzymeSummary = new EnzymeSummary();
            GetEntriesCaller caller = new GetEntriesCaller();
            enzymeSummary = caller.setEnzymeProperties(mainEntry);
            return enzymeSummary;
        }

        public List<EnzymeAccession> getSpecies(Query query) {
             AttributeIterator<UniProtEntry> attributes  = queryService
                     .getAttributes(query, "ognl:organism");
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
                 if (commonName.equalsIgnoreCase("HUMAN")) {
                     accSpeciesList.add(0,enzymeAccession);
                 } else {
                    accSpeciesList.add(enzymeAccession);
                 }

             }
             return accSpeciesList;
        }

        /*
        public List<EnzymeAccession> getRelatedSpecies(List<UniProtEntry> secEntries ) {
            List<EnzymeAccession> relatedSpecies = new ArrayList<EnzymeAccession>();
            Iterator it = secEntries.iterator();
            while (it.hasNext()) {
                UniProtEntry entry = (UniProtEntry)it.next();
                String commonName = entry.getOrganism().getCommonName().getValue();
                EnzymeAccession enzymeAccession = new EnzymeAccession();
                Species species = new Species();
                species.setCommonname(commonName);
                enzymeAccession.setSpecies(species);
                String accesion = entry.getPrimaryUniProtAccession().getValue();
                enzymeAccession.getUniprotaccessions().add(accesion);
                relatedSpecies.add(enzymeAccession);

            }
            return relatedSpecies;
        }
         *
         */
//                    AttributeIterator<UniProtEntry> speciesList  = queryService.getAttributes(uniprotQuery, "ognl:organism.scientificName");

        public List<UniProtEntry> separateEntries(EntryIterator<UniProtEntry> entries) {
            List<UniProtEntry> secEntries = new ArrayList<UniProtEntry>();
            boolean hasMainEntry = false;
            for (UniProtEntry uniProtEntry: entries) {
                String species = uniProtEntry.getOrganism().getCommonName().getValue();
                if (species.equalsIgnoreCase("HUMAN")) {
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

}
