package uk.ac.ebi.ep.mm.app;

import java.io.IOException;
import java.util.*;

import org.apache.commons.cli.CommandLine;
import org.apache.log4j.Logger;
import org.hibernate.NonUniqueResultException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import uk.ac.ebi.ep.adapter.chembl.*;
import uk.ac.ebi.ep.mm.Entry;
import uk.ac.ebi.ep.mm.MmDatabase;
import uk.ac.ebi.ep.mm.Relationship;
import uk.ac.ebi.ep.mm.XRef;

/**
 * Parser for EB-Eye XML files to populate a mega-map.
 * Currently, this implementation has been used with ChEBI and
 * ChEMBL-target files (ChEMBL target is the
 * only one for this database which links compounds with protein IDs).
 * @author rafa
 *
 */
public class EbeyeSaxParser extends MmSaxParser {

	/**
	 * Cross-references to be extracted from the EB-Eye XML file, which depend
	 * on the file (database) being parsed.
	 */
	private static Map<MmDatabase, MmDatabase[]> interestingXrefs =
			new HashMap<MmDatabase, MmDatabase[]>();
	
	static {
		final MmDatabase[] chebiInterestingXrefs = new MmDatabase[]{
			MmDatabase.SwissProt,
			MmDatabase.PDBeChem,
			MmDatabase.ChEMBL,
			MmDatabase.PDB
		};
		Arrays.sort(chebiInterestingXrefs);
		interestingXrefs.put(MmDatabase.ChEBI, chebiInterestingXrefs);
		interestingXrefs.put(MmDatabase.PDBeChem,
				new MmDatabase[]{ MmDatabase.PDB });
//		interestingXrefs.put(MmDatabase.ChEMBL_Target,
//				new MmDatabase[]{ MmDatabase.ChEMBL });
	}
	
	private static final String DATABASE_NAME = "//database/name";
	private static final String DATABASE_ENTRIES = "//database/entries";
	private static final String DATABASE_ENTRIES_ENTRY =
			DATABASE_ENTRIES + "/entry";
	private static final String DATABASE_ENTRIES_ENTRY_NAME = 
			DATABASE_ENTRIES_ENTRY + "/name";
	private static final String DATABASE_ENTRIES_ENTRY_XREFS =
			DATABASE_ENTRIES_ENTRY + "/cross_references";
	private static final String DATABASE_ENTRIES_ENTRY_XREFS_REF =
			DATABASE_ENTRIES_ENTRY_XREFS + "/ref";
	
	private final Logger LOGGER = Logger.getLogger(EbeyeSaxParser.class);

	private boolean isDbName;
	private boolean isEntry;
	private boolean isEntryName;
	private boolean isXrefs;
	private boolean isRef;
	
	private MmDatabase db;
	private Entry entry;
	private String chemblTargetId;
	private Collection<XRef> xrefs = new HashSet<XRef>();
    private IChemblAdapter chemblAdapter;

