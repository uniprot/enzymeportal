package uk.ac.ebi.ep.core.search;

import uk.ac.ebi.ebeye.ParamOfGetResultsIds;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.xml.bind.JAXBException;
import javax.xml.rpc.ServiceException;
import org.apache.log4j.Logger;
import uk.ac.ebi.ebeye.ParamGetEntries;
import uk.ac.ebi.ebeye.ParamGetNumberOfResults;
import uk.ac.ebi.ebeye.ResultOfGetNumberOfResults;
import uk.ac.ebi.ebeye.ResultOfGetReferencedEntriesSet;
import uk.ac.ebi.ebeye.ResultOfGetResultsIds;
import uk.ac.ebi.ep.config.Domain;
import uk.ac.ebi.ep.core.search.EBeyeWsCallable.GetEntriesCallable;
import uk.ac.ebi.ep.core.search.EBeyeWsCallable.GetNumberOfResultsCallable;
import uk.ac.ebi.ep.core.search.EBeyeWsCallable.GetResultsIdCallable;
import uk.ac.ebi.ep.search.exception.EnzymeFinderException;
import uk.ac.ebi.ep.search.exception.InvalidSearchException;
import uk.ac.ebi.ep.search.exception.QueryException;
import uk.ac.ebi.ep.search.parameter.SearchParams;
import uk.ac.ebi.ep.search.result.EnzymeSearchResults;
import uk.ac.ebi.ep.search.result.EnzymeSummaryCollection;
import uk.ac.ebi.ep.util.query.LuceneQueryBuilder;
import uk.ac.ebi.ep.util.validation.Validator;
import uk.ac.ebi.util.result.DataTypeConverter;
import uk.ac.ebi.util.result.ResultCalculator;


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
    //protected List<String> uniprotIdList;
    //protected SortedMap<String, Integer> uniprotIdList = new TreeSet<String>();
    //protected EBeyeClient eBeyeClient;
    //protected EBISearchService eBISearchService;
    //TODO    
    public static final int TOP_RESULT_SIZE = 99;
    //public static final int RESULT_PER_DOMAIN_LIMIT = 20;
    public static final int GET_RESULTS_IDS_SIZE = 20;
    public static final int MAX_QUERIES_PER_DOMAIN = 2;
    public static final int MAX_THREADS_PER_POOL = 20;

    public static final int NUMBER_OF_IDS_PER_QUERY = 200;
    public static final String UNIPROT_DOMAIN = "uniprot";
    public static final String INTENZ_DOMAIN = "intenz";
    public static final int DEFAULT_QUERY_START = 0;
    public static final int MAX_IDS_FOR_FILTERING = 1005;
    protected SearchParams searchParams;


    //public static final String UNIPROT_URL = "http://www.uniprot.org/uniprot/";    
    private static Logger log = Logger.getLogger(EnzymeFinder.class);

//******************************** CONSTRUCTORS ******************************//

    public EnzymeFinder() {

    }


//****************************** GETTER & SETTER *****************************//
    public SearchParams getSearchParams() {
        return searchParams;
    }

    public void setSearchParams(SearchParams searchParams) {
        this.searchParams = searchParams;
    }


