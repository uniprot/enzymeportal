package uk.ac.ebi.ep.data.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import uk.ac.ebi.ep.adapter.chembl.ChemblAdapterException;
import uk.ac.ebi.ep.adapter.chembl.ChemblBioactivities;
import uk.ac.ebi.ep.adapter.chembl.ChemblConfig;
import uk.ac.ebi.ep.adapter.chembl.ChemblWsAdapter;
import uk.ac.ebi.ep.adapter.chembl.IChemblAdapter;
import uk.ac.ebi.ep.data.domain.EnzymePortalCompound;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.helper.EbinocleParser;
import uk.ac.ebi.ep.data.helper.MmDatabase;
import uk.ac.ebi.ep.data.helper.Relationship;
import uk.ac.ebi.ep.data.repositories.EnzymePortalCompoundRepository;
import uk.ac.ebi.ep.data.repositories.UniprotEntryRepository;

/**
 * Parser for the <code>chembl-target_component.xml</code> file (ebinocle
 * schema). It reads the UniProt accession of every entry and any target IDs.
 * If the accession is an enzyme (that is, already existing in the mega-map)
 * then the target IDs are used with the chembl-adapter to query for
 * bioactivities, of which only the most significant will be kept and their
 * ChEMBL compound IDs used for the cross-references.
 * $Author$
 * @since 1.0.22
 */
public class ChemblSaxParser  extends DefaultHandler implements EbinocleParser {

    private final Logger LOGGER = Logger.getLogger(ChemblSaxParser.class);
    
       /**
     * The current element (tree path) being parsed.
     */
    protected Stack<String> currentContext = new Stack<>();
    /**
     * The text value of the current element being parsed.
     */
    protected StringBuilder currentChars = new StringBuilder();

    /**
     * The current entry (target component, i.e. UniProt entry) being processed.
     */
    private UniprotEntry entry;

    /**
     * The proxy used to extract information from ChEMBL (only used if
     * {@link #db} = ChEMBL).
     */
    private final IChemblAdapter chemblAdapter;

    /* Flags to mark the current element being processed: */
    private boolean isEntry;
    private boolean isRef;
    private boolean isField;
    private boolean isAccession;

    /* Values stored for the current entry being processed: */
    private String accession;
    private final  List<String> targetIds = new ArrayList<>();
    
      private final EnzymePortalCompoundRepository repository;
      private final UniprotEntryRepository uniprotEntryRepository;
    
    public ChemblSaxParser(EnzymePortalCompoundRepository repository, UniprotEntryRepository entryRepository) {
        ChemblConfig chemblConfig = null;
        try {
            chemblConfig = ChemblConfig.readFromFile();
        } catch (IOException e) {
            LOGGER.error("Unable to read config file, using defaults", e);
            chemblConfig = new ChemblConfig();
        }
        chemblAdapter = new ChemblWsAdapter(chemblConfig);
        this.repository = repository;
        this.uniprotEntryRepository = entryRepository;
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
                //check if accession is enzyme
  
                entry = uniprotEntryRepository.findByAccession(accession);
              
                if (entry != null){
                    LOGGER.info(accession + " is enzyme.");
                  
                            computeCompound(entry);
        
                        entry = null;
         
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


    
    private void computeCompound(UniprotEntry e){
         Set<EnzymePortalCompound> compounds = null;
        try {
           
            
            final Collection<String> chemblCompoundIds = getFilteredChemblCompounds();
           
             if (chemblCompoundIds != null){
                 compounds = new HashSet<>();
               
                  for (String chemblCompoundId : chemblCompoundIds) {
                    EnzymePortalCompound chemblEntry = new EnzymePortalCompound();
                    chemblEntry.setCompoundSource(MmDatabase.ChEMBL.name());
                    chemblEntry.setCompoundId(chemblCompoundId);
                   
                    chemblEntry.setCompoundName(chemblAdapter
                            .getPreferredName(chemblCompoundId));
                    chemblEntry.setRelationship(Relationship.between(
                            MmDatabase.UniProt, MmDatabase.ChEMBL)
                            .name());
                    chemblEntry.setUniprotAccession(e);
                    
                 
                   compounds.add(chemblEntry);
                   repository.save(compounds);
                  
                   
              
                }  
                 
                 
                 
                 
             }

        } catch (ChemblAdapterException ex) {
            LOGGER.error(targetIds, ex);
        }
        
        //return compounds;
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
    
        protected String getCurrentXpath() {
        StringBuilder xpath = new StringBuilder("/");
        for (String string : currentContext) {
            xpath.append('/').append(string);
        }
        return xpath.toString();
    }
        
        /**
     * Parses a XML file and indexes/stores cross-references in a mega-map.<br>
     * This method is not thread safe.
     * @param xmlFilePath the XML file to parse
     * @throws java.io.FileNotFoundException if the UniProt XML file is not found
     * 		or not readable.
     * @throws org.xml.sax.SAXException if no default XMLReader can be found or
     * 		instantiated, or exception during parsing.
     * @throws java.io.IOException if the lucene index cannot be opened/created,
     * 		or from the parser.
     */
    public void parse(String xmlFilePath) throws Exception {
          File xmlFile = new File(xmlFilePath);
        parse(new FileInputStream(xmlFile));
    }

    private void parse(InputStream is) throws IOException, SAXException {
       
        try {
           // mm.openMap();
            XMLReader xr = XMLReaderFactory.createXMLReader();
            xr.setContentHandler(this);
            xr.setErrorHandler(this);
            InputSource source = new InputSource(is);
            LOGGER.info("Parsing start");
            xr.parse(source);
            LOGGER.info("Parsing end");
     
        } catch (IOException | SAXException e){
            LOGGER.error("During parsing", e);
          
            throw e;
        }
    }   

}
