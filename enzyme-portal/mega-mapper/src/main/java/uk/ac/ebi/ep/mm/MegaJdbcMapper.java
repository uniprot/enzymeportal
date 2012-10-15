package uk.ac.ebi.ep.mm;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import uk.ac.ebi.biobabel.util.db.SQLLoader;

/**
 * Plain JDBC implementation of {@link MegaMapper}.
 *
 * @author rafa
 *
 */
public class MegaJdbcMapper implements MegaMapper {

    private final Logger LOGGER = Logger.getLogger(MegaJdbcMapper.class);
    private Connection con;
    private SQLLoader sqlLoader;

    public MegaJdbcMapper(Connection con) throws IOException {
        this.con = con;
        openMap();
    }

    /**
     * {@inheritDoc} <br> This implementation prepares the required statements.
     */
    public final void openMap() throws IOException {
        sqlLoader = new SQLLoader(this.getClass(), con);
    }

    public void writeEntry(Entry entry) throws IOException {
        try {
            if (existsInMegaMap(entry)) {
                return;
            }
            PreparedStatement wEntryStm = sqlLoader.getPreparedStatement(
                    "--insert.entry", new String[]{"id"}, (Object) null);
            int paramNum = 1;
//			wEntryStm.setInt(paramNum++, entry.getId());
            wEntryStm.setString(paramNum++, entry.getDbName());
            wEntryStm.setString(paramNum++, entry.getEntryId());
            if (entry.getEntryName() != null) {
                wEntryStm.setString(paramNum++, entry.getEntryName());
            } else {
                wEntryStm.setNull(paramNum++, Types.VARCHAR);
            }
            wEntryStm.execute();
            final ResultSet generatedKeys = wEntryStm.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                entry.setId(id);
//				Statement checkMe = generatedKeys.getStatement();
//				checkMe.close();
            } else {
                LOGGER.warn("No generated keys!");
            }
            generatedKeys.close();
            if (entry.getEntryAccessions() != null) {
                int index = 0;
                PreparedStatement wAccStm =
                        sqlLoader.getPreparedStatement("--insert.accession");
                for (String accession : entry.getEntryAccessions()) {
                    paramNum = 1;
                    wAccStm.setInt(paramNum++, entry.getId());
                    wAccStm.setString(paramNum++, accession);
                    wAccStm.setInt(paramNum++, index++);
                    wAccStm.execute();
                }
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    public void writeEntries(Collection<Entry> entries) throws IOException {
        if (entries != null) {
            for (Entry entry : entries) {
                writeEntry(entry);
            }
        }
    }

    /**
     * {@inheritDoc} <br> This implementation writes any (or both) of the two
     * linked entries in case they don't already exist in the database.
     */
    public void writeXref(XRef xref) throws IOException {
        try {
            if (existsInMegaMap(xref)) {
                return;
            }
            writeEntry(xref.getFromEntry());
            writeEntry(xref.getToEntry());
            PreparedStatement wXrefStm = sqlLoader.getPreparedStatement(
                    "--insert.xref", new String[]{"id"}, (Object) null);
            int paramNum = 1;
//			wXrefStm.setInt(paramNum++, xref.getId());
            wXrefStm.setInt(paramNum++, xref.getFromEntry().getId());
            wXrefStm.setString(paramNum++, xref.getRelationship());
            wXrefStm.setInt(paramNum++, xref.getToEntry().getId());
            wXrefStm.execute();
            final ResultSet generatedKeys = wXrefStm.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                xref.setId(id);
            } else {
                LOGGER.warn("No generated keys!");
            }
            generatedKeys.close();
        } catch (SQLException e) {
            LOGGER.error(xref, e);
            throw new IOException(e);
        }
    }

    public void writeXrefs(Collection<XRef> xrefs) throws IOException {
        if (xrefs != null) {
            for (XRef xref : xrefs) {
                writeXref(xref);
            }
        }
    }

    public void write(Collection<Entry> entries, Collection<XRef> xrefs)
            throws IOException {
        writeEntries(entries);
        writeXrefs(xrefs);
    }

    /**
     * Deletes one entry <i>and all the associated accessions and xrefs</i>.
     *
     * @param entry
     */
    public void deleteEntry(Entry entry) {
        try {
            PreparedStatement dXrefStm =
                    sqlLoader.getPreparedStatement("--delete.xrefs");
            //dXrefStm.setInt(1, entry.getId());
            // dXrefStm.setInt(2, entry.getId());
            dXrefStm.setString(1, entry.getEntryId());
            dXrefStm.setString(2, entry.getEntryId());
            dXrefStm.execute();
            PreparedStatement dAccStm =
                    sqlLoader.getPreparedStatement("--delete.accessions");
            dAccStm.setInt(1, entry.getId());
            dAccStm.execute();
            PreparedStatement dEntryStm =
                    sqlLoader.getPreparedStatement("--delete.entry");
            dEntryStm.setInt(1, entry.getId());
            dEntryStm.execute();
        } catch (SQLException e) {
            LOGGER.error(entry.getEntryId()
                    + " (" + entry.getDbName() + ")", e);
        }

    }

    /**
     * Checks if an entry already exists in the database. If so, the passed {@link Entry}
     * object is updated with the internal id.
     *
     * @param entry
     * @return
     * <code>true</code> if the entry exists.
     * @throws SQLException
     */
    private boolean existsInMegaMap(Entry entry) throws SQLException {
        
        PreparedStatement rEntryStm =
                sqlLoader.getPreparedStatement("--entry.by.entryid");
        int paramNum = 1;
        rEntryStm.setString(paramNum++, entry.getEntryId());
        rEntryStm.setString(paramNum++, entry.getDbName());
        final ResultSet rs = rEntryStm.executeQuery();
        final boolean exists = rs.next();
        if (exists) {
            entry.setId(rs.getInt("id"));
        }
        rs.close();
        return exists;
    }

    private boolean existsInMegaMap(XRef xref) throws SQLException {
        PreparedStatement rXrefStm =
                sqlLoader.getPreparedStatement("--xref.by.entryids");
        int paramNum = 1;
        rXrefStm.setString(paramNum++, xref.getFromEntry().getDbName());
        rXrefStm.setString(paramNum++, xref.getFromEntry().getEntryId());
        rXrefStm.setString(paramNum++, xref.getToEntry().getDbName());
        rXrefStm.setString(paramNum++, xref.getToEntry().getEntryId());
        final ResultSet rs = rXrefStm.executeQuery();
        final boolean exists = rs.next();
        if (exists) {
            final int xrefId = rs.getInt("id");
            LOGGER.warn("XRef already exists in the database: "
                    + xref.toString() + " as " + xrefId);
            xref.setId(xrefId);
        }
        rs.close();
        return exists;
    }

    /**
     * Converts an array of database objects into a comma-delimited list of
     * single-quoted database names
     *
     * @param dbs
     * @return
     */
    private String dbArrayForQuery(MmDatabase... dbs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dbs.length; i++) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append('\'').append(dbs[i].name()).append('\'');
        }
        return sb.toString();
    }

    private Entry getEntryById(int id) throws SQLException {
        Entry entry = null;
        PreparedStatement rEntryStm =
                sqlLoader.getPreparedStatement("--entry.by.id");
        rEntryStm.setInt(1, id);
        ResultSet rs = rEntryStm.executeQuery();
        if (rs.next()) {
            entry = new Entry();
            entry.setId(id);
            entry.setDbName(rs.getString("db_name"));
            entry.setEntryId(rs.getString("entry_id"));
            entry.setEntryName(rs.getString("entry_name"));
        }
        rs.close();
        return entry;
    }

    /**
     * Builds a list of XRef objects from a result set, and closes it.
     *
     * @param rs
     * @return a list of {@link XRef}s, or <code>null</code> if none found.
     * @throws SQLException
     */
    private List<XRef> buildXref(ResultSet rs) throws SQLException {
        List<XRef> xrefs = null;
        while (rs.next()) {
            if (xrefs == null) {
                xrefs = new ArrayList<XRef>();
            }
            XRef xref = new XRef();
            xref.setId(rs.getInt("id"));
            xref.setFromEntry(getEntryById(rs.getInt("from_entry")));
            xref.setToEntry(getEntryById(rs.getInt("to_entry")));
            xref.setRelationship(rs.getString("relationship"));
            xrefs.add(xref);
        }
        rs.close();
        return xrefs;
    }

    /**
     * {@inheritDoc} <br> Unlike the hibernate implementation, in case of
     * getting more than one result this will will be logged as a warning,
     * returning the first entry only.
     */
    public Entry getEntryForAccession(MmDatabase db, String accession) {
        Entry entry = null;
        ResultSet rs = null;
        try {
            PreparedStatement entryForAccessionStm =
                    sqlLoader.getPreparedStatement("--entry.by.accession");
            int paramNum = 1;
            entryForAccessionStm.setString(paramNum++, accession);
            entryForAccessionStm.setString(paramNum++, db.name());
            rs = entryForAccessionStm.executeQuery();
            if (rs.next()) {
                entry = new Entry();
                entry.setId(rs.getInt("id"));
                entry.setDbName(rs.getString("db_name"));
                entry.setEntryId(rs.getString("entry_id"));
                entry.setEntryName(rs.getString("entry_name"));
                // TODO: load accessions?
                if (rs.next()) {
                    LOGGER.error("More than one entry for same accession! "
                            + accession + " (" + db.name() + ")");
                }
            }
        } catch (SQLException e) {
            LOGGER.error(accession + " (" + db.name() + ")", e);
        } finally {
        	closeResultSet(rs);
        }
        return entry;
    }

    public Collection<XRef> getXrefs(Entry entry) {
        Collection<XRef> xrefs = null;
        try {
            if (entry.getId() == null) {
                // update with value from the database:
                existsInMegaMap(entry);
            }
            if (entry.getId() != null) {
                PreparedStatement allXrefsByEntryStm =
                        sqlLoader.getPreparedStatement("--xrefs.all.by.entry");
                allXrefsByEntryStm.setInt(1, entry.getId());
                allXrefsByEntryStm.setInt(2, entry.getId());
                ResultSet rs = allXrefsByEntryStm.executeQuery();
                xrefs = buildXref(rs);
            } else {
                LOGGER.warn(entry.toString() + " not known in the mega-map");
            }
        } catch (SQLException e) {
            LOGGER.error(entry.getId(), e);
        }
        return xrefs;
    }

    public Collection<XRef> getXrefs(Entry entry, MmDatabase... dbs) {
        Collection<XRef> xrefs = null;
        try {
            if (entry.getId() == null) {
                // update with value from the database:
                existsInMegaMap(entry);
            }
            if (entry.getId() != null) {
                PreparedStatement ps = sqlLoader.getPreparedStatement(
                        "--xrefs.by.entry", dbArrayForQuery(dbs));
                int paramNum = 1;
                ps.setInt(paramNum++, entry.getId());
                ps.setInt(paramNum++, entry.getId());
                ResultSet rs = ps.executeQuery();
                xrefs = buildXref(rs);
            } else {
                LOGGER.warn(entry.toString() + " not known in the mega-map");
            }
        } catch (SQLException e) {
            LOGGER.error(entry.getId(), e);
        }
        return xrefs;
    }

    public Collection<XRef> getXrefs(Collection<Entry> entries,
            MmDatabase... dbs) {
        // TODO check if a dedicated query is more performant!
        Collection<XRef> allXrefs = null;
        for (Entry entry : entries) {
            Collection<XRef> xrefs = getXrefs(entry, dbs);
            if (allXrefs == null) {
                allXrefs = xrefs;
            } else if (xrefs != null) {
                allXrefs.addAll(xrefs);
            }
        }
        return allXrefs;
    }

    public Collection<XRef> getXrefs(MmDatabase db, String accession) {
        Collection<XRef> xrefs = null;
        try {
            PreparedStatement allXrefsByAccStm =
                    sqlLoader.getPreparedStatement("--xrefs.all.by.accession");
            allXrefsByAccStm.setString(1, accession);
            allXrefsByAccStm.setString(2, db.name());
            xrefs = buildXref(allXrefsByAccStm.executeQuery());
        } catch (SQLException e) {
            LOGGER.error(accession + " (" + db.name() + ")", e);
        }
        return xrefs;
    }

    public Collection<XRef> getXrefs(MmDatabase db, String accession,
            MmDatabase... xDbs) {
        Collection<XRef> xrefs = null;
        try {
            PreparedStatement ps = sqlLoader.getPreparedStatement(
                    "--xrefs.by.accession", dbArrayForQuery(xDbs));
            ps.setString(1, accession);
            ps.setString(2, db.name());
            xrefs = buildXref(ps.executeQuery());
        } catch (SQLException e) {
            LOGGER.error(accession + " (" + xDbs + ")", e);
        }

        return xrefs;
    }

    public Collection<XRef> getXrefs(MmDatabase db, String accession,
			Relationship relationship) {
		Collection<XRef> xrefs = null;
        try {
            PreparedStatement ps = sqlLoader.getPreparedStatement(
                    "--xrefs.by.accession.and.relationship");
            ps.setString(1, accession);
            ps.setString(2, db.name());
            ps.setString(3, relationship.name());
            xrefs = buildXref(ps.executeQuery());
        } catch (SQLException e) {
            LOGGER.error(accession + " (" + relationship + ")", e);
        }
        return xrefs;
	}

	public Collection<XRef> getXrefs(MmDatabase db, String idFragment,
			Constraint constraint, Relationship rel) {
		Collection<XRef> xrefs = null;
		String idConst = null;
		switch (constraint) {
		case EQUALS:
			idConst = "--constraint.equals";
			break;
		case STARTS_WITH:
			idConst = "--constraint.like";
			idFragment = idFragment + "%";
			break;
		case CONTAINS:
			idConst = "--constraint.like";
			idFragment = "%" + idFragment + "%";
			break;
		case ENDS_WITH:
			idConst = "--constraint.like";
			idFragment = "%" + idFragment;
			break;
		}
		String dbConst = db == null? "" : "--constraint.db";
		String relConst = rel == null? "" : "--constraint.relationship";
		try {
			PreparedStatement ps = sqlLoader.getPreparedStatement(
					"--xrefs.by.id.fragment", idConst, dbConst, relConst);
			int n = 1;
			ps.setString(n++, idFragment);
			if (db != null) ps.setString(n++, db.name());
			ps.setString(n++, idFragment);
			if (db != null) ps.setString(n++, db.name());
			if (rel != null) ps.setString(n++, rel.name());
            xrefs = buildXref(ps.executeQuery());
		} catch (SQLException e) {
			LOGGER.error(db + " - " + idFragment + " - " + constraint
					+ " - " + rel, e);
		}
		return xrefs;
	}

	/**
     * retrieves a List of XRef with database name as ChEMBL.
     *
     * @param db database where the accession is found
     * @param accession the accession number
     * @param xDbs chEMBL database
     * @return a List of XRef with database name as chEMBL. Note: the maximum
     * number of XRef retrieved is 5.
     */
    public List<XRef> getChMBLXrefs(MmDatabase db, String accession,
            MmDatabase... xDbs) {
        List<XRef> xrefs = null;
        try {
            if (xrefs == null) {
                xrefs = new ArrayList<XRef>();
            }
            PreparedStatement ps = sqlLoader.getPreparedStatement(
                    "--xrefs.by.ChEMBL", dbArrayForQuery(xDbs));
            ps.setString(1, accession);
            ps.setString(2, db.name());
            xrefs = buildXref(ps.executeQuery());
        } catch (SQLException e) {
            LOGGER.error(accession + " (" + xDbs + ")", e);
        }

        return xrefs;
    }

    /**
     * retrieves the total number of Xrefs found for a given accession.
     *
     * @param db the database where the accession is found
     * @param accession the accession number
     * @param xDb the referencing/referenced database(s).
     * @return the total number of Xrefs found or 0 if none was found.
     */
    public int getXrefsSize(MmDatabase db, String accession,
            MmDatabase... xDbs) {
        int total = 0;
        ResultSet rs = null;
        try {
            PreparedStatement ps = sqlLoader.getPreparedStatement(
                    "--xrefs.by.accession.total", dbArrayForQuery(xDbs));
            ps.setString(1, accession);
            ps.setString(2, db.name());
            rs = ps.executeQuery();
            while (rs.next()) {
                total = rs.getInt("rowcount");
            }
        } catch (SQLException e) {
            LOGGER.error(accession + " (" + xDbs + ")", e);
        } finally {
        	closeResultSet(rs);
        }

        return total;
    }

    public void handleError() throws IOException {
        closeStatements();
    }

    /**
     * {@inheritDoc} <br> This implementation just closes the prepared
     * statements. Note that the connection is not closed, that is the client's
     * responsibility.
     */
    public void closeMap() throws IOException {
        closeStatements();
    }

    /**
     * @throws IOException
     */
    private void closeStatements() throws IOException {
        try {
            sqlLoader.close();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    public void commit() throws IOException {
        try {
            con.commit();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    public void rollback() throws IOException {
        try {
            con.rollback();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    /**
     * to retrieve all accessions for the given database
     *
     * @param database the database with accessions
     * @return list of accessions from a given database
     */
    public List<String> getAllUniProtAccessions(MmDatabase database) {
        List<String> accessionList = null;
        ResultSet resultSet = null;
        try {
            if (accessionList == null) {
                accessionList = new ArrayList<String>();
            }

            PreparedStatement preparedStatement = sqlLoader
            		.getPreparedStatement("--AllUniProtAccessions.accession");
            preparedStatement.setString(1, database.name());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String accession = resultSet.getString("ACCESSION");
                accessionList.add(accession);

            }
        } catch (SQLException ex) {
            LOGGER.fatal(String.format("Error retrieving accession from %s", database.name()), ex);
        } finally {
        	closeResultSet(resultSet);
        }

        return accessionList;
    }

    //get disease using list of accessions
     public Map<String, String> getDiseaseNew(MmDatabase db, String accession,
            MmDatabase... xDbs) {
        Map<String, String> diseasesEntryMap = null;
        ResultSet resultSet = null;
        PreparedStatement ps = null;
        //String[] acc = accessions.split("_");
        //String accession = acc[0].concat("_%");

        try {
            if (diseasesEntryMap == null) {
                diseasesEntryMap = new HashMap<String, String>();
            }

            String queryOld = "select DISTINCT e2.entry_id, e2.entry_name, e2.db_name from mm_entry e1,mm_xref xr, mm_entry e2 where e1.db_name =? and e1.entry_id like ? "
                    + "and ((e1.id = xr.from_entry and xr.to_entry = e2.id) or "
                    + "(e1.id = xr.to_entry and xr.from_entry = e2.id)) and e2.db_name in (?,?,?) and e2.entry_name is not null ";
            
            
            String query = "select DISTINCT e.db_name, e.entry_id, e.entry_name from mm_entry e, mm_accession a, mm_xref x "
                    + "where a.accession = ? and a.id = x.from_entry and x.to_entry = e.id and e.db_name in (?,?,?)";

            if (con != null) {
                ps = con.prepareStatement(query);


                ps.setString(1, accession);
                ps.setString(2, xDbs[0].name());
                ps.setString(3, xDbs[1].name());
                ps.setString(4, xDbs[2].name());


                resultSet = ps.executeQuery();

                while (resultSet.next()) {

                    String entryId = resultSet.getString("ENTRY_ID");
                    String entryName = resultSet.getString("ENTRY_NAME");
                    if (entryId != null && entryName != null) {
                        diseasesEntryMap.put(entryId, entryName);
                    }

                }
            }

        } catch (SQLException e) {
            LOGGER.error(accession + " (" + xDbs + ")", e);
        } finally {
        	closeResultSet(resultSet);
        	closePreparedStatement(ps);
        }

        return diseasesEntryMap;
    }

	/**
	 * Closes safely a ResultSet, logging in case of error.
	 * @param resultSet
	 */
	private void closeResultSet(ResultSet resultSet) {
		if (resultSet != null){
		    try {
				resultSet.close();
			} catch (SQLException e) {
				LOGGER.error("Unable to close ResultSet", e);
			}
		}
	}
	
	/**
	 * Closes safely a PreparedStatement, logging in case of error.
	 * @param ps
	 */
	private void closePreparedStatement(PreparedStatement ps){
		if (ps != null){
			try {
				ps.close();
			} catch (SQLException e){
				LOGGER.error("Unable to close PreparedStatement");
			}
		}
	}
     
     
         public Map<String, String> getCompoundsNew(MmDatabase db, String accession,
            MmDatabase... xDbs) {
      
        Map<String, String> compoundEntryMap = null;
 



        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
      
            if (compoundEntryMap == null) {
                compoundEntryMap = new HashMap<String, String>();
            }
            
            String query = "select DISTINCT e.db_name, e.entry_id, e.entry_name from mm_entry e, mm_accession a, mm_xref x"
                    + " where a.accession = ? and a.id = x.from_entry and x.to_entry = e.id and e.db_name = ? union "
                    + "select e.db_name, e.entry_id, e.entry_name from mm_entry e, mm_accession a, mm_xref x where"
                    + " a.accession = ? and a.id = x.to_entry and x.from_entry = e.id and e.db_name = ?";
                    
                    
            String queryOLD = "select DISTINCT e2.entry_id, e2.entry_name, e2.db_name from mm_entry e1, mm_xref xr, mm_entry e2 where e1.db_name = ? and e1.entry_id like ? and ((e1.id = xr.from_entry and xr.to_entry = e2.id) or (e1.id = xr.to_entry and xr.from_entry = e2.id))and e2.db_name in (?,?) and e2.entry_name is not null";
  
            if (con != null) {
                ps = con.prepareStatement(query);

                ps.setString(1, accession);
                ps.setString(3, accession);
                ps.setString(2, xDbs[0].name());
                ps.setString(4, xDbs[1].name());



                resultSet = ps.executeQuery();
                while (resultSet.next()) {

                    String entryId = resultSet.getString("ENTRY_ID");
                    String entryName = resultSet.getString("ENTRY_NAME");
                    if (entryId != null && entryName != null) {
                    
                        compoundEntryMap.put(entryId, entryName);
                    }

     
                }
            }

        } catch (SQLException e) {
            LOGGER.error(accession + " (" + xDbs + ")", e);
        } finally {
        	closeResultSet(resultSet);
        	closePreparedStatement(ps);
        }
        return compoundEntryMap;
    }

    //working version

    public Map<String, String> getDisease(MmDatabase db, String accessions,
            MmDatabase... xDbs) {
        Map<String, String> diseasesEntryMap = null;
        ResultSet resultSet = null;
        PreparedStatement ps = null;
        String[] acc = accessions.split("_");
        String accession = acc[0].concat("_%");

        try {
            if (diseasesEntryMap == null) {
                diseasesEntryMap = new HashMap<String, String>();
            }

            String query = "select DISTINCT e2.entry_id, e2.entry_name, e2.db_name from mm_entry e1,mm_xref xr, mm_entry e2 where e1.db_name =? and e1.entry_id like ? "
                    + "and ((e1.id = xr.from_entry and xr.to_entry = e2.id) or "
                    + "(e1.id = xr.to_entry and xr.from_entry = e2.id)) and e2.db_name in (?,?,?) and e2.entry_name is not null ";

            if (con != null) {
                ps = con.prepareStatement(query);


                ps.setString(1, db.name());
                ps.setString(2, accession);
                ps.setString(3, xDbs[0].name());
                ps.setString(4, xDbs[1].name());
                ps.setString(5, xDbs[2].name());




                resultSet = ps.executeQuery();

                while (resultSet.next()) {

                    String entryId = resultSet.getString("ENTRY_ID");
                    String entryName = resultSet.getString("ENTRY_NAME");
                    if (entryId != null && entryName != null) {
                        diseasesEntryMap.put(entryId, entryName);
                    }

                }
            }

        } catch (SQLException e) {
            LOGGER.error(accession + " (" + xDbs + ")", e);
        } finally {
        	closeResultSet(resultSet);
        	closePreparedStatement(ps);
        }

        return diseasesEntryMap;
    }

    private static final String ILLEGAL_COMPOUND = "water";
     public Map<String, String> getCompounds(MmDatabase db, String accessions,
            MmDatabase... xDbs) {
        //List<CompoundEntry> compoundList = null;
        Map<String, String> compoundEntryMap = null;
        String[] acc = accessions.split("_");
        String accession = acc[0].concat("_%");
        // String x = "some_example";
        // String  acc = accession.;
        // String [] s = x.split("_");
        // String y = s[0].concat("_%");


        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
       

            if (compoundEntryMap == null) {
                compoundEntryMap = new HashMap<String, String>();
            }
            String query = "select DISTINCT e2.entry_id, e2.entry_name, e2.db_name from mm_entry e1, mm_xref xr, mm_entry e2"
                    + " where e1.db_name = ? and e1.entry_id like ? and ((e1.id = xr.from_entry and xr.to_entry = e2.id) "
                    + "or (e1.id = xr.to_entry and xr.from_entry = e2.id))and e2.db_name in (?,?) and e2.entry_name is not null and e2.entry_name != ?";
            
            if (con != null) {
                ps = con.prepareStatement(query);

                ps.setString(2, accession);
                ps.setString(1, db.name());
                ps.setString(3, xDbs[0].name());
                ps.setString(4, xDbs[1].name());
                ps.setString(5, ILLEGAL_COMPOUND);



                resultSet = ps.executeQuery();
                while (resultSet.next()) {

                    String entryId = resultSet.getString("ENTRY_ID");
                    String entryName = resultSet.getString("ENTRY_NAME");
                    if (entryId != null && entryName != null) {
                        // System.out.println("data "+ entryName);
                        compoundEntryMap.put(entryId, entryName);
                    }

             
                }
            }

        } catch (SQLException e) {
            LOGGER.error(accession + " (" + xDbs + ")", e);
        } finally {
        	closeResultSet(resultSet);
        	closePreparedStatement(ps);
        }
        return compoundEntryMap;
    }

    /**
     * Builds a list of XRef objects from a result set.
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private List<XRef> buildCompound(ResultSet rs) throws SQLException {
        List<XRef> xrefs = null;
        while (rs.next()) {
            if (xrefs == null) {
                xrefs = new ArrayList<XRef>();
            }
            XRef xref = new XRef();
            xref.setId(rs.getInt("id"));
            xref.setFromEntry(getEntryById(rs.getInt("from_entry")));
            xref.setToEntry(getEntryById(rs.getInt("to_entry")));
            xref.setRelationship(rs.getString("relationship"));
            xrefs.add(xref);
        }
        rs.close();
        return xrefs;
    }
}
