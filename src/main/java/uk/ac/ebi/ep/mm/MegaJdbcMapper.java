package uk.ac.ebi.ep.mm;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import uk.ac.ebi.biobabel.util.db.SQLLoader;

/**
 * Plain JDBC implementation of {@link MegaMapper}.
 * @author rafa
 *
 */
public class MegaJdbcMapper implements MegaMapper {

	private final Logger LOGGER = Logger.getLogger(MegaJdbcMapper.class);
	
	private Connection con;
	private SQLLoader sqlLoader;
	
	public MegaJdbcMapper(Connection con) throws IOException{
		this.con = con;
	}
	
	/**
	 * {@inheritDoc}
	 * <br>
	 * This implementation prepares the required statements.
	 */
	public void openMap() throws IOException {
		sqlLoader = new SQLLoader(this.getClass(), con);
	}

	public void writeEntry(Entry entry) throws IOException {
		try {
			if (existsInMegaMap(entry)) return;
			PreparedStatement wEntryStm = sqlLoader.getPreparedStatement("--insert.entry");
			wEntryStm.setInt(1, entry.getId());
			wEntryStm.setString(2, entry.getDbName());
			wEntryStm.setString(3, entry.getEntryId());
			if (entry.getEntryName() != null){
				wEntryStm.setString(4, entry.getEntryName());
			} else {
				wEntryStm.setNull(4, Types.VARCHAR);
			}
			wEntryStm.execute();
			if (entry.getEntryAccessions() != null){
				int index = 0;
				PreparedStatement wAccStm = sqlLoader.getPreparedStatement("--insert.accession");;
				for (String accession : entry.getEntryAccessions()) {
					wAccStm.setInt(1, entry.getId());
					wAccStm.setString(2, accession);
					wAccStm.setInt(3, index++);
					wAccStm.execute();
				}
			}
		} catch (SQLException e) {
			throw new IOException(e);
		}
	}

