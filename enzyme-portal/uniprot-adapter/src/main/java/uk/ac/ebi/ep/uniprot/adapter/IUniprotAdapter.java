package uk.ac.ebi.ep.uniprot.adapter;

import java.util.List;
import java.util.Set;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.ep.search.model.EnzymeSummary;

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
    public static final int ENTRY_TIMEOUT = 60;

//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//
    public EnzymeSummary getEnzymeEntry(String accession);

    public List<EnzymeSummary> getEnzymeEntries(Set<String> queries) throws
            MultiThreadingException;
    
    public List<EnzymeSummary> queryEnzymeByIdPrefixes(List<String> queries) throws
            MultiThreadingException;

}