    /**
	 * Parses an EB-Eye XML file and indexes/stores the entry IDs, accessions
	 * and xrefs into a mega-map.
	 * @param args see {@link CliOptionsParser#getCommandLine(String...)}
	 * @throws Exception in case of error while parsing.
	 */
	public static void main(String... args) throws Exception {
        CommandLine cl = CliOptionsParser.getCommandLine(args);
        if (cl != null) mainParse(cl, new EbeyeSaxParser());
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		currentContext.push(localName);
		// Update flags:
		String currentXpath = getCurrentXpath();
		isDbName = DATABASE_NAME.equals(currentXpath);
		isEntry = DATABASE_ENTRIES_ENTRY.equals(currentXpath);
		isEntryName = DATABASE_ENTRIES_ENTRY_NAME.equals(currentXpath);
		isXrefs = DATABASE_ENTRIES_ENTRY_XREFS.equals(currentXpath);
		isRef = DATABASE_ENTRIES_ENTRY_XREFS_REF.equals(currentXpath);
		
		/*
		 * ChEMBL_Target entries are proteins, the entry will be set to
		 * the xref'd Swiss-Prot entry (see below).
		 */
		if (isEntry){
            if (MmDatabase.ChEMBL_Target.equals(db)){
                chemblTargetId = attributes.getValue("", "id");
                LOGGER.debug("ChEMBL target ID: " + chemblTargetId);
            } else {
                entry = new Entry();
                entry.setDbName(db.name());
                entry.setEntryId(attributes.getValue("", "id"));
                // ChEBI acc is the id with a 'CHEBI:' prefix
                // PDBeChem id and acc are identical
                final String acc = attributes.getValue("", "acc");
                if (acc != null){
                    entry.setEntryAccessions(Collections.singletonList(acc));
                }
                LOGGER.debug("Parsing entry " + entry.getEntryId());
            }
		} else if (isRef){
			final MmDatabase refdDb =
					MmDatabase.parse(attributes.getValue("", "dbname"));
			String dbKey = attributes.getValue("", "dbkey");
			if (dbKey == null){
				// Strange case: PDBeChem refers to PDBe using altkey:
				dbKey = attributes.getValue("", "altkey");
			}
			// The xref'd database might be unknown to us:
			if (refdDb != null && dbKey != null){
				if (MmDatabase.ChEMBL_Target.equals(db)
						&& MmDatabase.SwissProt.equals(refdDb)){
					// ChEMBL-Target entries can be proteins targeted by drugs:
                    setMmUniprotEntry(dbKey);
                    // If it is an enzyme (is in the mega-map), go ahead:
                    if (entry != null){
                        addChemblXrefs();
                    }
                } else if (isInterestingXref(db, refdDb)){
                    Entry refdEntry = getReferencedEntry(refdDb, dbKey);
					if (refdEntry != null){
						LOGGER.debug("\tParsing xref to " + refdEntry.getEntryId());
						XRef xref = new XRef();
						xref.setFromEntry(entry);
						xref.setRelationship(Relationship.between(db, refdDb).name());
						xref.setToEntry(refdEntry);
						xrefs.add(xref);
					}
				}
			}
		}
		// Clear placeholder:
		currentChars.delete(0, Integer.MAX_VALUE);
	}

    /**
     * Adds cross references to any ChEMBL compound IDs which show any
     * bioactivity against <code>chemblTargetId</code>.
     */
    private void addChemblXrefs() {
        try {
            final Collection<String> chemblCompoundIds =
                    getFilteredChemblCompounds();
            if (chemblCompoundIds != null){
                for (String chemblCompoundId : chemblCompoundIds) {
                    Entry chemblEntry = new Entry();
                    chemblEntry.setDbName(MmDatabase.ChEMBL.name());
                    chemblEntry.setEntryId(chemblCompoundId);
                    chemblEntry.setEntryName(chemblAdapter
                            .getPreferredName(chemblCompoundId));
                    XRef xref = new XRef();
                    xref.setFromEntry(entry);
                    xref.setRelationship(Relationship.between(
                            MmDatabase.UniProt, MmDatabase.ChEMBL)
                            .name());
                    xref.setToEntry(chemblEntry);
                    xrefs.add(xref);
                }
            }
        } catch (Exception e){
            LOGGER.error(chemblTargetId, e);
        }
    }

    /**
     * Gets bioactivities from WS for the target ID stored in the field
     * <code>chemblTargetId</code> and filters them.
     * @return a collection of ChEMBL compound IDs which have significant
     *      bioactivities,or <code>null</code> if no bioactivities are found
     *      for the target ID.
     * @throws ChemblAdapterException
     */
    private Collection<String> getFilteredChemblCompounds()
    throws ChemblAdapterException {
        Collection<String> filteredBioactivities = null;
        ChemblBioactivities bioactivities =
                chemblAdapter.getTargetBioactivities(chemblTargetId);
        //LOGGER.debug(chemblTargetId + " bioactivities: " + bioactivities);
        if (bioactivities != null){
            filteredBioactivities = bioactivities.filter(
                    chemblAdapter.getConfig().getMinAssays(),
                    chemblAdapter.getConfig().getMinConf4(),
                    chemblAdapter.getConfig().getMinConf9(),
                    chemblAdapter.getConfig().getMinFunc());
//            LOGGER.debug(chemblTargetId + " filtered bioactivities: "
//                    + filteredBioactivities);
            LOGGER.debug(bioactivities.getMap().size() + " down to "
                    + filteredBioactivities.size());
        }
        return filteredBioactivities;
    }

