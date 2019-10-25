package uk.ac.ebi.ep.xml.transformer;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ep.xml.entities.EntryToGeneMapping;
import uk.ac.ebi.ep.xml.entities.EnzymePortalCompound;
import uk.ac.ebi.ep.xml.entities.EnzymePortalDisease;
import uk.ac.ebi.ep.xml.entities.EnzymePortalPathways;
import uk.ac.ebi.ep.xml.entities.EnzymePortalReactant;
import uk.ac.ebi.ep.xml.entities.EnzymePortalReaction;
import uk.ac.ebi.ep.xml.entities.EnzymePortalUniprotFamilies;
import uk.ac.ebi.ep.xml.schema.Field;
import uk.ac.ebi.ep.xml.schema.Ref;
import uk.ac.ebi.ep.xml.util.DatabaseName;
import uk.ac.ebi.ep.xml.util.FieldName;

/**
 *
 * @author Joseph
 */
public abstract class XmlTransformer {

    public static final String ENZYME_PORTAL = "Enzyme Portal";
    public static final String ENZYME_PORTAL_DESCRIPTION = "The Enzyme Portal integrates publicly available information about enzymes, such as small-molecule chemistry, biochemical pathways and drug compounds.";
    private static final String REACTOME_PATHWAY_ID_REGEX = "^R-.*-.*";

    static final String COFACTOR = "COFACTOR";
    static final String INHIBITOR = "INHIBITOR";
    static final String ACTIVATOR = "ACTIVATOR";

    protected void addUniprotIdFields(String uniprotName, Set<Field> fields) {
        if (!StringUtils.isEmpty(uniprotName)) {
            Field field = new Field(FieldName.UNIPROT_NAME.getName(), uniprotName);
            fields.add(field);

        }
    }

    protected void addProteinNameFields(String proteinName, Set<Field> fields) {
        if (!StringUtils.isEmpty(proteinName)) {
            Field field = new Field(FieldName.PROTEIN_NAME.getName(), proteinName);
            fields.add(field);

        }
    }

    protected void addScientificNameFields(String scientificName, Set<Field> fields) {
        if (!StringUtils.isEmpty(scientificName)) {
            Field field = new Field(FieldName.SCIENTIFIC_NAME.getName(), scientificName);
            fields.add(field);

        }
    }

    protected void addCommonNameFields(String commonName, Set<Field> fields) {

//        if (commonName == null || StringUtils.isEmpty(commonName)) {
//            commonName = uniprotEntry.getScientificName();
//        }
        if (!StringUtils.isEmpty(commonName)) {
            Field field = new Field(FieldName.COMMON_NAME.getName(), commonName + " ");
            fields.add(field);

        }
    }

    void computeSynonymsAndBuildFields(String synonymName, String proteinName, Set<Field> fields) {
        Stream.of(synonymName
                .split(";"))
                .distinct()
                .filter(otherName -> (!otherName.trim().equalsIgnoreCase(proteinName.trim())))
                .map(syn -> {
                    return new Field(FieldName.SYNONYM.getName(), syn);

                }).forEach(field -> fields.add(field));

    }

    protected void addSynonymFields(String synonymNames, String proteinName, Set<Field> fields) {
        if (synonymNames != null && proteinName != null) {

            Optional<String> synonymName = Optional.ofNullable(synonymNames);
            computeSynonymsAndBuildFields(synonymName.orElse(""), proteinName, fields);

        }
    }

    protected void addGeneNameFields(Set<EntryToGeneMapping> geneMappings, Set<Field> fields) {
        if (!geneMappings.isEmpty()) {

            geneMappings.stream()
                    .map(geneMapping -> new Field(FieldName.GENE_NAME.getName(), geneMapping.getGeneName()))
                    .forEach(field -> fields.add(field));

        }
    }

    protected void addAccessionXrefs(String accession, Set<Ref> refs) {
        if (!StringUtils.isEmpty(accession)) {
            Ref xref = new Ref(accession, DatabaseName.UNIPROTKB.getDbName());
            refs.add(xref);

        }
    }

    protected void addTaxonomyXrefs(Long taxonomy, Set<Ref> refs) {
        String taxId = Long.toString(taxonomy);
        if (!StringUtils.isEmpty(taxId)) {
            Ref xref = new Ref(taxId, DatabaseName.TAXONOMY.getDbName());
            refs.add(xref);

        }
    }

