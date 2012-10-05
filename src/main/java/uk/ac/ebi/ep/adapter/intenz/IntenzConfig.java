package uk.ac.ebi.ep.adapter.intenz;

public class IntenzConfig implements IntenzConfigMBean {

	private int timeout;
	
	private String intenzXmlUrl;
	
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getIntenzXmlUrl() {
		return intenzXmlUrl;
	}

	public void setIntenzXmlUrl(String intenzXmlUrl) {
		this.intenzXmlUrl = intenzXmlUrl;
	}

}
