package uk.ac.ebi.ep.xml.transformer;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ep.xml.entities.Protein;
import uk.ac.ebi.ep.xml.schema.Field;
import uk.ac.ebi.ep.xml.schema.Ref;
import uk.ac.ebi.ep.xml.util.DatabaseName;
import uk.ac.ebi.ep.xml.util.FieldName;

/**
 *
 * @author Joseph
 */
public abstract class XmlTransformer extends XmlProcessorUtil {

    public static final String ENZYME_PORTAL = "Enzyme Portal";
    public static final String ENZYME_PORTAL_DESCRIPTION = "The Enzyme Portal integrates publicly available information about enzymes, such as small-molecule chemistry, biochemical pathways and drug compounds.";
    private static final String REACTOME_PATHWAY_ID_REGEX = "^R-.*-.*";

    static final String COFACTOR = "COFACTOR";
    static final String INHIBITOR = "INHIBITOR";
    static final String ACTIVATOR = "ACTIVATOR";

    abstract void addUniprotFamilyFieldsAndXrefs(Protein family, Set<Field> fields, Set<Ref> refs);

    abstract void addDiseaseFieldsAndXrefs(Protein disease, Set<Field> fields, Set<Ref> refs);

    protected String withResourceField(String resourceId, String accession, String commonName, int entryType) {
        return String.format("%s;%s;%s;%d", resourceId, accession, commonName, entryType).intern();
    }

    protected void addGeneNameFields(Protein entry, Set<Field> fields) {
        if (!StringUtils.isEmpty(entry.getGeneName())) {
            addField(FieldName.GENE_NAME.getName(), entry.getGeneName(), fields);
        }

    }

    protected void addProteinNameFields(String proteinName, Set<Field> fields) {
        if (!StringUtils.isEmpty(proteinName)) {
            fields.add(new Field(FieldName.PROTEIN_NAME.getName(), proteinName));

        }
    }

    protected void addScientificNameFields(String scientificName, Set<Field> fields) {
        if (!StringUtils.isEmpty(scientificName)) {
            fields.add(new Field(FieldName.SCIENTIFIC_NAME.getName(), scientificName));

        }
    }

    protected void addCommonNameFields(String commonName, Set<Field> fields) {

        if (!StringUtils.isEmpty(commonName)) {
            fields.add(new Field(FieldName.COMMON_NAME.getName(), commonName));

        }
    }

    void computeSynonymsAndBuildFields(String synonymName, String proteinName, Set<Field> fields) {
        Stream.of(synonymName
                .split(";"))
                .distinct()
                .filter(otherName -> (!otherName.trim().equalsIgnoreCase(proteinName.trim())))
                .map(syn -> new Field(FieldName.SYNONYM.getName(), syn))
                .forEach(fields::add);

    }

    protected void addSynonymFields(String synonymNames, String proteinName, Set<Field> fields) {
        if (synonymNames != null && proteinName != null) {

            Optional<String> synonymName = Optional.ofNullable(synonymNames);
            computeSynonymsAndBuildFields(synonymName.orElse(""), proteinName, fields);

        }
    }

    protected void addAccessionXrefs(String accession, Set<Ref> refs) {
        if (!StringUtils.isEmpty(accession)) {
            refs.add(new Ref(accession, DatabaseName.UNIPROTKB.getDbName()));

        }
    }

    protected void addTaxonomyFieldAndXrefs(Protein taxonomy, Set<Field> fields, Set<Ref> refs) {

        if (Objects.nonNull(taxonomy.getTaxId())) {

            fields.add(new Field(FieldName.WITH_TAXONOMY.getName(), withResourceField(Long.toString(taxonomy.getTaxId()), taxonomy.getAccession(), taxonomy.getCommonName(), taxonomy.getEntryType())));
            refs.add(new Ref(Long.toString(taxonomy.getTaxId()), DatabaseName.TAXONOMY.getDbName()));

        }
    }

