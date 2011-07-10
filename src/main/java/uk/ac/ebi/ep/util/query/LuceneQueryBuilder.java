package uk.ac.ebi.ep.util.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import uk.ac.ebi.biobabel.lucene.LuceneParser;
//import uk.ac.ebi.ebeye.ParamOfGetResultsIds;
//import uk.ac.ebi.ebeye.ResultOfGetNumberOfResults;
import uk.ac.ebi.ep.config.Domain;
import uk.ac.ebi.ep.config.SearchField;
import uk.ac.ebi.ep.search.model.SearchParams;

/**
 * Hello world!
 *
 */
public class LuceneQueryBuilder {
    public static final String LUCENE_EQUAL = ":";
    public static final String LUCENE_QUOTE = "\"";
    public static final String ENZYME_FILTER =
            "EC:(1* OR 2* OR 3* OR 4* OR 5* OR 6* OR 7*)";
    public static final String UNIPROT_ID_FIELD ="id";
    public static final String UNIPROT_NAME_FIELD ="name";
    public static final String ACCESSION_FIELD ="acc";
    public static final String LUCENE_WILDCARD ="*";
    public static final int EBEYE_MAX_RESULTS_PER_QUERY = 100;
    public static final String ENZYME_FILTER_UNIPROTAPI =
            "ec:*.*";

     //public static LuceneParser luceneParser = new LuceneParser();

    //public static final String FIELD_SPECIES_NAME ="organism_species";
    public static final String UNIPROT_SPECIES_FIELD ="organism";
    public static final String EBEYE_SPECIES_FIELD ="organism_scientific_name";
    //public static final String[] FIELD_SPECIES_ARRAY = {FIELD_SPECIES_NAME,FIELD_SPECIES_NAME_SCI};
    //public static final List<String> FIELD_SPECIES_LIST = Arrays.asList(FIELD_SPECIES_ARRAY);

    //133343 results does not pass the load test
    //public static final int MAX_RESULTS = 100000;
    public static LuceneParser luceneParser = new LuceneParser();  

    public static String createGetRelatedUniprotAccessionsQueries(Domain domain, SearchParams searchParams) {
        StringBuffer query = new StringBuffer();
        //System.out.println(domain.getId());
        //System.out.println(domain.getName());
        List<SearchField> SearchFieldList = domain.getSearchFieldList().getSearchField();
        List<String> fieldNames = new ArrayList<String>();
        for (SearchField field: SearchFieldList) {
            fieldNames.add(field.getId());
        }
        String keywords = searchParams.getText();
        query.append(createFieldsQuery(fieldNames,keywords));
        return query.toString();
    }

        public static String createGetUniprotFieldQueries(Domain domain, SearchParams searchParams) {
        StringBuffer query = new StringBuffer();
        //System.out.println(domain.getId());
        //System.out.println(domain.getName());
        List<SearchField> SearchFieldList = domain.getSearchFieldList().getSearchField();
        List<String> fieldNames = new ArrayList<String>();
        for (SearchField field: SearchFieldList) {
            fieldNames.add(field.getId());
        }
        String keywords = searchParams.getText();
        query.append(createFieldsQuery(fieldNames,keywords));
        query.append(" AND " +ENZYME_FILTER);
        //add species filters
        /*
        List<String> speciesFilter = searchParams.getSpecies();
        if ( speciesFilter!= null) {
            if (speciesFilter.size() > 0) {
                String simpleQuery = query.toString();
                query.append(addSpeciesFilterQuery(
                        simpleQuery, EBEYE_SPECIES_FIELD, speciesFilter));
            }
        }
        */
        return query.toString();
    }


    public static String createFieldsQuery(List<String> fieldNames, String fieldValue) {
        StringBuffer query = new StringBuffer();
        int listLength = fieldNames.size();
        int counter = 1;
        query.append("(");
        for (String field: fieldNames) {
            query.append(field);
            //query.append(":\"");
            query.append(LUCENE_EQUAL);
            query.append(LUCENE_QUOTE);
            query.append(fieldValue);
            query.append(LUCENE_QUOTE);
            if (counter <listLength) {
                query.append(" OR ");
            }
            else {
                query.append(")");
            }

            counter++;
        }
        return query.toString();
    }


    public static String addSpeciesFilterQuery(String simpleQuery
            , String speciesField, Collection<String> speciesList) {
        StringBuffer sb = new StringBuffer();
        if (speciesList == null) {
            return simpleQuery;
        } else {
            int listLength = speciesList.size();
            if (listLength > 0) {
                int counter = 1;                
                sb.append(simpleQuery);
                sb.append(" AND ");
                sb.append(speciesField);
                sb.append(LUCENE_EQUAL);
                sb.append("(");
                for (String species: speciesList) {
                    sb.append(LUCENE_QUOTE);
                    sb.append(species);
                    sb.append(LUCENE_QUOTE);
                    if (counter <listLength) {
                        sb.append(" OR ");
                    }
                     counter++;
                }
                sb.append(")");
            } else {
                return simpleQuery;
            }

        }
        return sb.toString();
    }

