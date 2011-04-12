package uk.ac.ebi.ep.util.query;

import java.util.Iterator;
import java.util.List;
import uk.ac.ebi.ep.config.Domain;
import uk.ac.ebi.ep.config.SearchField;
import uk.ac.ebi.ep.search.parameter.SearchParams;

/**
 * Hello world!
 *
 */
public class LucenceQuery {
    public static final String ENZYME_FILTER =
            "EC:(1* OR 2* OR 3* OR 4* OR 5* OR 6* OR 7* OR 8* OR 9*)";

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
        while (fieldIt.hasNext()) {
            SearchField searchField = (SearchField) fieldIt.next();
            query.append(searchField.getId());
            query.append(":");
            query.append(searchParams.getKeywords());
            query.append("*");
            if (counter <listLength)
            query.append(" OR ");            
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
}
