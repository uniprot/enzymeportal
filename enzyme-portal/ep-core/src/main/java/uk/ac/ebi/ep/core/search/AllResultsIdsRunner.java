package uk.ac.ebi.ep.core.search;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.rpc.ServiceException;
import uk.ac.ebi.ep.core.ext.api.EBeyeClient;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class AllResultsIdsRunner extends EBeyeClient implements Runnable {


//********************************* VARIABLES ********************************//

    protected EBeyeRequestProcessor eBeyeRequestProcessor;
    


//******************************** CONSTRUCTORS ******************************//

    public AllResultsIdsRunner(EBeyeRequestProcessor eBeyeRequestProcessor) {
        this.eBeyeRequestProcessor = eBeyeRequestProcessor;
    }


//****************************** GETTER & SETTER *****************************//
    public EBeyeRequestProcessor geteBeyeRequestProcessor() {
        return eBeyeRequestProcessor;
    }

    public void seteBeyeRequestProcessor(EBeyeRequestProcessor eBeyeRequestProcessor) {
        this.eBeyeRequestProcessor = eBeyeRequestProcessor;
    }


//********************************** METHODS *********************************//


    public void run() {
        try {
            eBeyeRequestProcessor.getAllResultsIds();
        } catch (ServiceException ex) {
            Logger.getLogger(AllResultsIdsRunner.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