    protected void addTaxonomyFieldAndXrefs(Long taxonomy, String accession, String commonName, int entryType, Set<Field> fields, Set<Ref> refs) {
        String taxId = Long.toString(taxonomy);
        if (!StringUtils.isEmpty(taxId)) {
            String withTaxonomy = String.format("%s;%s;%s;%d", taxId, accession, commonName, entryType);
            Field field = new Field(FieldName.WITH_TAXONOMY.getName(), withTaxonomy);
            fields.add(field);
            Ref xref = new Ref(taxId, DatabaseName.TAXONOMY.getDbName());
            refs.add(xref);

        }
    }

    protected void addEcSource(String ec, Set<Ref> refs) {
        if (!StringUtils.isEmpty(ec)) {
            Ref xref = new Ref(ec, DatabaseName.INTENZ.getDbName());
            refs.add(xref);
        }
    }

    protected void addCompoundFieldsAndXrefs(Set<EnzymePortalCompound> compounds, Set<Field> fields, Set<Ref> refs) {

        compounds.stream()
                .map(compound -> {
                    Field field = new Field(FieldName.COMPOUND_NAME.getName(), compound.getCompoundName());
                    //Field compoundType = new Field(FieldName.COMPOUND_TYPE.getName(), compound.getCompoundRole());
                    fields.add(field);
                    //fields.add(compoundType);
                    return new Ref(compound.getCompoundId(), compound.getCompoundSource().toUpperCase());

                }).forEach(xref -> {
            refs.add(xref);
        });

    }

    protected void addCompoundDataFieldsAndXrefs(Set<EnzymePortalCompound> compounds, Set<Field> fields, Set<Ref> refs) {

        for (EnzymePortalCompound compound : compounds) {
            switch (compound.getCompoundRole()) {
                case COFACTOR:
                    addChebiField(compound, fields, refs);
                    addCofactorField(compound, fields);
                    break;
                case INHIBITOR:
                    addCompoundFieldAndXref(compound, FieldName.INHIBITOR.getName(), FieldName.INHIBITOR_NAME.getName(), fields, refs);
                    break;
                case ACTIVATOR:
                    addCompoundFieldAndXref(compound, FieldName.ACTIVATOR.getName(), FieldName.ACTIVATOR_NAME.getName(), fields, refs);
                    break;
                default:
                    Field field = new Field(FieldName.COMPOUND_NAME.getName(), compound.getCompoundName());
                    fields.add(field);
                    Ref xref = new Ref(compound.getCompoundId(), compound.getCompoundSource().toUpperCase());
                    refs.add(xref);
                    break;
            }
        }
    }

    protected void addCompoundDataFieldsAndXrefs(Set<EnzymePortalCompound> compounds, String accession, String commonName, int entryType, Set<Field> fields, Set<Ref> refs) {

        if (compounds != null | !compounds.isEmpty()) {
            for (EnzymePortalCompound compound : compounds) {
                switch (compound.getCompoundRole()) {
                    case COFACTOR:
                        addChebiField(compound, fields, refs);
                        addCofactorField(compound, accession, commonName, entryType, fields);
                        break;
                    case INHIBITOR:
                        addCompoundFieldAndXref(compound, FieldName.INHIBITOR.getName(), FieldName.INHIBITOR_NAME.getName(), fields, refs);
                        break;
                    case ACTIVATOR:
                        addCompoundFieldAndXref(compound, FieldName.ACTIVATOR.getName(), FieldName.ACTIVATOR_NAME.getName(), fields, refs);
                        break;
                    default:
                        Field field = new Field(FieldName.COMPOUND_NAME.getName(), compound.getCompoundName());
                        fields.add(field);
                        Ref xref = new Ref(compound.getCompoundId(), compound.getCompoundSource().toUpperCase());
                        refs.add(xref);
                        break;
                }
            }
        }
    }

    private void addCompoundFieldAndXref(EnzymePortalCompound compound, String fieldIdkey, String fieldNameKey, Set<Field> fields, Set<Ref> refs) {
        Field fieldId = new Field(fieldIdkey, compound.getCompoundId());
        fields.add(fieldId);
        Field fieldName = new Field(fieldNameKey, compound.getCompoundName());
        fields.add(fieldName);
        Ref xref = new Ref(compound.getCompoundId(), compound.getCompoundSource().toUpperCase());
        refs.add(xref);
    }

