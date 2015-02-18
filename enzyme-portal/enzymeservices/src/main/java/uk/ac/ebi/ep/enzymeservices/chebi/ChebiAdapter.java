package uk.ac.ebi.ep.enzymeservices.chebi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;
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
import uk.ac.ebi.ep.data.enzyme.model.ChemicalEntity;
import uk.ac.ebi.ep.data.enzyme.model.CountableMolecules;
import uk.ac.ebi.ep.data.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.data.enzyme.model.Molecule;


/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ChebiAdapter implements IChebiAdapter {

	private static final Logger LOGGER = Logger.getLogger(ChebiAdapter.class);
    
    public static final int IDS_MAX_SIZE = 10;
	
    protected ChebiWebServiceClient client;

    private ChebiConfig config;
    
    public ChebiAdapter() {
         client = new ChebiWebServiceClient();
    }

    public ChebiConfig getConfig() {
		return config;
	}

	public void setConfig(ChebiConfig config) {
		this.config = config;
	}

//	public uk.ac.ebi.ep.enzyme.model.Entity getEpChemicalEntity(
//            String chebiId) throws ChebiFetchDataException {
//        Entity entity = getChebiCompleteEntity(chebiId);
//        uk.ac.ebi.ep.enzyme.model.Entity epEntity = Transformer.transformChebiToEpMoleculeEntity(entity);
//        return epEntity;
//    }
        
        	public Molecule getEpChemicalEntity(
            String chebiId) throws ChebiFetchDataException {
        Entity entity = getChebiCompleteEntity(chebiId);
        Molecule epEntity = Transformer.transformChebiToEpMoleculeEntity(entity);
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
            LOGGER.debug("Invoking getLiteEntity");
            LiteEntityList entities = client.getLiteEntity(query, SearchCategory.ALL, 50, StarsCategory.ALL);
            List<LiteEntity> resultList = entities.getListElement();
            for (LiteEntity liteEntity : resultList) {
                results.add(liteEntity.getChebiId());
                LOGGER.debug("CHEBI ID: " + liteEntity.getChebiId());
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
        ExecutorService pool = Executors.newFixedThreadPool(config.getMaxThreads());
//	    ExecutorService pool = Executors.newCachedThreadPool();
	    CompletionService<Molecule> ecs =
	    		new ExecutorCompletionService<Molecule>(pool);

        final CountableMolecules drugs = chemicalEntity.getDrugs();
        final CountableMolecules inhibitors = chemicalEntity.getInhibitors();
        final CountableMolecules activators = chemicalEntity.getActivators();
        final CountableMolecules cofactors = chemicalEntity.getCofactors();
        final CountableMolecules bioactive =
                chemicalEntity.getBioactiveLigands();
	    try {
        	LOGGER.debug("MOLECULES before submitting drugs");
            Map<Future<Molecule>, Molecule> drugFut =
	    			getFuture2MoleculeMap(drugs, ecs);
        	LOGGER.debug("MOLECULES before submitting inhibitors");
            Map<Future<Molecule>, Molecule> inhFut =
		    		getFuture2MoleculeMap(inhibitors, ecs);
        	LOGGER.debug("MOLECULES before submitting activators");
            Map<Future<Molecule>, Molecule> actFut =
	        		getFuture2MoleculeMap(activators, ecs);
        	LOGGER.debug("MOLECULES before submitting cofactors");
            Map<Future<Molecule>, Molecule> cofFut =
	        		getFuture2MoleculeMap(cofactors, ecs);
        	LOGGER.debug("MOLECULES before submitting bioactives");
            Map<Future<Molecule>, Molecule> bioactFut =
	        		getFuture2MoleculeMap(bioactive, ecs);
	        // How many jobs have we sent?
	        final int numOfMolecules = drugFut.size() + inhFut.size()
	        		+ actFut.size() + cofFut.size() + bioactFut.size();
	        // Let's retrieve them:
        	LOGGER.debug("MOLECULES before getting molecules");
			for (int i = 0; i < numOfMolecules; i++){
	        	Future<Molecule> future = null;
	        	try {
	        		future = ecs.take();
//	        		future = ecs.poll(config.getTimeout(), TimeUnit.MILLISECONDS);
	        		if (future != null){
	        			final Molecule molecule = future.get();
	        			if (molecule != null){
		        			// Replace the incomplete molecule with the complete one:
	        				if (drugFut.containsKey(future)){
	        					drugFut.put(future, molecule);
	        				} else if (inhFut.containsKey(future)){
	        					inhFut.put(future, molecule);
	        				} else if (actFut.containsKey(future)){
	        					actFut.put(future, molecule);
	        				} else if (cofFut.containsKey(future)){
	        					cofFut.put(future, molecule);
	        				} else if (bioactFut.containsKey(future)){
	        					bioactFut.put(future, molecule);
	        				}
	        			} else {
		    				LOGGER.warn("Molecule retrieved is null!");
	        			}
	        		} else {
	    				LOGGER.warn("Job result not retrieved!");
	        		}
	        	} catch (Exception e){
	            	// Don't stop the others
	        		LOGGER.error("Callable " + (i+1) + " of " + numOfMolecules
	            			+ " - " + e.getMessage(), e);
	        	}
	        }
        	LOGGER.debug("MOLECULES before setting molecules");
        	// Replace first items in each list with the retrieved ones:
        	if (drugs != null){
                drugs.getMolecule().subList(0, drugFut.size()).clear();
                drugs.getMolecule().addAll(0, drugFut.values());
            }
            if (inhibitors != null){
                inhibitors.getMolecule().subList(0, inhFut.size()).clear();
                inhibitors.getMolecule().addAll(0, inhFut.values());
            }
            if (activators != null){
                activators.getMolecule().subList(0, actFut.size()).clear();
                activators.getMolecule().addAll(0, actFut.values());
            }
            if (cofactors != null){
                cofactors.getMolecule().subList(0, cofFut.size()).clear();
                cofactors.getMolecule().addAll(0, cofFut.values());
            }
            if (bioactive != null){
                bioactive.getMolecule().subList(0, bioactFut.size()).clear();
                bioactive.getMolecule().addAll(0, bioactFut.values());
            }
	    } finally {
	    	pool.shutdown();
	    }
        return enzymeModel;
    }
    
    /**
     * Prepares callables and sends them to a completion service in order to
     * retrieve complete entities from ChEBI.
     * @param molecules the list of molecules to be retrieved from ChEBI.
     * @param ecs the completion service where the jobs are submitted.
     * @return a map of <code>Future</code>s to <code>Molecule</code>s
     */
    private Map<Future<Molecule>, Molecule> getFuture2MoleculeMap(
    		CountableMolecules molecules, CompletionService<Molecule> ecs){
    	Map<Future<Molecule>, Molecule> fut2mol =
    			new LinkedHashMap<Future<Molecule>, Molecule>();
        if (molecules != null && molecules.getMolecule() != null){
            for (Molecule molecule : molecules.getMolecule()) {
                ChebiWsCallable callable = getCallable(molecule);
                if (callable != null){
                    fut2mol.put(ecs.submit(callable), molecule);
                }
                // Limit to the first few molecules:
                if (fut2mol.size() >= config.getMaxRetrievedMolecules()) break;
            }
        }
        return fut2mol;
    }

	/**
	 * Prepares a callable suitable to retrieve a complete Molecule from ChEBI
	 * depending on the information available.
	 * @param molecule an incomplete Molecule.
	 * @return a callable to retrieve the complete Molecule.
	 */
	private ChebiWsCallable getCallable(Molecule molecule) {
		LOGGER.debug("before getting ChEBI callable");
		ChebiWsCallable callable = null;
		final String drugId = molecule.getId();
		if (drugId != null){
			callable = drugId.startsWith("CHEBI:")?
					new ChebiWsCallable(config, drugId,
							SearchCategory.CHEBI_ID):
					new ChebiWsCallable(config, drugId,
							SearchCategory.DATABASE_LINK_REGISTRY_NUMBER_CITATION);
		} else if (molecule.getName() != null){
			callable = new ChebiWsCallable(config, molecule.getName(),
					SearchCategory.ALL_NAMES);
		}
		if (callable == null){
			LOGGER.warn("The molecule has no id or name");
		}
		LOGGER.debug("after getting ChEBI callable");
		return callable;
	}


  public static void getOntologyParentsExample (){

    try {

      // Create client
      ChebiWebServiceClient client = new ChebiWebServiceClient();
      LOGGER.debug("Invoking getOntologyParents");
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

        if (relType.equalsIgnoreCase("has role")) {
            String chebName = ontologyDataItem.getChebiName();
            //IChebiAdapter.MoleculeType.valueOf(chebName)
            if (chebName.contains("drug") || chebName.contains("inhibitor") || chebName.contains("activator")) {
                LOGGER.debug("CHEBI names: " + ontologyDataItem.getChebiName());
                LOGGER.debug("CHEBI names: " + ontologyDataItem.getType());
            }
        }
      }

  }
}
