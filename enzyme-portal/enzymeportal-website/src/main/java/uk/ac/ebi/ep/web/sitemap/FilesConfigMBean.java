
package uk.ac.ebi.ep.web.sitemap;

/**
 * @author joseph
 */
public interface FilesConfigMBean {

    String getSitemapUrl();

    void setSitemapUrl(String sitemapUrl);

    String getSitemapIndex();

    void setSitemapIndex(String sitemapIndex);

    String getBaseDirectory();

    /**
     * Sets the path to the base directory for any files served by the
     * {@link uk.ac.ebi.ep.web.FileProxyServlet}. Note that this is not a URL
     * (i.e. no 'file:' protocol) but just a path in the file system.
     * @param baseDirectory
     */
    void setBaseDirectory(String baseDirectory);
}
