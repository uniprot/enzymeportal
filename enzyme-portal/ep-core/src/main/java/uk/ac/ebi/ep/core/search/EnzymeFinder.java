package uk.ac.ebi.ep.core.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.collections.list.SetUniqueList;
import org.apache.log4j.Logger;
import uk.ac.ebi.biobabel.blast.Hit;
import uk.ac.ebi.biobabel.blast.Hsp;
import uk.ac.ebi.biobabel.blast.NcbiBlastClient;
import uk.ac.ebi.biobabel.blast.NcbiBlastClientException;
import uk.ac.ebi.biobabel.lucene.LuceneParser;
import uk.ac.ebi.ep.adapter.ebeye.Domains;
import uk.ac.ebi.ep.adapter.ebeye.EbeyeAdapter;
import uk.ac.ebi.ep.adapter.ebeye.IEbeyeAdapter;
import uk.ac.ebi.ep.adapter.ebeye.IEbeyeAdapter.FieldsOfGetResults;
import uk.ac.ebi.ep.adapter.ebeye.IEbeyeAdapter.FieldsOfUniprotNameMap;
import uk.ac.ebi.ep.adapter.ebeye.param.ParamOfGetResults;
import uk.ac.ebi.ep.adapter.intenz.IintenzAdapter;
import uk.ac.ebi.ep.adapter.intenz.IntenzAdapter;
import uk.ac.ebi.ep.adapter.uniprot.IUniprotAdapter;
import uk.ac.ebi.ep.adapter.uniprot.UniprotJapiAdapter;
import uk.ac.ebi.ep.adapter.uniprot.UniprotWsAdapter;
import uk.ac.ebi.ep.core.CompoundDefaultWrapper;
import uk.ac.ebi.ep.core.DiseaseDefaultWrapper;
import uk.ac.ebi.ep.core.SpeciesDefaultWrapper;
import uk.ac.ebi.ep.core.search.util.EnzymeSummaryProcessor;
import uk.ac.ebi.ep.core.search.util.SynonymsProcessor;
import uk.ac.ebi.ep.mm.CustomXRef;
import uk.ac.ebi.ep.mm.Entry;
import uk.ac.ebi.ep.mm.MmDatabase;
import uk.ac.ebi.ep.search.exception.EnzymeFinderException;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.ep.search.model.Compound;
import uk.ac.ebi.ep.search.model.Disease;
import uk.ac.ebi.ep.search.model.EnzymeAccession;
import uk.ac.ebi.ep.search.model.EnzymeSummary;
import uk.ac.ebi.ep.search.model.SearchFilters;
import uk.ac.ebi.ep.search.model.SearchParams;
import uk.ac.ebi.ep.search.model.SearchResults;
import uk.ac.ebi.ep.search.model.Species;
import uk.ac.ebi.ep.util.EPUtil;
import uk.ac.ebi.ep.util.query.LuceneQueryBuilder;

/**
 * NOTE: the adapters must be configured before using this finder!
 *
 * @since 1.0
 * @version $LastChangedRevision$ <br/> $LastChangedDate: 2012-02-27 17:03:08
 * +0000 (Mon, 27 Feb 2012) $ <br/> $Author$
 * @author $Author$
 */
