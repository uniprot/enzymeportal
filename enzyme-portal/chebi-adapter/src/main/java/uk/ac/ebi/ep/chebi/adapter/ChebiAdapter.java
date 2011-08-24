package uk.ac.ebi.ep.chebi.adapter;

import java.util.ArrayList;
import java.util.List;
import uk.ac.ebi.chebi.webapps.chebiWS.client.ChebiWebServiceClient;
import uk.ac.ebi.chebi.webapps.chebiWS.model.ChebiWebServiceFault_Exception;
import uk.ac.ebi.chebi.webapps.chebiWS.model.DataItem;
import uk.ac.ebi.chebi.webapps.chebiWS.model.Entity;
import uk.ac.ebi.chebi.webapps.chebiWS.model.LiteEntity;
import uk.ac.ebi.chebi.webapps.chebiWS.model.LiteEntityList;
import uk.ac.ebi.chebi.webapps.chebiWS.model.OntologyDataItem;
import uk.ac.ebi.chebi.webapps.chebiWS.model.OntologyDataItemList;
import uk.ac.ebi.chebi.webapps.chebiWS.model.SearchCategory;
import uk.ac.ebi.chebi.webapps.chebiWS.model.StarsCategory;
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
public class ChebiAdapter implements IChebiAdapter {

//********************************* VARIABLES ********************************//
    protected ChebiWebServiceClient client;

     public ChebiAdapter() {
         client = new ChebiWebServiceClient();
    }
//******************************** CONSTRUCTORS ******************************//

//****************************** GETTER & SETTER *****************************//
//********************************** METHODS *********************************//
    public static void main(String[] args) throws ChebiFetchDataException {
        ChebiAdapter chebiAdapter = new ChebiAdapter();
        //getCompleteEntityExample ();
        //List<String> results =
           //     chebiAdapter.getChebiLiteEntity("DB00277");
        //chebiAdapter.getChebiCompleteEntity(results.get(0));
        getOntologyParentsExample ();
    }

    public uk.ac.ebi.ep.enzyme.model.Entity getChebiCompleteEntity(
            String chebiId) throws ChebiFetchDataException {
        uk.ac.ebi.ep.enzyme.model.Molecule molecule = new Molecule();
        try {
            // Create client
            
            System.out.println("Invoking getCompleteEntity");
            Entity entity = client.getCompleteEntity(chebiId);
            molecule.setId(chebiId);
            molecule.setDescription(entity.getDefinition());
            molecule.setName(entity.getChebiAsciiName());
            List<DataItem> dataItems = entity.getFormulae();
            StringBuffer sb = new StringBuffer();
            for (DataItem dataItem: dataItems) {
                sb.append(dataItem.getData());
                if (dataItems.size() > 1) {
                    sb.append("; ");
                }
            }
            molecule.setFormula(sb.toString());
            
            molecule.setUrl(Database.CHEBI.getEntryUrl(chebiId));

            List<DataItem> synonyms = entity.getSynonyms();
            // List all synonyms
            for (DataItem dataItem : synonyms) {
                System.out.println("synonyms: " + dataItem.getData());
            }
        } catch (ChebiWebServiceFault_Exception e) {
            throw new ChebiFetchDataException("Failed to retrieve complete entry from Chebi WS ", e);
        }
        return molecule;
    }

    public List<String> getChebiLiteEntity(String query) throws ChebiFetchDataException {
        List<String> results = new ArrayList<String>();
        try {
            System.out.println("Invoking getLiteEntity");
            LiteEntityList entities = client.getLiteEntity(query, SearchCategory.ALL, 50, StarsCategory.ALL);
            List<LiteEntity> resultList = entities.getListElement();
            for (LiteEntity liteEntity : resultList) {
                results.add(liteEntity.getChebiId());
                System.out.println("CHEBI ID: " + liteEntity.getChebiId());
            }

        } catch (ChebiWebServiceFault_Exception e) {
            throw new ChebiFetchDataException("Failed to retrieve summary entries from Chebi WS ", e);
        }

        return results;
    }

  public static void getOntologyParentsExample (){

    try {

      // Create client
      ChebiWebServiceClient client = new ChebiWebServiceClient();
      System.out.println("Invoking getOntologyParents");
      //OntologyDataItemList parents = client.getOntologyParents("CHEBI:17012");
      //OntologyDataItemList parents = client.getOntologyParents("CHEBI:28714");
      OntologyDataItemList parents = client.getOntologyParents("CHEBI:22526");
      List<OntologyDataItem> parentList = parents.getListElement();
      getOntologyParentsResursively(parentList, true);
    } catch ( ChebiWebServiceFault_Exception e ) {
      System.err.println(e.getMessage());
    }
  }

  public static void getOntologyParentsResursively (List<OntologyDataItem> parentList, boolean stop){
      for ( OntologyDataItem ontologyDataItem : parentList ) {
        String relType = ontologyDataItem.getType();
               // System.out.println("CHEBI names: " + ontologyDataItem.getChebiName());
                //System.out.println("CHEBI names: " + ontologyDataItem.getType());

        if (relType.equalsIgnoreCase("has role")) {
            String chebName = ontologyDataItem.getChebiName();
            //IChebiAdapter.MoleculeType.valueOf(chebName)
            if (chebName.contains("drug") || chebName.contains("inhibitor") || chebName.contains("activator")) {
                System.out.println("CHEBI names: " + ontologyDataItem.getChebiName());
                System.out.println("CHEBI names: " + ontologyDataItem.getType());
            }
        }
      }

  }
}

/*
 Invoking getOntologyParents
CHEBI names: monocarboxylic acid
CHEBI names: is a
CHEBI names: non-steroidal anti-inflammatory drug
CHEBI names: has role
CHEBI names: non-steroidal anti-inflammatory drug
CHEBI names: has role
CHEBI names: indoles
CHEBI names: is a
CHEBI names: cyclooxygenase 2 inhibitor
CHEBI names: has role
CHEBI names: cyclooxygenase 2 inhibitor
CHEBI names: has role
CHEBI names: non-narcotic analgesic
CHEBI names: has role
 */