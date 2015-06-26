/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.base.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import uk.ac.ebi.biobabel.blast.Hit;
import uk.ac.ebi.biobabel.blast.Hsp;
import uk.ac.ebi.biobabel.blast.NcbiBlastClient;
import uk.ac.ebi.biobabel.blast.NcbiBlastClientException;
import uk.ac.ebi.biobabel.lucene.LuceneParser;
import static uk.ac.ebi.ep.data.batch.PartitioningSpliterator.partition;
import uk.ac.ebi.ep.data.common.CommonSpecies;
import uk.ac.ebi.ep.data.domain.EnzymePortalCompound;
import uk.ac.ebi.ep.data.domain.EnzymePortalDisease;
import uk.ac.ebi.ep.data.domain.EnzymePortalPathways;
import uk.ac.ebi.ep.data.domain.EnzymePortalReaction;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.entry.Protein;
import uk.ac.ebi.ep.data.exceptions.EnzymeFinderException;
import uk.ac.ebi.ep.data.exceptions.MultiThreadingException;
import uk.ac.ebi.ep.data.search.model.Compound;
import uk.ac.ebi.ep.data.search.model.Disease;
import uk.ac.ebi.ep.data.search.model.EcNumber;
import uk.ac.ebi.ep.data.search.model.EnzymeAccession;
import uk.ac.ebi.ep.data.search.model.SearchFilters;
import uk.ac.ebi.ep.data.search.model.SearchParams;
import uk.ac.ebi.ep.data.search.model.SearchResults;
import uk.ac.ebi.ep.data.search.model.Species;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
import uk.ac.ebi.ep.ebeye.EbeyeRestService;
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
    List<UniprotEntry> enzymeSummaryList;
    @Deprecated
    List<String> confirmedEnzymes;

    private final EnzymePortalService service;

    //@Autowired
    protected IntenzAdapter intenzAdapter;

    private final EbeyeRestService ebeyeRestService;

    Set<Species> uniqueSpecies = new TreeSet<>();
    List<Disease> diseaseFilters = new LinkedList<>();
    List<Compound> compoundFilters = new ArrayList<>();
    LinkedList<EcNumber> ecNumberFilters = new LinkedList<>();

    Set<Compound> uniquecompounds = new HashSet<>();
    Set<Disease> uniqueDiseases = new HashSet<>();
    private NcbiBlastClient blastClient;
    private final int LIMIT = 7000;
    private final int ACCESSION_LIMIT = 800;

    public EnzymeFinder(EnzymePortalService service, EbeyeRestService ebeyeRestService) {
        this.service = service;
        //this.ebeyeService = eService;
        this.ebeyeRestService = ebeyeRestService;

        enzymeSearchResults = new SearchResults();

        uniprotAccessions = new ArrayList<>();
        uniprotAccessionSet = new LinkedHashSet<>();
        enzymeSummaryList = new ArrayList<>();
        intenzAdapter = new IntenzAdapter();

        uniprotNameprefixes = new TreeSet<>();
        uniprotNameprefixSet = new LinkedHashSet<>();
        confirmedEnzymes = new ArrayList<>();
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

        //String cleanKeyword = HtmlUtility.cleanText(keyword);
        this.searchParams.setText(keyword);
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

//    @Deprecated
//    private EbeyeSearchResult getEbeyeSearchResult() {
//
//        String query = searchParams.getText();
//        if (!StringUtils.isEmpty(query)) {
//            query = query.trim();
//        }
//
//        EbeyeSearchResult searchResult = ebeyeService.query(query);
//
//        return searchResult;
//    }
    private void getResultsFromEpIndex() {

        String query = searchParams.getText();
        if (!StringUtils.isEmpty(query)) {
            query = query.trim();
        }
        List<String> accessions = ebeyeRestService.queryEbeyeForAccessions(query, true, LIMIT);
        // List<String> accessions = ebeyeRestService.queryEbeyeForAccessions(query, true);
        //List<String> accessions = ebeyeRestService.queryEbeyeForAccessions(query);
        LOGGER.warn("Number of Processed Accession for  " + query + " :=:" + accessions.size());

        uniprotAccessions = accessions.stream().distinct().limit(ACCESSION_LIMIT).collect(Collectors.toList());

    }

    /**
     * Queries EB-Eye for UniProt IDs corresponding to enzymes, and adds them to
     * the uniprotEnzymeIds field.
     *
     * @throws EnzymeFinderException
     */
    private void queryEbeyeForUniprotAccessions() {
        getResultsFromEpIndex();
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

    @Deprecated
    private void computeDedicatedFilters(List<String> accessions) {

        Stream<String> existingStream = accessions.stream();
        Stream<List<String>> partitioned = partition(existingStream, 100, 1);

        partitioned.parallel().forEach((chunk) -> {
            List<EnzymePortalCompound> compounds = service.findCompoundsByAccessions(chunk).stream().distinct().collect(Collectors.toList());
            if (!compounds.isEmpty()) {
                compoundFilters.addAll(compounds);
            }
        });

    }

    private void computeFilterFacets(UniprotEntry entry) {

        ecNumberFilters.addAll(entry.getEnzymePortalEcNumbersSet().stream().distinct().collect(Collectors.toList()));
        compoundFilters.addAll(entry.getEnzymePortalCompoundSet().stream().distinct().collect(Collectors.toList()));
        diseaseFilters.addAll(entry.getEnzymePortalDiseaseSet().stream().distinct().collect(Collectors.toList()));
        //uniqueSpecies.add(entry.getSpecies());

//        entry.getRelatedProteinsId().getUniprotEntrySet().stream().map((e) -> {
//            diseaseFilters.addAll(e.getEnzymePortalDiseaseSet().stream().distinct().collect(Collectors.toList()));
//            return e;
//        }).map((e) -> {
//            compoundFilters.addAll(e.getEnzymePortalCompoundSet().stream().distinct().collect(Collectors.toList()));
//            return e;
//        }).map(e -> {
//            ecNumberFilters.addAll(e.getEnzymePortalEcNumbersSet().stream().distinct().sorted().collect(Collectors.toList()));
//            return e;
//        }).forEach((e) -> {
//            uniqueSpecies.add(e.getSpecies());
//        });
        entry.getRelatedProteinsId().getUniprotEntrySet().stream().forEach((e) -> {
            uniqueSpecies.add(e.getSpecies());

        });

    }

    @Deprecated
    private void computeFilterFacets(List<UniprotEntry> entries) {
        for (UniprotEntry entry : entries) {
            entry.getRelatedProteinsId().getUniprotEntrySet().stream().map((e) -> {
                diseaseFilters.addAll(e.getEnzymePortalDiseaseSet().stream().distinct().collect(Collectors.toList()));
                return e;
            }).map((e) -> {
                compoundFilters.addAll(e.getEnzymePortalCompoundSet().stream().distinct().collect(Collectors.toList()));
                return e;
            }).forEach((e) -> {
                uniqueSpecies.add(e.getSpecies());
            });
        }

    }

    private List<UniprotEntry> computeUniqueEnzymes(List<UniprotEntry> enzymes) {
        List<UniprotEntry> enzymeList = new ArrayList<>();
        Set<String> proteinNames = new HashSet<>();
        for (UniprotEntry entry : enzymes) {

            if (!proteinNames.contains(entry.getProteinName())) {

                enzymeList.add(entry);

            }
            proteinNames.add(entry.getProteinName());
            confirmedEnzymes.add(entry.getAccession());
            computeFilterFacets(entry);

        }
        enzymeList.sort(SWISSPROT_FIRST);
        return enzymeList.stream().distinct().collect(Collectors.toList());
    }

    private List<UniprotEntry> computeUniqueEnzymes(List<UniprotEntry> enzymes, String keyword) {
        //LinkedList<UniprotEntry> enzymeList = new LinkedList<>();
        LinkedList<UniprotEntry> theEnzymes = new LinkedList<>();
        Deque<UniprotEntry> enzymeList = new LinkedList<>();
        Set<String> proteinNames = new HashSet<>();
        for (UniprotEntry entry : enzymes) {

            if (!proteinNames.contains(entry.getProteinName())) {

                String enzymeName = HtmlUtility.cleanText(entry.getProteinName()).toLowerCase();
                if (enzymeName.toLowerCase().matches(".*" + keyword.toLowerCase() + ".*") && entry.getEntryType() != 1) {

                    enzymeList.offerFirst(entry);

                } else {

                    enzymeList.offerLast(entry);

                }

            }

            proteinNames.add(entry.getProteinName());
            confirmedEnzymes.add(entry.getAccession());
            computeFilterFacets(entry);

        }
        //enzymeList.sort(SWISSPROT_FIRST);
        for (UniprotEntry enzyme : enzymeList) {
            if (HtmlUtility.cleanText(enzyme.getProteinName()).toLowerCase().equalsIgnoreCase(keyword.toLowerCase()) && enzyme.getEntryType() != 1) {

                LOGGER.info("FOUND A MATCH " + enzyme.getProteinName() + " => " + keyword + " entry type " + enzyme.getEntryType());
                theEnzymes.offerFirst(enzyme);

            } else {
                theEnzymes.offerLast(enzyme);

            }
        }

        return theEnzymes.stream().distinct().collect(Collectors.toList());
    }

    private List<UniprotEntry> getEnzymesByAccessions(List<String> accessions, String keyword) {

        final Set<UniprotEntry> enzymeList = new LinkedHashSet<>();
        //LOGGER.warn("Number of Accession to query for enzymes :=:" + accessions.size());
        if (accessions.size() > 0) {
            Pageable pageable = new PageRequest(0, 500, Sort.Direction.ASC, "function", "entryType");

            long startTime = System.nanoTime();
            Stream<String> existingStream = accessions.stream();
            Stream<List<String>> partitioned = partition(existingStream, 100, 1);


            partitioned.parallel().forEach((chunk) -> {

                // Page<UniprotEntry> page = service.findEnzymesByAccessionsIn(chunk, pageable);
                
                //Page<UniprotEntry> page = service.findEnzymesByAccessions(chunk, pageable);//39 sec 53.610s
                //List<UniprotEntry> enzymes = page.getContent().stream().map(EnzymePortal::new).distinct().map(EnzymePortal::unwrapProtein).filter(Objects::nonNull).collect(Collectors.toList());
               // List<UniprotEntry> enzymes = service.findEnzymesByAccessions(chunk).stream().map(EnzymePortal::new).distinct().map(EnzymePortal::unwrapProtein).filter(Objects::nonNull).collect(Collectors.toList());

             List<UniprotEntry> enzymes = service.findEnzymesByAccessions(chunk); //36 sec 58.683s
                enzymeList.addAll(computeUniqueEnzymes(enzymes, keyword));

                        
            });
            //computeDedicatedFilters(confirmedEnzymes);
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            long elapsedtime = TimeUnit.SECONDS.convert(duration, TimeUnit.NANOSECONDS);
            LOGGER.warn("Time taken to process accessions of size (" + accessions.size() + ") :  (" + elapsedtime + " sec)");

        }

        return enzymeList.stream().collect(Collectors.toList());

    }

    private final Comparator<UniprotEntry> SWISSPROT_FIRST = (UniprotEntry e1, UniprotEntry e2) -> e1.getEntryType().compareTo(e2.getEntryType());

    private static Comparator<UniprotEntry> SWISSPROT_WITH_FUNCTION_FIRST = (UniprotEntry e1, UniprotEntry e2) -> {
        int comparison = e1.getEntryType().compareTo(e2.getEntryType());
        if (comparison == 0 && e1.getFunction() != null && e2.getFunction() != null) {
            comparison = e1.getFunction().compareToIgnoreCase(e2.getFunction());
        }
        return comparison;
    };

    @Deprecated
    private List<UniprotEntry> getEnzymesByNamePrefixes(List<String> nameprefixes, String keyword) {

        List<UniprotEntry> enzymeList = new ArrayList<>();

        if (nameprefixes.size() > 0) {
            Pageable pageable = new PageRequest(0, 50, Sort.Direction.ASC, "function", "entryType");//temp impl
            Page<UniprotEntry> page = service.findEnzymesByNamePrefixes(nameprefixes, pageable);
            //List<UniprotEntry>  enzymes = service.findEnzymesByNamePrefixes(nameprefixes);
            List<UniprotEntry> enzymes = page.getContent();
            enzymeList = computeUniqueEnzymes(enzymes, keyword);

        }

        return enzymeList;

    }

    /**
     * Retrieves full enzyme summaries.
     *
     * @param accessions a list of UniProt Accessions.
     * @return a list of enzyme summaries ready to show in a result list.
     * @throws MultiThreadingException problem getting the original summaries,
     * or creating a processor to add synonyms to them.
     */
    private List<UniprotEntry> getEnzymeSummariesByAccessions(List<String> accessions, String keyword) {

        List<UniprotEntry> summaries = getEnzymesByAccessions(accessions, keyword);

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
            // Search in EBEye for Uniprot accessions
            LOGGER.debug("Starting new search");

            queryEbeyeForUniprotAccessions();

            LOGGER.debug("UniProt Accession from Ebeye Rest Service: "
                    + uniprotAccessions.size());

            uniprotAccessionSet.addAll(uniprotAccessions.stream().distinct().collect(Collectors.toList()));
            uniprotNameprefixSet.addAll(uniprotNameprefixes.stream().distinct().collect(Collectors.toList()));

        }

        List<String> accessionList
                = new ArrayList<>(uniprotAccessionSet);

        String keyword = HtmlUtility.cleanText(this.searchParams.getText());
        keyword = keyword.replaceAll("&quot;", "");

        LOGGER.debug("Getting enzyme summaries...");

        enzymeSummaryList = getEnzymeSummariesByAccessions(accessionList, keyword);

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
        //  String[] commonSpecie = {"HUMAN", "MOUSE", "RAT", "Fruit fly", "WORM", "Yeast", "ECOLI"};
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
                // HUMAN, MOUSE, RAT, Fly, WORM, Yeast, ECOLI 
                // "Homo sapiens","Mus musculus","Rattus norvegicus", "Drosophila melanogaster","WORM","Saccharomyces cerevisiae","ECOLI"
                if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.HUMAN.getScientificName())) {
                    priorityMapper.put(1, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.MOUSE.getScientificName())) {
                    priorityMapper.put(2, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.RAT.getScientificName())) {
                    priorityMapper.put(3, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.FRUIT_FLY.getScientificName())) {
                    priorityMapper.put(4, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.WORM.getScientificName())) {
                    priorityMapper.put(5, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.ECOLI.getScientificName())) {
                    priorityMapper.put(6, sp);
                } else if (sp.getScientificname().split("\\(")[0].trim().equalsIgnoreCase(CommonSpecies.BAKER_YEAST.getScientificName())) {
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
        //ecNumberFilters.sort(SORT_BY_EC);
        filters.setEcNumbers(ecNumberFilters.stream().distinct().sorted().collect(Collectors.toList()));
        searchResults.setSearchfilters(filters);
    }

    public static Comparator<EcNumber> SORT_BY_EC = (EcNumber ec1, EcNumber ec2) -> ec1.getEc().compareTo(ec2.getEc());

    /**
     * Builds search results from a list of UniProt IDs. It groups orthologs and
     * builds summaries for them.
     *
     * @param uniprotIds The UniProt IDs from a search.
     * @return the search results with summaries.
     * @throws EnzymeFinderException
     * @since
     */
    private SearchResults getSearchResults(List<String> uniprotIds, String keyword)
            throws EnzymeFinderException {
        SearchResults results = new SearchResults();

        List<String> distinctPrefixes = uniprotIds.stream().distinct().collect(Collectors.toList());
        @SuppressWarnings("unchecked")
        List<UniprotEntry> summaries
                = getEnzymeSummariesByAccessions(distinctPrefixes, keyword);
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

        return getSearchResults(accessions, searchParams.getText());
    }

    private NcbiBlastClient getBlastClient() {
        //refer to this doc
        //http://www.ebi.ac.uk/Tools/sss/ncbiblast/help/index-protein.html#step3
        if (blastClient == null) {
            blastClient = new NcbiBlastClient();
            blastClient.setEmail("enzymeportal-devel@lists.sourceforge.net");
            //blastClient.setBaseUrl("http://www.ebi.ac.uk/Tools/services/rest/ncbiblast");
            //blastClient.setDatabase("uniprotkb_swissprot");
            //blastClient.setDatabase("uniprotkb");
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

        //enzymeSummaryList = getEnzymeSummariesByAccessions(uniprotAccessionList, "");
        enzymeSummaryList = getEnzymesByAccessions(uniprotAccessionList);

        for (UniprotEntry es : enzymeSummaryList) {

            for (EnzymeAccession ea : es.getRelatedspecies()) {

                String hitAcc = ea.getUniprotaccessions().get(0);

                ea.setScoring(scorings.get(hitAcc));
                es.setScoring(scorings.get(hitAcc));

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

        enzymeSummaryList.sort(SORT_BY_BITSCORE);
        enzymeSearchResults.setSummaryentries(enzymeSummaryList);
        enzymeSearchResults.setTotalfound(enzymeSummaryList.size());
        LOGGER.debug("Building filters...");
        buildFilters(enzymeSearchResults);
        return enzymeSearchResults;
    }

    public static Comparator<UniprotEntry> SORT_BY_BITSCORE = (UniprotEntry o1, UniprotEntry o2) -> {
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
    };

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

        return diseases;
    }

    public SearchResults computeEnzymeSummariesByMeshId(String meshId) {
        SearchResults searchResults = new SearchResults();
        List<UniprotEntry> enzymes = service.findEnzymesByMeshId(meshId);

        List<UniprotEntry> enzymeList = computeUniqueEnzymes(enzymes);
        //computeDedicatedFilters(confirmedEnzymes);
        searchResults.setSummaryentries(enzymeList);
        searchResults.setTotalfound(enzymeList.size());

        LOGGER.debug("Building filters...");
        buildFilters(searchResults);
        LOGGER.debug("Finished search");

        return searchResults;
    }

    public SearchResults computeEnzymeSummariesByPathwayId(String pathwayId) {
        SearchResults searchResults = new SearchResults();
        List<UniprotEntry> enzymes = service.findEnzymesByPathwayId(pathwayId);

        List<UniprotEntry> enzymeList = computeUniqueEnzymes(enzymes);
        //computeDedicatedFilters(confirmedEnzymes);
        searchResults.setSummaryentries(enzymeList);
        searchResults.setTotalfound(enzymeList.size());

        LOGGER.debug("Building filters...");
        buildFilters(searchResults);
        LOGGER.debug("Finished search");

        return searchResults;
    }

    public SearchResults computeEnzymeSummariesByEc(String ec) {
        SearchResults searchResults = new SearchResults();
        List<String> accessions = new ArrayList<>();
        //List<UniprotEntry> enzymes = service.findEnzymesByEc(ec);
        List<Protein> proteins = service.findProteinByEc(ec);
        proteins.stream().forEach((protein) -> {
            accessions.add(protein.getAccession());
        });

        List<UniprotEntry> enzymeList = getEnzymesByAccessions(accessions);
        searchResults.setSummaryentries(enzymeList);
        searchResults.setTotalfound(enzymeList.size());

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

    private List<UniprotEntry> getEnzymesByAccessions(List<String> accessions) {

        final List<UniprotEntry> enzymeList = new LinkedList<>();

        if (accessions.size() > 0) {
            Pageable pageable = new PageRequest(0, 500, Sort.Direction.ASC, "function", "entryType");

            Stream<String> existingStream = accessions.stream();
            Stream<List<String>> partitioned = partition(existingStream, 124, 1);

            partitioned.parallel().forEach((chunk) -> {

                //Page<UniprotEntry> page = service.findEnzymesByAccessions(chunk, pageable);
                //List<UniprotEntry> enzymes = page.getContent();
                List<UniprotEntry> enzymes = service.findEnzymesByAccessions(chunk);

                enzymeList.addAll(computeUniqueEnzymes(enzymes));

            });

            //computeDedicatedFilters(confirmedEnzymes);

        }

        return enzymeList;

    }

}
