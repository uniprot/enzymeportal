package uk.ac.ebi.ep.chebi.adapter;

import java.util.ArrayList;
import java.util.List;
import uk.ac.ebi.chebi.webapps.chebiWS.client.ChebiWebServiceClient;
import uk.ac.ebi.chebi.webapps.chebiWS.model.ChebiWebServiceFault_Exception;
import uk.ac.ebi.chebi.webapps.chebiWS.model.DataItem;
import uk.ac.ebi.chebi.webapps.chebiWS.model.Entity;
import uk.ac.ebi.chebi.webapps.chebiWS.model.LiteEntity;
import uk.ac.ebi.chebi.webapps.chebiWS.model.LiteEntityList;
import uk.ac.ebi.chebi.webapps.chebiWS.model.SearchCategory;
import uk.ac.ebi.chebi.webapps.chebiWS.model.StarsCategory;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ChebiAdapter {

//********************************* VARIABLES ********************************//
//******************************** CONSTRUCTORS ******************************//
//****************************** GETTER & SETTER *****************************//
//********************************** METHODS *********************************//
    public static void main(String[] args) {
        //getCompleteEntityExample ();
        List<String> results = 
                getLiteEntityExample();
        getCompleteEntityExample (results.get(0));
    }

    public static void getCompleteEntityExample(String chebiId) {
        try {

            // Create client
            ChebiWebServiceClient client = new ChebiWebServiceClient();
            System.out.println("Invoking getCompleteEntity");
            Entity entity = client.getCompleteEntity(chebiId);
            System.out.println("GetName: " + entity.getChebiAsciiName());
            List<DataItem> synonyms = entity.getSynonyms();
            // List all synonyms
            for (DataItem dataItem : synonyms) {                
                System.out.println("synonyms: " + dataItem.getData());
            }

        } catch (ChebiWebServiceFault_Exception e) {
            System.err.println(e.getMessage());
        }        
    }

    public static List<String> getLiteEntityExample() {
        List<String> results = new ArrayList<String>();
        try {

            // Create client
            ChebiWebServiceClient client = new ChebiWebServiceClient();
            System.out.println("Invoking getLiteEntity");
            LiteEntityList entities = client.getLiteEntity("DB00277", SearchCategory.ALL, 50, StarsCategory.ALL);
            List<LiteEntity> resultList = entities.getListElement();
            for (LiteEntity liteEntity : resultList) {
                results.add(liteEntity.getChebiId());
                System.out.println("CHEBI ID: " + liteEntity.getChebiId());
            }

        } catch (ChebiWebServiceFault_Exception e) {
            System.err.println(e.getMessage());
        }
        return results;
    }
}
