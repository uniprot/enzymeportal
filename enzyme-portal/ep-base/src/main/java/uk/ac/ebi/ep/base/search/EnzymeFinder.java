/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.base.search;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ebi.biobabel.blast.Hit;
import uk.ac.ebi.biobabel.blast.Hsp;
import uk.ac.ebi.biobabel.blast.NcbiBlastClient;
import uk.ac.ebi.biobabel.blast.NcbiBlastClientException;
import uk.ac.ebi.biobabel.lucene.LuceneParser;
import uk.ac.ebi.ep.data.common.CommonSpecies;
import uk.ac.ebi.ep.data.domain.EnzymePortalSummary;
import uk.ac.ebi.ep.data.domain.RelatedProteins;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.enzyme.model.Enzyme;
import uk.ac.ebi.ep.data.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.data.enzyme.model.Sequence;
import uk.ac.ebi.ep.data.exceptions.EnzymeFinderException;
import uk.ac.ebi.ep.data.exceptions.MultiThreadingException;
import uk.ac.ebi.ep.data.search.model.Compound;
import uk.ac.ebi.ep.data.search.model.Disease;
import uk.ac.ebi.ep.data.search.model.EnzymeAccession;
import uk.ac.ebi.ep.data.search.model.EnzymeSummary;
import uk.ac.ebi.ep.data.search.model.SearchFilters;
import uk.ac.ebi.ep.data.search.model.SearchParams;
import uk.ac.ebi.ep.data.search.model.SearchResults;
import uk.ac.ebi.ep.data.search.model.Species;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
import uk.ac.ebi.ep.ebeye.EbeyeSearchResult;
import uk.ac.ebi.ep.ebeye.EbeyeService;
import uk.ac.ebi.ep.ebeye.UniProtDomain;
import uk.ac.ebi.ep.enzymeservices.intenz.IntenzAdapter;

/**
 *
 * @author joseph
 */
public class EnzymeFinder {

    private final Logger LOGGER = Logger.getLogger(EnzymeFinder.class);
    protected SearchParams searchParams;
    protected SearchResults enzymeSearchResults;
    List<String> uniprotAccessions;
    List<String> uniprotNameprefixes;
    boolean newSearch;
    Set<String> uniprotAccessionSet;
    Set<String> uniprotNameprefixSet;
    List<String> speciesFilter;
    List<String> compoundFilter;
    List<EnzymeSummary> enzymeSummaryList;

    private final EnzymePortalService service;

    //@Autowired
    protected IntenzAdapter intenzAdapter;
    @Autowired
    private final EbeyeService ebeyeService;

    //building summary -based on uniprot adapter
    public static final String SEQUENCE_URL_BASE = "http://www.uniprot.org/uniprot/";
    public static final String SEQUENCE_URL_SUFFIX = ".html#section_seq";

    Set<Species> uniqueSpecies = new TreeSet<>();
    List<Disease> diseaseFilters = new LinkedList<>();
    List<Compound> compoundFilters = new ArrayList<>();
    Set<Compound> uniquecompounds = new HashSet<>();
    Set<Disease> uniqueDiseases = new HashSet<>();
    private NcbiBlastClient blastClient;

    public EnzymeFinder(EnzymePortalService service, EbeyeService eService) {
        this.service = service;
        this.ebeyeService = eService;

        enzymeSearchResults = new SearchResults();

        uniprotAccessions = new ArrayList<>();
        uniprotAccessionSet = new LinkedHashSet<>();
        enzymeSummaryList = new ArrayList<>();
        intenzAdapter = new IntenzAdapter();

        uniprotNameprefixes = new ArrayList<>();
        uniprotNameprefixSet = new LinkedHashSet<>();
    }

    public EnzymePortalService getService() {
        return service;
    }

    /**
     * Escapes the keywords, validates the filters and sets the global variables
     * to be used in other methods.
     *
     * @param searchParams
     */
    private void processInputs(SearchParams searchParams) {
        this.searchParams = searchParams;
        speciesFilter = searchParams.getSpecies();
        LuceneParser luceneParser = new LuceneParser();
        String keyword = luceneParser.escapeLuceneSpecialChars(this.searchParams.getText());
        String cleanKeyword = HtmlUtility.cleanText(keyword);

        this.searchParams.setText(cleanKeyword);
        String previousText = searchParams.getPrevioustext();
        String currentText = searchParams.getText();
        compoundFilter = searchParams.getCompounds();


        /*
         * There are 2 cases to treat the search as new search: case 1 - the new
         * text is different from the previous text case 2 - all filters are
         * empty
         */
        if (!previousText.equalsIgnoreCase(currentText)
                || (compoundFilter.isEmpty() && speciesFilter.isEmpty())) {
            newSearch = true;
            searchParams.getSpecies().clear();
            searchParams.getCompounds().clear();
        }
    }

