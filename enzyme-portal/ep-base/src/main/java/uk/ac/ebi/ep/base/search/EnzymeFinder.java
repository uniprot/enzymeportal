package uk.ac.ebi.ep.base.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import uk.ac.ebi.biobabel.lucene.LuceneParser;
import static uk.ac.ebi.ep.data.batch.PartitioningSpliterator.partition;
import uk.ac.ebi.ep.data.common.ModelOrganisms;
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
import uk.ac.ebi.ep.ebeye.ProteinGroupService;

/**
 *
 * @author joseph
 */
public class EnzymeFinder extends EnzymeBase implements EnzymeFinderService {

    private final Logger logger = Logger.getLogger(EnzymeFinder.class);
    protected SearchParams searchParams;
    protected SearchResults enzymeSearchResults;

    boolean newSearch;

    List<String> speciesFilter;
    List<String> compoundFilter;
    List<UniprotEntry> enzymeSummaryList;

    Set<Species> uniqueSpecies;
    List<Disease> diseaseFilters;
    List<Compound> compoundFilters;
    List<EnzymePortalEcNumbers> ecNumberFilters;

    Set<Compound> uniquecompounds;
    Set<Disease> uniqueDiseases;

    Map<Integer, Species> priorityMapper;
    List<Long> modelTaxIdList;

    private static final int ASSOCIATED_PROTEIN_LIMIT = 8_00;

    public EnzymeFinder(EnzymePortalService service, EbeyeRestService ebeyeRestService, ProteinGroupService proteinGroupService) {
        super(service, ebeyeRestService, proteinGroupService);

        enzymeSearchResults = new SearchResults();

        enzymeSummaryList = new ArrayList<>();

        uniqueSpecies = new TreeSet<>();
        diseaseFilters = new LinkedList<>();
        compoundFilters = new ArrayList<>();
        ecNumberFilters = new LinkedList<>();

        uniquecompounds = new HashSet<>();
        uniqueDiseases = new HashSet<>();

        priorityMapper = new TreeMap<>();
        modelTaxIdList = new ArrayList<>();

    }

    public EnzymePortalService getService() {
        return enzymePortalService;
    }

    public SearchParams getSearchParams() {
        return searchParams;
    }

    @Override
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
        List<String> accessions = proteinGroupService.queryForUniquePrimaryAccessions(searchTerm, limit);

        logger.warn("Number of Processed Accession for  " + searchTerm + " :=:" + accessions.size());

