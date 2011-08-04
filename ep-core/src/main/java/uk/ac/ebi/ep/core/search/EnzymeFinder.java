package uk.ac.ebi.ep.core.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import uk.ac.ebi.biobabel.lucene.LuceneParser;
import uk.ac.ebi.ep.config.Domain;
import uk.ac.ebi.ep.search.exception.EnzymeFinderException;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.ep.search.model.SearchResults;
import uk.ac.ebi.ep.util.query.LuceneQueryBuilder;
import uk.ac.ebi.ebeye.param.ParamOfGetResults;
import uk.ac.ebi.ep.ebeye.adapter.EbeyeAdapter;
import uk.ac.ebi.ep.ebeye.adapter.IEbeyeAdapter;
import uk.ac.ebi.ep.ebeye.result.Result;
import uk.ac.ebi.ep.intenz.adapter.IintenzAdapter;
import uk.ac.ebi.ep.intenz.adapter.IntenzAdapter;
import uk.ac.ebi.ep.search.model.Compound;
import uk.ac.ebi.ep.search.model.EnzymeAccession;
import uk.ac.ebi.ep.search.model.EnzymeSummary;
import uk.ac.ebi.ep.search.model.SearchFilters;
import uk.ac.ebi.ep.search.model.SearchParams;
import uk.ac.ebi.ep.search.model.Species;
import uk.ac.ebi.ep.uniprot.adapter.IUniprotAdapter;
import uk.ac.ebi.ep.uniprot.adapter.UniprotAdapter;
import uk.ac.ebi.util.result.DataTypeConverter;