    private void queryEbeyeRest() {

    }

    private EbeyeSearchResult getEbeyeSearchResult() {

        // RestTemplate restTemplate = new RestTemplate();
        String query = searchParams.getText();

        EbeyeSearchResult searchResult = ebeyeService.query(query);

        return searchResult;
    }

    private void getResultsFromUniProt() {
        EbeyeSearchResult searchResult = getEbeyeSearchResult();

   

        List<UniProtDomain> results = searchResult.getUniProtDomains().stream().distinct().collect(Collectors.toList());


        results.stream().distinct().forEach((result) -> {
   
            uniprotNameprefixes.add(result.getUniport_name());
   
            uniprotAccessions.add(result.getUniprot_accession());

        });

    }

    /**
     * Queries EB-Eye for UniProt IDs corresponding to enzymes, and adds them to
     * the uniprotEnzymeIds field.
     *
     * @throws EnzymeFinderException
     */
    private void queryEbeyeForUniprotIds() {
        getResultsFromUniProt();
    }

    /**
     * Retrieves the protein recommended name as well as any synonyms.
     *
     * @param namesColumn the column returned by the web service
     * @return a list of names, the first one of them being the recommended one.
     */
    protected List<String> parseNameSynonyms(String namesColumn) {
        List<String> nameSynonyms = new ArrayList<>();
        if (namesColumn != null) {
            final int sepIndex = namesColumn.indexOf(" (");

            //System.out.println("syn index "+ sepIndex);
            if (sepIndex == -1) {
                // no synonyms, just recommended name:

                nameSynonyms.add(namesColumn);
            } else {
                // Recommended name:
                nameSynonyms.add(namesColumn.substring(0, sepIndex));
                // take out starting and ending parentheses
                String[] synonyms = namesColumn.substring(sepIndex + 2, namesColumn.length() - 1).split("\\) \\(");
                nameSynonyms.addAll(Arrays.asList(synonyms));
            }
            return nameSynonyms.stream().distinct().collect(Collectors.toList());
        }
        return nameSynonyms;
    }

    /**
     * Builds an EnzymeModel and populates it with basic information (enough for
     * an EnzymeSummary).
     *
     * @param accession
     * @param id
     * @param nameSynonymsString
     * @param ecsString
     * @param species
     * @param seqLength
     * @param functionString
     * @param pdbAccessions
     * @param relSpecies
     * @return
     */
    private EnzymeModel buildSummary(String accession, String id,
            String nameSynonymsString, String ecsString, Species species,
            List<Disease> diseases, String seqLength, String functionString,
            List<String> pdbAccessions,
            List<EnzymeAccession> relSpecies) {
        EnzymeModel summary = new EnzymeModel();
        summary.getUniprotaccessions().add(accession);
        summary.setUniprotid(id);
        final List<String> nameSynonyms = parseNameSynonyms(nameSynonymsString);
        summary.setName(nameSynonyms.get(0));
        if (nameSynonyms.size() > 1) {
            summary.getSynonym().addAll(nameSynonyms.subList(0, nameSynonyms.size() - 1));
        }
        if (ecsString != null && ecsString.length() > 0) {
            summary.getEc().addAll(Arrays.asList(ecsString.split("; ")));
        }
        summary.setSpecies(species);
        // summary.setRelatedspecies(relSpecies);
        // Optional fields:
        if (diseases != null) {
            summary.setDisease(diseases);
        }
        if (seqLength != null) {
            Sequence seq = new Sequence();
            try {
                seq.setLength(Integer.valueOf(seqLength));
            } catch (NumberFormatException e) {
                LOGGER.error(seqLength + ": wrong length for " + accession);
            }
            // TODO seq.setWeight(value);
            seq.setSequenceurl(SEQUENCE_URL_BASE + accession
                    + SEQUENCE_URL_SUFFIX);
            Enzyme enzyme = new Enzyme();
            enzyme.setSequence(seq);
            summary.setEnzyme(enzyme);
        }
        if (functionString != null && functionString.length() > 0) {
            summary.setFunction(functionString);
        }
        if (pdbAccessions != null) {
            summary.setPdbeaccession(pdbAccessions);
        }
//        if (rpString != null) {
//            summary.getReactionpathway().add(parseReactionPathways(rpString));
//        }
//        if (drugsString != null || regulString != null) {
//            summary.setMolecule(parseChemicalEntity(drugsString, regulString));
//        }
        return summary;
    }

