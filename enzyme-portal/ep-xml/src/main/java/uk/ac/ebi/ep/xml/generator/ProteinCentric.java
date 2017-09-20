package uk.ac.ebi.ep.xml.generator;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import javax.xml.bind.JAXBException;
import org.hibernate.SessionFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.xml.StaxWriterCallback;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import uk.ac.ebi.ep.model.ProteinGroups;
import uk.ac.ebi.ep.model.UniprotEntry;
import uk.ac.ebi.ep.model.service.EnzymePortalXmlService;
import uk.ac.ebi.ep.xml.config.XmlConfigParams;
import uk.ac.ebi.ep.xml.generator.protein.ProteinCentricHeader;
import uk.ac.ebi.ep.xml.generator.protein.ProteinXmlFooterCallback;
import uk.ac.ebi.ep.xml.model.AdditionalFields;
import uk.ac.ebi.ep.xml.model.CrossReferences;
import uk.ac.ebi.ep.xml.model.Entry;
import uk.ac.ebi.ep.xml.model.Field;
import uk.ac.ebi.ep.xml.model.Ref;
import uk.ac.ebi.ep.xml.util.PrettyPrintStaxEventItemWriter;
import uk.ac.ebi.ep.xml.util.XmlFileUtils;
import uk.ac.ebi.ep.xml.validator.EnzymePortalXmlValidator;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class ProteinCentric extends XmlGenerator {

    private final static String ROOT_TAG_NAME = "database";
    private final static String QUERY = "SELECT p FROM ProteinGroups p";
    private final XmlConfigParams xmlConfigParams;
    private final SessionFactory sessionFactory;

    public ProteinCentric(EnzymePortalXmlService enzymePortalXmlService, XmlConfigParams xmlConfigParams, SessionFactory sessionFactory) {
        super(enzymePortalXmlService, xmlConfigParams);
        this.xmlConfigParams = xmlConfigParams;
        this.sessionFactory = sessionFactory;

    }

    @Override
    public void validateXML() {
        String ebeyeXSDs = xmlConfigParams.getEbeyeXSDs();
        String proteinCentricXmlDir = xmlConfigParams.getProteinCentricXmlDir();

        if (ebeyeXSDs == null || proteinCentricXmlDir == null) {
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

            EnzymePortalXmlValidator.validateXml(proteinCentricXmlDir, xsdFiles);

        }
    }

    private Long proteinGroupsCount() {
        return enzymePortalXmlService.countProteinGroups();
    }

    private StaxWriterCallback addXmlHeader() {
        String numEntries = proteinGroupsCount().toString();

        return new ProteinCentricHeader(xmlConfigParams.getReleaseNumber(), numEntries, enzymePortalXmlService);
    }

    private Resource resource(String proteinCentricXmlDir) {
        return new FileSystemResource(proteinCentricXmlDir);

    }

    @Override
    public void generateXmL() throws JAXBException {
        //generateXmL(xmlConfigParams.getXmlDir());
        generateXmL(xmlConfigParams.getProteinCentricXmlDir());
    }

    private PrettyPrintStaxEventItemWriter<Entry> getXmlWriter(String locationToWriteXml) throws JAXBException {
        final PrettyPrintStaxEventItemWriter<Entry> xmlWriter = new PrettyPrintStaxEventItemWriter<>();

        XmlFileUtils.createDirectory(locationToWriteXml);
        xmlWriter.setResource(resource(locationToWriteXml));
        xmlWriter.setRootTagName(ROOT_TAG_NAME);
        xmlWriter.setHeaderCallback(addXmlHeader());

        xmlWriter.setMarshaller(springXmlMarshaller(Entry.class));
        xmlWriter.setFooterCallback(new ProteinXmlFooterCallback());
        xmlWriter.setOverwriteOutput(true);
        ExecutionContext executionContext = new ExecutionContext();
        xmlWriter.open(executionContext);
        return xmlWriter;
    }

    @Override
    public void generateXmL(String xmlFileLocation) throws JAXBException {

        int batchSize = xmlConfigParams.getChunkSize();
        Set<Field> fields = Collections.synchronizedSet(new HashSet<>());
        Set<Ref> refs = Collections.synchronizedSet(new HashSet<>());

        final PrettyPrintStaxEventItemWriter<Entry> xmlWriter = getXmlWriter(xmlFileLocation);
        Long numberOfEntries = proteinGroupsCount();

        try (Stream<ProteinGroups> entryStream = enzymePortalXmlService.streamProteinGroupsInBatch(sessionFactory, QUERY, batchSize)) {

            entryStream.forEach(protein -> writeEntry(xmlWriter, protein, fields, refs));

        }

        xmlWriter.close();

        //TODO DELETE ME AFTER TEST
        logger.error("Number of entries processed :  " + entryCounter.get() + " out of " + numberOfEntries);
    }

    AtomicInteger entryCounter = new AtomicInteger(0);//DELETE AFTER TEST

    private void writeEntry(PrettyPrintStaxEventItemWriter<Entry> xmlWriter, ProteinGroups protein, Set<Field> fields, Set<Ref> refs) {
        final Entry entry = processProteinEntries(protein, fields, refs);

        try {
            xmlWriter.write(Arrays.asList(entry));
            entryCounter.getAndIncrement();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private Entry processProteinEntries(ProteinGroups pg, Set<Field> fields, Set<Ref> refs) {
        Entry entry = new Entry();
        entry.setId(pg.getProteinGroupId());
        entry.setName(pg.getProteinName());
        entry.setDescription(pg.getProteinName());
        addFunctionFields(pg, fields);
        addEntryTypeFields(pg, fields);
        addPrimaryProteinField(pg, fields);
        addPDBFields(pg, fields);
        addRelatedSpeciesField(pg, fields);
        pg.getUniprotEntryList()
                .stream()
                .parallel()
                .forEach(uniprotEntry -> addFieldsAndXRefs(uniprotEntry, fields, refs));

        AdditionalFields additionalFields = new AdditionalFields();
        additionalFields.setField(fields);
        entry.setAdditionalFields(additionalFields);

        CrossReferences cr = new CrossReferences();
        cr.setRef(refs);
        entry.setCrossReferences(cr);

        return entry;
    }

    private void addFieldsAndXRefs(UniprotEntry uniprotEntry, Set<Field> fields, Set<Ref> refs) {
        addUniprotIdFields(uniprotEntry, fields);

        addScientificNameFields(uniprotEntry, fields);
        addCommonNameFields(uniprotEntry, fields);
        addGeneNameFields(uniprotEntry, fields);

        addSynonymFields(uniprotEntry, fields);
        addAccessionXrefs(uniprotEntry, refs);

       // addCompoundFieldsAndXrefs(uniprotEntry, fields, refs);
        addCompoundDataFieldsAndXrefs(uniprotEntry, fields, refs);
        addDiseaseFieldsAndXrefs(uniprotEntry, fields, refs);
        addEcXrefs(uniprotEntry, refs);
        addPathwaysXrefs(uniprotEntry, refs);
        addTaxonomyXrefs(uniprotEntry, refs);
    }

}
