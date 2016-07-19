package uk.ac.ebi.ep.base.search;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import uk.ac.ebi.biobabel.lucene.LuceneParser;
import static uk.ac.ebi.ep.data.batch.PartitioningSpliterator.partition;
import uk.ac.ebi.ep.data.common.CommonSpecies;
import uk.ac.ebi.ep.data.domain.EnzymePortalDisease;
import uk.ac.ebi.ep.data.domain.EnzymePortalEcNumbers;
import uk.ac.ebi.ep.data.domain.EnzymePortalPathways;
import uk.ac.ebi.ep.data.domain.EnzymePortalReaction;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.entry.EnzymePortal;
import uk.ac.ebi.ep.data.entry.Family;
import uk.ac.ebi.ep.data.exceptions.EnzymeFinderException;
import uk.ac.ebi.ep.data.exceptions.MultiThreadingException;
import uk.ac.ebi.ep.data.search.model.Compound;
import uk.ac.ebi.ep.data.search.model.Disease;
import uk.ac.ebi.ep.data.search.model.EcNumber;
import uk.ac.ebi.ep.data.search.model.SearchFilters;
import uk.ac.ebi.ep.data.search.model.SearchParams;
import uk.ac.ebi.ep.data.search.model.SearchResults;
import uk.ac.ebi.ep.data.search.model.Species;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
import uk.ac.ebi.ep.ebeye.EbeyeRestService;

/**
 *
 * @author joseph
 */
public class EnzymeFinder extends EnzymeBase {

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

    Set<Species> uniqueSpecies;
    List<Disease> diseaseFilters;
    List<Compound> compoundFilters;
    List<EnzymePortalEcNumbers> ecNumberFilters;

    Set<Compound> uniquecompounds;
    Set<Disease> uniqueDiseases;

    private final int LIMIT = 8_00;
    private final int ACCESSION_LIMIT = 8_00;
    // private AccessionService accessionService;

    public EnzymeFinder(EnzymePortalService service, EbeyeRestService ebeyeRestService) {
        super(service, ebeyeRestService);

        enzymeSearchResults = new SearchResults();

        uniprotAccessions = new ArrayList<>();
        uniprotAccessionSet = new LinkedHashSet<>();
        enzymeSummaryList = new ArrayList<>();

        uniprotNameprefixes = new TreeSet<>();
        uniprotNameprefixSet = new LinkedHashSet<>();

        uniqueSpecies = new TreeSet<>();
        diseaseFilters = new LinkedList<>();
        compoundFilters = new ArrayList<>();
        ecNumberFilters = new LinkedList<>();

        uniquecompounds = new HashSet<>();
        uniqueDiseases = new HashSet<>();
    }

//    public EnzymeFinder(EnzymePortalService service, EbeyeRestService ebeyeRestService, AccessionService accessionService) {
//        this(service, ebeyeRestService);
//        this.accessionService = accessionService;
//        
//    }



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

    public List<String> findAccessionsforSearchTerm(String searchTerm, int limit) {
        if (!StringUtils.isEmpty(searchTerm)) {
            searchTerm = searchTerm.trim();
        }
        List<String> accessions = ebeyeRestService.queryForUniqueAccessions(searchTerm, limit);

        LOGGER.warn("Number of Processed Accession for  " + searchTerm + " :=:" + accessions.size());

        return accessions;
    }

