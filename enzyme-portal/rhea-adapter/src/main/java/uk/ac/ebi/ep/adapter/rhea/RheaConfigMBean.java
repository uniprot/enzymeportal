package uk.ac.ebi.ep.adapter.rhea;

/**
 * 
 * @author rafa
 * @since 1.0.4
 */
public interface RheaConfigMBean {

    public abstract String getReactionBaseUrl();

    /**
     * Sets the base URL of reactions in Rhea web site.
     * @param reactionBaseUrl the base URL.
     */
    public abstract void setReactionBaseUrl(String reactionBaseUrl);

}