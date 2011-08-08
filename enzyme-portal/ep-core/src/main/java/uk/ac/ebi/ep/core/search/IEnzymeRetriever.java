package uk.ac.ebi.ep.core.search;

import uk.ac.ebi.ep.enzyme.model.EnzymeModel;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public interface IEnzymeRetriever {

//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    public EnzymeModel retieveEnzyme(String uniprotAccession);

}
