package uk.ac.ebi.ep.reactome;

import uk.ac.ebi.ep.enzyme.model.EnzymeModel;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public interface IReactomeAdapter {

//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    public EnzymeModel getPathways(String uniprotAccession);

    public String[] getReaction(String reactomeUrl)throws ReactomeServiceException;

    public Object[] getReactionPathway(String reactomeUrl)throws ReactomeServiceException;

}
