package uk.ac.ebi.ep.core.search;

import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import uk.ac.ebi.ep.config.Config;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import org.apache.log4j.Logger;
import uk.ac.ebi.biobabel.lucene.LuceneParser;
import uk.ac.ebi.ep.config.jaxb.Domain;
import uk.ac.ebi.ep.search.exception.EnzymeFinderException;
import uk.ac.ebi.ep.search.exception.InvalidSearchException;
import uk.ac.ebi.ep.search.parameter.SearchParams;
import uk.ac.ebi.ep.search.result.jaxb.EnzymeSearchResults;
import uk.ac.ebi.ep.search.result.jaxb.EnzymeSummaryCollection;
import uk.ac.ebi.ep.util.query.LuceneQueryBuilder;
import uk.ac.ebi.ep.util.validation.Validator;
import uk.ac.ebi.ebeye.param.ParamOfGetAllResults;
import uk.ac.ebi.ep.ebeye.adapter.EbeyeAdapter;
import uk.ac.ebi.ep.ebeye.adapter.IEbeyeAdapter;
import uk.ac.ebi.ep.ebeye.adapter.IEbeyeAdapter.FieldsOfGetResults;
import uk.ac.ebi.ep.ebeye.result.jaxb.Result;
import uk.ac.ebi.ep.ebeye.result.jaxb.Xref;
import uk.ac.ebi.ep.search.result.jaxb.EnzymeSummary;
import uk.ac.ebi.ep.uniprot.adapter.IUniprotAdapter;
import uk.ac.ebi.ep.uniprot.adapter.UniprotAdapter;


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

    public List<String> rankEnzymes(List<String> uniprotIdList) {
        ResultRanker resultRanker = new ResultRanker(uniprotIdList);
        resultRanker.rankResults();
        List<String> rankedResults = resultRanker.getRankedResults();
        return rankedResults;
    }
