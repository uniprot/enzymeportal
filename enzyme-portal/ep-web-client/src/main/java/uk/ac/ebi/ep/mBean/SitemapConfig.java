/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.mBean;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author joseph
 */
public class SitemapConfig implements SitemapConfigMBean {

    private String userHome = System.getProperty("user.home");
    private String filename = "sitemap_index";
    //private String output = String.format("%s\\%s.xml.gz", userHome, filename);
    private String output = String.format("%s/%s.xml", userHome, filename);
    private String sitemapUrl = output;
    private List<String> sitemapList = new LinkedList<String>();
    private String sitemapIndex;

    public String getSitemapUrl() {
        return sitemapUrl;
    }

    public void setSitemapUrl(String sitemapUrl) {
        this.sitemapUrl = sitemapUrl;
    }

    public String getSitemapIndex() {
        return sitemapIndex;
    }

    public void setSitemapIndex(String sitemapIndex) {
        this.sitemapIndex = sitemapIndex;
    }

    /**read the files this directory and add only siteMaps for processing
     * 
     * @param directory the directory where the SiteMaps are located
     */
    public void readFile(String directory) {
        String[] s = directory.split(":");
        File f = new File(s[1]);

        File[] files = new File(s[1]).listFiles();

        if (files.length > 0) {
            for (File file : files) {
                if (file.isFile()) {

                    if (file.getName().startsWith("SiteMap")) {
                        sitemapList.add(file.getName());

                    }
                }
            }
        }
    }

    public List<String> getSitemapList() {
        return sitemapList;
    }
}
