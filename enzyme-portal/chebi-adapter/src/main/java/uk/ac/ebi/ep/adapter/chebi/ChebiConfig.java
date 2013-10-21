package uk.ac.ebi.ep.adapter.chebi;

import uk.ac.ebi.chebi.webapps.chebiWS.model.StarsCategory;

public class ChebiConfig implements ChebiConfigMBean {

	int maxThreads = 10;
	
	int maxRetrievedMolecules = 3;
	
	StarsCategory searchStars = StarsCategory.ALL;
	
	int timeout = 30000;
	
	protected String compoundBaseUrl =
	        "http://www.ebi.ac.uk/chebi/searchId.do?chebiId=";

	protected String compoundImgBaseUrl =
	        "http://www.ebi.ac.uk/chebi/displayImage.do?defaultImage=true&imageIndex=0&chebiId=";
	
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

	public int getMaxRetrievedMolecules() {
		return maxRetrievedMolecules;
	}

	public void setMaxRetrievedMolecules(int maxRetrievedMolecules) {
		this.maxRetrievedMolecules = maxRetrievedMolecules;
	}

    public String getCompoundBaseUrl() {
        return compoundBaseUrl;
    }

    public void setCompoundBaseUrl(String compoundBaseUrl) {
        this.compoundBaseUrl = compoundBaseUrl;
    }

    public String getCompoundImgBaseUrl() {
        return compoundImgBaseUrl;
    }

    public void setCompoundImgBaseUrl(String compoundImgBaseUrl) {
        this.compoundImgBaseUrl = compoundImgBaseUrl;
    }
	
}
