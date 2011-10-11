package uk.ac.ebi.ep.core.search;

import uk.ac.ebi.ep.core.search.IEnzymeFinder.UniprotImplementation;
import uk.ac.ebi.ep.core.search.IEnzymeFinder.UniprotSource;

public interface ConfigMBean {

	public abstract void setMaxPages(int maxPages);

	public abstract int getMaxPages();

	public abstract void setResultsPerPage(int resultsPerPage);

	public abstract int getResultsPerPage();

	/**
	 * Sets the resource used to retrieve UniProt entries.
	 * @param retrieverUniprotSource
	 */
	public abstract void setRetrieverUniprotSource(String retrieverUniprotSource);

	public abstract String getRetrieverUniprotSource();

	/**
	 * Sets the resource used to search UniProt entries.
	 * @param finderUniprotSource
	 */
	public abstract void setFinderUniprotSource(String finderUniprotSource);

	public abstract String getFinderUniprotSource();
	
	/**
	 * Sets the implementation used to search/retrieve UniProt entries.
	 * @param imp
	 */
	public abstract void setUniprotImplementation(String imp);
	
	public abstract String getUniprotImplementation();

}