/*
    public boolean isUniprotResults(List<Result> resultList) {
        Iterator it = resultList.iterator();
        boolean isUniprotResults = false;
        while (it.hasNext()) {
            Result result = (Result)it.next();
            if ((result instanceof UniprotResult)) {
                return true;
            }
        }
        return isUniprotResults;
    }
*/

    public Set<String> getUniprotPrimaAccList(
            List<Result> cachedResults, List<String> ids) {
        Set<String> accessions = new TreeSet<String>();
        for (String id:ids) {
            for (Result result:cachedResults) {
                String selectedId = result.getId();
                if (selectedId.equals(id)) {
                    accessions.add(result.getAcc().get(0));
                    break;
                }
            }
        }
        return accessions;
    }

    public  Map<String, List<Result>> queryResultsForAllDomains() {
        IEbeyeAdapter ebeyeAdapter = new EbeyeAdapter();
        List<String> fieldNames = new ArrayList<String>();
        FieldsOfGetResults[] fields = IEbeyeAdapter.FieldsOfGetResults.values();
        for (FieldsOfGetResults field:fields) {
            String fieldName = field.name();
            fieldNames.add(fieldName);
        }


        //prepare list of parameters
        List<ParamOfGetAllResults> paramOfGetAllResults =
                                    prepareParamOfGetAllResults(searchParams, fieldNames);
        //List<List<Result>> allDomainsResults = null;
        Map<String, List<Result>> allDomainsResults =
                ebeyeAdapter.getAllDomainsResults(paramOfGetAllResults);

        return allDomainsResults;
    }

    public void validateSearchInput(String userKeywords) throws InvalidSearchException {
        if (!Validator.isSearchParamsOK(userKeywords)) {
            throw new InvalidSearchException("Search can only be performed with " +
                    "at leasr one keyword!");
        }
        LuceneParser luceneParser = new LuceneParser();        
        String cleanedKeywords = luceneParser
                .escapeLuceneSpecialChars(userKeywords);
        this.searchParams.setKeywords(cleanedKeywords);
    }

    public EnzymeSearchResults getEnzymes(SearchParams searchParams) throws EnzymeFinderException {
        this.searchParams = searchParams;
        String userKeywords = this.searchParams.getKeywords();
        validateSearchInput(userKeywords);
        EnzymeSearchResults enzymeSearchResults = createEmptyResponse();

        //Unit test: queryResultsForAllDomains, prepareParamOfGetAllResults,
        //ebeyeAdapter.getAllDomainsResults
        Map<String, List<Result>> allDomainsResults = queryResultsForAllDomains();

        //List<Result> cachedIds = Config.enzymesInUniprot;

        List<Result> uniprotMergedResults = this.filterNonEnzymes(allDomainsResults);

        //Old code
        /*
        List<String> uniprotIdList = new ArrayList<String>();

        Iterator it = uniprotMergedResults.iterator();

        while (it.hasNext()) {
            Result result = (Result)it.next();
            uniprotIdList.add(result.getId());
        }

        //Rank return a list of ids
        List<String> rankedResults = new ArrayList<String>();
        rankedResults = this.rankEnzymes(uniprotIdList);
        List<String> topRankedResults = this.getTopRankedResults(
                 rankedResults, searchParams.getStart(), searchParams.getSize()
                );
        Set<String> topRankedAccessions =
        getUniprotPrimaAccList(cachedIds, topRankedResults);
        */
        //get data for the list of top ranked ids
        EnzymeSummaryCollection searchResults = new EnzymeSummaryCollection();

        //Process the pagination

        //int totalFound = rankedResults.size();
        //searchResults.setTotalfound(totalFound);
        //resultsize
        //searchResults.setResultsize(topRankedResults.size());
        int totalFound = uniprotMergedResults.size();
        int size = searchParams.getSize();
        int start =  searchParams.getStart();
        int subListIndex = start+size;
        if (totalFound<subListIndex) {
            subListIndex=totalFound;
        }
        List<Result> briefResultSubset = uniprotMergedResults.subList(
               start, subListIndex);
        
        searchResults.setTotalfound(totalFound);
        //resultsize
        searchResults.setResultsize(briefResultSubset.size());


        searchResults.setResultstartat(searchParams.getStart());
        
        //this.getUniprotResults(topRankedAccessions);
        
        IUniprotAdapter uniprotAdapter = new UniprotAdapter();
        List<EnzymeSummary> enzymeSummaryList = new ArrayList<EnzymeSummary>();
        try {
            enzymeSummaryList = uniprotAdapter.getEnzymeEntries(briefResultSubset);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            java.util.logging.Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            java.util.logging.Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
        searchResults.getEnzymesummary().addAll(enzymeSummaryList);


        enzymeSearchResults.setEnzymesummarycollection(searchResults);
        searchParams.setKeywords(userKeywords);
        return enzymeSearchResults;
        

        //invoke getAllDomainsResults

        //check if the accession numbers are enzymes

        //rank result by ids

        //send getEntries for first 100 records

        //create the same entry for different species

        /*
        List<ResultOfGetNumberOfResults> totalResultsList =
                new ArrayList<ResultOfGetNumberOfResults>();
        totalResultsList = this.getNumberOfResults(this.searchParams);
        //No results found

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


        resultsOfGetEntries.setTotalfound(rankedResults.size());
        //resultsize
        resultsOfGetEntries.setResultsize(topRankedResults.size());
        resultsOfGetEntries.setResultstartat(searchParams.getStart());
        enzymeSearchResults = new EnzymeSearchResults();
        enzymeSearchResults.setEnzymesummarycollection(resultsOfGetEntries);
        return enzymeSearchResults;
         * *
         */
    }
/*
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


        resultsOfGetEntries.setTotalfound(rankedResults.size());
        //resultsize
        resultsOfGetEntries.setResultsize(topRankedResults.size());
        resultsOfGetEntries.setResultstartat(searchParams.getStart());
        enzymeSearchResults = new EnzymeSearchResults();
        enzymeSearchResults.setEnzymesummarycollection(resultsOfGetEntries);        
        return enzymeSearchResults;
    }

    */

    public static List<ParamOfGetAllResults>  prepareParamOfGetAllResults(
            SearchParams searchParams, List<String> resultFields) {
        Iterator<Domain> it = Config.domainList.iterator();
        List<ParamOfGetAllResults> paramList = new ArrayList<ParamOfGetAllResults>();
        while (it.hasNext()) {
            Domain domain = (Domain)it.next();
            String query = LuceneQueryBuilder.createQueryOR(domain, searchParams);
            String domainId = domain.getId();
            //List<String> fields = DataTypeConverter.getConfigResultFields(domain);
            ParamOfGetAllResults paramOfGetAllResults =
                    new ParamOfGetAllResults(domainId, query, resultFields);
            paramList.add(paramOfGetAllResults);
        }
        return paramList;
    }

    public List<Result> getUniprotResults(Set<String> accessions) {
        Iterator accessionsIt = accessions.iterator();
        List<Result> uniprotResults = new ArrayList<Result>();
        while (accessionsIt.hasNext()) {
            String acc = (String)accessionsIt.next();
            uniprotResults.add(getUniprotResult(acc));
        }
        return uniprotResults;
    }
    
    public Result getUniprotResult(String accession) {
        List<Result> accConfigList = Config.enzymesInUniprot;
        Iterator accConfigIt = accConfigList.iterator();
        Result uniprotResult = null;
        while (accConfigIt.hasNext()) {
            uniprotResult = (Result)accConfigIt.next();
            if (uniprotResult.getAcc().contains(accession)) {
                return uniprotResult;
            }
        }
        return uniprotResult;
    }
    
    public List<Result> getUniprotResult(List<Xref> xRefList) {
        List<Result> results = new ArrayList<Result>();
        Iterator it = xRefList.iterator();
        Set<String> uniprotAccList = new TreeSet<String>();
        while (it.hasNext()) {
               Xref uniprotXref = (Xref)it.next();
               List<String> uniprotXrefAccs = uniprotXref.getAcc();
               for (String accession: uniprotXrefAccs) {
                   uniprotAccList.add(accession);
               }
               //results.addAll( getUniprotResults(uniprotXrefAccs));
        }
        results.addAll(getUniprotResults(uniprotAccList));
        return results;
    }
    /*
    public List<Result> getEnzymeUniprot(List<Result> resultList) {
        List<Result> filteredResults = new ArrayList<Result>();
        if (resultList.size() != 0) {
            Iterator it1 = resultList.iterator();
            while (it1.hasNext()) {
                Result resultObj = (Result) it1.next();

                //Uniprot Xref
                List<Xref> accList = resultObj.getXrefs();
                filteredResults.addAll(getUniprotResult(accList));
            }
        }
        return filteredResults;
    }
*/
    public Set<String> mergeUniprotAccessions(List<Result> resultList) {
        Set<String> uniprotAccList = new TreeSet<String>();
        if (resultList.size() != 0) {
            Iterator it1 = resultList.iterator();
            while (it1.hasNext()) {
                Result resultObj = (Result) it1.next();

                //Uniprot Xref
                List<Xref> xRefList = resultObj.getXrefs();
                for (Xref xRef: xRefList) {
                    List<String> accList = xRef.getAcc();
                    for (String acc:accList) {
                        uniprotAccList.add(acc);
                    }
                }
                //filteredResults.addAll(getUniprotResult(accList));
            }
        }
        return uniprotAccList;
    }

    //Results from more than one domains
    public List<Result> filterNonEnzymes(Map<String,List<Result>> results) {
        Set<String> mergedUniprotAccs = mergeUniprotAccessions(results);
        //iterate thru each domain
        List<Result> filteredResults = getUniprotResults(mergedUniprotAccs);

        return filteredResults;
    }

    public Set<String> mergeUniprotAccessions(Map<String,List<Result>> results) {
        Iterator it = results.keySet().iterator();
        Set<String> uniprotAccList = new TreeSet<String>();
        while (it.hasNext()) {
            String key = (String)it.next();
            List<Result> resultList = (List<Result>)results.get(key);
            uniprotAccList.addAll(mergeUniprotAccessions(resultList));
        }
        return uniprotAccList;
    }

    public EnzymeSearchResults createEmptyResponse() {
        EnzymeSearchResults enzymeSearchResults = new EnzymeSearchResults();
        EnzymeSummaryCollection enzymeSummaryCollection = new EnzymeSummaryCollection();
        enzymeSummaryCollection.setTotalfound(0);
        enzymeSearchResults.setEnzymesummarycollection(enzymeSummaryCollection);
        return enzymeSearchResults;
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

  }