    private void getResultsFromEpIndex() {
        String query = searchParams.getText();

        if (!StringUtils.isEmpty(query)) {
            query = query.trim();
        }

        List<String> accessions = ebeyeRestService.queryForUniqueAccessions(query, LIMIT);

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

    private void computeFilterFacets(UniprotEntry entry) {

        ecNumberFilters.addAll(entry.getEnzymePortalEcNumbersSet().stream().distinct().collect(Collectors.toList()));
        compoundFilters.addAll(entry.getEnzymePortalCompoundSet().stream().distinct().collect(Collectors.toList()));
        diseaseFilters.addAll(entry.getEnzymePortalDiseaseSet().stream().distinct().collect(Collectors.toList()));

        entry.getRelatedProteinsId().getUniprotEntrySet().stream().forEach(e -> {
            if (e.getSpecies().getScientificname() != null) {
                uniqueSpecies.add(e.getSpecies());
            }

        });

    }

    @Deprecated
    private List<UniprotEntry> computeUniqueEnzymes(UniprotEntry entry, String keyword) {

        LinkedList<UniprotEntry> theEnzymes = new LinkedList<>();
        Deque<UniprotEntry> enzymeList = new LinkedList<>();
        Set<String> proteinNames = new HashSet<>();

        if (!proteinNames.contains(entry.getProteinName())) {

            String enzymeName = HtmlUtility.cleanText(entry.getProteinName()).toLowerCase();
            if (enzymeName.toLowerCase().matches(".*" + keyword.toLowerCase() + ".*") && entry.getEntryType() != 1) {

                enzymeList.offerFirst(entry);

            } else {

                enzymeList.offerLast(entry);

            }

        }

        proteinNames.add(entry.getProteinName());

        //enzymeList.sort(SWISSPROT_FIRST);
        for (UniprotEntry enzyme : enzymeList) {
            if (HtmlUtility.cleanText(enzyme.getProteinName()).toLowerCase().equalsIgnoreCase(keyword.toLowerCase()) && enzyme.getEntryType() != 1) {

                LOGGER.info("FOUND A MATCH " + enzyme.getProteinName() + " => " + keyword + " entry type " + enzyme.getEntryType());
                theEnzymes.offerFirst(enzyme);

            } else {
                theEnzymes.offerLast(enzyme);

            }
            computeFilterFacets(enzyme);
        }

        return theEnzymes.stream().distinct().collect(Collectors.toList());
    }

    private List<UniprotEntry> computeUniqueEnzymes(List<UniprotEntry> enzymes, String keyword) {

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

        }

        for (UniprotEntry enzyme : enzymeList) {
            if (HtmlUtility.cleanText(enzyme.getProteinName()).toLowerCase().equalsIgnoreCase(keyword.toLowerCase()) && enzyme.getEntryType() != 1) {

                LOGGER.info("FOUND A MATCH " + enzyme.getProteinName() + " => " + keyword + " entry type " + enzyme.getEntryType());
                theEnzymes.offerFirst(enzyme);

            } else {
                theEnzymes.offerLast(enzyme);

            }
            computeFilterFacets(enzyme);
        }

        return theEnzymes.stream().distinct().collect(Collectors.toList());
    }

    private List<UniprotEntry> combine(List<UniprotEntry> part1, List<UniprotEntry> part2) {

        List<UniprotEntry> data = new LinkedList<>();
        data.addAll(part1);
        data.addAll(part2);

        return data;
    }

    protected List<UniprotEntry> useSplitCompletableFutures(List<String> accessions, String keyword) {
        Set<UniprotEntry> enzymeList = new LinkedHashSet<>();
        try {

            int half = divide(accessions);

            List<String> firstPart = accessions.subList(0, half);
            List<String> secondPart = accessions.subList(firstPart.size(), accessions.size());

            CompletableFuture<List<UniprotEntry>> future = CompletableFuture
                    .supplyAsync(() -> useSpliterator(firstPart, keyword));

            CompletableFuture<List<UniprotEntry>> future2 = CompletableFuture
                    .supplyAsync(() -> useSpliterator(secondPart, keyword));

            ///return   (List<UniprotEntry>) CompletableFuture.anyOf(future,future2).get();
            return future.thenCombineAsync(future2, (a, b) -> combine(a, b)).get().stream().distinct().collect(Collectors.toList());

        } catch (InterruptedException | ExecutionException ex) {
            LOGGER.error("InterruptedException | ExecutionException exception", ex);
        }
        return enzymeList.stream().collect(Collectors.toList());
    }

    private List<UniprotEntry> useSpliterator(List<String> accessions, String keyword) {
        Set<UniprotEntry> enzymeList = new LinkedHashSet<>();

        Stream<String> existingStream = accessions.stream();
        Stream<List<String>> partitioned = partition(existingStream, 100, 1);

        partitioned.parallel().forEach((chunk) -> {
            List<UniprotEntry> enzymes = service.findEnzymesByAccessions(chunk);//.stream().map(EnzymePortal::new).distinct().map(EnzymePortal::unwrapProtein).filter(Objects::nonNull).collect(Collectors.toList());

            if (enzymes != null) {
                //enzyme centric don't require to do this check
//                if (StringUtils.isEmpty(keyword)) {
//                    enzymeList.addAll(computeUniqueEnzymes(enzymes));
//                } else {
//                    enzymeList.addAll(computeUniqueEnzymes(enzymes, keyword));
//                }
                
                enzymeList.addAll(computeUniqueEnzymes(enzymes));
            }

        });

        return enzymeList.stream().collect(Collectors.toList());
    }