    private List<EnzymeAccession> getRelatedSPecies(EnzymePortalSummary enzymePortalSummary) {
        String defaultSpecies = CommonSpecies.Human.getScientificName();

        List<EnzymeAccession> relatedPecies = new LinkedList<>();

        String nameprefix = enzymePortalSummary.getUniprotAccession().getName().substring(0, enzymePortalSummary.getUniprotAccession().getName().indexOf("_"));
        List<UniprotEntry> enzyme = service.findEnzymeByNamePrefixAndProteinName(nameprefix, enzymePortalSummary.getUniprotAccession().getProteinName());

     
        enzyme.stream().map((entry) -> {
            EnzymeAccession ea = new EnzymeAccession();
             ea.setPdbeaccession(getPdbCodes(entry));

            ea.getUniprotaccessions().add(entry.getAccession());
            ea.setSpecies(entry);
            ea.setCompounds(entry.getEnzymePortalCompoundSet().stream().distinct().collect(Collectors.toList()));
            ea.setDiseases(entry.getEnzymePortalDiseaseSet().stream().distinct().collect(Collectors.toList()));
            diseaseFilters.addAll(entry.getEnzymePortalDiseaseSet().stream().distinct().collect(Collectors.toList()));
            compoundFilters.addAll(entry.getEnzymePortalCompoundSet().stream().distinct().collect(Collectors.toList()));
            if (entry.getScientificname() != null && entry.getScientificname().equalsIgnoreCase(defaultSpecies)) {
                relatedPecies.add(0, ea);
            } else if (entry.getScientificname() != null && !entry.getScientificname().equalsIgnoreCase(defaultSpecies)) {
                relatedPecies.add(ea);
            }
            return entry;
        }).forEach((entry) -> {
            uniqueSpecies.add(entry);
        });

        return relatedPecies;

    }

    private List<EnzymeAccession> getRelatedSPecies(List<String> accessions) {
        String defaultSpecies = CommonSpecies.Human.getScientificName();

        List<EnzymeAccession> relatedPecies = new LinkedList<>();

        //String nameprefix = enzymePortalSummary.getUniprotAccession().getName().substring(0, enzymePortalSummary.getUniprotAccession().getName().indexOf("_"));
        List<UniprotEntry> enzyme = service.findEnzymesByAccessions(accessions);
        //findEnzymeByNamePrefixAndProteinName(nameprefix, enzymePortalSummary.getUniprotAccession().getProteinName());

        enzyme.stream().map((entry) -> {
            EnzymeAccession ea = new EnzymeAccession();
            // ea.setPdbeaccession(getPdbCodes(entry));

            ea.getUniprotaccessions().add(entry.getAccession());
            ea.setSpecies(entry);
            ea.setCompounds(entry.getEnzymePortalCompoundSet().stream().distinct().collect(Collectors.toList()));
            ea.setDiseases(entry.getEnzymePortalDiseaseSet().stream().distinct().collect(Collectors.toList()));
            diseaseFilters.addAll(entry.getEnzymePortalDiseaseSet().stream().distinct().collect(Collectors.toList()));
            compoundFilters.addAll(entry.getEnzymePortalCompoundSet().stream().distinct().collect(Collectors.toList()));
            if (entry.getScientificname() != null && entry.getScientificname().equalsIgnoreCase(defaultSpecies)) {
                relatedPecies.add(0, ea);
            } else if (entry.getScientificname() != null && !entry.getScientificname().equalsIgnoreCase(defaultSpecies)) {
                relatedPecies.add(ea);
            }
            return entry;
        }).forEach((entry) -> {
            uniqueSpecies.add(entry);
        });

        return relatedPecies;

    }

