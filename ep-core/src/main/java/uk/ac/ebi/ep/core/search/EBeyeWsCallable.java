package uk.ac.ebi.ep.core.search;

import java.util.List;
import java.util.concurrent.Callable;
import uk.ac.ebi.ebeye.ParamGetEntries;
import uk.ac.ebi.ebeye.ParamGetNumberOfResults;
import uk.ac.ebi.ebeye.ParamOfGetResultsIds;
import uk.ac.ebi.ebeye.ResultOfGetNumberOfResults;
import uk.ac.ebi.ebeye.ResultOfGetReferencedEntriesSet;
import uk.ac.ebi.ebeye.ResultOfGetResultsIds;
import uk.ac.ebi.ep.search.result.EnzymeSummaryCollection;
import uk.ac.ebi.util.result.EBeyeDataTypeConverter;
import uk.ac.ebi.webservices.ebeye.ArrayOfArrayOfString;
import uk.ac.ebi.webservices.ebeye.ArrayOfEntryReferences;
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
public class EBeyeWsCallable {

//********************************* VARIABLES ********************************//
    private static EBISearchService eBISearchService
            = new EBISearchService_Service().getEBISearchServiceHttpPort();


//******************************** INNER CLASS *******************************//

    public static class GetNumberOfResultsCallable 
            implements Callable<ResultOfGetNumberOfResults> {
        protected ParamGetNumberOfResults paramGetNumberOfResults;

        public GetNumberOfResultsCallable(
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
        public ResultOfGetNumberOfResults callGetResultsIds() {
            ResultOfGetNumberOfResults resultOfGetNumberOfResults=
                    new ResultOfGetNumberOfResults();
            int totalFound = eBISearchService.getNumberOfResults(
                    paramGetNumberOfResults.getDomain()
                    , paramGetNumberOfResults.getQuery());                 
                resultOfGetNumberOfResults
                    .setParamGetNumberOfResults(paramGetNumberOfResults);
                resultOfGetNumberOfResults.setTotalFound(totalFound);
            return resultOfGetNumberOfResults;
        }

        public ResultOfGetNumberOfResults call() throws Exception {
           return callGetResultsIds();
        }
    
    }


//******************************** INNER CLASS *******************************//
    public static class GetResultsIdCallable
            implements Callable<ResultOfGetResultsIds> {

        protected ParamOfGetResultsIds paramOfGetResultsIds;

        public GetResultsIdCallable(ParamOfGetResultsIds paramOfGetResultsIds) {
            this.paramOfGetResultsIds = paramOfGetResultsIds;
        }

        public ResultOfGetResultsIds call() throws Exception {
            return callGetResultsIds();
        }

        public ResultOfGetResultsIds callGetResultsIds() {
            ResultOfGetResultsIds result = new ResultOfGetResultsIds();
            int start = paramOfGetResultsIds.getStart();
            int size = paramOfGetResultsIds.getSize();
            String domain = paramOfGetResultsIds
                    .getResultOfGetNumberOfResults()
                    .getParamGetNumberOfResults()
                    .getDomain();
            String query = paramOfGetResultsIds
                    .getResultOfGetNumberOfResults()
                    .getParamGetNumberOfResults()
                    .getQuery();
            ArrayOfString EbeyeResult = eBISearchService
                    .getResultsIds(domain, query, start, size);
            //result.setResultOfGetNumberOfResults(paramOfGetResultsIds)
            result.setParamOfGetResultsIds(paramOfGetResultsIds);
            result.setResult(EbeyeResult);
            return result;
        }

    }
    
//******************************** INNER CLASS *******************************//
    public static class GetReferencedEntriesSetCallable
            implements Callable<ResultOfGetReferencedEntriesSet> {
        protected ResultOfGetResultsIds resultOfGetResultsIds;

        public GetReferencedEntriesSetCallable(
                ResultOfGetResultsIds resultOfGetResultsIds) {
            this.resultOfGetResultsIds = resultOfGetResultsIds;
        }

        public ResultOfGetResultsIds getResultOfGetResultsIds() {
            return resultOfGetResultsIds;
        }

        public void setResultOfGetResultsIds(
                ResultOfGetResultsIds resultOfGetResultsIds) {
            this.resultOfGetResultsIds = resultOfGetResultsIds;
        }
        


        public ResultOfGetReferencedEntriesSet call() throws Exception {
            return CallGetReferencedEntriesSet();
        }

        public ResultOfGetReferencedEntriesSet CallGetReferencedEntriesSet() {
            ResultOfGetReferencedEntriesSet resultOfGetReferencedEntriesSet
                    = new ResultOfGetReferencedEntriesSet();
            resultOfGetReferencedEntriesSet
                    .setResultOfGetResultsIds(resultOfGetResultsIds);
        ArrayOfString resultRefFields= new ArrayOfString();
        resultRefFields.getString().add("id");
        ArrayOfString result = this.resultOfGetResultsIds.getResult();
        String domain = this.resultOfGetResultsIds
                .getParamOfGetResultsIds()
                .getResultOfGetNumberOfResults()
                .getParamGetNumberOfResults()
                .getDomain();
        //TODO uniprot should not be hardcoded
        ArrayOfEntryReferences refSearchResult = null;
        if (!domain.equals("uniprot")) {
            refSearchResult = eBISearchService
                    .getReferencedEntriesSet(domain, result, "uniprot", resultRefFields);
            if (refSearchResult != null) {

                 resultOfGetReferencedEntriesSet.setUniprotXRefList(
                        EBeyeDataTypeConverter
                                .convertArrayOfEntryReferencesToList(refSearchResult)
                                );
                 //Filter out uniprot ids that are not enzymes
            }
        }
         return resultOfGetReferencedEntriesSet;
        }


    }

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
            EnzymeSummaryCollection enzymeSummaryCollection =
                    EBeyeDataTypeConverter
                    .getEntriesResultsToEnzymeSummaryCollection(results);
            return enzymeSummaryCollection;
        }


        public ArrayOfArrayOfString callGetEntries() {
                List<String> ids = paramGetEntries.getEntries();
                ArrayOfString IdArray = EBeyeDataTypeConverter
                        .listToArrayOfString(ids);
                String domainId = paramGetEntries.getDomain();
                ArrayOfString resultFields = EBeyeDataTypeConverter
                        .listToArrayOfString(paramGetEntries.getFields());
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
