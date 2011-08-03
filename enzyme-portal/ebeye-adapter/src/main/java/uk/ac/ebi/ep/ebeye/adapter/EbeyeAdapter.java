package uk.ac.ebi.ep.ebeye.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.log4j.Logger;
import uk.ac.ebi.ebeye.param.ParamOfGetResults;
import uk.ac.ebi.ebeye.util.Transformer;
import uk.ac.ebi.ep.ebeye.adapter.EbeyeCallable.GetEntriesCallable;
import uk.ac.ebi.ep.ebeye.adapter.EbeyeCallable.GetReferencedEntriesSet;
import uk.ac.ebi.ep.ebeye.adapter.EbeyeCallable.GetResultsCallable;
import uk.ac.ebi.ep.ebeye.adapter.EbeyeCallable.NumberOfResultsCaller;
import uk.ac.ebi.ep.ebeye.result.Result;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.ep.search.result.Pagination;
import uk.ac.ebi.ep.util.query.LuceneQueryBuilder;
import uk.ac.ebi.webservices.ebeye.ArrayOfArrayOfString;
import uk.ac.ebi.webservices.ebeye.ArrayOfEntryReferences;
import uk.ac.ebi.webservices.ebeye.ArrayOfString;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class EbeyeAdapter implements IEbeyeAdapter {

//********************************* VARIABLES ********************************//
//private static Logger log = Logger.getLogger(EbeyeAdapter.class);
  private static Logger log = Logger.getLogger(EbeyeAdapter.class);

//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

      public List<Result> getResults(ParamOfGetResults param)
            throws MultiThreadingException {
          return getResults(param, false);
      }
    public List<Result> getResults(ParamOfGetResults param, boolean convertResultToUniprot)
            throws MultiThreadingException {
        List<String> domainFields = param.getFields();
        String domain  = param.getDomain();

        //Limited uniprot results to 100
        /*
        if (domain.equals(IEbeyeAdapter.Domains.uniprot.name())) {
            if (param.getTotalFound()> IEbeyeAdapter.EBEYE_RESULT_LIMIT) {
                param.setTotalFound(IEbeyeAdapter.EBEYE_RESULT_LIMIT);
            }
        }
        */
        List<Callable<ArrayOfArrayOfString>> callableList
                 = prepareCallableCollection(param);


         //List<ArrayOfArrayOfString> rawResults =
            //      submitAll(paramOfGetResultsList);
         //List<ArrayOfArrayOfString> rawResults = submitAll(callableList);
         List<ArrayOfArrayOfString> rawResults = executeCallables(callableList);
            List<List<String>> transformedResults
                    = Transformer.transformToList(rawResults);

            Collection<String> accessionList = Transformer.transformFieldValueToList(rawResults, true);
            //Save the content to an object
            ResultFactory resultFactory = new ResultFactory(
                    domain, domainFields);
            List<Result> resultList = resultFactory.getResults(transformedResults
                    , convertResultToUniprot);
         /*
        List<List<String>> transformedResults
                = Transformer.transformToList(rawResults);
        //Save the content to an object

        ResultFactory resultFactory = new ResultFactory(
                domain, domainFields);
        List<Result> resultList = resultFactory.getResults(transformedResults);
          *
          */
         return resultList;

    }

    /**
     * {@inheritDoc }
     * @param paramOfGetResultsList
     * @return
     * @throws MultiThreadingException
     */
    public Map<String, List<Result>> getUniprotResults(
            List<ParamOfGetResults> paramOfGetResultsList) throws MultiThreadingException  {        
        //getNumberOfResults(paramOfGetResultsList);
        Map<String, List<Result>> allDomainsResults = new HashMap<String, List<Result>>();
        //getResultsByAccessions(paramOfGetResultsList)

        for (ParamOfGetResults param:paramOfGetResultsList )  {
            List<Result> resultList = this.getResults(param, true);
                    allDomainsResults.put(param.getDomain(), resultList);
        }
        return allDomainsResults;
    }

    /*
    public Map<String, List<ParamOfGetResults>> getNumberOfResults(
            List<ParamOfGetAllResults> paramOfGetAllResultsList) throws MultiThreadingException {
        Map<String, List<ParamOfGetResults>> domainParams = new
                Hashtable<String, List<ParamOfGetResults>>();
        ExecutorService pool = Executors.newCachedThreadPool();        
        try {
            for (ParamOfGetAllResults param:paramOfGetAllResultsList) {
                Callable<Integer> callable = new NumberOfResultsCaller(param);
                Future<Integer> future = pool.submit(callable);
                Integer totalFound;
                try {
                    totalFound = future.get(IEbeyeAdapter.EBEYE_ONLINE_REQUEST_TIMEOUT, TimeUnit.SECONDS);

                if (totalFound > IEbeyeAdapter.EP_RESULTS_PER_DOIMAIN_LIMIT) {
                    totalFound = IEbeyeAdapter.EP_RESULTS_PER_DOIMAIN_LIMIT;
                }

                List<ParamOfGetResults> paramOfGetResultsList = null;
                if (totalFound > 0) {
                        paramOfGetResultsList = prepareGetResultsParams(totalFound, param);
                        domainParams.put(param.getDomain(), paramOfGetResultsList);
                }
                } catch (InterruptedException ex) {
                    throw  new MultiThreadingException(ex.getMessage(), ex);
                } catch (ExecutionException ex) {
                    throw  new MultiThreadingException(ex.getMessage(), ex);
                } catch (TimeoutException ex) {
                    throw  new MultiThreadingException(ex.getMessage(), ex);
                }
                      
            }            
        }
        finally {
            pool.shutdown();
        }

        return domainParams;

    }
*/
/*
    public Map<String, List<Result>> executeCallables(
            List<ParamOfGetResults> paramOfGetResultsList) throws MultiThreadingException {

        //Get the number of results and save it in the param
        getNumberOfResults(paramOfGetResultsList);
        Map<String, List<Result>> allDomainsResults = new HashMap<String, List<Result>>();
        //getResultsByAccessions(paramOfGetResultsList)
        
        for (ParamOfGetResults param:paramOfGetResultsList )  {
                    List<String> domainFields = param.getFields();
                    String domain  = param.getDomain();

                    List<Callable<ArrayOfArrayOfString>> callableList
                             = prepareCallableCollection(param);


                     //List<ArrayOfArrayOfString> rawResults =
                        //      submitAll(paramOfGetResultsList);
                     //List<ArrayOfArrayOfString> rawResults = submitAll(callableList);
                     List<ArrayOfArrayOfString> rawResults = executeCallables(callableList);
                    List<List<String>> transformedResults
                            = Transformer.transformToList(rawResults);
                    //Save the content to an object
                    ResultFactory resultFactory = new ResultFactory(
                            domain, domainFields);
                    List<Result> resultList = resultFactory.getResults(transformedResults);
                    allDomainsResults.put(domain, resultList);
        }
        return allDomainsResults;
    }
 * */

    /*
    public List<ArrayOfArrayOfString>  submitAll(
            List<ParamOfGetResults> paramOfGetResultsList) {
        ExecutorService pool = Executors.newCachedThreadPool();
        List<ArrayOfArrayOfString> resultList = new ArrayList<ArrayOfArrayOfString>();


        try {
            for (ParamOfGetResults param: paramOfGetResultsList) {
                ArrayOfString ebeyeFields = Transformer.transformToArrayOfString(
                        param.getFields());
                Callable<ArrayOfArrayOfString> callable =
                            new GetResultsCallable(
                            param.getDomain(),
                            param.getQuery(),
                            ebeyeFields,
                            param.getStart(),
                            param.getSize()
                            );
                Future<ArrayOfArrayOfString>future = pool.submit(callable);
                ArrayOfArrayOfString result = null;
                try {
                    result = (ArrayOfArrayOfString) future
                        .get(IEbeyeAdapter.EBEYE_ONLINE_REQUEST_TIMEOUT, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                log.error(ex.getMessage(), ex.getCause());
            } catch (ExecutionException ex) {
                log.error(ex.getMessage(), ex.getCause());
            } catch (TimeoutException ex) {
                log.error(ex.getMessage(), ex.getCause());
            }
                resultList.add(result);
            }
        }
        finally {
            pool.shutdown();
        }
        return resultList;
    }
     *
     */

    public List<ArrayOfArrayOfString>  submitAll(
            List<Callable<ArrayOfArrayOfString>> callableList) throws MultiThreadingException{
        Iterator it = callableList.iterator();
        ExecutorService pool =  pool = Executors.newCachedThreadPool();
        List<ArrayOfArrayOfString> resultList = new ArrayList<ArrayOfArrayOfString>();
        try {                   
            while (it.hasNext()) {
                Callable<ArrayOfArrayOfString> callable =
                        (Callable<ArrayOfArrayOfString>)it.next();
                Future<ArrayOfArrayOfString>future = pool.submit(callable);
                ArrayOfArrayOfString result = null;
                try {
                    result = (ArrayOfArrayOfString) future
                        .get(IEbeyeAdapter.EBEYE_ONLINE_REQUEST_TIMEOUT, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                throw  new MultiThreadingException(ex.getMessage(), ex);
            } catch (ExecutionException ex) {
                throw  new MultiThreadingException(ex.getMessage(), ex);
            } catch (TimeoutException ex) {
                throw  new MultiThreadingException(ex.getMessage(), ex);
            }
                resultList.add(result);
            }
        }
        finally {
            pool.shutdownNow();
        }
        return resultList;
    }
/*
    public List<ArrayOfArrayOfString> getResultFromThreads(
            List<Future<ArrayOfArrayOfString>> futureList )
            throws InterruptedException, ExecutionException, TimeoutException {
        List<ArrayOfArrayOfString> resultList = new ArrayList<ArrayOfArrayOfString>();
        Iterator it = futureList.iterator();
        while (it.hasNext()) {
            Future<ArrayOfArrayOfString> future
                    =(Future<ArrayOfArrayOfString>)it.next();
            ArrayOfArrayOfString resultLine = (ArrayOfArrayOfString)future.get(
                    IEbeyeAdapter.EBEYE_ONE_RECORD_TIMEOUT, TimeUnit.MILLISECONDS);
            resultList.add(resultLine);
        }
        return resultList;
    }
 * *
 */
/*
    public List<ArrayOfArrayOfString> getAllEbeyeResults(
            ParamOfGetAllResults paramOfGetAllResults){
        NumberOfResultsCaller caller =
                new EbeyeCallable.NumberOfResultsCaller(paramOfGetAllResults);

        int totalFound = caller.getNumberOfResults();
        List<ArrayOfArrayOfString> rawResults =
                this.getEbeyeResults(totalFound, paramOfGetAllResults);
        return rawResults;
    }

        public List<ArrayOfArrayOfString> getEbeyeResults (int totalFound
            , ParamOfGetAllResults paramOfGetAllResults){
        String domain = paramOfGetAllResults.getDomain();
        String query = paramOfGetAllResults.getQuery();
        List<String> fields = paramOfGetAllResults.getFields();

        List<ArrayOfArrayOfString> resultList = new ArrayList<ArrayOfArrayOfString>();
        if (totalFound > 0) {
            ExecutorService pool = Executors.newCachedThreadPool();
            try {
                ArrayOfString ebeyeFields = Transformer.transformToArrayOfString(fields);
                int numberOfLoops = Calculator.calTotalPages(totalFound,
                        IEbeyeAdapter.EBEYE_RESULT_LIMIT);
                int lastLoopSize = Calculator.getLastPageResults(totalFound,
                        IEbeyeAdapter.EBEYE_RESULT_LIMIT);
                int currentLoop = 0;
                int start = 0;
                while (currentLoop < numberOfLoops) {
                    int size = IEbeyeAdapter.EBEYE_RESULT_LIMIT;
                    if (currentLoop == numberOfLoops-1) {
                        size = lastLoopSize;
                    }
                    Callable<ArrayOfArrayOfString> callable =
                            new GetResultsCallable(
                            domain, query, ebeyeFields, start,size);
                    Future<ArrayOfArrayOfString> future = pool.submit(callable);
                    ArrayOfArrayOfString rawResults = null;
                    try {
                        rawResults = (ArrayOfArrayOfString) future.get(
                                IEbeyeAdapter.EBEYE_ONLINE_REQUEST_TIMEOUT, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                log.error(ex.getMessage(), ex.getCause());
            } catch (ExecutionException ex) {
                log.error(ex.getMessage(), ex.getCause());
            } catch (TimeoutException ex) {
                log.error(ex.getMessage(), ex.getCause());
            }
                    resultList.add(rawResults);
                    start = start+size;
                    currentLoop++;
                }
            }
            finally {
                pool.shutdown();
            }

        }
        return resultList;
    }
 * *
 */
        public List<Callable<ArrayOfArrayOfString>> prepareCallableCollection (
                List<ParamOfGetResults> paramList){
            List<Callable<ArrayOfArrayOfString>> callableList
                    = new ArrayList<Callable<ArrayOfArrayOfString>>();
            if (paramList.size() > 0) {
                Iterator it = paramList.iterator();
                while (it.hasNext()) {
                    ParamOfGetResults param = (ParamOfGetResults)it.next();
                    List<Callable<ArrayOfArrayOfString>> callables =
                                                    prepareCallableCollection(param);
                    if (callables.size()>0) {
                        callableList.addAll(callables);
                    }

                }
            }
            return callableList;
        }
        public List<Callable<ArrayOfArrayOfString>> prepareCallableCollection (
                ParamOfGetResults param){
            List<Callable<ArrayOfArrayOfString>> callableList
                    = new ArrayList<Callable<ArrayOfArrayOfString>>();
                    int totalFound = param.getTotalFound();
                    int size = IEbeyeAdapter.EBEYE_RESULT_LIMIT;
                    //Work around to solve big result set issue
                    Pagination pagination = new Pagination(totalFound, IEbeyeAdapter.EBEYE_RESULT_LIMIT);
                    int nrOfQueries = pagination.calTotalPages();
                    int start = 0;
                    //TODO
                    for (int i = 0; i < nrOfQueries; i++) {
                        if (i == nrOfQueries - 1 && (totalFound % IEbeyeAdapter.EBEYE_RESULT_LIMIT) > 0) {
                            size =pagination.getLastPageResults();
                        }

                        Callable<ArrayOfArrayOfString> callable =
                                new GetResultsCallable(param, start, size);
                        callableList.add(callable);
                        //TODO check
                        start = start+ size;
                    }
            return callableList;
        }

        /*
        public List<Callable<ArrayOfArrayOfString>> prepareCallableCollection (
                List<ParamOfGetResults> params){
            List<Callable<ArrayOfArrayOfString>> callableList
                    = new ArrayList<Callable<ArrayOfArrayOfString>>();
            for (ParamOfGetResults param: params) {
                List<Callable<ArrayOfArrayOfString>> callableSubList =
                        prepareCallableCollection(param);
                if (callableSubList.size() > 0) {
                    callableList.addAll(callableSubList);
                }
            }
            return callableList;
        }
         *
         */

/*
        public List<ParamOfGetResults> prepareGetResultsParams (
                ParamOfGetResults paramOfGetResults) {
        int totalFound = paramOfGetResults.getTotalFound();
        List<String> fields = paramOfGetResults.getFields();
        List<ParamOfGetResults> paramList = new ArrayList<ParamOfGetResults>();
        if (totalFound > 0) {
            int numberOfLoops = Calculator.calTotalPages(totalFound,
                    IEbeyeAdapter.EBEYE_RESULT_LIMIT);
            int lastLoopSize = Calculator.getLastPageResults(totalFound,
                    IEbeyeAdapter.EBEYE_RESULT_LIMIT);
            int currentLoop = 0;
            int start = 0;
            while (currentLoop < numberOfLoops) {
                int size = IEbeyeAdapter.EBEYE_RESULT_LIMIT;
                if (currentLoop == numberOfLoops-1) {
                    size = lastLoopSize;
                }
                ParamOfResultSize paramOfResultSize = new ParamOfResultSize(start, size);
                paramOfGetResults.getResultSizeList().add(paramOfResultSize);
                start = start+size;
                currentLoop++;
            }
        }
        return paramList;
    }
*/
/*
    public List<ParamOfGetResults> prepareQueriesForAccessions(
            List<String> fields, List<String> uniprotXrefAccs, int limitOfAccessions
            , List<String> speciesFilter) {
        List<ParamOfGetResults> params = new ArrayList<ParamOfGetResults>();
        if (uniprotXrefAccs.size() > 0) {
            List<String> uniprotXrefAccList = new ArrayList<String>();
            uniprotXrefAccList.addAll(uniprotXrefAccs);
            int endIndex = 0;
            int total = uniprotXrefAccs.size();
            //Work around to solve big result set issue
            if (total > IEbeyeAdapter.EP_UNIPROT_XREF_RESULT_LIMIT) {
                total = IEbeyeAdapter.EP_UNIPROT_XREF_RESULT_LIMIT;
            }
            Pagination pagination = new Pagination(total, limitOfAccessions);
            int nrOfQueries = pagination.calTotalPages();
            int start = 0;
            for (int i = 0; i < nrOfQueries; i++) {
                if (i == nrOfQueries - 1 && (total % limitOfAccessions) > 0) {
                    endIndex = endIndex + pagination.getLastPageResults();
                } else {
                    endIndex = endIndex + limitOfAccessions;
                }
                String simpleQuery = LuceneQueryBuilder.createUniprotQueryForEnzyme(uniprotXrefAccList.subList(start, endIndex));
                String query = "";
                if (speciesFilter == null) {
                   query = simpleQuery;
                } else {
                    if (speciesFilter.size() > 0) {
                        query = LuceneQueryBuilder.addSpeciesFilterQuery(simpleQuery, speciesFilter);
                    }
                    else {
                         query = simpleQuery;
                    }
                     
                }

                ParamOfGetResults paramOfGetAllResults =
                        new ParamOfGetResults(IEbeyeAdapter.Domains.uniprot.name(), query, fields);
                params.add(paramOfGetAllResults);
                start = endIndex;
            }

        }
        return params;
    }
*/
    /*
    public List<ParamOfGetResults> queryNrOfResultsByAccessions(
            List<String> accessions, List<String> fields) throws MultiThreadingException {
        //List<String> fields = new ArrayList<String>();
        //IEbeyeAdapter.FieldsOfGetResults.getFields();
        //fields.add(IEbeyeAdapter.FieldsOfGetResults.id.name());
        //fields.add(IEbeyeAdapter.FieldsOfGetResults.acc.name());
        List<ParamOfGetResults> params = prepareQueriesForAccessions(
                fields, accessions, IEbeyeAdapter.EBEYE_NR_OF_ACC_LIMIT);
                //Get and save
        getNumberOfResults(params);
        return params;

    }
     * 
     */

    public int countNrOfResults(List<ParamOfGetResults> params) {
        int totalFound = 0;
        for (ParamOfGetResults param: params) {
            totalFound = totalFound +param.getTotalFound();
        }

        return totalFound;
    }
/*
  public int getNrOfResultsByAccessions(String domain
            , List<String> accessions) throws MultiThreadingException {
      List<ParamOfGetResults> params = queryNrOfResultsByAccessions(domain, accessions);
      return countNrOfResults(params);
  }
 *
 */
    /*
    public List<Result> getUniprotEnzymes(List<String> accessions) throws MultiThreadingException {
        return getUniprotEnzymes(accessions, null);
    }

    public List<Result> getUniprotEnzymes(List<String> accessions, List<String> speciesFilter) throws MultiThreadingException {
        List<String> fields = new ArrayList<String>();
        //IEbeyeAdapter.FieldsOfGetResults.getFields();
        fields.add(IEbeyeAdapter.FieldsOfGetResults.id.name());
        fields.add(IEbeyeAdapter.FieldsOfGetResults.acc.name());

        //List<ParamOfGetResults> params = queryNrOfResultsByAccessions(accessions, fields);
        List<ParamOfGetResults> params = prepareQueriesForAccessions(
                fields, accessions, IEbeyeAdapter.EBEYE_NR_OF_ACC_LIMIT, speciesFilter);

        //Get and save
        List<ParamOfGetResults> paramsWithNrOfResults = new ArrayList<ParamOfGetResults>();
        paramsWithNrOfResults.addAll(getNumberOfResults(params));

        List<Result> resultList = null;
        if (params.size()>0) {
            //Maybe we don't need to get the results for domains other than uniprot,
            //        but only get results for uniprot
            List<Callable<ArrayOfArrayOfString>> callables = prepareCallableCollection(params);

            List<ArrayOfArrayOfString> ebeyeResultList = executeCallables(callables);
            List<List<String>> transformedResults
                    = Transformer.transformToList(ebeyeResultList);
            //Save the content to an object
            ResultFactory resultFactory = new ResultFactory(
                    IEbeyeAdapter.Domains.uniprot.name(), params.get(0).getFields());
            resultList = resultFactory.getResults(transformedResults,false);

//            resultList = transformRawResult(domain, params.get(0).getFields(), ebeyeResultList);
        }

        return resultList;
    }
*/
/*
    public List<Result> transformRawResult(String domain, List<String> fields
            , List<ArrayOfArrayOfString> ebeyeResultList) {
            List<List<String>> transformedResults
                    = Transformer.transformToList(ebeyeResultList);
            //Save the content to an object
            ResultFactory resultFactory = new ResultFactory(
                    domain, fields);
            List<Result> resultList = resultFactory.g.getResults(transformedResults);
            return resultList;
    }
*/
    public List<ArrayOfArrayOfString> executeCallables(
            List<Callable<ArrayOfArrayOfString>> callables) throws MultiThreadingException {
        List<ArrayOfArrayOfString> ebeyeResultList = new ArrayList<ArrayOfArrayOfString>();
           ExecutorService pool = Executors.newCachedThreadPool();
        int counter = 0;
        try {
            for (Callable<ArrayOfArrayOfString> callable:callables) {
                Future<ArrayOfArrayOfString> future  = pool.submit(callable);
                ArrayOfArrayOfString rawResults = null;
                try {
                    rawResults = (ArrayOfArrayOfString) future
                            .get(IEbeyeAdapter.EBEYE_ONLINE_REQUEST_TIMEOUT, TimeUnit.SECONDS);
                } catch (InterruptedException ex) {
                    throw  new MultiThreadingException(ex.getMessage(), ex);
                } catch (ExecutionException ex) {
                    throw  new MultiThreadingException(ex.getMessage(), ex);
                } catch (TimeoutException ex) {
                    throw  new MultiThreadingException(ex.getMessage(), ex);
                }
                ebeyeResultList.add(rawResults);
                counter++;
                if (counter >  IEbeyeAdapter.EP_THREADS_LIMIT)
                    break;
            }
        }
        finally {
            pool.shutdown();
        }

        return ebeyeResultList;


    }

    public List<ParamOfGetResults> getNumberOfResults(
            List<ParamOfGetResults> paramOfGetResults) throws MultiThreadingException {
        List<ParamOfGetResults> params = new ArrayList<ParamOfGetResults>();
        ExecutorService pool = Executors.newCachedThreadPool();
        try {
            for (ParamOfGetResults param:paramOfGetResults) {
                Callable<Integer> callable = new NumberOfResultsCaller(param);
                Future<Integer> future = pool.submit(callable);
                try {
                    int totalFound = future.get(IEbeyeAdapter.EBEYE_ONLINE_REQUEST_TIMEOUT, TimeUnit.SECONDS);
                    if (totalFound > 0) {
                        param.setTotalFound(totalFound);
                        params.add(param);
                    }
                    
                } catch (InterruptedException ex) {
                    throw  new MultiThreadingException(ex.getMessage(), ex);
                } catch (ExecutionException ex) {
                    throw  new MultiThreadingException(ex.getMessage(), ex);
                } catch (TimeoutException ex) {
                    throw  new MultiThreadingException(ex.getMessage(), ex);
                }

            }
        }
        finally {
            pool.shutdown();
        }
        return params;
    }

    public Map<String, List<Result>> getResults(List<ParamOfGetResults> paramOfGetResultsList) throws MultiThreadingException {
        Map<String, List<Result>> allDomainsResults = new HashMap<String, List<Result>>();
        //getResultsByAccessions(paramOfGetResultsList)

        for (ParamOfGetResults param:paramOfGetResultsList )  {
            List<Result> resultList = this.getResults(param, false);
                    allDomainsResults.put(param.getDomain(), resultList);
        }
        return allDomainsResults;

    }

/*
    public Map<String, String> getNames(String domain, List<String> ids){
        ArrayOfString fields = Transformer
                .transformToArrayOfString(FieldsOfGetNames.getFields());
        ArrayOfString idsArray = Transformer
                .transformToArrayOfString(ids);

        GetEntriesCallable caller = new EbeyeCallable
                .GetEntriesCallable(domain, idsArray, fields);

        ArrayOfArrayOfString results = caller.callGetEntries();
        Map<String,String> resultMap = Transformer.transformToMap(results);
        return resultMap;
    }
*/
    public Map<String,String> getReferencedIds(List<String> ids, String targetDomain) {
        ArrayOfString uniprotIds = Transformer.transformToArrayOfString(ids);
        ArrayOfString fields = Transformer.transformToArrayOfString(
                IEbeyeAdapter.FieldsOfGetResults.id.name());
        String uniprotDomain = IEbeyeAdapter.Domains.uniprot.name();

        GetReferencedEntriesSet caller = new EbeyeCallable.GetReferencedEntriesSet(
                uniprotDomain, uniprotIds, targetDomain, fields);
        ArrayOfEntryReferences rawResults = caller.callGetReferencedEntriesSet();
        Map<String,String> results = Transformer.transformToMap(rawResults);
        return results;
    }
/*
    public Map<String,Map<String,String>> getUniprotXrefIdAndName(List<String> ids, String xRefDomain) {
        ArrayOfString uniprotIds = Transformer.transformToArrayOfString(ids);
        ArrayOfString fields = Transformer.transformToArrayOfString(
                FieldsOfChebiNameMap.getFields()
                );
        String uniprotDomain = IEbeyeAdapter.Domains.uniprot.name();

        GetReferencedEntriesSet caller = new EbeyeCallable.GetReferencedEntriesSet(
                uniprotDomain, uniprotIds, xRefDomain, fields);
        ArrayOfArrayOfString rawResults = caller.callGetReferencedEntriesFlatSet();
        Map<String, Map<String, String>> results = Transformer.transformIdAndNameMap(rawResults);
        return results;
    }
 * 
 */
      public Map<String, String> getNameMapByAccessions(String domain, Collection<String> accessions) {
        Domains domains = IEbeyeAdapter.Domains.valueOf(domain);
        List<String> configFields = null;
        switch (domains) {
            case chebi:
                configFields = IEbeyeAdapter.FieldsOfChebiNameMap.getFields();
                break;
           default: configFields = IEbeyeAdapter.FieldsOfUniprotNameMap.getFields();

        }
        ArrayOfString fields = Transformer
                .transformToArrayOfString(configFields);
        
        //TODO limited array size
        ArrayOfString idsArray = Transformer
                .transformToArrayOfString(accessions);

        GetEntriesCallable caller = new EbeyeCallable
                .GetEntriesCallable(domain, idsArray, fields);

        ArrayOfArrayOfString results = caller.callGetEntries();
        Map<String,String> resultMap = Transformer.transformToMap(results);
        return resultMap;
    }

    public Collection<String> getNameSetByAccessions(String domain, Collection<String> accessions) {
        List<String> fields = new ArrayList<String>();
        fields.add(IEbeyeAdapter.FieldsOfUniprotNameMap.descRecName.name());

        //GetEntriesCallable caller = new EbeyeCallable
           //     .GetEntriesCallable(domain, idsArray, fields);

        GetResultsCallable caller = new GetResultsCallable();
        ArrayOfArrayOfString results = caller.
        callGetResults(domain
                , LuceneQueryBuilder.createAccessionQueryIN(accessions)
                , fields
                //TODO
                , 0
                , 20);
        Set<String> resultSet = Transformer.transformToSet(results);
        return resultSet;

    }

    public Collection<String> getUniprotXrefAccessions(List<ParamOfGetResults> params) throws MultiThreadingException {

        List<Callable<ArrayOfArrayOfString>> callableList
                 = prepareCallableCollection(params);


         //List<ArrayOfArrayOfString> rawResults =
            //      submitAll(paramOfGetResultsList);
         //List<ArrayOfArrayOfString> rawResults = submitAll(callableList);
         List<ArrayOfArrayOfString> rawResults = executeCallables(callableList);
         Collection<String> transformedResults
                    = Transformer.transformFieldValueToList(rawResults, true);
            //Save the content to an object
         /*
        List<List<String>> transformedResults
                = Transformer.transformToList(rawResults);
        //Save the content to an object

        ResultFactory resultFactory = new ResultFactory(
                domain, domainFields);
        List<Result> resultList = resultFactory.getResults(transformedResults);
          *
          */
         return transformedResults;


    }

    public ParamOfGetResults getNumberOfResults(
            ParamOfGetResults param){
        NumberOfResultsCaller callable = new NumberOfResultsCaller(param);
        int totalFound = callable.getNumberOfResults();
        param.setTotalFound(totalFound);
        return param;
    }

    public Collection<String> getRelatedUniprotAccessionSet(ParamOfGetResults param)
            throws MultiThreadingException {
        List<Callable<ArrayOfArrayOfString>> callableList
                 = prepareCallableCollection(param);         
         Collection<String> accessionList = getFieldValue(callableList, true);
         return accessionList;
    }

    public LinkedHashSet<String> getFieldValue(List<Callable<ArrayOfArrayOfString>> callableList, boolean isUNIPROTfield) throws MultiThreadingException {
         List<ArrayOfArrayOfString> rawResults = executeCallables(callableList);
         LinkedHashSet<String> accessionList = Transformer.transformFieldValueToList(rawResults, isUNIPROTfield);
         return accessionList;

    }

    public Set<String> getValueOfFields(List<ParamOfGetResults> paramOfGetResults) throws MultiThreadingException {
            List<Callable<ArrayOfArrayOfString>> callableList
                     = prepareCallableCollection(paramOfGetResults);
             List<ArrayOfArrayOfString> rawResults = executeCallables(callableList);
             Set<String> NameList = Transformer.transformFieldValueToList(rawResults, false);
             return NameList;
    }

    public Set<String> getRelatedUniprotAccessionSet(List<ParamOfGetResults> paramOfGetResults) throws MultiThreadingException {
        Set<String> accessionSet = new LinkedHashSet<String>();
        for (ParamOfGetResults param:paramOfGetResults )  {
            Collection<String> resultsPerDomain = getRelatedUniprotAccessionSet(param);
            if (resultsPerDomain.size() > 0) {
                accessionSet.addAll(resultsPerDomain);
            }
        }
        return accessionSet;
    }

    public Set<String> getValueOfFields(ParamOfGetResults paramOfGetResults) throws MultiThreadingException {
        List<Callable<ArrayOfArrayOfString>> callableList
                 = prepareCallableCollection(paramOfGetResults);
         List<ArrayOfArrayOfString> rawResults = executeCallables(callableList);
         Set<String> NameList = Transformer.transformFieldValueToList(rawResults, false);
         return NameList;
    }

    public Map<String,List<String>> getUniprotRefAccesionsMap(ParamOfGetResults paramOfGetResults) throws MultiThreadingException {
        List<Callable<ArrayOfArrayOfString>> callableList
                 = prepareCallableCollection(paramOfGetResults);
         List<ArrayOfArrayOfString> rawResults = executeCallables(callableList);
         Map<String,List<String>> uniprotRefAccesionsMap = Transformer.transformChebiResults(rawResults, true);
         return uniprotRefAccesionsMap;
    }

    public Collection<String> getNameSetByAccessions(String domain, Collection<String> accessions, int from, int size) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getNrOfResultsOfGetNameSetByAccessions(String domain, Collection<String> accessions) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*
        public Collection<String> getUniprotXrefAccessions(List<ParamOfGetResults> paramOfGetResultsList) throws MultiThreadingException {
        Map<String, List<Result>> allDomainsResults = new HashMap<String, List<Result>>();
        //getResultsByAccessions(paramOfGetResultsList)

        for (ParamOfGetResults param:paramOfGetResultsList )  {
            List<Result> resultList = this.getResults(param, false);
                    allDomainsResults.put(param.getDomain(), resultList);
        }
        return allDomainsResults;

        }

    
    public Collection<String> getUniprotXrefs(String query) {
        String domain = IEbeyeAdapter.Domains.uniprot.name();
        String fieldName = IEbeyeAdapter.FieldsOfGetResults.UNIPROT.name();
        List<String> fields = new ArrayList<String>();
        fields.add(fieldName);
        int totalFound = getNumberOfResults(domain, query);
        Pagination pagination = new Pagination(totalFound, EBEYE_RESULT_LIMIT);
        int nrOfQueries = pagination.calTotalPages();
        int resultSize = IEbeyeAdapter.EBEYE_RESULT_LIMIT;
        Set<String> results = new LinkedHashSet<String>();
        int start = 0;
        ParamOfGetResults param = new ParamOfGetResults(domain, query, fields);
        //TODO
        for (int i = 0; i < nrOfQueries; i++) {
            //if (i == nrOfQueries - 1 && (totalFound % IEbeyeAdapter.EBEYE_RESULT_LIMIT) > 0) {
            //Last page
            if (i == nrOfQueries - 1) {
                resultSize =pagination.getLastPageResults();
            }

            Callable<ArrayOfArrayOfString> callable = new GetResultsCallable(
                    param, start, resultSize);
        }

    }



    public int getNumberOfResults(String domain, String query) {
        NumberOfResultsCaller caller = new NumberOfResultsCaller();
        return caller.getNumberOfResults(domain, query);
    }
*/
}
