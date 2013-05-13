/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.mm.app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.chemblws.domain.Compound;
import uk.ac.ebi.chemblws.exception.ChemblServiceException;
import uk.ac.ebi.chemblws.exception.CompoundNotFoundException;
import uk.ac.ebi.chemblws.exception.InvalidCompoundIdentifierException;
import uk.ac.ebi.chemblws.restclient.ChemblRestClient;
import uk.ac.ebi.ep.mm.*;

/**
 *
 * @author joseph
 */
public class CompoundsChEMBL_Impl implements ICompoundsDAO {

    private final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(CompoundsChEMBL_Impl.class);
    private ChemblRestClient chemblRestClient;
    private DatabaseResources databaseResources;
    private String dbConfig;
    private MegaMapper mapper;

    //private ExecutorService executorService = Executors.newCachedThreadPool();
    public CompoundsChEMBL_Impl(String dbConfig) {
        databaseResources = new DatabaseResources(dbConfig);
        this.dbConfig = dbConfig;

        init();
    }

    private void init() {
        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        chemblRestClient = applicationContext.getBean(
                "chemblRestClient", ChemblRestClient.class);
        // FIXME:
        chemblRestClient.setChemblServiceUrl("http://www.ebi.ac.uk/chemblws/");
    }
  

    private Compound getCompoundById(String chemblId) {
       
        //Compound chemblCompound = chemblRestClient.getCompound(chemblId);
        Compound chemblCompound = null;


        try {
            chemblCompound = chemblRestClient.getCompound(chemblId);
//             System.out.println("returned comp from client "+ chemblCompound.getPreferredCompoundName());
        } catch (CompoundNotFoundException e) {
            LOGGER.fatal("CompoundNotFoundException thrown", e);

            //Do something
        } catch (ChemblServiceException e) {
            LOGGER.fatal("ChemblServiceException thrown", e);

            //Do something
        } catch (InvalidCompoundIdentifierException ex) {
            LOGGER.error("InvalidCompoundIdentifierException thrown", ex);

        }



        return chemblCompound;
    }

    private void computeAndUpdateEntry(Compound compound) throws Exception {
        if (compound != null) {
            Entry entry = new Entry();
            entry.setDbName(MmDatabase.ChEMBL.name());
            entry.setEntryId(compound.getChemblId());
            //TODO computation if there is no preferred name
            if (compound.getPreferredCompoundName() != null) {
                //System.out.println("pref comp "+ compound.getPreferredCompoundName());
                entry.setEntryName(compound.getPreferredCompoundName());
                this.updateEntry(entry);
            }
 
        }

    }

    public void updateChemblCompounds() throws IOException, InterruptedException, ExecutionException, SQLException {
         Connection con = OracleDatabaseInstance.getInstance(dbConfig).getConnection();
 
        MegaMapper    mapper = new MegaJdbcMapper(con);
            mapper.openMap();
        
        
        //MegaMapper mapper = databaseResources.getMegaMapper();
      
        List<String> chemblIds = mapper.getAllEntryIds(MmDatabase.ChEMBL);
        Compound chembl_compound;
  
        // if (chemblIds.size() >= 5000) {
        for (String chemblId : chemblIds) {

            chembl_compound = getCompoundById(chemblId);
            try {

                computeAndUpdateEntry(chembl_compound);
            } catch (Exception ex) {
                LOGGER.error( ex);
            }

        
                
        }
        
        

    }
    
    


    public void writeEntriesAndXrefs(Collection<Entry> entries, Collection<XRef> xRefs) throws IOException {
        try {
            this.writeEntries(entries);
            this.writeXrefs(xRefs);
        } catch (IOException ex) {
            LOGGER.fatal("Error while writing ChEMBL Compounds to Mega Mapper", ex);
        }
    }

    public void writeEntry(Entry entry) throws IOException {
        MegaMapper mapper = databaseResources.getMegaMapper();
        //System.out.println("mapper on write entry "+ mapper);
        mapper.writeEntry(entry);
        //System.out.println("done writing");
    }

    public void writeEntries(Collection<Entry> entries) throws IOException {
        MegaMapper mapper = databaseResources.getMegaMapper();
        mapper.writeEntries(entries);
    }

    public void writeXref(XRef ref) throws IOException {
        MegaMapper mapper = databaseResources.getMegaMapper();
        mapper.writeXref(ref);
    }

    public void writeXrefs(Collection<XRef> xRefs) throws IOException {
        MegaMapper mapper = databaseResources.getMegaMapper();
        mapper.writeXrefs(xRefs);
    }

    public void buildCompound() {
        Connection con = null;
        try {
            con = OracleDatabaseInstance.getInstance(dbConfig).getConnection();
            con.setAutoCommit(false);
            mapper = new MegaJdbcMapper(con);
            mapper.openMap();
            updateChemblCompounds();
            con.commit();
        } catch (Exception ex) {
             LOGGER.fatal("ERROR while building ChEMBL compounds", ex);
             if (con != null) try {
                 con.rollback();
             } catch (SQLException e) {
                LOGGER.error("Unable to roll connection back", e);
             }
        } finally {
            if (mapper != null) try {
                mapper.closeMap();
            } catch (IOException e) {
                LOGGER.error("Unable to close mega-map", e);
            }
            if (con != null) try {
                con.close();
            } catch (SQLException e) {
                LOGGER.error("Unable to close connection", e);
            }
        }
    }

    private void updateEntry(Entry entry) throws Exception {
        mapper.updateEntry(entry);
        //int num_row_affected = mapper.updateEntry(entry);
        //System.out.println("number of rows affected " + num_row_affected);
        //LOGGER.info("Number of rows affected during an update operation = " + num_row_affected);
    }
}