    private List<RelatedProteins> getRelatedProteins(EnzymePortalSummary enzymePortalSummary) {
        String defaultSpecies = CommonSpecies.Human.getScientificName();

        List<RelatedProteins> relatedPecies = new LinkedList<>();

        String nameprefix = enzymePortalSummary.getUniprotAccession().getName().substring(0, enzymePortalSummary.getUniprotAccession().getName().indexOf("_"));
         //List<UniprotEntry> enzyme = service.findEnzymeByNamePrefixAndProteinName(nameprefix, enzymePortalSummary.getUniprotAccession().getProteinName());


        List<RelatedProteins> relatedProteins = service.findRelatedProteinsByNamePrefix(nameprefix).stream().distinct().collect(Collectors.toList());

        for (RelatedProteins entry : relatedProteins) {

            if (entry.getUniprotAccession().getScientificname() != null && entry.getUniprotAccession().getScientificname().equalsIgnoreCase(defaultSpecies)) {
                relatedPecies.add(0, entry);
            } else if (entry.getUniprotAccession().getScientificname() != null && !entry.getUniprotAccession().getScientificname().equalsIgnoreCase(defaultSpecies)) {
                relatedPecies.add(entry);
            }

            diseaseFilters.addAll(entry.getUniprotAccession().getEnzymePortalDiseaseSet().stream().distinct().collect(Collectors.toList()));
            compoundFilters.addAll(entry.getUniprotAccession().getEnzymePortalCompoundSet().stream().distinct().collect(Collectors.toList()));
            uniqueSpecies.add(entry.getUniprotAccession());
        }

//        enzyme.stream().map((entry) -> {
//            EnzymeAccession ea = new EnzymeAccession();
//            // ea.setPdbeaccession(getPdbCodes(entry));
//            System.out.println("RELATED PROTEINS " + entry.getRelatedProteinsSet().size());
//            ea.getUniprotaccessions().add(entry.getAccession());
//            ea.setSpecies(entry);
//            ea.setCompounds(entry.getEnzymePortalCompoundSet().stream().distinct().collect(Collectors.toList()));
//            ea.setDiseases(entry.getEnzymePortalDiseaseSet().stream().distinct().collect(Collectors.toList()));
//            diseaseFilters.addAll(entry.getUniprotAccession().getEnzymePortalDiseaseSet().stream().distinct().collect(Collectors.toList()));
//            compoundFilters.addAll(entry.getUniprotAccession().getEnzymePortalCompoundSet().stream().distinct().collect(Collectors.toList()));
//            
        //if (entry.getScientificname() != null && entry.getScientificname().equalsIgnoreCase(defaultSpecies)) {
//                relatedPecies.add(0, ea);
//            } else if (entry.getScientificname() != null && !entry.getScientificname().equalsIgnoreCase(defaultSpecies)) {
//                relatedPecies.add(ea);
//            }
//            return entry;
//        }).forEach((entry) -> {
//            uniqueSpecies.add(entry.getUniprotAccession());
//        });
        return relatedPecies.stream().distinct().collect(Collectors.toList());

    }

    private Species getSpecies(UniprotEntry entry) {
        Species specie = new Species();
        specie.setCommonname(entry.getCommonName());
        specie.setScientificname(entry.getScientificName());
        specie.setSelected(false);
        return specie;
    }

    private List<String> getPdbCodes(UniprotEntry e) {
        List<String> pdbcodes = new ArrayList<>();
        e.getUniprotXrefSet().stream().distinct().filter((xref) -> (xref.getSource().equalsIgnoreCase("PDB"))).forEach((xref) -> {
            pdbcodes.add(xref.getSourceId());
        });
        return pdbcodes;
    }

    private String getFunctionFromSummary(EnzymePortalSummary enzymePortalSummary) {
        String function = null;
        if (enzymePortalSummary.getCommentType().equalsIgnoreCase("FUNCTION")) {

            function = enzymePortalSummary.getCommentText();
        }

        return function;
    }