	public void writeEntries(Collection<Entry> entries) throws IOException {
		if (entries != null){
			for (Entry entry : entries) {
				writeEntry(entry);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * <br>
	 * This implementation writes any (or both) of the two linked
	 * entries in case they don't already exist in the database.
	 */
	public void writeXref(XRef xref) throws IOException {
		try {
			writeEntry(xref.getFromEntry());
			writeEntry(xref.getToEntry());
			PreparedStatement wXrefStm = sqlLoader.getPreparedStatement("--insert.xref");
			wXrefStm.setInt(1, xref.getId());
			wXrefStm.setInt(2, xref.getFromEntry().getId());
			wXrefStm.setString(3, xref.getRelationship());
			wXrefStm.setInt(4, xref.getToEntry().getId());
			wXrefStm.execute();
		} catch (SQLException e){
			throw new IOException(e);
		}
	}

	public void writeXrefs(Collection<XRef> xrefs) throws IOException {
		if (xrefs != null){
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
	 * @param entry
	 */
	public void deleteEntry(Entry entry){
		try {
			PreparedStatement dXrefStm = sqlLoader.getPreparedStatement("--delete.xrefs");
			dXrefStm .setInt(1, entry.getId());
			dXrefStm.setInt(2, entry.getId());
			dXrefStm.execute();
			PreparedStatement dAccStm = sqlLoader.getPreparedStatement("--delete.accessions");
			dAccStm .setInt(1, entry.getId());
			dAccStm.execute();
			PreparedStatement dEntryStm = sqlLoader.getPreparedStatement("--delete.entry");
			dEntryStm .setInt(1, entry.getId());
			dEntryStm.execute();
		} catch (SQLException e) {
			LOGGER.error(entry.getEntryId() + " (" + entry.getDbName() + ")", e);
		}
		
	}

	private boolean existsInMegaMap(Entry entry) throws SQLException{
		PreparedStatement rEntryStm = sqlLoader.getPreparedStatement("--entry.by.id");
		rEntryStm.setInt(1, entry.getId());
		return rEntryStm.executeQuery().next();
	}
	
	/**
	 * Converts an array of database objects into a comma-delimited list of
	 * single-quoted database names
	 * @param dbs
	 * @return
	 */
	private String dbArrayForQuery(MmDatabase... dbs) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < dbs.length; i++) {
			if (sb.length() > 0) sb.append(',');
			sb.append('\'').append(dbs[i].name()).append('\'');
		}
		return sb.toString();
	}

	private Entry getEntryById(int id) throws SQLException{
		Entry entry = null;
		PreparedStatement rEntryStm = sqlLoader.getPreparedStatement("--entry.by.id");
		rEntryStm.setInt(1, id);
		ResultSet rs = rEntryStm.executeQuery();
		if (rs.next()){
			entry = new Entry();
			entry.setDbName(rs.getString("db_name"));
			entry.setEntryId(rs.getString("entry_id"));
			entry.setEntryName(rs.getString("entry_name"));
		}
		return entry;
	}

	/**
	 * Builds a list of XRef objects from a result set.
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private List<XRef> buildXref(ResultSet rs) throws SQLException {
		List<XRef> xrefs = null;
		while (rs.next()){
			if (xrefs == null) xrefs = new ArrayList<XRef>();
			XRef xref = new XRef();
			xref.setFromEntry(getEntryById(rs.getInt("from_entry")));
			xref.setToEntry(getEntryById(rs.getInt("to_entry")));
			xref.setRelationship(rs.getString("relationship"));
			xrefs.add(xref);
		}
		return xrefs;
	}

	public Entry getEntryForAccession(MmDatabase db, String accession){
		Entry entry = null;
		try {
			PreparedStatement entryForAccessionStm =
					sqlLoader.getPreparedStatement("--entry.by.accession");
			entryForAccessionStm .setString(1, accession);
			entryForAccessionStm.setString(2, db.name());
			ResultSet rs = entryForAccessionStm.executeQuery();
			if (rs.next()){
				String dbName = rs.getString("db_name");
				String entryId = rs.getString("entry_id");
				String entryName = rs.getString("entry_name");
				if (rs.next()){
					LOGGER.error("More than one entry for same accession!"
							+ accession + " (" + db.name() + ")");
				}
				entry = new Entry();
				entry.setDbName(dbName);
				entry.setEntryId(entryId);
				entry.setEntryName(entryName);
				// TODO: load accessions?
			}
		} catch (SQLException e){
			LOGGER.error(accession + " (" + db.name() + ")", e);
		}
		return entry;
	}

	public Collection<XRef> getXrefs(Entry entry) {
		Collection<XRef> xrefs = null;
		try {
			PreparedStatement allXrefsByEntryStm =
					sqlLoader.getPreparedStatement("--xrefs.all.by.entry");
			allXrefsByEntryStm .setInt(1, entry.getId());
			allXrefsByEntryStm.setInt(2, entry.getId());
			ResultSet rs = allXrefsByEntryStm.executeQuery();
			xrefs = buildXref(rs);
		} catch (SQLException e){
			LOGGER.error(entry.getId(), e);
		}
		return xrefs;
	}

	public Collection<XRef> getXrefs(Entry entry, MmDatabase... dbs) {
		Collection<XRef> xrefs = null;
		try {
			PreparedStatement ps = sqlLoader.getPreparedStatement(
					"--xrefs.by.entry", dbArrayForQuery(dbs));
			int paramNum = 1;
			ps.setInt(paramNum++, entry.getId());
			ps.setInt(paramNum++, entry.getId());
			ResultSet rs = ps.executeQuery();
			xrefs = buildXref(rs);
		} catch (SQLException e){
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
			if (allXrefs == null) allXrefs = xrefs;
			else if (xrefs != null) allXrefs.addAll(xrefs);
		}
		return allXrefs;
	}

	public Collection<XRef> getXrefs(MmDatabase db, String accession) {
		Collection<XRef> xrefs = null;
		try {
			PreparedStatement allXrefsByAccStm =
					sqlLoader.getPreparedStatement("--xrefs.all.by.accession");
			allXrefsByAccStm .setString(1, accession);
			allXrefsByAccStm.setString(2, db.name());
			xrefs = buildXref(allXrefsByAccStm.executeQuery());
		} catch (SQLException e){
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
			xrefs = buildXref(ps.executeQuery());
		} catch (SQLException e) {
			LOGGER.error(accession + " (" + xDbs + ")", e);
		}
		
		return xrefs;
	}

	public void handleError() throws IOException {
		closeStatements();
	}

	/**
	 * {@inheritDoc}
	 * <br>
	 * This implementation just closes the prepared statements. Note that the
	 * connection is not closed, that is the client's responsibility.
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

}