    protected void addReactantFieldsAndXrefs(Protein reactant, Set<Field> fields, Set<Ref> refs) {

        if (Objects.nonNull(reactant.getReactantSource())) {

            fields.add(new Field(FieldName.REACTANT.getName(), reactant.getReactantName()));
            if (reactant.getReactantSource().toUpperCase().equalsIgnoreCase("CHEBI")) {

                fields.add(new Field(FieldName.CHEBI_ID.getName(), reactant.getReactantId()));
            } else {

                fields.add(new Field(FieldName.RHEA_ID.getName(), reactant.getReactantId()));
            }

            refs.add(new Ref(reactant.getReactantId(), reactant.getReactantSource().toUpperCase()));
        }
    }

    protected void addCompoundDataFieldsAndXrefs(Protein compound, Set<Field> fields, Set<Ref> refs) {

        if (Objects.nonNull(compound.getCompoundSource()) && Objects.nonNull(compound.getCompoundId()) && Objects.nonNull(compound.getCompoundName())) {
            switch (compound.getCompoundRole()) {
                case COFACTOR:
                    addChebiField(compound, fields, refs);
                    addCofactorField(compound, compound.getAccession(), compound.getCommonName(), compound.getEntryType(), fields);
                    break;
                case INHIBITOR:
                    addCompoundFieldAndXref(compound, FieldName.INHIBITOR.getName(), FieldName.INHIBITOR_NAME.getName(), fields, refs);
                    break;
                case ACTIVATOR:
                    addCompoundFieldAndXref(compound, FieldName.ACTIVATOR.getName(), FieldName.ACTIVATOR_NAME.getName(), fields, refs);
                    break;
                default:

                    fields.add(new Field(FieldName.COMPOUND_NAME.getName(), compound.getCompoundName()));
                    refs.add(new Ref(compound.getCompoundId(), compound.getCompoundSource().toUpperCase()));
                    break;
            }
        }

    }

    private void addChebiField(Protein compound, Set<Field> fields, Set<Ref> refs) {

        fields.add(new Field(FieldName.CHEBI_ID.getName(), compound.getCompoundId()));

        refs.add(new Ref(compound.getCompoundId(), compound.getCompoundSource().toUpperCase()));

    }

    private void addCompoundFieldAndXref(Protein compound, String fieldIdkey, String fieldNameKey, Set<Field> fields, Set<Ref> refs) {

        fields.add(new Field(fieldIdkey, compound.getCompoundId()));

        fields.add(new Field(fieldNameKey, compound.getCompoundName()));

        refs.add(new Ref(compound.getCompoundId(), compound.getCompoundSource().toUpperCase()));
    }

    private void addCofactorField(Protein compound, String accession, String commonName, int entryType, Set<Field> fields) {

        fields.add(new Field(FieldName.COFACTOR.getName(), compound.getCompoundId().replace("CHEBI:", "")));

        fields.add(new Field(FieldName.COFACTOR_NAME.getName(), compound.getCompoundName()));

        fields.add(new Field(FieldName.WITH_COFACTOR.getName(), withResourceField(compound.getCompoundId().replace("CHEBI:", ""), accession, commonName, entryType)));

    }

    protected void addPathwayFieldsAndXrefs(Protein pathway, Set<Field> fields, Set<Ref> refs) {

        if (Objects.nonNull(pathway.getPathwayId())) {

            fields.add(new Field(FieldName.WITH_PATHWAY.getName(), withResourceField(pathway.getPathwayId(), pathway.getAccession(), pathway.getCommonName(), pathway.getEntryType())));

            refs.add(new Ref(parseReactomePathwayId(pathway.getPathwayId()), DatabaseName.REACTOME.getDbName()));
        }
    }

    protected String parseReactomePathwayId(String reactomePathwayId) {

        if (reactomePathwayId.matches(REACTOME_PATHWAY_ID_REGEX)) {
            String[] sections = reactomePathwayId.split("-");
            return sections[0] + "-" + sections[2];
        }

        return reactomePathwayId;
    }

    protected void addReactionFieldsAndXrefs(Protein reaction, Set<Field> fields, Set<Ref> refs) {
        if (Objects.nonNull(reaction.getReactionId()) && Objects.nonNull(reaction.getReactionSource())) {

            fields.add(new Field(FieldName.RHEA_ID.getName(), reaction.getReactionId()));
            refs.add(new Ref(reaction.getReactionId(), reaction.getReactionSource()));
        }
    }

}
