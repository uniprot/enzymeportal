package uk.ac.ebi.ep.xml.generator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.bind.JAXBException;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ep.model.EnzymePortalEcNumbers;
import uk.ac.ebi.ep.model.IntenzEnzymes;
import uk.ac.ebi.ep.model.UniprotEntry;
import uk.ac.ebi.ep.model.service.EnzymePortalXmlService;
import uk.ac.ebi.ep.xml.config.XmlConfigParams;
import static uk.ac.ebi.ep.xml.generator.XmlGenerator.logger;
import uk.ac.ebi.ep.xml.model.AdditionalFields;
import uk.ac.ebi.ep.xml.model.CrossReferences;
import uk.ac.ebi.ep.xml.model.Database;
import uk.ac.ebi.ep.xml.model.Entries;
import uk.ac.ebi.ep.xml.model.Entry;
import uk.ac.ebi.ep.xml.model.Field;
import uk.ac.ebi.ep.xml.model.Ref;
import uk.ac.ebi.ep.xml.util.DatabaseName;
import uk.ac.ebi.ep.xml.util.FieldName;
import uk.ac.ebi.ep.xml.validator.EnzymePortalXmlValidator;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
public class EnzymeXML extends XmlGenerator {

    private final XmlConfigParams xmlConfigParams;
    private ForkJoinPool forkJoinPool;

    private static final Integer EC_CLASS_1 = 1;
    private static final Integer EC_CLASS_2 = 2;
    private static final Integer EC_CLASS_3 = 3;
    private static final Integer EC_CLASS_4 = 4;
    private static final Integer EC_CLASS_5 = 5;
    private static final Integer EC_CLASS_6 = 6;

    public EnzymeXML(EnzymePortalXmlService enzymePortalXmlService, XmlConfigParams xmlConfigParams) {
        super(enzymePortalXmlService, xmlConfigParams);
        this.xmlConfigParams = xmlConfigParams;
    }

    private ForkJoinPool getForkJoinPool() {
        if (forkJoinPool == null) {
            forkJoinPool = new ForkJoinPool();
        }

        return forkJoinPool;
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

        CompletableFuture<Stream<Entry>> completableFutureClass_1 = CompletableFuture
                .supplyAsync(() -> findAndProcessEnzymesByEcClass(EC_CLASS_1), getForkJoinPool());
        CompletableFuture<Stream<Entry>> completableFutureClass_2 = CompletableFuture
                .supplyAsync(() -> findAndProcessEnzymesByEcClass(EC_CLASS_2), getForkJoinPool());
        CompletableFuture<Stream<Entry>> completableFutureClass_3 = CompletableFuture
                .supplyAsync(() -> findAndProcessEnzymesByEcClass(EC_CLASS_3), getForkJoinPool());
        CompletableFuture<Stream<Entry>> completableFutureClass_4 = CompletableFuture
                .supplyAsync(() -> findAndProcessEnzymesByEcClass(EC_CLASS_4), getForkJoinPool());
        CompletableFuture<Stream<Entry>> completableFutureClass_5 = CompletableFuture
                .supplyAsync(() -> findAndProcessEnzymesByEcClass(EC_CLASS_5), getForkJoinPool());
        CompletableFuture<Stream<Entry>> completableFutureClass_6 = CompletableFuture
                .supplyAsync(() -> findAndProcessEnzymesByEcClass(EC_CLASS_6), getForkJoinPool());

        CompletableFuture<Stream<Entry>> completableFutureIntenz = CompletableFuture
                .supplyAsync(() -> processIntenzEnzymes(enzymePortalXmlService.findNonTransferredIntenzEnzymesWithNoAcc()), getForkJoinPool());

//        List<Entry> entryList = Stream.of(completableFutureClass_1, completableFutureClass_2,
//                completableFutureClass_3, completableFutureClass_4, completableFutureClass_5,
//                completableFutureClass_6, completableFutureIntenz)
//                .flatMap(CompletableFuture::join)
//                      .collect(Collectors.toList());
//    
        List<Entry> entryList = completableFutureClass_1
                .thenCombineAsync(completableFutureClass_2, (a, b) -> combineEntries(a, b), getForkJoinPool())
                .thenCombineAsync(completableFutureClass_3, (c, d) -> combineEntries(c.stream(), d), getForkJoinPool())
                .thenCombineAsync(completableFutureClass_4, (e, f) -> combineEntries(e.stream(), f), getForkJoinPool())
                .thenCombineAsync(completableFutureClass_5, (g, h) -> combineEntries(g.stream(), h), getForkJoinPool())
                .thenCombineAsync(completableFutureClass_6, (i, j) -> combineEntries(i.stream(), j), getForkJoinPool())
                .thenCombineAsync(completableFutureIntenz, (x, y) -> combineEntries(x.stream(), y), getForkJoinPool())
                .join()
                .stream()
                //.distinct()
                .collect(Collectors.toList());

        int entryCount = entryList.size();
        logger.warn("Number of entries processed : " + entryCount);
        
        Database database = buildDatabaseInfo(entryCount);
        Entries entries = new Entries();

        entries.setEntry(entryList);
        database.setEntries(entries);

        String xmlDirectory = xmlConfigParams.getXmlDir();
        writeXml(database, xmlDirectory, xmlFileLocation);
    }

