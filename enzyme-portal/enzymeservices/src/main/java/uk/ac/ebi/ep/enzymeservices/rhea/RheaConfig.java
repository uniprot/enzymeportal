package uk.ac.ebi.ep.enzymeservices.rhea;

/**
 * Configuration for the Rhea proxy in the enzyme portal.
 * @author rafa
 * @since 1.0.4
 */
public class RheaConfig implements RheaConfigMBean {

    private String reactionBaseUrl =
            "http://www.ebi.ac.uk/rhea/reaction.xhtml?id=";

    public String getReactionBaseUrl() {
        return reactionBaseUrl;
    }

    public void setReactionBaseUrl(String reactionBaseUrl) {
        this.reactionBaseUrl = reactionBaseUrl;
    }
    
}
