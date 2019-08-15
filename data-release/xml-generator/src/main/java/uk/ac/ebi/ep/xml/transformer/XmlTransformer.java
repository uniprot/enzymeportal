package uk.ac.ebi.ep.xml.transformer;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ep.xml.entities.ProteinXml;
import uk.ac.ebi.ep.xml.schema.Field;
import uk.ac.ebi.ep.xml.schema.Ref;
import uk.ac.ebi.ep.xml.util.DatabaseName;
import uk.ac.ebi.ep.xml.util.FieldName;

/**
 *
 * @author Joseph
 */
public abstract class XmlTransformer extends Transformer {

    public static final String ENZYME_PORTAL = "Enzyme Portal";
    public static final String ENZYME_PORTAL_DESCRIPTION = "The Enzyme Portal integrates publicly available information about enzymes, such as small-molecule chemistry, biochemical pathways and drug compounds.";
    private static final String REACTOME_PATHWAY_ID_REGEX = "^R-.*-.*";

    static final String COFACTOR = "COFACTOR";
    static final String INHIBITOR = "INHIBITOR";
    static final String ACTIVATOR = "ACTIVATOR";

    private String withResourceField(String resourceId, String accession, String commonName, int entryType) {
       return resourceId+";"+accession+";"+commonName+";"+entryType;
        
        //return String.format("%s;%s;%s;%d", resourceId, accession, commonName, entryType);
    }

    protected void addProteinNameFields(String proteinName, Set<Field> fields) {
        if (!StringUtils.isEmpty(proteinName)) {
            //Field field = new Field(FieldName.PROTEIN_NAME.getName(), proteinName);
            fields.add(new Field(FieldName.PROTEIN_NAME.getName(), proteinName));

        }
    }

    protected void addScientificNameFields(String scientificName, Set<Field> fields) {
        if (!StringUtils.isEmpty(scientificName)) {
            //Field field = new Field(FieldName.SCIENTIFIC_NAME.getName(), scientificName);
            fields.add(new Field(FieldName.SCIENTIFIC_NAME.getName(), scientificName));

        }
    }

    protected void addCommonNameFields(String commonName, Set<Field> fields) {

        if (!StringUtils.isEmpty(commonName)) {
            //Field field = new Field(FieldName.COMMON_NAME.getName(), commonName);
            fields.add(new Field(FieldName.COMMON_NAME.getName(), commonName));

        }
    }