//********************************** METHODS *********************************//
    public EnzymeSearchResults find(SearchParams searchParams) throws EnzymeFinderException {
        if (!Validator.isSearchParamsOK(searchParams)) {
            throw new InvalidSearchException("Search can only be performed with " +
                    "at leasr one keyword!");
        }
        //TODO set in the constructor
        this.searchParams = searchParams;

        List<ResultOfGetNumberOfResults> resultOfGetNumberOfResults = null;
        try {
            resultOfGetNumberOfResults = this.processGetNumberOfResults();
            //TODO
        } catch (QueryException ex) {
            
        } catch (InterruptedException ex) {
            
        } catch (ExecutionException ex) {
            
        }
        //No results found
        if (resultOfGetNumberOfResults == null
                || resultOfGetNumberOfResults.size() == 0) {
            return createEmptyResponse();
        }

        List<ResultOfGetResultsIds> resultOfGetResultsIdsList = null;
        try {
            resultOfGetResultsIdsList =
                    this.processGetResultsIds(resultOfGetNumberOfResults);
            //TODO
        } catch (QueryException ex) {

        } catch (InterruptedException ex) {

        } catch (ExecutionException ex) {

        }
        List<String> resultIdsList = DataTypeConverter
                .getResultsIds(resultOfGetResultsIdsList);

        if (resultIdsList == null || resultIdsList.size() == 0) {
            return createEmptyResponse();
        }

        //List of all uniprot ids (enzymes and none enzymes)
        List<String> uniprotIdList = new ArrayList<String>();
        List<String> uniprotResults = DataTypeConverter
                .getResultsIdsFromDomain(resultOfGetResultsIdsList, UNIPROT_DOMAIN);
        //Save uniprot ids (enzymes and none enzyme) in a list
        if (uniprotResults != null || uniprotResults.size() > 0) {
            uniprotIdList.addAll(uniprotResults);
        }


        //Retrieve uniprot ids of the entries from domains other than uniprot
        List<ResultOfGetReferencedEntriesSet> resultsOfGetReferencedEntriesSet=null;
        try {
            resultsOfGetReferencedEntriesSet =
                    this.invokeGetReferencedEntriesSet(resultOfGetResultsIdsList);
        } catch (InterruptedException ex) {
          
        } catch (ExecutionException ex) {
          
        }
        List<String> intenzUniprotXrefs =
        DataTypeConverter.getXrefIdsFromDomain(
                resultsOfGetReferencedEntriesSet, INTENZ_DOMAIN);
        if (intenzUniprotXrefs != null) {
            uniprotIdList.addAll(intenzUniprotXrefs);
        }
        List<String> uniprotXrefs = getXrefUniprotIds(
                resultsOfGetReferencedEntriesSet);
        /*
        if (uniprotXrefs != null || uniprotXrefs.size() > 0) {
            uniprotIdList.addAll(uniprotXrefs);
        }
        if (uniprotIdList == null || uniprotIdList.size() == 0) {
            return createEmptyResponse();
        }
         *
         */


        
        //merge uniprot ids recieved from querying directly to uniprot        
        //Filter out uniprot ids that are not enzymes

        List<String> limitedIdsToBeFiltered = null;
        if (uniprotXrefs.size() > EnzymeFinder.MAX_IDS_FOR_FILTERING) {
                limitedIdsToBeFiltered = uniprotXrefs.subList(
                EnzymeFinder.DEFAULT_QUERY_START, EnzymeFinder.MAX_IDS_FOR_FILTERING);
        }
        else {
            limitedIdsToBeFiltered = uniprotXrefs;
        }
        List<ResultOfGetResultsIds> enzymeUniprotIds =
                filterNoneEnzymeIds(limitedIdsToBeFiltered);

        List<String> enzymeUniprotIdsList = 
                DataTypeConverter.getResultsIds(enzymeUniprotIds);
        int totalFound = uniprotIdList.size()+enzymeUniprotIdsList.size();
        if (enzymeUniprotIdsList != null || enzymeUniprotIdsList.size()!=0) {
            uniprotIdList.addAll(enzymeUniprotIdsList);
        }
        //Rank return a list of ids
        List<String> topRankedResults = this.getTopRankedResults(
                uniprotIdList
                );

        //get data for the list of top ranked ids
        EnzymeSummaryCollection resultsOfGetEntries =
                this.processGetEntries(topRankedResults);

        //Process the pagination
        /*
        int totalEnzymesFound = ResultCalculator
                .calTotalResultsFound(resultOfGetNumberOfResults);
         *
         */

        resultsOfGetEntries.setTotalfound(totalFound);
        EnzymeSearchResults enzymeSearchResults = new EnzymeSearchResults();
        enzymeSearchResults.setEnzymesummarycollection(resultsOfGetEntries);        
        return enzymeSearchResults;
    }

    public EnzymeSummaryCollection processGetEntries(List<String> topRankedResults) {
        Domain uniprotDomain = Config.getDomain(UNIPROT_DOMAIN);
        List<String> resultFields = Config.getResultFields(uniprotDomain);

        ParamGetEntries paramGetEntries =
                new ParamGetEntries(UNIPROT_DOMAIN, topRankedResults, resultFields);

        //Callable<EnzymeSummaryCollection> callable =
        GetEntriesCallable request =
        new GetEntriesCallable(paramGetEntries);
        EnzymeSummaryCollection resultsOfGetEntries = request.getEntries();
        //The size of top ranked results has been calculated previously
        resultsOfGetEntries.setResultsize(topRankedResults.size());
        resultsOfGetEntries.setResultstartat(searchParams.getStart());
        return resultsOfGetEntries;
    }

    public List<ResultOfGetResultsIds> processGetResultsIds(
            List<ResultOfGetNumberOfResults> resultOfGetNumberOfResults)
            throws QueryException, InterruptedException, ExecutionException {
        List<ParamOfGetResultsIds> paramOfGetResultsIdsList =
            prepareGetResultsIdsQueries(resultOfGetNumberOfResults);

        if (paramOfGetResultsIdsList == null
                || paramOfGetResultsIdsList.size() ==0) {
            throw new QueryException(
                    "Unable to create queries for GetResultsIds operation"
                    +" from keywords "
                    +searchParams.getKeywords());
        }
        List<ResultOfGetResultsIds> resultList =
                this.invokeGetResultsIds(paramOfGetResultsIdsList);
        return resultList;

    }

    public List<ResultOfGetNumberOfResults> processGetNumberOfResults() throws
            QueryException, InterruptedException, ExecutionException {
        //Prepare queries of getNumberOfResults for all domains
        List<ParamGetNumberOfResults> queries
                = this.prepareGetNumberOfResultsQueries(searchParams);

        if (queries == null || queries.size()==0) {
            throw new QueryException(
                    "Unable to create queries for GetNumberOfResults operation"
                    +" from keywords "
                    +searchParams.getKeywords());
        }

        //Query the number of results for all domains
        List<ResultOfGetNumberOfResults> resultOfGetNumberOfResults =
                this.invokeGetNumberOfResults(queries);
        return resultOfGetNumberOfResults;
    }

    public EnzymeSearchResults createEmptyResponse() {
        EnzymeSearchResults enzymeSearchResults = new EnzymeSearchResults();
        EnzymeSummaryCollection enzymeSummaryCollection = new EnzymeSummaryCollection();
        enzymeSummaryCollection.setTotalfound(0);
        enzymeSearchResults.setEnzymesummarycollection(enzymeSummaryCollection);
        return enzymeSearchResults;
    }

    public List<ResultOfGetResultsIds> filterNoneEnzymeIds(List<String> uniprotIdList) {
        List<ParamGetNumberOfResults> numberOfResultsParams =
                this.prepareUniprotFilterQueries(uniprotIdList);
        List<ResultOfGetNumberOfResults> numberOfResultsList = null;
        try {
            numberOfResultsList = this.invokeGetNumberOfResults(numberOfResultsParams);
        } catch (InterruptedException ex) {
            //java.util.logging.Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            //java.util.logging.Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
        int totalFound = ResultCalculator.calTotalResultsFound(numberOfResultsList);
        List<ResultOfGetResultsIds> enzymeUniprotIds = null;
        if (totalFound > 0) {
            List<ParamOfGetResultsIds> ResultsIdsParams =
            this.prepareGetResultsIdsQueries(numberOfResultsList);
            //List<ResultOfGetResultsIds> enzymeUniprotIds = null;
            try {
                enzymeUniprotIds = this.invokeGetResultsIds(ResultsIdsParams);
            } catch (InterruptedException ex) {
                //java.util.logging.Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                //java.util.logging.Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return enzymeUniprotIds;
    }
    /**
     * retrieve xref uniprot ids of domain other than intenz and uniprot
     * @param xRefList
     * @return
     */
    public List<String> getXrefUniprotIds(
            List<ResultOfGetReferencedEntriesSet> xRefList) {
        Iterator it = xRefList.iterator();
        List<String> uniprotIdList = new ArrayList<String>();
        while (it.hasNext()) {
            ResultOfGetReferencedEntriesSet xRef =
            (ResultOfGetReferencedEntriesSet)it.next();
            String domain = xRef.getResultOfGetResultsIds()
                    .getParamOfGetResultsIds()
                    .getResultOfGetNumberOfResults()
                    .getParamGetNumberOfResults()
                    .getDomain();
            List<String> uniprotXrefList = xRef.getUniprotXRefList();
            if (uniprotXrefList!=null && (!domain.equals(UNIPROT_DOMAIN)
                    || !domain.equals(INTENZ_DOMAIN))) {
                uniprotIdList.addAll(uniprotXrefList);
            }

        }
        return uniprotIdList;
    }
    
    public List<String>  getTopRankedResults(List<String> uniprotIdList) {
        ResultRanker resultRanker = new ResultRanker(uniprotIdList);
        resultRanker.rankResults();
        List<String> rankedResults = resultRanker.getRankedResults();
        List<String> topResults = null;
        if (rankedResults !=null && rankedResults.size()>TOP_RESULT_SIZE) {
            topResults = rankedResults.subList(0, TOP_RESULT_SIZE);
        }
        else {
            topResults = rankedResults;
        }
        return topResults;

    }
    /*
    public List<String> getUniprotIdXrefs(
                                    List<ResultOfGetResultsIds> resultList) {
        List<String> uniprotIds = new ArrayList<String>();
        if (resultList == null) {
            return null;
        }
        Iterator it = resultList.iterator();
        while (it.hasNext()) {
            ResultOfGetResultsIds resultEntry = (ResultOfGetResultsIds)it.next();
            String domainId = resultEntry
                                        .getResultOfGetNumberOfResults()
                                        .getParamOfGetResultsIds().getDomain();
            ArrayOfString result = resultEntry.getResult();
                if (domainId.equals(UNIPROT_DOMAIN)) {
                    uniprotIds.addAll(result.getString());
                } //If the domain is not uniprot then the results must be converted
                //into uniprot ids
                else {
                    try {
                        ArrayOfEntryReferences uniprotXRefs = invokeGetReferencedEntriesSet(domainId, result);
                        List<String> refList = EBeyeDataTypeConverter.convertArrayOfEntryReferencesToList(uniprotXRefs);

                        if (domainId.equals("intenz")) {

                            //addUniprotIdsToResults(uniprotIds);
                            uniprotIds.addAll(refList);
                        } else {
                            List<String> uniprotIdsEnzymeOnly = filterOutNonEnzymeUniprot(
                                    refList);
                            uniprotIds.addAll(uniprotIdsEnzymeOnly);
                            //addUniprotIdsToResults(uniprotIds);
                        }

                    } catch (ServiceException ex) {
                        // Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

        }
        return uniprotIds;
    }
    */

    public ParamGetNumberOfResults prepareGetNumberOfResultsIdsQuery(Domain domain
            , SearchParams searchParams) throws QueryException{
        String query = LuceneQueryBuilder.createQueryOR(domain, searchParams);
        String domainId = domain.getId();
        /*
        int totalResultsFoundPerDomain = eBISearchService.getNumberOfResults(domainId, query);
        totalResultsFound = totalResultsFound + totalResultsFoundPerDomain;
        log.debug("getNumberOfResults response = " +totalResultsFoundPerDomain);
        int resultLimit = 0;
        if (totalResultsFoundPerDomain<RESULT_PER_DOMAIN_LIMIT) {
        resultLimit = totalResultsFoundPerDomain;
        }
        else {
        resultLimit = RESULT_PER_DOMAIN_LIMIT;
        }
        //test thread
         *
         */
        if (query == null) {
            throw new QueryException("Unable to create a query for domain "
                    + domainId
                    + " and keywords "
                    + searchParams.getKeywords());
        }
        ParamGetNumberOfResults param = new ParamGetNumberOfResults(
                domainId, query);

        return param;
    }
    public List<ParamGetNumberOfResults> prepareGetNumberOfResultsQueries(
            SearchParams searchParams) throws QueryException {
        List<Domain> domainList = Config.domainList;
        if (domainList == null || domainList.size() == 0) {
            throw new QueryException("Unable to load the domain list from " +
                    "the configuation file!");
        }
        Iterator it = domainList.iterator();
        List<ParamGetNumberOfResults> params = new ArrayList<ParamGetNumberOfResults>();
        while (it.hasNext()) {
            Domain domain = (Domain)it.next();
                //param.setTotalResultFound(totalResultsFound);
            ParamGetNumberOfResults param =
                    this.prepareGetNumberOfResultsIdsQuery(domain, searchParams);
            params.add(param);
        }
        return params;
    }

    public ParamOfGetResultsIds prepareGetResultsIdsQuery(
                        ResultOfGetNumberOfResults resultOfGetNumberOfResults
                        , int start) {
        int totalFound = resultOfGetNumberOfResults.getTotalFound();
        //TODO - This size should be the size of the final result,
        //not the size per query
        //searchParams.setSize(
           //     ResultCalculator.calGetResultsIdsSize(totalFound
              //                                  , NUMBER_OF_RECORDS_PER_PAGE));
        int size = ResultCalculator.calGetResultsIdsSize(totalFound
                                                , GET_RESULTS_IDS_SIZE);
        ParamOfGetResultsIds paramOfGetResultsIds
                = new ParamOfGetResultsIds(
                        resultOfGetNumberOfResults
                        ,start
                        ,size);
        return paramOfGetResultsIds;

    }

    public List<ParamOfGetResultsIds> prepareGetResultsIdsQueries(
            List<ResultOfGetNumberOfResults> resultOfGetNumberOfResultsList) {
        Iterator it = resultOfGetNumberOfResultsList.iterator();
        List<ParamOfGetResultsIds> ParamOfGetResultsIdsList
                = new ArrayList<ParamOfGetResultsIds>();
        while (it.hasNext()){
            ResultOfGetNumberOfResults resultOfGetNumberOfResults
                    = (ResultOfGetNumberOfResults)it.next();
            int totalPerDomain = resultOfGetNumberOfResults.getTotalFound();
            if (totalPerDomain>0) {
                ParamOfGetResultsIds paramOfGetResultsIds =
                    prepareGetResultsIdsQuery(resultOfGetNumberOfResults
                        , DEFAULT_QUERY_START);
                ParamOfGetResultsIdsList.add(paramOfGetResultsIds);
            }
        }
        return ParamOfGetResultsIdsList;
    }
    /*
    public List<ResultOfGetResultsIds> invokeGetResultsIds(
                List<ParamOfGetResultsIds> paramList)
                    throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newFixedThreadPool(paramList.size());
        Iterator it = paramList.iterator();
        List<ResultOfGetResultsIds> resultList
                                = new ArrayList<ResultOfGetResultsIds>();
        while (it.hasNext()) {
            ParamOfGetResultsIds param = (ParamOfGetResultsIds)it.next();
            Callable<ResultOfGetResultsIds> callable = new GetResultsIdCallable(param);
            Future<ResultOfGetResultsIds> future = pool.submit(callable);
            resultList.add((ResultOfGetResultsIds)future.get());
        }
         pool.shutdown();
         return resultList;
    }
     *
     */

        public List<ResultOfGetResultsIds> invokeGetResultsIds(
                List<ParamOfGetResultsIds> paramList)
                    throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newFixedThreadPool(paramList.size());
        Iterator it = paramList.iterator();
        List<ResultOfGetResultsIds> resultList
                                = new ArrayList<ResultOfGetResultsIds>();
        while (it.hasNext()) {
            ParamOfGetResultsIds paramOfGetResultsIds
                    = (ParamOfGetResultsIds)it.next();
            Callable<ResultOfGetResultsIds> callable = 
                    new GetResultsIdCallable(paramOfGetResultsIds);
            Future<ResultOfGetResultsIds> future = pool.submit(callable);
            ResultOfGetResultsIds resultOfGetResultsIds =
                    (ResultOfGetResultsIds)future.get();
            if (resultOfGetResultsIds != null) {
                resultList.add(resultOfGetResultsIds);
            }
            
        }
         pool.shutdown();
         return resultList;
    }

    /**
     *
     * @param paramList
     * @return An object of {@link ResultOfGetNumberOfResults} that might
     * contains the total results found equal to 0.
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public List<ResultOfGetNumberOfResults> invokeGetNumberOfResults(
                            List<ParamGetNumberOfResults> paramList)
                                    throws InterruptedException, ExecutionException {
        Iterator it = paramList.iterator();
        ExecutorService pool = Executors.newFixedThreadPool(paramList.size());
        List<ResultOfGetNumberOfResults> resultList
                                = new ArrayList<ResultOfGetNumberOfResults>();
        while (it.hasNext()) {
            ParamGetNumberOfResults paramGetNumberOfResults =
                    (ParamGetNumberOfResults)it.next();
            /* this will be done in the callable method
            ResultOfGetNumberOfResults resultOfGetNumberOfResults =
                    new ResultOfGetNumberOfResults();
            resultOfGetNumberOfResults.setParamGetNumberOfResults(
                    paramGetNumberOfResults
                    );
             *
             */
            Callable<ResultOfGetNumberOfResults> callable =
                    new GetNumberOfResultsCallable(paramGetNumberOfResults);
            Future<ResultOfGetNumberOfResults> future = pool.submit(callable);
            ResultOfGetNumberOfResults resultOfGetNumberOfResults
                    = (ResultOfGetNumberOfResults)future.get();
            resultList.add(resultOfGetNumberOfResults);
        }
         pool.shutdown();
         return resultList;
    }


    public static void main(String[] args) throws ServiceException, IOException, JAXBException {
        System.out.println("java.class.path");
        /*
        EnzymeFinder enzyme = new EnzymeFinder();
        int numberOfResults = enzyme.eBISearchService.getNumberOfResults(UNIPROT_DOMAIN, "EC:1.1*");
        Set<String> uniprotEnzymeList = new TreeSet<String>();
        ArrayOfString result = null;
        if (numberOfResults>RESULT_PER_DOMAIN_LIMIT) {
            int counter = 0;
            while (counter<numberOfResults) {                
                result = enzyme.eBISearchService.getResultsIds(UNIPROT_DOMAIN, "EC:1*", counter-1,RESULT_PER_DOMAIN_LIMIT);
                counter = counter+RESULT_PER_DOMAIN_LIMIT;
            }
        }
        else {
            result = enzyme.eBISearchService.getResultsIds(UNIPROT_DOMAIN, "EC:1*", START_AT, numberOfResults);
        }
         List<String> resultList = result.getString();
        Iterator it = resultList.iterator();
        while (it.hasNext()) {
            String id = (String)it.next();
            uniprotEnzymeList.add(id);
            System.out.println(id);
        }
*/
        
    }

