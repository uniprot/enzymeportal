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

    static final String CHEBI_PREFIX = "CHEBI:";
    static final String METABOLIGHTS_PREFIX = "MTBLC";
    static final String RHEA_PREFIX = "RHEA:";
    static final String CHEBI = "CHEBI";
    static final String RHEA = "RHEA";
    public static final String ENZYME_PORTAL = "Enzyme Portal";
    public static final String ENZYME_PORTAL_DESCRIPTION = "The Enzyme Portal integrates publicly available information about enzymes, such as small-molecule chemistry, biochemical pathways and drug compounds.";
    private static final String REACTOME_PATHWAY_ID_REGEX = "^R-.*-.*";

    static final String COFACTOR = "COFACTOR";
    static final String INHIBITOR = "INHIBITOR";
    static final String ACTIVATOR = "ACTIVATOR";
    static final String METABOLITE = "METABOLITE";
    static final String REACTANT = "REACTANT";

    abstract void addUniprotFamilyFieldsAndXrefs(Protein family, Set<Field> fields, Set<Ref> refs);

    abstract void addDiseaseFieldsAndXrefs(Protein disease, Set<Field> fields, Set<Ref> refs);

    abstract void addReactantFieldsAndXrefs(Protein reactant, Set<Field> fields, Set<Ref> refs);

    abstract void addPathwayFieldsAndXrefs(Protein pathway, Set<Field> fields, Set<Ref> refs);

    abstract void addChebiCompoundFieldsAndXrefs(Protein entry, Set<Field> fields, Set<Ref> refs);

    abstract void addCompoundFieldsAndXrefs(Protein entry, Set<Field> fields, Set<Ref> refs);

    abstract void addMetaboliteFieldsAndXrefs(Protein entry, Set<Field> fields, Set<Ref> refs);

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

            fields.add(new Field(FieldName.WITH_TAXONOMY.getName(), withResourceField(Long.toString(taxonomy.getTaxId()), taxonomy.getAccession(), taxonomy.getOrganismName(), taxonomy.getEntryType())));
            refs.add(new Ref(Long.toString(taxonomy.getTaxId()), DatabaseName.TAXONOMY.getDbName()));

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
            String rheaId = reaction.getReactionId().replace(RHEA_PREFIX, RHEA.toLowerCase());
            fields.add(new Field(FieldName.RHEAID.getName(), rheaId));
        }
    }

}
