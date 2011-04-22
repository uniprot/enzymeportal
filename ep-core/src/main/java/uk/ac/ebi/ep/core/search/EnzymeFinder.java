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
import uk.ac.ebi.ebeye.ParamGetNumberOfResults;
import uk.ac.ebi.ebeye.ResultOfGetNumberOfResults;
import uk.ac.ebi.ebeye.ResultOfGetReferencedEntriesSet;
import uk.ac.ebi.ebeye.ResultOfGetResultsIds;
import uk.ac.ebi.ep.config.Domain;
import uk.ac.ebi.ep.core.search.EBeyeWsCallable.GetNumberOfResultsCallable;
import uk.ac.ebi.ep.core.search.EBeyeWsCallable.GetResultsIdCallable;
import uk.ac.ebi.ep.search.exception.EnzymeFinderException;
import uk.ac.ebi.ep.search.exception.InvalidSearchException;
import uk.ac.ebi.ep.search.exception.QueryException;
import uk.ac.ebi.ep.search.parameter.SearchParams;
import uk.ac.ebi.ep.search.result.EnzymeSearchResults;
import uk.ac.ebi.ep.search.result.EnzymeSummary;
import uk.ac.ebi.ep.search.result.EnzymeSummaryCollection;
import uk.ac.ebi.ep.search.result.Species;
import uk.ac.ebi.ep.util.query.LuceneQueryBuilder;
import uk.ac.ebi.ep.util.validation.Validator;
import uk.ac.ebi.util.result.DataTypeConverter;
import uk.ac.ebi.util.result.EBeyeDataTypeConverter;
import uk.ac.ebi.util.result.ResultCalculator;
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
public class EnzymeFinder implements IEnzymeFinder {

//********************************* VARIABLES ********************************//
    //protected List<String> uniprotIdList;
    //protected SortedMap<String, Integer> uniprotIdList = new TreeSet<String>();
    //protected EBeyeClient eBeyeClient;
    protected EBISearchService eBISearchService;
    //TODO    
    public static final int TOP_RESULT_SIZE = 100;
    public static final int RESULT_PER_DOMAIN_LIMIT = 20;
    public static final int NUMBER_OF_RECORDS_PER_PAGE = 20;
    public static final String UNIPROT_DOMAIN = "uniprot";
    public static final int START_AT = 0;
    public static final int MAX_RANK_SIZE = 1000;
    protected SearchParams searchParams;


    //public static final String UNIPROT_URL = "http://www.uniprot.org/uniprot/";    
    private static Logger log = Logger.getLogger(EnzymeFinder.class);

//******************************** CONSTRUCTORS ******************************//

