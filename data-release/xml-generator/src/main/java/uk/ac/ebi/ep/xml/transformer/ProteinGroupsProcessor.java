package uk.ac.ebi.ep.xml.transformer;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ep.xml.entities.PrimaryProtein;
import uk.ac.ebi.ep.xml.entities.Protein;
import uk.ac.ebi.ep.xml.entities.ProteinGroups;
import uk.ac.ebi.ep.xml.entities.repositories.ProteinXmlRepository;
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
    private final AtomicInteger count = new AtomicInteger(0);

    private final ProteinXmlRepository proteinXmlRepository;

    public ProteinGroupsProcessor(ProteinXmlRepository repository) {
        this.proteinXmlRepository = repository;
    }

    private void addPrimaryProteinField(PrimaryProtein primaryProtein, Set<Field> fields) {

        String accession = primaryProtein.getAccession();
        String commonName = primaryProtein.getCommonName();
        if (commonName == null || commonName.isEmpty()) {
            commonName = primaryProtein.getScientificName();
        }

        fields.add(new Field(FieldName.PRIMARY_ORGANISM.getName(), commonName));

        fields.add(new Field(FieldName.PRIMARY_ACCESSION.getName(), accession));

    }

    private void addPrimaryFunctionFields(PrimaryProtein primaryProtein, Set<Field> fields) {

        if (primaryProtein.getFunction() != null && !StringUtils.isEmpty(primaryProtein.getFunction())) {
            fields.add(new Field(FieldName.FUNCTION.getName(), primaryProtein.getFunction()));
        }

    }

    private void addPrimaryImage(PrimaryProtein primaryProtein, Set<Field> fields) {

        char hasPdbFlag = 'Y';

        if (primaryProtein.getPdbFlag().equals(hasPdbFlag)) {
            fields.add(new Field(FieldName.PRIMARY_IMAGE.getName(), primaryProtein.getPdbId() + "|" + primaryProtein.getPdbSpecies() + "|" + primaryProtein.getPdbLinkedAcc()));
        }

    }

    private void addEntryTypeFields(PrimaryProtein primaryProtein, Set<Field> fields) {

        if (primaryProtein.getEntryType() != null) {
            fields.add(new Field(FieldName.ENTRY_TYPE.getName(), "" + primaryProtein.getEntryType().intValue()));
        }

    }

    @Override
    public Entry process(ProteinGroups proteinGroups) throws Exception {
        AdditionalFields additionalFields = new AdditionalFields();
        CrossReferences cr = new CrossReferences();

        Set<Field> fields = new HashSet<>();
        Set<Ref> refs = new HashSet<>();
        Set<String> relSpecies = new HashSet<>();

        // if (log.isDebugEnabled()) {
        log.info("Processor " + Runtime.getRuntime().availableProcessors() + " current entry : " + proteinGroups.getProteinGroupId() + "  entry count : " + count.getAndIncrement());

        //}
        Entry entry = new Entry();

        entry.setId(proteinGroups.getProteinGroupId());
        entry.setName(proteinGroups.getProteinName());
        entry.setDescription(proteinGroups.getProteinName());

        addPrimaryProtein(proteinGroups, fields);

        addProteinInformation(proteinGroups, fields, refs, relSpecies);
        additionalFields.setField(fields);
        entry.setAdditionalFields(additionalFields);
        cr.setRef(refs);
        entry.setCrossReferences(cr);
        return entry;
    }

    private void addProteinInformation(ProteinGroups proteinGroups, Set<Field> fields, Set<Ref> refs, Set<String> relSpecies) {
        try (Stream<Protein> protein = proteinXmlRepository.streamProteinByProteinGroupId(proteinGroups.getProteinGroupId())) {

            protein
                    .parallel()
                    .forEach(data -> processEntries(data, relSpecies, fields, refs));
            addRelatedSpeciesField(relSpecies, fields);

        }

    }

    private void addPrimaryProtein(ProteinGroups proteinGroups, Set<Field> fields) {
        PrimaryProtein primaryProtein = proteinGroups.getPrimaryProtein();
        if (primaryProtein != null) {

            addPrimaryProteinField(primaryProtein, fields);
            addPrimaryImage(primaryProtein, fields);
            addEntryTypeFields(primaryProtein, fields);
            addPrimaryFunctionFields(primaryProtein, fields);

        }

    }

    private void processEntries(Protein uniprotEntry, Set<String> relSpecies, Set<Field> fields, Set<Ref> refs) {
        addScientificNameFields(uniprotEntry.getScientificName(), fields);
        addCommonNameFields(uniprotEntry.getCommonName(), fields);
        addAccessionXrefs(uniprotEntry.getAccession(), refs);
        addRelatedSpecies(uniprotEntry, relSpecies);

        addReactantFieldsAndXrefs(uniprotEntry, fields, refs);

        addCompoundDataFieldsAndXrefs(uniprotEntry, fields, refs);
        addChebiCompoundDataFieldsAndXrefs(uniprotEntry, fields, refs);
        addMetaboliteFieldsAndXrefs(uniprotEntry, fields, refs);
        addDiseaseFieldsAndXrefs(uniprotEntry, fields, refs);

        addPrimaryEntities(uniprotEntry, fields);
        addEcField(uniprotEntry, fields);
        addEcXrefs(uniprotEntry, refs);
        addTaxonomyFieldAndXrefs(uniprotEntry, fields, refs);

        addUniprotFamilyFieldsAndXrefs(uniprotEntry, fields, refs);
        addPathwayFieldsAndXrefs(uniprotEntry, fields, refs);
        addReactionFieldsAndXrefs(uniprotEntry, fields, refs);

    }

    private void addPrimaryEntities(Protein entry, Set<Field> fields) {
        if (entry.getAccession().equals(entry.getPrimaryAccession())) {

            addPrimarySynonymFields(entry, fields);
            addPrimaryEc(entry, fields);
            addPrimaryCatalyticActivityFields(entry, fields);
            addPrimaryGeneNameFields(entry, fields);
            addEnzymeFamilyToProteinField(entry, fields);
        }
    }

    private void addPrimarySynonymFields(Protein entry, Set<Field> fields) {

        if (Objects.nonNull(entry.getSynonymNames()) && Objects.nonNull(entry.getProteinName())) {

            Optional<String> synonymName = Optional.ofNullable(entry.getSynonymNames());
            computeSynonymsAndBuildFields(synonymName.orElse(""), entry.getProteinName(), fields);

        }

    }

    private void addPrimaryEc(Protein entry, Set<Field> fields) {
        addField(FieldName.PRIMARY_EC.getName(), "ec;"+entry.getEcNumber(), fields);

    }

    private void addEcField(Protein entry, Set<Field> fields) {
        addField(FieldName.EC.getName(), entry.getEcNumber(), fields);

    }

    private void addEcXrefs(Protein entry, Set<Ref> refs) {

        Ref xref = new Ref(entry.getEcNumber(), DatabaseName.INTENZ.getDbName());
        refs.add(xref);

    }

    private void addEnzymeFamilyToProteinField(Protein entry, Set<Field> fields) {
        if (Objects.nonNull(entry.getEcFamily())) {
            addField(FieldName.ENZYME_FAMILY.getName(), computeEcToFamilyName(entry.getEcFamily()), fields);
        }
    }

    private void addPrimaryGeneNameFields(Protein entry, Set<Field> fields) {
        addGeneNameFields(entry, fields);

    }

    private void addPrimaryCatalyticActivityFields(Protein entry, Set<Field> fields) {
        if (Objects.nonNull(entry.getCatalyticActivity())) {
            addField(FieldName.CATALYTIC_ACTIVITY.getName(), entry.getCatalyticActivity(), fields);
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

    private void addRelatedSpecies(Protein uniprotEntry, Set<String> relSpecies) {
        if (Objects.equals(uniprotEntry.getRelatedProteinsId(), uniprotEntry.getPrimaryRelatedProteinsId())) {

            relSpecies.add(uniprotEntry.getAccession() + ";" + uniprotEntry.getCommonName() + ";" + uniprotEntry.getScientificName() + ";" + uniprotEntry.getExpEvidenceFlag() + ";" + uniprotEntry.getTaxId());

        }
    }

    @Override
    void addDiseaseFieldsAndXrefs(Protein disease, Set<Field> fields, Set<Ref> refs) {

        if (Objects.nonNull(disease.getOmimNumber()) && Objects.nonNull(disease.getDiseaseName())) {

            fields.add(new Field(FieldName.DISEASE_NAME.getName(), disease.getDiseaseName()));

            fields.add(new Field(FieldName.WITH_DISEASE.getName(), withResourceField(disease.getOmimNumber(), disease.getAccession(), disease.getCommonName(), disease.getEntryType())));
            fields.add(new Field(FieldName.HAS_DISEASE.getName(), HAS_DISEASE));
            refs.add(new Ref(disease.getOmimNumber(), DatabaseName.OMIM.getDbName()));
        }
    }

    @Override
    void addUniprotFamilyFieldsAndXrefs(Protein family, Set<Field> fields, Set<Ref> refs) {

        if (Objects.nonNull(family.getFamilyGroupId())) {

            //fields.add(new Field(FieldName.PROTEIN_FAMILY_NAME.getName(), family.getFamilyName()));
            fields.add(new Field(FieldName.PROTEIN_FAMILY_ID.getName(), family.getFamilyGroupId()));

            fields.add(new Field(FieldName.WITH_PROTEIN_FAMILY.getName(), withResourceField(family.getFamilyGroupId(), family.getAccession(), family.getCommonName(), family.getEntryType())));
            fields.add(new Field(FieldName.HAS_PROTEIN_FAMILY.getName(), HAS_PROTEIN_FAMILY));
            refs.add(new Ref(family.getFamilyGroupId(), DatabaseName.PROTEIN_FAMILY.getDbName()));
        }
    }

    @Override
    void addReactantFieldsAndXrefs(Protein reactant, Set<Field> fields, Set<Ref> refs) {

        if (Objects.nonNull(reactant.getReactantSource())) {

            if (reactant.getReactantSource().toUpperCase().equalsIgnoreCase(RHEA)) {
                fields.add(new Field(FieldName.RHEA_ID.getName(), reactant.getReactantId()));
            }

            refs.add(new Ref(reactant.getReactantId(), reactant.getReactantSource().toUpperCase()));
        }
    }

    @Override
    void addPathwayFieldsAndXrefs(Protein pathway, Set<Field> fields, Set<Ref> refs) {

        if (Objects.nonNull(pathway.getPathwayId())) {

            fields.add(new Field(FieldName.WITH_PATHWAY.getName(), withResourceField(parseReactomePathwayId(pathway.getPathwayId()), pathway.getAccession(), pathway.getCommonName(), pathway.getEntryType())));
            fields.add(new Field(FieldName.HAS_PATHWAY.getName(), HAS_PATHWAY));
            refs.add(new Ref(parseReactomePathwayId(pathway.getPathwayId()), DatabaseName.REACTOME.getDbName()));
        }
    }

    private void addMetaboliteFieldsAndXrefs(Protein chebiCompound, Set<Field> fields, Set<Ref> refs) {

        if (Objects.nonNull(chebiCompound.getChebiCompoundRole()) && Objects.nonNull(chebiCompound.getChebiCompoundId()) && Objects.nonNull(chebiCompound.getChebiCompoundName())) {

            if (chebiCompound.getChebiCompoundRole().equalsIgnoreCase(METABOLITE)) {
                fields.add(new Field(FieldName.HAS_METABOLITE.getName(), HAS_METABOLITE));
                String metaboliteId = chebiCompound.getChebiCompoundId().replace(CHEBI_PREFIX, METABOLITE.toLowerCase());
                fields.add(new Field(FieldName.METABOLITE.getName(), metaboliteId));
                fields.add(new Field(FieldName.METABOLITE_NAME.getName(), chebiCompound.getChebiCompoundName()));
                String metabolightId = chebiCompound.getChebiCompoundId().replace(CHEBI_PREFIX, METABOLIGHTS_PREFIX);
                refs.add(new Ref(metabolightId, DatabaseName.METABOLIGHTS.getDbName()));

            }

        }

    }

}
