package uk.ac.ebi.ep.adapter.das;

public class DASConfig implements DASConfigMBean {

	private int clientPoolSize = 4;

	/* (non-Javadoc)
	 * @see uk.ac.ebi.ep.adapter.das.DASConfigMBean#getDasClientPoolSize()
	 */
	public int getClientPoolSize() {
		return clientPoolSize;
	}

	/**
	 * {@inheritDoc}
	 * <br>
	 * <b>Please note that this implementation does not notify yet to the pool
	 * of the change</b>.
	 */
	public void setClientPoolSize(int clientPoolSize) {
		this.clientPoolSize = clientPoolSize;
	}

}
