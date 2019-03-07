package uk.ac.ebi.ep.xml.transformer;

import cyclops.futurestream.LazyReact;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import uk.ac.ebi.ep.xml.entity.EnzymeCatalyticActivity;
import uk.ac.ebi.ep.xml.entity.protein.PrimaryProtein;
import uk.ac.ebi.ep.xml.entity.protein.ProteinEcNumbers;
import uk.ac.ebi.ep.xml.entity.protein.ProteinGroups;
import uk.ac.ebi.ep.xml.entity.protein.UniprotEntry;
import uk.ac.ebi.ep.xml.schema.AdditionalFields;
import uk.ac.ebi.ep.xml.schema.CrossReferences;
import uk.ac.ebi.ep.xml.schema.Entry;
import uk.ac.ebi.ep.xml.schema.Field;
import uk.ac.ebi.ep.xml.schema.FieldAndXref;
import uk.ac.ebi.ep.xml.schema.Ref;
import uk.ac.ebi.ep.xml.util.DatabaseName;
import uk.ac.ebi.ep.xml.util.FieldName;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Slf4j
public class ProteinGroupsProcessor extends Transformer implements ItemProcessor<ProteinGroups, Entry> {

    protected static final String REVIEWED = "reviewed";
    protected static final String UNREVIEWED = "unreviewed";
    private final AtomicInteger count = new AtomicInteger(1);

//    @Override
//    public Entry process(ProteinGroups proteinGroups) throws Exception {
//        AdditionalFields additionalFields = new AdditionalFields();
//           CopyOnWriteArraySet<Field> fields = new CopyOnWriteArraySet<>();
//         CopyOnWriteArraySet<Ref> refs = new CopyOnWriteArraySet<>();
////        Set<Field> fields = new HashSet<>();
////        Set<Ref> refs = new HashSet<>();
//        Entry entry = new Entry();
//
//        entry.setId(proteinGroups.getProteinGroupId());
//        entry.setName(proteinGroups.getProteinName());
//        entry.setDescription(proteinGroups.getProteinName());
//
//        addPrimaryProtein(proteinGroups, fields, refs);
//
//        additionalFields.setField(fields);
//        entry.setAdditionalFields(additionalFields);
//
//        CrossReferences cr = new CrossReferences();
//        cr.setRef(refs);
//        entry.setCrossReferences(cr);
//
//        return entry;
//    }
    @Override
    public Entry process(ProteinGroups proteinGroups) throws Exception {
        AdditionalFields additionalFields = new AdditionalFields();
        CrossReferences cr = new CrossReferences();
        // CopyOnWriteArraySet<Field> fields = new CopyOnWriteArraySet<>();
        //CopyOnWriteArraySet<Ref> refs = new CopyOnWriteArraySet<>();
        Set<Field> fields = new HashSet<>();
        Set<Ref> refs = new HashSet<>();
        FieldAndXref fieldAndXref = new FieldAndXref(fields, refs);
        Entry entry = new Entry();

        entry.setId(proteinGroups.getProteinGroupId());
        entry.setName(proteinGroups.getProteinName());
        entry.setDescription(proteinGroups.getProteinName());

        FieldAndXref result = addPrimaryProtein(proteinGroups, fields, refs, fieldAndXref);

        additionalFields.setField(result.getField());
        cr.setRef(result.getRef());

        entry.setAdditionalFields(additionalFields);
        entry.setCrossReferences(cr);

//        additionalFields.setField(fields);
//        entry.setAdditionalFields(additionalFields);
//
//        //CrossReferences cr = new CrossReferences();
//        cr.setRef(refs);
//        entry.setCrossReferences(cr);
        return entry;
    }

