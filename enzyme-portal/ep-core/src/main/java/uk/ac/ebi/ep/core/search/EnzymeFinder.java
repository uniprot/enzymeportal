package uk.ac.ebi.ep.core.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import javax.xml.bind.JAXBException;
import javax.xml.rpc.ServiceException;
import org.apache.log4j.Logger;
import uk.ac.ebi.ep.config.Domain;
import uk.ac.ebi.ep.search.parameter.SearchParams;
import uk.ac.ebi.ep.search.result.EnzymeSearchResults;
import uk.ac.ebi.ep.search.result.EnzymeSummary;
import uk.ac.ebi.ep.search.result.EnzymeSummaryCollection;
import uk.ac.ebi.ep.search.result.Species;
import uk.ac.ebi.ep.util.query.LuceneQueryBuilder;
import uk.ac.ebi.util.result.DataTypeConverter;
import uk.ac.ebi.util.result.EBeyeDataTypeConverter;
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
public class EnzymeFinder implements IEnzymeFinder {

//********************************* VARIABLES ********************************//
    protected List<String> uniprotIdList;
    //protected SortedMap<String, Integer> uniprotIdList = new TreeSet<String>();
    //protected EBeyeClient eBeyeClient;
    protected EBISearchService eBISearchService;
    //TODO
    public static final int TOP_RESULT_SIZE = 100;
    public static final int RESULT_PER_DOMAIN_LIMIT = 20;
    public static final int NUMBER_OF_RECORDS_PER_PAGE = 20;
    public static final String UNIPROT_DOMAIN = "uniprot";
    public static final int START_AT = 0;
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
        uniprotIdList = new ArrayList<String>();
    }


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//
/*
    public EnzymeSearchResults find(SearchParams searchInput) {
        String query = null;
        List<Domain> domainList = Config.domainList;
        Iterator it = domainList.iterator();
        ArrayOfString result = null;
        EnzymeSearchResults resultSet = null;
        int totalResultsFound = 0;
        while (it.hasNext()) {
            Domain domain = (Domain)it.next();
            query = LuceneQueryBuilder.createQueryOR(domain, searchInput);
            String domainId = domain.getId();
            log.debug("Domain: " +domainId);
            log.debug("getResultsIds query: " +query);
            log.debug("Start at: " +START_AT);
            log.debug("Finish at: " +RESULT_PER_DOMAIN_LIMIT);

            log.debug("Sending getNumberOfResults request from " +eBISearchService);
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
                result = invokeGetResultsIds(domainId, query, START_AT, resultLimit);

                //end test thread
                //result = eBISearchService
                   //     .getResultsIds(domainId, query, START_AT, resultLimit);
            if (result != null) {
                log.debug("Results: "
                        + EBeyeDataTypeConverter.convertArrayOfStringToString(result));

                //ArrayOfString result = eBISearchService.getAllResultsIds(domainId, query);
                if (domainId.equals(UNIPROT_DOMAIN)) {
                    this.uniprotIdList.addAll(result.getString());
                } //If the domain is not uniprot then the results must be converted
                //into uniprot ids
                else {
                    try {
                        ArrayOfEntryReferences uniprotIds = getUniprotIds(domainId, result);
                        List<String> refList = EBeyeDataTypeConverter.convertArrayOfEntryReferencesToList(uniprotIds);

                        if (domainId.equals("intenz")) {
                            //if the domain is intenz then there is no need to check if
                            //the entry is enzyme.
                             
                            //addUniprotIdsToResults(uniprotIds);
                            this.uniprotIdList.addAll(refList);
                        } else {
                            List<String> uniprotIdsEnzymeOnly = filterOutNonEnzymeUniprot(
                                    refList);
                            this.uniprotIdList.addAll(uniprotIdsEnzymeOnly);
                            //addUniprotIdsToResults(uniprotIds);
                        }

                    } catch (ServiceException ex) {
                        // Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

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
            resultSet.getEnzymesummarycollection().setTotalfound(totalResultsFound);
            resultSet.getEnzymesummarycollection().setResultsize(TOP_RESULT_SIZE);

        } catch (ServiceException ex) {
            //Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultSet;
    }
*/
    public EnzymeSearchResults find(SearchParams searchInput) {
        List<ParamOfGetResultsIds> queries = 
                this.prepareGetResultsIdsQueries(searchInput);
        Map<String, ArrayOfString> resultMap= null;
        try {
            resultMap = this.invokeGetResultsIds(queries);
        } catch (InterruptedException ex) {
            //java.util.logging.Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            //java.util.logging.Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<String> mergedResults = this.covertResultsToUniprotIds(resultMap);
        EnzymeSearchResults enzymeSearchResults = this.rankResults(mergedResults);
        return enzymeSearchResults;
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
            //TODO
            resultSet.getEnzymesummarycollection().setTotalfound(uniprotIdList.size());
            resultSet.getEnzymesummarycollection().setResultsize(TOP_RESULT_SIZE);

        } catch (ServiceException ex) {
            //Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultSet;

    }
    public List<String> covertResultsToUniprotIds(Map<String,ArrayOfString> resultMap) {
        List<String> uniprotIds = new ArrayList<String>();
        if (resultMap == null) {
            return null;
        }
        Iterator it = resultMap.keySet().iterator();
        while (it.hasNext()) {
            String domainId = (String)it.next();
            ArrayOfString result = resultMap.get(domainId);
                if (domainId.equals(UNIPROT_DOMAIN)) {
                    uniprotIds.addAll(result.getString());
                } //If the domain is not uniprot then the results must be converted
                //into uniprot ids
                else {
                    try {
                        ArrayOfEntryReferences uniprotXRefs = getUniprotIds(domainId, result);
                        List<String> refList = EBeyeDataTypeConverter.convertArrayOfEntryReferencesToList(uniprotXRefs);

                        if (domainId.equals("intenz")) {
                            /*if the domain is intenz then there is no need to check if
                            the entry is enzyme.
                             */
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

    public List<ParamOfGetResultsIds> prepareGetResultsIdsQueries(SearchParams searchParams) {
        String query = null;
        List<Domain> domainList = Config.domainList;
        Iterator it = domainList.iterator();
        ArrayOfString result = null;
        EnzymeSearchResults resultSet = null;
        int totalResultsFound = 0;
        List<ParamOfGetResultsIds> params = new ArrayList<ParamOfGetResultsIds>();
        while (it.hasNext()) {
            Domain domain = (Domain)it.next();
            query = LuceneQueryBuilder.createQueryOR(domain, searchParams);
            String domainId = domain.getId();
            log.debug("Domain: " +domainId);
            log.debug("getResultsIds query: " +query);
            log.debug("Start at: " +START_AT);
            log.debug("Finish at: " +RESULT_PER_DOMAIN_LIMIT);
            /*
            System.out.println("Domain: " +domainId);
            System.out.println("getResultsIds query: " +query);
            System.out.println("Start at: " +START_AT);
            System.out.println("Finish at: " +RESULT_PER_DOMAIN_LIMIT);
             *
             */
            log.debug("Sending getNumberOfResults request from " +eBISearchService);
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
                ParamOfGetResultsIds param = new ParamOfGetResultsIds(
                        domainId, query, START_AT, resultLimit);
                param.setTotalResultFound(totalResultsFound);
                params.add(param);
        }
        return params;
    }
    public Map<String,ArrayOfString> invokeGetResultsIds(List<ParamOfGetResultsIds> paramList) throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newFixedThreadPool(paramList.size());
        Iterator it = paramList.iterator();
         Map<String,ArrayOfString> resulMap = new Hashtable<String,ArrayOfString>();
        while (it.hasNext()) {
            ParamOfGetResultsIds param = (ParamOfGetResultsIds)it.next();
            Callable<ArrayOfString> callable = new EBeyeWsCallable(param);
            Future<ArrayOfString> future = pool.submit(callable);
            resulMap.put(param.getDomain(), (ArrayOfString)future.get());
        }
         pool.shutdown();
         return resulMap;

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


    public ArrayOfEntryReferences getUniprotIds(String domainId, ArrayOfString result) throws ServiceException{
        ArrayOfString resultRefFields= new ArrayOfString();
        resultRefFields.getString().add("id");
        ArrayOfEntryReferences refSearchResult = eBISearchService
                .getReferencedEntriesSet(domainId, result, UNIPROT_DOMAIN, resultRefFields);
         return refSearchResult;
    }
  

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
}
