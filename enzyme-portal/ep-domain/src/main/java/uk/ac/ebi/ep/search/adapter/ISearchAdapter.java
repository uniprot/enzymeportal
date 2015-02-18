package uk.ac.ebi.ep.search.adapter;

import java.util.Map;
import java.util.Set;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public interface ISearchAdapter {

//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    public Map<String, String> getNameMapByAccessions(Set<String> accessions);

    public Set<String> getNameSetByAccessions(Set<String> accessions);

    public Set<String> getIdSet(String query);

    public Set<String> getIdAccessionMap(String query);

    public int getNumberOfResults(String query);

}