    private Set<Field> addRelatedSpeciesField(PrimaryProtein primaryProtein, UniprotEntry uniprotEntry, final Set<Field> fields) {

        if (uniprotEntry.getRelatedProteinsId().getRelProtInternalId() == primaryProtein.getRelatedProteinsId()) {
            List<String> specieList = Stream.of(uniprotEntry.getAccession() + ";" + uniprotEntry.getCommonName() + ";" + uniprotEntry.getScientificName() + ";" + uniprotEntry.getExpEvidenceFlag() + ";" + uniprotEntry.getTaxId())
                    .collect(Collectors.toList());
            if (!specieList.isEmpty()) {
                String rs = String.join(" | ", specieList);

                String rsField = StringUtils.removeEnd(rs, " | ");

                Field relatedSpeciesField = new Field(FieldName.RELATED_SPECIES.getName(), rsField);
                fields.add(relatedSpeciesField);
            }
        }
        return fields;
    }

    private void addRelatedSpeciesFieldX(PrimaryProtein primaryProtein, UniprotEntry uniprotEntry, final Set<Field> fields) {

        if (uniprotEntry.getRelatedProteinsId().getRelProtInternalId() == primaryProtein.getRelatedProteinsId()) {
            List<String> specieList = Stream.of(uniprotEntry.getAccession() + ";" + uniprotEntry.getCommonName() + ";" + uniprotEntry.getScientificName() + ";" + uniprotEntry.getExpEvidenceFlag() + ";" + uniprotEntry.getTaxId())
                    .collect(Collectors.toList());
            if (!specieList.isEmpty()) {
                String rs = String.join(" | ", specieList);

                String rsField = StringUtils.removeEnd(rs, " | ");

                Field relatedSpeciesField = new Field(FieldName.RELATED_SPECIES.getName(), rsField);
                fields.add(relatedSpeciesField);
            }
        }

    }

    @Deprecated
    private void addRelatedSpeciesField(PrimaryProtein primaryProtein, List<UniprotEntry> entries, final Set<Field> fields) {
        // private void addRelatedSpeciesField( List<UniprotEntry> entries, final Set<Field> fields) {

        List<String> specieList
                = entries
                        .stream()
                        .filter((uniprotEntry) -> (uniprotEntry.getRelatedProteinsId().getRelProtInternalId() == primaryProtein.getRelatedProteinsId()))
                        .map(u -> (u.getAccession() + ";" + u.getCommonName() + ";" + u.getScientificName() + ";" + u.getExpEvidenceFlag() + ";" + u.getTaxId()))
                        .distinct()
                        .collect(Collectors.toList());

        if (!specieList.isEmpty()) {
            String rs = String.join(" | ", specieList);

            String rsField = StringUtils.removeEnd(rs, " | ");

            Field relatedSpeciesField = new Field(FieldName.RELATED_SPECIES.getName(), rsField);
            fields.add(relatedSpeciesField);
        }
    }

    private FieldAndXref addPrimaryProtein(ProteinGroups proteinGroups, Set<Field> fields, Set<Ref> refs, FieldAndXref fieldAndXref) {

        PrimaryProtein primaryProtein = proteinGroups.getPrimaryProtein();
        if (primaryProtein != null) {
            addPrimaryProteinField(primaryProtein, fields);
            addPrimaryImage(primaryProtein, fields);

            addEntryTypeFields(primaryProtein, fields);
            addPrimaryFunctionFields(primaryProtein, fields);
            List<UniprotEntry> entries = proteinGroups.getUniprotEntryList();
            int numEntry = entries.size();
            log.warn(proteinGroups.getProteinGroupId() + " Number of proteints to process " + numEntry + " count : " + count.getAndIncrement());
            //log.info("Available Processor " + Runtime.getRuntime().availableProcessors());

            //addRelatedSpeciesField(primaryProtein, entries, fields);
            //for (UniprotEntry uniprotEntry : relatedProteins) {// TODO commented out due to uniprot_family issues where found in enzyme-centric but zero in protein-centric
            //addProteinCentricFields(proteinGroups, entries, fields, refs);
            // entries.stream()
            // .forEach(uniprotEntry -> computeEntry(proteinGroups, uniprotEntry, fields, refs, fieldAndXref));
          
            if(numEntry ==0){
              return fieldAndXref;  
            }
        
            if(numEntry > 1_000){
            //splitAndProcess(entries, proteinGroups, fields, refs, fieldAndXref);
            parallel(entries, proteinGroups, fields, refs, fieldAndXref); 
            }else{
              parallelStream(entries, proteinGroups, fields, refs, fieldAndXref);  
            }
            
            
    

            return fieldAndXref;

        }

        return fieldAndXref;
    }

