package uk.ac.ebi.ep.xml.transformer;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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
 * @author joseph
 */
//@Deprecated
public abstract class Transformer {

    public static final String ENZYME_PORTAL = "Enzyme Portal";
    public static final String ENZYME_PORTAL_DESCRIPTION = "The Enzyme Portal integrates publicly available information about enzymes, such as small-molecule chemistry, biochemical pathways and drug compounds.";
    private static final String REACTOME_PATHWAY_ID_REGEX = "^R-.*-.*";

    static final String COFACTOR = "COFACTOR";
    static final String INHIBITOR = "INHIBITOR";
    static final String ACTIVATOR = "ACTIVATOR";

    protected Set<Field> addProteinNameFields(String proteinName, Set<Field> fields) {
        //System.out.println("ADD PROTEIN CALLED " + System.currentTimeMillis());
        if (!StringUtils.isEmpty(proteinName)) {
            Field field = new Field(FieldName.PROTEIN_NAME.getName(), proteinName);
            fields.add(field);

            return fields;
        }
        return fields;
    }

    protected Set<Ref> addReactionXrefs(Set<EnzymePortalReaction> reactions, Set<Ref> refs) {

        //System.out.println("ADD REACTION CALLED " + System.currentTimeMillis());

        if (!reactions.isEmpty()) {
            return reactions
                    .stream()
                    .map(reaction -> new Ref(reaction.getReactionId(), reaction.getReactionSource()))
                    .collect(Collectors.toSet());
        }
        return refs;
    }

    //////////////
    protected Set<Field> addScientificNameFields(String scientificName, Set<Field> fields) {
        //System.out.println("ADD SCIENCENAME CALLED " + System.currentTimeMillis());

        if (!StringUtils.isEmpty(scientificName)) {
            Field field = new Field(FieldName.SCIENTIFIC_NAME.getName(), scientificName);
            fields.add(field);
            return fields;
        }
        return fields;
    }

    protected Set<Field> addCommonNameFields(String commonName, Set<Field> fields) {
       // System.out.println("ADD COMMON NAME CALLED " + System.currentTimeMillis());

        if (!StringUtils.isEmpty(commonName)) {
            Field field = new Field(FieldName.COMMON_NAME.getName(), commonName + " ");
            fields.add(field);

            return fields;
        }
        return fields;
    }

    protected Stream<Field> computeSynonymsAndBuildFields(String synonymName, String proteinName, Set<Field> fields) {
        return Stream.of(synonymName
                .split(";"))
                .distinct()
                .filter(otherName -> (!otherName.trim().equalsIgnoreCase(proteinName.trim())))
                .map(syn -> new Field(FieldName.SYNONYM.getName(), syn));

    }

    protected Set<Field> addSynonymFields(String synonymNames, String proteinName, Set<Field> fields) {
        if (synonymNames != null && proteinName != null) {

            Optional<String> synonymName = Optional.ofNullable(synonymNames);
            return computeSynonymsAndBuildFields(synonymName.orElse(""), proteinName, fields)
                    .collect(Collectors.toSet());

        }
        return fields;
    }

    protected Set<Field> addGeneNameFields(Set<EntryToGeneMapping> geneMappings, Set<Field> fields) {
        if (!geneMappings.isEmpty()) {

            return geneMappings.stream()
                    .map(geneMapping -> new Field(FieldName.GENE_NAME.getName(), geneMapping.getGeneName()))
                    .collect(Collectors.toSet());

        }
        return fields;
    }

    protected Set<Ref> addAccessionXrefs(String accession, Set<Ref> refs) {
        if (!StringUtils.isEmpty(accession)) {
            Ref xref = new Ref(accession, DatabaseName.UNIPROTKB.getDbName());
            refs.add(xref);
            return refs;

        }
        return refs;
    }

    protected Set<Ref> addTaxonomyXrefs(Long taxonomy, Set<Ref> refs) {
        String taxId = Long.toString(taxonomy);
        if (!StringUtils.isEmpty(taxId)) {
            Ref xref = new Ref(taxId, DatabaseName.TAXONOMY.getDbName());
            refs.add(xref);

            return refs;

        }
        return refs;
    }

    protected Set<Ref> addEcSource(String ec, Set<Ref> refs) {
        if (!StringUtils.isEmpty(ec)) {
            Ref xref = new Ref(ec, DatabaseName.INTENZ.getDbName());
            refs.add(xref);
            return refs;

        }
        return refs;
    }

    protected Set<Field> addEnzymeFamilyField(String ec, Set<Field> fields) {

        EcNumber ecNumber = new EcNumber();
        String enzymeFamily = ecNumber.computeFamily(ec);
        Field field = new Field(FieldName.ENZYME_FAMILY.getName(), enzymeFamily);
        fields.add(field);
        return fields;
    }