    //private static final Comparator<String> NAME_COMPARATOR = new ChemicalNameComparator();
//    static final Comparator<uk.ac.ebi.ep.search.model.Disease> SORT_DISEASES = new Comparator<uk.ac.ebi.ep.search.model.Disease>() {
//        @Override
//        public int compare(EnzymePortalSummary e1, EnzymePortalSummary e2) {
//
//            if (d1.getName() == null && d2.getName() == null) {
//
//                return NAME_COMPARATOR.compare(d1.getName(), d2.getName());
//            }
//            int compare = NAME_COMPARATOR.compare(d1.getName(), d2.getName());
//
//            return ((compare == 0) ? NAME_COMPARATOR.compare(d1.getName(), d2.getName()) : compare);
//
//        }
//    };
    Set<EnzymePortalSummary> UNIQUE = new TreeSet<>((EnzymePortalSummary e1, EnzymePortalSummary e2) -> e1.getUniprotAccession().getAccession().compareToIgnoreCase(e2.getUniprotAccession().getAccession()));

    private List<EnzymeSummary> getEnzymesFromUniprotAPI(
            List<String> accessions, List<String> nameprefixes) {

        Set<EnzymeSummary> enzymeList = new HashSet<>();

        List<EnzymePortalSummary> enzymes = service.findEnzymeSumariesByAccessions(accessions).stream().distinct().collect(Collectors.toList());
     
        LocalDateTime startTime = LocalDateTime.now();
        for (EnzymePortalSummary summary : enzymes) {
            EnzymeSummary es = new EnzymeSummary();



            es.setName(summary.getUniprotAccession().getProteinName());
             //es.setRelatedspecies(getRelatedProteins(summary));
            es.setRelatedspecies(getRelatedSPecies(summary));
            es.setDiseases(summary.getUniprotAccession().getEnzymePortalDiseaseSet().stream().distinct().collect(Collectors.toList()));
            es.setCompounds(summary.getUniprotAccession().getEnzymePortalCompoundSet().stream().distinct().collect(Collectors.toList()));
            es.setAccession(summary.getUniprotAccession().getAccession());
            es.setName(summary.getUniprotAccession().getProteinName());

            es.setPdbeaccession(getPdbCodes(summary.getUniprotAccession()));

            es.setUniprotid(summary.getUniprotAccession().getName());

            es.setSpecies(summary.getUniprotAccession());
            if (summary.getCommentType().equalsIgnoreCase("FUNCTION")) {
                es.setFunction(summary.getCommentText());
            }
            if (summary.getCommentType().equalsIgnoreCase("EC_NUMBER")) {

                es.getEc().add(summary.getCommentText());

            }

            List<String> synonyms = new LinkedList<>();

            String namesColumn = summary.getUniprotAccession().getSynonymNames();

            if (namesColumn != null && namesColumn.contains(";")) {
                String[] syn = namesColumn.split(";");
                for (String x : syn) {

                    synonyms.addAll(parseNameSynonyms(x));
                }
            }

            es.setSynonym(synonyms.stream().distinct().collect(Collectors.toList()));

            enzymeList.add(es);

        }

        return enzymeList.stream().distinct().collect(Collectors.toList());

    }

//    private List<EnzymeSummary> getEnzymesFromUniprotAPIXXX(
//            List<String> prefixes, List<String> paramList) {
//
//        System.out.println("prefix from EBI " + prefixes.size());
//        Set<EnzymeSummary> enzymeList = new HashSet<>();
//        Set<EnzymePortalSummary> uniqueEnzymes = new HashSet<>(UNIQUE);
//        //List<EnzymePortalSummary> enzymes = service.findEnzymeSummariesByNamePrefixes(prefixes).stream().distinct().collect(Collectors.toList());
//        // List<UniprotEntry> entry = service.findEnzymesByNamePrefixes(prefixes);
//
//        // List<UniprotEntry> entry = service.findEnzymesByAccessions(prefixes);
//        //System.out.println("entry not used size "+ entry.size());
//        //getRelatedSPecies(prefixes);
//        //EnzymePortalSummary e = new EnzymeSummary();
//        // List<EnzymePortalSummary> enzymes = service.findEnzymeSumariesByAccessions(prefixes);
//        //enzymeList.addAll(enzymes);
//        //Set<EnzymePortalSummary>  
//        uniqueEnzymes = service.findEnzymeSumariesByAccessions(prefixes).stream().distinct().collect(Collectors.toSet());
//        System.out.println("Unique enzmes " + uniqueEnzymes.size());
//
//        //System.out.println("NUM ENZYMES FOUND "+ enzymes.size());
//        //uniqueEnzymes1.addAll(uniqueEnzymes);
//        //System.out.println("second unique "+ uniqueEnzymes1.size());
//        //enzymeList.addAll(uniqueEnzymes);
//        for (EnzymePortalSummary enzymePortalSummary : uniqueEnzymes) {
//            //enzymePortalSummary.setRelatedspecies(getRelatedSPecies(enzymePortalSummary));
//            // enzymePortalSummary.setPdbeaccession(getPdbCodes(enzymePortalSummary.getUniprotAccession()));
//            //enzymeList.add(enzymePortalSummary);
//
//        }
//
//        //for (EnzymePortalSummary enzymePortalSummary : uniqueEnzymes) {
//        List<Compound> compoundsInEnzyme = new ArrayList<>();
//        List<Disease> diseaseInEnzyme = new ArrayList<>();
//
//        //System.out.println("CHECK "+ enzymePortalSummary);
//        // diseaseInEnzyme.addAll(enzymePortalSummary.getUniprotAccession().getEnzymePortalDiseaseSet());
//        //compoundFilters.addAll(enzymePortalSummary.getUniprotAccession().getEnzymePortalCompoundSet());
//        //compoundsInEnzyme.addAll(enzymePortalSummary.getUniprotAccession().getEnzymePortalCompoundSet());
//        //EnzymeSummary summary = enzymePortalSummary;
//        //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
////            summary.setAccession(enzymePortalSummary.getUniprotAccession().getAccession());
////            summary.setName(enzymePortalSummary.getUniprotAccession().getProteinName());
////            
////            List<String> synonyms = new LinkedList<>();
////            
////            String namesColumn = enzymePortalSummary.getUniprotAccession().getSynonymNames();
////           
////            if (namesColumn != null && namesColumn.contains(";")) {
////                String[] syn = namesColumn.split(";");
////                for (String x : syn) {
////                   
////                    synonyms.addAll(parseNameSynonyms(x));
////                }
////            }
////
//////      
////            summary.setSynonym(synonyms.stream().distinct().collect(Collectors.toList()));
////         summary.setPdbeaccession(getPdbCodes(enzymePortalSummary.getUniprotAccession()));
////         
////           // summary.setCompounds(compoundsInEnzyme);
////            //summary.setDiseases(diseaseInEnzyme);
////            
////            summary.setDiseases(enzymePortalSummary.getUniprotAccession().getEnzymePortalDiseaseSet().stream().distinct().collect(Collectors.toList()));
////            summary.setCompounds(enzymePortalSummary.getUniprotAccession().getEnzymePortalCompoundSet().stream().distinct().collect(Collectors.toList()));
////            
////            summary.setUniprotid(enzymePortalSummary.getUniprotAccession().getName());
////         
////            summary.setSpecies(enzymePortalSummary.getUniprotAccession());
////            if (enzymePortalSummary.getCommentType().equalsIgnoreCase("FUNCTION")) {
////                summary.setFunction(enzymePortalSummary.getCommentText());
////            }
////            if (enzymePortalSummary.getCommentType().equalsIgnoreCase("EC_NUMBER")) {
////             
////                summary.getEc().add(enzymePortalSummary.getCommentText());
////      
////
////            }
//        //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
//        //summary.setRelatedspecies(getRelatedSPecies(enzymePortalSummary));
//        // enzymeList.add(summary);
//        //}
//        return enzymeList.stream().distinct().collect(Collectors.toList());
//        //return enzymes;
//
//    }

