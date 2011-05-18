package uk.ac.ebi.ep.uniprot.adapter;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import uk.ac.ebi.ep.search.result.jaxb.EnzymeSummary;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public interface IUniprotAdapter {
    //second
    public static final int ENTRY_TIMEOUT = 15;

//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//
    public EnzymeSummary getEnzymeEntry(String uniprotAccession);

    public List<EnzymeSummary> getEnzymeEntries(List<String> uniprotAccession)
            throws InterruptedException, ExecutionException, TimeoutException ;

}


