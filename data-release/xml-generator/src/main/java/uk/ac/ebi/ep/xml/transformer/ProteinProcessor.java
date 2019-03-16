package uk.ac.ebi.ep.xml.transformer;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import uk.ac.ebi.ep.xml.entity.EnzymeCatalyticActivity;
import uk.ac.ebi.ep.xml.entity.protein.PrimaryProtein;
import uk.ac.ebi.ep.xml.entity.protein.ProteinEcNumbers;
import uk.ac.ebi.ep.xml.entity.protein.UniprotEntry;
import uk.ac.ebi.ep.xml.schema.AdditionalFields;
import uk.ac.ebi.ep.xml.schema.CrossReferences;
import uk.ac.ebi.ep.xml.schema.Entry;
import uk.ac.ebi.ep.xml.schema.Field;
import uk.ac.ebi.ep.xml.schema.Ref;
import uk.ac.ebi.ep.xml.util.DatabaseName;
import uk.ac.ebi.ep.xml.util.FieldName;

/**
 *
 * @author joseph
 */
@Slf4j
public class ProteinProcessor extends XmlTransformer implements ItemProcessor<UniprotEntry, Entry> {

    protected static final String REVIEWED = "reviewed";
    protected static final String UNREVIEWED = "unreviewed";
   // private final AtomicInteger count = new AtomicInteger(1);
    // ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    @Override
    public Entry process(UniprotEntry uniprotEntry) throws Exception {
        AdditionalFields additionalFields = new AdditionalFields();
        CrossReferences cr = new CrossReferences();
//           CopyOnWriteArraySet<Field> fields = new CopyOnWriteArraySet<>();
//         CopyOnWriteArraySet<Ref> refs = new CopyOnWriteArraySet<>();
        Set<Field> fields = new HashSet<>();
        Set<Ref> refs = new HashSet<>();
        Entry entry = new Entry();

        entry.setId(uniprotEntry.getAccession());
        entry.setName(uniprotEntry.getProteinName());
        entry.setDescription(uniprotEntry.getProteinName());

        // addPrimaryProtein(uniprotEntry, fields, refs);
        processEntries(uniprotEntry, fields, refs);

        additionalFields.setField(fields);
        entry.setAdditionalFields(additionalFields);

        cr.setRef(refs);
        entry.setCrossReferences(cr);

        return entry;
    }

    private void addRelatedSpeciesField(UniprotEntry uniprotEntry, final Set<Field> fields) {

        List<String> specieList = Stream.of(uniprotEntry.getAccession() + ";" + uniprotEntry.getCommonName() + ";" + uniprotEntry.getScientificName() + ";" + uniprotEntry.getExpEvidenceFlag() + ";" + uniprotEntry.getTaxId())
                .collect(Collectors.toList());
        if (!specieList.isEmpty()) {
            String rs = String.join(" | ", specieList);

            String rsField = StringUtils.removeEnd(rs, " | ");

            Field relatedSpeciesField = new Field(FieldName.RELATED_SPECIES.getName(), rsField);
            fields.add(relatedSpeciesField);
        }

    }

//    private void addPrimaryProtein(ProteinGroups proteinGroups, Set<Field> fields, Set<Ref> refs) {
// //private FieldAndXref addPrimaryProtein(ProteinGroups proteinGroups, Set<Field> fields, Set<Ref> refs, FieldAndXref fieldAndXref) {
//
//        PrimaryProtein primaryProtein = proteinGroups.getPrimaryProtein();
//        if (primaryProtein != null) {
//            addPrimaryProteinField(primaryProtein, fields);
//            addPrimaryImage(primaryProtein, fields);
//
//            addEntryTypeFields(primaryProtein, fields);
//            addPrimaryFunctionFields(primaryProtein, fields);
//            List<UniprotEntry> entries = proteinGroups.getUniprotEntryList();
//            int numEntry = entries.size();
//            log.warn("Processor "+ Runtime.getRuntime().availableProcessors()+" "+proteinGroups.getProteinGroupId() + " Number of proteins to process " + numEntry + " count : " + count.getAndIncrement());
//  
//             entries.parallelStream()
//              .parallel()
//              //.peek(p->System.out.println(" POOL "+  ForkJoinPool.commonPool()))
//             .forEach(uniprotEntry -> processEntries(proteinGroups, uniprotEntry, fields, refs));
//
//
//        }
//
//
//    }
    //synchronized
    private void processEntries(UniprotEntry uniprotEntry, Set<Field> fields, Set<Ref> refs) {

//                    addPrimaryProteinField(primaryProtein, fields);
//            addPrimaryImage(primaryProtein, fields);
//
//            addEntryTypeFields(primaryProtein, fields);
//            addPrimaryFunctionFields(primaryProtein, fields);
//related protein
        addRelatedSpeciesField(uniprotEntry, fields);
        //addPrimaryEntities(proteinGroups, uniprotEntry, fields);
        addPrimarySynonymFields(uniprotEntry, uniprotEntry.getProteinName(), fields);
        addPrimaryEc(uniprotEntry, fields);
        addPrimaryCatalyticActivityFields(uniprotEntry, fields);
        addPrimaryGeneNameFields(uniprotEntry, fields);

        addScientificNameFields(uniprotEntry.getScientificName(), fields);
        addCommonNameFields(uniprotEntry.getCommonName(), fields);

        addUniprotFamilyFieldsAndXrefs(uniprotEntry.getUniprotFamiliesSet(), fields, refs);
        addAccessionXrefs(uniprotEntry.getAccession(), refs);

        addCompoundDataFieldsAndXrefs(uniprotEntry.getEnzymePortalCompoundSet(), fields, refs);

        addDiseaseFieldsAndXrefs(uniprotEntry.getEnzymePortalDiseaseSet(), fields, refs);
        addEcXrefs(uniprotEntry.getEnzymePortalEcNumbersSet(), refs);
        addEnzymeFamilyToProteinField(uniprotEntry.getEnzymePortalEcNumbersSet(), fields);
        addPathwaysXrefs(uniprotEntry.getEnzymePortalPathwaysSet(), refs);
        addTaxonomyXrefs(uniprotEntry.getTaxId(), refs);
        addReactionFieldsAndXrefs(uniprotEntry.getEnzymePortalReactionSet(), fields, refs);
        addReactantFieldsAndXrefs(uniprotEntry.getEnzymePortalReactantSet(), fields, refs);
    }
//    private void addPrimaryEntities(ProteinGroups proteinGroups, UniprotEntry uniprotEntry, Set<Field> fields) {
//        if (uniprotEntry.getAccession().equals(proteinGroups.getPrimaryProtein().getAccession())) {
//
//            addPrimarySynonymFields(uniprotEntry, proteinGroups.getProteinName(), fields);
//            addPrimaryEc(uniprotEntry, fields);
//            addPrimaryCatalyticActivityFields(uniprotEntry, fields);
//            addPrimaryGeneNameFields(uniprotEntry, fields);
//        }
//    }

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

}