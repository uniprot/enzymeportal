package uk.ac.ebi.ep.sitemap;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.log4j.Logger;
import uk.ac.ebi.ep.exception.Severity;

/**
 * This is the main class for the sitemapGenerator.
 * To run with maven, execute the following command.
 * for example:
 * mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.sitemap.SiteMapMain" -Dexec.args="ep-mm-db-enzdev C:/Users/joseph SiteMap" 
 * where ep-mm-db-enzdev C:/Users/joseph SiteMap" are input parameters for DBConfig,
 * fileDirectory and filename respectively.
 *
 */
public class SiteMapMain {

    private final static Logger LOGGER = Logger.getLogger(SiteMapMain.class);

    public static void main(String[] args) throws FileNotFoundException, SiteMapException, IOException {

        args = new String[3];
//        String dbConfig = "ep-mm-db-enzdev";
//        String userHome = System.getProperty("user.home");
//        String filename = "SiteMap";
//        args[0] = dbConfig;
//        args[1] = userHome;
//        args[2] = filename;

        if (args.length == 0 || args.length > 3) {

            throw new SiteMapException("Three arguments are required. DbConfig, fileDirectory, and filename", Severity.WARNING);
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