/*
    public void printMergedResults() {
         Iterator it = this.uniprotIdList.iterator();
          Pattern p = Pattern.compile("HUMAN");
          int counter = 0;
         while (it.hasNext()) {
             String id = (String)it.next();
         }
    }
*/
    /*
    public EnzymeSearchResults queryUniprotResults(List<String> idList) throws ServiceException {
        Iterator it = idList.iterator();
        ArrayOfString result = null;        
        Domain uniprotDomain = Config.getDomain(UNIPROT_DOMAIN);
        ArrayOfString resultFields = EBeyeDataTypeConverter
                                    .createEbeyeFieldArray(uniprotDomain);
       EnzymeSearchResults resultSet = new EnzymeSearchResults();
        EnzymeSummaryCollection enzymes = new EnzymeSummaryCollection();

        while (it.hasNext()) {
            String id = (String)it.next();
            result = eBISearchService.getEntry(
                uniprotDomain.getId(), id,resultFields);
                //enzymes.getEnzymeSummary().add();
                enzymes.getEnzymesummary().add(createResultSet(result.getString()));
        }
         //resultSet.setEnzymeSummaryCollection(enzymes);
         resultSet.setEnzymesummarycollection(enzymes);
         return resultSet;
     //  Config.getDomain(UNIPROT_DOMAIN).getResultFieldList().getResultField().
    }
     * 
     */
