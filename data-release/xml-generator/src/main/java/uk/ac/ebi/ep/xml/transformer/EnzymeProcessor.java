package uk.ac.ebi.ep.xml.transformer;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import uk.ac.ebi.ep.xml.entity.enzyme.EnzymePortalUniqueEc;
import uk.ac.ebi.ep.xml.entity.enzyme.IntenzAltNames;
import uk.ac.ebi.ep.xml.entity.enzyme.UniprotEntryEnzyme;
import uk.ac.ebi.ep.xml.schema.AdditionalFields;
import uk.ac.ebi.ep.xml.schema.CrossReferences;
import uk.ac.ebi.ep.xml.schema.Entry;
import uk.ac.ebi.ep.xml.schema.Field;
import uk.ac.ebi.ep.xml.schema.Ref;
import uk.ac.ebi.ep.xml.util.FieldName;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Slf4j
public class EnzymeProcessor extends XmlTransformer implements ItemProcessor<EnzymePortalUniqueEc, Entry> {

//    ForkJoinPool forkJoinPool = new ForkJoinPool();
    // private Executor executor = Executors.newCachedThreadPool();
    //private ExecutorService executorService  = Executors.newFixedThreadPool(20);
    private final AtomicInteger count = new AtomicInteger(1);

    @Override
    public Entry process(EnzymePortalUniqueEc enzyme) throws Exception {
//        CopyOnWriteArraySet<Field> fields = new CopyOnWriteArraySet<>();
//        CopyOnWriteArraySet<Ref> refs = new CopyOnWriteArraySet<>();

        Set<Field> fields = new HashSet<>();
        Set<Ref> refs = new HashSet<>();
        AdditionalFields additionalFields = new AdditionalFields();
        CrossReferences cr = new CrossReferences();
        Entry entry = new Entry();
        entry.setId(enzyme.getEcNumber());
        entry.setName(enzyme.getEnzymeName());

        String description = String.format("%s %s", enzyme.getEcNumber(), enzyme.getEnzymeName());
        entry.setDescription(description);

        addEnzymeFamilyField(enzyme.getEcNumber(), fields);

        addCofactorsField(enzyme.getCofactor(), fields);
        addCatalyticActivityField(enzyme.getCatalyticActivity(), fields);
        addAltNamesField(enzyme.getIntenzAltNamesSet(), fields);
        addEcSource(enzyme.getEcNumber(), refs);

        int numEnzymes = enzyme.getEnzymePortalEcNumbersSet().size();
        log.warn(enzyme.getEcNumber() + " Number of ezymes to process " + numEnzymes + " count : " + count.getAndIncrement());

        //default
//        enzyme.getEnzymePortalEcNumbersSet()
//                .stream()
//                .parallel()
//                .forEach(ec -> processUniprotEntry(ec.getUniprotAccession(), fields, refs));
//
//        AdditionalFields additionalFields = new AdditionalFields();
//        additionalFields.setField(fields);
//        entry.setAdditionalFields(additionalFields);
//
//        CrossReferences cr = new CrossReferences();
//        cr.setRef(refs);
//        entry.setCrossReferences(cr);

        return enzyme.getEnzymePortalEcNumbersSet()
                .stream()
                .map(data -> processAsync(data.getUniprotAccession(), fields, refs, additionalFields, cr, entry).join())
                .findAny()
                .get();
                //.join();

        // return entry;
    }
    // @Transactional
//    @Override
//    public Entry process(EnzymePortalUniqueEc enzyme) throws Exception {
//        CopyOnWriteArraySet<Field> fields = new CopyOnWriteArraySet<>();
//        CopyOnWriteArraySet<Ref> refs = new CopyOnWriteArraySet<>();
//        //Set s = Collections.synchronizedSet(new HashSet<>());
//        //Set<Field> fields = new HashSet<>();
//        //Set<Ref> refs = new HashSet<>();
//        Entry entry = new Entry();
//        entry.setId(enzyme.getEcNumber());
//        entry.setName(enzyme.getEnzymeName());
//
//        String description = String.format("%s %s", enzyme.getEcNumber(), enzyme.getEnzymeName());
//        entry.setDescription(description);
//
//        addEnzymeFamilyField(enzyme.getEcNumber(), fields);
//
//        addCofactorsField(enzyme.getCofactor(), fields);
//        addCatalyticActivityField(enzyme.getCatalyticActivity(), fields);
//        addAltNamesField(enzyme.getIntenzAltNamesSet(), fields);
//        addEcSource(enzyme.getEcNumber(), refs);
//
//        int numEnzymes = enzyme.getEnzymePortalEcNumbersSet().size();
//        log.warn(enzyme.getEcNumber() + " Number of ezymes to process " + numEnzymes + " count : " + count.getAndIncrement());
//
//        //boolean phase = true;
//        //if (phase) {
//          entry =  processInParallelReturnVoid(entry, enzyme, fields, refs);
//            //return entry;
//        //}
//       // return processInParallel(entry, enzyme, fields, refs);
//               AdditionalFields additionalFields = new AdditionalFields();
//        additionalFields.setField(fields);
//        entry.setAdditionalFields(additionalFields);
//
//        CrossReferences cr = new CrossReferences();
//        cr.setRef(refs);
//        entry.setCrossReferences(cr);
//        return entry;
//    }