public class EnzymeFinder implements IEnzymeFinder {

//********************************* VARIABLES ********************************//
    private static final Logger LOGGER = Logger.getLogger(EnzymeFinder.class);
    Config searchConfig;
    IEbeyeAdapter ebeyeAdapter;
    IUniprotAdapter uniprotAdapter;
    IintenzAdapter intenzAdapter;
    protected SearchParams searchParams;
    SearchResults enzymeSearchResults;
    List<String> uniprotEnzymeIds;
    boolean newSearch;
    Set<String> uniprotIdPrefixSet;
    List<String> speciesFilter;
    List<String> compoundFilter;
    List<EnzymeSummary> enzymeSummaryList;
    MegaMapperConnection megaMapperConnection;
    private Set<DiseaseDefaultWrapper> uniqueDiseases = new TreeSet<DiseaseDefaultWrapper>();
    private NcbiBlastClient blastClient;

//******************************** CONSTRUCTORS ******************************//
    public EnzymeFinder(Config config) {
        this.searchConfig = config;
        enzymeSearchResults = new SearchResults();
        ebeyeAdapter = new EbeyeAdapter();
        uniprotEnzymeIds = new ArrayList<String>();
        uniprotIdPrefixSet = new LinkedHashSet<String>();
        enzymeSummaryList = new ArrayList<EnzymeSummary>();
        intenzAdapter = new IntenzAdapter();
        megaMapperConnection = new MegaMapperConnection(config.getMmDatasource());

        switch (config.uniprotImplementation) {
            case JAPI:
                uniprotAdapter = new UniprotJapiAdapter();
                break;
            case WS:
                uniprotAdapter = new UniprotWsAdapter();
                break;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        closeResources();
    }

    public void closeResources() {
        megaMapperConnection.closeMegaMapperConnection();
    }

    public void setSearchParams(SearchParams searchParams) {
        this.searchParams = searchParams;
    }

    public IEbeyeAdapter getEbeyeAdapter() {
        return ebeyeAdapter;
    }

    public IintenzAdapter getIntenzAdapter() {
        return intenzAdapter;
    }

    public IUniprotAdapter getUniprotAdapter() {
        return uniprotAdapter;
    }

    @Override
    public SearchResults getEnzymes(SearchParams searchParams)
            throws EnzymeFinderException {

        processInputs(searchParams);

        /*
         * First time search or when user inserts a new keyword, the filter is
         * reset then the search is performed across all domains without
         * considering the filter.
         */
        if (newSearch) {
            // Search in EBEye for Uniprot ids, the search is filtered by ec:*
            LOGGER.debug("Starting new search");
            try {
                queryEbeyeForUniprotIds();
                LOGGER.debug("UniProt IDs from UniProt: " + uniprotEnzymeIds.size());
            } catch (EnzymeFinderException e){
                LOGGER.error("Unable to search EB-Eye uniprot domain", e);
            }
            // Search in Intenz, Rhea, Reactome, PDBe etc. for Uniprot ids.
            try {
                queryEbeyeOtherDomainForIds();
                LOGGER.debug("UniProt IDs from UniProt+ChEBI+others: "
                        + uniprotEnzymeIds.size());
            } catch (EnzymeFinderException e){
                LOGGER.error("Unable to search EB-Eye other domains", e);
            }

            uniprotIdPrefixSet.addAll(EPUtil.getIdPrefixes(uniprotEnzymeIds));
        }

        List<String> idPrefixesList =
                new ArrayList<String>(uniprotIdPrefixSet);

        LOGGER.debug("Getting enzyme summaries...");
        enzymeSummaryList = getEnzymeSummaries(idPrefixesList, searchParams.getSpecies());
        enzymeSearchResults.setSummaryentries(enzymeSummaryList);
        enzymeSearchResults.setTotalfound(enzymeSummaryList.size());
        if (uniprotIdPrefixSet.size() != enzymeSummaryList.size()) {
            LOGGER.warn((uniprotIdPrefixSet.size() - enzymeSummaryList.size())
                    + " UniProt ID prefixes have been lost");
        }
        LOGGER.debug("Building filters...");
        buildFilters(enzymeSearchResults);
        LOGGER.debug("Finished search");
        closeResources();
        return enzymeSearchResults;
    }
    
    /**
     * Builds search results from a list of UniProt IDs. It groups orthologs and
     * builds summaries for them.
     * @param uniprotIds The UniProt IDs from a search.
     * @return the search results with summaries.
     * @throws EnzymeFinderException
     * @since
     */
    private SearchResults getSearchResults(List<String> uniprotIds)
    throws EnzymeFinderException{
        SearchResults results = new SearchResults();
        SetUniqueList distinctPrefixes =
                SetUniqueList.decorate(EPUtil.getIdPrefixes(uniprotIds));
        @SuppressWarnings("unchecked")
        List<EnzymeSummary> summaries =
                getEnzymeSummaries(distinctPrefixes, null);
        results.setSummaryentries(summaries);
        results.setTotalfound(summaries.size());
        if (distinctPrefixes.size() != summaries.size()){
            LOGGER.warn((distinctPrefixes.size() - summaries.size())
                    + " UniProt ID prefixes have been lost.");
        }
        buildFilters(results);
        return results;
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
        List<String> commonSpecieList = new ArrayList<String>();
        for (CommonSpecies commonSpecies : CommonSpecies.values()) {
            commonSpecieList.add(commonSpecies.getScientificName());
        }

        Map<Integer, Species> priorityMapper = new TreeMap<Integer, Species>();

        Set<SpeciesDefaultWrapper> uniqueSpecies = new TreeSet<SpeciesDefaultWrapper>();
        Set<CompoundDefaultWrapper> related_compounds =
                new TreeSet<CompoundDefaultWrapper>();

        for (EnzymeSummary summaryEntry : searchResults.getSummaryentries()) {

            final Collection<Compound> summaryCompounds =
                    computeCompound(summaryEntry.getUniprotid());
            if (summaryCompounds != null) {
                // TODO: reduce the list to unique compounds
                summaryEntry.setCompounds(
                        new ArrayList<Compound>(summaryCompounds));
                related_compounds.addAll(wrap(summaryCompounds));
            }
            Set<DiseaseDefaultWrapper> disease_found = computeDiseaseWithUniprotId(summaryEntry.getUniprotid());



            if (!disease_found.isEmpty() && disease_found.size() > 0) {
                for (DiseaseDefaultWrapper ddw : disease_found) {
                    summaryEntry.getDiseases().add(ddw.getDisease());
                }
            }



            for (EnzymeAccession ea : summaryEntry.getRelatedspecies()) {
                Species sp = ea.getSpecies();


                if (sp != null) {

                    if (!disease_found.isEmpty() && disease_found.size() > 0) {
                        for (DiseaseDefaultWrapper ddw : disease_found) {
                            sp.getDiseases().add(ddw.getDisease());

                        }
                    }

                    uniqueSpecies.add(new SpeciesDefaultWrapper(sp));

                }


            }
        }
        AtomicInteger key = new AtomicInteger(50);
        AtomicInteger customKey = new AtomicInteger(6);
        for (SpeciesDefaultWrapper wrapper : uniqueSpecies) {
            Species sp = wrapper.getSpecies();

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


        List<Species> speciesFilters = new LinkedList<Species>();
        for (Map.Entry<Integer, Species> map : priorityMapper.entrySet()) {
            speciesFilters.add(map.getValue());

        }



        List<Compound> compoundFilters = new ArrayList<Compound>();
        for (CompoundDefaultWrapper cw : related_compounds) {
            compoundFilters.add(cw.getCompound());
        }
        List<Disease> diseaseFilter = new ArrayList<Disease>();
        for (DiseaseDefaultWrapper dw : uniqueDiseases) {
            diseaseFilter.add(dw.getDisease());
        }

        SearchFilters filters = new SearchFilters();
        filters.setSpecies(speciesFilters);
        filters.setCompounds(compoundFilters);
        filters.setDiseases(diseaseFilter);
        searchResults.setSearchfilters(filters);
    }

    /**
     * Builds filters - species, compounds, diseases - from a result list.
     *
     * @param searchResults the result list, which will be modified by setting
     * the relevant filters.
     */
    private SearchFilters buildSearchFilters(SearchResults searchResults) {
        //  String[] commonSpecie = {"Human", "Mouse", "Rat", "Fruit fly", "Worm", "Yeast", "Ecoli"};
        // CommonSpecies [] commonSpecie = {"Homo sapiens","Mus musculus","Rattus norvegicus", "Drosophila melanogaster","Saccharomyces cerevisiae"};
        // List<String> commonSpecieList = Arrays.asList(commonSpecie);
        List<String> commonSpecieList = new ArrayList<String>();
        for (CommonSpecies commonSpecies : CommonSpecies.values()) {
            commonSpecieList.add(commonSpecies.getScientificName());
        }

        Map<Integer, Species> priorityMapper = new TreeMap<Integer, Species>();

        Set<SpeciesDefaultWrapper> uniqueSpecies = new TreeSet<SpeciesDefaultWrapper>();
        Set<CompoundDefaultWrapper> related_compounds =
                new TreeSet<CompoundDefaultWrapper>();

        for (EnzymeSummary summaryEntry : searchResults.getSummaryentries()) {

            final Collection<Compound> summaryCompounds =
                    computeCompound(summaryEntry.getUniprotid());
            if (summaryCompounds != null) {
                // TODO: reduce the list to unique compounds
                summaryEntry.setCompounds(
                        new ArrayList<Compound>(summaryCompounds));
                related_compounds.addAll(wrap(summaryCompounds));
            }
            Set<DiseaseDefaultWrapper> disease_found = computeDiseaseWithUniprotId(summaryEntry.getUniprotid());



            if (!disease_found.isEmpty() && disease_found.size() > 0) {
                for (DiseaseDefaultWrapper ddw : disease_found) {
                    summaryEntry.getDiseases().add(ddw.getDisease());
                }
            }



            for (EnzymeAccession ea : summaryEntry.getRelatedspecies()) {
                Species sp = ea.getSpecies();


                if (sp != null) {

                    if (!disease_found.isEmpty() && disease_found.size() > 0) {
                        for (DiseaseDefaultWrapper ddw : disease_found) {
                            sp.getDiseases().add(ddw.getDisease());

                        }
                    }

                    uniqueSpecies.add(new SpeciesDefaultWrapper(sp));

                }


            }
        }
        AtomicInteger key = new AtomicInteger(50);
        AtomicInteger customKey = new AtomicInteger(6);
        for (SpeciesDefaultWrapper wrapper : uniqueSpecies) {
            Species sp = wrapper.getSpecies();

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


        List<Species> speciesFilters = new LinkedList<Species>();
        for (Map.Entry<Integer, Species> map : priorityMapper.entrySet()) {
            speciesFilters.add(map.getValue());

        }



        List<Compound> compoundFilters = new ArrayList<Compound>();
        for (CompoundDefaultWrapper cw : related_compounds) {
            compoundFilters.add(cw.getCompound());
        }
        List<Disease> diseaseFilter = new ArrayList<Disease>();
        for (DiseaseDefaultWrapper dw : uniqueDiseases) {
            diseaseFilter.add(dw.getDisease());
        }

        SearchFilters filters = new SearchFilters();
        filters.setSpecies(speciesFilters);
        filters.setCompounds(compoundFilters);
        filters.setDiseases(diseaseFilter);
        searchResults.setSearchfilters(filters);
        return filters;
    }

    /**
     * Retrieves a collection of compounds related to a UniProt entry.
     *
     * @param uniprotId a UniProt ID (<i>not accession</i>), or the prefix of it
     * including the underscore.
     * @return a collection of compounds related to a UniProt entry.
     */
    private Collection<Compound> computeCompound(String uniprotId) {
        return megaMapperConnection.getMegaMapper()
                .getCompounds(uniprotId.substring(0, uniprotId.indexOf("_") + 1));
    }

    private Set<CompoundDefaultWrapper> wrap(Collection<Compound> compounds) {
        Set<CompoundDefaultWrapper> wrappers =
                new TreeSet<CompoundDefaultWrapper>();
        for (Compound compound : compounds) {
            wrappers.add(new CompoundDefaultWrapper(compound));
        }
        return wrappers;
    }

    public Set<DiseaseDefaultWrapper> computeDiseaseWithUniprotId(String uniprotAccession) {
        Set<DiseaseDefaultWrapper> related_disease = new TreeSet<DiseaseDefaultWrapper>();
        Collection<Disease> diseaseCollection = megaMapperConnection.getMegaMapper().getDiseaseByUniprotId(MmDatabase.UniProt, uniprotAccession, MmDatabase.EFO, MmDatabase.OMIM, MmDatabase.MeSH);

        if (diseaseCollection != null) {

            for (Disease disease : diseaseCollection) {


                DiseaseDefaultWrapper diseaseDefaultWrapper = new DiseaseDefaultWrapper(disease);
                related_disease.add(diseaseDefaultWrapper);
                uniqueDiseases.add(diseaseDefaultWrapper);


            }
        }
        return related_disease;

    }

    public Set<Disease> findAllDiseases() {
  
       return megaMapperConnection.getMegaMapper().findAllDiseases(MmDatabase.UniProt, MmDatabase.EFO, MmDatabase.MeSH, MmDatabase.OMIM);
    }
    public List<String> findAllEcNumbers(){
        return megaMapperConnection.getMegaMapper().findEcNumbers();
    }


    public  Collection<CustomXRef> getXrefs(Entry entry) {

        Collection<CustomXRef> xrefs = megaMapperConnection.getMegaMapper().getXrefs_ec_Only(entry, MmDatabase.UniProt);
        
         return xrefs;
        
    }
    
    public Entry findEntryById(String entryId) {
        Entry entry = megaMapperConnection.getMegaMapper().findByEntryId(entryId);
        return entry;
    }

    public SearchResults computeEnzymeSummary(List<String> idPrefixesList, SearchResults searchResults) {

        List<EnzymeSummary> summaryList = new ArrayList<EnzymeSummary>();

        try {


            summaryList = getEnzymeSummaries(idPrefixesList, null);



        } catch (MultiThreadingException ex) {
            LOGGER.error("Error while getting enzymeSummaries", ex);
        }
        searchResults.setSummaryentries(summaryList);
        searchResults.setTotalfound(summaryList.size());

        if (idPrefixesList.size() != summaryList.size()) {
            LOGGER.warn((idPrefixesList.size() - summaryList.size())
                    + " UniProt ID prefixes have been lost");
        }
        LOGGER.debug("Building filters...");
        SearchFilters filters = buildSearchFilters(searchResults);
        searchResults.setSearchfilters(filters);

        LOGGER.debug("Finished search");
        closeResources();
        return searchResults;
    }

    /**
     * Limit the number of results to the
     * {@code IEbeyeAdapter.EP_RESULTS_PER_DOIMAIN_LIMIT}
     *
     * @param params
     */
    public void resetNrOfResultsToLimit(List<ParamOfGetResults> params) {
        for (ParamOfGetResults param : params) {
            resetNrOfResultsToLimit(param);
        }
    }

    public void resetNrOfResultsToLimit(ParamOfGetResults param) {
        int totalFound = param.getTotalFound();
        int maxResults;
        if (param.getDomain().equals(Domains.uniprot.name())) {
            maxResults = ebeyeAdapter.getConfig().getMaxUniprotResults();
        } else if (param.getDomain().equals(Domains.chebi.name())) {
            maxResults = ebeyeAdapter.getConfig().getMaxChebiResults();
        } else {
            maxResults = ebeyeAdapter.getConfig().getMaxResults();
        }
        if (totalFound > maxResults) {
            LOGGER.warn("[CUTOFF] Limiting results for " + param.getDomain()
                    + " from " + totalFound + " to " + maxResults);
            param.setTotalFound(maxResults);
        }
    }

    private List<String> limiteResultList(List<String> resultList, int maxResults) {
        List<String> subList = null;
        if (resultList != null) {
            if (resultList.size() > maxResults) {
                subList = resultList.subList(0, maxResults);
                LOGGER.warn("[CUTOFF] Limiting results from "
                        + resultList.size() + " to " + maxResults);
            } else {
                subList = resultList;
            }
        }
        return subList;
    }

    private List<ParamOfGetResults> getNrOfRecordsRelatedToUniprot()
    throws EnzymeFinderException {
        List<ParamOfGetResults> paramsWithoutNrOfResults =
                prepareGetRelatedRecordsToUniprotQueries(searchParams);
        return ebeyeAdapter.getNumberOfResults(paramsWithoutNrOfResults);
    }

    public ParamOfGetResults getUniprotNrOfRecords() {
        //prepare list of parameters
        ParamOfGetResults param = prepareGetUniprotIdQueries(searchParams);
        ebeyeAdapter.getNumberOfResults(param);
        //resetNrOfResultsToLimit(param);
        return param;
    }

    public List<ParamOfGetResults> prepareParamsForQueryIN(
            String domain, List<String> queries, List<String> resultFields) {
        List<ParamOfGetResults> paramList = new ArrayList<ParamOfGetResults>();
        for (String query : queries) {
            ParamOfGetResults param = new ParamOfGetResults(
                    domain, query, resultFields);
            paramList.add(param);
        }
        return paramList;
    }

    /**
     * Filters UniProt accessions which are enzymes and returns their IDs.
     *
     * @param accs UniProt <i>accessions</i>
     * @return a list of UniProt <i>IDs</i> of only those which are enzymes.
     * @throws MultiThreadingException
     */
    private List<String> filterEnzymes(List<String> accs)
            throws MultiThreadingException {
        String queryField = IEbeyeAdapter.FieldsOfGetResults.acc.name();
        List<String> resultFields = new ArrayList<String>();

        resultFields.add(IEbeyeAdapter.FieldsOfGetResults.id.name());

        //Enzyme filter must be added
        List<String> queries = LuceneQueryBuilder.createQueriesIn(
                queryField, accs, false,
                ebeyeAdapter.getConfig().getMaxAccessionsInQuery());
        List<ParamOfGetResults> paramList = this.prepareParamsForQueryIN(
                Domains.uniprot.name(), queries,
                resultFields);
        ebeyeAdapter.getNumberOfResults(paramList);
        int otherDomainsNrOfResults = calTotalResultsFound(paramList);
        List<String> results = new ArrayList<String>();
        if (otherDomainsNrOfResults > 0) {
            results.addAll(ebeyeAdapter.getValueOfFields(paramList)); // FIXME: VARIABLE #RESULTS
        }
        return results;
    }

    /**
     * Queries EB-Eye for UniProt IDs from domains other than UniProt or ChEBI.
     * The returned accessions are filtered for enzymes, whose UniProt IDs are
     * stored in the
     * <code>uniprotEnzymeIds</code> field.
     *
     * @throws EnzymeFinderException
     */
    private void queryEbeyeOtherDomainForIds() throws EnzymeFinderException {
        List<ParamOfGetResults> nrOfResultParams = this.getNrOfRecordsRelatedToUniprot();
        LOGGER.debug("Other domains, phase A: " + nrOfResultParams.size());
        resetNrOfResultsToLimit(nrOfResultParams);
        LOGGER.debug("Other domains, phase B: " + nrOfResultParams.size());
        // Retrieve accessions referencing uniprot:
        List<String> relatedUniprotAccessionList =
                ebeyeAdapter.getRelatedUniprotAccessionSet(nrOfResultParams);
        LOGGER.debug("Other domains, phase C: " + relatedUniprotAccessionList.size());
        if (relatedUniprotAccessionList.size() > 0) {
            List<String> limitedResults = this.limiteResultList(
                    relatedUniprotAccessionList,
                    ebeyeAdapter.getConfig().getMaxUniprotResultsFromOtherDomains());
            LOGGER.debug("Other domains, phase D: " + limitedResults.size());
            List<String> uniprotIdsRefFromOtherDomains =
                    filterEnzymes(limitedResults);
            LOGGER.debug("SEARCH after filterEnzymes, results: " + uniprotIdsRefFromOtherDomains.size());
            if (uniprotIdsRefFromOtherDomains != null) {
                uniprotEnzymeIds.addAll(uniprotIdsRefFromOtherDomains);
            }
        }
    }

    /**
     * Queries EB-Eye for UniProt IDs corresponding to enzymes, and adds them to
     * the uniprotEnzymeIds field.
     *
     * @throws EnzymeFinderException
     */
    private void queryEbeyeForUniprotIds() throws EnzymeFinderException {
        ParamOfGetResults uniprotNrOfResultParams = this.getUniprotNrOfRecords();
        resetNrOfResultsToLimit(uniprotNrOfResultParams);
        int uniprotResultSize = uniprotNrOfResultParams.getTotalFound();
        //UniProt results are ranked in the first place.
        if (uniprotResultSize > 0) {
            uniprotEnzymeIds.addAll(ebeyeAdapter.getValueOfFields(uniprotNrOfResultParams));
        }
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
        //List<String> speciesFilter = searchParams.getSpecies();

        /*
         * There are 2 cases to treat the search as new search: case 1 - the new
         * text is different from the previous text case 2 - all filters are
         * empty
         */
        if (!previousText.equalsIgnoreCase(currentText)
                || (compoundFilter.size() == 0 && speciesFilter.size() == 0)) {
            newSearch = true;
            searchParams.getSpecies().clear();
            searchParams.getCompounds().clear();
        }
    }
    
//this is the part that takes ages.....
    public List<EnzymeSummary> getEnzymesFromUniprotAPI(
            List<String> resultSubList1, List<String> paramList)
            throws MultiThreadingException {
       
        List<String> resultSubList = resultSubList1;
        uniprotAdapter.setMmConnection(megaMapperConnection.getConnection());
        List<EnzymeSummary> enzymeList = uniprotAdapter.getEnzymesByIdPrefixes(resultSubList, IUniprotAdapter.DEFAULT_SPECIES, speciesFilter);
        return enzymeList;
    }
    /**
     * Retrieves full enzyme summaries.
     *
     * @param uniprotIdPrefixes a list of UniProt ID prefixes.
     * @return a list of enzyme summaries ready to show in a result list.
     * @throws MultiThreadingException problem getting the original summaries,
     * or creating a processor to add synonyms to them.
     */

    private List<EnzymeSummary> getEnzymeSummaries(
            List<String> uniprotIdPrefixes, List<String> paramList)
            throws MultiThreadingException {
      List<EnzymeSummary> summaries = getEnzymesFromUniprotAPI(uniprotIdPrefixes, paramList);
        if (summaries != null && !summaries.isEmpty()) {
            EnzymeSummaryProcessor[] processors = {
                new SynonymsProcessor(summaries, intenzAdapter)
            };
            for (EnzymeSummary summary : summaries) {
                for (EnzymeSummaryProcessor processor : processors) {
                    processor.process(summary);
        }
            }
        }
        return summaries;
    }

    public static int calTotalResultsFound(
            List<ParamOfGetResults> resultList) {
        if (resultList == null) {
            return 0;
        }
        int counter = 0;
        for (ParamOfGetResults param : resultList) {
            counter = counter + param.getTotalFound();
        }
        return counter;
    }

    /**
     * Prepare field queries for the UNIPROT pseudo-field, or the id field if
     * the pseudo-field is not available in the domain.
     *
     * @param searchParams
     * @return parameters to search any field and get the pseudo-field UNIPROT
     *      - or, if not available, the id field - in all
     *      {@link uk.ac.ebi.ep.adapter.ebeye.Domains domains} except uniprot.
     * @see {@link EbeyeAdapter#getRelatedUniprotAccessionSet(java.util.List)}
     */
    private List<ParamOfGetResults> prepareGetRelatedRecordsToUniprotQueries(
            SearchParams searchParams) {
        List<ParamOfGetResults> paramList = new ArrayList<ParamOfGetResults>();
        for (Domains domain : Domains.values()) {
            final String[] searchFields = ebeyeAdapter.getConfig()
                    .getSearchFields(domain.name());
            String query;
            if (searchFields == null){
                query = searchParams.getText();
            } else {
                query = LuceneQueryBuilder.createFieldsQuery(
                        Arrays.asList(searchFields), searchParams.getText());
            }
            switch (domain){
                case uniprot:
                    break;
                case efo:
                case omim:
                case mesh:
                    // these don't have a UNIPROT pseudo-field
                    paramList.add(new ParamOfGetResults(
                            domain.name(),
                            query,
                            Collections.singletonList(
                                FieldsOfGetResults.id.name())));
                    break;
                default:
                    paramList.add(new ParamOfGetResults(
                            domain.name().replace('_', '-'),
                            query,
                            Collections.singletonList(
                                IEbeyeAdapter.UNIPROT_REF_FIELD)));
            }
        }
        return paramList;
    }

    private ParamOfGetResults prepareGetUniprotIdQueries(
            SearchParams searchParams) {
        final String[] searchFields = ebeyeAdapter.getConfig()
                .getSearchFields(Domains.uniprot.name());
        String query;
        if (searchFields == null){
            query = LuceneQueryBuilder.createQueryWithEnzymeFilter(
                    searchParams.getText());
        } else {
            query = LuceneQueryBuilder.createFieldsQueryWithEnzymeFilter(
                    Arrays.asList(searchFields), searchParams.getText());
        }
        return new ParamOfGetResults(Domains.uniprot.name(),
                query,
                Collections.singletonList(FieldsOfUniprotNameMap.id.name()));
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
        Map<String, Hsp> scorings = new HashMap<String, Hsp>();

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
            Collections.sort(es.getRelatedspecies(),
                    new Comparator<EnzymeAccession>() {
                        @SuppressWarnings({"unchecked", "rawtypes"})
                        public int compare(EnzymeAccession o1, EnzymeAccession o2) {
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

                        }
                    });

        }
        //List<String> uniprotIdPrefixes = filterBlastResults(hits);
        //enzymeSummaryList = getEnzymeSummaries(uniprotIdPrefixes, null);
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
        List<String> accs = new ArrayList<String>();
        for (Hit hit : hits) {
            accs.add(hit.getUniprotAccession());
        }
        return EPUtil.getIdPrefixes(filterEnzymes(accs));
    }

    /**
     * Retrieves search results for a given compound ID.
     * @param searchParams The search parameters, including
     *      <code>type=COMPOUND</code> and a compound ID as <code>text</code>.
     * @return Search results (including summaries).
     * @throws EnzymeFinderException
     * @since 1.0.22
     */
    public SearchResults getEnzymesByCompound(SearchParams searchParams)
    throws EnzymeFinderException {
        List<String> uniprotIds = megaMapperConnection.getMegaMapper()
                .getEnzymesByCompound(searchParams.getText());
        return getSearchResults(uniprotIds);
    }
}