/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class EnzymeFinder implements IEnzymeFinder {

//********************************* VARIABLES ********************************//
    protected SearchParams searchParams;
    SearchResults enzymeSearchResults;
    IEbeyeAdapter ebeyeAdapter;
    List<String> uniprotEnzymeIds;
    boolean newSearch;
    //This could be an ArrayList, because the prefix id List will eliminate the duplications
    


    private static Logger log = Logger.getLogger(EnzymeFinder.class);

//******************************** CONSTRUCTORS ******************************//

    public EnzymeFinder() {
        enzymeSearchResults = new SearchResults();
        ebeyeAdapter = new EbeyeAdapter();
        uniprotEnzymeIds = new ArrayList<String>();

    }


//****************************** GETTER & SETTER *****************************//
    public SearchParams getSearchParams() {
        return searchParams;
    }

    public void setSearchParams(SearchParams searchParams) {
        this.searchParams = searchParams;
    }


//********************************** METHODS *********************************//

    public List<String> rankEnzymes(List<String> uniprotIdList) {
        ResultRanker resultRanker = new ResultRanker(uniprotIdList);
        resultRanker.rankResults();
        List<String> rankedResults = resultRanker.getRankedResults();
        return rankedResults;
    }

    public Set<String> getUniprotPrimaAccList(
            List<Result> cachedResults, List<String> ids) {
        Set<String> accessions = new TreeSet<String>();
        for (String id:ids) {
            for (Result result:cachedResults) {
                String selectedId = result.getId();
                if (selectedId.equals(id)) {
                    accessions.add(result.getAcc().get(0));
                    break;
                }
            }
        }
        return accessions;
    }

    public ParamOfGetResults getDomainParam(List<ParamOfGetResults> params, String domain) {
        for (ParamOfGetResults param:params) {
            if (param.getDomain().equals(domain)) {
                return param;
            }
        }
        return null;
    }

    public List<ParamOfGetResults> getNrOfRecordsRelatedToUniprot() throws EnzymeFinderException {
        //Uniprot number of result
        //IEbeyeAdapter ebeyeAdapter = new EbeyeAdapter();

        //prepare list of parameters
        List<ParamOfGetResults> paramsWithoutNrOfResults =
                                    prepareGetRelatedRecordsToUniprotQueries(searchParams);
       List<ParamOfGetResults> paramsWithNrOfResults = new ArrayList<ParamOfGetResults>();
        try {
            //List<List<Result>> allDomainsResults = null;
            //get and set the value at the same time
            paramsWithNrOfResults.addAll(ebeyeAdapter.getNumberOfResults(paramsWithoutNrOfResults));
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
       return param;
    }

    public ParamOfGetResults getChebiNrOfRecords() throws EnzymeFinderException {
        //Uniprot number of result
        //IEbeyeAdapter ebeyeAdapter = new EbeyeAdapter();
        //prepare list of parameters
        ParamOfGetResults param =prepareChebiQueries();
        ebeyeAdapter.getNumberOfResults(param);
       return param;
    }

    /**
     * Create a list of {@link ParamOfGetResults} which contains the queries of the
     * EBeye getResult method. The query is a Lucene query IN of a list of values
     * of a field, eg.: id or name. If the values list is too long then it is
     * broken down into smaller lists and a query is create for each smaller list.
     * @param domain
     * @param queryField
     * @param wildcards
     * @param fieldValues
     * @return
     * @throws EnzymeFinderException
     */
    /*
    public List<ParamOfGetResults> createSubQueries(
            String domain, String queryField, boolean wildcards, List<String> fieldValues) throws EnzymeFinderException {
        List<ParamOfGetResults> paramList = new ArrayList<ParamOfGetResults>();
        int listSize = fieldValues.size();
        int endIndex = 0;
        //Work around to solve big result set issue
        Pagination pagination = new Pagination(listSize, IEbeyeAdapter.EBEYE_NR_OF_QUERY_IN_LIMIT);
        int nrOfQueries = pagination.calTotalPages();
        int start = 0;
        //TODO
        for (int i = 0; i < nrOfQueries; i++) {            
            if (i == (nrOfQueries - 1) && (listSize % IEbeyeAdapter.EBEYE_NR_OF_QUERY_IN_LIMIT) > 0) {
                endIndex = endIndex + pagination.getLastPageResults();
            } else {
                endIndex = endIndex + IEbeyeAdapter.EBEYE_NR_OF_QUERY_IN_LIMIT;
            }

            List<String> subList = fieldValues.subList(start, endIndex);
            
             String query = LuceneQueryBuilder.createQueryIN(queryField, false, subList);
            if (this.searchParams.getSpecies().size() > 0) {
                String simple = LuceneQueryBuilder.createQueryIN(queryField, true, subList);
                query = LuceneQueryBuilder.addSpeciesFilterQuery(simple, 
                        IEbeyeAdapter.EBEYE_SPECIES_FIELD, this.searchParams.getSpecies());
                
            }
            ParamOfGetResults param = createParamOfGetResults(domain, query, subList);
            paramList.add(param);
            start = endIndex;
        }
        return paramList;
    }
*/
    public static List<String> createIdWildcardQueriesWithSpeciesFilter (
                    List<String> ids, List<String> seletedSpecies)  {
        String fieldName = IEbeyeAdapter.FieldsOfGetResults.id.name();        
        boolean wildcard = true;
        int idListSizeLimit = EbeyeAdapter.EBEYE_NR_OF_QUERY_IN_LIMIT;
        String filterFieldName = EbeyeAdapter.EBEYE_SPECIES_FIELD;
        List<String> queries = LuceneQueryBuilder.createQueriesIn(
                fieldName, ids, wildcard, idListSizeLimit);
        //List<String> queriesWithFilter = new ArrayList<String>();
        //String filterQuery = LuceneQueryBuilder.createQueryIN(filterFieldName, false, filterValues);
        List<String> joinedQueries = LuceneQueryBuilder.addFilterQueriesAND(
                queries, filterFieldName, seletedSpecies);
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


    public List<ParamOfGetResults> prepareParamsForQueryInWithFilter(
            String domain, String queryField, List<String> fieldValues, boolean wildcard) throws EnzymeFinderException {
        List<ParamOfGetResults> paramList = new ArrayList<ParamOfGetResults>();
        List<List<String>> subLists = DataTypeConverter
                .createSubLists(fieldValues, IEbeyeAdapter.EBEYE_NR_OF_QUERY_IN_LIMIT);

        for (List<String> subList: subLists) {
            String query = LuceneQueryBuilder.createQueryIN(queryField, wildcard, subList);
            ParamOfGetResults param = createParamOfGetResults(domain, query, subList);
            paramList.add(param);

        }
        return paramList;
    }



    public ParamOfGetResults getNrOfRecords(String domain, Collection<String> accessions) throws EnzymeFinderException {
        //Uniprot number of result
        //IEbeyeAdapter ebeyeAdapter = new EbeyeAdapter();

        String query = LuceneQueryBuilder.createUniprotQueryForEnzyme (accessions, null);
        ParamOfGetResults param = createParamOfGetResults(domain, query, accessions);
        ebeyeAdapter.getNumberOfResults(param);
       return param;
    }
/*
    public List<ParamOfGetResults> getNrOfRecordsWithFilter(
            String domain, List<String> queryValues) throws EnzymeFinderException {
        //Uniprot number of result
        IEbeyeAdapter ebeyeAdapter = new EbeyeAdapter();
        //TODO createSubQueries
        List<ParamOfGetResults> params = createSubQueries(
                  domain
                , IEbeyeAdapter.FieldsOfGetResults.id.name()
                , true
                , queryValues);
       return params;
    }
*/
    public ParamOfGetResults createParamOfGetResults(String domain, String query, Collection<String> queryValues) throws EnzymeFinderException {
        //Uniprot number of result
        //String query = LuceneQueryBuilder.createUniprotQueryForEnzyme(queryValues, null);

        List<String> resultFields = new ArrayList<String>();
        //resultFields.add(IEbeyeAdapter.FieldsOfUniprotNameMap.descRecName.name());
        resultFields.add(IEbeyeAdapter.FieldsOfUniprotNameMap.id.name());
        //prepare list of parameters
        ParamOfGetResults param = new ParamOfGetResults(
                domain, query, resultFields);
       return param;
    }
/*
    public  Map<String, List<Result>> queryResultsForAllDomains(
            List<ParamOfGetResults> paramOfGetAllResults) throws EnzymeFinderException {
        IEbeyeAdapter ebeyeAdapter = new EbeyeAdapter();
        Map<String, List<Result>> allDomainsResults;
        try {
            allDomainsResults = ebeyeAdapter.getMultiDomainsResults(paramOfGetAllResults);
        } catch (MultiThreadingException ex) {
            throw new EnzymeFinderException("Find enzymes process failed! ", ex);
        }

        return allDomainsResults;
    }
 * 
 */

    /*
    public void validateSearchInput(String userKeywords) throws InvalidSearchException {
        if (!Validator.isSearchParamsOK(userKeywords)) {
            throw new InvalidSearchException("Search can only be performed with " +
                    "at least one keyword!");
        }
        LuceneParser luceneParser = new LuceneParser();        
        String cleanedKeywords = luceneParser
                .escapeLuceneSpecialChars(userKeywords);
        this.searchParams.setText(cleanedKeywords);
    }
*/
    public String getIdPrefix(String id) {
       String[] idSplit = id.split("_");
       String idPrefix = idSplit[0];
       return idPrefix;
    }

    public LinkedHashSet<String> getIdPrefixes(Collection<String> results) {
        LinkedHashSet<String> idPrefixes = new LinkedHashSet<String>();
       for (String id:results) {
           String idPrefix = getIdPrefix(id);
           idPrefixes.add(idPrefix);
       }
       return idPrefixes;
    }

    public Set<String> getIdPrefixes(List<Result> results) {
        Set<String> idPrefixes = new LinkedHashSet<String>();
       for (Result result:results) {
           String id = result.getId();
           String idPrefix = getIdPrefix(id);
           idPrefixes.add(idPrefix);
       }
       return idPrefixes;
    }


    public Map<String, List<Result>> groupResultsBySpecies(Set<String> idPrefixes, List<Result> results) {
        Map<String, List<Result>> resultsGroupedBySpecies = new HashMap<String, List<Result>>();
        for (String idPrefix:idPrefixes) {
            List<Result> resultsSubList = new ArrayList<Result>();
           for (Result result:results) {
               String idPrefixFromResult = getIdPrefix(result.getId());
               if (idPrefix.equals(idPrefixFromResult)) {
                   resultsSubList.add(result);
               }

           }
           resultsGroupedBySpecies.put(idPrefix, resultsSubList);

        }
       return resultsGroupedBySpecies;
    }

   public Map<String, List<Result>> groupResultsBySpecies(Map<String, Result> resultsMap) {
       Iterator it = resultsMap.keySet().iterator();
       Map<String, List<Result>> resultsGroupedBySpecies = new HashMap<String, List<Result>>();
       while (it.hasNext()) {
           String id = (String)it.next();
           Result result = resultsMap.get(id);
           String idPrefix = this.getIdPrefix(id);
           List<Result> results = resultsGroupedBySpecies.get(idPrefix);
           if (results == null) {
               results = new ArrayList<Result>();
           }
           results.add(result);
           resultsGroupedBySpecies.put(idPrefix, results);
           }
       return resultsGroupedBySpecies;
   }



      public Map<String, Result> removeDuplications(List<Result> results) {
            Map<String, Result> resultsGroupedBySpecies = new HashMap<String, Result>();
                //List<Result> resultsSubList = new ArrayList<Result>();
               for (Result result:results) {
                   String id = result.getId();
                   resultsGroupedBySpecies.put(id, result);
                   /*
                   if (resultsGroupedBySpecies.containsKey(id)) {
                       List<Result> resultList = resultsGroupedBySpecies.get(id);
                       resultList.add(result);
                   }
                   else {
                       List<Result> resultList = new ArrayList<Result>();
                       resultList.add(result);
                       resultsGroupedBySpecies.put(id, resultsSubList);
                   }
                    *
                    */
            }
           return resultsGroupedBySpecies;
        }

      public Map<String, List<Result>> subMap(List<String> keys, Map<String, List<Result>> resultGroupedBySpecies) {
          Iterator it = keys.iterator();
          Map<String, List<Result>> subMap = new HashMap<String, List<Result>>();
          while (it.hasNext()) {
              String idPrefix = (String)it.next();
              List<Result> resultlist = resultGroupedBySpecies.get(idPrefix);
              subMap.put(idPrefix, resultlist);
          }
          return subMap;
      }

      /**
       * Get Uniprot ids which are enzymes
       * @param accs
       * @return
       * @throws MultiThreadingException
       */
    public List<String> queryUniprotEnzymeIdRefFromOtherDomains(List<String> accs) throws MultiThreadingException {
        String queryField = IEbeyeAdapter.FieldsOfGetResults.acc.name();
        int subListSize = IEbeyeAdapter.EBEYE_NR_OF_QUERY_IN_LIMIT;
        List<String> resultFields = new ArrayList<String>();

        resultFields.add(IEbeyeAdapter.FieldsOfGetResults.id.name());
        List<String> limitedList = null;
        //Limited the results to process in order to improve the performance
        if (accs.size() > IEbeyeAdapter.EP_UNIPROT_XREF_RESULT_LIMIT){
            limitedList = accs.subList(0, IEbeyeAdapter.EP_UNIPROT_XREF_RESULT_LIMIT);
        } else {
            limitedList = accs;
        }

        //Enzyme filter must be added
        List<String> queries = LuceneQueryBuilder.createQueriesIn(
                queryField, limitedList, false, subListSize);
        List<ParamOfGetResults> paramList = this.prepareParamsForQueryIN(
                IEbeyeAdapter.Domains.uniprot.name(), queries,
                resultFields);

        ebeyeAdapter.getNumberOfResults(paramList);

        int otherDomainsNrOfResults = calTotalResultsFound(paramList);
        List<String> results = new ArrayList<String>();
        if (otherDomainsNrOfResults > 0) {
            results.addAll(ebeyeAdapter.getValueOfFields(paramList));
        }
        return results;
    }

    public void queryEbeyeChebiForUniprotIds() throws EnzymeFinderException {
        Collection<String> uniprotAccessionList = new ArrayList<String>();
        //Chebi has to be processed separately due to the filter
        ParamOfGetResults chebiParam = this.getChebiNrOfRecords();
        //int chebiResultSize = chebiParam.getTotalFound();

        Map<String,List<String>> chebiResults =
                ebeyeAdapter.getUniprotRefAccesionsMap(chebiParam);

        int chebiResultSize = chebiParam.getTotalFound();
        Collection<List<String>> chebiCol = chebiResults.values();

        LinkedHashSet<String> uniprotAccsFromChebiSet = DataTypeConverter.mergeList(
                chebiCol);
         List<String> uniprotAccsFromChebi = new ArrayList<String>(uniprotAccsFromChebiSet);
        List<String> uniprotIdsRefFromChebi =
        queryUniprotEnzymeIdRefFromOtherDomains(uniprotAccsFromChebi);

        if (uniprotIdsRefFromChebi != null) {
            this.uniprotEnzymeIds.addAll(uniprotIdsRefFromChebi);
        }
        
        //Save chebi ids
        //String chebiDomain = IEbeyeAdapter.Domains.chebi.name();
        //List<Result> chebiResults =  allDomainsResults.get(chebiDomain);
        //List<String> chebiAccs = this.getResultsXrefsList(chebiResults);
        Set<String> chebiAccs = null;
                //this.getResultsXrefs(chebiResults);

        //Save pdbE results

        //List<Result> pdbeResults = allDomainsResults.get(IEbeyeAdapter.Domains.pdbe.name());



        SearchFilters searchFilter = new SearchFilters();

        //IEbeyeDomainAdapter ebeyeDomainAdapter = new EbeyeChebiAdapter();

        //TODO

        //Map<String,String> chebiNames = ebeyeAdapter.getNameMapByAccessions(chebiDomain,chebiAccs);
        //Set<String> chebiNames = chebiResults.keySet();
        Set<String> chebiIds = chebiResults.keySet();
        Map<String,String> chebiIdNameMap = ebeyeAdapter.getNameMapByAccessions(
                IEbeyeAdapter.Domains.chebi.name(), chebiIds);
        List<Compound> compList = DataTypeConverter.mapToCompound(chebiIdNameMap);
        searchFilter.getCompounds().addAll(compList);
        //searchFilter.getCompounds().addAll(chebiAccs);
        enzymeSearchResults.setSearchfilters(searchFilter);
    }

    public void queryEbeyeForUniprotIds() throws EnzymeFinderException {
        //Query the number of results for all domains and save the value in ParamOfGetResults object
        List<ParamOfGetResults> nrOfResultParams = this.getNrOfRecordsRelatedToUniprot();

        //Retrieve accessions from UNIPROT field
        //Filter does not work here
        Set<String> relatedUniprotAccessionSet =
                ebeyeAdapter.getRelatedUniprotAccessionSet(nrOfResultParams);


        List<String> relatedUniprotAccessionList = new ArrayList<String>();
        relatedUniprotAccessionList.addAll(relatedUniprotAccessionSet);

        //Query the number of results from UNIPROT accessions
        //Species Filter can not be added here because some species in the top 10 result set
        //are from uniprot API and can not be found here
        //ParamOfGetResults nrOfNameResult = this.getNrOfRecords(uniprotDomain, relatedUniprotAccessionSet);

        List<String> uniprotIdsRefFromOtherDomains =
                queryUniprotEnzymeIdRefFromOtherDomains(relatedUniprotAccessionList);


        //Query uniprot directly
        ParamOfGetResults uniprotNrOfResultParams = this.getUniprotNrOfRecords();
        int uniprotResultSize = uniprotNrOfResultParams.getTotalFound();

        //Uniprot results are ranked in the first place.
        if (uniprotResultSize > 0) {
            uniprotEnzymeIds.addAll(ebeyeAdapter.getValueOfFields(uniprotNrOfResultParams));
        }

        if (uniprotIdsRefFromOtherDomains != null) {
            uniprotEnzymeIds.addAll(uniprotIdsRefFromOtherDomains);
        }
    }

    public void processInputs(SearchParams searchParams) {
        this.searchParams = searchParams;
        LuceneParser luceneParser = new LuceneParser();
        String cleanedKeywords = luceneParser
                .escapeLuceneSpecialChars(this.searchParams.getText());
        this.searchParams.setText(cleanedKeywords);
        String previousText = searchParams.getPrevioustext();
        String currentText = searchParams.getText();
        List<String> compoundFilter = searchParams.getCompounds();
        List<String> speciesFilter = searchParams.getSpecies();

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



    public SearchResults getEnzymes(SearchParams searchParams) throws EnzymeFinderException {        
        //setting variable values and validation
        //keywords before being cleaned
        String userKeywords = new String(searchParams.getText());
        processInputs(searchParams);
        List<String> compoundFilter = searchParams.getCompounds();
        List<String> speciesFilter = searchParams.getSpecies();
        String uniprotDomain = IEbeyeAdapter.Domains.uniprot.name();

        if (newSearch) {
            queryEbeyeForUniprotIds();
            //queryEbeyeChebiForUniprotIds();
        }
        //When there is filter the query is created using the id only
        queryEbeyeChebiForUniprotIds();


        List<String> resultList = new ArrayList<String>();

        List<String> idPrefixes = new ArrayList<String>();
        idPrefixes.addAll(this.getIdPrefixes(this.uniprotEnzymeIds));
        
        

        //Ignore filters for new search
        if (speciesFilter != null && !newSearch) {
            if (speciesFilter.size() > 0) {
                List<String> resultFields = new ArrayList<String>();
                resultFields.add(IEbeyeAdapter.FieldsOfGetResults.id.name());
                List<String> filterQueries = createIdWildcardQueriesWithSpeciesFilter(idPrefixes, speciesFilter);
                List<ParamOfGetResults> filteredNrOfResults =
                        this.prepareParamsForQueryIN(uniprotDomain, filterQueries,
                        resultFields);
                ebeyeAdapter.getNumberOfResults(filteredNrOfResults);
                Collection filteredResults = ebeyeAdapter.getValueOfFields(filteredNrOfResults);
                 idPrefixes = new ArrayList<String>();
                idPrefixes.addAll(this.getIdPrefixes(filteredResults));
            }
         }

        resultList.addAll(idPrefixes);        

        //TEST
        
        //Set<String> uniprotNameList = (Set<String>) ebeyeAdapter.getNameSetByAccessions(
           //     IEbeyeAdapter.Domains.uniprot.name(),accessionsFromOtherDomains);
        int resultSize = resultList.size();


        //Process the pagination
        //int totalFound = uniprotResults.size();
        int totalFound = resultList.size();
        int size = searchParams.getSize();
        int start =  searchParams.getStart();
        int subListIndex = start+size;
        if (totalFound<subListIndex) {
            subListIndex=totalFound;
        }

        List<String> resultSubList = resultList.subList(
               start, subListIndex);

        enzymeSearchResults.setTotalfound(totalFound);

        //List<String> idPrefixesSubList = (List<String>) uniprotNameList;
                //idPrefixes.subList(
               //start, subListIndex);
        //searchResults.setTotalfound(totalFound);

        //TODO ????
        //searchModel.getSearchparams().setStart(searchParams.getStart());
        
        IUniprotAdapter uniprotAdapter = new UniprotAdapter();
        List<EnzymeSummary> enzymeSummaryList = new ArrayList<EnzymeSummary>();
        //enzymeSummaryList = uniprotAdapter.getEnzymeEntries(briefResultSubset);
        //List<String> queries = LuceneQueryBuilder.createUniprotQueryByIdPrefixes(idPrefixesSubList);
        //enzymeSummaryList = uniprotAdapter.queryEnzymeByIdPrefixes(queries);
        List<String> queries = LuceneQueryBuilder
                .createUniprotAPIQueryByIdPrefixes(resultSubList, speciesFilter);

        
        enzymeSummaryList = uniprotAdapter.queryEnzymeByIdPrefixes(queries);

        //setPdbeAccession(enzymeSummaryList, pdbeResults);
        //resultsize
        List<String> uniprotIds = getUniprotIds(enzymeSummaryList);
        Map<String,String> pdbeAccs =
        ebeyeAdapter.getReferencedIds(uniprotIds, IEbeyeAdapter.Domains.pdbe.name());

        setPdbeAccession(enzymeSummaryList, pdbeAccs);



        //Ma<String,Map<String,String>> chebiFilters =
           //     ebeyeAdapter.getUniprotXrefIdAndName(uniprotIds, IEbeyeAdapter.Domains.chebi.name());


        setIntenzSynonyms(enzymeSummaryList);
        //TODO
        //searchResults.setResultsize(enzymeSummaryList.size());

        //searchResults.getEnzymesummary().addAll(enzymeSummaryList);

        //EnzymeSummary enzymeSummary = searchResults.getEnzymesummary().get(0);
        //enzymeSummary.
        enzymeSearchResults.getSummaryentries().addAll(enzymeSummaryList);
        enzymeSearchResults.setTotalfound(totalFound);

        createSpeciesFilter(enzymeSearchResults);
        searchParams.setStart(start);
        searchParams.setText(userKeywords);
        searchParams.setPrevioustext(userKeywords);

        return enzymeSearchResults;

    }


    public static int calTotalResultsFound(
            List<ParamOfGetResults> resultList) {
        if (resultList ==  null) {
            return 0;
        }
        Iterator it = resultList.iterator();
        int counter = 0;
        for (ParamOfGetResults param:resultList){
            counter = counter + param.getTotalFound();
        }
        return counter;
    }
    public void setIntenzSynonyms(
            List<EnzymeSummary> enzymeSummaryList) throws MultiThreadingException {

        Set<String> ecSet  = getUniprotEcs(enzymeSummaryList);
        Map<String,Set<String>> intenzSynonyms = queryIntenzSynonyms(ecSet);

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
    }

    /**
     * ec numbers end with "-" will be excluded from the results.
     * @param enzymeSummaryList
     * @return
     * @throws MultiThreadingException
     */
    public Set<String> getUniprotEcs(
            List<EnzymeSummary> enzymeSummaryList) throws MultiThreadingException {        
        Set<String> ecSet = new TreeSet<String>();
        for (EnzymeSummary enzymeSummary:enzymeSummaryList) {
            List<String> ecList = enzymeSummary.getEc();
            for (String ec: ecList) {
                if (!ec.contains("-")) {
                    ecSet.add(ec);
                }
            }
        }
        return ecSet;
    }

    public Map<String,Set<String>>  queryIntenzSynonyms(Set<String> ecSet) throws MultiThreadingException {
            IintenzAdapter intenzAdapter = new IntenzAdapter();
            return intenzAdapter.getSynonyms(ecSet);
    }

    public void setPdbeAccession(List<EnzymeSummary> enzymeSummaryList
            ,  Map<String,String> pdbeAccs) {
        for (EnzymeSummary enzymeSummary:enzymeSummaryList) {
            String uniprotId = enzymeSummary.getUniprotid();
            String pdbeAcc = pdbeAccs.get(uniprotId);
            enzymeSummary.getPdbeaccession().add(pdbeAcc);
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
    public Set<String> getUniprotAccs(List<EnzymeSummary> enzymeSummaryList) {
        Set<String> uniprotAccs = new TreeSet<String>();
        for (EnzymeSummary enzymeSummary:enzymeSummaryList) {
            String uniprotAcc = enzymeSummary.getUniprotaccessions().get(0);
            uniprotAccs.add(uniprotAcc);
        }
        return uniprotAccs;
    }


    public void createSpeciesFilter(SearchResults enzymeSearchResults) {
        List<EnzymeSummary> enzymeSummaryList = enzymeSearchResults
                .getSummaryentries();
        
        SearchFilters searchFilters = enzymeSearchResults.getSearchfilters();
        List<Species> speciesFilter = new ArrayList<Species>();

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
                speciesFilter.add(species);
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
                    speciesFilter.add(relSpecies);
                }
            }
        }

        searchFilters.getSpecies().addAll(speciesFilter);
        enzymeSearchResults.setSearchfilters(searchFilters);
        System.out.print(speciesFilter);

    }


    /**
     * Prepare field queries for all domains except for uniprot and chebi.
     * @param searchParams
     * @return
     */
    public static List<ParamOfGetResults>  prepareGetRelatedRecordsToUniprotQueries(
            SearchParams searchParams) {
        List<Domain> domains = Config.domainList;
        List<ParamOfGetResults> paramList = new ArrayList<ParamOfGetResults>();
        for (Domain domain: domains) {
            String domainId = domain.getId();
            if (!domainId.equals(IEbeyeAdapter.Domains.uniprot.name())
                    || !domainId.equals(IEbeyeAdapter.Domains.chebi.name())) {
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


    public static ParamOfGetResults  prepareGetUniprotIdQueries(
            SearchParams searchParams) {
        Domain domain = Config.getDomain(IEbeyeAdapter.Domains.uniprot.name());
        //List<String> searchFields = DataTypeConverter.getConfigSearchFields(domain);
            List<String> resultFields = new ArrayList<String>();
            resultFields.add(IEbeyeAdapter.FieldsOfUniprotNameMap.id.name());
            String query = LuceneQueryBuilder.createFieldsQueryWithEnzymeFilter(domain, searchParams);
           ParamOfGetResults paramOfGetAllResults =
                    new ParamOfGetResults(IEbeyeAdapter.Domains.uniprot.name(), query, resultFields);
        return paramOfGetAllResults;
    }

    public ParamOfGetResults  prepareChebiQueries() {
        Domain domain = Config.getDomain(IEbeyeAdapter.Domains.chebi.name());
        //List<String> searchFields = DataTypeConverter.getConfigSearchFields(domain);
            List<String> resultFields = new ArrayList<String>();
            //resultFields.add(IEbeyeAdapter.FieldsOfChebiNameMap.name.name());
            resultFields.add(IEbeyeAdapter.FieldsOfChebiNameMap.id.name());
            resultFields.add(IEbeyeAdapter.FieldsOfGetResults.UNIPROT.name());
            //The query has to be created differently if the compound filter is selected
            String query = null;
            List<String> compoundFilter = this.searchParams.getCompounds();
            if (compoundFilter.size() > 0) {
               query = LuceneQueryBuilder.createQueryIN(
                       IEbeyeAdapter.FieldsOfChebiNameMap.id.name(), false, compoundFilter);
            } else {
                query = LuceneQueryBuilder.createFieldsQuery(domain, searchParams);
            }

            //LuceneQueryBuilder.createQueryIN(query, false, resultFields)
           ParamOfGetResults paramOfGetAllResults =
                    new ParamOfGetResults(IEbeyeAdapter.Domains.chebi.name(), query, resultFields);
        return paramOfGetAllResults;
    }
     
    public List<String>  getTopRankedResults(
                 List<String> rankedResults, int start, int size) {
        List<String> topResults = null;
        int rankedResultsSize = rankedResults.size();
        if (rankedResults !=null && rankedResultsSize>size) {
            int endIndex = start+size;
            //To avoid OutOfBoundIndex exception
            if (endIndex > rankedResultsSize) {
                endIndex = rankedResultsSize;
            }
            topResults = rankedResults.subList(start, endIndex);
        }
        else {
            topResults = rankedResults;
        }
        return topResults;

    }

  }