    private CompletableFuture<Entry> processAsync(UniprotEntryEnzyme uniprotEntry, Set<Field> fields, Set<Ref> refs, AdditionalFields additionalFields, CrossReferences cr, Entry entry) {
        return CompletableFuture.supplyAsync(() -> computeEntry(uniprotEntry, fields, refs, additionalFields, cr, entry));
    }

    private Entry computeEntry(UniprotEntryEnzyme uniprotEntry, Set<Field> fields, Set<Ref> refs, AdditionalFields additionalFields, CrossReferences cr, Entry entry) {
        //CompletableFuture.runAsync(() -> processUniprotEntry(uniprotEntry, fields, refs));
//        Set<Field> proteinField = addProteinNameFields(uniprotEntry.getProteinName(), fields);
//        Set<Field> scientificNameField = addScientificNameFields(uniprotEntry.getScientificName(), fields);
//        Set<Field> commonNameField = addCommonNameFields(uniprotEntry.getCommonName(), fields);
//        Set<Field> geneNameField = addGeneNameFields(uniprotEntry.getEntryToGeneMappingSet(), fields);
//        Set<Field> synNameField = addSynonymFields(uniprotEntry.getSynonymNames(), uniprotEntry.getProteinName(), fields);
//
//        Set<Ref> reactionRefs = addReactionXrefs(uniprotEntry.getEnzymePortalReactionSet(), refs);
//        Set<Ref> accRef = addAccessionXrefs(uniprotEntry.getAccession(), refs);
//        Set<Ref> taxRef = addTaxonomyXrefs(uniprotEntry.getTaxId(), refs);
//        Set<Ref> pathRef = addPathwaysXrefs(uniprotEntry.getEnzymePortalPathwaysSet(), refs);

        // entry = addUniprotFamilyFieldsAndXrefs(uniprotEntry.getUniprotFamiliesSet(), fields, refs, entry);
        // entry = addCompoundFieldsAndXrefs(uniprotEntry.getEnzymePortalCompoundSet(), fields, refs, entry);
        //entry = addDiseaseFieldsAndXrefs(uniprotEntry.getEnzymePortalDiseaseSet(), fields, refs, entry);
        // entry = addReactantFieldsAndXrefs(uniprotEntry.getEnzymePortalReactantSet(), fields, refs, entry);
//        additionalFields.setField(proteinField);
//        additionalFields.setField(scientificNameField);
//        additionalFields.setField(commonNameField);
//        additionalFields.setField(geneNameField);
//        additionalFields.setField(synNameField);
//     
        addProteinNameFields(uniprotEntry.getProteinName(), fields);
        addScientificNameFields(uniprotEntry.getScientificName(), fields);
        addCommonNameFields(uniprotEntry.getCommonName(), fields);
        addGeneNameFields(uniprotEntry.getEntryToGeneMappingSet(), fields);

        addUniprotFamilyFieldsAndXrefs(uniprotEntry.getUniprotFamiliesSet(), fields, refs);

        addSynonymFields(uniprotEntry.getSynonymNames(), uniprotEntry.getProteinName(), fields);
        addAccessionXrefs(uniprotEntry.getAccession(), refs);
        addTaxonomyXrefs(uniprotEntry.getTaxId(), refs);

        addCompoundFieldsAndXrefs(uniprotEntry.getEnzymePortalCompoundSet(), fields, refs);
        addDiseaseFieldsAndXrefs(uniprotEntry.getEnzymePortalDiseaseSet(), fields, refs);
        addPathwaysXrefs(uniprotEntry.getEnzymePortalPathwaysSet(), refs);
        addReactantFieldsAndXrefs(uniprotEntry.getEnzymePortalReactantSet(), fields, refs);
        addReactionXrefs(uniprotEntry.getEnzymePortalReactionSet(), refs);

        additionalFields.setField(fields);

        entry.setAdditionalFields(additionalFields);

//        cr.setRef(reactionRefs);
//        cr.setRef(accRef);
//        cr.setRef(taxRef);
//        cr.setRef(pathRef);
        cr.setRef(refs);
        entry.setCrossReferences(cr);

        return entry;
    }
//synchronized
//original impl
//    private synchronized void processUniprotEntry(UniprotEntryEnzyme uniprotEntry, Set<Field> fields, Set<Ref> refs) {
//
//        addProteinNameFields(uniprotEntry.getProteinName(), fields);
//
//        addScientificNameFields(uniprotEntry.getScientificName(), fields);
//        addCommonNameFields(uniprotEntry.getCommonName(), fields);
//        addGeneNameFields(uniprotEntry.getEntryToGeneMappingSet(), fields);
//
//        addUniprotFamilyFieldsAndXrefs(uniprotEntry.getUniprotFamiliesSet(), fields, refs);
//
//        addSynonymFields(uniprotEntry.getSynonymNames(), uniprotEntry.getProteinName(), fields);
//        //addSource(enzyme, refs);
//        addAccessionXrefs(uniprotEntry.getAccession(), refs);
//        addTaxonomyXrefs(uniprotEntry.getTaxId(), refs);
//
//        addCompoundFieldsAndXrefs(uniprotEntry.getEnzymePortalCompoundSet(), fields, refs);
//        addDiseaseFieldsAndXrefs(uniprotEntry.getEnzymePortalDiseaseSet(), fields, refs);
//        addPathwaysXrefs(uniprotEntry.getEnzymePortalPathwaysSet(), refs);
//        //addReactantFields(uniprotEntry.getEnzymePortalReactantSet(), fields);
//        addReactantFieldsAndXrefs(uniprotEntry.getEnzymePortalReactantSet(), fields, refs);
//        addReactionXrefs(uniprotEntry.getEnzymePortalReactionSet(), refs);
//    }

