/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.base.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml_cml.schema.cml2.react.Reaction;
import uk.ac.ebi.ep.data.common.CommonSpecies;
import uk.ac.ebi.ep.data.domain.EnzymePortalCompound;
import uk.ac.ebi.ep.data.domain.EnzymePortalEcNumbers;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.domain.UniprotXref;
import uk.ac.ebi.ep.data.enzyme.model.ChemicalEntity;
import uk.ac.ebi.ep.data.enzyme.model.CountableMolecules;
import uk.ac.ebi.ep.data.enzyme.model.EcClass;
import uk.ac.ebi.ep.data.enzyme.model.Enzyme;
import uk.ac.ebi.ep.data.enzyme.model.EnzymeHierarchy;
import uk.ac.ebi.ep.data.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.data.enzyme.model.EnzymeReaction;
import uk.ac.ebi.ep.data.enzyme.model.Molecule;
import uk.ac.ebi.ep.data.enzyme.model.Pathway;
import uk.ac.ebi.ep.data.enzyme.model.ProteinStructure;
import uk.ac.ebi.ep.data.enzyme.model.ReactionPathway;
import uk.ac.ebi.ep.data.enzyme.model.Sequence;
import uk.ac.ebi.ep.data.exceptions.EnzymeRetrieverException;
import uk.ac.ebi.ep.data.exceptions.MultiThreadingException;
import uk.ac.ebi.ep.data.search.model.Compound;
import uk.ac.ebi.ep.data.search.model.Disease;
import uk.ac.ebi.ep.data.search.model.EnzymeAccession;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
import uk.ac.ebi.ep.ebeye.EbeyeRestService;
import uk.ac.ebi.ep.enzymeservices.chebi.ChebiAdapter;
import uk.ac.ebi.ep.enzymeservices.chebi.ChebiFetchDataException;
import uk.ac.ebi.ep.enzymeservices.chebi.IChebiAdapter;
import uk.ac.ebi.ep.enzymeservices.intenz.IntenzAdapter;
import uk.ac.ebi.ep.enzymeservices.rhea.IRheaAdapter;
import uk.ac.ebi.ep.enzymeservices.rhea.RheaWsAdapter;
import uk.ac.ebi.ep.literatureservice.service.LiteratureService;
import uk.ac.ebi.ep.uniprotservice.transferObjects.LabelledCitation;
import uk.ac.ebi.rhea.ws.client.RheaFetchDataException;

/**
 *
 * @author joseph
 */
public class EnzymeRetriever extends EnzymeFinder {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnzymeRetriever.class);
    protected IRheaAdapter rheaAdapter;
    protected IChebiAdapter chebiAdapter;
    //protected ILiteratureAdapter litAdapter;
    //@Autowired
    private LiteratureService literatureService;

    public EnzymeRetriever(EnzymePortalService service, EbeyeRestService eService) {
        super(service, eService);

        rheaAdapter = new RheaWsAdapter();

    }

    public EnzymeRetriever(EnzymePortalService service, EbeyeRestService eService, LiteratureService pmc) {
        this(service, eService);

        this.literatureService = pmc;

    }

    /**
     * Lazily constructs a new adapter if needed.
     *
     * @return a ChEBI adapter.
     */
    public IChebiAdapter getChebiAdapter() {
        if (chebiAdapter == null) {
            chebiAdapter = new ChebiAdapter();
        }
        return chebiAdapter;
    }