    private <T> Integer divide(List<T> list) {

        float f = (float) list.size() / 2;
        Integer part = Math.round(f);
        return part;
    }

    private List<UniprotEntry> useParallelExec(List<String> accessions, String keyword) {
        Set<UniprotEntry> enzymeList = new LinkedHashSet<>();

        try {

            int half = divide(accessions);

            List<String> chunkA = accessions.subList(0, half);
            List<String> chunkB = accessions.subList(chunkA.size(), accessions.size());

            List<String> firstPart = chunkA.subList(0, divide(chunkA));
            List<String> secondPart = chunkA.subList(firstPart.size(), chunkA.size());

            List<String> thirdPart = chunkB.subList(0, divide(chunkB));
            List<String> fourthPart = chunkB.subList(thirdPart.size(), chunkB.size());

            CompletableFuture<List<UniprotEntry>> future = CompletableFuture
                    .supplyAsync(() -> useSpliterator(firstPart, keyword));
            CompletableFuture<List<UniprotEntry>> future2 = CompletableFuture
                    .supplyAsync(() -> useSpliterator(secondPart, keyword));
            CompletableFuture<List<UniprotEntry>> future3 = CompletableFuture
                    .supplyAsync(() -> useSpliterator(thirdPart, keyword));
            CompletableFuture<List<UniprotEntry>> future4 = CompletableFuture
                    .supplyAsync(() -> useSpliterator(fourthPart, keyword));

            return future.thenCombineAsync(future2, (a, b) -> combine(a, b))
                    .thenCombineAsync(future3, (c, d) -> combine(c, d))
                    .thenCombineAsync(future4, (x, y) -> combine(x, y)).get().stream().distinct().collect(Collectors.toList());

        } catch (InterruptedException | ExecutionException ex) {
            LOGGER.error("InterruptedException | ExecutionException exception", ex);
        }
        return enzymeList.stream().collect(Collectors.toList());
    }

    private List<UniprotEntry> getEnzymesByAccessions(List<String> accessions, String keyword) {

        List<UniprotEntry> enzymeList = new LinkedList<>();
        if (!accessions.isEmpty()) {

            if (accessions.size() < 600) {

                long startTime = System.nanoTime();
                enzymeList = useParallelExec(accessions, keyword);
                long endTime = System.nanoTime();
                long duration = endTime - startTime;

                long elapsedtime = TimeUnit.SECONDS.convert(duration, TimeUnit.NANOSECONDS);
                LOGGER.warn("Time taken to process accessions of size (" + accessions.size() + ") :  (" + elapsedtime + " sec)");

                return enzymeList.stream().distinct().collect(Collectors.toList());

            } else if (accessions.size() >= 600) {

                long startTime = System.nanoTime();
                enzymeList = useSpliterator(accessions, keyword);
                long endTime = System.nanoTime();
                long duration = endTime - startTime;

                long elapsedtime = TimeUnit.SECONDS.convert(duration, TimeUnit.NANOSECONDS);
                LOGGER.warn("Final Time taken to process accessions of size (" + accessions.size() + ") :  (" + elapsedtime + " sec)");

                return enzymeList.stream().distinct().sorted().collect(Collectors.toList());

            }

        }
        return enzymeList.stream().collect(Collectors.toList());
    }

    private final Comparator<UniprotEntry> SWISSPROT_FIRST = (UniprotEntry e1, UniprotEntry e2) -> e1.getEntryType().compareTo(e2.getEntryType());

    private final Comparator<UniprotEntry> SWISSPROT_WITH_FUNCTION_FIRST = (UniprotEntry e1, UniprotEntry e2) -> {
        int comparison = e1.getEntryType().compareTo(e2.getEntryType());
        if (comparison == 0 && e1.getFunction() != null && e2.getFunction() != null) {
            comparison = e1.getFunction().compareToIgnoreCase(e2.getFunction());
        }
        return comparison;
    };

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

