package uk.ac.ebi.ep.adapter.das;

import java.util.NoSuchElementException;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolUtils;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

import uk.ac.ebi.das.jdas.client.FeaturesClient;

/**
 * Pool of {@link FeaturesClient}s. These objects are slow to instantiate and
 * may create a memory leak.<br>
 * This class is a singleton.
 * @author rafa
 *
 */
public class FeaturesClientPool implements ObjectPool<FeaturesClient> {

	private static final ObjectPool<FeaturesClient> instance =
			new GenericObjectPool<FeaturesClient>(
					new BasePoolableObjectFactory<FeaturesClient>(){
						@Override
						public FeaturesClient makeObject() throws Exception {
							return new FeaturesClient();
						}
					});
	
	private FeaturesClientPool(){
		// avoid instantiation
	}
	
	public static ObjectPool<FeaturesClient> getInstance(){
		return instance;
	}

	/**
	 * Sets the size of the pool.
	 * @param size
	 * @throws IllegalArgumentException if the passed size is lower than the
	 * 		number of current active clients.
	 * @throws Exception in case of trouble manipulating the underlying pool.
	 */
	public static void setSize(int size)
	throws IllegalArgumentException, Exception {
		if (size < instance.getNumActive()){
			throw new IllegalArgumentException(
					instance.getNumActive() + " active clients in the pool");
		}
		if (size > instance.getNumActive()){
			instance.clear();
			PoolUtils.prefill(instance, size - instance.getNumActive());
		}
		((GenericObjectPool<FeaturesClient>) instance).setMaxActive(size);
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
