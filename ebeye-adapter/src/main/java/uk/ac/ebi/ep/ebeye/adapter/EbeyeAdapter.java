package uk.ac.ebi.ep.ebeye.adapter;

import java.util.ArrayList;
import java.util.HashMap;
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
            List<ParamOfGetAllResults> ParamOfGetAllResultsList)  {
        Map<String, List<Result>>  result = null;
        result = executeCallables(ParamOfGetAllResultsList);

        return result;
    }

    public Map<String, List<Result>> executeCallables(
            List<ParamOfGetAllResults> ParamOfGetAllResultsList) {
        Map<String, List<Result>> allDomainsResults = new HashMap<String, List<Result>>();
        Iterator it = ParamOfGetAllResultsList.iterator();

            while (it.hasNext()) {
                ParamOfGetAllResults param = (ParamOfGetAllResults) it.next();
                NumberOfResultsCaller caller =
                        new EbeyeCallable.NumberOfResultsCaller(param);

                int totalFound = caller.getNumberOfResults();
                if (totalFound > IEbeyeAdapter.EBEYE_RESULTSET_LIMIT) {
                    totalFound = IEbeyeAdapter.EBEYE_RESULTSET_LIMIT;
                }

                List<ParamOfGetResults> paramOfGetResultsList =
                        prepareGetResultsParams(totalFound, param);
                 List<Callable<ArrayOfArrayOfString>> callableList
                         = prepareCallableCollection(paramOfGetResultsList);

                 //List<ArrayOfArrayOfString> rawResults =
                    //      submitAll(paramOfGetResultsList);
                 List<ArrayOfArrayOfString> rawResults = submitAll(callableList);

                List<List<String>> transformedResults
                        = Transformer.transformToList(rawResults);
                //Save the content to an object
                ResultFactory resultFactory = new ResultFactory(
                        param.getDomain(), param.getFields());
                List<Result> resultList = resultFactory.getResults(transformedResults);
                allDomainsResults.put(param.getDomain(), resultList);
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
            List<Callable<ArrayOfArrayOfString>> callableList){
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
                paramList.add(paramOfGetResults);
                start = start+size;
                currentLoop++;
            }
        }
        return paramList;
    }

    public List<Result> getResults(ParamOfGetResults paramOfGetResults) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