    private void parallelStream(List<UniprotEntry> entries, ProteinGroups proteinGroups, Set<Field> fields, Set<Ref> refs, FieldAndXref fieldAndXref) {

        entries.parallelStream().forEach(uniprotEntry -> computeEntry(proteinGroups, uniprotEntry, fields, refs, fieldAndXref));

    }

    private void parallel(List<UniprotEntry> entries, ProteinGroups proteinGroups, Set<Field> fields, Set<Ref> refs, FieldAndXref fieldAndXref) {
        // System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "256");
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        //LazyReact builder = new LazyReact(forkJoinPool, true, MaxActive.CPU);

        // LazyReact builder = new LazyReact(forkJoinPool, true, MaxActive.IO);
        LazyReact builder = new LazyReact(100, forkJoinPool);
        //LazyReact builder = new LazyReact(100, 100);
        builder.autoOptimizeOn().async()
                .from(entries)
                .grouped(10)
                .flatMapStream(x -> x.stream().parallel())
                .parallel()
                .map(uniprotEntry -> computeEntry(proteinGroups, uniprotEntry, fields, refs, fieldAndXref))
                //.peek(p->System.out.println(" POOL "+ forkJoinPool))
                .join();

        forkJoinPool.shutdown();
    }

    private void splitAndProcess(List<UniprotEntry> entries, ProteinGroups proteinGroups, Set<Field> fields, Set<Ref> refs, FieldAndXref fieldAndXref) {
        //ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        //ForkJoinPool forkJoinPool = new ForkJoinPool(96);// ForkJoinPool.commonPool();
        List<List<UniprotEntry>> chunks = ListUtils.partition(entries, entries.size() / 10);

        List<CompletableFuture<Void>> cfList = new ArrayList<>();
        for (int x = 0; x < chunks.size(); x++) {
            final int index = x;
            CompletableFuture<Void> task = CompletableFuture
                    .runAsync(() -> chunks.get(index)
                    .parallelStream()
                    //.peek(p->System.out.println(" POOL "+ forkJoinPool))
                    .forEach(uniprotEntry -> computeEntry(proteinGroups, uniprotEntry, fields, refs, fieldAndXref)));
            cfList.add(task);
        }
        log.warn("Number of tasks submitted " + cfList.size());

        CompletableFuture<Void> allCompletableFuture = CompletableFuture.allOf(cfList.toArray(new CompletableFuture<?>[0]));
        allCompletableFuture.join();

        // forkJoinPool.shutdown();
    }

