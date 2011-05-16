package uk.ac.ebi.ep.ebeye.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
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


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//
    public List<Result> getAllResults(ParamOfGetAllResults paramOfGetAllResults)
                                        throws InterruptedException, ExecutionException {
         List<Result> resultList = new ArrayList<Result>();        
        List<ArrayOfArrayOfString> rawResults = getAllEbeyeResults(paramOfGetAllResults);
        List<List<String>> transformedResults = Transformer.transformToList(rawResults);
        //Save the content to an object
        ResultFactory resultFactory = new ResultFactory(
                paramOfGetAllResults.getDomain(), paramOfGetAllResults.getFields());
        resultList = resultFactory.getResults(transformedResults);

        return resultList;

    }


/*
    public List<Result> getAllResults(ParamOfGetAllResults paramOfGetAllResults)
                                        throws InterruptedException, ExecutionException {
        String domain = paramOfGetAllResults.getDomain();
        List<String> fields = paramOfGetAllResults.getFields();
        List<Result> resultList = new ArrayList<Result>();        
        //getNumberOfResults
        NumberOfResultsCaller caller =
                new EbeyeCallable.NumberOfResultsCaller(paramOfGetAllResults);

        int totalFound = caller.getNumberOfResults();
        List<ArrayOfArrayOfString> rawResults =
                this.getEbeyeResults(totalFound, paramOfGetAllResults);
        List<List<String>> transformedResults = Transformer.transformToList(rawResults);
        //Save the content to an object
        ResultFactory resultFactory = new ResultFactory(domain, fields);
        resultList = resultFactory.getResults(transformedResults);

        return resultList;
    }

    public List<ArrayOfArrayOfString> getEbeyeResults (int totalFound
            , ParamOfGetAllResults paramOfGetAllResults)
            throws InterruptedException, ExecutionException {
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
                    ArrayOfArrayOfString rawResults = (ArrayOfArrayOfString)future.get();     
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
*/

    public List<List<Result>> getAllDomainsResults(
            List<ParamOfGetAllResults> ParamOfGetAllResultsList)
            throws InterruptedException, ExecutionException {
         List<List<Result>> allDomainsResults = new ArrayList<List<Result>>();
        Iterator it = ParamOfGetAllResultsList.iterator();
        while (it.hasNext()) {
            ParamOfGetAllResults param = (ParamOfGetAllResults)it.next();
            allDomainsResults.add(getAllResults(param));
            //Callable callable = new GetAllResultsCallable(param);
        }
        return allDomainsResults;
    }

    public List<List<Result>> executeCallables(
            List<ParamOfGetAllResults> ParamOfGetAllResultsList)
            throws InterruptedException, ExecutionException {
         List<List<Result>> allDomainsResults = new ArrayList<List<Result>>();
        Iterator it = ParamOfGetAllResultsList.iterator();
        ExecutorService pool = Executors.newCachedThreadPool();
        while (it.hasNext()) {
            ParamOfGetAllResults param = (ParamOfGetAllResults)it.next();
            //allDomainsResults.add(getAllResults(param));
            //Callable callable = new GetAllResultsCallable(param);
        NumberOfResultsCaller caller =
                new EbeyeCallable.NumberOfResultsCaller(param);

        int totalFound = caller.getNumberOfResults();

        List<ParamOfGetResults> paramOfGetResultsList =
                                prepareGetResultsParams(totalFound, param);


        List<Future<ArrayOfArrayOfString>> futureList =  pool.invokeAll(
                prepareCallableCollection(paramOfGetResultsList)
                , 2
                , TimeUnit.HOURS
                );
        List<ArrayOfArrayOfString> rawResults = getResultFromThreads(futureList);
        List<List<String>> transformedResults = Transformer.transformToList(rawResults);
        //Save the content to an object
        ResultFactory resultFactory = new ResultFactory(
                param.getDomain(), param.getFields());
        List<Result> resultList = resultFactory.getResults(transformedResults);
        allDomainsResults.add(resultList);
        }
        pool.shutdown();
        return allDomainsResults;
    }

    public List<ArrayOfArrayOfString> getResultFromThreads(
            List<Future<ArrayOfArrayOfString>> futureList )
            throws InterruptedException, ExecutionException {
        List<ArrayOfArrayOfString> resultList = new ArrayList<ArrayOfArrayOfString>();
        Iterator it = futureList.iterator();
        while (it.hasNext()) {
            Future<ArrayOfArrayOfString> future
                    =(Future<ArrayOfArrayOfString>)it.next();
            ArrayOfArrayOfString resultLine = (ArrayOfArrayOfString)future.get();
            resultList.add(resultLine);
        }
        return resultList;
    }
    public List<ArrayOfArrayOfString> getAllEbeyeResults(
            ParamOfGetAllResults paramOfGetAllResults)
                                        throws InterruptedException, ExecutionException {
        NumberOfResultsCaller caller =
                new EbeyeCallable.NumberOfResultsCaller(paramOfGetAllResults);

        int totalFound = caller.getNumberOfResults();
        List<ArrayOfArrayOfString> rawResults =
                this.getEbeyeResults(totalFound, paramOfGetAllResults);
        return rawResults;
    }

        public List<ArrayOfArrayOfString> getEbeyeResults (int totalFound
            , ParamOfGetAllResults paramOfGetAllResults)
            throws InterruptedException, ExecutionException {
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
                    ArrayOfArrayOfString rawResults = (ArrayOfArrayOfString)future.get();
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
        public List<Callable<ArrayOfArrayOfString>> prepareCallableCollection (List<ParamOfGetResults> paramList)
            throws InterruptedException, ExecutionException {
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
            , ParamOfGetAllResults paramOfGetAllResults)
            throws InterruptedException, ExecutionException {
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

        /*
    public List<ArrayOfArrayOfString> getEbeyeResults (int totalFound
            , ParamOfGetAllResults paramOfGetAllResults)
            throws InterruptedException, ExecutionException {
        String domain = paramOfGetAllResults.getDomain();
        String query = paramOfGetAllResults.getQuery();
        List<String> fields = paramOfGetAllResults.getFields();

        List<ArrayOfArrayOfString> resultList = new ArrayList<ArrayOfArrayOfString>();
        List<Callable<ArrayOfArrayOfString>> queue = new ArrayList<Callable<ArrayOfArrayOfString>>();
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
                    queue.add(callable);           
                    start = start+size;
                    currentLoop++;
                }

                    List<Future<ArrayOfArrayOfString>> futures = pool.invokeAll(queue);
                    Iterator fIt = futures.iterator();
                    while (fIt.hasNext()) {
                        Future<ArrayOfArrayOfString> future = (Future<ArrayOfArrayOfString>)fIt.next();
                        ArrayOfArrayOfString rawResults = (ArrayOfArrayOfString)future.get();
                        resultList.add(rawResults);
                    }

            }
            finally {
                pool.shutdown();
            }

        }
        return resultList;
    }
*/

    public List<Result> getResults(ParamOfGetResults paramOfGetResults) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
