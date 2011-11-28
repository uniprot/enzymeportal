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
	is_activator_of
}