    private FieldAndXref computeEntry(ProteinGroups proteinGroups, UniprotEntry uniprotEntry, Set<Field> fields, Set<Ref> refs, FieldAndXref fieldAndXref) {

        //related protein
        addRelatedSpeciesField(proteinGroups.getPrimaryProtein(), uniprotEntry, fields);
        addEnzymeFamilyToProteinField(uniprotEntry.getEnzymePortalEcNumbersSet(), fields);

        addPrimaryEntities(proteinGroups, uniprotEntry, fields);

        addScientificNameFields(uniprotEntry.getScientificName(), fields);
        addCommonNameFields(uniprotEntry.getCommonName(), fields);

        addUniprotFamilyFieldsAndXrefs(uniprotEntry.getUniprotFamiliesSet(), fields, refs, fieldAndXref);

        addAccessionXrefs(uniprotEntry.getAccession(), refs);
        addEcXrefs(uniprotEntry.getEnzymePortalEcNumbersSet(), refs);

        addPathwaysXrefs(uniprotEntry.getEnzymePortalPathwaysSet(), refs);
        addTaxonomyXrefs(uniprotEntry.getTaxId(), refs);
        addReactionXrefs(uniprotEntry.getEnzymePortalReactionSet(), refs);
        addReactantFieldsAndXrefs(uniprotEntry.getEnzymePortalReactantSet(), fields, refs, fieldAndXref);

        addCompoundDataFieldsAndXrefs(uniprotEntry.getEnzymePortalCompoundSet(), fields, refs);

        addDiseaseFieldsAndXrefs(uniprotEntry.getEnzymePortalDiseaseSet(), fields, refs);

        fieldAndXref.setField(fields);
        fieldAndXref.setRef(refs);

        return fieldAndXref;
    }

//    private void addProteinCentricFields(ProteinGroups proteinGroups, List<UniprotEntry> entries, CopyOnWriteArraySet<Field> fields, CopyOnWriteArraySet<Ref> refs) {
//        log.warn(proteinGroups.getProteinGroupId() + " Number of proteints to process " + entries.size() + " count : " + count.getAndIncrement());
//
//        entries.stream()
//                .parallel()
//                .forEach(uniprotEntry -> processEntries(proteinGroups, uniprotEntry, fields, refs));
//
//    }
    //synchronized
//    private synchronized void processEntries(ProteinGroups proteinGroups, UniprotEntry uniprotEntry, CopyOnWriteArraySet<Field> fields, CopyOnWriteArraySet<Ref> refs) {
//        addPrimaryEntities(proteinGroups, uniprotEntry, fields);
//
//        addScientificNameFields(uniprotEntry.getScientificName(), fields);
//        addCommonNameFields(uniprotEntry.getCommonName(), fields);
//
//        addUniprotFamilyFieldsAndXrefs(uniprotEntry.getUniprotFamiliesSet(), fields, refs);
//        addAccessionXrefs(uniprotEntry.getAccession(), refs);
//
//        addCompoundDataFieldsAndXrefs(uniprotEntry.getEnzymePortalCompoundSet(), fields, refs);
//
//        addDiseaseFieldsAndXrefs(uniprotEntry.getEnzymePortalDiseaseSet(), fields, refs);
//        addEcXrefs(uniprotEntry.getEnzymePortalEcNumbersSet(), refs);
//        addEnzymeFamilyToProteinField(uniprotEntry.getEnzymePortalEcNumbersSet(), fields);
//        addPathwaysXrefs(uniprotEntry.getEnzymePortalPathwaysSet(), refs);
//        addTaxonomyXrefs(uniprotEntry.getTaxId(), refs);
//        addReactionXrefs(uniprotEntry.getEnzymePortalReactionSet(), refs);
//        addReactantFieldsAndXrefs(uniprotEntry.getEnzymePortalReactantSet(), fields, refs);
//    }
    private void addPrimaryEntities(ProteinGroups proteinGroups, UniprotEntry uniprotEntry, Set<Field> fields) {
        if (uniprotEntry.getAccession().equals(proteinGroups.getPrimaryProtein().getAccession())) {

            addPrimarySynonymFields(uniprotEntry, proteinGroups.getProteinName(), fields);
            addPrimaryEc(uniprotEntry, fields);
            addPrimaryCatalyticActivityFields(uniprotEntry, fields);
            addPrimaryGeneNameFields(uniprotEntry, fields);
        }
    }

    private void addEnzymeFamilyToProteinField(Set<ProteinEcNumbers> enzymes, Set<Field> fields) {

        enzymes
                .stream()
                .map(ec -> new Field(FieldName.ENZYME_FAMILY.getName(), ec.getFamily()))
                .forEach((field) -> fields.add(field));

    }

