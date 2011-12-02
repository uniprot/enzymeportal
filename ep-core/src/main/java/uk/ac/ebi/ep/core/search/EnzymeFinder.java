package uk.ac.ebi.ep.core.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import uk.ac.ebi.biobabel.lucene.LuceneParser;
import uk.ac.ebi.ep.adapter.ebeye.EbeyeAdapter;
import uk.ac.ebi.ep.adapter.ebeye.IEbeyeAdapter;
import uk.ac.ebi.ep.adapter.ebeye.param.ParamOfGetResults;
import uk.ac.ebi.ep.adapter.intenz.IintenzAdapter;
import uk.ac.ebi.ep.adapter.intenz.IntenzAdapter;
import uk.ac.ebi.ep.adapter.uniprot.IUniprotAdapter;
import uk.ac.ebi.ep.adapter.uniprot.UniprotJapiAdapter;
import uk.ac.ebi.ep.adapter.uniprot.UniprotWsAdapter;
import uk.ac.ebi.ep.config.Domain;
import uk.ac.ebi.ep.core.CompoundDefaultWrapper;
import uk.ac.ebi.ep.core.DiseaseDefaultWrapper;
import uk.ac.ebi.ep.core.SpeciesDefaultWrapper;
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
import uk.ac.ebi.util.result.DataTypeConverter;


/**
 * NOTE: the adapters must be configured before using this finder!
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class EnzymeFinder implements IEnzymeFinder {

//********************************* VARIABLES ********************************//
    private static final Logger LOGGER = Logger.getLogger(EnzymeFinder.class);
    
    IEbeyeAdapter ebeyeAdapter;
    IUniprotAdapter uniprotAdapter; 
    IintenzAdapter intenzAdapter;

    protected SearchParams searchParams;
    SearchResults enzymeSearchResults;
    List<String> uniprotEnzymeIds;
    Map<String,Collection<Species>> uniprotIds2species;
    boolean newSearch;
    Map<String,List<String>> chebiResults;
    List<String> chebiIds;
    Set<String> uniprotIdPrefixSet;
    List<String> speciesFilter;
    List<String> compoundFilter;
    List<EnzymeSummary> enzymeSummaryList;

//******************************** CONSTRUCTORS ******************************//

    public EnzymeFinder(Config config) {
        enzymeSearchResults = new SearchResults();
        ebeyeAdapter = new EbeyeAdapter();
        uniprotEnzymeIds = new ArrayList<String>();
        uniprotIdPrefixSet = new LinkedHashSet<String>();
        enzymeSummaryList = new ArrayList<EnzymeSummary>();
        intenzAdapter = new IntenzAdapter();
        switch (config.uniprotImplementation) {
		case JAPI:
	        uniprotAdapter = new UniprotJapiAdapter();
			break;
		case WS:
			uniprotAdapter = new UniprotWsAdapter();
			break;
		}
    }


