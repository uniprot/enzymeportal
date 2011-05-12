package uk.ac.ebi.ep.ebeye.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import uk.ac.ebi.ebeye.param.ParamGetNumberOfResults;
import uk.ac.ebi.ebeye.param.ParamOfGetResults;
import uk.ac.ebi.ep.ebeye.result.jaxb.Result;
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



}
