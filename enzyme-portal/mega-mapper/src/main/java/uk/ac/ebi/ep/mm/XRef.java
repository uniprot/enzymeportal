package uk.ac.ebi.ep.mm;

import java.io.Serializable;

/**
 * This class is one case of triple, same as found in RDF.
 * @author rafa
 *
 */
public class XRef implements Serializable {

	private static final long serialVersionUID = 6877255103431212096L;
	
	private Integer id;
	private Entry fromEntry;
	private Relationship relationship;
	private Entry toEntry;
        
	public Integer getId() {
		return id;
//		return hashCode(); // FIXME, possibly not unique
	}
	public void setId(Integer id) {
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
		return relationship == null? null : relationship.name();
	}
	public void setRelationship(String s) {
		this.relationship = Relationship.valueOf(s);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fromEntry == null) ? 0 : fromEntry.hashCode());
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
		XRef other = (XRef) obj;
		if (fromEntry == null) {
			if (other.fromEntry != null)
				return false;
		} else if (!fromEntry.equals(other.fromEntry))
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
	
	@Override
	public String toString() {
		return new StringBuilder(fromEntry.toString())
			.append(" =[").append(relationship.name()).append("]=> ")
			.append(toEntry.toString())
			.toString();
	}
	
	
}
