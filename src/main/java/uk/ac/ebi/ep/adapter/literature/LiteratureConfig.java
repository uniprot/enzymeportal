package uk.ac.ebi.ep.adapter.literature;

public class LiteratureConfig implements LiteratureConfigMBean {

	private int maxThreads = 4;

	/* (non-Javadoc)
	 * @see uk.ac.ebi.ep.adapter.literature.LiteratureConfigMBean#getMaxThreads()
	 */
	public int getMaxThreads() {
		return maxThreads;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ebi.ep.adapter.literature.LiteratureConfigMBean#setMaxThreads(int)
	 */
	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}
	
	
}
