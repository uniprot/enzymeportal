package uk.ac.ebi.ep.util.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import uk.ac.ebi.ebeye.ParamOfGetResultsIds;
import uk.ac.ebi.ebeye.ResultOfGetNumberOfResults;
import uk.ac.ebi.ep.config.Domain;
import uk.ac.ebi.ep.config.SearchField;
import uk.ac.ebi.ep.search.exception.QueryException;
import uk.ac.ebi.ep.search.parameter.SearchParams;
import uk.ac.ebi.ep.search.result.Pagination;

/**
 * Hello world!
 *
 */
public class LuceneQueryBuilder {
    public static final String ENZYME_FILTER =
            "EC:(1* OR 2* OR 3* OR 4* OR 5* OR 6* OR 7*)";
    public static final int EBEYE_MAX_RESULTS_PER_QUERY = 100;
    //133343 results does not pass the load test
    public static final int MAX_RESULTS = 100000;

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }

    public static String createQueryOR(Domain domain, SearchParams searchParams) {
        StringBuffer query = new StringBuffer();
        //System.out.println(domain.getId());
        //System.out.println(domain.getName());
        List<SearchField> SearchFieldList = domain.getSearchFieldList().getSearchField();
        Iterator fieldIt = SearchFieldList.iterator();
        int listLength = SearchFieldList.size();
        int counter = 1;
        query.append("(");
        while (fieldIt.hasNext()) {
            SearchField searchField = (SearchField) fieldIt.next();            
            query.append(searchField.getId());
            query.append(":");
            query.append(searchParams.getKeywords());
            query.append("*");
            if (counter <listLength) {
                query.append(" OR ");
            }
            else {
                query.append(")");
            }
            
            counter++;
        }
            if (domain.getId().equalsIgnoreCase("uniprot")) {
                query.append(" AND " +ENZYME_FILTER);
            }

        //System.out.println(query.toString());
        return query.toString();
    }

    public static String createQueryToGetEnzymeOnly(String uniprotId){
        StringBuffer query = new StringBuffer();
        query.append("id:");
        query.append(uniprotId);
        query.append(" AND " );
        query.append(ENZYME_FILTER);
        return query.toString();
    }

    public static String createQueryIN(List<String> ids) {
        StringBuffer query = new StringBuffer();
        Iterator it = ids.iterator();
        query.append("id:(");
        int counter = 1;
        while(it.hasNext()) {
            String id = (String)it.next();
            query.append(id);
            if (counter <ids.size()) {
                query.append(" OR ");
            }
            counter++;
        }
         query.append(")");
        return query.toString();
    }

    public static String createUniprotQueryForEnzyme(List<String> ids) {
        return createQueryIN(ids) + " AND " + ENZYME_FILTER;
    }

    public static ParamOfGetResultsIds prepareGetResultsIdsQuery(
                        ResultOfGetNumberOfResults resultOfGetNumberOfResults
                        , int start, int size) throws QueryException {
        //TODO - This size should be the size of the final result,
        //not the size per query
        //searchParams.setSize(
           //     ResultCalculator.calGetResultsIdsSize(totalFound
              //                                  , NUMBER_OF_RECORDS_PER_PAGE));
        ParamOfGetResultsIds paramOfGetResultsIds
                = new ParamOfGetResultsIds(
                        resultOfGetNumberOfResults
                        ,start
                        ,size);
        if (paramOfGetResultsIds == null) {
           throw new QueryException(
                    "Unable to create query for GetResultsIds operation");
        }
        return paramOfGetResultsIds;

    }


    public static List<ParamOfGetResultsIds> prepareGetResultsIdsQueries(
            List<ResultOfGetNumberOfResults> resultOfGetNumberOfResultsList) 
            throws QueryException {

        //    , int start, int configResultSize
        Iterator it = resultOfGetNumberOfResultsList.iterator();
        List<ParamOfGetResultsIds> paramOfGetResultsIdsList
                = new ArrayList<ParamOfGetResultsIds>();
        //loop to process each domain
        while (it.hasNext()){
            ResultOfGetNumberOfResults resultOfGetNumberOfResults
                    = (ResultOfGetNumberOfResults)it.next();
            int totalPerDomain = resultOfGetNumberOfResults.getTotalFound();
            if (totalPerDomain>0) {
                //TODO: if the size > 100 what happens?
                //int size = ResultCalculator.calGetResultsIdsSize(totalPerDomain
                   //                     , EBEYE_MAX_RESULTS);                
                Pagination pagination = new Pagination(totalPerDomain, EBEYE_MAX_RESULTS_PER_QUERY);
                pagination.calTotalPages();
                int lastPageResult = pagination.getLastPageResults();
                int numberOfQueries =  pagination.getTotalPages();
                //cal the size of the result
                int resultSize = EBEYE_MAX_RESULTS_PER_QUERY;

                ParamOfGetResultsIds paramOfGetResultsIds = null;
                int start = 0;
                int counter = 0;
                //loop to process results > 100
                while (counter < numberOfQueries) {
                    counter++;
                    //last page result size
                    if (counter==numberOfQueries) {
                        resultSize = lastPageResult;
                    }
                    paramOfGetResultsIds =
                        prepareGetResultsIdsQuery(resultOfGetNumberOfResults
                        , start
                        , resultSize
                        );
                    paramOfGetResultsIdsList.add(paramOfGetResultsIds);
                    start = start+resultSize;
                    if (start > MAX_RESULTS) {
                        break;
                    }
                }
            }
        }
        if (paramOfGetResultsIdsList == null
                || paramOfGetResultsIdsList.size() ==0) {
            throw new QueryException(
                    "Unable to create queries for GetResultsIds operation");
        }
        return paramOfGetResultsIdsList;
    }


}
