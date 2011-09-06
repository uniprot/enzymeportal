package uk.ac.ebi.ep.reactome;

import java.util.List;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.enzyme.model.Pathway;

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

    public String getReactionDescription(String reactomeAccession)throws ReactomeServiceException;

    public String getPathwayDescription(String reactomeAccession)throws ReactomeServiceException;

    public EnzymeModel getReationDescription(EnzymeModel enzymeModel)throws ReactomeServiceException;

    public List<Pathway> getPathwayDescription(List<String> stableIds)throws ReactomeServiceException;
}
