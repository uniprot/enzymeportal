package uk.ac.ebi.ep.enzymeservices.chebi;

import java.util.NoSuchElementException;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolUtils;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

import uk.ac.ebi.chebi.webapps.chebiWS.client.ChebiWebServiceClient;

/**
 * Pool of <code>ChebiWebServiceClient</code>s used by {@link ChebiWsCallable}
 * (<code>ChebiWebServiceClient</code> objects are slow to instantiate).<br>
 * This class is a singleton.
 * @author rafa
 *
 */
public class ChebiWsClientPool implements ObjectPool<ChebiWebServiceClient> {

	private static final Logger LOGGER =
			Logger.getLogger(ChebiWsClientPool.class);
	
	private static final int MAX_CLIENTS = 8; // FIXME: refactory this out to config
	
	private static final ObjectPool<ChebiWebServiceClient> instance =
			new GenericObjectPool<ChebiWebServiceClient>(
				new ChebiWsClientFactory(), MAX_CLIENTS);
	static {
		try {
			PoolUtils.prefill(instance, MAX_CLIENTS);
		} catch (Exception e) {
			LOGGER.error("Unable to prefill pool", e);
		}
	}
	
	private ChebiWsClientPool(){
		// avoid instantiation
	}
	
	public static ObjectPool<ChebiWebServiceClient> getInstance(){
		return instance;
	}
	
	public void addObject() throws Exception, IllegalStateException,
			UnsupportedOperationException {
		instance.addObject();
	}

	public ChebiWebServiceClient borrowObject() throws Exception,
			NoSuchElementException, IllegalStateException {
		return instance.borrowObject();
	}

	public void clear() throws Exception, UnsupportedOperationException {
		instance.clear();
	}

	public void close() throws Exception {
		instance.close();
	}

	public int getNumActive() throws UnsupportedOperationException {
		return instance.getNumActive();
	}

	public int getNumIdle() throws UnsupportedOperationException {
		return instance.getNumIdle();
	}

	public void invalidateObject(ChebiWebServiceClient arg0) throws Exception {
		instance.invalidateObject(arg0);
	}

	public void returnObject(ChebiWebServiceClient arg0) throws Exception {
		instance.returnObject(arg0);
	}

	public void setFactory(PoolableObjectFactory<ChebiWebServiceClient> arg0)
			throws IllegalStateException, UnsupportedOperationException {
		instance.setFactory(arg0);
	}

}
