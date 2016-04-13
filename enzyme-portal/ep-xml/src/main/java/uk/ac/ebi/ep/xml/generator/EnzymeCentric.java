package uk.ac.ebi.ep.xml.generator;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
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
    private static final int SEQUENCE_THRESHOLD = 10_000;

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
        List<Entry> entryList = new LinkedList<>();

        List<IntenzEnzymes> enzymes
                = enzymePortalXmlService.findAllIntenzEnzymes().stream().sorted().collect(Collectors.toList());

        int entryCount = enzymes.size();

        logger.info("Number of Intenz enzymes ready to be processed : " + entryCount);

        Database database = buildDatabaseInfo(entryCount);

        enzymes.forEach((enzyme) -> {
            Entry entry = new Entry();

            entry.setId(enzyme.getEcNumber());
            entry.setName(enzyme.getEnzymeName());
            entry.setDescription(enzyme.getCatalyticActivity());

            List<UniprotEntry> entries = enzymePortalXmlService.findEnzymesByEcNumberNativeQuery(enzyme.getEcNumber());
            if (entries.size() <= SEQUENCE_THRESHOLD) {

                Entry processedEntry = processEntriesInSequence(entries, enzyme, entry);
                entryList.add(processedEntry);
            } else {
                Entry processedEntry = getForkJoinPool()
                        .submit(() -> processEntriesInParallel(entries, enzyme, entry))
                        .join();
                entryList.add(processedEntry);
            }

        });

        Entries entries = new Entries();
        entries.setEntry(entryList);
        database.setEntries(entries);

        //write xml
        writeXml(database, xmlFileLocation);

    }

    private Entry processEntriesInParallel(List<UniprotEntry> entries, IntenzEnzymes enzyme, Entry entry) {
        AdditionalFields additionalFields = new AdditionalFields();
        Set<Field> fields = Collections.synchronizedSet(new HashSet<>());
        Set<Ref> refs = Collections.synchronizedSet(new HashSet<>());
        CrossReferences cr = new CrossReferences();

        entries.stream().parallel().forEach((uniprotEntry) -> {
            addUniprotIdFields(uniprotEntry, fields);
            addProteinNameFields(uniprotEntry, fields);

            addScientificNameFields(uniprotEntry, fields);

            addSynonymFields(uniprotEntry, fields);
            addSource(enzyme, refs);
            addAccessionXrefs(uniprotEntry, refs);

            addCompoundFieldsAndXrefs(uniprotEntry, fields, refs);

            addDiseaseFields(uniprotEntry, fields);
        });
        additionalFields.setField(fields);
        entry.setAdditionalFields(additionalFields);
        cr.setRef(refs);
        entry.setCrossReferences(cr);

        return entry;
    }

    private Entry processEntriesInSequence(List<UniprotEntry> entries, IntenzEnzymes enzyme, Entry entry) {
        Set<Field> fields = new HashSet<>();
        Set<Ref> refs = new HashSet<>();
        AdditionalFields additionalFields = new AdditionalFields();
        CrossReferences cr = new CrossReferences();

        entries.stream().forEach((uniprotEntry) -> {
            addUniprotIdFields(uniprotEntry, fields);
            addProteinNameFields(uniprotEntry, fields);
            addScientificNameFields(uniprotEntry, fields);

            addSynonymFields(uniprotEntry, fields);
            addSource(enzyme, refs);
            addAccessionXrefs(uniprotEntry, refs);
            addCompoundFieldsAndXrefs(uniprotEntry, fields, refs);
            addDiseaseFields(uniprotEntry, fields);

        });
        additionalFields.setField(fields);
        cr.setRef(refs);
        entry.setAdditionalFields(additionalFields);
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
