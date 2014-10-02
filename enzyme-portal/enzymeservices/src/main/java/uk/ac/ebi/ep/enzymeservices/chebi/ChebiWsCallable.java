package uk.ac.ebi.ep.enzymeservices.chebi;

import java.util.concurrent.Callable;
import org.apache.log4j.Logger;
import uk.ac.ebi.chebi.webapps.chebiWS.client.ChebiWebServiceClient;
import uk.ac.ebi.chebi.webapps.chebiWS.model.Entity;
import uk.ac.ebi.chebi.webapps.chebiWS.model.LiteEntityList;
import uk.ac.ebi.chebi.webapps.chebiWS.model.SearchCategory;
import uk.ac.ebi.chebi.webapps.chebiWS.model.StarsCategory;
import uk.ac.ebi.ep.data.enzyme.model.Molecule;


/**
 * Utility class to retrieve Molecule objects from ChEBI in several threads.
 * Basically, a wrapper around a ChEBI web service client.
 * @author rafa
 *
 */
public class ChebiWsCallable implements Callable<Molecule> {
	
	private static final Logger LOGGER = Logger.getLogger(ChebiWsCallable.class);
	
	private ChebiConfig config;
	private String query;
	private SearchCategory field;
	
    /**
     * Constructor.
     * @param config 
     * @param query The query sent to ChEBI web services.
     * @param field the field or category to be searched (see
     * 		{@link SearchCategory} enumeration), usually
     * 		{@link SearchCategory#CHEBI_ID} or
     * 		{@link SearchCategory#ALL_NAMES}.
     */
	public ChebiWsCallable(ChebiConfig config, String query, SearchCategory field) {
		this.config = config;
		this.query = query;
		this.field = field;
	}

	public Molecule call() throws Exception {
		Molecule molecule = null;
		switch (field) {
		case CHEBI_ID:
			molecule = getMoleculeByChebiId(query);
			break;
		default:
			molecule = getMolecule(query);
			break;
		}
		return molecule;
	}

	/**
	 * Retrieves a compound by ChEBI ID.
	 * @param chebiId the ChEBI ID.
	 * @return a Molecule object, or <code>null</code> if not found.
	 * @throws ChebiFetchDataException
	 */
	Molecule getMoleculeByChebiId(String chebiId)
	throws ChebiFetchDataException{
		return Transformer.transformChebiToEpMoleculeEntity(
				getChebiCompleteEntity(chebiId));
	}
	
	/**
	 * Retrieves a compound from ChEBI by xref identifier.
	 * @param xref an external database identifier.
	 * @return a Molecule, or <code>null</code> if not found in ChEBI.
	 * @throws ChebiFetchDataException
	 */
	Molecule getMoleculeByXref(String xref) throws ChebiFetchDataException {
		return getMolecule(xref);
	}
	
	/**
	 * Retrieves a compound from ChEBI by name.
	 * @param name a compound name or synonym.
	 * @return a Molecule, or <code>null</code> if not found in ChEBI.
	 * @throws ChebiFetchDataException
	 */
	Molecule getMoleculeByName(String name) throws ChebiFetchDataException{
		return getMolecule(name);
	}

	/**
	 * Does the actual job of querying the web services for a compound name
	 * or cross-reference.
	 * @param s The query sent to the web services.
	 * @return one Molecule object, or <code>null</code> if not found.
	 * 		Note that it will be only the first one if the query matches more
	 * 		than one. 
	 * @throws ChebiFetchDataException
	 */
    private Molecule getMolecule(String s) throws ChebiFetchDataException{
		Molecule molecule = null;
		ChebiWebServiceClient chebiWsClient = null;
		try {
			StarsCategory stars = config != null?
					config.searchStars : StarsCategory.ALL;
			chebiWsClient = ChebiWsClientPool.getInstance().borrowObject();
			LiteEntityList lite = chebiWsClient.getLiteEntity(s, field, 1, stars);
			if (lite.getListElement().isEmpty()){
				LOGGER.warn("No ChEBI entity found for " + s);
			} else {
				Entity complete = getChebiCompleteEntity(
						lite.getListElement().get(0).getChebiId());
				molecule = Transformer.transformChebiToEpMoleculeEntity(complete);
			}
		} catch (Exception e) {
			
                    LOGGER.error("ChebiFetchDataException", e);
		} finally {
			if (chebiWsClient != null){
				try {
					ChebiWsClientPool.getInstance().returnObject(chebiWsClient);
				} catch (Exception e) {
					LOGGER.error("Unable to return chebi WS client", e);
				}
			}
		}
		return molecule;
	}

    /**
     * Retrieves a complete ChEBI Entity by ChEBI ID.
     * @param chebiId the ChEBI ID
     * @return a ChEBI web service Entity object
     * @throws ChebiFetchDataException
     */
	private Entity getChebiCompleteEntity(String chebiId)
	throws ChebiFetchDataException {
        Entity entity = null;
        ChebiWebServiceClient chebiWsClient = null;
        try {
        	chebiWsClient = ChebiWsClientPool.getInstance().borrowObject();
			entity = chebiWsClient.getCompleteEntity(chebiId);
        } catch (Exception e) {
            throw new ChebiFetchDataException("Failed to retrieve complete entry from Chebi WS ", e);
		} finally {
			if (chebiWsClient != null){
				try {
					ChebiWsClientPool.getInstance().returnObject(chebiWsClient);
				} catch (Exception e) {
					LOGGER.error("Unable to return chebi WS client", e);
				}
			}
        }
        return entity;
    }
}
