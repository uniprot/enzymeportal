package uk.ac.ebi.ep.adapter.ebeye;

/**
 * MBean interface for the EB-Eye search configuration.
 * @author rafa
 */
public interface EbeyeConfigMBean {

	// these were in IEbeyeAdapter (ebeye-adapter module)
	
	public abstract int getResultsLimit();

	public void setResultsLimit(int i); // EBEYE_RESULT_LIMIT
	
	public abstract int getMaxAccessionsInQuery();

	/**
	 * Sets the maximum number of accessions to be included in the same query
	 * sent to EB-Eye. <b>It should not exceed 400</b>.
	 * @param i the maximum number of accessions to be included in one query.
	 */
	public void setMaxAccessionsInQuery(int i); // EBEYE_NR_OF_QUERY_IN_LIMIT
	
	public abstract int getMaxResults();

	/**
	 * Sets the maximum number of results retrieved for a single domain other
	 * than UniProt or ChEBI.
	 * @param i the limit of results per domain.
	 */
	public void setMaxResults(int i); // EP_RESULTS_PER_DOIMAIN_LIMIT
	
	public abstract int getMaxChebiResults();

	/**
	 * Sets the maximum number of results to retrieve for the ChEBI domain.
	 * @param i the maximum number of results to retrieve for the ChEBI domain.
	 */
	public void setMaxChebiResults(int i); // EP_CHEBI_RESULTS_LIMIT
	/**
	 * @return the maximum number of results to retrieve for the UniProt domain.
	 */
	public abstract int getMaxUniprotResults();

	/**
	 * Sets the maximum number of results to retrieve for the UniProt domain.
	 * @param i the maximum number of results to retrieve for the UniProt domain.
	 */
	public void setMaxUniprotResults(int i); // QUERY_ENZYME_DOMAIN_RESULT_LIMIT
	
	public abstract int getMaxUniprotResultsFromChebi();

	/**
	 * Sets the maximum number of UniProt accessions to use from a query to
	 * the ChEBI domain.
	 * @param i the maximum number of UniProt accessions to use
	 */
	public void setMaxUniprotResultsFromChebi(int i); // QUERY_UNIPROT_FIELD_RESULT_LIMIT
	
	public abstract int getMaxUniprotResultsFromOtherDomains();

	/**
	 * Sets the maximum number of UniProt accessions to use from a joint query
	 * to domains other than UniProt or ChEBI.
	 * @param i the maximum number of UniProt accessions to use
	 */
	public void setMaxUniprotResultsFromOtherDomains(int i); // JOINT_QUERY_UNIPROT_FIELD_RESULT_LIMIT

	public abstract int getMaxThreads();

	/**
	 * Sets the maximum number of threads to run concurrently.
	 * @param i the maximum number of threads.
	 */
	public void setMaxThreads(int i); // EP_THREADS_LIMIT
	
	public abstract int getThreadTimeout();

	/**
	 * Sets the timeout for threaded jobs.
	 * @param i the timeout in miliseconds.
	 */
	public void setThreadTimeout(int i); // EBEYE_ONLINE_REQUEST_TIMEOUT
}
