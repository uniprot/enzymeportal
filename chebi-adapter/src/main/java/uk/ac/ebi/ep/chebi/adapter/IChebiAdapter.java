package uk.ac.ebi.ep.chebi.adapter;

import java.util.List;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public interface IChebiAdapter {

//********************************* VARIABLES ********************************//

 public static enum MoleculeType {drug, inhibitor, activator, cofactor};
//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    public uk.ac.ebi.ep.enzyme.model.Entity getChebiCompleteEntity(
            String chebiId) throws ChebiFetchDataException;

    public List<String> getChebiLiteEntity(String query) throws ChebiFetchDataException;     
}
