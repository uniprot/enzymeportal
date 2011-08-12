package uk.ac.ebi.ep.core.search;

import java.util.List;
import uk.ac.ebi.ep.entry.exception.EnzymeRetrieverException;
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

    public EnzymeModel getEnzyme(String uniprotAccession) throws EnzymeRetrieverException;

    public EnzymeModel getReactionsPathways(String uniprotAccession) throws EnzymeRetrieverException;

    public EnzymeModel getProteinStructure(String uniprotAccession) throws EnzymeRetrieverException;

    public EnzymeModel getMolecules(String uniprotAccession) throws EnzymeRetrieverException;

    public EnzymeModel getDiseases(String uniprotAccession) throws EnzymeRetrieverException;

    public EnzymeModel getLiterarture(String uniprotAccession) throws EnzymeRetrieverException;

}
