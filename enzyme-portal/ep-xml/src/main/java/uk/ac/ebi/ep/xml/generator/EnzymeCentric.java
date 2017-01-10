package uk.ac.ebi.ep.xml.generator;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.bind.JAXBException;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ep.data.domain.IntenzEnzymes;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.service.EnzymePortalXmlService;
import uk.ac.ebi.ep.xml.config.XmlConfigParams;
import uk.ac.ebi.ep.xml.model.AdditionalFields;
import uk.ac.ebi.ep.xml.model.CrossReferences;
import uk.ac.ebi.ep.xml.model.Database;
import uk.ac.ebi.ep.xml.model.Entries;
import uk.ac.ebi.ep.xml.model.Entry;
import uk.ac.ebi.ep.xml.model.Field;
import uk.ac.ebi.ep.xml.model.Ref;
import uk.ac.ebi.ep.xml.util.DatabaseName;
import uk.ac.ebi.ep.xml.validator.EnzymePortalXmlValidator;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class EnzymeCentric extends XmlGenerator {

    private final XmlConfigParams xmlConfigParams;
    private ForkJoinPool forkJoinPool;
    private static final int SEQUENTIAL_THRESHOLD = 10_000;

    public EnzymeCentric(EnzymePortalXmlService enzymePortalXmlService, XmlConfigParams xmlConfigParams) {
        super(enzymePortalXmlService, xmlConfigParams);
        this.xmlConfigParams = xmlConfigParams;
    }

    @Override
    public void validateXML() {
        String ebeyeXSDs = xmlConfigParams.getEbeyeXSDs();
        String enzymeCentricXmlDir = xmlConfigParams.getEnzymeCentricXmlDir();

        if (ebeyeXSDs == null || enzymeCentricXmlDir == null) {
            try {
                String msg
                        = "Xsd files or XML directory cannot be Null. Please ensure that ep-xml-config.properties is in"
                        + " the classpath.";
                throw new FileNotFoundException(msg);
            } catch (FileNotFoundException ex) {
                logger.error(ex.getMessage(), ex);
            }
        } else {
            String[] xsdFiles = ebeyeXSDs.split(",");

            EnzymePortalXmlValidator.validateXml(enzymeCentricXmlDir, xsdFiles);
        }
    }

    @Override
    public void generateXmL() throws JAXBException {
        generateXmL(xmlConfigParams.getEnzymeCentricXmlDir());
    }

    @Override
    public void generateXmL(String xmlFileLocation) throws JAXBException {
       logger.warn("about to query database for enzymes "+ enzymePortalXmlService);
//        List<IntenzEnzymes> enzymes
//                = enzymePortalXmlService.findAllIntenzEnzymes().stream().sorted().collect(Collectors.toList());  
        
          List<IntenzEnzymes> enzymes
                = enzymePortalXmlService.findNonTransferredEnzymes();
             
        int entryCount = enzymes.size();

        logger.warn("Number of Intenz enzymes ready to be processed : " + entryCount);
       
        Database database = buildDatabaseInfo(entryCount);

        List<Entry> entryList = enzymes.stream().map(enzyme -> {
            List<UniprotEntry> entries = enzymePortalXmlService.findEnzymesByEcNumberNativeQuery(enzyme.getEcNumber());
            Entry processedEntry;

            if (entries.size() <= SEQUENTIAL_THRESHOLD) {
                Stream<UniprotEntry> sequentialUniProtEntryStream = entries.stream();

                Set<Field> seqFields = new HashSet<>();
                Set<Ref> seqRefs = new HashSet<>();

                processedEntry = processEntries(sequentialUniProtEntryStream, enzyme, seqFields, seqRefs);
            } else {
                Stream<UniprotEntry> parallelUniProtEntryStream = entries.stream().parallel();

                Set<Field> parallelFields = Collections.synchronizedSet(new HashSet<>());
                Set<Ref> parallelRefs = Collections.synchronizedSet(new HashSet<>());

                processedEntry = getForkJoinPool()
                        .submit(() -> processEntries(parallelUniProtEntryStream, enzyme, parallelFields, parallelRefs))
                        .join();
            }

            return processedEntry;
        }).collect(Collectors.toList());

        Entries entries = new Entries();
        entries.setEntry(entryList);
        database.setEntries(entries);

        String xmlDirectory = xmlConfigParams.getXmlDir();
        writeXml(database, xmlDirectory, xmlFileLocation);
    }

    private Entry processEntries(Stream<UniprotEntry> entries, IntenzEnzymes enzyme, Set<Field> fields, Set<Ref> refs) {
        Entry entry = new Entry();

        entry.setId(enzyme.getEcNumber());
        entry.setName(enzyme.getEnzymeName());
        entry.setDescription(enzyme.getCatalyticActivity());
        addEnzymeFamilyField(enzyme.getEcNumber(), fields);
        entries.forEach((uniprotEntry) -> {
            addUniprotIdFields(uniprotEntry, fields);
            addProteinNameFields(uniprotEntry, fields);

            addScientificNameFields(uniprotEntry, fields);
            addCommonNameFields(uniprotEntry, fields);
            addGeneNameFields(uniprotEntry, fields);

            addSynonymFields(uniprotEntry, fields);
            addSource(enzyme, refs);
            addAccessionXrefs(uniprotEntry, refs);
            addTaxonomyXrefs(uniprotEntry, refs);

            addCompoundFieldsAndXrefs(uniprotEntry, fields, refs);
            addDiseaseFieldsAndXrefs(uniprotEntry, fields, refs);
            addPathwaysXrefs(uniprotEntry, refs);
        });

        AdditionalFields additionalFields = new AdditionalFields();
        additionalFields.setField(fields);
        entry.setAdditionalFields(additionalFields);

        CrossReferences cr = new CrossReferences();
        cr.setRef(refs);
        entry.setCrossReferences(cr);

        return entry;
    }

    private void addSource(IntenzEnzymes enzyme, Set<Ref> refs) {
        if (!StringUtils.isEmpty(enzyme.getEcNumber())) {
            Ref xref = new Ref(enzyme.getEcNumber(), DatabaseName.INTENZ.getDbName());
            refs.add(xref);
        }
    }

    private ForkJoinPool getForkJoinPool() {
        if (forkJoinPool == null) {
            forkJoinPool = new ForkJoinPool();
        }

        return forkJoinPool;
    }
}