    public SearchResults getAssociatedProteins(String ec, String searchTerm, int limit) {

        List<String> accessions = ebeyeRestService.queryForUniqueAccessions(ec, searchTerm, limit);
        if(accessions.isEmpty()){
          accessions = ebeyeRestService.queryForUniqueAccessions(ec, limit);  
        }

        LOGGER.info("Number of Processed Accession for  " + ec + " " + searchTerm + " :=:" + accessions.size());

        uniprotAccessions = accessions.stream().distinct().collect(Collectors.toList());
        uniprotAccessionSet.addAll(uniprotAccessions.stream().distinct().collect(Collectors.toList()));

        List<String> accessionList
                = new ArrayList<>(uniprotAccessionSet);

        String keyword = HtmlUtility.cleanText(searchTerm);
        keyword = keyword.replaceAll("&quot;", "");

        LOGGER.debug("Getting enzyme summaries...");

        enzymeSummaryList = getEnzymeSummariesByAccessions(accessionList, keyword);

        enzymeSearchResults.setSummaryentries(enzymeSummaryList);
        enzymeSearchResults.setTotalfound(enzymeSummaryList.size());
        if (uniprotAccessionSet.size() != enzymeSummaryList.size()) {
            LOGGER.warn((uniprotAccessionSet.size() - enzymeSummaryList.size())
                    + " Some UniProt Accession have been lost");
        }
        LOGGER.debug("Building filters...");
        buildFilters(enzymeSearchResults);
        LOGGER.debug("Finished search");

        return enzymeSearchResults;

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
        priorityMapper.entrySet().stream().forEach(map -> {
            speciesFilters.add(map.getValue());
        });

        SearchFilters filters = new SearchFilters();
        filters.setSpecies(speciesFilters);
        filters.setCompounds(compoundFilters.stream().filter(Objects::nonNull).distinct().sorted(SORT_COMPOUND).collect(Collectors.toList()));

        filters.setDiseases(diseaseFilters.stream().distinct().sorted(SORT_DISEASE).collect(Collectors.toList()));

        filters.setEcNumbers(ecNumberFilters.stream().map(Family::new).distinct().sorted().map(Family::unwrapFamily).filter(Objects::nonNull).collect(Collectors.toList()));

        searchResults.setSearchfilters(filters);
    }

    private static final Comparator<EcNumber> SORT_BY_EC = (EcNumber ec1, EcNumber ec2) -> ec1.getEc().compareTo(ec2.getEc());
    private final Comparator<Disease> SORT_DISEASE = (Disease d1, Disease d2) -> d1.getName().compareToIgnoreCase(d2.getName());
    private final Comparator<Compound> SORT_COMPOUND = (Compound c1, Compound c2) -> c1.getName().compareToIgnoreCase(c2.getName());

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

    private static final Comparator<UniprotEntry> SORT_BY_IDENTITY_REVERSE_ORDER = (UniprotEntry key1, UniprotEntry key2) -> -(key1.getRelatedspecies().stream().findFirst().get().getIdentity().compareTo(key2.getRelatedspecies().stream().findFirst().get().getIdentity()));

    private static final Comparator<UniprotEntry> SORT_BY_IDENTITY = (UniprotEntry o1, UniprotEntry o2) -> {
        if (o1.getIdentity() == null && o2.getIdentity() == null) {
            return 0;
        }
        if (o1.getIdentity() == null) {
            return 1;
        }

        if (o2.getIdentity() == null) {
            return -1;
        }

        return -((Comparable) o1.getIdentity())
                .compareTo(o2.getIdentity());
    };

    /**
     *
     * @param accs accessions to filter for enzymes
     * @return confirmed enzymes
     */
    private List<String> filterBlastResultsForEnzymes(List<String> accs) {

        return service.filterEnzymesInAccessions(accs).stream().distinct().collect(Collectors.toList());

    }

    private List<UniprotEntry> computeUniqueEnzymes(List<UniprotEntry> enzymes) {
        List<UniprotEntry> enzymeList = new ArrayList<>();
        Set<String> proteinNames = new HashSet<>();
        for (UniprotEntry entry : enzymes) {

            if (!proteinNames.contains(entry.getProteinName())) {

                enzymeList.add(entry);

            }
            proteinNames.add(entry.getProteinName());

        }

        enzymeList.stream().forEach(e -> {
            computeFilterFacets(e);
        });

        return enzymeList.stream().distinct().sorted(SWISSPROT_FIRST).collect(Collectors.toList());
    }

    private List<UniprotEntry> getEnzymesByAccessions(List<String> accessions) {

        List<UniprotEntry> enzymeList = new LinkedList<>();
        if (!accessions.isEmpty()) {

            long startTime = System.nanoTime();
            enzymeList = useSpliterator(accessions, null);
            long endTime = System.nanoTime();
            long duration = endTime - startTime;

            long elapsedtime = TimeUnit.SECONDS.convert(duration, TimeUnit.NANOSECONDS);
            LOGGER.warn("Final Time taken to process accessions of size (" + accessions.size() + ") :  (" + elapsedtime + " sec)");

            return enzymeList.stream().distinct().sorted(SWISSPROT_FIRST).collect(Collectors.toList());

        }
        return enzymeList;
    }

