/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.mm.app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.ep.mm.MegaJdbcMapper;
import uk.ac.ebi.ep.mm.MegaMapper;

/**
 *
 * @author joseph
 */
public class DatabaseResources {

    private final Logger LOGGER = Logger.getLogger(DatabaseResources.class);
    private MegaMapper megaMapper;
    private Connection connection;
    private int threadPoolSize = 64;
    //private ExecutorService executorService =  Executors.newFixedThreadPool(threadPoolSize);

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

//    public ExecutorService getExecutorService() {
//        return executorService;
//    }


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
                
                LOGGER.fatal("No Database connection due to invalid config : "+ dbConfig);
               
            }
            megaMapper = new MegaJdbcMapper(connection);
            megaMapper.openMap();
        } catch (Exception ex) {
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
    
//       public Future<?> executeQuery(String query) {
//                return this.executorService.submit(new QueryRequest(this, query));
//        } 
        
//        public Future<ResultSet> executeQueryResult(String query, MmDatabase mmDatabase) {
//                return this.executorService.submit(new CallableResultSet(this, query,mmDatabase));
//        }

    }
