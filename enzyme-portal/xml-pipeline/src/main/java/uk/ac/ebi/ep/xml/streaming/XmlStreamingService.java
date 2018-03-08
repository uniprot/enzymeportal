package uk.ac.ebi.ep.xml.streaming;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.bind.JAXBException;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.StaxWriterCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.xml.config.XmlFileProperties;
import uk.ac.ebi.ep.xml.entity.EnzymePortalUniqueEc;
import uk.ac.ebi.ep.xml.entity.UniprotEntry;
import uk.ac.ebi.ep.xml.helper.CustomStaxEventItemWriter;
import uk.ac.ebi.ep.xml.helper.XmlFooterCallback;
import uk.ac.ebi.ep.xml.helper.XmlHeaderCallback;
import uk.ac.ebi.ep.xml.schema.AdditionalFields;
import uk.ac.ebi.ep.xml.schema.CrossReferences;
import uk.ac.ebi.ep.xml.schema.Entry;
import uk.ac.ebi.ep.xml.schema.Field;
import uk.ac.ebi.ep.xml.schema.Ref;
import uk.ac.ebi.ep.xml.service.XmlService;
import uk.ac.ebi.ep.xml.transformer.DataTransformer;
import uk.ac.ebi.ep.xml.util.FieldName;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Service("xmlStreamingService")
public class XmlStreamingService extends DataTransformer {

    private final static String ROOT_TAG_NAME = "database";
    private static final Short EC_CLASS_1 = 1;
    private static final Short EC_CLASS_2 = 2;
    private static final Short EC_CLASS_3 = 3;
    private static final Short EC_CLASS_4 = 4;
    private static final Short EC_CLASS_5 = 5;
    private static final Short EC_CLASS_6 = 6;
    private final XmlService xmlService;
    private final XmlFileProperties xmlFileProperties;

    @Autowired
    public XmlStreamingService(XmlService xmlService, XmlFileProperties xmlFileProperties) {
        super(xmlFileProperties);
        this.xmlService = xmlService;
        this.xmlFileProperties = xmlFileProperties;
    }
    
//    private void paging(){
//         final PageRequest pageable = new PageRequest(0, DEFAULT_PAGE_SIZE, Sort.Direction.DESC, "creationDate");
//
//         Page page = null;
//         
//              int current = page.getNumber() + 1;
//        int begin = Math.max(1, current - 5);
//        int end = Math.min(begin + 10, page.getTotalPages());
//    }

    private List<EnzymePortalUniqueEc> findEnzymesByClass() {

        CompletableFuture<EnzymePortalUniqueEc> future1 = xmlService.findCompletableFutureEnzymesByEcClass(EC_CLASS_1);
        CompletableFuture<EnzymePortalUniqueEc> future2 = xmlService.findCompletableFutureEnzymesByEcClass(EC_CLASS_2);

        CompletableFuture<EnzymePortalUniqueEc> future3 = xmlService.findCompletableFutureEnzymesByEcClass(EC_CLASS_3);

        CompletableFuture<EnzymePortalUniqueEc> future4 = xmlService.findCompletableFutureEnzymesByEcClass(EC_CLASS_4);

        CompletableFuture<EnzymePortalUniqueEc> future5 = xmlService.findCompletableFutureEnzymesByEcClass(EC_CLASS_5);

        CompletableFuture<EnzymePortalUniqueEc> future6 = xmlService.findCompletableFutureEnzymesByEcClass(EC_CLASS_6);

        List<EnzymePortalUniqueEc> enzymes = Stream.of(future1, future2, future3, future4, future5, future6)
                .map(CompletableFuture::join)
                .distinct()
                .collect(Collectors.toList());

        return enzymes;
    }

    private Long enzymeCount() {

        return xmlService.countEnzymes();
    }

    private StaxWriterCallback addXmlHeader() {
        String numEntries = enzymeCount().toString();
        return new XmlHeaderCallback(xmlFileProperties.getReleaseNumber(), numEntries);
    }

    private Resource resource(String xmlDir) {
        return new FileSystemResource(xmlDir);

    }