        return accessions;
    }

    private List<String> getResultsFromEpIndex() {
        String query = searchParams.getText();

        if (!StringUtils.isEmpty(query)) {
            query = query.trim();
        }

        List<String> accessions = proteinGroupService.queryForUniquePrimaryAccessions(query, ASSOCIATED_PROTEIN_LIMIT);

        logger.warn("Number of Processed Accession for  " + query + " :=:" + accessions.size());

        return accessions;
    }

    /**
     * Queries EB-Eye for UniProt IDs corresponding to enzymes, and adds them to
     * the uniprotEnzymeIds field.
     *
     * @throws EnzymeFinderException
     */
    private List<String> queryEbeyeForUniprotAccessions() {
        return getResultsFromEpIndex();
    }

    private void computeFilterFacets(UniprotEntry entry) {

        ecNumberFilters.addAll(entry.getEnzymePortalEcNumbersSet());
        compoundFilters.addAll(entry.getCompounds());
        // compoundFilters.addAll(entry.getEnzymePortalCompoundSet().stream().distinct().collect(Collectors.toList()));
        diseaseFilters.addAll(entry.getDiseases());

        entry.getRelatedProteinsId()
                .getUniprotEntrySet()
                //.parallelStream()
                .stream()
                .forEach(e -> uniqueSpecies.add(e.getSpecies()));

    }

    private List<UniprotEntry> findUniprotEntriesbyAccessions(List<String> accessions) {
        List<UniprotEntry> enzymes = new ArrayList<>();
        if (!accessions.isEmpty()) {
            int accessionSize = accessions.size();
            if (accessionSize >= 0 && accessionSize <= 5000) {
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                logger.warn("About to perform serial query to the database with " + accessions.size() + " accessions");
                enzymes = enzymePortalService.findEnzymesByAccessions(accessions);
                stopWatch.stop();
                logger.warn("Enzymes :: Sequential Query took " + stopWatch.getTotalTimeSeconds() + " secs for " + accessions.size() + " accessions");

            } else {

                StopWatch stopWatch = new StopWatch();
                stopWatch.start();

                logger.warn("About to query database with " + accessions.size() + " accessions");
                enzymes = queryDatabaseForEnzymes(accessions);

                stopWatch.stop();
                logger.warn("Enzymes :: Parallel Database Query took " + stopWatch.getTotalTimeSeconds() + " secs for " + accessions.size() + " accessions");

            }
            logger.warn("Number of Enzymes found before filter and sorting :: " + enzymes.size());
            List<UniprotEntry> enzymeResult = enzymes
                    .stream()
                    .filter(Objects::nonNull)
                    .sorted()
                    .collect(Collectors.toList());
            computeUniqueEnzymes(enzymes);
            return enzymeResult;

        }

        return enzymes;
    }

    private List<UniprotEntry> queryDatabaseForEnzymesXXX(List<String> accessions) {

        List<UniprotEntry> result = new ArrayList<>();

        accessions
                .stream()
                //.parallel()
                .map(accession -> enzymePortalService.findEnzymeByAccession(accession))
                .forEach(protein -> result.add(protein));

        return result;

    }

    private List<UniprotEntry> queryDatabaseForEnzymes(List<String> accessions) {
//
//        // ExecutorService executorService = Executors.newCachedThreadPool();
//        // ExecutorService executorService = Executors.newFixedThreadPool(Math.min(accessions.size(), 10));
//                //int corePoolSize = 10;
////        int maximumPoolSize = 50;
////        long keepAliveTime = 30;//seconds
////        // ExecutorService executorService = new CustomThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime);
//        

        final ForkJoinPool executorService = new ForkJoinPool();

        List<CompletableFuture<UniprotEntry>> allFutures = new ArrayList<>();
        for (String accession : accessions) {
            CompletableFuture<UniprotEntry> query = CompletableFuture.supplyAsync(() -> {
                // submit query to db
                return enzymePortalService.findEnzymeByAccession(accession);

            }, executorService).exceptionally(error -> {
                return null;
            });
            allFutures.add(query);
        }
        executorService.shutdown();
        return allFutures
                .stream()
                .map(x -> x.join())
                .collect(Collectors.toList());

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
    private List<UniprotEntry> getEnzymeSummariesByAccessions(List<String> accessions) {

        List<UniprotEntry> summaries = findUniprotEntriesbyAccessions(accessions);

        return summaries;
    }

    @Override
    public SearchResults getAssociatedProteinsByEc(String ec, int limit) {

        List<String> accessions = proteinGroupService.queryForPrimaryAccessionsByEc(ec, limit);

        logger.error("Number of Processed Accession for  EC " + ec + " :: " + accessions.size());
        return getSearchResultsFromAccessions(accessions);
    }

    /**
     * build a searchResult (protein entries) by querying EBI
     * enzymePortalService using EC and OMIM number
     *
     * @param omimId OMIM number
     * @param ec
     * @param limit
     * @return SearchResults (AssociatedProteins)
     */
    @Override
    public SearchResults getAssociatedProteinsByOmimIdAndEc(String omimId, String ec, int limit) {

        List<String> accessions = proteinGroupService.queryForPrimaryAccessionsByOmimIdAndEc(omimId, ec, limit);

        logger.info("Number of Processed Accession for  EC " + ec + " AND OMIM " + omimId + " :=:" + accessions.size());
        return getSearchResultsFromAccessions(accessions);
    }

    @Override
    public SearchResults getAssociatedProteinsByTaxIdAndEc(String taxId, String ec, int limit) {

        List<String> accessions = proteinGroupService.queryForPrimaryAccessionsByTaxIdAndEc(taxId, ec, limit);

        logger.info("Number of Processed Accession for  EC " + ec + " AND TAXID " + taxId + " :=:" + accessions.size());
        return getSearchResultsFromAccessions(accessions);
    }

    @Override
    public SearchResults getAssociatedProteinsByPathwayIdAndEc(String pathwayId, String ec, int limit) {

        List<String> accessions = proteinGroupService.queryForPrimaryAccessionsByPathwayIdAndEc(pathwayId, ec, limit);

        logger.info("Number of Processed Accession for  EC " + ec + " AND PathwayId " + pathwayId + " :=:" + accessions.size());
        return getSearchResultsFromAccessions(accessions);
    }

    /**
     * build a searchResult (protein entries) by querying EBI
     * enzymePortalService using EC and a keyword (FullText search)
     *
     * @param ec
     * @param searchTerm the keyword
     * @param limit
     * @return SearchResults (AssociatedProteins)
     */
    @Override
    public SearchResults getAssociatedProteinsByEcAndFulltextSearch(String ec, String searchTerm, int limit) {

        List<String> accessions = proteinGroupService.queryForPrimaryAccessionsByEcAndKeyword(ec, searchTerm, limit);
        if (accessions.isEmpty()) {
            accessions = proteinGroupService.queryForUniquePrimaryAccessions(ec, limit);
        }

        logger.info("Number of Processed Accession for  " + ec + " " + searchTerm + " :=:" + accessions.size());

        return getSearchResultsFromAccessions(accessions);

    }

    private SearchResults getSearchResultsFromAccessions(List<String> accessions) {

        logger.info("Getting enzyme summaries..." + accessions.size());

        enzymeSummaryList = findUniprotEntriesbyAccessions(accessions);
        enzymeSearchResults.setSummaryentries(enzymeSummaryList);
        enzymeSearchResults.setTotalfound(enzymeSummaryList.size());
//        if (uniprotAccessionSet.size() != enzymeSummaryList.size()) {
//            logger.warn((uniprotAccessionSet.size() - enzymeSummaryList.size())
//                    + " Some UniProt Accession have been lost");
//        }     
        logger.debug("Building filters...");
        buildFilters(enzymeSearchResults);
        logger.debug("Finished search");

        return enzymeSearchResults;
    }

    @Override
    public SearchResults getEnzymes(SearchParams searchParams) {

        processInputs(searchParams);
        List<String> uniprotAccessions = queryEbeyeForUniprotAccessions();

        logger.debug("Getting enzyme summaries...");

        enzymeSummaryList = getEnzymeSummariesByAccessions(uniprotAccessions);
        enzymeSearchResults.setSummaryentries(enzymeSummaryList);
        enzymeSearchResults.setTotalfound(enzymeSummaryList.size());

        logger.debug("Building filters...");
        buildFilters(enzymeSearchResults);
        logger.debug("Finished search");

        return enzymeSearchResults;
    }

    /**
     * Builds filters - species, compounds, diseases - from a result list.
     *
     * @param searchResults the result list, which will be modified by setting
     * the relevant filters.
     */
    private void buildFilters(SearchResults searchResults) {

        for (ModelOrganisms modelOrganisms : ModelOrganisms.values()) {
            modelTaxIdList.add(modelOrganisms.getTaxId());
        }

        AtomicInteger key = new AtomicInteger(50);
        AtomicInteger customKey = new AtomicInteger(6);
        SearchFilters filters = new SearchFilters();

        uniqueSpecies
                .forEach(sp -> sortSpecies(modelTaxIdList, sp, priorityMapper, customKey, key));

        priorityMapper
                .entrySet()
                .forEach(map -> filters.getSpecies().add(map.getValue()));

        filters.setCompounds(compoundFilters
                .stream()
                .filter(Objects::nonNull)
                .distinct()
                //.sorted(SORT_COMPOUND)
                .collect(Collectors.toList()));
//
        filters.setDiseases(diseaseFilters
                .stream()
                .distinct()
                //.sorted(SORT_DISEASE)
                .collect(Collectors.toList()));
//
//        filters.setEcNumbers(ecNumberFilters.stream().map(Family::new).distinct().sorted().map(Family::unwrapFamily).filter(Objects::nonNull).collect(Collectors.toList()));
        //filters.setCompounds(compoundFilters);
        //filters.setDiseases(diseaseFilters);
        List<EcNumber> ecNumbers = ecNumberFilters
                .stream()
                .map(Family::new)
                .distinct()
                .sorted()
                .map(Family::unwrapFamily)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        filters.setEcNumbers(ecNumbers);

        searchResults.setSearchfilters(filters);
    }

    private void sortSpecies(List<Long> modelTaxIdList, Species sp, Map<Integer, Species> priorityMapper, AtomicInteger customKey, AtomicInteger key) {
        //Human,Mouse, Mouse-ear cress, fruit fly, yeast, e.coli, Rat,worm
        // "Homo sapiens","Mus musculus","Rattus norvegicus", "Drosophila melanogaster","WORM","Saccharomyces cerevisiae","ECOLI"
        if (modelTaxIdList.contains(sp.getTaxId())) {
            if (sp.getTaxId().equals(ModelOrganisms.HUMAN.getTaxId())) {

                priorityMapper.put(1, sp);
            } else if (sp.getTaxId().equals(ModelOrganisms.MOUSE.getTaxId())) {

                priorityMapper.put(2, sp);
            } else if (sp.getTaxId().equals(ModelOrganisms.MOUSE_EAR_CRESS.getTaxId())) {

                priorityMapper.put(3, sp);
            } else if (sp.getTaxId().equals(ModelOrganisms.FRUIT_FLY.getTaxId())) {

                priorityMapper.put(4, sp);
            } else if (sp.getTaxId().equals(ModelOrganisms.ECOLI.getTaxId())) {

                priorityMapper.put(5, sp);
            } else if (sp.getTaxId().equals(ModelOrganisms.BAKER_YEAST.getTaxId())) {
                priorityMapper.put(6, sp);

            } else if (sp.getTaxId().equals(ModelOrganisms.RAT.getTaxId())) {
                priorityMapper.put(customKey.getAndIncrement(), sp);
            }

        } else {
            priorityMapper.put(key.getAndIncrement(), sp);
        }
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
    private SearchResults getSearchResults(List<String> uniprotIds)
            throws EnzymeFinderException {
        SearchResults results = new SearchResults();

        List<String> distinctPrefixes = uniprotIds.stream().distinct().collect(Collectors.toList());
        @SuppressWarnings("unchecked")
        List<UniprotEntry> summaries
                = getEnzymeSummariesByAccessions(distinctPrefixes);
        results.setSummaryentries(summaries);
        results.setTotalfound(summaries.size());
        if (distinctPrefixes.size() != summaries.size()) {
            logger.warn((distinctPrefixes.size() - summaries.size())
                    + " UniProt ID prefixes have been lost.");
        }
        buildFilters(results);
        return results;
    }

    @Override
    public SearchResults getEnzymesByCompound(SearchParams searchParams) throws EnzymeFinderException {
        List<String> accessions = this.getService().findEnzymesByCompound(searchParams.getText());

        return getSearchResults(accessions);
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

    private Collection<UniprotEntry> computeUniqueEnzymes(Collection<UniprotEntry> enzymes) {
        enzymes
                .stream()
                .filter(Objects::nonNull)
                .forEach(e -> computeFilterFacets(e));

        return enzymes;
    }

    /**
     *
     * @return all diseases
     */
    @Override
    public List<EnzymePortalDisease> findDiseases() {

        List<EnzymePortalDisease> diseases = enzymePortalService.findAllDiseases().stream().distinct().collect(Collectors.toList());

        return diseases;
    }

    @Override
    public SearchResults computeEnzymeSummariesByOmimNumber(String omimNumber) {
        SearchResults searchResults = new SearchResults();

        List<String> accessions = enzymePortalService.findAccessionsByOmimNumber(omimNumber).stream().distinct().collect(Collectors.toList());

        List<UniprotEntry> enzymeList = findUniprotEntriesbyAccessions(accessions);
        searchResults.setSummaryentries(enzymeList);
        searchResults.setTotalfound(enzymeList.size());

        logger.debug("Building filters...");
        buildFilters(searchResults);
        logger.debug("Finished search");

        return searchResults;
    }

    @Override
    public SearchResults computeEnzymeSummariesByPathwayName(String pathwayName) {
        SearchResults searchResults = new SearchResults();
        List<String> accessions = enzymePortalService.findAccessionsByPathwayName(pathwayName);

        List<UniprotEntry> enzymeList = findUniprotEntriesbyAccessions(accessions).stream().map(EnzymePortal::new).distinct().map(EnzymePortal::unwrapProtein).filter(Objects::nonNull).collect(Collectors.toList());

        searchResults.setSummaryentries(enzymeList);
        searchResults.setTotalfound(enzymeList.size());

        logger.debug("Building filters...");
        buildFilters(searchResults);
        logger.debug("Finished search");

        return searchResults;
    }

    @Override
    public SearchResults computeEnzymeSummariesByPathwayId(String pathwayId) {
        SearchResults searchResults = new SearchResults();
        List<String> accessions = enzymePortalService.findAccessionsByPathwayId(pathwayId);

        List<UniprotEntry> enzymeList = findUniprotEntriesbyAccessions(accessions).stream().map(EnzymePortal::new).distinct().map(EnzymePortal::unwrapProtein).filter(Objects::nonNull).collect(Collectors.toList());

        searchResults.setSummaryentries(enzymeList);
        searchResults.setTotalfound(enzymeList.size());

        logger.debug("Building filters...");
        buildFilters(searchResults);
        logger.debug("Finished search");

        return searchResults;
    }

    @Override
    public SearchResults computeEnzymeSummariesByEc(String ec) {
        SearchResults searchResults = new SearchResults();
        List<String> accessions = enzymePortalService.findAccessionsByEcNumber(ec).stream().distinct().collect(Collectors.toList());

        List<UniprotEntry> enzymeList = findUniprotEntriesbyAccessions(accessions);
        searchResults.setSummaryentries(enzymeList);
        searchResults.setTotalfound(enzymeList.size());

        logger.debug("Building filters...");
        buildFilters(searchResults);
        logger.debug("Finished search");

        return searchResults;
    }

    /**
     *
     * @return all reactions
     */
    public List<EnzymePortalReaction> findAllReactions() {

        return enzymePortalService.findReactions().stream().distinct().collect(Collectors.toList());
    }

    /**
     *
     * @return all pathways
     */
    @Override
    public List<EnzymePortalPathways> findAllPathways() {

        return enzymePortalService.findPathways().stream().distinct().collect(Collectors.toList());
    }

    @Deprecated
    private List<UniprotEntry> blastEnzymesByAccessions(List<String> accessions) {

        final LinkedList<UniprotEntry> enzymeList = new LinkedList<>();
        List<UniprotEntry> results = new ArrayList<>();
        if (!accessions.isEmpty()) {

            Stream<String> existingStream = accessions.stream();
            Stream<List<String>> partitioned = partition(existingStream, 124, 1);

            partitioned.parallel().forEach(chunk -> {

                List<UniprotEntry> enzymes = enzymePortalService.findEnzymesByAccessions(chunk);

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
