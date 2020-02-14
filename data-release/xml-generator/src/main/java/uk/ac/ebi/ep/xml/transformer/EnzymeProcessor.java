package uk.ac.ebi.ep.xml.transformer;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ep.xml.entities.EnzymePortalUniqueEc;
import uk.ac.ebi.ep.xml.entities.IntenzAltNames;
import uk.ac.ebi.ep.xml.entities.Protein;
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

    private final AtomicInteger count = new AtomicInteger(0);
    private final ProteinXmlRepository proteinXmlRepository;

    public EnzymeProcessor(ProteinXmlRepository proteinXmlRepository) {
        this.proteinXmlRepository = proteinXmlRepository;
    }

    @Override
    public Entry process(EnzymePortalUniqueEc enzyme) throws Exception {

        Set<Field> fields = new HashSet<>();
        Set<Ref> refs = new HashSet<>();
        AdditionalFields additionalFields = new AdditionalFields();
        CrossReferences cr = new CrossReferences();

        //if (log.isDebugEnabled()) {
        log.debug("Processor " + Runtime.getRuntime().availableProcessors() + " current entry : " + enzyme.getEcNumber() + "  entry count : " + count.getAndIncrement());
        //}
        Entry entry = new Entry();
        entry.setId(enzyme.getEcNumber());
        entry.setName(enzyme.getEnzymeName());

        entry.setDescription(String.format("%s %s", enzyme.getEcNumber(), enzyme.getEnzymeName()));

        addEnzymeFamilyField(enzyme.getEcNumber(), fields);

        addIntenzCofactorsField(enzyme.getCofactor(), fields);
        addCatalyticActivityField(enzyme.getCatalyticActivity(), fields);
        addAltNamesField(enzyme.getIntenzAltNamesSet(), fields);
        addEcSource(enzyme.getEcNumber(), refs);

        addProteinInformation(enzyme, fields, refs);

        additionalFields.setField(fields);
        entry.setAdditionalFields(additionalFields);

        cr.setRef(refs);
        entry.setCrossReferences(cr);

        return entry;
    }

    private void addProteinInformation(EnzymePortalUniqueEc enzyme, Set<Field> fields, Set<Ref> refs) {
        try (Stream<Protein> protein = proteinXmlRepository.streamProteinByEcNumber(enzyme.getEcNumber())) {
            protein
                    .parallel()
                    .forEach(data -> processUniprotEntry(data, fields, refs));
        }
    }

    private void processUniprotEntry(Protein uniprotEntry, Set<Field> fields, Set<Ref> refs) {

        addProteinNameFields(uniprotEntry.getProteinName(), fields);

        addScientificNameFields(uniprotEntry.getScientificName(), fields);
        addCommonNameFields(uniprotEntry.getCommonName(), fields);
        addGeneNameFields(uniprotEntry, fields);

        addUniprotFamilyFieldsAndXrefs(uniprotEntry, fields, refs);

        addSynonymFields(uniprotEntry.getSynonymNames(), uniprotEntry.getProteinName(), fields);

        addAccessionXrefs(uniprotEntry.getAccession(), refs);
        addTaxonomyXrefs(uniprotEntry, refs);
        addChebiSynonymField(uniprotEntry, fields);
        addChebiCompoundFieldsAndXrefs(uniprotEntry, fields, refs);
        addCompoundFieldsAndXrefs(uniprotEntry, fields, refs);
        addMetaboliteFieldsAndXrefs(uniprotEntry, fields, refs);
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

    private void addIntenzCofactorsField(String cofactor, Set<Field> fields) {
        if (Objects.nonNull(cofactor)) {
            fields.add(new Field(FieldName.INTENZ_COFACTORS.getName(), cofactor));
        }
    }

    private void addCatalyticActivityField(String catalyticActivity, Set<Field> fields) {
        if (Objects.nonNull(catalyticActivity)) {
            fields.add(new Field(FieldName.CATALYTIC_ACTIVITY.getName(), catalyticActivity));
        }
    }

    private void addEnzymeFamilyField(String ec, Set<Field> fields) {
        if (Objects.nonNull(ec)) {
            fields.add(new Field(FieldName.ENZYME_FAMILY.getName(), computeFamily(ec)));
        }
    }

    private void addEcSource(String ec, Set<Ref> refs) {
        if (!StringUtils.isEmpty(ec)) {
            refs.add(new Ref(ec, DatabaseName.INTENZ.getDbName()));
        }
    }

    @Override
    void addUniprotFamilyFieldsAndXrefs(Protein family, Set<Field> fields, Set<Ref> refs) {

        if (Objects.nonNull(family.getFamilyGroupId()) && Objects.nonNull(family.getFamilyName())) {

            fields.add(new Field(FieldName.PROTEIN_FAMILY_NAME.getName(), family.getFamilyName()));
            fields.add(new Field(FieldName.PROTEIN_FAMILY_ID.getName(), family.getFamilyGroupId()));
            refs.add(new Ref(family.getFamilyGroupId(), DatabaseName.PROTEIN_FAMILY.getDbName()));
            fields.add(new Field(FieldName.HAS_PROTEIN_FAMILY.getName(), HAS_PROTEIN_FAMILY));
        }
    }

    @Override
    void addDiseaseFieldsAndXrefs(Protein disease, Set<Field> fields, Set<Ref> refs) {

        if (Objects.nonNull(disease.getOmimNumber()) && Objects.nonNull(disease.getDiseaseName())) {

            fields.add(new Field(FieldName.DISEASE_NAME.getName(), disease.getDiseaseName()));
            fields.add(new Field(FieldName.HAS_DISEASE.getName(), HAS_DISEASE));
            refs.add(new Ref(disease.getOmimNumber(), DatabaseName.OMIM.getDbName()));
        }

    }

    @Override
    void addReactantFieldsAndXrefs(Protein reactant, Set<Field> fields, Set<Ref> refs) {

        if (Objects.nonNull(reactant.getReactantSource())) {

            fields.add(new Field(FieldName.REACTANT.getName(), reactant.getReactantName()));
            if (reactant.getReactantSource().toUpperCase().equalsIgnoreCase(RHEA)) {
                fields.add(new Field(FieldName.RHEA_ID.getName(), reactant.getReactantId()));
                String rheaId = reactant.getReactionId().replace(RHEA_PREFIX, "rhea");
                fields.add(new Field(FieldName.RHEAID.getName(), rheaId));
            }
//            if (reactant.getReactantSource().toUpperCase().equalsIgnoreCase("CHEBI")) {
//
//                fields.add(new Field(FieldName.CHEBI_ID.getName(), reactant.getReactantId()));
//            } else {
//
//                fields.add(new Field(FieldName.RHEA_ID.getName(), reactant.getReactantId()));
//            }

            refs.add(new Ref(reactant.getReactantId(), reactant.getReactantSource().toUpperCase()));
        }
    }

    @Override
    void addPathwayFieldsAndXrefs(Protein pathway, Set<Field> fields, Set<Ref> refs) {

        if (Objects.nonNull(pathway.getPathwayId())) {

            fields.add(new Field(FieldName.WITH_PATHWAY.getName(), withResourceField(parseReactomePathwayId(pathway.getPathwayId()), pathway.getAccession(), pathway.getCommonName(), pathway.getEntryType())));
            fields.add(new Field(FieldName.HAS_PATHWAY.getName(), HAS_PATHWAY));
            if (Objects.nonNull(pathway.getPathwayName())) {
                fields.add(new Field(FieldName.PATHWAY_NAME.getName(), pathway.getPathwayName()));
            }
            refs.add(new Ref(parseReactomePathwayId(pathway.getPathwayId()), DatabaseName.REACTOME.getDbName()));
        }
    }

    private void addTaxonomyXrefs(Protein taxonomy, Set<Ref> refs) {

        if (Objects.nonNull(taxonomy.getTaxId())) {

            refs.add(new Ref(Long.toString(taxonomy.getTaxId()), DatabaseName.TAXONOMY.getDbName()));

        }
    }

    private void addChebiSynonymField(Protein chebiCompound, Set<Field> fields) {
        if (Objects.nonNull(chebiCompound.getChebiSynonyms())) {
            Stream.of(chebiCompound.getChebiSynonyms()
                    .split(";"))
                    .distinct()
                    .map(syn -> new Field(FieldName.CHEBI_SYNONYMS.getName(), syn))
                    .forEach(fields::add);
        }

    }

    private void addCompoundFieldsAndXrefs(Protein compound, Set<Field> fields, Set<Ref> refs) {

        if (Objects.nonNull(compound.getCompoundSource()) && Objects.nonNull(compound.getCompoundId()) && Objects.nonNull(compound.getCompoundName())) {

            if (compound.getCompoundRole().equalsIgnoreCase(COFACTOR)) {
                fields.add(new Field(FieldName.HAS_COFACTOR.getName(), HAS_COFACTOR));
                String cofactorId = compound.getCompoundId().replace("CHEBI:", "cofactor");
                fields.add(new Field(FieldName.COFACTOR.getName(), cofactorId));

            }

            fields.add(new Field(FieldName.COMPOUND_NAME.getName(), compound.getCompoundName()));
            fields.add(new Field(FieldName.CHEBI_ID.getName(), compound.getCompoundId()));
            refs.add(new Ref(compound.getCompoundId(), compound.getCompoundSource().toUpperCase()));

        }

    }

    private void addChebiCompoundFieldsAndXrefs(Protein chebiCompound, Set<Field> fields, Set<Ref> refs) {

        if (Objects.nonNull(chebiCompound.getChebiCompoundRole()) && Objects.nonNull(chebiCompound.getChebiCompoundId()) && Objects.nonNull(chebiCompound.getChebiCompoundName())) {

//            if (chebiCompound.getChebiCompoundRole().equalsIgnoreCase(COFACTOR)) {
//                fields.add(new Field(FieldName.HAS_COFACTOR.getName(), HAS_COFACTOR));
//                String cofactorId = chebiCompound.getChebiCompoundId().replace(CHEBI_PREFIX, "cofactor");
//                fields.add(new Field(FieldName.COFACTOR.getName(), cofactorId));
//
//            }
//            if (chebiCompound.getChebiCompoundRole().equalsIgnoreCase(METABOLITE)) {
//                fields.add(new Field(FieldName.HAS_METABOLITE.getName(), HAS_METABOLITE));
//                String metaboliteId = chebiCompound.getChebiCompoundId().replace(CHEBI_PREFIX, "metabolite");
//                fields.add(new Field(FieldName.METABOLITE.getName(), metaboliteId));
//
//            }
            fields.add(new Field(FieldName.COMPOUND_NAME.getName(), chebiCompound.getChebiCompoundName()));
            fields.add(new Field(FieldName.CHEBI_ID.getName(), chebiCompound.getChebiCompoundId()));
            refs.add(new Ref(chebiCompound.getChebiCompoundId(), CHEBI));
            String chebiId = chebiCompound.getChebiCompoundId().replace(CHEBI_PREFIX, "chebi");
            fields.add(new Field(FieldName.CHEBIID.getName(), chebiId));

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

    private void addPathwaysXrefs(Protein pathway, Set<Ref> refs) {

        if (Objects.nonNull(pathway.getPathwayId())) {
            refs.add(new Ref(parseReactomePathwayId(pathway.getPathwayId()), DatabaseName.REACTOME.getDbName()));
        }
    }

}
