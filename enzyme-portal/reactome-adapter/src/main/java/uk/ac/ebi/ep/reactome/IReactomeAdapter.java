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
    public static final String REACTOME_SEARCH_URL =
            "http://www.reactome.org/cgi-bin/search2?OPERATOR=ALL&SPECIES=48887&QUERY=";


//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    public String getReactionDescription(String reactomeAccession)throws ReactomeServiceException;

    public String getPathwayDescription(String reactomeAccession)throws ReactomeServiceException;

    public Object[] getReactionPathway(String reactomeUrl)throws ReactomeServiceException;



}
