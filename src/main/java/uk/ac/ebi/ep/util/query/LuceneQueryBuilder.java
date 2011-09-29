package uk.ac.ebi.ep.util.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import uk.ac.ebi.biobabel.lucene.LuceneParser;
//import uk.ac.ebi.ebeye.ParamOfGetResultsIds;
//import uk.ac.ebi.ebeye.ResultOfGetNumberOfResults;
import uk.ac.ebi.ep.config.Domain;
import uk.ac.ebi.ep.config.SearchField;
import uk.ac.ebi.ep.search.model.SearchParams;
import uk.ac.ebi.util.result.DataTypeConverter;

/**
 * Hello world!
 *
 */
public class LuceneQueryBuilder {
    public static final String LUCENE_EQUAL = ":";
    public static final String LUCENE_QUOTE = "\"";
    //FOr EBeye EC must be in upper case
    public static final String ENZYME_FILTER =
            "EC:(1* OR 2* OR 3* OR 4* OR 5* OR 6*)";
    public static final String UNIPROT_ID_FIELD ="id";
    //public static final String UNIPROT_NAME_FIELD ="name";
    //public static final String ACCESSION_FIELD ="acc";
    public static final String LUCENE_WILDCARD ="_*";
    //public static final String ENZYME_FILTER_UNIPROTAPI ="ec:*.*";
    public static final String ENZYME_FILTER_UNIPROTAPI ="ec:(1.* OR 2.* OR 3.* OR 4.* OR 5.* OR 6.*)";
    public static final String UNIPROT_SPECIES_FIELD ="organism";
    public static final String EBEYE_SPECIES_FIELD ="organism_scientific_name";

    //133343 results does not pass the load test
    //public static final int MAX_RESULTS = 100000;
    public static LuceneParser luceneParser = new LuceneParser();  

    /**
     * Create a Lucene query for a list of fields of which value is from
     * the searchParam. The query consists of several search fields configured
     * in the Config.xml file. Each field is separated by an OR statement.
     * The keyword in the {@link SearchParams} is the value to be
     * queried in each field. Eg.: id:"Sildenafil" OR name:"Sildenafil".
     * @param domain The Domain object that contains the search fields which are
     * used to create the fields query.
     * @param searchParams The SearchParam object which contain the keywords and
     * the filter which are used to ass 
     * @return a Lucene query
     */
    public static String createFieldsQuery(Domain domain, SearchParams searchParams) {
        StringBuffer query = new StringBuffer();
        List<SearchField> SearchFieldList = domain.getSearchFieldList().getSearchField();
        List<String> fieldNames = new ArrayList<String>();
        for (SearchField field: SearchFieldList) {
            fieldNames.add(field.getId());
        }
        String keywords = searchParams.getText();
        query.append(createFieldsQuery(fieldNames,keywords));
        return query.toString();
    }

    /**
     * Similar to the {@link #createFieldsQuery(uk.ac.ebi.ep.config.Domain, uk.ac.ebi.ep.search.model.SearchParams)}
     * except this method adds the enzyme filter String to query enzyme only in
     * Uniprot domain. This query can only be used to query data from Uniprot domain,
     * because other domains do not have the ec field.
     * @param domain
     * @param searchParams
     * @return
     * @see {@link #createFieldsQuery(uk.ac.ebi.ep.config.Domain, uk.ac.ebi.ep.search.model.SearchParams)}
     */
    public static String createFieldsQueryWithEnzymeFilter(Domain domain, SearchParams searchParams) {
        StringBuffer query = new StringBuffer();
        query.append(createFieldsQuery(domain, searchParams));
        query.append(" AND " +ENZYME_FILTER);
        return query.toString();
    }

    /**
     * Overloaded method to create a Lucene query from the fieldNames and fieldValue
     * instead of a Domain object and a SearchParam object.
     * @param fieldNames The search field names
     * @param fieldValue The value of the field names to query
     * @return a Lucene query OR
     */
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

    public static String createFieldValueQuery(String field, String value) {
        StringBuffer query = new StringBuffer();
        query.append(field);
        query.append(":");
        query.append("\"");
        query.append(value);
        query.append("\"");
        return query.toString();
    }

