package uk.ac.ebi.ep.mm.app;

import java.util.*;

import org.apache.commons.cli.CommandLine;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import uk.ac.ebi.ebisearchservice.ArrayOfString;
import uk.ac.ebi.ebisearchservice.EBISearchService;
import uk.ac.ebi.ebisearchservice.EBISearchService_Service;
import uk.ac.ebi.ep.mm.Entry;
import uk.ac.ebi.ep.mm.MmDatabase;
import uk.ac.ebi.ep.mm.Relationship;
import uk.ac.ebi.ep.mm.XRef;

/**
 * UniProt XML parser which takes into account only primary accessions,
 * entry names (IDs), organisms, EC numbers and PDB codes, and indexes/stores
 * them in a mega-map.
 * <br>
 * Only enzymes - i.e. entries with an EC number assigned - are considered.
 * @author rafa
 *
 */
public class UniprotSaxParser extends MmSaxParser {

	private static final String UNIPROT_ENTRY =
			"//uniprot/entry";

	private static final String UNIPROT_ENTRY_NAME =
			"//uniprot/entry/name";

	private static final String UNIPROT_ENTRY_ACCESSION =
			"//uniprot/entry/accession";

	private static final String UNIPROT_ENTRY_DBREFERENCE =
			"//uniprot/entry/dbReference";

	private static final String UNIPROT_ENTRY_DBREFERENCE_PROPERTY =
			"//uniprot/entry/dbReference/property";

	private static final String UNIPROT_ENTRY_ORGANISM_NAME =
			"//uniprot/entry/organism/name";

	private static final String UNIPROT_ENTRY_PROTEIN_REC_NAME =
			"//uniprot/entry/protein/recommendedName/fullName";
	
	private final Logger LOGGER = Logger.getLogger(UniprotSaxParser.class);

    protected boolean isEntry;

	protected boolean isAccession;

	protected boolean isEntryName;

	protected boolean isOrgSciName;

	protected boolean isOrgComName;
	
	protected boolean isDbRef;
	
	protected boolean isProperty;
	
	protected boolean isProtRecName;

    protected List<String> accessions = new ArrayList<String>();

	protected List<String> entryNames = new ArrayList<String>();

	protected String orgSciName;

	protected String orgComName;
	
	protected List<String> ecs = new ArrayList<String>();
	
	protected List<String> pdbCodes = new ArrayList<String>();

	/* DrugBank IDs */
	protected List<String> dbIds = new ArrayList<String>();

    /* DrugBank names */
    protected List<String> dbNames = new ArrayList<String>();

	protected String protRecName;
	
	private EBISearchService ebeyeService =
			new EBISearchService_Service().getEBISearchServiceHttpPort();

