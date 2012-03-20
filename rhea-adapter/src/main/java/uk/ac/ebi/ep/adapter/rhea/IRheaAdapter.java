package uk.ac.ebi.ep.adapter.rhea;

import java.util.List;
import uk.ac.ebi.ep.enzyme.model.ReactionPathway;
import uk.ac.ebi.rhea.ws.client.RheaFetchDataException;
import uk.ac.ebi.rhea.ws.response.cmlreact.Reaction;
import uk.ac.ebi.rhea.ws.response.search.RheaReaction;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision: 1820 $ <br/>
 *          $LastChangedDate: 2011-08-19 15:47:22 +0100 (Fri, 19 Aug 2011) $ <br/>
 *          $Author: hongcao $
 * @author  $Author: hongcao $
 */
public interface IRheaAdapter {
	
    public enum RheaQueryFields {
        EC
    }

     public Reaction getReactionInCmlreact(String rheaCmlReactUri)
	 throws RheaFetchDataException;

     public List<RheaReaction> searchRheasInCmlreact(String query)
	 throws RheaFetchDataException;

     public List<Reaction> getRheasInCmlreact(String query)
	 throws RheaFetchDataException;

     public ReactionPathway getReactionPathway(Reaction reaction);
}
