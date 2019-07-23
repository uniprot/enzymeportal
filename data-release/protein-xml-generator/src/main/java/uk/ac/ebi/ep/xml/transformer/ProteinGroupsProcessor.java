package uk.ac.ebi.ep.xml.transformer;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ep.xml.entities.EnzymeCatalyticActivity;
import uk.ac.ebi.ep.xml.entities.EnzymePortalEcNumbers;
import uk.ac.ebi.ep.xml.entities.PrimaryProtein;
import uk.ac.ebi.ep.xml.entities.ProteinGroups;
import uk.ac.ebi.ep.xml.entities.UniprotEntry;
import uk.ac.ebi.ep.xml.schema.AdditionalFields;
import uk.ac.ebi.ep.xml.schema.CrossReferences;
import uk.ac.ebi.ep.xml.schema.Entry;
import uk.ac.ebi.ep.xml.schema.Field;
import uk.ac.ebi.ep.xml.schema.Ref;
import uk.ac.ebi.ep.xml.util.DatabaseName;
import uk.ac.ebi.ep.xml.util.FieldName;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Slf4j
public class ProteinGroupsProcessor extends XmlTransformer implements ItemProcessor<ProteinGroups, Entry> {

    protected static final String REVIEWED = "reviewed";
    protected static final String UNREVIEWED = "unreviewed";
    private final AtomicInteger count = new AtomicInteger(1);
    //ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    @Override
    public Entry process(ProteinGroups proteinGroups) throws Exception {
        AdditionalFields additionalFields = new AdditionalFields();
        CrossReferences cr = new CrossReferences();
//        CopyOnWriteArraySet<Field> fields = new CopyOnWriteArraySet<>();
//        CopyOnWriteArraySet<Ref> refs = new CopyOnWriteArraySet<>();
//        CopyOnWriteArraySet<String> relSpecies = new CopyOnWriteArraySet<>();

        Set<Field> fields = new HashSet<>();
        Set<Ref> refs = new HashSet<>();
        Set<String> relSpecies = new HashSet<>();
        
        Entry entry = new Entry();

        entry.setId(proteinGroups.getProteinGroupId());
        entry.setName(proteinGroups.getProteinName());
        entry.setDescription(proteinGroups.getProteinName());

        addPrimaryProtein(proteinGroups, fields, refs, relSpecies);

        additionalFields.setField(fields);
        entry.setAdditionalFields(additionalFields);
        cr.setRef(refs);
        entry.setCrossReferences(cr);

        return entry;
    }


    private void addPrimaryProtein(ProteinGroups proteinGroups, Set<Field> fields, Set<Ref> refs, Set<String> relSpecies) {
        PrimaryProtein primaryProtein = proteinGroups.getPrimaryProtein();
        if (primaryProtein != null) {

            //synchronized (this) {
                addPrimaryProteinField(primaryProtein, fields);
                addPrimaryImage(primaryProtein, fields);
                addEntryTypeFields(primaryProtein, fields);
            //}

            addPrimaryFunctionFields(primaryProtein, fields);
            Set<UniprotEntry> entries = proteinGroups.getUniprotEntrySet();
        
            long numEntry = entries.stream().count();
            log.warn("Processor " + Runtime.getRuntime().availableProcessors() + " " + proteinGroups.getProteinGroupId() + " Number of proteins to process " + numEntry + " count : " + count.getAndIncrement());

            entries
                    .parallelStream()
                    .parallel()
                    .forEach(uniprotEntry -> processEntries(primaryProtein, uniprotEntry, relSpecies, fields, refs));

            addRelatedSpeciesField(relSpecies, fields);

        }

    }

    private void addRelatedSpeciesField(Set<String> relSpecies, Set<Field> fields) {
        if (!relSpecies.isEmpty()) {
            String rsField = relSpecies
                    .stream()
                    .collect(Collectors.joining("|"));
            Field field = new Field(FieldName.RELATED_SPECIES.getName(), rsField);
            fields.add(field);
        }
    }

    private void addRelatedSpecies(PrimaryProtein primaryProtein, UniprotEntry uniprotEntry, Set<String> relSpecies) {
        if (Objects.equals(uniprotEntry.getRelatedProteinsId().getRelProtInternalId(), primaryProtein.getRelatedProteinsId())) {

            String species = uniprotEntry.getAccession() + ";" + uniprotEntry.getCommonName() + ";" + uniprotEntry.getScientificName() + ";" + uniprotEntry.getExpEvidenceFlag() + ";" + uniprotEntry.getTaxId();
            relSpecies.add(species);

        }
    }