    private void addCofactorField(EnzymePortalCompound compound, Set<Field> fields) {

        String cofactorId = compound.getCompoundId().replaceAll("CHEBI:", "");

        Field cofactor = new Field(FieldName.COFACTOR.getName(), cofactorId);
        fields.add(cofactor);
        Field cofactorName = new Field(FieldName.COFACTOR_NAME.getName(), compound.getCompoundName());
        fields.add(cofactorName);

    }

    private void addCofactorField(EnzymePortalCompound compound, String accession, String commonName, int entryType, Set<Field> fields) {
        if (compound != null) {
            String cofactorId = compound.getCompoundId().replaceAll("CHEBI:", "");

            Field cofactor = new Field(FieldName.COFACTOR.getName(), cofactorId);
            fields.add(cofactor);
            Field cofactorName = new Field(FieldName.COFACTOR_NAME.getName(), compound.getCompoundName());
            fields.add(cofactorName);
            String withCofactor = String.format("%s;%s;%s;%d", cofactorId, accession, commonName, entryType);
            Field identityField = new Field(FieldName.WITH_COFACTOR.getName(), withCofactor);
            fields.add(identityField);
        }

    }

    private void addChebiField(EnzymePortalCompound compound, Set<Field> fields, Set<Ref> refs) {

        Field chebi = new Field(FieldName.CHEBI_ID.getName(), compound.getCompoundId());
        fields.add(chebi);
        Ref xref = new Ref(compound.getCompoundId(), compound.getCompoundSource().toUpperCase());
        refs.add(xref);

    }

    protected void addDiseaseFieldsAndXrefs(Set<EnzymePortalDisease> diseases, Set<Field> fields, Set<Ref> refs) {

        diseases.stream()
                .map(disease -> {
                    Field field = new Field(FieldName.DISEASE_NAME.getName(), disease.getDiseaseName());
                    fields.add(field);
                    return new Ref(disease.getOmimNumber(), DatabaseName.OMIM.getDbName());

                }).forEach(xref -> refs.add(xref));

    }

    protected void addDiseaseFieldsAndXrefs(Set<EnzymePortalDisease> diseases, String accession, String commonName, int entryType, Set<Field> fields, Set<Ref> refs) {
        if (!diseases.isEmpty()) {

            diseases.forEach(disease -> addDiseaseFieldsXrefs(disease, accession, commonName, entryType, fields, refs));
        }

    }

    private void addDiseaseFieldsXrefs(EnzymePortalDisease disease, String accession, String commonName, int entryType, Set<Field> fields, Set<Ref> refs) {
        Field field = new Field(FieldName.DISEASE_NAME.getName(), disease.getDiseaseName());
        fields.add(field);

        String withDisease = String.format("%s;%s;%s;%d", disease.getOmimNumber(), accession, commonName, entryType);
        Field identityField = new Field(FieldName.WITH_DISEASE.getName(), withDisease);
        fields.add(identityField);

        Ref xref = new Ref(disease.getOmimNumber(), DatabaseName.OMIM.getDbName());
        refs.add(xref);
    }

    protected void addEnzymeFamilyField(String ec, Set<Field> fields) {

        EcNumber ecNumber = new EcNumber();
        String enzymeFamily = ecNumber.computeFamily(ec);
        Field field = new Field(FieldName.ENZYME_FAMILY.getName(), enzymeFamily);
        fields.add(field);
    }

    protected void addPathwayFieldsAndXrefs(Set<EnzymePortalPathways> pathways, String accession, String commonName, int entryType, Set<Field> fields, Set<Ref> refs) {
        if (!pathways.isEmpty()) {

            pathways.forEach(pathway -> addPathwayFieldsXrefs(pathway, accession, commonName, entryType, fields, refs));

        }

    }

    private void addPathwayFieldsXrefs(EnzymePortalPathways pathway, String accession, String commonName, int entryType, Set<Field> fields, Set<Ref> refs) {
        String withPathway = String.format("%s;%s;%s;%d", pathway.getPathwayId(), accession, commonName, entryType);
        Field identityField = new Field(FieldName.WITH_PATHWAY.getName(), withPathway);
        fields.add(identityField);
        Ref ref = new Ref(parseReactomePathwayId(pathway.getPathwayId()), DatabaseName.REACTOME.getDbName());
        refs.add(ref);
    }

