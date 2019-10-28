package uk.ac.ebi.ep.web.sitemap;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @author joseph
 */
public class FilesConfig implements FilesConfigMBean {

    private final String userHome = System.getProperty("user.home");
    private final String filename = "sitemap_index";
    //private String output = String.format("%s\\%s.xml.gz", userHome, filename);
    private final String output = String.format("%s/%s.xml", userHome, filename);
    private String sitemapUrl = output;
    private final List<String> sitemapList = new LinkedList<>();
    private String sitemapIndex;
    private String baseDirectory;

    @Override
    public String getSitemapUrl() {
        return sitemapUrl;
    }

    @Override
    public void setSitemapUrl(String sitemapUrl) {
        this.sitemapUrl = sitemapUrl;
    }

    @Override
    public String getBaseDirectory() {
        return baseDirectory;
    }

    @Override
    public void setBaseDirectory(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    @Override
    public String getSitemapIndex() {
        return sitemapIndex;
    }

    @Override
    public void setSitemapIndex(String sitemapIndex) {
        this.sitemapIndex = sitemapIndex;
    }

    /**
     * read the files this directory and add only siteMaps for processing
     *
     * @param directory the directory where the SiteMaps are located
     */
    public void readFile(String directory) {
        String[] s = directory.split(":");

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
