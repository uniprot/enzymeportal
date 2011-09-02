package uk.ac.ebi.ep.chebi.adapter;

import java.util.List;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;

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

    public uk.ac.ebi.ep.enzyme.model.Entity getEpChemicalEntity(
            String chebiId) throws ChebiFetchDataException;

    public List<String> getChebiLiteEntity(String query) throws ChebiFetchDataException;

    public EnzymeModel getMoleculeCompleteEntries(EnzymeModel enzymeModel) throws ChebiFetchDataException;
}
