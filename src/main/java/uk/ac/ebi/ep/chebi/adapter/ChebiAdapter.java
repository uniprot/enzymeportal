package uk.ac.ebi.ep.chebi.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import uk.ac.ebi.ep.enzyme.model.ChemicalEntity;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.enzyme.model.Molecule;
import uk.ac.ebi.util.result.DataTypeConverter;

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
    public static final int IDS_MAX_SIZE = 10;

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


    public uk.ac.ebi.ep.enzyme.model.Entity getEpChemicalEntity(
            String chebiId) throws ChebiFetchDataException {
        Entity entity = getChebiCompleteEntity(chebiId);
        uk.ac.ebi.ep.enzyme.model.Entity epEntity = Transformer.transformChebiToEpMoleculeEntity(entity);
        return epEntity;
    }

    public Entity getChebiCompleteEntity(
            String chebiId) throws ChebiFetchDataException {
        Entity entity = null;
        try {
            entity = client.getCompleteEntity(chebiId);
        } catch (ChebiWebServiceFault_Exception e) {
            throw new ChebiFetchDataException("Failed to retrieve complete entry from Chebi WS ", e);
        }
        return entity;
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

    public Entity querySynonymsForChebiId(String name, List<String> ids) throws ChebiFetchDataException {
        Entity chebiEntityMappedToSynonym = null;
        Entity chebiEntityMappedToUniprotSynonym = null;
        Entity chebiEntitySelected = null;
        try {
            List<Entity> results = client.getCompleteEntityByList(ids);
            for (Entity result:results) {
                String chebiId = result.getChebiId();
                List<DataItem> synonyms = result.getSynonyms();
                // List all synonyms
                for (DataItem dataItem : synonyms) {                    
                    String source = dataItem.getSource();
                    String synonymName = dataItem.getData().trim();
                    if (source.equalsIgnoreCase("UniProt")) {
                        if (synonymName.equalsIgnoreCase(name)) {
                            chebiEntityMappedToUniprotSynonym = result;
                        }
                    } //other synonyms different from Uniprot
                    else {
                        if (synonymName.equalsIgnoreCase(name)) {
                            chebiEntityMappedToSynonym = result;
                            break;
                        }
                    }
                }
            if (chebiEntityMappedToUniprotSynonym != null) {
                chebiEntitySelected = chebiEntityMappedToUniprotSynonym;
                break;
            } else {
                if (chebiEntityMappedToSynonym != null) {
                    chebiEntitySelected = chebiEntityMappedToSynonym;
                    break;
                }
            }

            }
        } catch (ChebiWebServiceFault_Exception e) {
            throw new ChebiFetchDataException("Failed to get complete entity list for " +ids.toString(), e);
        }

        return chebiEntitySelected;
    }
    
    public String queryChebiNameForId(String name) throws ChebiFetchDataException {
        String result = null;
        try {
            LiteEntityList entities = client.getLiteEntity(name, SearchCategory.CHEBI_NAME, 1, StarsCategory.ALL);
            List<LiteEntity> resultList = entities.getListElement();
            if (resultList.size() > 0) {
                LiteEntity liteEntity = (LiteEntity)resultList.get(0);
                //Work around to avoid Chebi Ws bug search "phosphatidic acid" find "acid"
                if (liteEntity.getChebiAsciiName().equalsIgnoreCase(name)){
                    result= liteEntity.getChebiId();
                }
            }

        } catch (ChebiWebServiceFault_Exception e) {
            throw new ChebiFetchDataException("Failed to get the id for Chebi name " +name, e);
        }

        return result;
    }

    public List<String> queryChebiAllNamesForIds(String name, int size) throws ChebiFetchDataException {
        List<String> results = new ArrayList<String>();
        try {
            LiteEntityList entities = client.getLiteEntity(name,  SearchCategory.ALL_NAMES, size, StarsCategory.ALL);
            List<LiteEntity> resultList = entities.getListElement();
            for (LiteEntity liteEntity:resultList) {
                results.add(liteEntity.getChebiId());
            }

        } catch (ChebiWebServiceFault_Exception e) {
            throw new ChebiFetchDataException("Failed to get the ids for Chebi name " +name, e);
        }
        return results;
    }

    public Map<String,Entity> queryChebiAllNamesForEntities(Set<String> names) throws ChebiFetchDataException  {
        Map<String,Entity> nameEntityMap = new HashMap<String, Entity>();
        for (String name:names) {
            //1st choice: exact chebi name. 2nd choice: uniprot synonym. 3rd choice: other synonym
            Entity chebiEntity = null;
            String chebiId = queryChebiNameForId(name);
            if (chebiId != null) {
                chebiEntity = getChebiCompleteEntity(chebiId);
            } else {
                List<String> candidateIds = queryChebiAllNamesForIds(name,ChebiAdapter.IDS_MAX_SIZE);
                if (candidateIds.size() > 0) {
                    chebiEntity = querySynonymsForChebiId(name, candidateIds);
                }
            }
            nameEntityMap.put(name, chebiEntity);
        }
        return nameEntityMap;
    }

    public List<Molecule> setMolecules(List<String> molNames, Map<String,Entity> nameIdMap) {
        List<Molecule> mols = new ArrayList<Molecule>();
        for (String molName:molNames) {
            Entity entity = nameIdMap.get(molName);
            if (entity != null) {
                Molecule mol = (Molecule)Transformer.transformChebiToEpMoleculeEntity(entity);
                mols.add(mol);
            }
        }
        return mols;
    }
    public EnzymeModel getMoleculeCompleteEntries(EnzymeModel enzymeModel) throws ChebiFetchDataException {
        ChemicalEntity chemicalEntity = enzymeModel.getMolecule();
        Set<String> uniqueMoleculeNames = new HashSet<String>();
        List<String> drugNames = DataTypeConverter.getMoleculeNames(chemicalEntity.getDrugs());// FIXME drugbank comes without names
        List<String> activatorNames = DataTypeConverter.getMoleculeNames(chemicalEntity.getActivators());
        List<String> inhibitorNames = DataTypeConverter.getMoleculeNames(chemicalEntity.getInhibitors());
        uniqueMoleculeNames.addAll(drugNames);
        uniqueMoleculeNames.addAll(activatorNames);
        uniqueMoleculeNames.addAll(inhibitorNames);
        Map<String,Entity> nameIdMap = null;
        if (uniqueMoleculeNames.size() > 0) {
            nameIdMap = queryChebiAllNamesForEntities(uniqueMoleculeNames);
            if (nameIdMap.size() > 0) {
                List<Molecule> drugMols = setMolecules(drugNames, nameIdMap);
                enzymeModel.getMolecule().setDrugs(drugMols);
                List<Molecule> activatorMols = setMolecules(activatorNames, nameIdMap);
                enzymeModel.getMolecule().setActivators(activatorMols);
                List<Molecule> inhibitorMols = setMolecules(inhibitorNames, nameIdMap);
                enzymeModel.getMolecule().setInhibitors(inhibitorMols);
            }
        }

        return enzymeModel;
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