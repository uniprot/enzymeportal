package uk.ac.ebi.ep.adapter.chebi;

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

	public static enum MoleculeType {drug, inhibitor, activator, cofactor};
	
	public void setConfig(ChebiConfig chebiConfig);

    public uk.ac.ebi.ep.enzyme.model.Entity getEpChemicalEntity(
            String chebiId) throws ChebiFetchDataException;

    public List<String> getChebiLiteEntity(String query) throws ChebiFetchDataException;

    /**
     * Completes the information stored in the Molecule objects inside the
     * enzyme model by querying ChEBI.
     * @param enzymeModel
     * @return
     * @throws ChebiFetchDataException
     */
    public EnzymeModel getMoleculeCompleteEntries(EnzymeModel enzymeModel)
	throws ChebiFetchDataException;

}