    protected <T> org.springframework.oxm.Marshaller springXmlMarshaller(Class<T> clazz) throws JAXBException {

        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

        marshaller.setClassesToBeBound(clazz);
        //marshaller.setPackagesToScan("uk.ac.ebi.ep.xml.schema");
        Map<String, Object> jaxbProps = new HashMap<>();
        jaxbProps.put(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setMarshallerProperties(jaxbProps);

        return marshaller;
    }

    //@Override
    @Transactional(readOnly = true)
    public void generateXmL() throws JAXBException {

        generateXmL(xmlFileProperties.getEnzymeCentric());

    }

    private StaxEventItemWriter<Entry> getXmlWriter(String locationToWriteXml) throws JAXBException {
        final StaxEventItemWriter<Entry> xmlWriter = new CustomStaxEventItemWriter<>();

        //XmlFileUtils.createDirectory(locationToWriteXml);
        // xmlWriter.setName("CORE-XML-WRITER");
        xmlWriter.setResource(resource(locationToWriteXml));
        xmlWriter.setRootTagName(ROOT_TAG_NAME);
        xmlWriter.setHeaderCallback(addXmlHeader());

        xmlWriter.setSaveState(true);
        xmlWriter.setTransactional(false);
        xmlWriter.setShouldDeleteIfEmpty(true);
        xmlWriter.setMarshaller(springXmlMarshaller(Entry.class));

        xmlWriter.setFooterCallback(new XmlFooterCallback());
        xmlWriter.setOverwriteOutput(true);

        ExecutionContext executionContext = new ExecutionContext();
        xmlWriter.open(executionContext);
        return xmlWriter;
    }

    // @Override
    @Transactional(readOnly = true)
    public void generateXmL(String xmlFileLocation) throws JAXBException {

        StaxEventItemWriter<Entry> xmlWriter = getXmlWriter(xmlFileLocation);

        //String ec = "6.1.2.2";
        try (Stream<EnzymePortalUniqueEc> entryStream = xmlService.streamEnzymes()) {
            // System.out.println("STREAMING ....");
            entryStream.forEach(enzyme -> writeEntry(xmlWriter, enzyme));

        }

//        System.out.println("submit job.....");
//             try (Stream<EnzymePortalUniqueEc> entryStream = xmlService.streamEnzymesByFamily(EC_CLASS_6)) {
//            System.out.println("STREAMING ....CLASS ");
//            entryStream.limit(2).forEach(enzyme -> writeEntry(xmlWriter, enzyme));
//            //return entryStream;
//        }
        xmlWriter.close();
    }

    @Transactional(readOnly = true)
    public void generateXmL_TODO(String xmlFileLocation) throws JAXBException {

        StaxEventItemWriter<Entry> xmlWriter = getXmlWriter(xmlFileLocation);
      // List<Short> families = Arrays.asList( EC_CLASS_2, EC_CLASS_3, EC_CLASS_4, EC_CLASS_6);
        List<Short> families = Arrays.asList(EC_CLASS_1, EC_CLASS_2, EC_CLASS_3, EC_CLASS_4, EC_CLASS_5, EC_CLASS_6);

        families.parallelStream()
                //.stream()
                //.parallel()
                .forEach(family ->  streamByEnzymeFamily(xmlWriter, family));

//        try {
//            xmlWriter.write(entries);
//        } catch (XmlMappingException | IOException ex) {
//            logger.error(ex.getMessage(), ex);
//        }
    }

    @Transactional(readOnly = true)
    private void streamByEnzymeFamily(StaxEventItemWriter<Entry> xmlWriter, Short ecFamily) {
 
        try (Stream<EnzymePortalUniqueEc> entryStream = xmlService.streamEnzymesByFamily(ecFamily)) {
             System.out.println("STREAMING ....CLASS " + ecFamily);
   
            entryStream.limit(1).forEach(enzyme -> writeEntry(xmlWriter, enzyme));
 
        }


    }

    
        private void writeEntry(StaxEventItemWriter<Entry> xmlWriter, EnzymePortalUniqueEc enzyme) {
        final Entry entry = processEntries(enzyme);

        try {
            xmlWriter.write(Arrays.asList(entry));
        } catch (XmlMappingException | IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public Entry processEntries(EnzymePortalUniqueEc enzyme) {
        Set<Field> fields = new HashSet<>();
        Set<Ref> refs = new HashSet<>();
        Entry entry = new Entry();
        entry.setId(enzyme.getEcNumber());
        entry.setName(enzyme.getEnzymeName());
        entry.setDescription(enzyme.getCatalyticActivity());

        addEnzymeFamilyField(enzyme.getEcNumber(), fields);

        addCofactorsField(enzyme.getCofactor(), fields);

        enzyme.getEnzymePortalEcNumbersSet()
                .stream()
                .parallel()
                .forEach(ec -> processUniprotEntry(ec.getUniprotAccession(), fields, refs));

        addAltNamesField(enzyme, fields);
        addEcSource(enzyme.getEcNumber(), refs);

        AdditionalFields additionalFields = new AdditionalFields();
        additionalFields.setField(fields);
        entry.setAdditionalFields(additionalFields);

        CrossReferences cr = new CrossReferences();
        cr.setRef(refs);
        entry.setCrossReferences(cr);

        return entry;

    }

    private void processUniprotEntry(UniprotEntry uniprotEntry, Set<Field> fields, Set<Ref> refs) {
        addUniprotIdFields(uniprotEntry, fields);
        addProteinNameFields(uniprotEntry, fields);

        addScientificNameFields(uniprotEntry, fields);
        addCommonNameFields(uniprotEntry, fields);
        addGeneNameFields(uniprotEntry, fields);

        addSynonymFields(uniprotEntry, fields);
        //addSource(enzyme, refs);
        addAccessionXrefs(uniprotEntry, refs);
        addTaxonomyXrefs(uniprotEntry, refs);

        addCompoundFieldsAndXrefs(uniprotEntry, fields, refs);
        addDiseaseFieldsAndXrefs(uniprotEntry, fields, refs);
        addPathwaysXrefs(uniprotEntry, refs);
    }

    private void addAltNamesField(EnzymePortalUniqueEc enzyme, Set<Field> fields) {

        if (!enzyme.getIntenzAltNamesSet().isEmpty()) {

            enzyme.getIntenzAltNamesSet()
                    .stream()
                    .map(altName -> new Field(FieldName.INTENZ_ALT_NAMES.getName(), altName.getAltName()))
                    .forEach(field -> fields.add(field));

        }
    }
}