/*
    public EnzymeSummary createResultSet(List<String> resultFields) {
        EnzymeSummary enzymeSummary= new EnzymeSummary();
        int counter = 0;
        for (String field:resultFields) {
            String resultFieldValue = new String(field);
            switch (counter) {
                case 0: {
                    enzymeSummary.setUniprotid(resultFieldValue);
                    break;
                }
                case 1: {

                    List accessionList =DataTypeConverter
                                            .accessionsToList(resultFieldValue.split("\\s"));
                    enzymeSummary.getUniprotaccessions().addAll(accessionList);
                    break;
                }
                case 2: {
                    enzymeSummary.setName(resultFieldValue);
                    break;
                }
                case 3: {
                    String name = enzymeSummary.getName();
                    if (name.isEmpty() || name==null) {
                        //descSubName
                        enzymeSummary.setName(resultFieldValue);
                    }                                            
                    break;
                }
                case 4: {
                    Species species = new Species();
                    species.setScientificname(resultFieldValue);
                    enzymeSummary.setSpecies(species);
                    break;
                }
            }
        counter++;
        }
      
       return enzymeSummary;

    }
 * 
 */
/*
    public ArrayOfEntryReferences invokeGetReferencedEntriesSet(
            String domainId, ArrayOfString result) throws ServiceException{
        ArrayOfString resultRefFields= new ArrayOfString();
        resultRefFields.getString().add("id");
        ArrayOfEntryReferences refSearchResult = eBISearchService
                .getReferencedEntriesSet(domainId, result, UNIPROT_DOMAIN, resultRefFields);
         return refSearchResult;
    }
*/
    public List<ResultOfGetReferencedEntriesSet> invokeGetReferencedEntriesSet(
            List<ResultOfGetResultsIds> resultList)
                    throws InterruptedException, ExecutionException {
        Iterator it = resultList.iterator();
        ExecutorService pool = Executors.newFixedThreadPool(resultList.size());
        List<ResultOfGetReferencedEntriesSet> uniprotXrefList =
                            new ArrayList<ResultOfGetReferencedEntriesSet>();
        while (it.hasNext()) {
            ResultOfGetResultsIds resultEntry = (ResultOfGetResultsIds)it.next();
            Callable<ResultOfGetReferencedEntriesSet> getReferencedEntriesSetCallable
                    = new EBeyeWsCallable.GetReferencedEntriesSetCallable(resultEntry);
            Future<ResultOfGetReferencedEntriesSet> future
                    = pool.submit(getReferencedEntriesSetCallable);
            ResultOfGetReferencedEntriesSet resultOfGetReferencedEntriesSet =
                    (ResultOfGetReferencedEntriesSet)future.get();
            if (resultOfGetReferencedEntriesSet != null) {
                uniprotXrefList.add(resultOfGetReferencedEntriesSet);
            }
        }
        pool.shutdown();
        return uniprotXrefList;
    }

    /*
    public ArrayOfEntryReferences getUniprotIds(String domainId, ArrayOfString result) throws ServiceException{
        ArrayOfString resultRefFields= new ArrayOfString();
        resultRefFields.getString().add("id");
        ArrayOfEntryReferences refSearchResult = eBISearchService
                .getReferencedEntriesSet(domainId, result, UNIPROT_DOMAIN, resultRefFields);
         return refSearchResult;
    }
  */
