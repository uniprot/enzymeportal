/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.base.search;

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
import uk.ac.ebi.ep.data.domain.EnzymePortalDisease;
import uk.ac.ebi.ep.data.domain.EnzymePortalPathways;
import uk.ac.ebi.ep.data.domain.EnzymePortalReaction;
import uk.ac.ebi.ep.data.domain.EnzymePortalSummary;
import uk.ac.ebi.ep.data.domain.RelatedProteins;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
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
    Set<String> uniprotNameprefixes;
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

        uniprotNameprefixes = new TreeSet<>();
        uniprotNameprefixSet = new LinkedHashSet<>();
    }

    public EnzymePortalService getService() {
        return service;
    }

    public SearchParams getSearchParams() {
        return searchParams;
    }

    public void setSearchParams(SearchParams searchParams) {
        this.searchParams = searchParams;
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
        if (searchResult != null) {
            List<UniProtDomain> results = searchResult.getUniProtDomains().stream().distinct().collect(Collectors.toList());

            results.parallelStream().distinct().forEach((result) -> {

                uniprotNameprefixes.add(result.getUniport_name());

                uniprotAccessions.add(result.getUniprot_accession());

            });
        }

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

    //Set<EnzymePortalSummary> UNIQUE = new TreeSet<>((EnzymePortalSummary e1, EnzymePortalSummary e2) -> e1.getUniprotAccession().getAccession().compareToIgnoreCase(e2.getUniprotAccession().getAccession()));
    private List<EnzymeAccession> computeRelatedSpecies(EnzymePortalSummary summary) {
        String defaultSpecies = CommonSpecies.Human.getScientificName();
        List<EnzymeAccession> relatedSpecies = new LinkedList<>();

        RelatedProteins relatedProteins = null;// service.findRelatedProteinsByNamePrefix(summary.getUniprotAccession().getName().substring(0, summary.getUniprotAccession().getName().indexOf("_")));

        if (relatedProteins == null) {

            for (UniprotEntry e : summary.getUniprotAccession().getRelatedProteinsId().getUniprotEntrySet()) {
                //for (UniprotEntry e : relatedProteins.getUniprotEntrySet()) {    

                EnzymeAccession ea = new EnzymeAccession();
                ea.setCompounds(e.getEnzymePortalCompoundSet().stream().distinct().collect(Collectors.toList()));

                ea.setDiseases(e.getEnzymePortalDiseaseSet().stream().distinct().collect(Collectors.toList()));

                ea.setPdbeaccession(e.getPdbeaccession());
                ea.getUniprotaccessions().add(e.getAccession());
                ea.setSpecies(e);

                if (e.getScientificname() != null && e.getScientificname().equalsIgnoreCase(defaultSpecies)) {

                    relatedSpecies.add(0, ea);

                } else if (e.getScientificname() != null && !e.getScientificname().equalsIgnoreCase(defaultSpecies)) {
                    relatedSpecies.add(ea);

                }

                diseaseFilters.addAll(e.getEnzymePortalDiseaseSet().stream().distinct().collect(Collectors.toList()));
                compoundFilters.addAll(e.getEnzymePortalCompoundSet().stream().distinct().collect(Collectors.toList()));
                uniqueSpecies.add(e);

            }
        }
        return relatedSpecies;
    }

    private List<EnzymeSummary> getEnzymesByAccessions(List<String> accessions) {

        Set<EnzymeSummary> enzymeList = new HashSet<>();

        List<EnzymePortalSummary> enzymes = null;
        if (accessions.size() > 0) {
            enzymes = service.findEnzymeSumariesByAccessions(accessions).stream().distinct().collect(Collectors.toList());

            for (EnzymePortalSummary enzyme_summary : enzymes) {
                EnzymeSummary summary = enzyme_summary;

                summary.setRelatedspecies(computeRelatedSpecies(enzyme_summary));

                enzymeList.add(summary);

            }

        }

        return enzymeList.stream().distinct().collect(Collectors.toList());

    }

    private List<EnzymeSummary> getEnzymesByNamePrefixes(List<String> nameprefixes) {

        Set<EnzymeSummary> enzymeList = new HashSet<>();
        List<EnzymePortalSummary> enzymes = null;
        if (nameprefixes.size() > 0) {

            enzymes = service.findEnzymeSummariesByNamePrefixes(nameprefixes).stream().distinct().collect(Collectors.toList());

            for (EnzymePortalSummary enzyme_summary : enzymes) {

                EnzymeSummary summary = enzyme_summary;
                summary.setRelatedspecies(computeRelatedSpecies(enzyme_summary));

                enzymeList.add(summary);

            }

        }

        return enzymeList.stream().distinct().collect(Collectors.toList());

    }

    /**
     * Retrieves full enzyme summaries.
     *
     * @param namePrefixes a list of UniProt ID prefixes.
     * @return a list of enzyme summaries ready to show in a result list.
     * @throws MultiThreadingException problem getting the original summaries,
     * or creating a processor to add synonyms to them.
     */
    private List<EnzymeSummary> getEnzymeSummariesByNamePrefixes(List<String> nameprefixes) {

        List<EnzymeSummary> summaries = getEnzymesByNamePrefixes(nameprefixes);

        return summaries;
    }

    private List<EnzymeSummary> getEnzymeSummariesByAccessions(List<String> accessions) {

        List<EnzymeSummary> summaries = getEnzymesByAccessions(accessions);

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
        enzymeSummaryList = getEnzymeSummariesByNamePrefixes(namePrefixesList);
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
                = getEnzymeSummariesByNamePrefixes(distinctPrefixes);
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

        List<String> uniprotAccessionList = filterBlastResults(hits).stream().distinct().collect(Collectors.toList());

        enzymeSummaryList = getEnzymeSummariesByAccessions(uniprotAccessionList);

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

    /**
     *
     * @return all diseases
     */
    public List<EnzymePortalDisease> findDiseases() {

        List<EnzymePortalDisease> diseases = service.findAllDiseases().stream().distinct().collect(Collectors.toList());
        
         // List<EnzymePortalDisease> diseases = service.findDiseases().stream().distinct().collect(Collectors.toList());

        return diseases;
    }

    public SearchResults computeEnzymeSummariesByAccessions(List<String> accessions) {
List<EnzymeSummary> summaryList = new ArrayList<>();
        SearchResults searchResults = new SearchResults();

         List<EnzymePortalSummary> enzymes = service.findEnzymeSumariesByAccessions(accessions).stream().distinct().collect(Collectors.toList());


        for (EnzymePortalSummary enzyme_summary : enzymes) {
            EnzymeSummary summary = enzyme_summary;

            summary.setRelatedspecies(computeRelatedSpecies(enzyme_summary));

            summaryList.add(summary);

        }

        searchResults.setSummaryentries(summaryList);
        searchResults.setTotalfound(summaryList.size());

        LOGGER.debug("Building filters...");
        buildFilters(searchResults);
        LOGGER.debug("Finished search");

        return searchResults;
    }
    
    
     /**
      * 
      * @return all reactions
      */
    public List<EnzymePortalReaction> findAllReactions() {

        return service.findReactions().stream().distinct().collect(Collectors.toList());
    }

   /**
    * 
    * @return all pathways
    */
    public List<EnzymePortalPathways> findAllPathways() {

        return service.findPathways().stream().distinct().collect(Collectors.toList());
    } 
    
    
    

//    public SearchResults computeEnzymeSummariesByDiseaseId(String diseaseId) {
//
//        SearchResults searchResults = new SearchResults();
//        List<EnzymeSummary> summaryList = new ArrayList<>();
//        
//        
//        List<String> accessions = service.findAccessionsByMeshId(diseaseId);
//    
//        
//         List<EnzymePortalSummary> enzymes = service.findEnzymeSumariesByAccessions(accessions).stream().distinct().collect(Collectors.toList());
//
//
//        for (EnzymePortalSummary enzyme_summary : enzymes) {
//            EnzymeSummary summary = enzyme_summary;
//
//            summary.setRelatedspecies(computeRelatedSpecies(enzyme_summary));
//
//            summaryList.add(summary);
//
//        }
//
//        searchResults.setSummaryentries(summaryList);
//        searchResults.setTotalfound(summaryList.size());
//
//        LOGGER.debug("Building filters...");
//        buildFilters(searchResults);
//        LOGGER.debug("Finished search");
//
//        return searchResults;
//    }
    
//        public SearchResults computeEnzymeSummariesByEc(String ec) {
//
//        SearchResults searchResults = new SearchResults();
//        
//        List<String> accessions = service.findAccessionsByEc(ec);
//            System.out.println("num accession per ec "+ accessions.size());
//
//        List<EnzymeSummary> summaryList = getEnzymeSummariesByAccessions(accessions).stream().distinct().collect(Collectors.toList());
//
//            System.out.println("num summary found "+ summaryList.size());
//        searchResults.setSummaryentries(summaryList);
//        searchResults.setTotalfound(summaryList.size());
//
//        LOGGER.debug("Building filters...");
//        //SearchFilters filters = buildFilters(searchResults);
//        //searchResults.setSearchfilters(filters);
//        buildFilters(searchResults);
//        LOGGER.debug("Finished search");
//
//        return searchResults;
//    }

}