    /**
     * Retrieves full enzyme summaries.
     *
     * @param uniprotIdPrefixes a list of UniProt ID prefixes.
     * @return a list of enzyme summaries ready to show in a result list.
     * @throws MultiThreadingException problem getting the original summaries,
     * or creating a processor to add synonyms to them.
     */
    private List<EnzymeSummary> getEnzymeSummaries(
            List<String> accessions, List<String> nameprefixes) {
        // List<EnzymeSummary> summaries = getEnzymesFromUniprotAPI(uniprotIdPrefixes, paramList);
        List<EnzymeSummary> summaries = getEnzymesFromUniprotAPI(accessions, nameprefixes);
       

        return summaries;
    }

    public SearchResults getEnzymes(SearchParams searchParams) {

        processInputs(searchParams);

        /*
         * First time search or when user inserts a new keyword, the filter is
         * reset then the search is performed across all domains without
         * considering the filter.
         */
        if (newSearch) {
            // Search in EBEye for Uniprot ids, the search is filtered by ec:*
            LOGGER.debug("Starting new search");
            // try {
            queryEbeyeForUniprotIds();
            //LOGGER.debug("UniProt IDs from UniProt: " + uniprotEnzymeIds.size());
            //} catch (EnzymeFinderException enzymePortalSummary){
            //LOGGER.error("Unable to search EB-Eye uniprot domain", enzymePortalSummary);
            //}
            // Search in Intenz, Rhea, Reactome, PDBe etc. for Uniprot ids.
            //try {
            //queryEbeyeOtherDomainForIds();
            LOGGER.debug("UniProt IDs from UniProt+ChEBI+others: "
                    + uniprotAccessions.size());
            //} catch (EnzymeFinderException enzymePortalSummary){
            // LOGGER.error("Unable to search EB-Eye other domains", enzymePortalSummary);
            //}
         
            uniprotAccessionSet.addAll(uniprotAccessions.stream().distinct().collect(Collectors.toList()));
            uniprotNameprefixSet.addAll(uniprotNameprefixes.stream().distinct().collect(Collectors.toList()));

        }

        List<String> accessionList
                = new ArrayList<>(uniprotAccessionSet);
        List<String> namePrefixesList
                = new ArrayList<>(uniprotNameprefixSet);

        LOGGER.debug("Getting enzyme summaries...");
        enzymeSummaryList = getEnzymeSummaries(accessionList, namePrefixesList);
        enzymeSearchResults.setSummaryentries(enzymeSummaryList);
        enzymeSearchResults.setTotalfound(enzymeSummaryList.size());
        if (uniprotAccessionSet.size() != enzymeSummaryList.size()) {
            LOGGER.warn((uniprotAccessionSet.size() - enzymeSummaryList.size())
                    + " UniProt ID prefixes have been lost");
        }
        LOGGER.debug("Building filters...");
        buildFilters(enzymeSearchResults);
        LOGGER.debug("Finished search");

        return enzymeSearchResults;
    }

