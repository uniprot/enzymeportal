/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.mBean;

/**
 *
 * @author joseph
 */
public class SitemapConfig implements SitemapConfigMBean {


    private String userHome = System.getProperty("user.home");
    private String filename = "SiteMap";
    private String output = String.format("%s\\%s.xml.gz", userHome, filename);
    private String sitemapUrl = output;

    public String getSitemapUrl() {
        return sitemapUrl;
    }

    public void setSitemapUrl(String sitemapUrl) {
        this.sitemapUrl = sitemapUrl;
    }

}