    void computeSynonymsAndBuildFields(String synonymName, String proteinName, Set<Field> fields) {
        Stream.of(synonymName
                .split(";"))
                .distinct()
                .filter(otherName -> (!otherName.trim().equalsIgnoreCase(proteinName.trim())))
                .map(syn ->  new Field(FieldName.SYNONYM.getName(), syn))
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
            //Ref xref = new Ref(accession, DatabaseName.UNIPROTKB.getDbName());
            refs.add(new Ref(accession, DatabaseName.UNIPROTKB.getDbName()));

        }
    }

    protected void addTaxonomyFieldAndXrefs(ProteinXml taxonomy, Set<Field> fields, Set<Ref> refs) {

//        String accession = taxonomy.getAccession();
//        String commonName = taxonomy.getCommonName();
//        int entryType = taxonomy.getEntryType();
        if (Objects.nonNull(taxonomy.getTaxId())) {

            //String taxId = Long.toString(taxonomy.getTaxId());

            //String withTaxonomy = withResourceField(taxId, accession, commonName, entryType);
            //Field field = new Field(FieldName.WITH_TAXONOMY.getName(), withTaxonomy);
            fields.add(new Field(FieldName.WITH_TAXONOMY.getName(), withResourceField(Long.toString(taxonomy.getTaxId()), taxonomy.getAccession(), taxonomy.getCommonName(), taxonomy.getEntryType())));
           // Ref xref = new Ref(taxId, DatabaseName.TAXONOMY.getDbName());
            refs.add(new Ref(Long.toString(taxonomy.getTaxId()), DatabaseName.TAXONOMY.getDbName()));

        }
    }

    protected void addReactantFieldsAndXrefs(ProteinXml reactant, Set<Field> fields, Set<Ref> refs) {

        if (Objects.nonNull(reactant.getReactantSource())) {
            //Field field = new Field(FieldName.REACTANT.getName(), reactant.getReactantName());
            fields.add(new Field(FieldName.REACTANT.getName(), reactant.getReactantName()));
            if (reactant.getReactantSource().toUpperCase().equalsIgnoreCase("CHEBI")) {
                //Field chebi = new Field(FieldName.CHEBI_ID.getName(), reactant.getReactantId());
                fields.add(new Field(FieldName.CHEBI_ID.getName(), reactant.getReactantId()));
            } else {
                //Field rheaComp = new Field(FieldName.RHEA_ID.getName(), reactant.getReactantId());
                fields.add(new Field(FieldName.RHEA_ID.getName(), reactant.getReactantId()));
            }

           // Ref xref = new Ref(reactant.getReactantId(), reactant.getReactantSource().toUpperCase());
            refs.add(new Ref(reactant.getReactantId(), reactant.getReactantSource().toUpperCase()));
        }
    }

    protected void addCompoundDataFieldsAndXrefs(ProteinXml compound, Set<Field> fields, Set<Ref> refs) {
//        String accession = compound.getAccession();
//        String commonName = compound.getCommonName();
//        int entryType = compound.getEntryType();
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
                   // Field field = new Field(FieldName.COMPOUND_NAME.getName(), compound.getCompoundName());
                    fields.add(new Field(FieldName.COMPOUND_NAME.getName(), compound.getCompoundName()));
                    //Ref xref = new Ref(compound.getCompoundId(), compound.getCompoundSource().toUpperCase());
                    refs.add(new Ref(compound.getCompoundId(), compound.getCompoundSource().toUpperCase()));
                    break;
            }
        }

    }

    private void addChebiField(ProteinXml compound, Set<Field> fields, Set<Ref> refs) {

        //Field chebi = new Field(FieldName.CHEBI_ID.getName(), compound.getCompoundId());
        fields.add(new Field(FieldName.CHEBI_ID.getName(), compound.getCompoundId()));
        //Ref xref = new Ref(compound.getCompoundId(), compound.getCompoundSource().toUpperCase());
        refs.add(new Ref(compound.getCompoundId(), compound.getCompoundSource().toUpperCase()));

    }

    private void addCompoundFieldAndXref(ProteinXml compound, String fieldIdkey, String fieldNameKey, Set<Field> fields, Set<Ref> refs) {
        //Field fieldId = new Field(fieldIdkey, compound.getCompoundId());
        fields.add(new Field(fieldIdkey, compound.getCompoundId()));
        //Field fieldName = new Field(fieldNameKey, compound.getCompoundName());
        fields.add(new Field(fieldNameKey, compound.getCompoundName()));
       // Ref xref = new Ref(compound.getCompoundId(), compound.getCompoundSource().toUpperCase());
        refs.add(new Ref(compound.getCompoundId(), compound.getCompoundSource().toUpperCase()));
    }

    private void addCofactorField(ProteinXml compound, String accession, String commonName, int entryType, Set<Field> fields) {

        //String cofactorId = compound.getCompoundId().replace("CHEBI:", "");

        Field cofactor = new Field(FieldName.COFACTOR.getName(), compound.getCompoundId().replace("CHEBI:", ""));
        fields.add(cofactor);
        Field cofactorName = new Field(FieldName.COFACTOR_NAME.getName(), compound.getCompoundName());
        fields.add(cofactorName);
        String withCofactor = withResourceField(compound.getCompoundId().replace("CHEBI:", ""), accession, commonName, entryType);
        Field identityField = new Field(FieldName.WITH_COFACTOR.getName(), withCofactor);
        fields.add(identityField);

    }

    protected void addDiseaseFieldsAndXrefs(ProteinXml disease, Set<Field> fields, Set<Ref> refs) {
//        String accession = disease.getAccession();
//        String commonName = disease.getCommonName();
//        int entryType = disease.getEntryType();

        if (Objects.nonNull(disease.getOmimNumber()) && Objects.nonNull(disease.getDiseaseName())) {
            Field field = new Field(FieldName.DISEASE_NAME.getName(), disease.getDiseaseName());
            fields.add(field);

            //String withDisease = withResourceField(disease.getOmimNumber(), disease.getAccession(), disease.getCommonName(), disease.getEntryType());
            Field identityField = new Field(FieldName.WITH_DISEASE.getName(), withResourceField(disease.getOmimNumber(), disease.getAccession(), disease.getCommonName(), disease.getEntryType()));
            fields.add(identityField);

            Ref xref = new Ref(disease.getOmimNumber(), DatabaseName.OMIM.getDbName());
            refs.add(xref);
        }
    }

    protected void addPathwayFieldsAndXrefs(ProteinXml pathway, Set<Field> fields, Set<Ref> refs) {
//        String accession = pathway.getAccession();
//        String commonName = pathway.getCommonName();
//        int entryType = pathway.getEntryType();
        if (Objects.nonNull(pathway.getPathwayId())) {
            //String withPathway = withResourceField(pathway.getPathwayId(), pathway.getAccession(), pathway.getCommonName(), pathway.getEntryType());
            Field identityField = new Field(FieldName.WITH_PATHWAY.getName(), withResourceField(pathway.getPathwayId(), pathway.getAccession(), pathway.getCommonName(), pathway.getEntryType()));
            fields.add(identityField);
            Ref ref = new Ref(parseReactomePathwayId(pathway.getPathwayId()), DatabaseName.REACTOME.getDbName());
            refs.add(ref);
        }
    }

    protected String parseReactomePathwayId(String reactomePathwayId) {
       //Pattern.compile(REACTOME_PATHWAY_ID_REGEX, Pattern.CASE_INSENSITIVE);
        
        if (reactomePathwayId.matches(REACTOME_PATHWAY_ID_REGEX)) {
            String[] sections = reactomePathwayId.split("-");
            return sections[0] + "-" + sections[2];
        }

        return reactomePathwayId;
    }

    protected void addReactionFieldsAndXrefs(ProteinXml reaction, Set<Field> fields, Set<Ref> refs) {
        if (Objects.nonNull(reaction.getReactionId()) && Objects.nonNull(reaction.getReactionSource())) {
            Field field = new Field(FieldName.RHEA_ID.getName(), reaction.getReactionId());
            fields.add(field);
            Ref xref = new Ref(reaction.getReactionId(), reaction.getReactionSource());
            refs.add(xref);
        }
    }

    protected void addUniprotFamilyFieldsAndXrefs(ProteinXml family, Set<Field> fields, Set<Ref> refs) {
//        String accession = family.getAccession();
//        String commonName = family.getCommonName();
//        int entryType = family.getEntryType();

        if (Objects.nonNull(family.getFamilyGroupId()) && Objects.nonNull(family.getFamilyName())) {
            Field field = new Field(FieldName.PROTEIN_FAMILY.getName(), family.getFamilyName());
            fields.add(field);
            Field fieldId = new Field(FieldName.PROTEIN_FAMILY_ID.getName(), family.getFamilyGroupId());
            fields.add(fieldId);

            //String withFamily = withResourceField(family.getFamilyGroupId(), family.getAccession(), family.getCommonName(), family.getEntryType());
            Field identityField = new Field(FieldName.WITH_PROTEIN_FAMILY.getName(), withResourceField(family.getFamilyGroupId(), family.getAccession(), family.getCommonName(), family.getEntryType()));
            fields.add(identityField);

            Ref xref = new Ref(family.getFamilyGroupId(), DatabaseName.PROTEIN_FAMILY.getDbName());
            refs.add(xref);
        }
    }

}