    private Stream<Entry> findAndProcessEnzymesByEcClass(Integer ecFamily) {
        //int num = 1;
        List<EnzymePortalEcNumbers> enzymes = enzymePortalXmlService.findEnzymesByEcClass(ecFamily);

        List<EnzymePortalEcNumbers> uniqueEnzymes = enzymes.stream().distinct().collect(Collectors.toList());

        List<Entry> entryList = new ArrayList<>();
        uniqueEnzymes.stream().map((enzyme) -> {
            List<UniprotEntry> entries = enzymePortalXmlService.findEnzymesByEcNumberNativeQuery(enzyme.getEcNumber());
            Entry entry = processEntries(entries, enzyme);
            return entry;
        }).forEach((entry) -> {
            entryList.add(entry);
        });

        return entryList.parallelStream();
    }

    private Entry processEntries(List<UniprotEntry> entries, EnzymePortalEcNumbers enzyme) {
        Set<Field> fields = new HashSet<>();
        Set<Ref> refs = new HashSet<>();

        Entry entry = new Entry();

        entry.setId(enzyme.getEcNumber());
        entry.setName(enzyme.getEnzymeName());
        entry.setDescription(enzyme.getCatalyticActivity());
        addEnzymeFamilyField(enzyme.getEcNumber(), fields);
        addCofactorsField(enzyme.getCofactor(), fields);
        //addIntenzCofactorsField(enzyme, fields);
        // addAltNamesField(enzyme, fields);
        entries.stream().parallel().forEach((uniprotEntry) -> {
            addUniprotIdFields(uniprotEntry, fields);
            addProteinNameFields(uniprotEntry, fields);

            addScientificNameFields(uniprotEntry, fields);
            addCommonNameFields(uniprotEntry, fields);
            addGeneNameFields(uniprotEntry, fields);

            addSynonymFields(uniprotEntry, fields);
            addEcSource(enzyme.getEcNumber(), refs);
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

    private Stream<Entry> processIntenzEnzymes(List<IntenzEnzymes> intenzEnzymes) {
        Stream<Entry> entryList = intenzEnzymes.stream().map(enzyme -> {

            Set<Field> seqFields = new HashSet<>();
            Set<Ref> seqRefs = new HashSet<>();

            return processEntries(enzyme, seqFields, seqRefs);

        });
        return entryList;
    }

    private Entry processEntries(IntenzEnzymes enzyme, Set<Field> fields, Set<Ref> refs) {
        Entry entry = new Entry();

        entry.setId(enzyme.getEcNumber());
        entry.setName(enzyme.getEnzymeName());
        entry.setDescription(enzyme.getCatalyticActivity());

        addEnzymeFamilyField(enzyme.getEcNumber(), fields);

        addCofactorsField(enzyme.getCofactor(), fields);
        addAltNamesField(enzyme, fields);
        addSource(enzyme, refs);

        AdditionalFields additionalFields = new AdditionalFields();
        additionalFields.setField(fields);
        entry.setAdditionalFields(additionalFields);

        CrossReferences cr = new CrossReferences();
        cr.setRef(refs);
        entry.setCrossReferences(cr);

        return entry;
    }

    private List<Entry> combineEntries(Stream<Entry> e1, Stream<Entry> e2) {

        Stream<Entry> combinedEntries = Stream.concat(e1, e2);

        return combinedEntries.collect(Collectors.toList());

    }

    //Option 2 stars here ////////////   
    private Stream<Entry> processCoreEnzymes(List<EnzymePortalEcNumbers> enzymes) {
        Stream<Entry> entryList = enzymes.stream().map(enzyme -> {

            List<UniprotEntry> entries = enzymePortalXmlService.findEnzymesByEcNumberNativeQuery(enzyme.getEcNumber());
            // Stream<UniprotEntry> entries = enzymePortalXmlService.streamEnzymesByEcNumber(enzyme.getEcNumber());
            // List<UniprotEntry> entries = new ArrayList<>();
            //try (Stream<UniprotEntry> entries = enzymePortalXmlService.streamEnzymesByEcNumber(enzyme.getEcNumber())) {

            Set<Field> seqFields = new HashSet<>();
            Set<Ref> seqRefs = new HashSet<>();

            return processEntries(entries.stream(), enzyme, seqFields, seqRefs);
            //}

        });//.collect(Collectors.toList());
        return entryList;
    }

    private List<Entry> combineEntries(Stream<Entry> e1, Stream<Entry> e2, Stream<Entry> e3, Stream<Entry> e4, Stream<Entry> e5, Stream<Entry> e6, Stream<Entry> e7) {

        Stream<Entry> combinedEntries = Stream.of(e1, e2, e3, e4, e5, e6, e7)
                .flatMap(x -> x.parallel());

        return combinedEntries.collect(Collectors.toList());

    }

    // @Transactional(readOnly = true)
    //@Override
    public void OLDgenerateXmL(String xmlFileLocation) throws JAXBException {

        // Long countEnzymes = enzymePortalXmlService.countEnzymes();
        //int epEntryCount = countEnzymes.intValue();
        System.out.println("search intenz enzymes ....");

        List<IntenzEnzymes> intenzEnzymes
                = enzymePortalXmlService.findNonTransferredIntenzEnzymesWithNoAcc();
        int intenzEntryCount = intenzEnzymes.size();

        logger.warn("Number of INTENZ enzymes found : " + intenzEntryCount);

      //int num = 1;

        CompletableFuture<List<EnzymePortalEcNumbers>> future1 = CompletableFuture
                .supplyAsync(() -> enzymePortalXmlService.findEnzymesByEcClass(EC_CLASS_1), getForkJoinPool());
        CompletableFuture<List<EnzymePortalEcNumbers>> future2 = CompletableFuture
                .supplyAsync(() -> enzymePortalXmlService.findEnzymesByEcClass(EC_CLASS_2), getForkJoinPool());
        CompletableFuture<List<EnzymePortalEcNumbers>> future3 = CompletableFuture
                .supplyAsync(() -> enzymePortalXmlService.findEnzymesByEcClass(EC_CLASS_3), getForkJoinPool());
        CompletableFuture<List<EnzymePortalEcNumbers>> future4 = CompletableFuture
                .supplyAsync(() -> enzymePortalXmlService.findEnzymesByEcClass(EC_CLASS_4), getForkJoinPool());
        CompletableFuture<List<EnzymePortalEcNumbers>> future5 = CompletableFuture
                .supplyAsync(() -> enzymePortalXmlService.findEnzymesByEcClass(EC_CLASS_5), getForkJoinPool());
        CompletableFuture<List<EnzymePortalEcNumbers>> future6 = CompletableFuture
                .supplyAsync(() -> enzymePortalXmlService.findEnzymesByEcClass(EC_CLASS_6), getForkJoinPool());

        List<EnzymePortalEcNumbers> enzymes = Stream.of(future1, future2, future3, future4, future5, future6)
                //.flatMap(Collection::stream)
                .map(CompletableFuture::join)
                .flatMap(Collection::parallelStream)
                //.flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

//        List<EnzymePortalEcNumbers> enzymes
//                = enzymePortalXmlService.findEnzymes(1)
//                .stream()
//                .distinct()
//                .collect(Collectors.toList());
        int epEntryCount = enzymes.size();

        logger.warn("Number of EP enzymes found : " + epEntryCount);

        int entryCount = epEntryCount + intenzEntryCount;

        logger.warn("Number of enzymes ready to be processed : " + entryCount);

//
        Database database = buildDatabaseInfo(entryCount);

//        List<Entry> entryList = enzymes.stream().map(enzyme -> {
//
//            List<UniprotEntry> entries = enzymePortalXmlService.findEnzymesByEcNumberNativeQuery(enzyme.getEcNumber());
//            Entry processedEntry;
//
//            if (entries.size() <= SEQUENTIAL_THRESHOLD) {
//                Stream<UniprotEntry> sequentialUniProtEntryStream = entries.stream();
//
//                Set<Field> seqFields = new HashSet<>();
//                Set<Ref> seqRefs = new HashSet<>();
//
//                processedEntry = processEntries(sequentialUniProtEntryStream, enzyme, seqFields, seqRefs);
//            } else {
//                Stream<UniprotEntry> parallelUniProtEntryStream = entries.stream().parallel();
//
//                Set<Field> parallelFields = Collections.synchronizedSet(new HashSet<>());
//                Set<Ref> parallelRefs = Collections.synchronizedSet(new HashSet<>());
//
//                processedEntry = getForkJoinPool()
//                        .submit(() -> processEntries(parallelUniProtEntryStream, enzyme, parallelFields, parallelRefs))
//                        .join();
//            }
//
//            return processedEntry;
//        }).collect(Collectors.toList());
        // List<Entry> entryList = processCoreEnzymes(enzymes);
        //add intenzEnzymes
        // List<Entry> intenzEntryList = processIntenzEnzymes(intenzEnzymes);
        //WORKING VERSION
        final ForkJoinPool executorService = new ForkJoinPool();

        CompletableFuture<Stream<Entry>> completableFutureCore = CompletableFuture
                .supplyAsync(() -> processCoreEnzymes(enzymes), executorService);

        CompletableFuture<Stream<Entry>> completableFutureIntenz = CompletableFuture
                .supplyAsync(() -> processIntenzEnzymes(intenzEnzymes), executorService);

        List<Entry> entryList = completableFutureCore
                .thenCombineAsync(completableFutureIntenz, (e1, e2) -> combineEntries(e1, e2), executorService)
                .join();
        //END WORKING VERSION

        Entries entries = new Entries();

        entries.setEntry(entryList);
        database.setEntries(entries);

        String xmlDirectory = xmlConfigParams.getXmlDir();
        writeXml(database, xmlDirectory, xmlFileLocation);
    }

    protected void addCofactorsField(String cofactor, Set<Field> fields) {
        if (cofactor != null) {
            Field field = new Field(FieldName.INTENZ_COFACTORS.getName(), cofactor);
            fields.add(field);
        }
    }

    private void addSource(IntenzEnzymes enzyme, Set<Ref> refs) {
        if (!StringUtils.isEmpty(enzyme.getEcNumber())) {
            Ref xref = new Ref(enzyme.getEcNumber(), DatabaseName.INTENZ.getDbName());
            refs.add(xref);
        }
    }

    private void addEcSource(String ec, Set<Ref> refs) {
        if (!StringUtils.isEmpty(ec)) {
            Ref xref = new Ref(ec, DatabaseName.INTENZ.getDbName());
            refs.add(xref);
        }
    }

    private void addAltNamesField(IntenzEnzymes enzyme, Set<Field> fields) {

        if (!enzyme.getIntenzAltNamesSet().isEmpty()) {

            enzyme.getIntenzAltNamesSet()
                    .stream()
                    .map(altName -> new Field(FieldName.INTENZ_ALT_NAMES.getName(), altName.getAltName()))
                    .forEach(field -> fields.add(field));

        }
    }

    private Entry processEntries(Stream<UniprotEntry> entries, EnzymePortalEcNumbers enzyme, Set<Field> fields, Set<Ref> refs) {
        Entry entry = new Entry();

        entry.setId(enzyme.getEcNumber());
        entry.setName(enzyme.getEnzymeName());
        entry.setDescription(enzyme.getCatalyticActivity());
        addEnzymeFamilyField(enzyme.getEcNumber(), fields);
        addCofactorsField(enzyme.getCofactor(), fields);
        //addIntenzCofactorsField(enzyme, fields);
        // addAltNamesField(enzyme, fields);
        entries.forEach((uniprotEntry) -> {
            addUniprotIdFields(uniprotEntry, fields);
            addProteinNameFields(uniprotEntry, fields);

            addScientificNameFields(uniprotEntry, fields);
            addCommonNameFields(uniprotEntry, fields);
            addGeneNameFields(uniprotEntry, fields);

            addSynonymFields(uniprotEntry, fields);
            addEcSource(enzyme.getEcNumber(), refs);
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

}
