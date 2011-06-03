package uk.ac.ebi.util.result;

import java.util.Comparator;
import uk.ac.ebi.ep.ebeye.result.jaxb.Result;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ResultComparator implements Comparator<Result>{


//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//
    public int compare(Result o1, Result o2) {
        String id1 = o1.getId();
        String id2 = o2.getId();
        return id1.compareTo(id2);
    }

}
