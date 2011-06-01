package uk.ac.ebi.ep.ebeye.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import uk.ac.ebi.ebeye.param.ParamOfGetAllResults;
import uk.ac.ebi.ebeye.param.ParamOfGetResults;
import uk.ac.ebi.ebeye.util.Calculator;
import uk.ac.ebi.ebeye.util.Transformer;
import uk.ac.ebi.ep.ebeye.adapter.EbeyeCallable.GetResultsCallable;
import uk.ac.ebi.ep.ebeye.adapter.EbeyeCallable.NumberOfResultsCaller;
import uk.ac.ebi.ep.ebeye.result.jaxb.Result;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.webservices.ebeye.ArrayOfArrayOfString;
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
    public List<Result> getAllResults(ParamOfGetAllResults paramOfGetAllResults){
         List<Result> resultList = new ArrayList<Result>();
         /*
        List<ArrayOfArrayOfString> rawResults = getAllEbeyeResults(paramOfGetAllResults);
        List<List<String>> transformedResults = Transformer.transformToList(rawResults);
        //Save the content to an object
        ResultFactory resultFactory = new ResultFactory(
                paramOfGetAllResults.getDomain(), paramOfGetAllResults.getFields());
        resultList = resultFactory.getResults(transformedResults);
        */
        return resultList;

    }

    public Map<String, List<Result>> getAllDomainsResults(
            List<ParamOfGetAllResults> ParamOfGetAllResultsList) throws MultiThreadingException  {
        Map<String, List<Result>>  result = null;
        result = executeCallables(ParamOfGetAllResultsList);

        return result;
    }

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

    public Map<String, List<Result>> executeCallables(
            List<ParamOfGetAllResults> paramOfGetAllResultsList) throws MultiThreadingException {
        Map<String, List<ParamOfGetResults>> nrOfResults =
                                getNumberOfResults(paramOfGetAllResultsList);
        Map<String, List<Result>> allDomainsResults = new HashMap<String, List<Result>>();
        Iterator it = nrOfResults.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            List<ParamOfGetResults> paramOfGetResultsList = nrOfResults.get(key);
            if (paramOfGetResultsList != null) {
                if (paramOfGetResultsList.size() > 0) {
                    List<String> domainFields = paramOfGetResultsList.get(0).getFields();
                    List<Callable<ArrayOfArrayOfString>> callableList
                             = prepareCallableCollection(paramOfGetResultsList);

                     //List<ArrayOfArrayOfString> rawResults =
                        //      submitAll(paramOfGetResultsList);
                     List<ArrayOfArrayOfString> rawResults = submitAll(callableList);

                    List<List<String>> transformedResults
                            = Transformer.transformToList(rawResults);
                    //Save the content to an object
                    ResultFactory resultFactory = new ResultFactory(
                            key, domainFields);
                    List<Result> resultList = resultFactory.getResults(transformedResults);
                    allDomainsResults.put(key, resultList);
                }
            }
        }
        return allDomainsResults;
    }
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
        ExecutorService pool = null;
        List<ArrayOfArrayOfString> resultList = new ArrayList<ArrayOfArrayOfString>();
        try {
            pool = Executors.newCachedThreadPool();
        
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
                    String domain = param.getDomain();
                    String query = param.getQuery();
                    List<String> fields = param.getFields();
                    int start = param.getStart();
                    int size = param.getSize();
                    ArrayOfString ebeyeFields = Transformer.transformToArrayOfString(fields);
                    Callable<ArrayOfArrayOfString> callable =
                            new GetResultsCallable(
                            domain, query, ebeyeFields, start,size);
                    callableList.add(callable);
                }
            }
            return callableList;
        }

        public List<ParamOfGetResults> prepareGetResultsParams (int totalFound
            , ParamOfGetAllResults paramOfGetAllResults) {
        String domain = paramOfGetAllResults.getDomain();
        String query = paramOfGetAllResults.getQuery();
        List<String> fields = paramOfGetAllResults.getFields();
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
                ParamOfGetResults paramOfGetResults =
                        new ParamOfGetResults(domain, query, fields, start,size);
                paramOfGetResults.setTotalFound(totalFound);
                paramList.add(paramOfGetResults);
                start = start+size;
                currentLoop++;
            }
        }
        return paramList;
    }

    public List<Result> getResults(List<ParamOfGetAllResults>
            paramOfGetAllResultsList) throws MultiThreadingException {

        List<ParamOfGetResults> paramList =
                getNrOfResultAccQuery(paramOfGetAllResultsList);
        
        List<Callable<ArrayOfArrayOfString>> callables = prepareCallableCollection(paramList);

        List<ArrayOfArrayOfString> ebeyeResultList = new ArrayList<ArrayOfArrayOfString>();

        ExecutorService pool = Executors.newCachedThreadPool();
        ArrayOfArrayOfString result = null;
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
        }
        List<String> domainFields = paramList.get(0).getFields();
        String domain =  paramList.get(0).getDomain();
            List<List<String>> transformedResults
                    = Transformer.transformToList(ebeyeResultList);
            //Save the content to an object
            ResultFactory resultFactory = new ResultFactory(
                    domain, domainFields);
            List<Result> resultList = resultFactory.getResults(transformedResults);
        return resultList;
    }

    public List<ParamOfGetResults> getNrOfResultAccQuery(
            List<ParamOfGetAllResults> paramOfGetAllResultsList) throws MultiThreadingException {
        List<ParamOfGetResults> params = new ArrayList<ParamOfGetResults>();
        ExecutorService pool = Executors.newCachedThreadPool();
        try {
            for (ParamOfGetAllResults param:paramOfGetAllResultsList) {
                Callable<Integer> callable = new NumberOfResultsCaller(param);
                Future<Integer> future = pool.submit(callable);
                try {
                    int totalFound = future.get(IEbeyeAdapter.EBEYE_ONLINE_REQUEST_TIMEOUT, TimeUnit.SECONDS);
                List<ParamOfGetResults> paramOfGetResultsList =
                        prepareGetResultsParams(totalFound, param);
                        params.addAll(paramOfGetResultsList);
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
}
