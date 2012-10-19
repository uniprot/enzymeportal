package uk.ac.ebi.ep.adapter.literature;

import java.util.NoSuchElementException;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolUtils;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

import uk.ac.ebi.biobabel.citations.CitexploreWSClient;

/**
 * A pool of CiteXplore web services clients. Its size is configurable, and
 * is both the number of clients in the pool and the maximum number of active
 * ones.
 * @author rafa
 *
 */
public class CitexploreWSClientPool implements ObjectPool<CitexploreWSClient> {

	private static final ObjectPool<CitexploreWSClient> instance =
			new GenericObjectPool<CitexploreWSClient>(
					new BasePoolableObjectFactory<CitexploreWSClient>() {
						@Override
						public CitexploreWSClient makeObject() throws Exception{
							return new CitexploreWSClient();
						}
					});
	
	private CitexploreWSClientPool(){
		// avoid instantiation
	}
	
	public static ObjectPool<CitexploreWSClient> getInstance(){
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
		((GenericObjectPool<CitexploreWSClient>) instance).setMaxActive(size);
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