    private void addEcXrefs(Set<ProteinEcNumbers> enzymes, Set<Ref> refs) {

        enzymes
                .stream()
                .map(ecNumbers -> new Ref(ecNumbers.getEcNumber(), DatabaseName.INTENZ.getDbName()))
                .forEach(xref -> refs.add(xref));

    }

    private void addPrimaryProteinField(PrimaryProtein primaryProtein, final Set<Field> fields) {

        if (primaryProtein != null) {
            Field primaryAccessionfield = new Field(FieldName.PRIMARY_ACCESSION.getName(), primaryProtein.getAccession());
            fields.add(primaryAccessionfield);
            String commonName = primaryProtein.getCommonName();
            if (commonName == null) {
                commonName = primaryProtein.getScientificName();
            }
            Field primaryOganismfield = new Field(FieldName.PRIMARY_ORGANISM.getName(), " " + commonName);
            fields.add(primaryOganismfield);

        }

    }

    private void addPrimaryFunctionFields(PrimaryProtein primaryProtein, Set<Field> fields) {

        if (primaryProtein != null) {
            if (primaryProtein.getFunction() != null && !StringUtils.isEmpty(primaryProtein.getFunction())) {
                Field primaryFunctionfield = new Field(FieldName.FUNCTION.getName(), primaryProtein.getFunction());

                fields.add(primaryFunctionfield);
            }
        }

    }

    private void addPrimaryImage(PrimaryProtein primaryProtein, Set<Field> fields) {

        Character hasPdbFlag = 'Y';

        if (primaryProtein.getPdbFlag().equals(hasPdbFlag)) {
            String pdbId = primaryProtein.getPdbId() + "|" + primaryProtein.getPdbSpecies() + "|" + primaryProtein.getPdbLinkedAcc();
            Field pdbfield = new Field(FieldName.PRIMARY_IMAGE.getName(), pdbId);
            fields.add(pdbfield);
        }

    }

    private void addEntryTypeFields(PrimaryProtein primaryProtein, Set<Field> fields) {

        BigInteger type = primaryProtein.getEntryType();

        if (type != null) {
            String entryType = String.valueOf(type);
            Field entryTypefield = new Field(FieldName.ENTRY_TYPE.getName(), entryType);

            fields.add(entryTypefield);
        }

    }

    private void addPrimarySynonymFields(UniprotEntry entry, String proteinName, Set<Field> fields) {

        if (entry.getSynonymNames() != null && proteinName != null) {

            Optional<String> synonymName = Optional.ofNullable(entry.getSynonymNames());
            computeSynonymsAndBuildFields(synonymName.orElse(""), proteinName, fields);

        }

    }

    private void addPrimaryGeneNameFields(UniprotEntry entry, Set<Field> fields) {

        entry.getEntryToGeneMappingSet()
                .stream()
                .map(geneMapping -> new Field(FieldName.GENE_NAME.getName(), geneMapping.getGeneName()))
                .forEach(field -> fields.add(field));

    }

    private void addPrimaryCatalyticActivityFields(UniprotEntry entry, Set<Field> fields) {

        Set<EnzymeCatalyticActivity> activities
                = entry.getEnzymeCatalyticActivitySet();

        if (!activities.isEmpty()) {

            activities.stream().map(activity -> new Field(FieldName.CATALYTIC_ACTIVITY.getName(), activity.getCatalyticActivity()))
                    .forEach((primaryActivityfield) -> fields.add(primaryActivityfield));

        }

    }

    private void addPrimaryEc(UniprotEntry entry, Set<Field> fields) {

        entry.getEnzymePortalEcNumbersSet()
                .stream()
                .map(ec -> new Field(FieldName.EC.getName(), ec.getEcNumber()))
                .forEach(ecfield -> fields.add(ecfield));

    }

    private void addStatus(Short type, Set<Field> fields) {
        if (type != null) {
            int entryType = type.intValue();

            if (entryType == 0) {
                Field field = new Field(FieldName.STATUS.getName(), REVIEWED);
                fields.add(field);
            }
            if (entryType == 1) {
                Field field = new Field(FieldName.STATUS.getName(), UNREVIEWED);
                fields.add(field);
            }

        }
    }