    /**
     * Sets the private field <code>entry</code> to a UniProt entry from the
     * mega-map. If it does not exist in the mega-map, the field is set to
     * <code>null</code>.
     * @param uniprotAcc the UniProt accession.
     */
    private void setMmUniprotEntry(String uniprotAcc) {
        try {
            entry = mm.getEntryForAccession(MmDatabase.UniProt, uniprotAcc);
            if (entry != null){
                LOGGER.info(uniprotAcc + " is enzyme.");
                for (XRef xref : xrefs) {
                    xref.setFromEntry(entry);
                }
            }
        } catch (NonUniqueResultException e){
            LOGGER.warn(uniprotAcc + " refers to more than one UniProt entry", e);
        }
    }

    /**
     * Retrieves or creates an entry.
     * @param refdDb The referenced database.
     * @param dbKey the ID or accession of the entry.
     * @return an entry retrieved from the mega-map (UniProt) or created from
     *      scratch (otherwise). It can be <code>null</code> in case of
     *      UniProt entries not stored in the mega-map (probably not enzymes).
     */
    private Entry getReferencedEntry(MmDatabase refdDb, String dbKey) {
        Entry refdEntry = null;
        if (refdDb.equals(MmDatabase.UniProt)){
            try {
                refdEntry = mm.getEntryForAccession(MmDatabase.UniProt, dbKey);
            } catch (NonUniqueResultException e){
                LOGGER.warn(dbKey + " refers to more than one UniProt entry", e);
            }
        } else {
            refdEntry = new Entry();
            refdEntry.setDbName(refdDb.name());
            refdEntry.setEntryId(dbKey);
            // TODO: setEntryName, if possible
        }
        return refdEntry;
    }

    /**
	 * Are we interested in a cross reference?
	 * @param db the database providing the EB-Eye XML file.
	 * @param refdDb the referenced database.
	 * @return <code>true</code> if the xref is interesting
	 * (see {@link #interestingXrefs}).
	 */
	private boolean isInterestingXref(MmDatabase db, MmDatabase refdDb) {
	    return interestingXrefs.get(db) != null
	            && Arrays.binarySearch(interestingXrefs.get(db), refdDb) > -1;
	}

	@Override
	public void characters(char[] ch, int start, int length)
    throws SAXException {
		if (isDbName || isEntryName){
			currentChars.append(Arrays.copyOfRange(ch, start, start+length));
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
    throws SAXException {
		if (isDbName){
			db = MmDatabase.parse(currentChars.toString());
			LOGGER.info("Parsing EB-Eye file for " + db.name());
			if (db.equals(MmDatabase.ChEMBL_Target)){
			    // TODO: CHANGE this is just for testing
			    ChemblConfig chemblConfig = new ChemblConfig();
			    chemblConfig.setMinAssays(5);
			    chemblConfig.setMinConf4(0.0);
			    chemblConfig.setMinConf9(0.5);
			    chemblConfig.setMinFunc(0.5);
                chemblAdapter = new ChemblWsAdapter(chemblConfig);
            }
		} else if (isEntryName && entry != null){
			entry.setEntryName(currentChars.toString());
		} else if (isEntry){
            try {
                if (entry != null && !xrefs.isEmpty()){
                    mm.write(Collections.singleton(entry), xrefs);
                }
            } catch (IOException e) {
                throw new SAXException("Adding entry to mega-map", e);
            } finally {
                entry = null;
                chemblTargetId = null;
                xrefs.clear();
            }
	    }
		currentContext.pop();
		// Update flags:
		String currentXpath = getCurrentXpath();
		isXrefs = DATABASE_ENTRIES_ENTRY_XREFS.equals(currentXpath);
		isEntry = DATABASE_ENTRIES_ENTRY.equals(currentXpath);
		isDbName = false;
		isEntryName = false;
		isRef = false;
	}

}
