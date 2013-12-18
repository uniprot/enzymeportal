package uk.ac.ebi.ep.adapter.chebi;

import org.apache.commons.pool.BasePoolableObjectFactory;

import uk.ac.ebi.chebi.webapps.chebiWS.client.ChebiWebServiceClient;

/** 
 * Factory used by {@link ChebiWsClientPool} to get the clients.
 * @author rafa
 *
 */
class ChebiWsClientFactory
extends BasePoolableObjectFactory<ChebiWebServiceClient>{

	@Override
	public ChebiWebServiceClient makeObject() throws Exception {
		return new ChebiWebServiceClient();
	}
	
}