    /**
     *
     * @return all diseases
     */
    public List<EnzymePortalDisease> findDiseases() {

        List<EnzymePortalDisease> diseases = service.findAllDiseases().stream().distinct().collect(Collectors.toList());

        return diseases;
    }

    public SearchResults computeEnzymeSummariesByOmimNumber(String omimNumber) {
        SearchResults searchResults = new SearchResults();

        List<String> accessions = service.findAccessionsByOmimNumber(omimNumber).stream().distinct().collect(Collectors.toList());
        //List<UniprotEntry> enzymes = service.findEnzymesByMeshId(meshId);

        List<UniprotEntry> enzymeList = getEnzymesByAccessions(accessions);
        searchResults.setSummaryentries(enzymeList);
        searchResults.setTotalfound(enzymeList.size());

        LOGGER.debug("Building filters...");
        buildFilters(searchResults);
        LOGGER.debug("Finished search");

        return searchResults;
    }

    public SearchResults computeEnzymeSummariesByPathwayName(String pathwayName) {
        SearchResults searchResults = new SearchResults();
        List<String> accessions = service.findAccessionsByPathwayName(pathwayName);

        List<UniprotEntry> enzymeList = getEnzymesByAccessions(accessions).stream().map(EnzymePortal::new).distinct().map(EnzymePortal::unwrapProtein).filter(Objects::nonNull).collect(Collectors.toList());

        searchResults.setSummaryentries(enzymeList);
        searchResults.setTotalfound(enzymeList.size());

        LOGGER.debug("Building filters...");
        buildFilters(searchResults);
        LOGGER.debug("Finished search");

        return searchResults;
    }

    public SearchResults computeEnzymeSummariesByPathwayId(String pathwayId) {
        SearchResults searchResults = new SearchResults();
        List<String> accessions = service.findAccessionsByPathwayId(pathwayId);

        List<UniprotEntry> enzymeList = getEnzymesByAccessions(accessions).stream().map(EnzymePortal::new).distinct().map(EnzymePortal::unwrapProtein).filter(Objects::nonNull).collect(Collectors.toList());

        searchResults.setSummaryentries(enzymeList);
        searchResults.setTotalfound(enzymeList.size());

        LOGGER.debug("Building filters...");
        buildFilters(searchResults);
        LOGGER.debug("Finished search");

        return searchResults;
    }

    public SearchResults computeEnzymeSummariesByEc(String ec) {
        SearchResults searchResults = new SearchResults();
        List<String> accessions = service.findAccessionsByEcNumber(ec).stream().distinct().collect(Collectors.toList());

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

    private List<UniprotEntry> blastEnzymesByAccessions(List<String> accessions) {

        final LinkedList<UniprotEntry> enzymeList = new LinkedList<>();
        List<UniprotEntry> results = new ArrayList<>();
        if (!accessions.isEmpty()) {

            Stream<String> existingStream = accessions.stream();
            Stream<List<String>> partitioned = partition(existingStream, 124, 1);

            partitioned.parallel().forEach(chunk -> {

                List<UniprotEntry> enzymes = service.findEnzymesByAccessions(chunk);

                enzymeList.addAll(enzymes);

                results.addAll(computeUniqueEnzymesFromBlast(enzymeList));

            });

            List<UniprotEntry> distinctEnzymes = results.stream().map(EnzymePortal::new).distinct().map(EnzymePortal::unwrapProtein).filter(Objects::nonNull).collect(Collectors.toList());

            return distinctEnzymes.stream().sorted(SORT_BY_IDENTITY_REVERSE_ORDER).collect(Collectors.toList());

        }

        return results;

    }

    private List<UniprotEntry> computeUniqueEnzymesFromBlast(List<UniprotEntry> enzymes) {
        List<UniprotEntry> enzymeList = new LinkedList<>();
        Set<String> proteinNames = new HashSet<>();
        for (UniprotEntry entry : enzymes) {

            if (!proteinNames.contains(entry.getProteinName().trim())) {

                enzymeList.add(entry);

            }
            proteinNames.add(entry.getProteinName().trim());
        }

        enzymeList.stream().forEach(e -> {
            computeFilterFacets(e);
        });

        return enzymeList.stream().distinct().sorted(SORT_BY_IDENTITY_REVERSE_ORDER).collect(Collectors.toList());
    }

}
