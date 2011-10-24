package uk.ac.ebi.ep.mm;

public enum MmDatabase {

	ChEBI, ChEMBL, PDBeChem;
	
	public MmField getIdField(){
		MmField idField = null;
		switch (this) {
		case ChEBI:
			idField = MmField.CHEBI_ID;
			break;
		case ChEMBL:
			idField = MmField.CHEMBL_ID;
			break;
		}
		return idField;
	}
	
	public MmField getNameField(){
		MmField nameField = null;
		switch (this) {
		case ChEBI:
			nameField = MmField.CHEBI_NAME;
			break;
		}
		return nameField;
	}
}
