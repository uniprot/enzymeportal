package uk.ac.ebi.ep.mm;

public enum Relationship {

	/** Most generic. */
	is_related_to,
	
	/**
	 * An entry in a database referencing basically the same entity in another
	 * one (unification xref).
	 */
	same_as,
	
	/** Is part of a wider group, is found in... */
	belongs_to,
	
	/** Is found in a concrete instance */
	is_part_of,
	
	// Relationships from compounds:
	binds_to,
	is_drug_for,
	is_cofactor_of,
	is_inhibitor_of,
	is_activator_of;
	
	/**
	 * Gets the usual relationship between two databases.
	 */
	public static Relationship between(MmDatabase from, MmDatabase to){
		Relationship result = is_related_to;
		switch (from) {
		case UniProt:
			switch (to) {
			case Linnean:
			case EC:
				result = belongs_to;
				break;
			case PDB:
				result = is_part_of;
			}
			break;
		case ChEBI:
			switch (to) {
			case PDBeChem:
				result = same_as;
				break;
			}
			break;
		case ChEMBL:
		case ChEMBL_Target:
			switch (to) {
			case UniProt:
				result = is_drug_for;
				break;
			}
			break;
		}
		return result;
	}
}
