package uk.ac.ebi.ep.adapter.literature;

public interface LiteratureConfigMBean {

	public abstract int getMaxThreads();

	/**
	 * Sets the maximum number of threads to be used to retrieve citations.
	 * @param maxThreads
	 */
	public abstract void setMaxThreads(int maxThreads);

	public abstract int getCitexploreClientPoolSize();

    /*
	 * Sets the size of the pool with CiteXplore clients.
	 * @param size
	 *
	 * This method won't be exposed via JMX until its implementation notifies
	 * the pool.
	public abstract void setCitexploreClientPoolSize(int size);
	*/

    int getCitexploreConnectTimeout();

    /**
     * Sets the timeout to get a connection to the CiteXplore web service.
     * @param citexploreConnectTimeout the timeout in ms
     * @since 1.0.7
     */
    void setCitexploreConnectTimeout(int citexploreConnectTimeout);

    int getCitexploreReadTimeout();

    /**
     * Sets the timeout to read data since the connection to the CiteXplore web
     * service has been established.
     * @param citexploreReadTimeout the timeout in ms
     * @since 1.0.7
     */
    void setCitexploreReadTimeout(int citexploreReadTimeout);

    boolean isUseCitexploreWs();

    /**
     * Whether to use CiteXplore to get the list of citations for a given
     * enzyme, or only to retrieve the nitty-gritty details
     * of them.<br/>
     * Set this to <code>false</code> in case of trouble with CiteXplore web
     * service. The details will be then retrieved from other sources after
     * the timeouts have expired.
     * @param useCitexploreWs <code>true</code> to retrieve the list of
     *      citations from CiteXplore (one single request), <code>false</code>
     *      to retrieve it from the different resources (one request per tab -
     *      enzyme|structure|molecules|diseases - plus one request more per
     *      citation).
     * @since 1.0.7
     */
    void setUseCitexploreWs(boolean useCitexploreWs);

    int getMaxCitations();

    /**
     * Sets the maximum number of citations to be retrieved and displayed.
     * @param maxCitations the maximum number of citations.
     */
    void setMaxCitations(int maxCitations);

}