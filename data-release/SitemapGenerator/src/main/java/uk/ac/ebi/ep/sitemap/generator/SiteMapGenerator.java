/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.sitemap.generator;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.log4j.Logger;
import uk.ac.ebi.ep.exception.Severity;

/**
 *
 * @author joseph
 * @param <T> the output form of this sitemap
 */
public abstract class SiteMapGenerator<T> implements ISiteMap<T> {
 private final Logger LOGGER = Logger.getLogger(SiteMapGenerator.class);
    
    
    
    

    
    
    
    
        /**
     * abstract method to generated siteMap from the specified database
     * 
     * @param fileDirectory the directory where the generated siteMap will be saved
     * @param filename the filename of the generated siteMap
     * @param testMode if in test mode
     * @throws SiteMapException if the siteMap cannot be generated.
     */
    public abstract void generateSitemap(String fileDirectory, String filename, boolean testMode) throws SiteMapException;
    
    protected void checkWriteableDirectory(String dirName) throws SiteMapException {
        File dir = new File(dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!dir.isDirectory()) {
            throw new SiteMapException(String.format("%s is not a directory", dirName), Severity.WARNING);

        } else if (!dir.canWrite()) {

            throw new SiteMapException(String.format("%s is not writeable", dirName), Severity.WARNING);
        }

    }

    protected void makeGzip(File file) throws IOException {
        BufferedInputStream bin = null;
        GZIPOutputStream gzos = null;

        try {

            FileInputStream fin = new FileInputStream(file);
            bin = new BufferedInputStream(fin);
            FileOutputStream fos = new FileOutputStream(file + ".gz");
            gzos = new GZIPOutputStream(fos);
            byte[] buf = new byte[1024];
            int len;
            while ((len = bin.read(buf)) > -1) {
                gzos.write(buf, 0, len);
            }
        } catch (IOException e) {
            LOGGER.fatal("Error while gzipping!", e);
        } finally {
            if (bin != null) {
                bin.close();
            }
            if (gzos != null) {

                gzos.close();
            }
        }

    }
    
    /**
     * This method is used to read the created sitemap( see JunitTest on how to use it)
     * @param filePath the directory and filename
     * @return a bufferedReader that reads the file
     * @throws FileNotFoundException if the file is nto found
     * @throws IOException 
     */
    protected BufferedReader readZipFile(String filePath) throws FileNotFoundException, IOException {

        FileInputStream fin = new FileInputStream(filePath);
        GZIPInputStream gzis = new GZIPInputStream(fin);
        InputStreamReader xover = new InputStreamReader(gzis);
        BufferedReader bufferedReader = new BufferedReader(xover);
        return bufferedReader;
    }
    
}