//    public ILiteratureAdapter getLiteratureAdapter() {
//        if (litAdapter == null) {
//            litAdapter = new SimpleLiteratureAdapter();
//        }
//        return litAdapter;
//    }
    public IntenzAdapter getIntenzAdapter() {
        if (intenzAdapter == null) {
            intenzAdapter = new IntenzAdapter();
        }

        return intenzAdapter;
    }

    List<String> computeSynonymNames(UniprotEntry uniprotEntry) {
        List<String> synonyms = new LinkedList<>();

        String namesColumn = uniprotEntry.getSynonymNames();

        if (namesColumn != null && namesColumn.contains(";")) {
            String[] syn = namesColumn.split(";");
            for (String x : syn) {

                synonyms.addAll(parseNameSynonyms(x));
            }
        }
        return synonyms.stream().sorted().distinct().collect(Collectors.toList());

    }

    private List<EnzymeAccession> getRelatedSPecies(UniprotEntry uniprotEntry) {
        String defaultSpecies = CommonSpecies.HUMAN.getScientificName();

        List<EnzymeAccession> relatedSpecies = new LinkedList<>();
        // TODO query for related proteins and use the obj. possible null pointer if db is not populated with related protein

        List<EnzymePortalCompound> compounds = super.getService().findCompoundsByAccession(uniprotEntry.getAccession());
        for (UniprotEntry e : uniprotEntry.getRelatedProteinsId().getUniprotEntrySet()) {

            EnzymeAccession ea = new EnzymeAccession();
            //ea.setCompounds(e.getEnzymePortalCompoundSet().stream().distinct().collect(Collectors.toList()));
            ea.setCompounds(compounds.stream().distinct().collect(Collectors.toList()));
            ea.setDiseases(e.getEnzymePortalDiseaseSet().stream().distinct().collect(Collectors.toList()));

            ea.setPdbeaccession(e.getPdbeaccession());
            ea.getUniprotaccessions().add(e.getAccession());
            ea.setSpecies(e.getSpecies());
            ea.setUniprotid(e.getName());

            if (e.getScientificName() != null && e.getScientificName().equalsIgnoreCase(defaultSpecies)) {

                relatedSpecies.add(0, ea);

            } else if (e.getScientificName() != null && !e.getScientificName().equalsIgnoreCase(defaultSpecies)) {
                relatedSpecies.add(ea);

            }

        }
        return relatedSpecies.stream().distinct().collect(Collectors.toList());

    }

    private EnzymeModel getEnzymeModel(String uniprotAccession) {
        UniprotEntry uniprotEntry = super.getService().findByAccession(uniprotAccession);

        EnzymeModel model = new EnzymeModel();

        if (uniprotEntry != null) {
            Enzyme enzyme = new Enzyme();

            Sequence sequence = new Sequence();
            sequence.setLength(uniprotEntry.getSequenceLength());
            enzyme.setSequence(sequence);

            //suppliment info
            model.setCommonName(uniprotEntry.getCommonName());
            model.setFunction(uniprotEntry.getFunction());
            model.setProteinName(uniprotEntry.getProteinName());
            model.setSpecies(uniprotEntry.getSpecies());
            model.setScientificName(uniprotEntry.getScientificName());
            model.setCommonName(uniprotEntry.getCommonName());

            model.setName(uniprotEntry.getProteinName());

            model.setSynonyms(uniprotEntry.getSynonym());

            model.setRelatedspecies(getRelatedSPecies(uniprotEntry));

            model.setAccession(uniprotEntry.getAccession());
            model.getUniprotaccessions().add(uniprotEntry.getAccession());
            //model.setSpecies(uniprotEntry.getSpecies());
            Set<EnzymePortalEcNumbers> ecNumbers = getService().findByEcNumbersByAccession(uniprotAccession)
                    .stream().collect(Collectors.toSet());
            model.setEnzymePortalEcNumbersSet(ecNumbers);
            // uniprotEntry.getEnzymePortalEcNumbersSet().stream().forEach((ec) -> {
            ecNumbers.stream().forEach(ec -> {
                //Enzyme enzyme = new Enzyme();
                EnzymeHierarchy enzymeHierarchy = new EnzymeHierarchy();
                EcClass ecClass = new EcClass();
                ecClass.setEc(ec.getEcNumber());
                enzymeHierarchy.getEcclass().add(ecClass);
                enzyme.getEchierarchies().add(enzymeHierarchy);

                model.getEc().add(ec.getEcNumber());
                //model.setEnzyme(enzyme);
            });
            model.setEnzyme(enzyme);
        }
        return model;
    }

    public EnzymeModel getEnzyme(String uniprotAccession) {

        EnzymeModel model = getEnzymeModel(uniprotAccession);
        try {
            intenzAdapter.getEnzymeDetails(model);
        } catch (MultiThreadingException ex) {
            LOGGER.error("Error getting enzyme details from Intenz webservice", ex);
        }
        List<String> prov = new LinkedList<>();
        prov.add("IntEnz");
        prov.add("UniProt");
        prov.add("IntEnz - (Integrated relational Enzyme database) is a freely available resource focused on enzyme nomenclature.\n");
        prov.add("UniProt - The mission of UniProt is to provide the scientific community with a comprehensive, high-quality and freely accessible resource of protein sequence and functional information");

        model.getEnzyme().setProvenance(prov);

        return model;
    }

    public EnzymeModel getProteinStructure(String uniprotAccession)
            throws EnzymeRetrieverException {
        LOGGER.debug(" -STR- before getEnzymeSummary");

        EnzymeModel model = getEnzymeModel(uniprotAccession);

        addProteinStructures(model);

        return model;
    }

    private void addProteinStructures(EnzymeModel model) {

        List<UniprotXref> pdbcodes = super.getService().findPDBcodesByAccession(model.getAccession());

        for (UniprotXref pdb : pdbcodes) {

            String pdbId = pdb.getSourceId().toLowerCase();
            model.getPdbeaccession().add(pdbId);

            ProteinStructure structure = new ProteinStructure();
            structure.setId(pdbId);
            structure.setName(pdb.getSourceName());
            model.getProteinstructure().add(structure);

        }

    }

    public EnzymeModel getDiseases(String uniprotAccession)
            throws EnzymeRetrieverException {

        EnzymeModel model = getEnzymeModel(uniprotAccession);
        addDiseases(model);
        return model;

    }

    /**
     * Adds any related diseases to the enzyme model.
     *
     * @param enzymeModel the model without disease info.
     * @since 1.1.0
     */
    protected void addDiseases(EnzymeModel enzymeModel) {

        List<Disease> diseases = super.getService().findDiseasesByAccession(enzymeModel.getAccession());

        enzymeModel.setDisease(diseases);

    }

    public EnzymeModel getLiterature(String uniprotAccession) throws EnzymeRetrieverException {

        //UniprotEntry uniprotEntry = super.getService().findByAccession(uniprotAccession);
        EnzymeModel model = getEnzymeModel(uniprotAccession);

        // model.setPdbeaccession(uniprotEntry.getPdbeaccession());
       

        List<LabelledCitation> citations = literatureService.getCitations(uniprotAccession);
//        getLiteratureAdapter().getCitations(
//                uniprotAccession, model.getPdbeaccession());

        if (citations != null) {

            model.setLiterature(new ArrayList<>(citations));

        }

        return model;
    }

    /**
     * Retrieves the whole enzyme model for comparisons.
     *
     * @param acc the UniProt accession of the enzyme.
     * @return a complete model.
     * @throws EnzymeRetrieverException in case of problem retrieving the model
     * from UniProt, or small molecules from ChEBI.
     * @since 1.1.0
     */
    public EnzymeModel getWholeModel(String acc)
            throws EnzymeRetrieverException {
        // This model includes summary, reactions and pathways:
        EnzymeModel model = getEnzymeModel(acc);
        // Add the missing bits:
        addReactionsPathwaysWholeModel(model);
        addProteinStructures(model);
        addMolecules(model);
        addDiseases(model);
        return model;
    }

    public EnzymeModel getMolecules(String uniprotAccession)
            throws EnzymeRetrieverException {

        EnzymeModel model = getEnzymeModel(uniprotAccession);

        addMolecules(model);
        return model;
    }

    /**
     * Adds any available data about small molecules to the model.
     *
     * @param model the model
     * @throws EnzymeRetrieverException in case of problem retrieving detailed
     * info about small molecules from ChEBI.
     * @since 1.1.0
     */
    protected void addMolecules(EnzymeModel model)
            throws EnzymeRetrieverException {

        try {

            List<EnzymePortalCompound> compounds = super.getService().findCompoundsByAccession(model.getAccession());

            CountableMolecules activators = null, inhibitors = null,
                    cofactors = null, drugs = null, bioactive = null;
            if (compounds != null) {
                for (Compound compound : compounds) {
                    // Classify compounds from the DB:
                    switch (compound.getRole()) {
                        case ACTIVATOR:
                            activators = addMoleculeToGroup(activators, compound);
                            break;
                        case INHIBITOR:
                            inhibitors = addMoleculeToGroup(inhibitors, compound);
                            break;
                        case COFACTOR:
                            cofactors = addMoleculeToGroup(cofactors, compound);
                            break;
                        case DRUG:
                            drugs = addMoleculeToGroup(drugs, compound);
                            break;
                        case BIOACTIVE:
                            bioactive = addMoleculeToGroup(bioactive, compound);
                            break;
                    }
                }
            }
            model.setMolecule(new ChemicalEntity()
                    .withActivators(activators)
                    .withInhibitors(inhibitors)
                    .withCofactors(cofactors)
                    .withDrugs(drugs)
                    .withBioactiveLigands(bioactive));

            LOGGER.debug("MOLECULES before getting complete entries from ChEBI");
            getChebiAdapter().getMoleculeCompleteEntries(model);
            LOGGER.debug("MOLECULES before provenance");
            List<String> prov = new LinkedList<>();
            prov.add("ChEBI");
            prov.add("ChEMBL");
            // prov.add("RELEASED DATE = " + new Date());
            prov.add("ChEBI - (Chemical Entities of Biological Interest) is a freely available dictionary of molecular entities focused on ‘small’ chemical compounds.");
            prov.add("ChEMBL is a database of bioactive drug-like small"
                    + " molecules, it contains 2-D structures, calculated"
                    + " properties (e.g. logP, Molecular Weight, Lipinski"
                    + " Parameters, etc.) and abstracted bioactivities (e.g."
                    + " binding constants, pharmacology and ADMET data).");
            if (model.getMolecule() != null) {
                model.getMolecule().setProvenance(prov);
            }
        } catch (ChebiFetchDataException ex) {
            throw new EnzymeRetrieverException(
                    "Failed to get small molecule details from Chebi", ex);
        }

    }

    /**
     * Adds a compound to the group (inhibitors, cofactors, etc).<br/>
     * If the group is <code>null</code> it will be created and initialized.
     * <br>
     * It will be modified by adding the passed compound and increasing the
     * total count.
     *
     * @param group a molecules group.
     * @param comp a compound.
     * @return the modified (possibly newly created) group of molecules.
     */
    private CountableMolecules addMoleculeToGroup(CountableMolecules group,
            Compound comp) {
        if (group == null) {
            group = new CountableMolecules();
            group.setMolecule(new ArrayList<>());
            group.setTotalFound(0);
        }
        Molecule molecule = new Molecule();
        molecule.setName(comp.getName());
        molecule.setId(comp.getId());
        group.getMolecule().add(molecule);
        group.setTotalFound(group.getTotalFound() + 1);
        return group;
    }

    public EnzymeModel getReactionsPathways(String uniprotAccession)
            throws EnzymeRetrieverException {

        EnzymeModel model = getEnzymeModel(uniprotAccession);
        List<String> catalyticActivities = super.getService().findCatalyticActivitiesByAccession(model.getAccession());
        model.setCatalyticActivities(catalyticActivities);
        addReactionsPathways(model);
        return model;
    }

    protected void addReactionsPathways(EnzymeModel model)
            throws EnzymeRetrieverException {

        //Get pathways from uniprot --> maybe not for now
        //Get pathways from Biomart (from Reactome reaction retrieved from Rhea)
        //Choose 2 top pathways to extract from Reactome Website
        // View pathway in reactome should be associated with the reaction.        
        //EnzymeModel enzymeModel = (EnzymeModel)this.uniprotAdapter.getReactionPathwaySummary(uniprotAccession);
        LOGGER.debug(" -RP- before uniprotAdapter.getEnzymeSummary");

        Set<ReactionPathway> rpList = new HashSet<>();

        //EnzymeModel model = getEnzymeModel(uniprotAccession);
        ReactionPathway reactionPathway = new ReactionPathway();

        List<EnzymeReaction> reactions = super.getService().findReactionsByAccession(model.getAccession());

        List<Pathway> pathways = super.getService().findPathwaysByAccession(model.getAccession());
        model.setPathways(pathways);
        reactionPathway.setPathways(pathways);

        if (reactions != null && !reactions.isEmpty()) {
            reactionPathway.setReactions(reactions);
        }

        if (!reactionPathway.getReactions().isEmpty()) {
            rpList.add(reactionPathway);

        }

        model.setReactionpathway(rpList.stream().distinct().collect(Collectors.toList()));

        // The model comes with any available Reactome pathway IDs
        // in one ReactionPathway object, no more.
        // Now we get more ReactionPathways (one per Rhea reaction):
        LOGGER.debug(" -RP- before queryRheaWsForReactions");
        queryRheaWsForReactions(model);

    }

    protected EnzymeModel addReactionsPathwaysWholeModel(EnzymeModel model)
            throws EnzymeRetrieverException {

        ReactionPathway reactionPathway = new ReactionPathway();

        List<EnzymeReaction> reactions = super.getService().findReactionsByAccession(model.getAccession());

        List<Pathway> pathways = super.getService().findPathwaysByAccession(model.getAccession());

        model.setPathways(pathways);
        reactionPathway.setPathways(pathways);

        if (reactions != null && !reactions.isEmpty()) {
            reactionPathway.setReactions(reactions);
        }

        model.getReactionpathway().add(reactionPathway);

        if (model.getReactionpathway().isEmpty()) {

            LOGGER.warn("Searching Rhea for reaction for accession " + model.getAccession());
            // The model comes with any available Reactome pathway IDs
            // in one ReactionPathway object, no more.
            // Now we get more ReactionPathways (one per Rhea reaction):
            LOGGER.debug(" -RP- before queryRheaWsForReactions");

            List<Reaction> rheaReactions = new ArrayList<>();
            try {
                rheaReactions = rheaAdapter.getRheasInCmlreact(model
                        .getUniprotaccessions().get(0));

            } catch (RheaFetchDataException ex) {
                //throw new EnzymeRetrieverException("Query data from Rhea failed! ", ex);
                LOGGER.error("Query data from Rhea failed! ", ex);
            }

            for (Reaction reaction : rheaReactions) {

                ReactionPathway rPathway = rheaAdapter.getReactionPathway(reaction);
                rPathway.setPathways(pathways);
                model.getReactionpathway().add(rPathway);

            }
        }
        model.getReactionpathway().stream().distinct().collect(Collectors.toList());

        return model;
    }

    /**
     * Searches Rhea for the primary UniProt accession in the model and adds the
     * corresponding reactions if found. <br><b>WARNING:</b> the added reactions
     * have links only to Reactome and MACiE. The links are strings containing a
     * complete URL.
     *
     * @param enzymeModel
     * @return the same model updated with ReactionPathway objects, one per
     * reaction found.
     * @throws EnzymeRetrieverException
     */
    private EnzymeModel queryRheaWsForReactions(EnzymeModel enzymeModel)
            throws EnzymeRetrieverException {
        List<Reaction> reactions = new ArrayList<>();

        try {
            reactions = rheaAdapter.getRheasInCmlreact(enzymeModel
                    .getUniprotaccessions().get(0));

        } catch (RheaFetchDataException ex) {
            throw new EnzymeRetrieverException("Query data from Rhea failed! ", ex);
        }

        for (Reaction reaction : reactions) {

// XXX This adapted reaction will have links only to Reactome and MACiE!:
            ReactionPathway reactionPathway = rheaAdapter.getReactionPathway(reaction);
            //reactionPathways.add(reactionPathway);
            enzymeModel.getReactionpathway().add(reactionPathway);

        }
        //enzymeModel.getReactionpathway().addAll(reactionPathways);
        enzymeModel.getReactionpathway().stream().distinct().collect(Collectors.toList());
        return enzymeModel;

    }

}
