package uk.ac.ebi.ep.ebeye.adapter;

import java.util.List;
import java.util.concurrent.Callable;
import uk.ac.ebi.ebeye.ParamGetEntries;
import uk.ac.ebi.ebeye.ResultOfGetNumberOfResults;
import uk.ac.ebi.ebeye.param.ParamGetNumberOfResults;
import uk.ac.ebi.ebeye.util.Transformer;
import uk.ac.ebi.ep.search.result.jaxb.EnzymeSummaryCollection;
import uk.ac.ebi.util.result.EBeyeDataTypeConverter;
import uk.ac.ebi.webservices.ebeye.ArrayOfArrayOfString;
import uk.ac.ebi.webservices.ebeye.ArrayOfString;
import uk.ac.ebi.webservices.ebeye.EBISearchService;
import uk.ac.ebi.webservices.ebeye.EBISearchService_Service;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class EbeyeCallable {

//********************************* VARIABLES ********************************//
    
    private static EBISearchService eBISearchService
            = new EBISearchService_Service().getEBISearchServiceHttpPort();


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//
//******************************** INNER CLASS *******************************//

    public static class NumberOfResultsCaller
            implements Callable<Integer> {
        protected ParamGetNumberOfResults paramGetNumberOfResults;

        public NumberOfResultsCaller(
                ParamGetNumberOfResults paramGetNumberOfResults) {
            this.paramGetNumberOfResults = paramGetNumberOfResults;
        }

        public ParamGetNumberOfResults getParamGetNumberOfResults() {
            return paramGetNumberOfResults;
        }

        public void setParamGetNumberOfResults(
                ParamGetNumberOfResults paramGetNumberOfResults) {
            this.paramGetNumberOfResults = paramGetNumberOfResults;
        }

        /**
         *
         * @return an {@link ResultOfGetNumberOfResults} object that might the
         * total result found equal to 0.
         */
        public Integer getNumberOfResults() {
            int totalFound = eBISearchService.getNumberOfResults(
                    paramGetNumberOfResults.getDomain()
                    , paramGetNumberOfResults.getQuery());
            return totalFound;
        }

        public Integer call() throws Exception {
           return getNumberOfResults();
        }

    }

//******************************** INNER CLASS *******************************//
    public static class GetResultsCallable
            implements Callable<ArrayOfArrayOfString> {
        protected String domain;
        protected String query;
        protected ArrayOfString fields;
        protected int start;
        protected int size;

        public GetResultsCallable(
                String domain
                , String query
                , ArrayOfString fields
                , int start, int size) {
            this.domain = domain;
            this.fields = fields;
            this.query = query;
            this.start = start;
            this.size = size;
        }


        public ArrayOfArrayOfString call() throws Exception {
            return callGetResults();
        }

        public ArrayOfArrayOfString callGetResults() {            
            ArrayOfArrayOfString EbeyeResult = eBISearchService
                    .getResults(domain, query, fields, start, size);
            return EbeyeResult;
        }

    }

//getAllDomainsResults

//******************************** INNER CLASS *******************************//
/*
    public static class GetAllResultsCallable
            implements Callable<List<ArrayOfArrayOfString>> {
        ParamOfGetAllResults paramOfGetAllResults;

        public GetAllResultsCallable(ParamOfGetAllResults paramOfGetAllResults) {
            this.paramOfGetAllResults = paramOfGetAllResults;
        }


        public List<ArrayOfArrayOfString> call() throws Exception {
            return getAllResults();
        }

    public List<ArrayOfArrayOfString> getAllResults()
                                        throws InterruptedException, ExecutionException {
        NumberOfResultsCaller caller =
                new EbeyeCallable.NumberOfResultsCaller(paramOfGetAllResults);

        int totalFound = caller.getNumberOfResults();
        List<ArrayOfArrayOfString> rawResults =
                this.getEbeyeResults(totalFound);
        return rawResults;
    }

    public List<ArrayOfArrayOfString> getEbeyeResults (int totalFound)
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

    }
*/

//******************************** INNER CLASS *******************************//
    public static class GetEntriesCallable
            implements Callable<EnzymeSummaryCollection> {

        protected ParamGetEntries paramGetEntries;

        public GetEntriesCallable() {
        }

        public GetEntriesCallable(ParamGetEntries paramGetEntries) {
            this.paramGetEntries = paramGetEntries;
        }

        public ParamGetEntries getParamGetEntries() {
            return paramGetEntries;
        }

        public void setParamGetEntries(ParamGetEntries paramGetEntries) {
            this.paramGetEntries = paramGetEntries;
        }

        public EnzymeSummaryCollection call() throws Exception {
            return getEntries();
        }


        public EnzymeSummaryCollection getEntries() {
            ArrayOfArrayOfString results = callGetEntries();
            EnzymeSummaryCollection enzymeSummaryCollection = null;
                   //getEntriesResultsToEnzymeSummaryCollection(results);
            return enzymeSummaryCollection;
        }
/*
    public static EnzymeSummaryCollection
            getEntriesResultsToEnzymeSummaryCollection (
                                            ArrayOfArrayOfString results) {
        List<ArrayOfString> resultList = results.getArrayOfString();
        Iterator it = resultList.iterator();
        EnzymeSummaryCollection enzymes = new EnzymeSummaryCollection();
        while (it.hasNext()) {
            ArrayOfString result = (ArrayOfString)it.next();
             EnzymeSummary enzymeSummary =
                     getEntryResultToEnzymeSummary(result.getString());
             enzymeSummary.setFunction();
             enzymeSummary.setEc();
             enzymeSummary.getRelatedspecies().add();
             enzymeSummary.getSynonym().add();

                enzymes.getEnzymesummary().add(enzymeSummary);
        }
         return enzymes;
    }

*/
        public ArrayOfArrayOfString callGetEntries() {
                List<String> ids = paramGetEntries.getEntries();
                ArrayOfString IdArray = Transformer.transformToArrayOfString(ids);
                String domainId = paramGetEntries.getDomain();
                ArrayOfString resultFields = Transformer.transformToArrayOfString(
                        paramGetEntries.getFields());
                ArrayOfArrayOfString result = eBISearchService
                        .getEntries(domainId, IdArray, resultFields);
                return result;
            }

/*
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
*/
    }
}
