package uk.ac.ebi.ep.core.search;

import java.util.concurrent.Callable;
import uk.ac.ebi.webservices.ebeye.ArrayOfString;
import uk.ac.ebi.webservices.ebeye.EBISearchService;
import uk.ac.ebi.webservices.ebeye.EBISearchService_Service;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class EBeyeWsCallable implements Callable<ArrayOfString>{
    protected EBISearchService eBISearchService;
    protected ParamOfGetResultsIds paramOfGetResultsIds;

    public EBeyeWsCallable(ParamOfGetResultsIds paramOfGetResultsIds) {
        EBISearchService_Service service = new EBISearchService_Service();
        eBISearchService = service.getEBISearchServiceHttpPort();
        this.paramOfGetResultsIds = paramOfGetResultsIds;
    }


//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//



//****************************** GETTER & SETTER *****************************//

    public EBISearchService geteBISearchService() {
        return eBISearchService;
    }

    public void seteBISearchService(EBISearchService eBISearchService) {
        this.eBISearchService = eBISearchService;
    }

    public ParamOfGetResultsIds getParamOfGetResultsIds() {
        return paramOfGetResultsIds;
    }

    public void setParamOfGetResultsIds(ParamOfGetResultsIds paramOfGetResultsIds) {
        this.paramOfGetResultsIds = paramOfGetResultsIds;
    }


//********************************** METHODS *********************************//
    public ArrayOfString call() throws Exception {
        return invokeGetResultsIds();
    }
    public ArrayOfString invokeGetResultsIds() {        
        ArrayOfString result = eBISearchService.getResultsIds(
                this.paramOfGetResultsIds.getDomain()
                , this.paramOfGetResultsIds.getQuery()
                , this.paramOfGetResultsIds.getStart()
                , this.paramOfGetResultsIds.getSize());
        return result;
    }


}
