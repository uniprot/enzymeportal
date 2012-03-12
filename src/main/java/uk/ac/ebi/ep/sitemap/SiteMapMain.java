package uk.ac.ebi.ep.sitemap;

import org.apache.log4j.Logger;
import uk.ac.ebi.ep.exception.Severity;

/**
 * Hello world!
 *
 */
public class SiteMapMain {

    private final static Logger LOGGER = Logger.getLogger(SiteMapResources.class);

    //run this on commandline for maven using this command
    // mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.sitemap.SiteMapMain" -Dexec.args="ep-mm-db-enzdev C:/Users/joseph EnzymePortalSiteMap" 
    //change the dbconfig, filedirectory and filename as required
    public static void main(String[] arg) throws SiteMapException {

        String[] args = new String[3];
        String dbConfig = "ep-mm-db-enzdev";
        String userHome = System.getProperty("user.home");
        String filename = "EnzymePortalSiteMap";
        args[0] = dbConfig;
        args[1] = userHome;
        args[2] = filename;

        if (args.length == 0) {

            throw new SiteMapException("These arguments are required. DbConfig, fileDirectory, and filename", Severity.WARNING);
        } else {


            if (args.length <= 2) {
                throw new SiteMapException(String.format("you have already provided %s and %s, however, one more argument is needed.", args[0], args[1]), Severity.WARNING);
            }
            SiteMapResources generator = new SiteMapImpl(args[0]);

            try {
                generator.generateSitemap(args[1], args[2]);
            } catch (SiteMapException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }

        }

    }
}
