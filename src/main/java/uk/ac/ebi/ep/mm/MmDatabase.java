package uk.ac.ebi.ep.mm;

/**
 * Databases included in the mega-map.
 * @author rafa
 *
 */
public enum MmDatabase {

	ChEBI, ChEMBL, PDBeChem, UniProt, Linnean, EC;
	
	public String getIdField(){
		return name() + "_ID";
	}
	
	public String getNameField(){
		return name() + "_NAME";
	}
	
	public String getAccessionField(){
		return name() + "_ACCESSION";
	}

	/**
	 * Parses a string into a MmDatabase value. This method is more flexible
	 * than <code>valueOf</code>, accepting names which simply start with the
	 * database name, ignoring letter case. It also interprets 'Swiss-Prot' and
	 * 'TrEMBL' as {@link MmDatabase#UniProt}.
	 * @param s
	 * @return a MmDatabase, or <code>null</code> if no match is found.
	 */
	public static MmDatabase parse(String s){
		if ("Swiss-Prot".equalsIgnoreCase(s) || "TrEMBL".equalsIgnoreCase(s)){
			return UniProt;
		}
		for (MmDatabase db : values()) {
			if (s.toUpperCase().startsWith(db.name().toUpperCase())){
				return db;
			}
		}
		return null;
	}
}
