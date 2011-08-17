package uk.ac.ebi.ep.reactome;

import java.util.List;
import org.reactome.cabig.domain.Reaction;
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

    public Reaction getReaction(String reactomeReactionAccession);

}