    protected Set<Ref> addPathwaysXrefs(Set<EnzymePortalPathways> pathways, Set<Ref> refs) {

        return pathways
                .stream()
                .map(pathway -> new Ref(parseReactomePathwayId(pathway.getPathwayId()), DatabaseName.REACTOME.getDbName()))
                .collect(Collectors.toSet());

    }

    private String parseReactomePathwayId(String reactomePathwayId) {
        if (reactomePathwayId.matches(REACTOME_PATHWAY_ID_REGEX)) {
            String[] sections = reactomePathwayId.split("-");
            return sections[0] + "-" + sections[2];
        }

        return reactomePathwayId;
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
//        for (EnzymePortalCompound compound : compounds) {
//            fields.add(new Field(FieldName.COMPOUND_NAME.getName(), compound.getCompoundName()));
//            entry.getAdditionalFields().setField(fields);
//            refs.add(new Ref(compound.getCompoundId(), compound.getCompoundSource().toUpperCase()));
//            entry.getCrossReferences().setRef(refs);
//        }
//        return entry;
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
                    //additionalFields.setField(fields);
                    Ref xref = new Ref(compound.getCompoundId(), compound.getCompoundSource().toUpperCase());
                    refs.add(xref);
                    //cr.setRef(refs);
                    //entry.getAdditionalFields().setField(fields);
                    //entry.getCrossReferences().setRef(refs);
                    break;
            }
        }
   
    }

    private void addCompoundFieldAndXref(EnzymePortalCompound compound, String compoundId, String compoundName, Set<Field> fields, Set<Ref> refs) {
        Field field = new Field(compoundId, compound.getCompoundId());
        fields.add(field);
        Field fieldName = new Field(compoundName, compound.getCompoundName());
        fields.add(fieldName);
        //entry.getAdditionalFields().setField(fields);
        Ref xref = new Ref(compound.getCompoundId(), compound.getCompoundSource().toUpperCase());
        refs.add(xref);
        //entry.getCrossReferences().setRef(refs);
        //return entry;
    }

    protected void addDiseaseFieldsAndXrefs(Set<EnzymePortalDisease> diseases, Set<Field> fields, Set<Ref> refs) {

        diseases.stream().map(disease -> {
            Field field = new Field(FieldName.DISEASE_NAME.getName(), disease.getDiseaseName());
            fields.add(field);
            return new Ref(disease.getOmimNumber(), DatabaseName.OMIM.getDbName());

        }).forEach(xref -> refs.add(xref));

//        diseases.stream().map(disease -> {
//            Field field = new Field(FieldName.DISEASE_NAME.getName(), disease.getDiseaseName());
//            fields.add(field);
//            entry.getAdditionalFields().setField(fields);
//            refs.add(new Ref(disease.getOmimNumber(), DatabaseName.OMIM.getDbName()));
//            return disease;
//        }).forEachOrdered(_item -> entry.getCrossReferences().setRef(refs));
//        return entry;
    }

    protected void addUniprotFamilyFieldsAndXrefs(Set<UniprotFamilies> families, Set<Field> fields, Set<Ref> refs ) {
        if (!families.isEmpty()) {
            families.stream().map(family -> {
                Field field = new Field(FieldName.UNIPROT_FAMILY.getName(), family.getFamilyName());
                fields.add(field);
                return new Ref(family.getFamilyGroupId(), DatabaseName.UNIPROT_FAMILY.getDbName());

            }).forEach(xref -> refs.add(xref));

//            for (UniprotFamilies family : families) {
//                Field field = new Field(FieldName.UNIPROT_FAMILY.getName(), family.getFamilyName());
//                fields.add(field);
//                refs.add(new Ref(family.getFamilyGroupId(), DatabaseName.UNIPROT_FAMILY.getDbName()));
//                fieldAndXref.setField(fields);
//                fieldAndXref.setRef(refs);
//            }
        }
//        return fieldAndXref;
    }

    protected void addReactantFieldsAndXrefs(Set<EnzymePortalReactant> reactants, Set<Field> fields, Set<Ref> refs) {

        if(!reactants.isEmpty()){
        reactants.stream().map(reactant -> {
            Field field = new Field(FieldName.REACTANT.getName(), reactant.getReactantName());
            fields.add(field);
            return new Ref(reactant.getReactantId(), reactant.getReactantSource().toUpperCase());

        }).forEach(xref -> refs.add(xref));
        }

//        for (EnzymePortalReactant reactant : reactants) {
//            Field field = new Field(FieldName.REACTANT.getName(), reactant.getReactantName());
//            fields.add(field);
//            refs.add(new Ref(reactant.getReactantId(), reactant.getReactantSource().toUpperCase()));
//            fieldAndXref.setField(fields);
//            fieldAndXref.setRef(refs);
//
//        }
//        return fieldAndXref;
    }
}
