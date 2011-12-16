package uk.ac.ebi.ep.mm;

import java.io.Serializable;
import java.util.List;

public class Entry implements Serializable {

	private static final long serialVersionUID = -4341633305667373550L;

	private String dbName;
	private String entryId;
	private String entryName;
	
	/**
	 * Ordered list of accession for the entry. Databases have usually primary
	 * accessions, which will appear as first in the list.
	 */
	private List<String> accessions;
	
	public int getId() {
		return hashCode(); // FIXME, possibly not unique
	}
	public void setId(int id) {
		// no-op
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String db) {
		this.dbName = db;
	}
	public String getEntryId() {
		return entryId;
	}
	public void setEntryId(String id) {
		this.entryId = id;
	}
	public String getEntryName() {
		return entryName;
	}
	public void setEntryName(String name) {
		this.entryName = name;
	}
	public List<String> getAccessions() {
		return accessions;
	}
	public void setAccessions(List<String> accessions) {
		this.accessions = accessions;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dbName == null) ? 0 : dbName.hashCode());
		result = prime * result + ((entryId == null) ? 0 : entryId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entry other = (Entry) obj;
		if (dbName == null) {
			if (other.dbName != null)
				return false;
		} else if (!dbName.equals(other.dbName))
			return false;
		if (entryId == null) {
			if (other.entryId != null)
				return false;
		} else if (!entryId.equals(other.entryId))
			return false;
		return true;
	}
	
	
}
