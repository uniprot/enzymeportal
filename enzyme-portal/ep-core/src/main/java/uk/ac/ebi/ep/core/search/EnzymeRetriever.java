package uk.ac.ebi.ep.core.search;

import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.uniprot.adapter.IUniprotAdapter;
import uk.ac.ebi.ep.uniprot.adapter.UniprotAdapter;

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

    public EnzymeModel retieveEnzyme(String uniprotAccession) {
        EnzymeModel enzymeModel = (EnzymeModel)this.uniprotAdapter.getEnzymeEntry(uniprotAccession);
        this.intenzAdapter.getEcDetails(enzymeModel);
        return enzymeModel;
    }

}