/*
    public boolean isEnzyme(String uniprotId){
        boolean isEnzyme=false;
        String query = LuceneQueryBuilder.createQueryToGetEnzymeOnly(uniprotId);
        ArrayOfString result = null;
        result = eBISearchService.getResultsIds(UNIPROT_DOMAIN, query, 0, 1);
        if (result != null) {
            if (result.getString().size()>0){
                isEnzyme = true;
            }
        }
        return isEnzyme;
    }
*/
    /*
    public List<String> filterOutNonEnzymeUniprot(List<String> ids) throws ServiceException {     
        int numberOfResults = ids.size();
        Pagination pagination = new Pagination();
        pagination.paginateResults(numberOfResults, NUMBER_OF_RECORDS_PER_PAGE);
        int startAt = 0;
        int endAt = 0;
        int totalPages = pagination.getTotalPages();
        List<String> resultlist = new ArrayList<String>();
        for (int i = 0; i < totalPages ; i++) {
            //0+20-1 = 19
            if (i==(pagination.getTotalPages()-1)) {
                endAt = ids.size();
            }
            else {
                endAt = ((startAt+NUMBER_OF_RECORDS_PER_PAGE));
            }

            List<String> subList = ids.subList(startAt, endAt);
            String query = LuceneQueryBuilder.createUniprotQueryForEnzyme(subList);
            int numberOfResultsOfSubQuery = eBISearchService.getNumberOfResults(UNIPROT_DOMAIN, query);
            ArrayOfString result = eBISearchService.getResultsIds(UNIPROT_DOMAIN, query, 0,numberOfResultsOfSubQuery);
            resultlist.addAll(result.getString());
            startAt = startAt+subList.size();
        }
        return resultlist;
    }
*/
    public List<ParamGetNumberOfResults> prepareUniprotFilterQueries (List<String> list) {
        int numberOfResults = list.size();
        Pagination pagination = new Pagination();
        pagination.paginateResults(numberOfResults, NUMBER_OF_IDS_PER_QUERY);
        int startAt = 0;
        int endAt = 0;
        int totalPages = pagination.getTotalPages();
        List<ParamGetNumberOfResults> queryList = new ArrayList<ParamGetNumberOfResults>();
        for (int i = 0; i < totalPages ; i++) {
            //0+20-1 = 19
            if (i==(pagination.getTotalPages()-1)) {
                endAt = numberOfResults;
            }
            else {
                endAt = ((startAt+NUMBER_OF_IDS_PER_QUERY));
            }

            List<String> subList = list.subList(startAt, endAt);
            String query = LuceneQueryBuilder.createUniprotQueryForEnzyme(subList);

            ParamGetNumberOfResults paramGetNumberOfResults =
                    new ParamGetNumberOfResults(UNIPROT_DOMAIN, query);
            queryList.add(paramGetNumberOfResults);
        }
        return queryList;
    }




  }