    public static List<String> createUniprotQueryByIdPrefixes(List<String> idPrefixes, Collection<String> speciesFilter) {
        List<String> queryList = new ArrayList<String>();
        for (String idPrefix : idPrefixes) {
            StringBuffer sb = new StringBuffer();
            sb.append(UNIPROT_ID_FIELD);
            sb.append(LUCENE_EQUAL);
            sb.append(idPrefix);
            sb.append(LUCENE_WILDCARD);
            sb.append(" AND " + ENZYME_FILTER_UNIPROTAPI);
            queryList.add(
                    addSpeciesFilterQuery(
                        sb.toString(), UNIPROT_SPECIES_FIELD, speciesFilter));
        }
        return queryList;
    }

/*
    public static List<String> createUniprotQueryByName(List<String> names) {
        List<String> queries = new ArrayList<String>();
        for (String name:names) {
            StringBuffer sb = new StringBuffer();
            sb.append(UNIPROT_NAME_FIELD);
            sb.append(":\"");
            sb.append(luceneParser.escapeLuceneSpecialChars(name));
            sb.append("\"");
            sb.append(" AND " +ENZYME_FILTER_UNIPROTAPI);
            queries.add(sb.toString());
        }
        return queries;
    }
*/
    /*
    public static String createQueryOR(Domain domain, SearchParams searchParams) {
        StringBuffer query = new StringBuffer();
        //System.out.println(domain.getId());
        //System.out.println(domain.getName());
        List<SearchField> SearchFieldList = domain.getSearchFieldList().getSearchField();
        Iterator fieldIt = SearchFieldList.iterator();
        int listLength = SearchFieldList.size();
        int counter = 1;
        //query.append("(");
        while (fieldIt.hasNext()) {
            SearchField searchField = (SearchField) fieldIt.next();
            query.append(searchField.getId());
            query.append(":(\"");
            query.append(searchParams.getKeywords());
            query.append("\")");
            if (counter <listLength) {
                query.append(" OR ");
            }
            else {
                //query.append(")");
            }

            counter++;
        }
        /*
            if (domain.getId().equalsIgnoreCase("uniprot")) {
                //query.insert(0, "(");
                //query.append(")");
                query.append(" AND " +ENZYME_FILTER);
            }
         * 
         */
        //System.out.println(query.toString());
       // return query.toString();
    //}
/*
    public static String createQueryToGetEnzymeOnly(String uniprotId){
        StringBuffer query = new StringBuffer();
        query.append("id:");
        query.append(uniprotId);
        query.append(" AND " );
        query.append(ENZYME_FILTER);
        return query.toString();
    }
*/
    public static String createQueryIN(
            String fieldName, boolean wildcard, Collection<String> fieldValues) {
        StringBuffer query = new StringBuffer();
        query.append(fieldName);
        query.append(LUCENE_EQUAL);
        query.append("(");
        int counter = 1;
        for (String fieldValue: fieldValues) {
            query.append(fieldValue);
            if (wildcard) {
                query.append(LUCENE_WILDCARD);
            }
            if (counter <fieldValues.size()) {
                query.append(" OR ");
            }
            counter++;
        }
         query.append(")");
        return query.toString();

    }
    public static String createAccessionQueryIN(Collection<String> accs) {
        return createQueryIN(ACCESSION_FIELD, false, accs);
    }

    public static String createUniprotQueryForEnzyme(Collection<String> accs, Collection<String> speciesFilter) {
        String query = createAccessionQueryIN(accs) + " AND " + ENZYME_FILTER;
        return addSpeciesFilterQuery(
                    query, EBEYE_SPECIES_FIELD, speciesFilter);
    }

    public static String createIdSuffixWildcardQuery(Collection<String> fieldValues
            , Collection<String> speciesFilter) {
        String query = createQueryIN(UNIPROT_ID_FIELD, true, fieldValues);
        return addSpeciesFilterQuery(query, EBEYE_SPECIES_FIELD, speciesFilter);
    }
   

    /*
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
*/


    public static void buildGetAllResultsQuery (String domain) {


    }

    /*
    public static List<String> buildDomainQueryOr(String keywords) {
        List<Domain> domains = Config.domainList;
        StringBuffer sb = new StringBuffer();
        List<String> queryList = new ArrayList();
        for (Domain domain:domains) {
            String domainId = domain.getId();
            List<SearchField> fields = domain.getSearchFieldList().getSearchField();
            queryList.add(buidFieldsQueryOr(keywords, fields));
        }
        return queryList;
    }

    public static String buidFieldsQueryOr(String keywords, List<SearchField> fields) {
        String query1 = null;
        String query2 = null;
        String cleanKeywords = cleanKeywords(keywords);
        StringBuffer sb = new StringBuffer();
        for (SearchField field: fields) {
            query1 = luceneParser.parseOrTerms(cleanKeywords, field.getId());
            query2 = luceneParser.parsePhraseTerms(cleanKeywords, field.getId());
            String query3 = luceneParser.parseUserTerms(cleanKeywords, field.getId());
            sb.append(query1);
        }
        return sb.toString();
    }

	private static String cleanKeywords(String keywords){
            StandardAnalyzer standardAnalyzer = new StandardAnalyzer(Version.LUCENE_31);
           // standardAnalyzer.

            return null;
        }
     * 
     */


}
