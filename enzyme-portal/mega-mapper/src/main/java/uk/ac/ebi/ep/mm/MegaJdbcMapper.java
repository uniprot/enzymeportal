package uk.ac.ebi.ep.mm;

import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.util.*;

import java.util.logging.Level;

import org.apache.log4j.Logger;

import uk.ac.ebi.biobabel.util.collections.ChemicalNameComparator;
import uk.ac.ebi.biobabel.util.db.SQLLoader;
import uk.ac.ebi.ep.search.model.Compound;
import uk.ac.ebi.ep.search.model.Disease;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

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
    private static String ENTRY_ID = "ENTRY_ID";
    private static String ENTRY_NAME = "ENTRY_NAME";
    private static String DB_NAME = "DB_NAME";
    public static final String[] BLACKLISTED_COMPOUNDS = {"ACID", "acid", "H(2)O", "H(+)", "ACID", "WATER", "water", "ion", "ION"};

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
     * Checks if an entry already exists in the database. If so, the passed
     * {@link Entry} object is updated with the internal id. Additionally, if
     * the passed entry has got a name but the one stored in the mega-map does
     * not, the latter is updated.
     *
     * @param entry the entry whose existance is checked.
     * @return <code>true</code> if the entry exists.
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
            // Check whether the stored name is the same:
            String nameInDb = rs.getString("entry_name");
            if (nameInDb == null && entry.getEntryName() != null) {
                updateEntry(entry);
                LOGGER.info(MessageFormat.format(
                        "Added name {0} to {1} entry {2}",
                        entry.getEntryName(), entry.getDbName(),
                        entry.getEntryId()));
            } else if (nameInDb != null && entry.getEntryName() != null
                    && !nameInDb.equals(entry.getEntryName())) {
                LOGGER.warn(MessageFormat.format(
                        "{0} entry {1} exists with a name {2} != {3}",
                        entry.getDbName(), entry.getEntryId(), nameInDb,
                        entry.getEntryName()));
            }
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
            //LOGGER.warn("XRef already exists in the database: "
            // + xref.toString() + " as " + xrefId);
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
                // This happens with UniProt demerged entries:
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

    /**
     * ======= /** {@inheritDoc} <br>Please note that this implementation uses
     * backslash (\) as escape character for any underscore (_) present in the
     * idFragment, so that it is taken as part of the ID instead of an oracle
     * wildcard.
     */
    public Collection<XRef> getXrefs(MmDatabase db, String idFragment,
            Constraint constraint, Relationship rel) {
        Collection<XRef> xrefs = null;
        String idConst = getSqlConstraint(constraint);
        idFragment = getIdParameter(constraint, idFragment);
        String dbConst = db == null ? "" : "--constraint.db";
        String relConst = rel == null ? "" : "--constraint.relationship";
        try {
            PreparedStatement ps = sqlLoader.getPreparedStatement(
                    "--xrefs.by.id.fragment", idConst, dbConst, relConst);
            int n = 1;
            ps.setString(n++, idFragment);
            if (db != null) {
                ps.setString(n++, db.name());
            }
            ps.setString(n++, idFragment);
            if (db != null) {
                ps.setString(n++, db.name());
            }
            if (rel != null) {
                ps.setString(n++, rel.name());
            }
            xrefs = buildXref(ps.executeQuery());
        } catch (SQLException e) {
            LOGGER.error(db + " - " + idFragment + " - " + constraint
                    + " - " + rel, e);
        }
        return xrefs;
    }

    public Collection<XRef> getXrefs(MmDatabase db, String idFragment,
            Constraint constraint, MmDatabase... xDbs) {
        Collection<XRef> xrefs = null;
        String idConst = getSqlConstraint(constraint);
        idFragment = getIdParameter(constraint, idFragment);
        String dbConst = db == null ? "" : "--constraint.db";
        String inClause = dbArrayForQuery(xDbs);
        try {
            PreparedStatement ps = sqlLoader.getPreparedStatement(
                    "--xrefs.by.id.fragment.and.db",
                    idConst, dbConst, inClause);
            int n = 1;
            ps.setString(n++, idFragment);
            if (db != null) {
                ps.setString(n++, db.name());
            }
            xrefs = buildXref(ps.executeQuery());
        } catch (SQLException e) {
            LOGGER.error(db + " - " + idFragment + " - " + constraint
                    + " - " + xDbs, e);
        }
        return xrefs;
    }

    /**
     * Converts an enumerated value into a SQLLoader constraint key.
     *
     * @param constraint the constraint to apply.
     * @return the key to be used by the SQLLoader.
     */
    private String getSqlConstraint(Constraint constraint) {
        switch (constraint) {
            case EQUALS:
                return "--constraint.equals";
            case STARTS_WITH:
            case CONTAINS:
            case ENDS_WITH:
                return "--constraint.like";
            default:
                throw new IllegalArgumentException("constraint not supported: "
                        + constraint);
        }
    }

    /**
     * Builds the appropriate oracle query string for the constraint.
     *
     * @param constraint a constraint.
     * @param idFragment a fragment of the string to match.
     * @return a string with the wildcard % in the appropriate place(s).
     */
    private String getIdParameter(Constraint constraint, String idFragment) {
        switch (constraint) {
            case EQUALS:
                return idFragment;
            case STARTS_WITH:
                return idFragment.replace("_", "\\_") + "%";
            case CONTAINS:
                return "%" + idFragment.replace("_", "\\_") + "%";
            case ENDS_WITH:
                return "%" + idFragment.replace("_", "\\_");
            default:
                throw new IllegalArgumentException("constraint not supported: "
                        + constraint);
        }
    }

    /**
     * >>>>>>> 5a58a8dbc73ad57eac346d1d217ac661f41fc064 retrieves a List of XRef
     * with database name as ChEMBL.
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
     * >>>>>>> 5a58a8dbc73ad57eac346d1d217ac661f41fc064 retrieves a List of XRef
     * with database name as ChEMBL.
     *
     * @param db database where the accession is found
     * @param accession the accession number
     * @param xDbs chEMBL database
     * @return a List of XRef with database name as chEMBL. Note: the maximum
     * number of XRef retrieved is 5.
     */
    @Deprecated
    public List<Entry> getChMBLEntries(MmDatabase db, String accession,
            MmDatabase... xDbs) {
        List<Entry> entries = null;
        try {
            if (entries == null) {
                entries = new ArrayList<Entry>();
            }
            String query = "SELECT mme2.* FROM mm_accession mma, mm_entry mme1, mm_entry mme2, mm_xref mmx\n"
                    + "WHERE mma.accession = ? AND mma.id = mme1.id AND mme1.db_name = ? \n"
                    + "AND ((mme1.id = mmx.from_entry AND mmx.to_entry = mme2.id AND mme2.db_name IN (?) \n"
                    + "and mme2.entry_name is not null)) and rownum <= 5 order by mme2.entry_name asc";
//            PreparedStatement ps = sqlLoader.getPreparedStatement(
//                    "--entries.by.ChEMBL", dbArrayForQuery(xDbs));

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, accession);
            ps.setString(2, db.name());
            ps.setString(3, xDbs[0].name());

            entries = buildChemblEntry(ps.executeQuery());
        } catch (SQLException e) {
            LOGGER.error(accession + " (" + xDbs + ")", e);
        }

        return entries;
    }

    private List<Entry> buildChemblEntry(ResultSet rs) throws SQLException {
        List<Entry> entries = null;
        while (rs.next()) {
            if (entries == null) {
                entries = new ArrayList<Entry>();
            }

            String entryId = rs.getString(ENTRY_ID);
            String entryName = rs.getString(ENTRY_NAME);
            String dbName = rs.getString(DB_NAME);


            if (entryId != null && entryName != null) {
                Entry entry = new Entry();
                entry.setDbName(dbName);
                entry.setEntryId(entryId);
                entry.setEntryName(entryName);

                entries.add(entry);


            }

        }
        rs.close();
        return entries;
    }

    /**
     * retrieves the total number of Xrefs found for a given accession.
     *
     * @param db the database where the accession is found
     * @param accession the accession number
     * @param xDbs the referencing/referenced database(s).
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

            PreparedStatement preparedStatement = sqlLoader.getPreparedStatement("--AllUniProtAccessions.accession");
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
    public Map<String, String> getDiseaseByAccession(MmDatabase db, String accession,
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

//            String queryOld = "select DISTINCT e2.entry_id, e2.entry_name, e2.db_name from mm_entry e1,mm_xref xr, mm_entry e2 where e1.db_name =? and e1.entry_id like ? "
//                    + "and ((e1.id = xr.from_entry and xr.to_entry = e2.id) or "
//                    + "(e1.id = xr.to_entry and xr.from_entry = e2.id)) and e2.db_name in (?,?,?) and e2.entry_name is not null ";


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

                    String entryId = resultSet.getString(ENTRY_ID);
                    String entryName = resultSet.getString(ENTRY_NAME);
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
     *
     * @param resultSet a ResultSet. Can be <code>null</code>.
     */
    private void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.error("Unable to close ResultSet", e);
            }
        }
    }

    /**
     * Closes safely a PreparedStatement, logging in case of error.
     *
     * @param ps a PreparedStatement. Can be <code>null</code>.
     */
    private void closePreparedStatement(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                LOGGER.error("Unable to close PreparedStatement");
            }
        }
    }

    @Deprecated
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


            //String queryOLD = "select DISTINCT e2.entry_id, e2.entry_name, e2.db_name from mm_entry e1, mm_xref xr, mm_entry e2 where e1.db_name = ? and e1.entry_id like ? and ((e1.id = xr.from_entry and xr.to_entry = e2.id) or (e1.id = xr.to_entry and xr.from_entry = e2.id))and e2.db_name in (?,?) and e2.entry_name is not null";

            if (con != null) {
                ps = con.prepareStatement(query);

                ps.setString(1, accession);
                ps.setString(3, accession);
                ps.setString(2, xDbs[0].name());
                ps.setString(4, xDbs[1].name());



                resultSet = ps.executeQuery();
                while (resultSet.next()) {

                    String entryId = resultSet.getString(ENTRY_ID);
                    String entryName = resultSet.getString(ENTRY_NAME);
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
    public Collection<Disease> getDiseaseByUniprotId(MmDatabase db, String accessions,
            MmDatabase... xDbs) {
        Collection<Disease> diseases_collection = null;
        ResultSet resultSet = null;
        PreparedStatement ps = null;
        String[] acc = accessions.split("_");
        String accession = acc[0].concat("\\_%");

        try {
            if (diseases_collection == null) {
                diseases_collection = new HashSet<Disease>();
            }

            String query = "select DISTINCT e2.entry_id, e2.entry_name, e2.db_name from mm_entry e1,mm_xref xr, mm_entry e2 where e1.db_name =? and e1.entry_id like ? escape '\\' "
                    + "and ((e1.id = xr.from_entry and xr.to_entry = e2.id) or "
                    + "(e1.id = xr.to_entry and xr.from_entry = e2.id)) and e2.db_name in (?,?,?) and e2.entry_name is not null ";

            if (con != null) {
                ps = con.prepareStatement(query);


                ps.setString(1, db.name());
                ps.setString(2, accessions);
                ps.setString(3, xDbs[0].name());
                ps.setString(4, xDbs[1].name());
                ps.setString(5, xDbs[2].name());




                resultSet = ps.executeQuery();

                while (resultSet.next()) {

                    String disaseID = resultSet.getString(ENTRY_ID);

                    String url = contructUrlFromDiseaseId(disaseID);

                    Disease disease = new Disease();

                    //set the url or web link for this compound
                    if (url != null) {
                        disease.setUrl(url);
                    } else {
                        disease.setUrl("#");
                    }



                    String entryId = resultSet.getString(ENTRY_ID);
                    String entryName = resultSet.getString(ENTRY_NAME);

                    if (entryId != null && entryName != null) {
                        disease.setId(entryId);
                        String diseaseName = resolveSpecialCharacters(entryName.toLowerCase(Locale.ENGLISH));
                        disease.setName(diseaseName.replaceAll(",", ""));

                        diseases_collection.add(disease);
                    }

                }
            }

        } catch (SQLException e) {
            LOGGER.error(accession + " (" + xDbs + ")", e);
        } finally {
            closeResultSet(resultSet);
            closePreparedStatement(ps);
        }

        return diseases_collection;
    }

    private String resolveSpecialCharacters(String data) {

        SpecialCharacters xchars = SpecialCharacters.getInstance(null);
        EncodingType[] encodings = {
            EncodingType.CHEBI_CODE,
            EncodingType.COMPOSED,
            EncodingType.EXTENDED_HTML,
            EncodingType.GIF,
            EncodingType.HTML,
            EncodingType.HTML_CODE,
            EncodingType.JPG,
            EncodingType.SWISSPROT_CODE,
            EncodingType.UNICODE
        };

        if (!xchars.validate(data)) {
            LOGGER.warn("SPECIAL CHARACTER PARSING ERROR : This is not a valid xchars string!" + data);

        }


        return xchars.xml2Display(data, EncodingType.CHEBI_CODE);
    }

    public Collection<Compound> getCompounds(String uniprotId) {
        Collection<Compound> compounds = null;
        ResultSet rs = null;
        String query = null, constraint = null;
        if (uniprotId.endsWith("_")) {
            query = uniprotId.replace("_", "\\_%");
            constraint = "--constraint.like";
        } else {
            query = uniprotId;
            constraint = "--constraint.equals";
        }

        //blackListed compounds
        StringBuilder queryClauseBuilder = new StringBuilder();
        for (int i = 2; i < BLACKLISTED_COMPOUNDS.length; i++) {

            queryClauseBuilder.append("?");
            if (i < BLACKLISTED_COMPOUNDS.length - 1) {
                queryClauseBuilder.append(",");
            }
        }

        //this query {--compounds.by.uniprot.id} is eqv to the queryString below
        // String queryString = "select compound_name, compound_id, relationship from uniprot2compound "
        //  + "where uniprot_id "+ constraint+" and compound_name is not null and compound_name NOT IN ("+queryClauseBuilder+") order by relationship";


        try {
            PreparedStatement ps = sqlLoader.getPreparedStatement(
                    "--compounds.by.uniprot.id", constraint, queryClauseBuilder);
            ps.setString(1, query);

            for (int i = 2; i < BLACKLISTED_COMPOUNDS.length; i++) {
                ps.setString(i, BLACKLISTED_COMPOUNDS[i]);
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                String compoundID = rs.getString("compound_id");
                String url = contructCompoundUrl(compoundID);

                Compound compound = new Compound();

                //set the url or web link for this compound
                if (url != null) {
                    compound.setUrl(url);
                } else {
                    compound.setUrl("#");
                }

                compound.setId(rs.getString("compound_id"));


                // compound.setName(rs.getString("compound_name").toLowerCase(Locale.ENGLISH));
                //TODO fix strange characters in compounds before populating the main compound list
                //compound.setName(resolveSpecialCharacters(rs.getString("compound_name").toLowerCase(Locale.ENGLISH).replace(",", "")));
                //compound.setName(resolveSpecialCharacters(rs.getString("compound_name").toLowerCase(Locale.ENGLISH)));
                compound.setName(rs.getString("compound_name"));
                switch (Relationship.valueOf(rs.getString("relationship"))) {
                    case is_reactant_or_product_of:
                    case is_substrate_or_product_of:
                    case is_reactant_of:
                    case is_substrate_of:
                    case is_product_of:
                        compound.setRole(Compound.Role.SUBSTRATE_OR_PRODUCT);
                        break;
                    case is_cofactor_of:
                        compound.setRole(Compound.Role.COFACTOR);
                        break;
                    case is_activator_of:
                        compound.setRole(Compound.Role.ACTIVATOR);
                        break;
                    case is_inhibitor_of:
                        compound.setRole(Compound.Role.INHIBITOR);
                        break;
                    case is_drug_for:
                    case is_target_of:
                        compound.setRole(compound.getId().startsWith("CHEMBL")
                                ? Compound.Role.BIOACTIVE : Compound.Role.DRUG);
                        break;
                }
                if (compounds == null) {
                    compounds = new HashSet<Compound>();
                }

                compounds.add(compound);
            }
        } catch (SQLException e) {
            LOGGER.error("Unable to get compounds for " + uniprotId, e);
        } finally {
            closeResultSet(rs);
        }
        return compounds;
    }

    //we had to use different methods for contructing urls for Disease and compound becuase some ID's are similar eg. Drug Bank is alpha numeric and starts with letter D and same for Dieases from MESH
    private String contructCompoundUrl(String id) {
        String url = null;
        if (id.startsWith("CHEMBL")) {
            url = "http://www.ebi.ac.uk/chembldb/compound/inspect/" + id;
        }
        if (id.startsWith("DB")) {
            url = "http://www.drugbank.ca/drugs/" + id;
        }

        if (id.startsWith("CHEBI")) {
            url = "http://www.ebi.ac.uk/chebi/searchId.do?chebiId=" + id;
        }

        return url;
    }

    private String contructUrlFromDiseaseId(String id) {
        String url = null;


        if (id.startsWith("EFO")) {
            url = "http://www.ebi.ac.uk/efo/" + id;
        }
        if (isAlphaNumeric(id) && id.startsWith("D")) {
            url = "http://purl.bioontology.org/ontology/MESH/" + id;
        }

        if (isNumeric(id)) {
            url = "http://purl.bioontology.org/ontology/OMIM/" + id;

        }
        return url;
    }

    public static boolean isNumeric(String str) {
        DecimalFormatSymbols currentLocaleSymbols = DecimalFormatSymbols.getInstance();
        char localeMinusSign = currentLocaleSymbols.getMinusSign();

        if (!Character.isDigit(str.charAt(0)) && str.charAt(0) != localeMinusSign) {
            return false;
        }

        boolean isDecimalSeparatorFound = false;
        char localeDecimalSeparator = currentLocaleSymbols.getDecimalSeparator();

        for (char c : str.substring(1).toCharArray()) {
            if (!Character.isDigit(c)) {
                if (c == localeDecimalSeparator && !isDecimalSeparatorFound) {
                    isDecimalSeparatorFound = true;
                    continue;
                }
                return false;
            }
        }
        return true;
    }

    private boolean isAlphaNumeric(String s) {
        String pattern = "^[a-zA-Z0-9]*$";
        if (s.matches(pattern)) {
            return true;
        }
        return false;
    }

    @Deprecated
    public Map<String, String> getCompounds(MmDatabase db, String uniprotId,
            MmDatabase... xDbs) {


        Map<String, String> compoundEntryMap = null;
        String[] acc = uniprotId.split("_");
        String accession = acc[0].concat("\\_%");


        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {


            StringBuilder queryClauseBuilder = new StringBuilder();

//            boolean firstValue = true;
//            for (int i = 5; i < BLACKLISTED_COMPOUNDS.length; i++) {
//                //queryClauseBuilder.append('?');
//                if (firstValue) {
//                   
//                    firstValue = false;
//                } else {
//                    queryClauseBuilder.append(',');
//                }
//                queryClauseBuilder.append('?');
//            }

            for (int i = 5; i < BLACKLISTED_COMPOUNDS.length; i++) {
                //queryClauseBuilder.append(BLACKLISTED_COMPOUNDS[i]);
                queryClauseBuilder.append("?");
                if (i < BLACKLISTED_COMPOUNDS.length - 1) {
                    queryClauseBuilder.append(",");
                }
            }

            if (compoundEntryMap == null) {
                compoundEntryMap = new HashMap<String, String>();
            }



            String query = "select DISTINCT e2.entry_id, e2.entry_name, e2.db_name from mm_entry e1, mm_xref xr, mm_entry e2"
                    + " where e1.db_name = ? and e1.entry_id like ? escape '\\' and ((e1.id = xr.from_entry and xr.to_entry = e2.id) "
                    + "or (e1.id = xr.to_entry and xr.from_entry = e2.id))and e2.db_name in (?,?) and e2.entry_name is not null and e2.entry_name not in (" + queryClauseBuilder.toString() + ')';

            if (con != null) {
                ps = con.prepareStatement(query);

                ps.setString(2, accession);
                ps.setString(1, db.name());
                ps.setString(3, xDbs[0].name());
                ps.setString(4, xDbs[1].name());//ignore ChEBI
                //ps.setString(5, BLACKLISTED_COMPOUNDS);


                for (int i = 5; i < BLACKLISTED_COMPOUNDS.length; i++) {
                    ps.setString(i, BLACKLISTED_COMPOUNDS[i]);

                }



                resultSet = ps.executeQuery();

                while (resultSet.next()) {

                    String entryId = resultSet.getString(ENTRY_ID);
                    String entryName = resultSet.getString(ENTRY_NAME);
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

    /**
     * Updates an entry name in the database, leaving the rest unchanged.
     *
     * @param entry the entry with an updated name.
     */
    public int updateEntry(Entry entry) {
        int n = 0;
        try {
            PreparedStatement ps =
                    sqlLoader.getPreparedStatement("--update.entry");
            ps.setString(1, entry.getEntryName());
            ps.setString(2, entry.getEntryId());
            ps.setString(3, entry.getDbName());
            n = ps.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.error("Updating " + entry.getEntryId(), ex);
        }
        return n;
    }

    private static int processUpdateCounts(int[] updateCounts) {
        int num_rows_affected = 0;
        for (int i = 0; i < updateCounts.length; i++) {
            if (updateCounts[i] >= 0) {
                // Successfully executed; the number represents number of affected rows
                num_rows_affected = updateCounts[i];
            } else if (updateCounts[i] == Statement.SUCCESS_NO_INFO) {
                // Successfully executed; number of affected rows not available
                num_rows_affected = updateCounts[i];
            } else if (updateCounts[i] == Statement.EXECUTE_FAILED) {
                // Failed to execute

                num_rows_affected = updateCounts[i];
            }
        }
        return num_rows_affected;
    }

    /**
     *
     * @param database the database to get the entry id's from
     * @return all entry ID's
     */
    public List<String> getAllEntryIds(MmDatabase database) {
        List<String> entryIdList = null;
        ResultSet resultSet = null;
        if (entryIdList == null) {
            entryIdList = new LinkedList<String>();
        }
        try {
            String query = "SELECT entry.* FROM MM_ENTRY entry WHERE entry.DB_NAME = ? AND entry.ENTRY_NAME IS NULL";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            //PreparedStatement preparedStatement = sqlLoader.getPreparedStatement("--allEntry.by.dbName");
            //and rownum <= 700000
            preparedStatement.setString(1, database.name());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String accession = resultSet.getString(ENTRY_ID);

                entryIdList.add(accession);



            }

        } catch (SQLException ex) {
            LOGGER.error(" (" + resultSet + ")", ex);
        } finally {
            closeResultSet(resultSet);
        }

        return entryIdList;


    }

    /**
     *
     * @param database the database to get the entry id's from
     * @return resultSet
     */
    public ResultSet getAllEntryIds(MmDatabase database, String query) {

        ResultSet resultSet = null;

        try {
            //String query = "SELECT entry.* FROM MM_ENTRY entry WHERE entry.DB_NAME = ? and rownum <= 700000";
            PreparedStatement preparedStatement = con.prepareStatement(query);

            preparedStatement.setString(1, database.name());
            resultSet = preparedStatement.executeQuery();


        } catch (SQLException ex) {
            LOGGER.error(" (" + resultSet + ")", ex);
            //java.util.logging.Logger.getLogger(MegaJdbcMapper.class.getName()).log(Level.SEVERE, null, ex);
        }


        return resultSet;


    }
    private static final Comparator<String> NAME_COMPARATOR =
            new ChemicalNameComparator();
    static final Comparator<uk.ac.ebi.ep.search.model.Disease> SORT_DISEASES = new Comparator<uk.ac.ebi.ep.search.model.Disease>() {
        public int compare(uk.ac.ebi.ep.search.model.Disease d1, uk.ac.ebi.ep.search.model.Disease d2) {

            if (d1.getName() == null && d2.getName() == null) {

                //return sp1.getScientificname().compareTo(sp2.getScientificname())
                return NAME_COMPARATOR.compare(d1.getName(), d2.getName());
            }
            int compare = NAME_COMPARATOR.compare(d1.getName(), d2.getName());

            return ((compare == 0) ? NAME_COMPARATOR.compare(d1.getName(), d2.getName()) : compare);

        }
    };

    public Set<Disease> findAllDiseases(MmDatabase uniprotDB,
            MmDatabase... xDbs) {
        Set<Disease> diseases = null;
        ResultSet resultSet = null;
        PreparedStatement ps = null;
        //String[] acc = accessions.split("_");
        // String accession = acc[0].concat("\\_%");

        try {
            if (diseases == null) {
                diseases = new TreeSet<Disease>(SORT_DISEASES);
            }



            //String test = "select ENTRY_ID,ENTRY_NAME from MM_ENTRY where DB_NAME in ('EFO','OMIM','MeSH') and ENTRY_NAME is not null order by ENTRY_NAME";

            String query = "select DISTINCT e2.entry_id, e2.entry_name,e2.db_name, count(e1.ENTRY_ID) as numEnzyme from mm_entry e1,mm_xref xr,"
                    + " mm_entry e2 where e1.db_name = ? and e1.entry_id is not null\n"
                    + "                    and ((e1.id = xr.from_entry and xr.to_entry = e2.id) or \n"
                    + "(e1.id = xr.to_entry and xr.from_entry = e2.id)) and e2.db_name in (?,?,?) "
                    + "and e2.entry_name is not null  group by e2.entry_id, e2.entry_name, e2.DB_NAME\n"
                    + "";

            if (con != null) {
                ps = con.prepareStatement(query);



                ps.setString(1, uniprotDB.name());
                ps.setString(2, xDbs[0].name());
                ps.setString(3, xDbs[1].name());
                ps.setString(4, xDbs[2].name());




                resultSet = ps.executeQuery();

                while (resultSet.next()) {

                    String disaseID = resultSet.getString(ENTRY_ID);

                    String url = contructUrlFromDiseaseId(disaseID);

                    Disease disease = new Disease();

                    //set the url or web link for this compound
                    if (url != null) {
                        disease.setUrl(url);
                    } else {
                        disease.setUrl("#");
                    }



                    String entryId = resultSet.getString(ENTRY_ID);
                    String entryName = resultSet.getString(ENTRY_NAME);
                    Integer numEnzyme = resultSet.getInt("numEnzyme");


                    if (entryId != null && entryName != null) {
                        disease.setId(entryId);
                        disease.setNumEnzyme(numEnzyme);
                        String diseaseName = resolveSpecialCharacters(entryName.toLowerCase(Locale.ENGLISH));
                        disease.setName(diseaseName.replaceAll(",", ""));

                        diseases.add(disease);
                    }

                }
            }

        } catch (SQLException e) {
            LOGGER.error(" (" + xDbs + ")", e);
        } finally {
            closeResultSet(resultSet);
            closePreparedStatement(ps);
        }

        return diseases;
    }

    public Entry findByEntryId(String entryId) {
        Entry entry = null;
        ResultSet resultSet = null;
        PreparedStatement ps = null;

        try {
            String query = "select entry.* from MM_ENTRY entry where entry.ENTRY_ID = ?";

            if (con != null) {
                ps = con.prepareStatement(query);




                ps.setString(1, entryId);


                resultSet = ps.executeQuery();

                while (resultSet.next()) {



                    String entryid = resultSet.getString(ENTRY_ID);
                    String entryName = resultSet.getString(ENTRY_NAME);
                    if (entryName == null) {
                        entryName = "";// to avoid nulL pointer when there is no entry name
                    }
                    String dbName = resultSet.getString(DB_NAME);
                    entry = new Entry();


                    if (entryid != null) {
                        entry.setEntryId(entryid);
                        entry.setEntryName(entryName);
                        entry.setDbName(dbName);

                    }

                }
            }

        } catch (SQLException e) {
            LOGGER.error(" (" + entryId + ")", e);
        } finally {
            closeResultSet(resultSet);
            closePreparedStatement(ps);
        }


        return entry;
    }

    public List<String> findEcNumbers() {

        ResultSet resultSet = null;
        PreparedStatement ps = null;
        List<String> ecNumbers = null;
        try {
            if (ecNumbers == null) {
                ecNumbers = new LinkedList<String>();
            }

            String query = "select * from MM_ENTRY where ENTRY_NAME is not null and DB_NAME ='EC'";


            if (con != null) {
                ps = con.prepareStatement(query);

                resultSet = ps.executeQuery();

                while (resultSet.next()) {



                    String entryid = resultSet.getString(ENTRY_ID);

                    if (entryid != null) {

                        ecNumbers.add(entryid);

                    }

                }
            }

        } catch (SQLException e) {
            LOGGER.error(" (" + resultSet + ")", e);
        } finally {
            closeResultSet(resultSet);
            closePreparedStatement(ps);
        }

        return ecNumbers;
    }
    
    
        public Collection<CustomXRef> getXrefs_ec_Only(Entry entry, MmDatabase... dbs) {
        Collection<CustomXRef> xrefs = null;
        PreparedStatement ps = null;
        int rowcount = 50;//param, 
        
        int start = 0;//param
        int end = start + rowcount;
        
        try {
            if (entry.getId() == null) {
                // update with value from the database:
                existsInMegaMap(entry);
            }
            if ( entry.getId() != null) {

                String query1 = "SELECT mmx.* FROM mm_xref mmx, mm_entry mme WHERE (mmx.from_entry = ? AND mmx.to_entry = mme.id AND mme.db_name IN (?)"
                        + "  OR (mmx.to_entry = ? AND mmx.from_entry = mme.id AND mme.db_name IN (?))) and rownum between ? and ? order by mmx.ID asc";


                String query = "SELECT * FROM (SELECT mmx.*, COUNT(*) OVER()RESULT_COUNT FROM mm_xref mmx, mm_entry mme WHERE (mmx.from_entry = ? AND mmx.to_entry = mme.id AND mme.db_name IN (?)\n" +
                                " OR (mmx.to_entry = ? AND mmx.from_entry = mme.id AND mme.db_name IN (?))) \n" +
                                " order by mmx.ID asc ) WHERE rownum between ? and ?";
                
               
                
                ps = con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                 ps.setInt(1, entry.getId());
                   ps.setString(2, dbs[0].name());
                  ps.setInt(3, entry.getId());
                ps.setString(4, dbs[0].name());
                ps.setInt(5, start);
                ps.setInt(6, end);
                
                ResultSet rs = ps.executeQuery();

                xrefs = buildXref_custom(rs);

            } else {
                LOGGER.warn(entry.toString() + " not known in the mega-map");
            }
        } catch (SQLException e) {
            LOGGER.error(entry.getId(), e);
        }
        return xrefs;
    }
        
            private List<CustomXRef> buildXref_custom(ResultSet rs) throws SQLException {
        List<CustomXRef> xrefs = null;
          CustomXRef xref = new CustomXRef();
        while (rs.next()) {
            if (xrefs == null) {
                xrefs = new ArrayList<CustomXRef>();
            }
   
            xref.setId(rs.getInt("id"));
            
            String [] from_entry = getEntryById(rs.getInt("from_entry")).getEntryId().split("_");
             String ids = from_entry[0];
             xref.getIdList().add(ids);
            xref.setFromEntry(getEntryById(rs.getInt("from_entry")));
            xref.setToEntry(getEntryById(rs.getInt("to_entry")));
            xref.setRelationship(rs.getString("relationship"));
            xref.setResult_count(rs.getInt("RESULT_COUNT"));
            }
         xrefs.add(xref);
        rs.close();
        return xrefs;
            }

    public List<String> getEnzymesByCompound(String compoundId) {
        List<String> uniprotIds = null;
        ResultSet rs = null;
        try {
            PreparedStatement ps = sqlLoader
                    .getPreparedStatement("--uniprot.ids.by.compound.id");
            ps.setString(1, compoundId);
            rs = ps.executeQuery();
            while (rs.next()){
                if (uniprotIds == null) uniprotIds = new ArrayList<String>();
                uniprotIds.add(rs.getString("uniprot_id"));
            }
        } catch (Exception e) {
            LOGGER.error("Unable to retrieve enzymes by compound ID", e);
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException e) {
                    LOGGER.error("Unable to close result set", e);
                }
        }
        return uniprotIds;

    }

}
