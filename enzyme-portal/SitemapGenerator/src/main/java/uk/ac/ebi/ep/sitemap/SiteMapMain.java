package uk.ac.ebi.ep.sitemap;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.log4j.Logger;
import uk.ac.ebi.ep.exception.Severity;

/**
 * This is the main class for the sitemapGenerator.
 * To run with maven, execute the following command.
 * for example:
 * mvn exec:java -Dexec.mainClass="uk.ac.ebi.ep.sitemap.SiteMapMain" -Dexec.args="ep-mm-db-enzdev /Users/joseph SiteMap false" 
 * where ep-mm-db-enzdev Users/joseph SiteMap true are input parameters for DBConfig,
 * fileDirectory, filename and testingMode respectively.
 *
 */
public class SiteMapMain {
    
    public final static Logger LOGGER = Logger.getLogger(SiteMapMain.class);
    
    public static void main(String[] args) throws FileNotFoundException, SiteMapException, IOException {
        LOGGER.info("***************SitemapGenerator Launched*********************");
      
       
  //////////////comment here ///////////////////////
        
//        args = new String[4];
//        String dbConfig = "ep-mm-db-enzdev";
//        //String dbConfig = "ep-mm-db-enzprel";
//        String userHome = System.getProperty("user.home");
//        String filename = "SiteMap";
//        String testing = "true";
//        
//               
//        args[0] = dbConfig;
//        args[1] = userHome;
//        args[2] = filename;
//        args[3] = testing;
        

       //////////////uncomment for testing parameters only ///////////////////////
       
       
       
      
        
        if(args == null){
            System.exit(1);
        }

        if (args.length == 0 || args.length > 4) {
            
            throw new SiteMapException("Three arguments are required. DbConfig, fileDirectory, and filename", Severity.WARNING);
        } else {
            
            
            if (args.length <= 3) {
                throw new SiteMapException(String.format("you have already provided %s, %s and %s, however, one more argument is needed. { testMode ? true : false}", args[0], args[1],args[2]), Severity.WARNING);
            }
            //System.err.print("Params passed "+ args[0] + " : "+ args[1] + " : "+ args[2] + " : "+ testMode);
            SiteMapResources generator = new EpSiteMapImpl(args[0]);// new SiteMapImpl(args[0]);
            //SiteMapResources generator =  new SiteMapImpl(args[0]);
              boolean testMode = Boolean.parseBoolean(args[3]);
            try {
                generator.generateSitemap(args[1], args[2],testMode);
            } catch (SiteMapException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
            
        }
        
    }
    }
