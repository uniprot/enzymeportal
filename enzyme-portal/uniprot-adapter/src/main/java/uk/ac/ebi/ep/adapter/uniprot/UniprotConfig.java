package uk.ac.ebi.ep.adapter.uniprot;

public class UniprotConfig implements UniprotConfigMBean {

	private int timeout;
	
	private boolean reviewed;
	
	/**
	 * http://www.uniprot.org/uniprot/?format=tab&sort=score&query={0}&columns={1}
	 */
	private String wsUrl;
	
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public boolean isReviewed() {
		return reviewed;
	}

	public void setReviewed(boolean reviewed) {
		this.reviewed = reviewed;
	}

}
