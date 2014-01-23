package uk.ac.ebi.ep.mm.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import uk.ac.ebi.ep.adapter.chembl.ChemblAdapterException;
import uk.ac.ebi.ep.adapter.chembl.ChemblBioactivities;
import uk.ac.ebi.ep.adapter.chembl.ChemblConfig;
import uk.ac.ebi.ep.adapter.chembl.ChemblWsAdapter;
import uk.ac.ebi.ep.adapter.chembl.IChemblAdapter;
import uk.ac.ebi.ep.mm.Entry;
import uk.ac.ebi.ep.mm.MmDatabase;
import uk.ac.ebi.ep.mm.Relationship;
import uk.ac.ebi.ep.mm.XRef;

/**
 * Parser for the <code>chembl-target_component.xml</code> file (ebinocle
 * schema). It reads the UniProt accession of every entry and any target IDs.
 * If the accession is an enzyme (that is, already existing in the mega-map)
 * then the target IDs are used with the chembl-adapter to query for
 * bioactivities, of which only the most significant will be kept and their
 * ChEMBL compound IDs used for the cross-references.
 * @author rafa
 * @since 1.0.22
 */
public class ChemblSaxParser extends MmSaxParser implements EbinocleParser {

    private final Logger LOGGER = Logger.getLogger(ChemblSaxParser.class);

    /**
     * The current entry (target component, i.e. UniProt entry) being processed.
     */
    private Entry entry;

    /**
     * The proxy used to extract information from ChEMBL (only used if
     * {@link #db} = ChEMBL).
     */
    private IChemblAdapter chemblAdapter;

    /* Flags to mark the current element being processed: */
    private boolean isEntry;
    private boolean isRef;
    private boolean isField;
    private boolean isAccession;

    /* Values stored for the current entry being processed: */
    private String accession;
    private List<String> targetIds = new ArrayList<String>();
    
    public ChemblSaxParser() {
        ChemblConfig chemblConfig = null;
        try {
            chemblConfig = ChemblConfig.readFromFile();
        } catch (IOException e) {
            LOGGER.error("Unable to read config file, using defaults", e);
            chemblConfig = new ChemblConfig();
        }
        chemblAdapter = new ChemblWsAdapter(chemblConfig);
    }

    public static void main(String[] args) throws Exception {
        CommandLine cl = CliOptionsParser.getCommandLine(args);
        if (cl != null) mainParse(cl, new ChemblSaxParser());
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        currentContext.push(localName);
        // Update flags:
        String currentXpath = getCurrentXpath();
        isEntry = DATABASE_ENTRIES_ENTRY.equals(currentXpath);
        isRef = DATABASE_ENTRIES_ENTRY_XREFS_REF.equals(currentXpath);
        isField = DATABASE_ENTRIES_ENTRY_FIELD.equals(currentXpath);
        
        if (isField){
            String name = attributes.getValue("", "name");
            isAccession = name != null && name.equals("accession");
        } else if (isRef){
            String dbName = attributes.getValue("", "dbname");
            if (dbName != null && dbName.equals("ChEMBL-Target")){
                targetIds.add(attributes.getValue("", "dbkey"));
            }
        }
        // Clear placeholder:
        currentChars.delete(0, Integer.MAX_VALUE);
    }

    @Override
    public void endElement(String uri, String localName, String qName)
    throws SAXException {
        if (isAccession){
            accession = currentChars.toString();
        } else if (isEntry){
            if (accession != null && !targetIds.isEmpty()){
                entry = mm.getEntryForAccession(MmDatabase.UniProt, accession);
                if (entry != null){
                    LOGGER.info(accession + " is enzyme.");
                    Collection<XRef> xrefs = getChemblXrefs();
                    try {
                        if (xrefs != null && !xrefs.isEmpty()){
                            mm.write(Collections.singleton(entry), xrefs);
                        }
                    } catch (IOException e) {
                        throw new SAXException("Adding entry to mega-map", e);
                    } finally {
                        entry = null;
                        targetIds.clear();
                    }
                }
            }
            accession = null;
            targetIds.clear();
        }
        currentContext.pop();
        // Update flags:
        String currentXpath = getCurrentXpath();
        isEntry = DATABASE_ENTRIES_ENTRY.equals(currentXpath);
        isRef = false;
        isField = false;
        isAccession = false;
    }

    @Override
    public void characters(char[] ch, int start, int length){
        if (isAccession){
            currentChars.append(Arrays.copyOfRange(ch, start, start+length));
        }
    }

    /**
     * Adds cross references to any ChEMBL compound IDs which show any
     * bioactivity against the <code>{@link #targetIds}</code> of the element
     * being processed.
     * @return a collection of cross-references from UniProt to ChEMBL, or
     *      <code>null</code> if none found.
     */
    private Collection<XRef> getChemblXrefs() {
        Collection<XRef> xrefs = null;
        try {
            final Collection<String> chemblCompoundIds =
                    getFilteredChemblCompounds();
            if (chemblCompoundIds != null){
                xrefs = new HashSet<XRef>();
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
                    xrefs .add(xref);
                }
            }
        } catch (Exception e){
            LOGGER.error(targetIds, e);
        }
        return xrefs;
    }

    /**
     * Gets bioactivities from WS for the target IDs stored in the field
     * <code>{@link #targetIds}</code> and filters them.
     * @return a collection of ChEMBL compound IDs which have significant
     *      bioactivities, or <code>null</code> if no bioactivities are found
     *      for the target ID.
     * @throws ChemblAdapterException
     */
    private Collection<String> getFilteredChemblCompounds()
    throws ChemblAdapterException {
        Collection<String> filteredBioactivities = null;
        for (String targetId : targetIds) {
            ChemblBioactivities bioactivities =
                    chemblAdapter.getTargetBioactivities(targetId);
            if (bioactivities != null){
                filteredBioactivities = bioactivities.filter(
                        chemblAdapter.getConfig().getMinAssays(),
                        chemblAdapter.getConfig().getMinConf4(),
                        chemblAdapter.getConfig().getMinConf9(),
                        chemblAdapter.getConfig().getMinFunc());
                LOGGER.debug(bioactivities.getMap().size()
                        + " bioactivities down to "
                        + filteredBioactivities.size());
            }
        }
        return filteredBioactivities;
    }

}
