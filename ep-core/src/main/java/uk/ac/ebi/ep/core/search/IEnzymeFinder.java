package uk.ac.ebi.ep.core.search;

import java.util.List;
import java.util.Map;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public interface IEnzymeFinder extends IEnzyme {

//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//
    public Map<String,Map<String,String>> find(Object searchInput);

}