//****************************** GETTER & SETTER *****************************//
    public SearchParams getSearchParams() {
        return searchParams;
    }

    public void setSearchParams(SearchParams searchParams) {
        this.searchParams = searchParams;
    }

    public List<String> getCompoundFilter() {
        return compoundFilter;
    }

    public void setCompoundFilter(List<String> compoundFilter) {
        this.compoundFilter = compoundFilter;
    }


    public List<String> getChebiIds() {
        return chebiIds;
    }

    public void setChebiIds(List<String> chebiIds) {
        this.chebiIds = chebiIds;
    }

    public Map<String, List<String>> getChebiResults() {
        return chebiResults;
    }

    public void setChebiResults(Map<String, List<String>> chebiResults) {
        this.chebiResults = chebiResults;
    }

    public IEbeyeAdapter getEbeyeAdapter() {
        return ebeyeAdapter;
    }

    public void setEbeyeAdapter(IEbeyeAdapter ebeyeAdapter) {
        this.ebeyeAdapter = ebeyeAdapter;
    }

    public SearchResults getEnzymeSearchResults() {
        return enzymeSearchResults;
    }

    public void setEnzymeSearchResults(SearchResults enzymeSearchResults) {
        this.enzymeSearchResults = enzymeSearchResults;
    }

    public List<EnzymeSummary> getEnzymeSummaryList() {
        return enzymeSummaryList;
    }

    public void setEnzymeSummaryList(List<EnzymeSummary> enzymeSummaryList) {
        this.enzymeSummaryList = enzymeSummaryList;
    }

    public IintenzAdapter getIntenzAdapter() {
        return intenzAdapter;
    }

    public void setIntenzAdapter(IintenzAdapter intenzAdapter) {
        this.intenzAdapter = intenzAdapter;
    }

    public boolean isNewSearch() {
        return newSearch;
    }

    public void setNewSearch(boolean newSearch) {
        this.newSearch = newSearch;
    }

    public List<String> getSpeciesFilter() {
        return speciesFilter;
    }

    public void setSpeciesFilter(List<String> speciesFilter) {
        this.speciesFilter = speciesFilter;
    }

    public IUniprotAdapter getUniprotAdapter() {
        return uniprotAdapter;
    }

    public void setUniprotAdapter(IUniprotAdapter uniprotAdapter) {
        this.uniprotAdapter = uniprotAdapter;
    }

    public List<String> getUniprotEnzymeIds() {
        return uniprotEnzymeIds;
    }

    public void setUniprotEnzymeIds(List<String> uniprotEnzymeIds) {
        this.uniprotEnzymeIds = uniprotEnzymeIds;
    }

    public Set<String> getUniprotIdPrefixSet() {
        return uniprotIdPrefixSet;
    }

    public void setUniprotIdPrefixSet(Set<String> uniprotIdPrefixSet) {
        this.uniprotIdPrefixSet = uniprotIdPrefixSet;
    }

    public SearchResults getEnzymes(SearchParams searchParams)
	throws EnzymeFinderException {        
        String userKeywords = new String(searchParams.getText());
        //setting variable values and validation keywords before being cleaned
        processInputs(searchParams);

        /* First time search or when user inserts a new keyword, the filter is reset
         * then the search is performed across all domains without considering the
         * filter.
         */
        if (newSearch) {
            // Search in EBEye for Uniprot ids, the search is filtered by ec:*
            queryEbeyeForUniprotIds();
/* replacing EB-Eye with UniProt ws here:
        	uniprotIds2species = new HashMap<String,Collection<Species>>();
    		final List<String> uniprotAccessions =
    				uniprotAdapter.getUniprotAccessions(searchParams.getText());
    		if (uniprotAccessions != null){
    			final Map<String, Collection<Species>> idsAndSpecies =
    					uniprotAdapter.getAccessionsAndSpecies(uniprotAccessions);
				if (idsAndSpecies != null) uniprotIds2species.putAll(idsAndSpecies);
    		}
*/
            LOGGER.debug("UniProt IDs from UniProt: " + uniprotEnzymeIds.size());
            
            /* Search in Ebeye for Uniprot ids that are referenced in Chebi domain
             * This search has to be performed separately, because the results
             * must contain Chebi ids to show in the Compound search filter. */
            queryEbeyeChebiForUniprotIds();
            LOGGER.debug("UniProt IDs from UniProt+ChEBI: " + uniprotEnzymeIds.size());
            /* Search in Intenz, Rhea, Reactome, PDBe for Uniprot ids. 
             * TODO: Process Intenz separately might improve the performance. */
            queryEbeyeOtherDomainForIds();
            LOGGER.debug("UniProt IDs from UniProt+ChEBI+others: " + uniprotEnzymeIds.size());
            uniprotIdPrefixSet.addAll(EPUtil.getIdPrefixes(uniprotEnzymeIds));
            chebiIds = new ArrayList<String>(chebiResults.keySet());
// NO FILTERING HERE!
//        } else { //Search with filters
//	            int speciesFilterSize = speciesFilter.size();
//	            List<String> uniprotIdPrefixesFromChebi = new ArrayList<String>();
//            if (compoundFilter.size() > 0) { // compound is selected
//                if (speciesFilterSize > 0) { //combined filters
//                    queryEbeyeForUniprotIds();
//                    //queryEbeyeChebiForUniprotIds();
//                    queryEbeyeOtherDomainForIds();
//
//                    //Only search in chebi ???????????????????? because even though other domains have results
//                    //only those available in chebi are shown
//
//                    //filter chebi results by species
//                    List<String> unfilteredIdPrefixes =
//                    		new ArrayList<String>(EPUtil.getIdPrefixes(uniprotEnzymeIds));
//
//                    Set<String> uniprotFilteredIdPrefixes = new LinkedHashSet<String>();
//                    uniprotFilteredIdPrefixes.addAll(
//                            filterUniprotIdPrefixesBySpecies(unfilteredIdPrefixes, speciesFilter));
//
//                    filterCompoundBySpecies(uniprotFilteredIdPrefixes);
//
//                } else { //species is not selected and compound is selected
//	                //When there is filter the query is created using the id only
//	                queryEbeyeChebiForUniprotIds();
//	                uniprotIdPrefixesFromChebi.addAll(EPUtil.getIdPrefixes(this.uniprotEnzymeIds));
//	                uniprotIdPrefixSet.addAll(uniprotIdPrefixesFromChebi);
//	                chebiIds = new ArrayList<String>(chebiResults.keySet());
//                }
//            } else { // compound is not selected
//                if (speciesFilterSize > 0) {
//                    queryEbeyeForUniprotIds();
//                    //queryEbeyeChebiForUniprotIds();
//                    queryEbeyeOtherDomainForIds();
//
//                    //How to filter chebi uniprot ids?
//                    List<String> unFilteredIdPrefixes = EPUtil.getIdPrefixes(this.uniprotEnzymeIds);
//
//                    Set<String> uniprotFilteredIdPrefixes = new LinkedHashSet<String>();
//
//                    uniprotFilteredIdPrefixes.addAll(
//                             filterUniprotIdPrefixesBySpecies(unFilteredIdPrefixes, speciesFilter));
//
//                    filterCompoundBySpecies(uniprotFilteredIdPrefixes);
//                }
//            }
          }
        
        List<String> idPrefixesList =
        		new ArrayList<String>(uniprotIdPrefixSet);
//		THIS PSEUDO-PAGINATION HAS MOVED OUT!
//      int totalFound = uniprotIdPrefixSet.size();
//      int size = this.searchParams.getSize();
//      int start =  this.searchParams.getStart();
//      int end = start+size;
//      if (totalFound < end) {
//          end = totalFound;
//      }
//    enzymeSearchResults.setTotalfound(totalFound);
//        List<String> resultSubList = idPrefixesList.subList(
//               start, end);

        enzymeSearchResults.setTotalfound(uniprotIdPrefixSet.size());
        enzymeSummaryList = getEnzymeSummaries(idPrefixesList);
        enzymeSearchResults.setSummaryentries(enzymeSummaryList);
//
//        searchParams.setStart(start);
//        searchParams.setText(userKeywords);
//        searchParams.setPrevioustext(userKeywords);

        // Filters: FIXME: these have to be built from the summaryEntries
//        SearchFilters filters = new SearchFilters();
//        enzymeSearchResults.setSearchfilters(filters);
//        filters.setCompounds(newCompoundFilter(chebiIds));
//        filters.setSpecies(newSpeciesFilter(uniprotAdapter.getSpecies(uniprotIdPrefixSet)));
        buildFilters(enzymeSearchResults);

        return enzymeSearchResults;
    }


    /**
     * Builds filters - species, compounds, diseases - from a result list.
     * @param searchResults the result list, which will be modified by setting
     * the relevant filters.
     */
    private void buildFilters(SearchResults searchResults) {
    	Set<SpeciesDefaultWrapper> uniqueSpecies = new TreeSet<SpeciesDefaultWrapper>();
    	Set<CompoundDefaultWrapper> uniqueCompounds = new TreeSet<CompoundDefaultWrapper>();
    	Set<DiseaseDefaultWrapper> uniqueDiseases = new TreeSet<DiseaseDefaultWrapper>();
		for (EnzymeSummary summaryEntry : searchResults.getSummaryentries()) {
			for (EnzymeAccession ea : summaryEntry.getRelatedspecies()) {
				Species sp = ea.getSpecies();
				if (sp != null) uniqueSpecies.add(new SpeciesDefaultWrapper(sp));
				List<Compound> compounds = ea.getCompounds();
				if (compounds != null){
					for (Compound compound : compounds) {
						uniqueCompounds.add(new CompoundDefaultWrapper(compound));
					}
				}
				List<Disease> diseases = ea.getDiseases();
				if (diseases != null){
					for (Disease disease : diseases) {
						uniqueDiseases.add(new DiseaseDefaultWrapper(disease));
					}
				}
			}
		}
		List<Species> speciesFilter = new ArrayList<Species>();
		for (SpeciesDefaultWrapper sw : uniqueSpecies) {
			speciesFilter.add(sw.getSpecies());
		}
		List<Compound> compoundFilter = new ArrayList<Compound>();
		for (CompoundDefaultWrapper cw : uniqueCompounds) {
			compoundFilter.add(cw.getCompound());
		}
		List<Disease> diseaseFilter = new ArrayList<Disease>();
		for (DiseaseDefaultWrapper dw : uniqueDiseases) {
			diseaseFilter.add(dw.getDisease());
		}
    	SearchFilters filters = new SearchFilters();
    	filters.setSpecies(speciesFilter);
    	filters.setCompounds(compoundFilter);
    	filters.setDiseases(diseaseFilter);
    	searchResults.setSearchfilters(filters);
	}


	/**
     * Limite the number of results to the {@code IEbeyeAdapter.EP_RESULTS_PER_DOIMAIN_LIMIT}
     * 
     * @param params
     */
    public void resetNrOfResultsToLimit(List<ParamOfGetResults> params) {
        for (ParamOfGetResults param:params) {
            resetNrOfResultsToLimit(param);
        }
    }

	public void resetNrOfResultsToLimit(ParamOfGetResults param) {
		int totalFound = param.getTotalFound();
		int maxResults;
		if (param.getDomain().equals(IEbeyeAdapter.Domains.uniprot.name())) {
			maxResults = ebeyeAdapter.getConfig().getMaxUniprotResults();
		} else if (param.getDomain().equals(IEbeyeAdapter.Domains.chebi.name())) {
			maxResults = ebeyeAdapter.getConfig().getMaxChebiResults();
		} else {
			maxResults = ebeyeAdapter.getConfig().getMaxResults();
		}
		if (totalFound > maxResults) {
			LOGGER.warn("[CUTOFF] Limiting results for " + param.getDomain() +
					" from " + totalFound + " to " + maxResults);
			param.setTotalFound(maxResults);
		}
	}

    public List<String> limiteResultList(List<String> resultList, int maxResults) {
        List<String> subList = null;
        if (resultList != null) {
            if (resultList.size() > maxResults){
                subList = resultList.subList(0, maxResults);
                LOGGER.warn("[CUTOFF] Limiting results from "
                		+ resultList.size() + " to " + maxResults);
            } else {
                subList = resultList;
            }
        }
        return subList;
    }

    public List<ParamOfGetResults> getNrOfRecordsRelatedToUniprot() throws EnzymeFinderException {

        //prepare list of parameters
        List<ParamOfGetResults> paramsWithoutNrOfResults =
                                    prepareGetRelatedRecordsToUniprotQueries(searchParams);
       List<ParamOfGetResults> paramsWithNrOfResults = new ArrayList<ParamOfGetResults>();
        try {
            //List<List<Result>> allDomainsResults = null;
            //get and set the value at the same time
            paramsWithNrOfResults.addAll(ebeyeAdapter.getNumberOfResults(paramsWithoutNrOfResults));

            //resetNrOfResultsToLimit(paramsWithNrOfResults);

        } catch (MultiThreadingException ex) {
            //java.util.logging.Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
       return paramsWithoutNrOfResults;
    }

    public ParamOfGetResults getUniprotNrOfRecords() throws EnzymeFinderException {
        //Uniprot number of result
        //IEbeyeAdapter ebeyeAdapter = new EbeyeAdapter();
        //prepare list of parameters
        ParamOfGetResults param =prepareGetUniprotIdQueries(searchParams);
        ebeyeAdapter.getNumberOfResults(param);
        //resetNrOfResultsToLimit(param);
       return param;
    }

    public ParamOfGetResults getChebiNrOfRecords() throws EnzymeFinderException {
        //Uniprot number of result
        //IEbeyeAdapter ebeyeAdapter = new EbeyeAdapter();
        //prepare list of parameters
        ParamOfGetResults param =prepareChebiQueries();
        ebeyeAdapter.getNumberOfResults(param);
        //resetNrOfResultsToLimit(param);
       return param;
    }

    /**
     * Builds lucene queries for the given UniProt IDs with enzyme filter and
     * species filter.
     * @param ids UniProt IDs
     * @param selectedSpecies the species we want
     * @param maxAccessionsInQuery max number of accessions to pass in one go
     * 		(if we have more than that, they are split and queried in chunks).
     * @return a list of lucene queries.
     */
    private static List<String> createIdWildcardQueriesWithSpeciesFilter (
                    List<String> ids, List<String> selectedSpecies,
                    int maxAccessionsInQuery)  {
        List<String> queries = LuceneQueryBuilder.createEbeyeQueriesIn(
                IEbeyeAdapter.FieldsOfGetResults.id.name(),
                ids, true, maxAccessionsInQuery);
        List<String> joinedQueries = LuceneQueryBuilder.addFilterQueriesAND(
                queries, IEbeyeAdapter.EBEYE_SPECIES_FIELD, selectedSpecies);
        return joinedQueries;
    }

    public List<ParamOfGetResults> prepareParamsForQueryIN(
            String domain, List<String> queries, List<String> resultFields) {
        List<ParamOfGetResults> paramList = new ArrayList<ParamOfGetResults>();       
        for (String query: queries) {
            ParamOfGetResults param = new ParamOfGetResults(
            domain, query, resultFields);
            paramList.add(param);
        }
        return paramList;
    }

      /**
       * Filters UniProt accessions which are enzymes and returns their IDs.
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
                IEbeyeAdapter.Domains.uniprot.name(), queries,
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
     * Queries EB-Eye for ChEBI IDs, and their corresponding UniProt accessions.
     * The result is stored in the <code>chebiResults</code> field, and those
     * UniProt accessions which are enzymes are added to the
     * <code>uniprotEnzymeIds</code> field.
     * @throws EnzymeFinderException
     */
    private void queryEbeyeChebiForUniprotIds() throws EnzymeFinderException {
        //Chebi has to be processed separately due to the filter
        ParamOfGetResults chebiParam = this.getChebiNrOfRecords();
        resetNrOfResultsToLimit(chebiParam);
        chebiResults = ebeyeAdapter.getUniprotRefAccesionsMap(chebiParam);
        Collection<List<String>> chebiAccs = chebiResults.values();
        if (chebiAccs.size() > 0){
            Set<String> uniprotAccsFromChebiSet =
            		DataTypeConverter.mergeAndLimitResult(chebiAccs,
            				ebeyeAdapter.getConfig().getMaxUniprotResultsFromChebi());
        	// Filter those which are enzymes:
        	List<String> uniprotIdsRefFromChebi =
        			filterEnzymes(new ArrayList<String>(uniprotAccsFromChebiSet));
        	uniprotEnzymeIds.addAll(uniprotIdsRefFromChebi);
/*
        	Map<String, Species> ids2species =
        			uniprotAdapter.getIdsAndSpecies(uniprotAccsFromChebiSet);
        	if (ids2species != null) uniprotIds2species.putAll(ids2species);
*/
        }
    }

    private List<Compound> newCompoundFilter(List<String> chebiIds) {
        Map<String,String> chebiIdNameMap = ebeyeAdapter.getNameMapByAccessions(
                IEbeyeAdapter.Domains.chebi.name(), chebiIds);
        return DataTypeConverter.mapToCompound(chebiIdNameMap);
    }
    
    /**
     * Reduces the species collection by removing redundant ones.<br>
     * This method is required as Species.class is generated from a XML schema
     * and lacks of equals or hashcode methods.
     * @param sps the organisms as returned by the UniProt web service.
     * @see UniprotWsAdapter#
     * @return
     */
    private List<Species> newSpeciesFilter(Collection<Species> sps){
		Map<String, Species> speciesMap = new HashMap<String, Species>();
		for (Species sp : sps) {
			speciesMap.put(sp.getScientificname(), sp);
		}
		return new ArrayList<Species>(speciesMap.values());
	}

    /**
     * Queries EB-Eye for UniProt IDs from domains other than UniProt or ChEBI.
     * The returned accessions are filtered for enzymes, whose UniProt IDs are
     * stored in the <code>uniprotEnzymeIds</code> field.
     * @throws EnzymeFinderException
     */
	private void queryEbeyeOtherDomainForIds() throws EnzymeFinderException {
        List<ParamOfGetResults> nrOfResultParams = this.getNrOfRecordsRelatedToUniprot();
    	LOGGER.debug("Other domains, phase A: " +  nrOfResultParams.size());
        resetNrOfResultsToLimit(nrOfResultParams);
    	LOGGER.debug("Other domains, phase B: " +  nrOfResultParams.size());
        //Retrieve accessions from UNIPROT field
        List<String> relatedUniprotAccessionList =
                ebeyeAdapter.getRelatedUniprotAccessionSet(nrOfResultParams);
    	LOGGER.debug("Other domains, phase C: " +  relatedUniprotAccessionList.size());
        if (relatedUniprotAccessionList.size() > 0){
            List<String> limitedResults = this.limiteResultList(
                    relatedUniprotAccessionList,
                    ebeyeAdapter.getConfig().getMaxUniprotResultsFromOtherDomains());
        	LOGGER.debug("Other domains, phase D: " +  relatedUniprotAccessionList.size());
            List<String> uniprotIdsRefFromOtherDomains =
                    filterEnzymes(limitedResults);
        	LOGGER.debug("SEARCH after filterEnzymes, results: " + uniprotIdsRefFromOtherDomains.size());
            if (uniprotIdsRefFromOtherDomains != null) {
                uniprotEnzymeIds.addAll(uniprotIdsRefFromOtherDomains);
            }
/*
        	Map<String, Species> ids2species = uniprotAdapter.getIdsAndSpecies(limitedResults);
        	if (ids2species != null) uniprotIds2species.putAll(ids2species);
*/
        }
    }
	
	/**
	 * Queries EB-Eye for UniProt IDs corresponding to enzymes, and adds them
	 * to the uniprotEnzymeIds field.
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
     * Filters UniProt IDs by species and gets only their prefix.
     * @param unfilteredIdPrefixes UniProt ID prefixes.
     * @param speciesFilter species we are interested in.
     * @return a list of UniProt ID prefixes.
     * @throws EnzymeFinderException
     */
	private List<String> filterUniprotIdPrefixesBySpecies(
			List<String> unfilteredIdPrefixes, List<String> speciesFilter)
	throws EnzymeFinderException {
		List<String> filteredIdPrefixes = new ArrayList<String>();
		filteredIdPrefixes.addAll(filterUniprotIdBySpecies(
				unfilteredIdPrefixes, speciesFilter));
		return EPUtil.getIdPrefixes(filteredIdPrefixes);
	}

    /**
     * Filters UniProt IDs by species.
     * @param unfilteredIdPrefixes UniProt ID prefixes (without species suffix).
     * @param speciesFilter list of species
     * @return a list of (complete) UniProt IDs corresponding only to the given
     * 		species.
     * @throws EnzymeFinderException
     */
    private List<String> filterUniprotIdBySpecies(List<String> unfilteredIdPrefixes,
    		List<String> speciesFilter)
	throws EnzymeFinderException {        
        Set<String> filteredResults = null;
        //Ignore filters for new search
        if (speciesFilter.size() > 0 && !newSearch) {
	        List<String> resultFields = new ArrayList<String>();
	        resultFields.add(IEbeyeAdapter.FieldsOfGetResults.id.name());
	        List<String> filterQueries =
	        		createIdWildcardQueriesWithSpeciesFilter(
	        				unfilteredIdPrefixes, speciesFilter,
	        				ebeyeAdapter.getConfig().getMaxAccessionsInQuery());
	        List<ParamOfGetResults> filteredNrOfResults =
	                prepareParamsForQueryIN(
	                		IEbeyeAdapter.Domains.uniprot.name(),
	                		filterQueries,
	                		resultFields);
	        ebeyeAdapter.getNumberOfResults(filteredNrOfResults);
	        filteredResults = ebeyeAdapter.getValueOfFields(filteredNrOfResults);
        }
        return new ArrayList<String>(filteredResults);
     }
   
    /**
     * Escapes the keyowrds, validates the filters and sets the global variables
     * to be used in other methods.
     * @param searchParams
     */
    private void processInputs(SearchParams searchParams) {
        this.searchParams = searchParams;
        speciesFilter = searchParams.getSpecies();
        LuceneParser luceneParser = new LuceneParser();
        String cleanedKeywords = luceneParser
                .escapeLuceneSpecialChars(this.searchParams.getText());
        this.searchParams.setText(cleanedKeywords);
        String previousText = searchParams.getPrevioustext();
        String currentText = searchParams.getText();
        compoundFilter = searchParams.getCompounds();
        //List<String> speciesFilter = searchParams.getSpecies();

        /**
         * There are 2 cases to treat the search as new search:
         * case 1 - the new text is different from the previous text
         * case 2 - all filters are empty
         */
        if (!previousText.equalsIgnoreCase(currentText) ||
                (compoundFilter.size() == 0 && speciesFilter.size() == 0)) {
             newSearch = true;
            searchParams.getSpecies().clear();
            searchParams.getCompounds().clear();
        }

    }

    /**
     * Queries EBEye (ChEBI domain) for UniProt IDs, filtering the result by
     * species. If there is no overlap between these results and the passed
     * UniProt ID prefixes, both are merged.
     * @param uniprotFilteredIdPrefixes previous results.
     * @throws EnzymeFinderException
     */
    private void filterCompoundBySpecies(Set<String> uniprotFilteredIdPrefixes)
	throws EnzymeFinderException {
         
        this.uniprotEnzymeIds.clear();
        this.uniprotIdPrefixSet.clear();
        queryEbeyeChebiForUniprotIds();
        List<String> chebiUnfilteredIdPrefixes = EPUtil.getIdPrefixes(uniprotEnzymeIds);
        //workaround does not completely solve non ref from uniprot to chebi problem

        Set<String> chebiFilteredIdPrefixes = new LinkedHashSet<String>(
        		filterUniprotIdPrefixesBySpecies(chebiUnfilteredIdPrefixes, speciesFilter));

        //If chebi

        //uniprotFilteredIdPrefixes.containsAll(chebiIds);

        if (Collections.disjoint(uniprotFilteredIdPrefixes, chebiFilteredIdPrefixes)) {
			uniprotIdPrefixSet.addAll(uniprotFilteredIdPrefixes);
			// XXX: no chebiFilteredIdPrefixes????
			chebiIds = new ArrayList<String>(); // XXX: no ChEBI IDs????
		} else {
			uniprotIdPrefixSet.addAll(chebiFilteredIdPrefixes);
			// XXX: no uniprotFilteredIdPrefixes???? even if slight overlap????
			if (chebiResults.size() > 0) {
				chebiIds = new ArrayList<String>(chebiResults.keySet());
				// To avoid showing compounds that are not for the selected
				// species
				// chebiIds.retainAll(uniprotFilteredIdPrefixes);
			}
		}
    }

    public List<EnzymeSummary> getEnzymesFromUniprotAPI(List<String> resultSubList)
	throws MultiThreadingException {
        List<EnzymeSummary> enzymeList =
        		uniprotAdapter.getEnzymesByIdPrefixes(resultSubList,
        				IUniprotAdapter.DEFAULT_SPECIES, speciesFilter);
        return enzymeList;
    }

    /**
     * Retrieves full enzyme summaries.
     * @param uniprotIdPrefixes a list of UniProt ID prefixes.
     * @return a list of enzyme summaries ready to show in a result list.
     * @throws MultiThreadingException
     */
    private List<EnzymeSummary> getEnzymeSummaries(List<String> uniprotIdPrefixes)
	throws MultiThreadingException {
        List<EnzymeSummary> enzymeList = getEnzymesFromUniprotAPI(uniprotIdPrefixes);
        if (enzymeList != null) {
            addIntenzSynonyms(enzymeList);
        }
        return enzymeList;
    }

    /*
    public List<ParamOfGetResults> prepareQueryForPdbeAccs(Collection<String> pdbeAccs) {
        List<ParamOfGetResults> params = new ArrayList<ParamOfGetResults>();
        for (String ec: pdbeAccs) {
            List<String> field = new ArrayList<String>();
            field.add(IEbeyeAdapter.FieldsOfGetResults.acc.name());
            String query = LuceneQueryBuilder
                    .createFieldValueQuery(
                            IEbeyeAdapter.FieldsOfGetResults.acc.name(),
                            ec);
            ParamOfGetResults pdbeParam = new ParamOfGetResults(
                  IEbeyeAdapter.Domains.pdbe.name(), query, field);
            params.add(pdbeParam);
        }
        return params;
    }
*/
    public static int calTotalResultsFound(
            List<ParamOfGetResults> resultList) {
        if (resultList ==  null) {
            return 0;
        }
        int counter = 0;
        for (ParamOfGetResults param:resultList){
            counter = counter + param.getTotalFound();
        }
        return counter;
    }
    public void addIntenzSynonyms(
            List<EnzymeSummary> enzymeSummaryList) throws MultiThreadingException {

        Set<String> ecSet  = DataTypeConverter.getUniprotEcs(enzymeSummaryList);
//        LOGGER.debug("SEARCH before intenzAdapter.getSynonyms");
        Map<String,Set<String>> intenzSynonyms = intenzAdapter.getSynonyms(ecSet);
//        LOGGER.debug("SEARCH before enzymeSummary loop, size = " + ecSet.size());
        for (EnzymeSummary enzymeSummary:enzymeSummaryList) {
            List<String> ecList = enzymeSummary.getEc();
            List<String> uniprotSyns = enzymeSummary.getSynonym();
            Set<String> intenzUniqueSyns = new TreeSet<String>();
            for (String ec:ecList) {
                Set<String> ecSynonyms = intenzSynonyms.get(ec);
                if (ecSynonyms != null) {
                    intenzUniqueSyns.addAll(ecSynonyms);
                }
                
            }

            intenzUniqueSyns.addAll(uniprotSyns);

            //Remove the synonyms from uniprot because they have been merged with
            //synonyms from intenz
            enzymeSummary.getSynonym().clear();

            enzymeSummary.getSynonym().addAll(intenzUniqueSyns);
        }
//        LOGGER.debug("SEARCH after  enzymeSummary loop");
    }




/*
    public void setPdbeAccession(Map<String,String> pdbeAccs) {
        for (EnzymeSummary enzymeSummary:enzymeSummaryList) {
            String uniprotId = enzymeSummary.getUniprotid();
            String pdbeAcc = pdbeAccs.get(uniprotId);
            enzymeSummary.getPdbeaccession().add(pdbeAcc);
        }       
    }
*/

    public void setPdbeAccession(Map<String,String> pdbeAccs) {
        for (EnzymeSummary enzymeSummary:enzymeSummaryList) {
            List<String> ecList = enzymeSummary.getEc();
            if (ecList.size() > 0) {
            String ec = enzymeSummary.getEc().get(0);
            String pdbeAcc = pdbeAccs.get(ec);
            if (pdbeAcc != null) {
                enzymeSummary.getPdbeaccession().add(pdbeAcc);
                }
            }
        }
    }

   public List<String> getUniprotIds(List<EnzymeSummary> enzymeSummaryList) {
        List<String> uniprotIds = new ArrayList<String>();
        for (EnzymeSummary enzymeSummary:enzymeSummaryList) {
            String uniprotId = enzymeSummary.getUniprotid();
            uniprotIds.add(uniprotId);
        }
        return uniprotIds;
    }

    public void createSpeciesFilter(SearchResults enzymeSearchResults) {        
        SearchFilters searchFilters = enzymeSearchResults.getSearchfilters();
        List<Species> uniprotSpeciesFilter = new ArrayList<Species>();

        Set<String> speciesNames = new TreeSet<String>();
        for (EnzymeSummary enzymeSummary: enzymeSummaryList) {
            Species species = enzymeSummary.getSpecies();
            String name = species.getCommonname();
            if (name == null || name.equals("")) {
                name = species.getScientificname();
            }
            boolean added = false;
            if (name != null) {
                added = speciesNames.add(name);
            }

            if (added) {
                uniprotSpeciesFilter.add(species);
            }

            List<EnzymeAccession> enzymeAccessions = enzymeSummary.getRelatedspecies();
            for (EnzymeAccession acc: enzymeAccessions) {
                Species relSpecies = acc.getSpecies();
                String relName = relSpecies.getCommonname();
                if (relName == null) {
                    relName = relSpecies.getScientificname();
                }
                boolean relAdded = speciesNames.add(relName);
                if (relAdded) {
                    uniprotSpeciesFilter.add(relSpecies);
                }
            }
        }

        searchFilters.getSpecies().addAll(uniprotSpeciesFilter);
    }

    /**
     * Prepare field queries for all domains except for uniprot and chebi.
     * @param searchParams
     * @return
     */
    public static List<ParamOfGetResults> prepareGetRelatedRecordsToUniprotQueries(
            SearchParams searchParams) {
        List<Domain> domains = Config.domainList;
        List<ParamOfGetResults> paramList = new ArrayList<ParamOfGetResults>();
        for (Domain domain: domains) {
            String domainId = domain.getId();
            if (!domainId.equals(IEbeyeAdapter.Domains.uniprot.name())
                    && !domainId.equals(IEbeyeAdapter.Domains.chebi.name())) {
                List<String> resultFields = new ArrayList<String>();
                resultFields.add(IEbeyeAdapter.UNIPROT_REF_FIELD);
                String query = LuceneQueryBuilder.createFieldsQuery(
                        domain, searchParams);
               ParamOfGetResults paramOfGetAllResults =
                        new ParamOfGetResults(domainId, query, resultFields);
                paramList.add(paramOfGetAllResults);
            }

        }
        return paramList;
    }

	public static ParamOfGetResults prepareGetUniprotIdQueries(
			SearchParams searchParams) {
		Domain domain = Config.getDomain(IEbeyeAdapter.Domains.uniprot.name());
		// List<String> searchFields =
		// DataTypeConverter.getConfigSearchFields(domain);
		List<String> resultFields = new ArrayList<String>();
		resultFields.add(IEbeyeAdapter.FieldsOfUniprotNameMap.id.name());
		String query = LuceneQueryBuilder.createFieldsQueryWithEnzymeFilter(
				domain, searchParams);
		ParamOfGetResults paramOfGetAllResults = new ParamOfGetResults(
				IEbeyeAdapter.Domains.uniprot.name(), query, resultFields);
		return paramOfGetAllResults;
	}

    private ParamOfGetResults prepareChebiQueries() {
        Domain domain = Config.getDomain(IEbeyeAdapter.Domains.chebi.name());
        //List<String> searchFields = DataTypeConverter.getConfigSearchFields(domain);
		List<String> resultFields = new ArrayList<String>();
		//resultFields.add(IEbeyeAdapter.FieldsOfChebiNameMap.name.name());
		resultFields.add(IEbeyeAdapter.FieldsOfChebiNameMap.id.name());
		/* THIS IS TOXIC: EBEye does not store such a field 'UNIPROT' in the chebi domain
		resultFields.add(IEbeyeAdapter.FieldsOfGetResults.UNIPROT.name());
		*/
		//The query has to be created differently if the compound filter is selected
		String query = null;
		List<String> compoundFilter = this.searchParams.getCompounds();
		if (compoundFilter.size() > 0) {
			query = LuceneQueryBuilder.createQueryIN(
					IEbeyeAdapter.FieldsOfChebiNameMap.id.name(), false, compoundFilter);
		} else {
			query = LuceneQueryBuilder.createFieldsQuery(domain, searchParams);
		}
		ParamOfGetResults paramOfGetAllResults =
				new ParamOfGetResults(IEbeyeAdapter.Domains.chebi.name(), query, resultFields);
        return paramOfGetAllResults;
    }
    
}
