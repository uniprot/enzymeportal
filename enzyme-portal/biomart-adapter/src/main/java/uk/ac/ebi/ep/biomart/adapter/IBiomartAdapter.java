package uk.ac.ebi.ep.biomart.adapter;

import java.util.List;
import uk.ac.ebi.ep.enzyme.model.EnzymeReaction;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public interface IBiomartAdapter {

//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    public List<String> getPathwaysByReactionId(String reactionStableId) throws BiomartFetchDataException;
    public List<EnzymeReaction> getReactionsByUniprotAccession(String accession) throws BiomartFetchDataException;
}
