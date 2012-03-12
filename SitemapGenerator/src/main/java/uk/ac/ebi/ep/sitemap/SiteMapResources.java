/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.sitemap;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;


import java.util.zip.GZIPOutputStream;
import org.apache.log4j.Logger;
import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.ep.exception.Severity;
import uk.ac.ebi.ep.mm.MegaJdbcMapper;
import uk.ac.ebi.ep.mm.MegaMapper;

/**
 *this class provides the database resources needed by the subclass(es)
 * @author joseph
 */
public abstract class SiteMapResources<T> implements ISiteMap<T> {

    private final Logger LOGGER = Logger.getLogger(SiteMapResources.class);
    private MegaMapper megaMapper;
    private Connection connection;

    /**
     * 
     * @param dbConfig the database configuration filename
     */
    public SiteMapResources(String dbConfig) {
        try {
            initMegaMapper(dbConfig);
        } catch (SiteMapException ex) {
            LOGGER.fatal("MegaMapper cannot be initialized", ex);
        }



    }

    private void initMegaMapper(String dbConfig) throws SiteMapException {
        try {
            connection = getConnection(dbConfig);
            if (connection == null) {
                throw new SiteMapException(String.format("No Database connection due to invalid config %s", dbConfig), Severity.SYSTEM_AFFECTING);
            }
            megaMapper = new MegaJdbcMapper(connection);
            megaMapper.openMap();
        } catch (IOException ex) {
            LOGGER.fatal("IOException while creating the megaMapper", ex);
        }
    }

    public final Connection getConnection(String dbConfig) throws IOException {
        if (connection == null) {
            connection = OracleDatabaseInstance.getInstance(dbConfig).getConnection();
        }
        return connection;
    }

    public MegaMapper getMegaMapper() {
        return megaMapper;
    }

    /**any subclass of this class must implement this abstract method to provided
     * mechanism on how the generated siteMap will be exported
     * 
     * @param fileDirectory the file directory
     * @param filename the name of the file
     * @return an OutputStream
     * @throws FileNotFoundException if the file was not found
     * @throws IOException 
     */
    public abstract T exportFile(String fileDirectory, String filename) throws FileNotFoundException, IOException;

    /**
     * abstract method to generated siteMap from the specified database
     * @param fileLocation the directory where the generated siteMap will be saved
     * @param filename the filename of the generated siteMap
     * @throws SiteMapException if the siteMap cannot be generated.
     */
    public abstract void generateSitemap(String fileDirectory, String filename) throws SiteMapException;

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
}
