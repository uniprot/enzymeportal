package uk.ac.ebi.ep.adapter.chebi;

import uk.ac.ebi.chebi.webapps.chebiWS.model.StarsCategory;

public class ChebiConfig implements ChebiConfigMBean {

	int maxThreads = 10;
	
	StarsCategory searchStars = StarsCategory.ALL;
	
	int timeout = 30000;

	public int getMaxThreads() {
		return maxThreads;
	}

	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	public String getSearchStars() {
		return searchStars.name();
	}

	public void setSearchStars(String searchStars) {
		this.searchStars = StarsCategory.valueOf(searchStars);
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
}