    public EnzymeFinder() throws ServiceException {
        //eBeyeClient = new EBeyeClient();
        EBISearchService_Service service = new EBISearchService_Service();        
        eBISearchService = service.getEBISearchServiceHttpPort();
        if (eBISearchService == null) {
            throw new ServiceException("Unable to connect to EBeye Search Service");
        }
        //uniprotIdList = new ArrayList<String>();
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
        this.searchParams = searchParams;
        EnzymeSearchResults enzymeSearchResults = new EnzymeSearchResults();
        List<ParamGetNumberOfResults> queries =
                this.prepareGetNumberOfResultsQueries(searchParams);
        if (queries == null || queries.size()==0) {
            throw new EnzymeFinderException(
                    "Unable to create queries for GetNumberOfResults operation"
                    +" from keywords "
                    +searchParams.getKeywords());
        }
        //List<ResultOfGetResultsIds> resultList= null;
        List<ResultOfGetNumberOfResults> resultOfGetNumberOfResults = null;
        //TODO query uniprot separately
        try {
            resultOfGetNumberOfResults = this.invokeGetNumberOfResults(queries);
        } catch (InterruptedException ex) {
            
        } catch (ExecutionException ex) {
            
        }
        //No results found
        if (resultOfGetNumberOfResults == null
                || resultOfGetNumberOfResults.size() == 0) {
            return createEmptyResponse();
        }

        //This list should contain only queries with total results found > 0
        List<ParamOfGetResultsIds> paramOfGetResultsIdsList =
            prepareGetResultsIdsQueries(resultOfGetNumberOfResults);

        if (paramOfGetResultsIdsList == null
                || paramOfGetResultsIdsList.size() ==0) {
            throw new EnzymeFinderException(
                    "Unable to create queries for GetResultsIds operation"
                    +" from keywords "
                    +searchParams.getKeywords());
        }
        List<ResultOfGetResultsIds> resultList= null;
        try {
            resultList = this.invokeGetResultsIds(paramOfGetResultsIdsList);
        } catch (InterruptedException ex) {
            
        } catch (ExecutionException ex) {
            
        }

        List<String> resultIdsList = EnzymeFinder.getResultsIds(resultList);
        if (resultIdsList == null || resultIdsList.size() == 0) {
            return createEmptyResponse();
        }
        List<ResultOfGetReferencedEntriesSet> uniprotXrefResults = null;
        try {
            uniprotXrefResults = this.invokeGetReferencedEntriesSet(resultList);
        } catch (InterruptedException ex) {
          
        } catch (ExecutionException ex) {
          
        }
        List<String> uniprotIdList = new ArrayList<String>();
        List<String> uniprotResults = getResultsIds(resultList);
        if (uniprotResults != null || uniprotResults.size() > 0) {
            uniprotIdList.addAll(uniprotResults);
        }        
        List<String> uniprotXrefs = getXrefUniprotIds(uniprotXrefResults);
        if (uniprotXrefs != null || uniprotXrefs.size() > 0) {
            uniprotIdList.addAll(uniprotXrefs);
        }

        if (uniprotIdList == null || uniprotIdList.size() == 0) {
            return createEmptyResponse();
        }
        
        //merge uniprot ids recieved from querying directly to uniprot        
        //Filter out uniprot ids that are not enzymes

        List<ResultOfGetResultsIds> enzymeUniprotIds = filterNoneEnzymeIds(uniprotResults);

        enzymeSearchResults = this.rankResults(
                EnzymeFinder.getResultsIds(enzymeUniprotIds)
                );
            //TODO
        /*
        EnzymeFinder resultOfGetNumberOfResults
        int totalFound = ResultCalculator
                .calTotalResultsFound(resultOfGetNumberOfResults);
        int totalEnzymeFound = enzymeUniprotIds.size();
         resultSet.getEnzymesummarycollection().setTotalfound(
                    ResultCalculator.estimateTotalUniprotXrefs(
                    , START_AT, START_AT));
*/
        return enzymeSearchResults;
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
        List<ParamOfGetResultsIds> ResultsIdsParams =
        this.prepareGetResultsIdsQueries(numberOfResultsList);
        List<ResultOfGetResultsIds> enzymeUniprotIds = null;
        try {
            enzymeUniprotIds = this.invokeGetResultsIds(ResultsIdsParams);
        } catch (InterruptedException ex) {
            //java.util.logging.Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            //java.util.logging.Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return enzymeUniprotIds;
    }
    public List<String> getXrefUniprotIds(
            List<ResultOfGetReferencedEntriesSet> xRefList) {
        Iterator it = xRefList.iterator();
        List<String> uniprotIdList = new ArrayList<String>();
        while (it.hasNext()) {
            ResultOfGetReferencedEntriesSet xRef =
            (ResultOfGetReferencedEntriesSet)it.next();
            List<String> uniprotXrefList = xRef.getUniprotXRefList();
            if (uniprotXrefList!=null) {
                uniprotIdList.addAll(uniprotXrefList);
            }

        }
        return uniprotIdList;
    }

    public static List<String> getUniprotIds(
        List<ResultOfGetResultsIds> resultList) {
        Iterator it = resultList.iterator();
        List<String> uniprotIdList = new ArrayList<String>();
        while (it.hasNext()) {
            ResultOfGetResultsIds result =
            (ResultOfGetResultsIds)it.next();
            String domain = result
                    .getParamOfGetResultsIds()
                    .getResultOfGetNumberOfResults()
                    .getParamGetNumberOfResults()
                    .getDomain();
            if (domain.equals(UNIPROT_DOMAIN)) {
                uniprotIdList.addAll(result.getResult().getString());
            }
        }
        return uniprotIdList;
    }

    /**
     * Retieve the ids of all domains in the {@link ResultOfGetResultsIds} list
     * @param resultList
     * @return
     */
    public static List<String> getResultsIds(
        List<ResultOfGetResultsIds> resultList) {
        Iterator it = resultList.iterator();
        List<String> resultsIds = new ArrayList<String>();
        while (it.hasNext()) {
            ResultOfGetResultsIds result =
            (ResultOfGetResultsIds)it.next();
            resultsIds.addAll(result.getResult().getString());
        }
        return resultsIds;
    }
    
    public EnzymeSearchResults rankResults(List<String> uniprotIdList) {
        EnzymeSearchResults resultSet = null;
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
        try {
            //enzyme.printMergedResults();
            resultSet = queryUniprotResults(topResults);
            resultSet.getEnzymesummarycollection().setResultsize(TOP_RESULT_SIZE);

        } catch (ServiceException ex) {
            //Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultSet;

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
                        ResultOfGetNumberOfResults resultOfGetNumberOfResults) {
        int totalFound = resultOfGetNumberOfResults.getTotalFound();
        searchParams.setSize(
                ResultCalculator.calGetResultsIdsSize(totalFound
                                                , NUMBER_OF_RECORDS_PER_PAGE));
        ParamOfGetResultsIds paramOfGetResultsIds
                = new ParamOfGetResultsIds(resultOfGetNumberOfResults, searchParams);
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
            if (resultOfGetNumberOfResults.getTotalFound()>0) {
                ParamOfGetResultsIds paramOfGetResultsIds =
                    prepareGetResultsIdsQuery(resultOfGetNumberOfResults);
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
    public EnzymeSearchResults queryUniprotResults(List<String> IdList) throws ServiceException {
        Iterator it = IdList.iterator();
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
                    /*
                    String accessionXLinks = DataTypeConverter
                                        .uniprotAccessionsToXLinks(resultFieldValue);
                     *
                     */
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
        pagination.paginateResults(numberOfResults, NUMBER_OF_RECORDS_PER_PAGE);
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
                endAt = ((startAt+NUMBER_OF_RECORDS_PER_PAGE));
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
