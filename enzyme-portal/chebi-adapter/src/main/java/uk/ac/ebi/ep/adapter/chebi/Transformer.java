package uk.ac.ebi.ep.adapter.chebi;

import java.util.List;
import uk.ac.ebi.chebi.webapps.chebiWS.model.DataItem;
import uk.ac.ebi.chebi.webapps.chebiWS.model.Entity;
import uk.ac.ebi.ep.enzyme.model.Molecule;
import uk.ac.ebi.rhea.domain.Database;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class Transformer {

//********************************* VARIABLES ********************************//
//******************************** CONSTRUCTORS ******************************//
//****************************** GETTER & SETTER *****************************//
//********************************** METHODS *********************************//

    public static uk.ac.ebi.ep.enzyme.model.Molecule transformChebiToEpMoleculeEntity(Entity chebiEntity){
        uk.ac.ebi.ep.enzyme.model.Molecule molecule = new Molecule();
        String chebiId = chebiEntity.getChebiId();
        molecule.setId(chebiId);
        molecule.setDescription(chebiEntity.getDefinition());
        molecule.setName(chebiEntity.getChebiAsciiName());
        List<DataItem> dataItems = chebiEntity.getFormulae();
        StringBuilder sb = new StringBuilder();
        for (DataItem dataItem : dataItems) {
            sb.append(dataItem.getData());
            if (dataItems.size() > 1) {
                sb.append("; ");
            }
        }
        molecule.setFormula(sb.toString());

        molecule.setUrl(Database.CHEBI.getEntryUrl(chebiId));
        return molecule;
    }
}
