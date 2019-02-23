package uk.ac.ebi.ep.xml.transformer;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ep.xml.entity.EntryToGeneMapping;
import uk.ac.ebi.ep.xml.entity.EnzymePortalCompound;
import uk.ac.ebi.ep.xml.entity.EnzymePortalDisease;
import uk.ac.ebi.ep.xml.entity.EnzymePortalPathways;
import uk.ac.ebi.ep.xml.entity.EnzymePortalReactant;
import uk.ac.ebi.ep.xml.entity.EnzymePortalReaction;
import uk.ac.ebi.ep.xml.entity.UniprotFamilies;
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

//    private final XmlFileProperties xmlFileProperties;
//
//    public XmlTransformer(XmlFileProperties xmlFileProperties) {
//        Preconditions.checkArgument(xmlFileProperties == null, "xmlFileProperties (XML Configuration) can not be null");
//
//        this.xmlFileProperties = xmlFileProperties;
//    }
//    protected Database buildDatabaseInfo(long entryCount) {
//        Database database = new Database();
//        database.setName(ENZYME_PORTAL);
//        database.setDescription(ENZYME_PORTAL_DESCRIPTION);
//        database.setRelease(xmlFileProperties.getReleaseNumber());
//        LocalDate date = LocalDate.now();
//        database.setReleaseDate(date);
//        database.setEntryCount(entryCount);
//        return database;
//    }
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

    protected void addEcSource(String ec, Set<Ref> refs) {
        if (!StringUtils.isEmpty(ec)) {
            Ref xref = new Ref(ec, DatabaseName.INTENZ.getDbName());
            refs.add(xref);
        }
    }

    protected void addCompoundFieldsAndXrefs(Set<EnzymePortalCompound> compounds, Set<Field> fields, Set<Ref> refs) {

        compounds.stream().map(compound -> {
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
                    addCompoundFieldAndXref(compound, FieldName.COFACTOR.getName(), FieldName.COFACTOR_NAME.getName(), fields, refs);
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

    private void addCompoundFieldAndXref(EnzymePortalCompound compound, String compoundId, String compoundName, Set<Field> fields, Set<Ref> refs) {
        Field field = new Field(compoundId, compound.getCompoundId());
        fields.add(field);
        Field fieldName = new Field(compoundName, compound.getCompoundName());
        fields.add(fieldName);
        Ref xref = new Ref(compound.getCompoundId(), compound.getCompoundSource().toUpperCase());
        refs.add(xref);
    }

//    protected void addCompoundDataFieldsAndXrefs(Set<EnzymePortalCompound> compounds, Set<Field> fields, Set<Ref> refs) {
//        String COFACTOR = "COFACTOR";
//        String INHIBITOR = "INHIBITOR";
//        String ACTIVATOR = "ACTIVATOR";
//
//        compounds
//                .stream()
//                .map(compound -> {
//                    if (compound.getCompoundRole().equalsIgnoreCase(COFACTOR)) {
//                        //String chebiId = compound.getCompoundId().replaceAll("CHEBI:", "");
//                        String chebiId = compound.getCompoundId();
//                        Field cofactor = new Field(FieldName.COFACTOR.getName(), chebiId);
//                        fields.add(cofactor);
//                        Field cofactorName = new Field(FieldName.COFACTOR_NAME.getName(), compound.getCompoundName());
//                        fields.add(cofactorName);
//                    }
//                    return compound;
//                }).map(compound -> {
//            if (compound.getCompoundRole().equalsIgnoreCase(INHIBITOR)) {
//                Field inhibitor = new Field(FieldName.INHIBITOR.getName(), compound.getCompoundId());
//                fields.add(inhibitor);
//                Field inhibitorName = new Field(FieldName.INHIBITOR_NAME.getName(), compound.getCompoundName());
//                fields.add(inhibitorName);
//            }
//            return compound;
//        }).map(compound -> {
//            if (compound.getCompoundRole().equalsIgnoreCase(ACTIVATOR)) {
//                Field activator = new Field(FieldName.ACTIVATOR.getName(), compound.getCompoundId());
//                fields.add(activator);
//                Field activatorName = new Field(FieldName.ACTIVATOR_NAME.getName(), compound.getCompoundName());
//                fields.add(activatorName);
//            }
//            return compound;
//        }).map(compound -> new Ref(compound.getCompoundId(), compound.getCompoundSource()))
//                .forEach(xref -> refs.add(xref));
//
//    }
    protected void addDiseaseFieldsAndXrefs(Set<EnzymePortalDisease> diseases, Set<Field> fields, Set<Ref> refs) {

        diseases.stream().map(disease -> {
            Field field = new Field(FieldName.DISEASE_NAME.getName(), disease.getDiseaseName());
            fields.add(field);
            return new Ref(disease.getOmimNumber(), DatabaseName.OMIM.getDbName());

        }).forEach(xref -> refs.add(xref));

    }

    protected void addEnzymeFamilyField(String ec, Set<Field> fields) {

        EcNumber ecNumber = new EcNumber();
        String enzymeFamily = ecNumber.computeFamily(ec);
        Field field = new Field(FieldName.ENZYME_FAMILY.getName(), enzymeFamily);
        fields.add(field);
    }

    protected void addPathwaysXrefs(Set<EnzymePortalPathways> pathways, Set<Ref> refs) {

        pathways
                .stream()
                .map(pathway -> new Ref(parseReactomePathwayId(pathway.getPathwayId()), DatabaseName.REACTOME.getDbName()))
                .forEach(xref -> refs.add(xref));

    }

    private String parseReactomePathwayId(String reactomePathwayId) {
        if (reactomePathwayId.matches(REACTOME_PATHWAY_ID_REGEX)) {
            String[] sections = reactomePathwayId.split("-");
            return sections[0] + "-" + sections[2];
        }

        return reactomePathwayId;
    }

//    protected void addUniprotFamilyFields(Set<UniprotFamilies> families, Set<Field> fields) {
//        if (!families.isEmpty()) {
//
//            families.stream()
//                    .map(family -> new Field(FieldName.UNIPROT_FAMILY.getName(), family.getFamilyName()))
//                    .forEach(field -> fields.add(field));
//
//        }
//    }
    protected void addUniprotFamilyFieldsAndXrefs(Set<UniprotFamilies> families, Set<Field> fields, Set<Ref> refs) {
        if (!families.isEmpty()) {
            families.stream().map(family -> {
                Field field = new Field(FieldName.UNIPROT_FAMILY.getName(), family.getFamilyName());
                fields.add(field);
                return new Ref(family.getFamilyGroupId(), DatabaseName.UNIPROT_FAMILY.getDbName());

            }).forEach(xref -> refs.add(xref));
        }
    }

//    protected void addReactionFieldsAndXrefs(Set<EnzymePortalReaction> reactions, Set<Field> fields, Set<Ref> refs) {
//
//        reactions.stream().map(reaction -> {
//            Field field = new Field(FieldName.RHEA.getName(), reaction.getReactionId());
//            fields.add(field);
//            return new Ref(reaction.getReactionId(), reaction.getReactionSource());
//
//        }).forEach(xref -> refs.add(xref));
//
//    }
    
        protected void addReactionXrefs(Set<EnzymePortalReaction> reactions, Set<Ref> refs) {
        if (!reactions.isEmpty()) {
            reactions
                    .stream()
                    .map(reaction -> new Ref(reaction.getReactionId(), reaction.getReactionSource()))
                    .forEachOrdered(xref ->  refs.add(xref));
        }
    }

    //use temporarly till fixed in db then use <code>addReactantFieldsAndXrefs</code> instead 
//    protected void addReactantFields(Set<EnzymePortalReactant> reactants, Set<Field> fields) {
//        if (!reactants.isEmpty()) {
//
//            reactants.stream()
//                    .map(reactant -> new Field(FieldName.REACTANT.getName(), reactant.getReactantName()))
//                    .forEach(field -> fields.add(field));
//
//        }
//    }

    
        protected void addReactantFieldsAndXrefs(Set<EnzymePortalReactant> reactants, Set<Field> fields, Set<Ref> refs) {

        reactants.stream().map(reactant -> {
            Field field = new Field(FieldName.REACTANT.getName(),reactant.getReactantName());
            fields.add(field);
            return new Ref(reactant.getReactantId(), reactant.getReactantSource().toUpperCase());

        }).forEach(xref -> refs.add(xref));

    }
}
