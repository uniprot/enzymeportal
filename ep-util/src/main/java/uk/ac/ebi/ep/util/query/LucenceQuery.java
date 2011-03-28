package uk.ac.ebi.ep.util.query;

import java.util.Iterator;
import java.util.List;
import uk.ac.ebi.ep.config.Domain;
import uk.ac.ebi.ep.config.SearchField;

/**
 * Hello world!
 *
 */
public class LucenceQuery {

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }

    public static String createQueryOR(Domain domain) {
        StringBuffer query = new StringBuffer();
        System.out.println(domain.getId());
        System.out.println(domain.getName());
        List<SearchField> SearchFieldList = domain.getSearchFieldList().getSearchField();
        Iterator fieldIt = SearchFieldList.iterator();
        int listLength = SearchFieldList.size();
        int counter = 1;
        while (fieldIt.hasNext()) {
            SearchField searchField = (SearchField) fieldIt.next();
            query.append(searchField);
            query.append("=");
            query.append("kinase");
            if (counter <listLength)
            query.append(" OR ");
            System.out.println(query);
            counter++;

        }
        return query.toString();
    }
}