    //synchronized
    private void processEntries(PrimaryProtein primaryProtein, UniprotEntry uniprotEntry, Set<String> relSpecies, Set<Field> fields, Set<Ref> refs) {
        //related protein

        addRelatedSpecies(primaryProtein, uniprotEntry, relSpecies);
        addPrimaryEntities(primaryProtein, uniprotEntry.getProteinName(), uniprotEntry, fields);

        addScientificNameFields(uniprotEntry.getScientificName(), fields);
        addCommonNameFields(uniprotEntry.getCommonName(), fields);

        AtomicInteger entryType = new AtomicInteger(uniprotEntry.getEntryType().intValue());
        String accession = uniprotEntry.getAccession();
        String commonName = uniprotEntry.getCommonName();

        addUniprotFamilyFieldsAndXrefs(uniprotEntry.getEnzymePortalUniprotFamiliesSet(), accession, commonName, entryType.get(), fields, refs);
        addAccessionXrefs(accession, refs);

        addCompoundDataFieldsAndXrefs(uniprotEntry.getEnzymePortalCompoundSet(), accession, commonName, entryType.get(), fields, refs);

        addDiseaseFieldsAndXrefs(uniprotEntry.getEnzymePortalDiseaseSet(), accession, commonName, entryType.get(), fields, refs);

        addEcXrefs(uniprotEntry.getEnzymePortalEcNumbersSet(), refs);
        addEnzymeFamilyToProteinField(uniprotEntry.getEnzymePortalEcNumbersSet(), fields);

        addPathwaysXrefs(uniprotEntry.getEnzymePortalPathwaysSet(), refs);

        addTaxonomyFieldAndXrefs(uniprotEntry.getTaxId(), accession, commonName, entryType.get(), fields, refs);
        addReactionFieldsAndXrefs(uniprotEntry.getEnzymePortalReactionSet(), fields, refs);
        addReactantFieldsAndXrefs(uniprotEntry.getEnzymePortalReactantSet(), fields, refs);

    }

    private void addPrimaryEntities(PrimaryProtein primaryProtein, String proteinName, UniprotEntry uniprotEntry, Set<Field> fields) {
        if (uniprotEntry.getAccession().equals(primaryProtein.getAccession())) {

            addPrimarySynonymFields(uniprotEntry, proteinName, fields);
            addPrimaryEc(uniprotEntry, fields);
            addPrimaryCatalyticActivityFields(uniprotEntry, fields);
            addPrimaryGeneNameFields(uniprotEntry, fields);
        }
    }

    private void addEnzymeFamilyToProteinField(Set<EnzymePortalEcNumbers> enzymes, Set<Field> fields) {

        enzymes
                .stream()
                .map(ec -> new Field(FieldName.ENZYME_FAMILY.getName(), computeEcToFamilyName(ec.getEcFamily())))
                .forEach((field) -> fields.add(field));

    }

    private void addEcXrefs(Set<EnzymePortalEcNumbers> enzymes, Set<Ref> refs) {

        if (!enzymes.isEmpty()) {
            enzymes
                    .stream()
                    .map(ecNumbers -> new Ref(ecNumbers.getEcNumber().getEcNumber(), DatabaseName.INTENZ.getDbName()))
                    .forEach(xref -> refs.add(xref));
        }

    }

    private void addPrimaryProteinField(PrimaryProtein primaryProtein, Set<Field> fields) {

        String accession = primaryProtein.getAccession();
        String commonName = primaryProtein.getCommonName();
        if (commonName == null || commonName.isEmpty()) {
            commonName = primaryProtein.getScientificName();
        }
        Field primaryOganismfield = new Field(FieldName.PRIMARY_ORGANISM.getName(), commonName);
        fields.add(primaryOganismfield);
        Field primaryAccessionfield = new Field(FieldName.PRIMARY_ACCESSION.getName(), accession);
        fields.add(primaryAccessionfield);

    }

    private void addPrimaryFunctionFields(PrimaryProtein primaryProtein, Set<Field> fields) {

        if (primaryProtein.getFunction() != null && !StringUtils.isEmpty(primaryProtein.getFunction())) {
            Field primaryFunctionfield = new Field(FieldName.FUNCTION.getName(), primaryProtein.getFunction());

            fields.add(primaryFunctionfield);
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
             Field entryTypefield = new Field(FieldName.ENTRY_TYPE.getName(), "" + type.intValue());

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
                .map(ec -> new Field(FieldName.EC.getName(), ec.getEcNumber().getEcNumber()))
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

    public String computeEcToFamilyName(int ec) {

        if (ec == 1) {

            return EcNumber.EnzymeFamily.OXIDOREDUCTASES.getName();
        }
        if (ec == 2) {
            return EcNumber.EnzymeFamily.TRANSFERASES.getName();
        }
        if (ec == 3) {
            return EcNumber.EnzymeFamily.HYDROLASES.getName();
        }
        if (ec == 4) {
            return EcNumber.EnzymeFamily.LYASES.getName();
        }
        if (ec == 5) {
            return EcNumber.EnzymeFamily.ISOMERASES.getName();
        }
        if (ec == 6) {
            return EcNumber.EnzymeFamily.LIGASES.getName();
        }
        if (ec == 7) {
            return EcNumber.EnzymeFamily.TRANSLOCASES.getName();
        }
        return "Invalid Ec Number";
    }

}
