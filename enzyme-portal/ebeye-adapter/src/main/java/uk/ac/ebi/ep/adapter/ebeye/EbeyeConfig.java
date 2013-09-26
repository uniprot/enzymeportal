package uk.ac.ebi.ep.adapter.ebeye;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration for the EB-Eye search.
 * @author rafa
 *
 */
public class EbeyeConfig implements EbeyeConfigMBean {

	private int ebeyeResultsLimit;
	private int maxAccessionsInQuery;
	private int maxResults;
	private int maxChebiResults;
	private int maxUniprotResults;
	private int maxUniprotResultsFromChebi;
	private int maxUniprotResultsFromOtherDomains;
	private int maxThreads;
	private int threadTimeout;
	private Map<String, String[]> searchFields;

    /**
     * Simple constructor.
     * The search fields for each EB-Eye domain are set initially to the default
     * values in the Domains enumeration.
     * @see Domains
     */
    public EbeyeConfig() {
        searchFields = new HashMap<String, String[]>();
        resetSearchFields();
    }

    public int getResultsLimit() {
		return ebeyeResultsLimit;
	}

	public void setResultsLimit(int i) {
		ebeyeResultsLimit = i;
	}

	public int getMaxAccessionsInQuery() {
		return maxAccessionsInQuery;
	}

	public void setMaxAccessionsInQuery(int i) {
		maxAccessionsInQuery = i;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int i) {
		maxResults = i;
	}

	public int getMaxChebiResults() {
		return maxChebiResults;
	}

	public void setMaxChebiResults(int i) {
		maxChebiResults = i;
	}

	public int getMaxUniprotResults() {
		return maxUniprotResults;
	}

	public void setMaxUniprotResults(int i) {
		maxUniprotResults = i;
	}

	public int getMaxUniprotResultsFromChebi() {
		return maxUniprotResultsFromChebi;
	}

	public void setMaxUniprotResultsFromChebi(int i) {
		maxUniprotResultsFromChebi = i;
	}

	public int getMaxUniprotResultsFromOtherDomains() {
		return maxUniprotResultsFromOtherDomains;
	}

	public void setMaxUniprotResultsFromOtherDomains(int i) {
		maxUniprotResultsFromOtherDomains = i;
	}

	public int getMaxThreads() {
		return maxThreads;
	}

	public void setMaxThreads(int i) {
		maxThreads = i;
	}

	public int getThreadTimeout() {
		return threadTimeout;
	}

	public void setThreadTimeout(int i) {
		threadTimeout = i;
	}

    public String[] getSearchFields(String domain) {
        return searchFields.get(domain);
    }

    /**
     * {@inheritDoc}
     * @param domain {@inheritDoc}
     * @param fields {@inheritDoc} A space-or-colon-or-semicolon separated list
     *      of field name.
     */
    public void setSearchFields(String domain, String fields) {
        if (fields == null || fields.trim().length() == 0){
            searchFields.remove(domain);
        } else {
            searchFields.put(domain, fields.split("\\s*[\\s,;]\\s*"));
        }
    }

    public void resetSearchFields() {
        for (Domains domain : Domains.values()) {
            searchFields.put(domain.name(), domain.getSearchFields());
        }
    }

}
