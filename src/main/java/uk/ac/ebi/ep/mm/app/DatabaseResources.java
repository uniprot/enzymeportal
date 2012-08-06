/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.mm.app;

import java.io.IOException;
import java.sql.Connection;
import java.util.Collection;
import org.apache.log4j.Logger;
import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.ep.mm.Entry;
import uk.ac.ebi.ep.mm.MegaJdbcMapper;
import uk.ac.ebi.ep.mm.MegaMapper;
import uk.ac.ebi.ep.mm.XRef;

/**
 *
 * @author joseph
 */
public abstract class DatabaseResources {

    private final Logger LOGGER = Logger.getLogger(DatabaseResources.class);
    private MegaMapper megaMapper;
    private Connection connection;

    /**
     *
     * @param dbConfig the database configuration filename
     */
    public DatabaseResources(String dbConfig) {
        try {
            initMegaMapper(dbConfig);
        } catch (Exception ex) {
            LOGGER.fatal("MegaMapper cannot be initialized", ex);
        }



    }

    private void initMegaMapper(String dbConfig) {
        try {
            connection = getConnection(dbConfig);
            if (connection == null) {
                //throw new SiteMapException(String.format("No Database connection due to invalid config %s", dbConfig), Severity.SYSTEM_AFFECTING);
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

    public void writeEntry(Entry entry) throws IOException{
        MegaMapper mapper = this.getMegaMapper();
        mapper.writeEntry(entry);
    }
    public void writeEntries(Collection<Entry> entries) throws IOException {
        MegaMapper mapper = this.getMegaMapper();
        mapper.writeEntries(entries);
    }
    
    public void writeXref(XRef ref) throws IOException{
        MegaMapper mapper = this.getMegaMapper();
        mapper.writeXref(ref);
    }

    public void writeXrefs(Collection<XRef> xRefs) throws IOException {
        MegaMapper mapper = this.getMegaMapper();
        mapper.writeXrefs(xRefs);
    }

    abstract void writeChebiCompounds(Collection<Entry> entries, Collection<XRef> xRefs) throws IOException;
}
