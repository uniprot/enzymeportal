package uk.ac.ebi.ep.adapter.literature;

import java.util.NoSuchElementException;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolUtils;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

import uk.ac.ebi.biobabel.citations.CitexploreWSClient;

public class CitexploreWSClientPool implements ObjectPool<CitexploreWSClient> {

	private static final Logger LOGGER =
			Logger.getLogger(CitexploreWSClientPool.class);
	
	private static final int MAX_CLIENTS = 8; // FIXME: refactor this out to config!
	
	private static final ObjectPool<CitexploreWSClient> instance =
			new GenericObjectPool<CitexploreWSClient>(
					new BasePoolableObjectFactory<CitexploreWSClient>() {
						@Override
						public CitexploreWSClient makeObject() throws Exception {
							return new CitexploreWSClient();
						}
					}, MAX_CLIENTS);
	
	static {
		try {
			PoolUtils.prefill(instance, MAX_CLIENTS);
		} catch (Exception e){
			LOGGER.error("Unable to prefill pool", e);
		}
	}
	
	private CitexploreWSClientPool(){
		// avoid instantiation
	}
	
	public static ObjectPool<CitexploreWSClient> getInstance(){
		return instance;
	}
	
	public void addObject() throws Exception, IllegalStateException,
			UnsupportedOperationException {
		instance.addObject();
	}

	public CitexploreWSClient borrowObject() throws Exception,
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

	public void invalidateObject(CitexploreWSClient arg0) throws Exception {
		instance.invalidateObject(arg0);
	}

	public void returnObject(CitexploreWSClient arg0) throws Exception {
		instance.returnObject(arg0);
	}

	public void setFactory(PoolableObjectFactory<CitexploreWSClient> arg0)
			throws IllegalStateException, UnsupportedOperationException {
		instance.setFactory(arg0);
	}

}
