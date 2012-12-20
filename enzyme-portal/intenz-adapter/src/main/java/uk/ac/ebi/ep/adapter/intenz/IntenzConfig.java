package uk.ac.ebi.ep.adapter.intenz;

public class IntenzConfig implements IntenzConfigMBean {

	private int timeout;
	
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
