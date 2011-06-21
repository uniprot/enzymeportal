package uk.ac.ebi.ep.util.query;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.util.Version;
import uk.ac.ebi.biobabel.lucene.LuceneParser;
//import uk.ac.ebi.ebeye.ParamOfGetResultsIds;
//import uk.ac.ebi.ebeye.ResultOfGetNumberOfResults;
import uk.ac.ebi.ep.config.jaxb.Domain;
import uk.ac.ebi.ep.config.jaxb.SearchField;
import uk.ac.ebi.ep.search.parameter.SearchParams;

/**
 * Hello world!
 *
 */
public class LuceneQueryBuilder {
    public static final String ENZYME_FILTER =
            "EC:(1* OR 2* OR 3* OR 4* OR 5* OR 6* OR 7*)";
    public static final String UNIPROT_ID_FIELD ="id";
    public static final String UNIPROT_ID_SUFFIX_WILDCARD ="_*";
    public static final int EBEYE_MAX_RESULTS_PER_QUERY = 100;
    public static final String ENZYME_FILTER_UNIPROTAPI =
            "EC:*";

    //133343 results does not pass the load test
    //public static final int MAX_RESULTS = 100000;
    public static LuceneParser luceneParser = new LuceneParser();  

    public static String createQueryOR(Domain domain, SearchParams searchParams) {
        StringBuffer query = new StringBuffer();
        //System.out.println(domain.getId());
        //System.out.println(domain.getName());
        List<SearchField> SearchFieldList = domain.getSearchFieldList().getSearchField();
        Iterator fieldIt = SearchFieldList.iterator();
        int listLength = SearchFieldList.size();
        int counter = 1;
        String keywords = searchParams.getKeywords();
        //int numberOfKeywords = keywords.split("\\s").length;
        query.append("(");
        while (fieldIt.hasNext()) {
            SearchField searchField = (SearchField) fieldIt.next();            
            query.append(searchField.getId());
            query.append(":\"");
            query.append(keywords);
            query.append("\"");
            if (counter <listLength) {
                query.append(" OR ");
            }
            else {
                query.append(")");
            }
            
            counter++;
        }        
            if (domain.getId().equalsIgnoreCase("uniprot")) {
                //query.insert(0, "(");
                //query.append(")");
                query.append(" AND " +ENZYME_FILTER);
            }
        //System.out.println(query.toString());
        return query.toString();
    }

    public static List<String> createUniprotQueryByIdPrefixes(List<String> idPrefixes) {
        List<String> queries = new ArrayList<String>();
        for (String idPrefix:idPrefixes) {
            StringBuffer sb = new StringBuffer();
            sb.append(UNIPROT_ID_FIELD);
            sb.append(":");
            sb.append(idPrefix);
            sb.append(UNIPROT_ID_SUFFIX_WILDCARD);
            //sb.append(" AND " +ENZYME_FILTER_UNIPROTAPI);
            queries.add(sb.toString());
        }
        return queries;
    }

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

    public static String createQueryToGetEnzymeOnly(String uniprotId){
        StringBuffer query = new StringBuffer();
        query.append("id:");
        query.append(uniprotId);
        query.append(" AND " );
        query.append(ENZYME_FILTER);
        return query.toString();
    }

    public static String createQueryIN(List<String> accs) {
        StringBuffer query = new StringBuffer();
        Iterator it = accs.iterator();
        query.append("acc:(");
        int counter = 1;
        while(it.hasNext()) {
            String id = (String)it.next();
            query.append(id);
            if (counter <accs.size()) {
                query.append(" OR ");
            }
            counter++;
        }
         query.append(")");
        return query.toString();
    }

    public static String createUniprotQueryForEnzyme(List<String> accs) {
        return createQueryIN(accs) + " AND " + ENZYME_FILTER;
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

   private static final String[] strings = {
        "cGMP-specific 3',5'-cyclic phosphodiesterase",
        "H2O7N8 + H2 +"
    };
   

    private static final Analyzer[] analyzers = new Analyzer[]{
        new WhitespaceAnalyzer(Version.LUCENE_31),
        new SimpleAnalyzer(Version.LUCENE_31),
        new StopAnalyzer(Version.LUCENE_31),
        new StandardAnalyzer(Version.LUCENE_31)
        //new SnowballAnalyzer(Version.LUCENE_31,"test")
    };

    public static void main(String[] args) throws IOException {
        for (int i = 0; i < strings.length; i++) {
            analyze(strings[i]);
        }
    }

    private static void analyze(String text) throws IOException {
for (int i = 0; i < analyzers.length; i++) {
            Analyzer analyzer = analyzers[i];
        System.out.println("Analzying \"" + text + "\"");        
            //Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);
            System.out.println("\t" + analyzer.getClass().getName() + ":");
            System.out.print("\t\t");

            TokenStream tokenStream = analyzer.tokenStream("contents", new StringReader(text));
            OffsetAttribute offsetAttribute = tokenStream.getAttribute(OffsetAttribute.class);
            CharTermAttribute termAttribute = tokenStream.getAttribute(CharTermAttribute.class);

            while (tokenStream.incrementToken()) {
                int startOffset = offsetAttribute.startOffset();
                int endOffset = offsetAttribute.endOffset();
                String term = termAttribute.toString();
                System.out.println(term);
            }
            
        }    
    }

}
