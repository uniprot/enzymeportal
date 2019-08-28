
package uk.ac.ebi.ep.metaboliteService.service;

import java.util.List;
import uk.ac.ebi.chebi.webapps.chebiWS.model.Entity;

/**
 *
 * @author joseph
 */
public interface ChebiService {
    
    Entity getCompleteChebiEntityInformation(String chebiId);
    
    List<String> getChebiSynonyms(String chebiId);
}
