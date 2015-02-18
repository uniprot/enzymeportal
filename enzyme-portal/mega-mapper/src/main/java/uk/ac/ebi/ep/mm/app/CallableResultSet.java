/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.mm.app;

import java.sql.ResultSet;
import java.util.concurrent.Callable;
import uk.ac.ebi.ep.mm.MmDatabase;

/**
 *
 * @author joseph
 */
public class CallableResultSet implements Callable<ResultSet> {
    
    private DatabaseResources databaseResources;
    private String query;
    private MmDatabase mmDatabase;

    public CallableResultSet(DatabaseResources databaseResources, String query, MmDatabase mmDatabase) {
        this.databaseResources = databaseResources;
        this.query = query;
        this.mmDatabase = mmDatabase;
    }

    public final MmDatabase getMmDatabase() {
        return mmDatabase;
    }

 

  

    public final String getQuery() {
        return query;
    }
    
    

    public ResultSet call() throws Exception {

                  
        ResultSet resultSet = databaseResources.getMegaMapper().getAllEntryIds(mmDatabase, query);
        return resultSet;

    }
    
  
    
}
