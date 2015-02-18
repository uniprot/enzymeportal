package uk.ac.ebi.ep.adapter.literature;

import org.springframework.stereotype.Service;

@Service
public class LiteratureConfig implements LiteratureConfigMBean {

	private int maxThreads = 4;
	
	private int citexploreClientPoolSize = 8;

	private int citexploreConnectTimeout = 0;

    private int citexploreReadTimeout = 0;

	private boolean useCitexploreWs = true;

	private int maxCitations = 50;

	/* (non-Javadoc)
	 * @see uk.ac.ebi.ep.adapter.literature.LiteratureConfigMBean#getMaxThreads()
	 */
	public int getMaxThreads() {
		return maxThreads;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ebi.ep.adapter.literature.LiteratureConfigMBean#setMaxThreads(int)
	 */
	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	public int getCitexploreClientPoolSize() {
		return citexploreClientPoolSize;
	}

	/**
	 * {@inheritDoc}
	 * <br>
	 * <b>Please note that this implementation does not notify of the change
	 * yet. This setting is only used at pool creation time.</b>
	 */
	public void setCitexploreClientPoolSize(int size) {
		this.citexploreClientPoolSize = size;
	}

    public int getCitexploreConnectTimeout() {
        return citexploreConnectTimeout;
    }

    public void setCitexploreConnectTimeout(int citexploreConnectTimeout) {
        this.citexploreConnectTimeout = citexploreConnectTimeout;
    }

    public int getCitexploreReadTimeout() {
        return citexploreReadTimeout;
    }

    public void setCitexploreReadTimeout(int citexploreReadTimeout) {
        this.citexploreReadTimeout = citexploreReadTimeout;
    }

    public boolean isUseCitexploreWs() {
        return useCitexploreWs;
    }

    public void setUseCitexploreWs(boolean useCitexploreWs) {
        this.useCitexploreWs = useCitexploreWs;
    }
    
    public int getMaxCitations() {
        return maxCitations;
    }

    public void setMaxCitations(int maxCitations) {
        this.maxCitations = maxCitations;
    }
}