	/**
	 * Parses a UniProt XML file and indexes/stores the UniProt accessions,
	 * IDs and organisms into a mega-map.
	 * @param args see {@link CliOptionsParser#getCommandLine(String...)}
	 * @throws Exception in case of error while parsing.
	 */
	public static void main(String... args) throws Exception {
        CommandLine cl = CliOptionsParser.getCommandLine(args);
        if (cl != null) mainParse(cl, new UniprotSaxParser());
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes)
    throws SAXException {
        currentContext.push(localName);
        // Update flags:
        String currentXpath = getCurrentXpath();
		isEntry = UNIPROT_ENTRY.equals(currentXpath);
		isAccession = UNIPROT_ENTRY_ACCESSION.equals(currentXpath);
		isEntryName = UNIPROT_ENTRY_NAME.equals(currentXpath);
		final String typeAttr = attributes == null?
				null : attributes.getValue("", "type");
		isOrgSciName = UNIPROT_ENTRY_ORGANISM_NAME.equals(currentXpath)
				&& "scientific".equals(typeAttr);
		isOrgComName = UNIPROT_ENTRY_ORGANISM_NAME.equals(currentXpath)
				&& "common".equals(typeAttr);
		isDbRef = UNIPROT_ENTRY_DBREFERENCE.equals(currentXpath);
		isProperty = UNIPROT_ENTRY_DBREFERENCE_PROPERTY.equals(currentXpath);
		isProtRecName = UNIPROT_ENTRY_PROTEIN_REC_NAME.equals(currentXpath);
		// Clear placeholder:
		if (currentChars.length() > 0){
			currentChars.delete(0, currentChars.length());
		}
		if (isDbRef){
            final String idAttr = attributes == null?
                    null : attributes.getValue("", "id");
			if (MmDatabase.EC.name().equalsIgnoreCase(typeAttr)){
				ecs.add(idAttr);
			} else if (MmDatabase.PDB.name().equalsIgnoreCase(typeAttr)){
				pdbCodes.add(idAttr);
			} else if (MmDatabase.DrugBank.name().equalsIgnoreCase(typeAttr)){
                dbIds.add(idAttr);
            }
		} else if (isProperty){
            final String valueAttr = attributes == null?
                    null : attributes.getValue("", "value");
			if ("method".equalsIgnoreCase(typeAttr)
					&& "Model".equalsIgnoreCase(valueAttr)){
				// Ignore xrefs to PDB theoretical models (which are deprecated)
				// Remove the last added xref to PDB:
				String model = pdbCodes.remove(pdbCodes.size() - 1);
				LOGGER.warn("Ignoring PDB theoretical model " + model);
			} else if ("generic name".equalsIgnoreCase(typeAttr)){
                dbNames.add(valueAttr);
            }
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// Check whether we need to do something:
		if (isAccession || isEntryName || isOrgSciName || isOrgComName || isProtRecName){
			currentChars.append(Arrays.copyOfRange(ch, start, start+length));
		}
	}

	/**
	 * Stores interesting data into the index.
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
	throws SAXException {
		if (isAccession){ // take only the primary one!
			accessions.add(currentChars.toString());
		} else if (isEntryName){
			entryNames.add(currentChars.toString());
		} else if (isOrgSciName){
			orgSciName = currentChars.toString();
		} else if (isOrgComName){
			orgComName = currentChars.toString();
		} else if (isProtRecName){
			protRecName = currentChars.toString();
		} else if (isEntry){
			if (!ecs.isEmpty()){ // XXX here is the enzyme filter
				try {
					Collection<Entry> entries = new HashSet<Entry>();
					Collection<XRef> xrefs = new HashSet<XRef>();

					Entry uniprotEntry = new Entry();
					uniprotEntry.setDbName(MmDatabase.UniProt.name());
					uniprotEntry.setEntryAccessions(accessions);
					uniprotEntry.setEntryId(entryNames.get(0)); // take first one
					uniprotEntry.setEntryName(protRecName);
					entries.add(uniprotEntry);
					
					Entry speciesEntry = new Entry();
					speciesEntry.setDbName(MmDatabase.Linnean.name());
					speciesEntry.setEntryId(orgSciName);
					speciesEntry.setEntryName(orgComName);
					entries.add(speciesEntry);
					
					XRef up2sp = new XRef();
					up2sp.setFromEntry(uniprotEntry);
					up2sp.setRelationship(Relationship.between(
							MmDatabase.UniProt, MmDatabase.Linnean).name());
					up2sp.setToEntry(speciesEntry);
					xrefs.add(up2sp);
					
					for (String ec: ecs){
						Entry ecEntry = new Entry();
						ecEntry.setDbName(MmDatabase.EC.name());
						ecEntry.setEntryId(ec);
						entries.add(ecEntry);
						
						XRef up2ec = new XRef();
						up2ec.setFromEntry(uniprotEntry);
						up2ec.setRelationship(Relationship.between(
								MmDatabase.UniProt, MmDatabase.EC).name());
						up2ec.setToEntry(ecEntry);
						xrefs.add(up2ec);
					}
					
					for (String pdbCode : pdbCodes) {
						Entry pdbEntry = new Entry();
						pdbEntry.setDbName(MmDatabase.PDB.name());
						pdbEntry.setEntryId(pdbCode);
						// Add structure name:
						try {
							ArrayOfString fields = new ArrayOfString();
							fields.getString().add("name");
							String name = ebeyeService
									.getEntry("pdbe", pdbCode, fields)
									.getString().get(0);
							pdbEntry.setEntryName(name);
						} catch (Exception e){
							LOGGER.error("Couldn't get name for " + pdbCode, e);
						}
						
						if (pdbEntry.getEntryName() == null){
							// Happens with obsolete/redirected PDB entries:
							continue;
						}
						
						entries.add(pdbEntry);
						
						XRef up2pdb = new XRef();
						up2pdb.setFromEntry(uniprotEntry);
						up2pdb.setRelationship(Relationship.between(
								MmDatabase.UniProt, MmDatabase.PDB).name());
						up2pdb.setToEntry(pdbEntry);
						xrefs.add(up2pdb);
					}

                    int dbNameIndex = 0;
                    for (String dbId : dbIds) {
                        Entry dbEntry = new Entry();
                        dbEntry.setDbName(MmDatabase.DrugBank.name());
                        dbEntry.setEntryId(dbId);
                        dbEntry.setEntryName(dbNames.get(dbNameIndex++));
                        entries.add(dbEntry);

                        XRef up2db = new XRef();
                        up2db.setFromEntry(uniprotEntry);
                        up2db.setRelationship(Relationship.between(
                            MmDatabase.UniProt, MmDatabase.DrugBank).name());
                        up2db.setToEntry(dbEntry);
                        xrefs.add(up2db);
                    }

                    mm.write(entries, xrefs);
				} catch (Exception e) {
					throw new RuntimeException("Adding entry to mega-map", e);
				}
			}
			// Clean up:
			accessions.clear();
			entryNames.clear();
			ecs.clear();
			pdbCodes.clear();
			orgSciName = null;
            orgComName = null;
		}
		currentContext.pop();
		// Update flags:
		String currentXpath = getCurrentXpath();
		isEntry = UNIPROT_ENTRY.equals(currentXpath);
		isAccession = false;
		isEntryName = false;
		isOrgSciName = false;
	}

}