    public static String createWildcardFieldValueQuery(String field, String value) {
        StringBuffer query = new StringBuffer();
        query.append(field);
        query.append(":");
        query.append(value);
        query.append("*");
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
                    //sb.append(luceneParser.escapeLuceneSpecialChars(species));
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

    public static List<String> escapeSpecies( Collection<String> speciesFilter) {
        List<String> escapedList = new ArrayList<String>();
        for (String species:speciesFilter) {
            String escapedSpecies = luceneParser.escapeLuceneSpecialChars(species);
            /*Work around to fix the bug in Uniprot API. Any species ended with
             * " / S288c" has 0 result
             */
            String newEscapedSpecies = escapedSpecies.replaceAll(" / S288c", "*/*S288c");
            escapedList.add(newEscapedSpecies);
        }
        return escapedList;
    }

    /**
     * Create a list of query by id. A query is created for each id. Queries
     * created by the method can only be used for Uniprot API.
     * @param idPrefixes
     * @param speciesFilter
     * @return
     */
    public static List<String> createUniprotAPIQueryByIdPrefixes(
            List<String> idPrefixes, Collection<String> speciesFilter) {
        List<String> queryList = new ArrayList<String>();
        List<String> escapedSpeciesFilter = null;
        if (speciesFilter != null) {
            escapedSpeciesFilter = escapeSpecies(speciesFilter);
        }
        for (String idPrefix : idPrefixes) {
            StringBuffer sb = new StringBuffer();
            sb.append(UNIPROT_ID_FIELD);
            sb.append(LUCENE_EQUAL);
            sb.append(idPrefix);
            sb.append(LUCENE_WILDCARD);
            /*Adding this will make the result list from Uniprot API smaller
           * than the result list from Ebeye
           */

            sb.append(" AND " + ENZYME_FILTER_UNIPROTAPI);
            queryList.add(
                    addSpeciesFilterQuery(
                        sb.toString(), UNIPROT_SPECIES_FIELD, escapedSpeciesFilter));
        }
        return queryList;
    }
 
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

    /**
     * Create a list of Lucene queries IN (eg.: id:("PDE7B_HUMAN","PDE7B_MOUSE"))
     * for a long list of field values. If the list is too long then it is divided
     * into sub lists. A query is created for each sub list. Every query includes
     * an "enzyme filter".
     * @param queryField
     * @param fieldValues
     * @param wildcard
     * @param subListSize
     * @return
     * @throws EnzymeFinderException
     */
    public static List<String> createQueriesIn(
            String queryField
            , List<String> fieldValues, boolean wildcard, int subListSize) {
        List<String> queries = new ArrayList<String>();
        List<List<String>> subLists = DataTypeConverter
                .createSubLists(fieldValues, subListSize);
        for (List<String> subList: subLists) {
            StringBuffer sb = new StringBuffer();
            sb.append(createQueryIN(queryField, wildcard, subList));
            sb.append( " AND ");
            sb.append(ENZYME_FILTER);
            queries.add(sb.toString());
        }
        return queries;
    }

    /**
     * Builds queries for the given field and field values, <i>adding an
     * enzyme filter</i>.
     * @param queryField field to query
     * @param fieldValues values to match
     * @param wildcard use a wildcard?
     * @param subListSize size of sublist to divide the original list of field
     * 		values, if it is too long.
     * @return
     */
    public static List<String> createEbeyeQueriesIn(String queryField,
            List<String> fieldValues, boolean wildcard, int subListSize) {
        List<String> queries = new ArrayList<String>();
        List<List<String>> subLists =
        		DataTypeConverter.createSubLists(fieldValues, subListSize);
        for (List<String> subList: subLists) {
            StringBuffer sb = new StringBuffer();
            sb.append(createQueryIN(queryField, wildcard, subList));
            //This will slow down the search. 
            sb.append( " AND ");
            sb.append(ENZYME_FILTER);
            queries.add(sb.toString());
        }
        return queries;
    }
    /**
     * Concat a simple query with a filter query by AND condition.
     * @param query
     * @param filterQuery
     * @return
     */
    public static String createANDQuery(String query, String filterQuery) {
        StringBuffer sb = new StringBuffer();
        sb.append(query);
        sb.append(" AND ");
        sb.append(filterQuery);
        return sb.toString();
    }

    public static List<String> addFilterQueriesAND(List<String> queries
            , String filterFieldName, List<String> filterValues)  {
        List<String> queriesWithFilter = new ArrayList<String>();
        //String filterQuery = LuceneQueryBuilder.a.createQueryIN(filterFieldName, false, filterValues);
        for (String query:queries) {
            String qryWithSpecies = addSpeciesFilterQuery(query, filterFieldName, filterValues);
            System.out.println(qryWithSpecies);
            queriesWithFilter.add(qryWithSpecies);
        }
        return queriesWithFilter;
    }
}