    private void addAltNamesField(Set<IntenzAltNames> altNames, Set<Field> fields) {

        altNames
                .stream()
                .map(altName -> new Field(FieldName.INTENZ_ALT_NAMES.getName(), altName.getAltName()))
                .forEach(field -> fields.add(field));

    }

    private void addCofactorsField(String cofactor, Set<Field> fields) {
        if (cofactor != null) {
            Field field = new Field(FieldName.INTENZ_COFACTORS.getName(), cofactor);
            fields.add(field);
        }
    }

    private void addCatalyticActivityField(String catalyticActivity, Set<Field> fields) {
        if (catalyticActivity != null) {
            Field field = new Field(FieldName.CATALYTIC_ACTIVITY.getName(), catalyticActivity);
            fields.add(field);
        }
    }
    //TODO

//    private Entry process(UniprotEntryEnzyme uniprotEntry, CopyOnWriteArraySet<Field> fields, CopyOnWriteArraySet<Ref> refs, Entry entry) {
//
//       // AtomicInteger tc = new AtomicInteger(1);
//        //StopWatch stopWatch = new StopWatch();
//        //stopWatch.start();
//        //System.out.println("PROCESSING " + uniprotEntry.getAccession());
//        addProteinNameFields(uniprotEntry.getProteinName(), fields);
//
//        addScientificNameFields(uniprotEntry.getScientificName(), fields);
//        addCommonNameFields(uniprotEntry.getCommonName(), fields);
//        addGeneNameFields(uniprotEntry.getEntryToGeneMappingSet(), fields);
//
//        addUniprotFamilyFieldsAndXrefs(uniprotEntry.getUniprotFamiliesSet(), fields, refs);
//
//        addSynonymFields(uniprotEntry.getSynonymNames(), uniprotEntry.getProteinName(), fields);
//        //addSource(enzyme, refs);
//        addAccessionXrefs(uniprotEntry.getAccession(), refs);
//        addTaxonomyXrefs(uniprotEntry.getTaxId(), refs);
//
//        addCompoundFieldsAndXrefs(uniprotEntry.getEnzymePortalCompoundSet(), fields, refs);
//        addDiseaseFieldsAndXrefs(uniprotEntry.getEnzymePortalDiseaseSet(), fields, refs);
//        addPathwaysXrefs(uniprotEntry.getEnzymePortalPathwaysSet(), refs);
//        addReactantFields(uniprotEntry.getEnzymePortalReactantSet(), fields);
//        addReactionFieldsAndXrefs(uniprotEntry.getEnzymePortalReactionSet(), fields, refs);
//
////        AdditionalFields additionalFields = new AdditionalFields();
////        additionalFields.setField(fields);
////        entry.setAdditionalFields(additionalFields);
////
////        CrossReferences cr = new CrossReferences();
////        cr.setRef(refs);
////        entry.setCrossReferences(cr);
//
//        //stopWatch.stop();
//        //log.error("Name-"+uniprotEntry.getAccession()+"="+tc.getAndIncrement() + " Executing query:{}, took: {}", uniprotEntry.getAccession(), stopWatch.getTotalTimeSeconds() + " sec");
//
//        return entry;
//    }
//    private void processVoid(UniprotEntryEnzyme uniprotEntry, CopyOnWriteArraySet<Field> fields, CopyOnWriteArraySet<Ref> refs,  AtomicInteger tc ) {
//
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        //System.out.println("PROCESSING " + uniprotEntry.getAccession());
//        addProteinNameFields(uniprotEntry.getProteinName(), fields);
//
//        addScientificNameFields(uniprotEntry.getScientificName(), fields);
//        addCommonNameFields(uniprotEntry.getCommonName(), fields);
//        addGeneNameFields(uniprotEntry.getEntryToGeneMappingSet(), fields);
//
//        addUniprotFamilyFieldsAndXrefs(uniprotEntry.getUniprotFamiliesSet(), fields, refs);
//
//        addSynonymFields(uniprotEntry.getSynonymNames(), uniprotEntry.getProteinName(), fields);
//        //addSource(enzyme, refs);
//        addAccessionXrefs(uniprotEntry.getAccession(), refs);
//        addTaxonomyXrefs(uniprotEntry.getTaxId(), refs);
//
//        addCompoundFieldsAndXrefs(uniprotEntry.getEnzymePortalCompoundSet(), fields, refs);
//        addDiseaseFieldsAndXrefs(uniprotEntry.getEnzymePortalDiseaseSet(), fields, refs);
//        addPathwaysXrefs(uniprotEntry.getEnzymePortalPathwaysSet(), refs);
//        addReactantFields(uniprotEntry.getEnzymePortalReactantSet(), fields);
//        addReactionFieldsAndXrefs(uniprotEntry.getEnzymePortalReactionSet(), fields, refs);
//
//        stopWatch.stop();
//        log.error(tc.getAndIncrement() + "  Executing query:{}, took: {}", uniprotEntry.getAccession(), stopWatch.getTotalTimeSeconds() + " sec");
//
//    }
//    
//    private Entry processInParallel(Entry entry, EnzymePortalUniqueEc enzyme, CopyOnWriteArraySet<Field> fields, CopyOnWriteArraySet<Ref> refs) {
//        MaxActive md = MaxActive.IO;
//       // LazyReact builder = new LazyReact(10, 10);   
//        //LazyReact builder = new LazyReact(executorService);
//         final AtomicInteger tc = new AtomicInteger(1);
//        //return builder
//        return LazyReact.parallelBuilder(100)
//                .ofAsync(()-> enzyme.getEnzymePortalEcNumbersSet())
//                //.objectPoolingOn()
//                //.from(enzyme.getEnzymePortalEcNumbersSet())
//                //.grouped(enzyme.getEnzymePortalEcNumbersSet().size())
//                //.onePer(1, TimeUnit.SECONDS)
//                .flatMap(x -> x.stream())
////                .peek(i -> {
////                    System.out.println(i.getUniprotAccession().getAccession() + " Thread " + Thread.currentThread().getId() + " POOL " + ForkJoinPool.getCommonPoolParallelism());
////                })
//                //.async()
//                //.peek(data -> System.out.println("DATA " + data.getUniprotAccession().getAccession()))
//                .map(ec -> process(ec.getUniprotAccession(), fields, refs, entry))
//                .backpressureAware()
//                //.onComplete(executorService::shutdown)
//               // .block();
//                .singleOrElse(entry);
//    }
//
//    private Entry processInParallelReturnVoid(Entry entry, EnzymePortalUniqueEc enzyme, CopyOnWriteArraySet<Field> fields, CopyOnWriteArraySet<Ref> refs) {
//         // final AtomicInteger tc = new AtomicInteger(1);
//          AtomicInteger atom = new AtomicInteger(1);
//          //SimpleTimer timer = new SimpleTimer();
////          return new LazyReact(10,10)
////                  .async()
////                  .fromStream(enzyme.getEnzymePortalEcNumbersSet().stream())
////                  //.grouped(100)
////                  //.flatMap(x->x.stream())
////                  .map(ec -> process(ec.getUniprotAccession(), fields, refs, entry))
////                  .singleOrElse(entry);
//
//
//    //LazyReact builder = new LazyReact(10, 100);
//      
//
//  
//    //builder.fromStreamAsync(stream);
//
//        LazyReact
//              // .parallelBuilder()
//               .parallelCommonBuilder()
//                .async()
//                //.ofAsync(() ->enzyme.getEnzymePortalEcNumbersSet())
//                //.flatMap(x->x.stream())
//                .from(enzyme.getEnzymePortalEcNumbersSet())
//                //.grouped(100)
//               //.flatMap(x -> x.stream())
//                //.async()
//               // .grouped(10)
//                //.peek(data -> System.out.println(" CHECK "+ atom.getAndIncrement()))
//                //.forEachAsync(ec -> process(ec.getUniprotAccession(), fields, refs, entry));
//                //.forEach(x);
//                //.forEach(ec -> process(ec.getUniprotAccession(), fields, refs, entry));
//                .map(ec -> process(ec.getUniprotAccession(), fields, refs, entry))
//                //.grouped(100)
//                .forEachAsync(data -> System.out.println(" CHECK "+ atom.getAndIncrement()));
//                //.singleOrElse(entry);
//                //.peek(time -> System.out.println(" CHECK "+ timer.getElapsedNanoseconds()))
//                //.grouped(10)
//                //.forEachAsync(data -> System.out.println(" CHECK "+ data));
//                //.grouped(10)
//                //.peek(data -> System.out.println(" CHECK "+ atom.getAndIncrement()))
//                //.block();
//
////        AdditionalFields additionalFields = new AdditionalFields();
////        additionalFields.setField(fields);
////        entry.setAdditionalFields(additionalFields);
////
////        CrossReferences cr = new CrossReferences();
////        cr.setRef(refs);
////        entry.setCrossReferences(cr);
//return entry;
//    }
//    @Deprecated
//    private void addReactionMechanism(Set<ReactionMechanism> reactionMechanismSet, Set<Field> fields) {
//
//        reactionMechanismSet
//                .stream()
//                .map(rm -> rm.getMcsaId() + ";" + rm.getEnzymeName() + ";" + rm.getImageId() + ";" + rm.getMechanismDescription())
//                .map(mechanism -> new Field(FieldName.REACTION_MECHANISM.getName(), mechanism))
//                .forEachOrdered(pdbfield -> fields.add(pdbfield));
//
//    }
    //https://medium.com/@johnmcclean/dysfunctional-programming-in-java-3-functional-composition-16828f0609c2
    //https://medium.com/@johnmcclean/reactive-programming-with-java-8-and-simple-react-batching-and-chunking-ecac62ce8bec
}
