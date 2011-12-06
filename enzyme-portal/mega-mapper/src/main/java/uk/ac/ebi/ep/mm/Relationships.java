package uk.ac.ebi.ep.mm;

public enum Relationships {

	/** Most generic. */
	is_related_to,
	/** Is part of, is found in... */
	belongs_to,
	
	// Relationships from compounds:
	binds_to,
	is_drug_for,
	is_cofactor_of,
	is_inhibitor_of,
	is_activator_of;
	
	/**
	 * Gets the usual relationship between two databases.
	 */
	public static Relationships between(MmDatabase from, MmDatabase to){
		Relationships result = is_related_to;
		switch (from) {
		case UniProt:
			switch (to) {
			case Linnean:
			case EC:
				result = belongs_to;
				break;
			}
			break;
		case ChEBI:
			switch (to) {
			case ChEMBL:
				break;
			case UniProt:
				break;
			}
			break;
		case ChEMBL:
			break;
		}
		return result;
	}
}
