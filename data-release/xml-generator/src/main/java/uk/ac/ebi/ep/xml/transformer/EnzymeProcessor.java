package uk.ac.ebi.ep.xml.transformer;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ep.xml.entities.EnzymePortalUniqueEc;
import uk.ac.ebi.ep.xml.entities.IntenzAltNames;
import uk.ac.ebi.ep.xml.entities.ProteinXml;
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
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Slf4j
public class EnzymeProcessor extends XmlTransformer implements ItemProcessor<EnzymePortalUniqueEc, Entry> {

   // private final AtomicInteger count = new AtomicInteger(1);
    private final ProteinXmlRepository proteinXmlRepository;

    public EnzymeProcessor(ProteinXmlRepository proteinXmlRepository) {
        this.proteinXmlRepository = proteinXmlRepository;
    }

    @Override
    public Entry process(EnzymePortalUniqueEc enzyme) throws Exception {
//        CopyOnWriteArraySet<Field> fields = new CopyOnWriteArraySet<>();
//        CopyOnWriteArraySet<Ref> refs = new CopyOnWriteArraySet<>();

        Set<Field> fields = new HashSet<>();
        Set<Ref> refs = new HashSet<>();
        AdditionalFields additionalFields = new AdditionalFields();
        CrossReferences cr = new CrossReferences();

        //log.warn("Processor " + Runtime.getRuntime().availableProcessors() + " current entry : " + enzyme.getEcNumber() + "  entry count : " + count.getAndIncrement());

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

        try (Stream<ProteinXml> protein = proteinXmlRepository.streamProteinDataByEcNumber(enzyme.getEcNumber())) {
            protein.parallel().forEach(data -> processUniprotEntry(data, fields, refs));
        }

        additionalFields.setField(fields);
        entry.setAdditionalFields(additionalFields);

        cr.setRef(refs);
        entry.setCrossReferences(cr);

        return entry;
    }

    private void processUniprotEntry(ProteinXml uniprotEntry, Set<Field> fields, Set<Ref> refs) {

        addProteinNameFields(uniprotEntry.getProteinName(), fields);

        addScientificNameFields(uniprotEntry.getScientificName(), fields);
        addCommonNameFields(uniprotEntry.getCommonName(), fields);
        addGeneNameFields(uniprotEntry, fields);

        addUniprotFamilyFieldsAndXrefs(uniprotEntry, fields, refs);

        addSynonymFields(uniprotEntry.getSynonymNames(), uniprotEntry.getProteinName(), fields);

        addAccessionXrefs(uniprotEntry.getAccession(), refs);
        addTaxonomyXrefs(uniprotEntry, refs);

        addCompoundFieldsAndXrefs(uniprotEntry, fields, refs);
        addDiseaseFieldsAndXrefs(uniprotEntry, fields, refs);
        addPathwaysXrefs(uniprotEntry, refs);
        addReactantFieldsAndXrefs(uniprotEntry, fields, refs);
        addReactionFieldsAndXrefs(uniprotEntry, fields, refs);
    }

    private void addAltNamesField(Set<IntenzAltNames> altNames, Set<Field> fields) {

        altNames
                .stream()
                .map(altName -> new Field(FieldName.INTENZ_ALT_NAMES.getName(), altName.getAltName()))
                .forEach(fields::add);

    }

    private void addCofactorsField(String cofactor, Set<Field> fields) {
        if (Objects.nonNull(cofactor)) {
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

    protected void addEnzymeFamilyField(String ec, Set<Field> fields) {

        String enzymeFamily = computeFamily(ec);
        Field field = new Field(FieldName.ENZYME_FAMILY.getName(), enzymeFamily);
        fields.add(field);
    }

    protected void addEcSource(String ec, Set<Ref> refs) {
        if (!StringUtils.isEmpty(ec)) {
            Ref xref = new Ref(ec, DatabaseName.INTENZ.getDbName());
            refs.add(xref);
        }
    }

    @Override
    protected void addUniprotFamilyFieldsAndXrefs(ProteinXml family, Set<Field> fields, Set<Ref> refs) {

        if (Objects.nonNull(family.getFamilyGroupId()) && Objects.nonNull(family.getFamilyName())) {
            Field field = new Field(FieldName.PROTEIN_FAMILY.getName(), family.getFamilyName());
            fields.add(field);
            Field fieldId = new Field(FieldName.PROTEIN_FAMILY_ID.getName(), family.getFamilyGroupId());
            fields.add(fieldId);

            Ref xref = new Ref(family.getFamilyGroupId(), DatabaseName.PROTEIN_FAMILY.getDbName());
            refs.add(xref);
        }
    }

    @Override
    protected void addDiseaseFieldsAndXrefs(ProteinXml disease, Set<Field> fields, Set<Ref> refs) {

        if (Objects.nonNull(disease.getOmimNumber()) && Objects.nonNull(disease.getDiseaseName())) {
            Field field = new Field(FieldName.DISEASE_NAME.getName(), disease.getDiseaseName());
            fields.add(field);

            Ref xref = new Ref(disease.getOmimNumber(), DatabaseName.OMIM.getDbName());
            refs.add(xref);
        }

    }

    protected void addTaxonomyXrefs(ProteinXml taxonomy, Set<Ref> refs) {

        if (Objects.nonNull(taxonomy.getTaxId())) {

            String taxId = Long.toString(taxonomy.getTaxId());

            Ref xref = new Ref(taxId, DatabaseName.TAXONOMY.getDbName());
            refs.add(xref);

        }
    }

    protected void addCompoundFieldsAndXrefs(ProteinXml compound, Set<Field> fields, Set<Ref> refs) {

        if (Objects.nonNull(compound.getCompoundSource()) && Objects.nonNull(compound.getCompoundId()) && Objects.nonNull(compound.getCompoundName())) {
            Field field = new Field(FieldName.COMPOUND_NAME.getName(), compound.getCompoundName());
            fields.add(field);

            Ref xref = new Ref(compound.getCompoundId(), compound.getCompoundSource().toUpperCase());
            refs.add(xref);
        }

    }

    protected void addPathwaysXrefs(ProteinXml pathway, Set<Ref> refs) {

        if (Objects.nonNull(pathway.getPathwayId())) {
            Ref ref = new Ref(parseReactomePathwayId(pathway.getPathwayId()), DatabaseName.REACTOME.getDbName());
            refs.add(ref);
        }
    }

}