    /**
     * Builds filters - species, compounds, diseases - from a result list.
     *
     * @param searchResults the result list, which will be modified by setting
     * the relevant filters.
     */
    private void buildFilters(SearchResults searchResults) {
        //  String[] commonSpecie = {"Human", "Mouse", "Rat", "Fruit fly", "Worm", "Yeast", "Ecoli"};
        // CommonSpecies [] commonSpecie = {"Homo sapiens","Mus musculus","Rattus norvegicus", "Drosophila melanogaster","Saccharomyces cerevisiae"};
        // List<String> commonSpecieList = Arrays.asList(commonSpecie);
        List<String> commonSpecieList = new ArrayList<>();
        for (CommonSpecies commonSpecies : CommonSpecies.values()) {
            commonSpecieList.add(commonSpecies.getScientificName());
        }

        Map<Integer, Species> priorityMapper = new TreeMap<>();

        AtomicInteger key = new AtomicInteger(50);
        AtomicInteger customKey = new AtomicInteger(6);

        for (Species sp : uniqueSpecies) {

            if (commonSpecieList.contains(sp.getScientificname().split("\\(")[0].trim())) {
                // Human, Mouse, Rat, Fly, Worm, Yeast, Ecoli 
                // "Homo sapiens","Mus musculus","Rattus norvegicus", "Drosophila melanogaster","Worm","Saccharomyces cerevisiae","Ecoli"
                if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.Human.getScientificName())) {
                    priorityMapper.put(1, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.Mouse.getScientificName())) {
                    priorityMapper.put(2, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.Rat.getScientificName())) {
                    priorityMapper.put(3, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.Fruit_fly.getScientificName())) {
                    priorityMapper.put(4, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.Worm.getScientificName())) {
                    priorityMapper.put(5, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.Ecoli.getScientificName())) {
                    priorityMapper.put(6, sp);
                } else if (sp.getScientificname().split("\\(")[0].trim().equalsIgnoreCase(CommonSpecies.Baker_Yeast.getScientificName())) {
                    priorityMapper.put(customKey.getAndIncrement(), sp);

                }
            } else {

                priorityMapper.put(key.getAndIncrement(), sp);

            }
        }

        List<Species> speciesFilters = new LinkedList<>();
        priorityMapper.entrySet().stream().forEach((map) -> {
            speciesFilters.add(map.getValue());
        });

        SearchFilters filters = new SearchFilters();
        filters.setSpecies(speciesFilters);
        filters.setCompounds(compoundFilters.stream().distinct().collect(Collectors.toList()));

        filters.setDiseases(diseaseFilters.stream().distinct().collect(Collectors.toList()));
        searchResults.setSearchfilters(filters);
    }
    
        /**
     * Builds search results from a list of UniProt IDs. It groups orthologs and
     * builds summaries for them.
     *
     * @param uniprotIds The UniProt IDs from a search.
     * @return the search results with summaries.
     * @throws EnzymeFinderException
     * @since
     */
    private SearchResults getSearchResults(List<String> uniprotIds)
            throws EnzymeFinderException {
        SearchResults results = new SearchResults();
 
        List<String> distinctPrefixes = uniprotIds.stream().distinct().collect(Collectors.toList());
        @SuppressWarnings("unchecked")
        List<EnzymeSummary> summaries
                = getEnzymeSummaries(distinctPrefixes, distinctPrefixes);
        results.setSummaryentries(summaries);
        results.setTotalfound(summaries.size());
        if (distinctPrefixes.size() != summaries.size()) {
            LOGGER.warn((distinctPrefixes.size() - summaries.size())
                    + " UniProt ID prefixes have been lost.");
        }
        buildFilters(results);
        return results;
    }

    public SearchResults getEnzymesByCompound(SearchParams searchParams) throws EnzymeFinderException {
        List<String> accessions = this.getService().findEnzymesByCompound(searchParams.getText());
 
        return getSearchResults(accessions);
    }

    private NcbiBlastClient getBlastClient() {
        if (blastClient == null) {
            blastClient = new NcbiBlastClient();
            blastClient.setEmail("enzymeportal-devel@lists.sourceforge.net");
        }
        return blastClient;
    }

    public String blast(String sequence) throws NcbiBlastClientException {
        return getBlastClient().run(sequence);
    }

    public NcbiBlastClient.Status getBlastStatus(String jobId)
            throws NcbiBlastClientException {
        return getBlastClient().getStatus(jobId);
    }

    public SearchResults getBlastResult(String jobId)
            throws NcbiBlastClientException, MultiThreadingException {
        List<Hit> hits = getBlastClient().getResults(jobId);
        Map<String, Hsp> scorings = new HashMap<>();

        for (Hit hit : hits) {

            scorings.put(hit.getUniprotAccession(), hit.getHsps().get(0));
          
        }
     

        List<String> uniprotIdPrefixes = filterBlastResults(hits);

        enzymeSummaryList = getEnzymeSummaries(uniprotIdPrefixes, null);

        for (EnzymeSummary es : enzymeSummaryList) {

            for (EnzymeAccession ea : es.getRelatedspecies()) {

                String hitAcc = ea.getUniprotaccessions().get(0);

                ea.setScoring(scorings.get(hitAcc));

            }
            Collections.sort(es.getRelatedspecies(), (EnzymeAccession o1, EnzymeAccession o2) -> {
                if (o1.getScoring() == null && o2.getScoring() == null) {
                    return 0;
                }
                if (o1.getScoring() == null) {
                    return 1;
                }

                if (o2.getScoring() == null) {
                    return -1;
                }

                return ((Comparable) o1.getScoring())
                        .compareTo(o2.getScoring());
            });

        }

        enzymeSearchResults.setSummaryentries(enzymeSummaryList);
        enzymeSearchResults.setTotalfound(enzymeSummaryList.size());
        LOGGER.debug("Building filters...");
        buildFilters(enzymeSearchResults);
        return enzymeSearchResults;
    }

    /**
     * Filters the hits returned by the Blast client to get only enzymes.
     *
     * @param hits Hits returned by the Blast client.
     * @return a list of unique UniProt ID prefixes (species stripped).
     * @throws MultiThreadingException
     */
    private List<String> filterBlastResults(List<Hit> hits)
            throws MultiThreadingException {
        List<String> accs = new ArrayList<>();
        hits.stream().forEach((hit) -> {
            accs.add(hit.getUniprotAccession());
        });

        return service.filterEnzymesInAccessions(accs);

       
    }

 }
