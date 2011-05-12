package uk.ac.ebi.ep.ebeye.adapter;

import java.util.List;
import java.util.concurrent.ExecutionException;
import uk.ac.ebi.ep.ebeye.result.jaxb.Result;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public interface IEbeyeAdapter {

//********************************* VARIABLES ********************************//
public static final int EBEYE_RESULT_LIMIT = 100;

//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//
    public List<Result> getAllResults(
            String domain, String query, List<String> fields)
                throws InterruptedException, ExecutionException;

}
