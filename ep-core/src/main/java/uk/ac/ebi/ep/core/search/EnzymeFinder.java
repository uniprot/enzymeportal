package uk.ac.ebi.ep.core.search;

import uk.ac.ebi.ep.search.result.Pagination;
import uk.ac.ebi.ebeye.ParamOfGetResultsIds;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.ep.search.exception.QueryException;
import uk.ac.ebi.ep.search.parameter.SearchParams;
import uk.ac.ebi.ep.search.result.jaxb.EnzymeSearchResults;
import uk.ac.ebi.ep.search.result.jaxb.EnzymeSummaryCollection;
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
    public static final int MAX_IDS_PER_FILTER_QUERY = 400;
    public static final String UNIPROT_DOMAIN = "uniprot";
    public static final String INTENZ_DOMAIN = "intenz";
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


    public List<String> getEnzymeUniprotIds(
            List<ResultOfGetResultsIds> resultsIdsList) throws MultiThreadingException, QueryException {
        List<String> enzymeUniprotIds = new ArrayList<String>();

        //Uniprot results from getResultsIds query which has been filtered as enzymes
        List<String> uniprotResults = new ArrayList<String>();
        uniprotResults = DataTypeConverter
                .getResultsIdsFromDomain(resultsIdsList, UNIPROT_DOMAIN);
        //Save uniprot ids (enzymes and none enzyme) in a list
        if (uniprotResults.size() > 0) {
            enzymeUniprotIds.addAll(uniprotResults);
        }

        //Retrieve uniprot ids of the entries from domains other than uniprot
        List<ResultOfGetResultsIds> noneUniprotIds =
        DataTypeConverter.excludeDomainFromResults(resultsIdsList, UNIPROT_DOMAIN);

        //List of uniprot ids derived from domains other than uniprot.
        List<ResultOfGetReferencedEntriesSet> refEntriesSet=
                new ArrayList<ResultOfGetReferencedEntriesSet>();

        refEntriesSet =
                this.getReferencedEntriesSet(noneUniprotIds);

        //Retrieve uniprot ids for intenz. Since those ids are, for sure enzymes
        //there is no need to filter them.
        List<String> intenzUniprotXrefs = new ArrayList<String>();

        //List of uniprot ids from domains other than intenz
        List<String> uniprotXrefs =  new ArrayList<String>();
        if (refEntriesSet.size() > 0) {
            intenzUniprotXrefs =
            DataTypeConverter.getXrefIdsFromDomain(
                    refEntriesSet, INTENZ_DOMAIN);
            if (intenzUniprotXrefs.size() > 0) {
                enzymeUniprotIds.addAll(intenzUniprotXrefs);
            }

            uniprotXrefs = excludeIntenzUniprotResults(
                    refEntriesSet);
        }

        //merge uniprot ids received from querying directly to uniprot
        //Filter out uniprot ids that are not enzymes        
            List<ResultOfGetResultsIds> filterResults =
                    new ArrayList<ResultOfGetResultsIds>();

        if(uniprotXrefs.size() > 0) {
                filterResults =
                        filterNoneEnzymeIds(uniprotXrefs);

        }

        //List<String> enzymeUniprotIdsList = new ArrayList<String>();
            if (filterResults.size() > 0) {
                enzymeUniprotIds.addAll(
                        DataTypeConverter.getResultsIds(filterResults));
            }
        return enzymeUniprotIds;

    }



    public List<String> rankEnzymes(List<String> uniprotIdList) {
        ResultRanker resultRanker = new ResultRanker(uniprotIdList);
        resultRanker.rankResults();
        List<String> rankedResults = resultRanker.getRankedResults();
        return rankedResults;
    }

    public EnzymeSearchResults getEnzymes(SearchParams searchParams) throws EnzymeFinderException {
        
        EnzymeSearchResults enzymeSearchResults = createEmptyResponse();
        if (!Validator.isSearchParamsOK(searchParams)) {
            throw new InvalidSearchException("Search can only be performed with " +
                    "at leasr one keyword!");
        }
        //TODO set in the constructor
        this.searchParams = searchParams;

        List<ResultOfGetNumberOfResults> totalResultsList =
                new ArrayList<ResultOfGetNumberOfResults>();
        totalResultsList = this.getNumberOfResults(this.searchParams);
        //No results found
        /*
        if (totalResultsList.size() == 0) {
            return enzymeSearchResults;
        }
        */
        if (ResultCalculator.calTotalResultsFound(totalResultsList)==0) {
            return enzymeSearchResults;
        }

        //This list contains results for all domains
        //Query directly xrefs
        List<ResultOfGetResultsIds> resultsIdsList =
                new ArrayList<ResultOfGetResultsIds>();
                    resultsIdsList =
        this.getResultsIds(totalResultsList);

        if (resultsIdsList.size() == 0) {
            return createEmptyResponse();
        }

        List<String> resultIdsStringList = new ArrayList<String>();

        resultIdsStringList = DataTypeConverter
                .getResultsIds(resultsIdsList);

        if (resultIdsStringList.size() == 0) {
            return createEmptyResponse();
        }

        //int totalFound = uniprotIdList.size()+enzymeUniprotIdsList.size();
        List<String> uniprotIdList = new ArrayList<String>();

        uniprotIdList = this.getEnzymeUniprotIds(resultsIdsList);

        //Rank return a list of ids
        List<String> rankedResults = new ArrayList<String>();
        rankedResults = this.rankEnzymes(uniprotIdList);
        List<String> topRankedResults = this.getTopRankedResults(
                 rankedResults, searchParams.getStart(), searchParams.getSize()
                );

        //get data for the list of top ranked ids
        EnzymeSummaryCollection resultsOfGetEntries =
                this.getUniprotEntries(topRankedResults);

        //Process the pagination
        /*
        int totalEnzymesFound = ResultCalculator
                .calTotalResultsFound(resultOfGetNumberOfResults);
         *
         */

        resultsOfGetEntries.setTotalfound(rankedResults.size());
        //resultsize
        resultsOfGetEntries.setResultsize(topRankedResults.size());
        resultsOfGetEntries.setResultstartat(searchParams.getStart());
        enzymeSearchResults = new EnzymeSearchResults();
        enzymeSearchResults.setEnzymesummarycollection(resultsOfGetEntries);        
        return enzymeSearchResults;
    }

    public EnzymeSummaryCollection getUniprotEntries(List<String> topRankedResults) {
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

    public List<ResultOfGetResultsIds> getResultsIds(
            List<ResultOfGetNumberOfResults> resultOfGetNumberOfResults)
            throws QueryException, MultiThreadingException {
        String msg = null;
        List<ParamOfGetResultsIds> paramOfGetResultsIdsList =
            LuceneQueryBuilder.prepareGetResultsIdsQueries(
            resultOfGetNumberOfResults);


        List<ResultOfGetResultsIds> resultList = new ArrayList<ResultOfGetResultsIds>();
        resultList = this.invokeGetResultsIds(paramOfGetResultsIdsList);

        return resultList;

    }

    public List<ResultOfGetNumberOfResults> getNumberOfResults(
            SearchParams searchParams) throws
        QueryException, MultiThreadingException {
        String msg = null;
        //Prepare queries of getNumberOfResults for all domains
        List<ParamGetNumberOfResults> queries
                = this.prepareGetNumberOfResultsQueries(searchParams);

        //Query the number of results for all domains
        List<ResultOfGetNumberOfResults> resultOfGetNumberOfResults;
        resultOfGetNumberOfResults = this.getNumberOfResults(queries);
        return resultOfGetNumberOfResults;
    }

    public EnzymeSearchResults createEmptyResponse() {
        EnzymeSearchResults enzymeSearchResults = new EnzymeSearchResults();
        EnzymeSummaryCollection enzymeSummaryCollection = new EnzymeSummaryCollection();
        enzymeSummaryCollection.setTotalfound(0);
        enzymeSearchResults.setEnzymesummarycollection(enzymeSummaryCollection);
        return enzymeSearchResults;
    }

    public List<ResultOfGetResultsIds> filterNoneEnzymeIds(
            List<String> uniprotIdList) throws QueryException, MultiThreadingException {
        List<ParamGetNumberOfResults> numberOfResultsParams =
                this.prepareUniprotFilterQueries(uniprotIdList);
        List<ResultOfGetNumberOfResults> numberOfResultsList = null;
        numberOfResultsList = this.getNumberOfResults(numberOfResultsParams);
        int totalFound = ResultCalculator.calTotalResultsFound(numberOfResultsList);
        List<ResultOfGetResultsIds> enzymeUniprotIds = null;
        if (totalFound > 0) {
            List<ParamOfGetResultsIds> ResultsIdsParams =
            //this.prepareGetResultsIdsQueries(numberOfResultsList);
            LuceneQueryBuilder.prepareGetResultsIdsQueries(numberOfResultsList);
            //List<ResultOfGetResultsIds> enzymeUniprotIds = null;
            enzymeUniprotIds = this.invokeGetResultsIds(ResultsIdsParams);
        }
        return enzymeUniprotIds;
    }
    /**
     * retrieve xref uniprot ids of domain other than intenz and uniprot
     * @param xRefList
     * @return
     */
    public List<String> excludeIntenzUniprotResults(
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

    public ParamGetNumberOfResults prepareGetNumberOfResultsIdsQuery(Domain domain
            , SearchParams searchParams) throws QueryException{
        String query = LuceneQueryBuilder.createQueryOR(domain, searchParams);
        String domainId = domain.getId();
        if (query == null) {
            throw new QueryException("Unable to create a query for"
                    +" GetNumberOfResults operation from domain "
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

        if (params == null || params.size()==0) {
            throw new QueryException(
                    "Unable to create queries for GetNumberOfResults operation"
                    +" from keywords "
                    +searchParams.getKeywords());
        }
        return params;
    }

    public List<ResultOfGetResultsIds> invokeGetResultsIds(
            List<ParamOfGetResultsIds> paramList) throws MultiThreadingException {
        String msg = null;
        //ExecutorService pool = Executors.newFixedThreadPool(paramList.size());
        ExecutorService pool = Executors.newCachedThreadPool();
        List<ResultOfGetResultsIds> resultList = new ArrayList<ResultOfGetResultsIds>();
        try {
            Iterator it = paramList.iterator();
            while (it.hasNext()) {
                ParamOfGetResultsIds paramOfGetResultsIds = (ParamOfGetResultsIds) it.next();
                Callable<ResultOfGetResultsIds> callable =
                        new GetResultsIdCallable(paramOfGetResultsIds);
                Future<ResultOfGetResultsIds> future = pool.submit(callable);
                ResultOfGetResultsIds resultOfGetResultsIds = null;
                try {
                    resultOfGetResultsIds = (ResultOfGetResultsIds) future.get();
                } catch (InterruptedException ex) {
                    msg = "getResultsIds operation failed! One of the threads has been interrupted";
                    throw new MultiThreadingException(msg, ex);
                } catch (ExecutionException ex) {
                    msg = "getResultsIds operation failed! One of the threads has not been executed!";
                    throw new MultiThreadingException(msg, ex);
                }
                if (resultOfGetResultsIds != null) {
                    resultList.add(resultOfGetResultsIds);
                }
            }
        }
        finally {
            pool.shutdown();
        }
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
    public List<ResultOfGetNumberOfResults> getNumberOfResults(
                            List<ParamGetNumberOfResults> paramList) throws MultiThreadingException {
        String msg = null;
        Iterator it = paramList.iterator();
        //ExecutorService pool = Executors.newFixedThreadPool(paramList.size());
        ExecutorService pool = Executors.newCachedThreadPool();
        List<ResultOfGetNumberOfResults> resultList
                                = new ArrayList<ResultOfGetNumberOfResults>();
        try {
            while (it.hasNext()) {
                ParamGetNumberOfResults paramGetNumberOfResults =
                        (ParamGetNumberOfResults)it.next();
                Callable<ResultOfGetNumberOfResults> callable =
                        new GetNumberOfResultsCallable(paramGetNumberOfResults);
                Future<ResultOfGetNumberOfResults> future = pool.submit(callable);
                ResultOfGetNumberOfResults resultOfGetNumberOfResults = null;
                try {
                    resultOfGetNumberOfResults = (ResultOfGetNumberOfResults) future.get();
                    if (resultOfGetNumberOfResults != null) {
                        resultList.add(resultOfGetNumberOfResults);
                    }
                } catch (InterruptedException ex) {
                    msg = "getNumberOfResults operation failed! One of the threads has been interrupted!";
                    throw new MultiThreadingException(msg, ex);
                } catch (ExecutionException ex) {
                    msg = "getNumberOfResults operation failed! One of the threads has not been executed!";
                    throw new MultiThreadingException(msg, ex);
                }
            }

        }
        finally {
            pool.shutdown();
        }
        return resultList;
    }


    public List<ResultOfGetReferencedEntriesSet> getReferencedEntriesSet(
            List<ResultOfGetResultsIds> resultList) throws MultiThreadingException {
        String msg = null;
        Iterator it = resultList.iterator();
        //ExecutorService pool = Executors.newFixedThreadPool(resultList.size());
        ExecutorService pool = Executors.newCachedThreadPool();
        List<ResultOfGetReferencedEntriesSet> uniprotXrefList =
                            new ArrayList<ResultOfGetReferencedEntriesSet>();
        try {
            while (it.hasNext()) {
                ResultOfGetResultsIds resultEntry = (ResultOfGetResultsIds)it.next();
                Callable<ResultOfGetReferencedEntriesSet> getReferencedEntriesSetCallable
                        = new EBeyeWsCallable.GetReferencedEntriesSetCallable(resultEntry);
                Future<ResultOfGetReferencedEntriesSet> future
                        = pool.submit(getReferencedEntriesSetCallable);
                ResultOfGetReferencedEntriesSet resultOfGetReferencedEntriesSet = null;
                try {
                    resultOfGetReferencedEntriesSet = (ResultOfGetReferencedEntriesSet) future.get();
                } catch (InterruptedException ex) {
                    msg = "getReferencedEntriesSet operation failed! One of the threads has been interrupted!";
                    throw new MultiThreadingException(msg, ex);
                } catch (ExecutionException ex) {
                    msg = "getReferencedEntriesSet operation failed! One of the threads has not been executed!";
                    throw new MultiThreadingException(msg, ex);
                }

                if (resultOfGetReferencedEntriesSet != null) {
                    uniprotXrefList.add(resultOfGetReferencedEntriesSet);
                }
            }
        }
        finally {
            pool.shutdown();
        }
        return uniprotXrefList;
    }

    public List<ParamGetNumberOfResults> prepareUniprotFilterQueries (List<String> list) {
        int numberOfResults = list.size();
        int startAt = 0;
        int endAt = 0;
        Pagination pagination = new Pagination(numberOfResults, MAX_IDS_PER_FILTER_QUERY);
        int totalPages = pagination.calTotalPages();
        List<ParamGetNumberOfResults> queryList = new ArrayList<ParamGetNumberOfResults>();
        for (int i = 0; i < totalPages ; i++) {
            //0+20-1 = 19
            if (i==(totalPages-1)) {
                endAt = numberOfResults;
            }
            else {
                endAt = ((startAt+MAX_IDS_PER_FILTER_QUERY));
            }

            List<String> subList = list.subList(startAt, endAt);
            String query = LuceneQueryBuilder.createUniprotQueryForEnzyme(subList);

            ParamGetNumberOfResults paramGetNumberOfResults =
                    new ParamGetNumberOfResults(UNIPROT_DOMAIN, query);
            queryList.add(paramGetNumberOfResults);
            startAt = endAt;
        }
        return queryList;
    }

  }
