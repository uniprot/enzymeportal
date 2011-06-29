package uk.ac.ebi.ep.intenz.adapter;

import java.util.Map;
import java.util.Set;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public interface IintenzAdapter {
    public static final String FAILED_MSG = "Unable to get synonyms from Intenz! ";

    public Map<String, Set<String>> getSynonyms(Set<String> ecNumbers)
            throws MultiThreadingException;

}
