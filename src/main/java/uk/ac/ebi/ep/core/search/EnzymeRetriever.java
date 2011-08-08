package uk.ac.ebi.ep.core.search;

import uk.ac.ebi.ep.entry.exception.EnzymeRetrieverException;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class EnzymeRetriever extends EnzymeFinder implements IEnzymeRetriever {

//********************************* VARIABLES ********************************//
    protected String accesion;

    public String getAccesion() {
        return accesion;
    }

    public void setAccesion(String accesion) {
        this.accesion = accesion;
    }


//******************************** CONSTRUCTORS ******************************//

    public EnzymeRetriever() {        
    }


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    public EnzymeModel retieveEnzyme(String uniprotAccession) throws EnzymeRetrieverException {
        EnzymeModel enzymeModel = (EnzymeModel)this.uniprotAdapter.getEnzymeEntry(uniprotAccession);
        try {
            this.intenzAdapter.getEnzymeDetails(enzymeModel);
        } catch (MultiThreadingException ex) {
            throw new EnzymeRetrieverException ("Unable to retrieve the entry details! ", ex);
        }
        return enzymeModel;
    }

}
