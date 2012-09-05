package uk.ac.ebi.ep.mm;

import org.apache.log4j.Logger;

/**
 * Databases included in the mega-map (or encountered in some way during the
 * process of building it).
 * @author rafa
 *
 */
public enum MmDatabase {
	
	Agricola("Agricola citation"),
	ArrayExpressAtlas,
	BRENDA,
	Beilstein("Beilstein Registry Number"),
	BioModels,
	CAS("CAS Registry Number"),
	COMe,
	ChEBI,
	ChEMBL("ChEMBL COMPOUND", "ChEMBL-Compound"),
	ChEMBL_Activity("ChEMBL-Activity"),
	ChEMBL_Target("ChEMBL.Target"),
	Chinese_Abstracts("Chinese Abstracts citation"),
	DrugBank,
	EC,
	EFO,
	Gmelin("Gmelin Registry Number"),
	Golm,
	HMDB,
	IEDB,
	IntAct,
	IntEnz,
	KEGG_COMPOUND("KEGG COMPOUND"),
	KEGG_DRUG("KEGG DRUG"),
	KEGG_GLYCAN("KEGG GLYCAN"),
	Linnean, // taxonomy
	LIPID_MAPS_class("LIPID MAPS class"),
	LIPID_MAPS_instance("LIPID MAPS instance"),
	MeSH,
	MetaCyc,
	MolBase,
	NMRShiftDB,
	OMIM,
	PDB("PDBe"),
	PDBeChem("MSDCHEM"),
	Patent,
	PubChem,
	PubMed("PubMed citation"),
	RESID,
	Reactome,
	Reaxys("Reaxys Registry Number"),
	Rhea,
	SABIO_RK("SABIO-RK"),
	UM_BBD_compID("UM-BBD compID"),
	UniProt("Swiss-Prot", "TrEMBL"),
	WebElements,
	Wikipedia;
	

	private static final Logger LOGGER = Logger.getLogger(MmDatabase.class);

	/**
	 * Any synonyms for the database.
	 */
	private String[] synonyms;
	private MmDatabase(String... synonyms){
		this.synonyms = synonyms;
	}
	
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
	 * than <code>valueOf</code>, accepting database synonyms (ex. 'TrEMBL'
	 * will be parsed as {@link #UniProt}) and ignoring letter case.
	 * @param s
	 * @return a MmDatabase, or <code>null</code> if no match is found.
	 */
	public static MmDatabase parse(String s){
		MmDatabase result = null;
		try {
			result = MmDatabase.valueOf(s);
		} catch (Exception e){
			dbLoop: for (MmDatabase db : values()) {
				// Try ignoring case:
				if (db.name().equalsIgnoreCase(s)){
					result = db;
					break dbLoop;
				}
				// Try to guess from synonyms:
				if (db.synonyms != null){
					for (String synonym: db.synonyms){
						if (synonym.equalsIgnoreCase(s)){
							result = db;
							break dbLoop;
						}
					}
				}
			}
		}
		if (result == null){
			LOGGER.warn("No database found for " + s);
		}
		return result;
	}
}