    //TODO
//        private void addProteinCentricFields(Entry entry, ProteinGroups proteinGroups, List<UniprotEntry> entries, CopyOnWriteArraySet<Field> fields, CopyOnWriteArraySet<Ref> refs) {
//        log.warn(proteinGroups.getProteinGroupId() + " Number of proteints to process " + entries.size() + " count : " + count.getAndIncrement());
////        entries.stream()
////                .parallel()
////                .map(uniprotEntry -> CompletableFuture.runAsync(() -> {
////            processEntries(proteinGroups, uniprotEntry, fields, refs);
////
////        }));
//
////        entries.stream()
////                .parallel()
////                .forEach(uniprotEntry -> processEntries(proteinGroups, uniprotEntry, fields, refs));
//        processInParallel(entry, proteinGroups, fields, refs);
//    }
//        private void processInParallel(Entry entry, ProteinGroups proteinGroups, CopyOnWriteArraySet<Field> fields, CopyOnWriteArraySet<Ref> refs) {
//        // final AtomicInteger tc = new AtomicInteger(1);
//        // AtomicInteger atom = new AtomicInteger(1);
//        //SimpleTimer timer = new SimpleTimer();
//        LazyReact.parallelCommonBuilder()
//                .async()
//                //.ofAsync(() ->enzyme.getEnzymePortalEcNumbersSet())
//                //.flatMap(x->x.stream())
//                .from(proteinGroups.getUniprotEntryList())
//                .grouped(100)
//                .flatMap(x -> x.stream())
//                //.peek(data -> System.out.println(" CHECK "+ atom.getAndIncrement()))
//                .forEachAsync(ec -> process(proteinGroups, ec, fields, refs, entry));
//
//    }
//
//    private Entry process(ProteinGroups proteinGroups, UniprotEntry uniprotEntry, CopyOnWriteArraySet<Field> fields, CopyOnWriteArraySet<Ref> refs, Entry entry) {
//
//        AtomicInteger tc = new AtomicInteger(1);
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        addPrimaryEntities(proteinGroups, uniprotEntry, fields);
//
//        addScientificNameFields(uniprotEntry.getScientificName(), fields);
//        addCommonNameFields(uniprotEntry.getCommonName(), fields);
//
//        addUniprotFamilyFieldsAndXrefs(uniprotEntry.getUniprotFamiliesSet(), fields, refs);
//        addAccessionXrefs(uniprotEntry.getAccession(), refs);
//
//        addCompoundDataFieldsAndXrefs(uniprotEntry.getEnzymePortalCompoundSet(), fields, refs);
//
//        addDiseaseFieldsAndXrefs(uniprotEntry.getEnzymePortalDiseaseSet(), fields, refs);
//        addEcXrefs(uniprotEntry.getEnzymePortalEcNumbersSet(), refs);
//        addEnzymeFamilyToProteinField(uniprotEntry.getEnzymePortalEcNumbersSet(), fields);
//        addPathwaysXrefs(uniprotEntry.getEnzymePortalPathwaysSet(), refs);
//        addTaxonomyXrefs(uniprotEntry.getTaxId(), refs);
//          addReactionFieldsAndXrefs(uniprotEntry.getEnzymePortalReactionSet(), fields, refs);
//
//        AdditionalFields additionalFields = new AdditionalFields();
//        additionalFields.setField(fields);
//        entry.setAdditionalFields(additionalFields);
//
//        CrossReferences cr = new CrossReferences();
//        cr.setRef(refs);
//        entry.setCrossReferences(cr);
//
//        stopWatch.stop();
//        //log.error("Name-"+uniprotEntry.getAccession()+"="+tc.getAndIncrement() + " Executing query:{}, took: {}", uniprotEntry.getAccession(), stopWatch.getTotalTimeSeconds() + " sec");
//
//        return entry;
//    }
}
