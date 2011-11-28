package uk.ac.ebi.ep.mm;

import java.io.Serializable;

public class Relationship implements Serializable {

	private static final long serialVersionUID = 6877255103431212096L;
	
	private long id;
	private Entry fromEntry;
	private String relationship;
	private Entry toEntry;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Entry getFromEntry() {
		return fromEntry;
	}
	public void setFromEntry(Entry fromEntity) {
		this.fromEntry = fromEntity;
	}
	public Entry getToEntry() {
		return toEntry;
	}
	public void setToEntry(Entry toEntity) {
		this.toEntry = toEntity;
	}
	public String getRelationship() {
		return relationship;
	}
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fromEntry == null) ? 0 : fromEntry.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((relationship == null) ? 0 : relationship.hashCode());
		result = prime * result
				+ ((toEntry == null) ? 0 : toEntry.hashCode());
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
		Relationship other = (Relationship) obj;
		if (fromEntry == null) {
			if (other.fromEntry != null)
				return false;
		} else if (!fromEntry.equals(other.fromEntry))
			return false;
		if (id != other.id)
			return false;
		if (relationship == null) {
			if (other.relationship != null)
				return false;
		} else if (!relationship.equals(other.relationship))
			return false;
		if (toEntry == null) {
			if (other.toEntry != null)
				return false;
		} else if (!toEntry.equals(other.toEntry))
			return false;
		return true;
	}
	
	
}
