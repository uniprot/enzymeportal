package uk.ac.ebi.ep.adapter.das;

import java.util.NoSuchElementException;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolUtils;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

import uk.ac.ebi.das.jdas.client.FeaturesClient;

/**
 * Pool of {@link FeaturesClient}s. These objects are slow to
 * instantiate and may create a memory leak.<br>
 * This class is a singleton.
 * @author rafa
 *
 */
public class FeaturesClientPool implements ObjectPool<FeaturesClient> {

	private static final Logger LOGGER =
			Logger.getLogger(FeaturesClientPool.class);

	private static final int MAX_CLIENTS = 8; // FIXME: refactor this out to config
	
	private static final ObjectPool<FeaturesClient> instance =
			new GenericObjectPool<FeaturesClient>(
					new BasePoolableObjectFactory<FeaturesClient>(){
						@Override
						public FeaturesClient makeObject() throws Exception {
							return new FeaturesClient();
						}
					}, MAX_CLIENTS);
	static {
		try {
			PoolUtils.prefill(instance, MAX_CLIENTS);
		} catch (Exception e){
			LOGGER.error("Unable to prefill pool", e);
		}
	}
	
	private FeaturesClientPool(){
		// avoid instantiation
	}
	
	public static ObjectPool<FeaturesClient> getInstance(){
		return instance;
	}
	
	public void addObject() throws Exception, IllegalStateException,
			UnsupportedOperationException {
		instance.addObject();
	}

	public FeaturesClient borrowObject() throws Exception,
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

	public void invalidateObject(FeaturesClient arg0) throws Exception {
		instance.invalidateObject(arg0);
	}

	public void returnObject(FeaturesClient arg0) throws Exception {
		instance.returnObject(arg0);
	}

	public void setFactory(PoolableObjectFactory<FeaturesClient> arg0)
			throws IllegalStateException, UnsupportedOperationException {
		instance.setFactory(arg0);
	}

}