    protected void addPathwaysXrefs(Set<EnzymePortalPathways> pathways, Set<Ref> refs) {
        if (!pathways.isEmpty()) {
            pathways
                    .stream()
                    .map(pathway -> new Ref(parseReactomePathwayId(pathway.getPathwayId()), DatabaseName.REACTOME.getDbName()))
                    .forEach(xref -> refs.add(xref));
        }

    }

    private String parseReactomePathwayId(String reactomePathwayId) {
        if (reactomePathwayId.matches(REACTOME_PATHWAY_ID_REGEX)) {
            String[] sections = reactomePathwayId.split("-");
            return sections[0] + "-" + sections[2];
        }

        return reactomePathwayId;
    }

    protected void addUniprotFamilyFieldsAndXrefs(Set<EnzymePortalUniprotFamilies> families, Set<Field> fields, Set<Ref> refs) {
        if (!families.isEmpty()) {
            families.stream()
                    .map(family -> {
                        Field field = new Field(FieldName.PROTEIN_FAMILY.getName(), family.getFamilyName());
                        fields.add(field);
                        Field fieldId = new Field(FieldName.PROTEIN_FAMILY_ID.getName(), family.getFamilyGroupId());
                        fields.add(fieldId);
                        return new Ref(family.getFamilyGroupId(), DatabaseName.PROTEIN_FAMILY.getDbName());

                    }).forEach(xref -> refs.add(xref));
        }
    }

    protected void addUniprotFamilyFieldsAndXrefs(Set<EnzymePortalUniprotFamilies> families, String accession, String commonName, int entryType, Set<Field> fields, Set<Ref> refs) {

        if (!families.isEmpty()) {

            families.forEach(family -> addUniprotFamilyFieldsXrefs(family, accession, commonName, entryType, fields, refs));
        }
    }

    private void addUniprotFamilyFieldsXrefs(EnzymePortalUniprotFamilies family, String accession, String commonName, int entryType, Set<Field> fields, Set<Ref> refs) {
        Field field = new Field(FieldName.PROTEIN_FAMILY.getName(), family.getFamilyName());
        fields.add(field);
        Field fieldId = new Field(FieldName.PROTEIN_FAMILY_ID.getName(), family.getFamilyGroupId());
        fields.add(fieldId);

        String withFamily = String.format("%s;%s;%s;%d", family.getFamilyGroupId(), accession, commonName, entryType);
        Field identityField = new Field(FieldName.WITH_PROTEIN_FAMILY.getName(), withFamily);
        fields.add(identityField);

        Ref xref = new Ref(family.getFamilyGroupId(), DatabaseName.PROTEIN_FAMILY.getDbName());
        refs.add(xref);
    }

    protected void addReactionFieldsAndXrefs(Set<EnzymePortalReaction> reactions, Set<Field> fields, Set<Ref> refs) {
        if (!reactions.isEmpty()) {

            reactions.forEach((reaction) -> addReactionFieldsXrefs(reaction, fields, refs));
        }

    }

    private void addReactionFieldsXrefs(EnzymePortalReaction reaction, Set<Field> fields, Set<Ref> refs) {
        Field field = new Field(FieldName.RHEA_ID.getName(), reaction.getReactionId());
        fields.add(field);
        Ref xref = new Ref(reaction.getReactionId(), reaction.getReactionSource());
        refs.add(xref);
    }

    protected void addReactantFieldsAndXrefs(Set<EnzymePortalReactant> reactants, Set<Field> fields, Set<Ref> refs) {
        if (!reactants.isEmpty()) {
            reactants.forEach(reactant -> addReactantFieldsXrefs(reactant, fields, refs));

        }

    }

    private void addReactantFieldsXrefs(EnzymePortalReactant reactant, Set<Field> fields, Set<Ref> refs) {
        Field field = new Field(FieldName.REACTANT.getName(), reactant.getReactantName());
        fields.add(field);
        if (reactant.getReactantSource().toUpperCase().equalsIgnoreCase("CHEBI")) {
            Field chebi = new Field(FieldName.CHEBI_ID.getName(), reactant.getReactantId());
            fields.add(chebi);
        } else {
            Field rhea_comp = new Field(FieldName.RHEA_ID.getName(), reactant.getReactantId());
            fields.add(rhea_comp);
        }

        Ref xref = new Ref(reactant.getReactantId(), reactant.getReactantSource().toUpperCase());
        refs.add(xref);
    }

}
