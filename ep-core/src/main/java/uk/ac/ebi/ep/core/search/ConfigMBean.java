package uk.ac.ebi.ep.core.search;

public interface ConfigMBean {

	public abstract void setMaxPages(int maxPages);

	public abstract int getMaxPages();

	public abstract void setResultsPerPage(int resultsPerPage);

	public abstract int getResultsPerPage();

}
