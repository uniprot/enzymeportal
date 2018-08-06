package uk.ac.ebi.ep.xml.transformer;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ep.xml.config.XmlFileProperties;
import uk.ac.ebi.ep.xml.entity.EntryToGeneMapping;
import uk.ac.ebi.ep.xml.entity.EnzymePortalCompound;
import uk.ac.ebi.ep.xml.entity.EnzymePortalDisease;
import uk.ac.ebi.ep.xml.entity.EnzymePortalPathways;
import uk.ac.ebi.ep.xml.schema.Database;
import uk.ac.ebi.ep.xml.schema.Field;
import uk.ac.ebi.ep.xml.schema.Ref;
import uk.ac.ebi.ep.xml.util.DatabaseName;
import uk.ac.ebi.ep.xml.util.FieldName;
import uk.ac.ebi.ep.xml.util.Preconditions;

/**
 *
 * @author Joseph
 */
public abstract class XmlTransformer {

  

    public static final String ENZYME_PORTAL = "Enzyme Portal";
    public static final String ENZYME_PORTAL_DESCRIPTION = "The Enzyme Portal integrates publicly available information about enzymes, such as small-molecule chemistry, biochemical pathways and drug compounds.";
    private static final String REACTOME_PATHWAY_ID_REGEX = "^R-.*-.*";
    private final XmlFileProperties xmlFileProperties;

    public XmlTransformer(XmlFileProperties xmlFileProperties) {
        Preconditions.checkArgument(xmlFileProperties == null, "xmlFileProperties (XML Configuration) can not be null");

        this.xmlFileProperties = xmlFileProperties;
    }

    protected Database buildDatabaseInfo(long entryCount) {
        Database database = new Database();
        database.setName(ENZYME_PORTAL);
        database.setDescription(ENZYME_PORTAL_DESCRIPTION);
        database.setRelease(xmlFileProperties.getReleaseNumber());
        LocalDate date = LocalDate.now();
        database.setReleaseDate(date);
        database.setEntryCount(entryCount);
        return database;
    }



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

    void computeSynonymsAndBuildFields(Optional<String> synonymName, String proteinName, Set<Field> fields) {
        if (synonymName.isPresent()) {
            Stream.of(synonymName
                    .get()
                    .split(";"))
                    .distinct()
                    .filter(otherName -> (!otherName.trim().equalsIgnoreCase(proteinName.trim())))
                    .map(syn -> {
                        return new Field(FieldName.SYNONYM.getName(), syn);

                    }).forEach(field -> fields.add(field));

        }
    }

    protected void addSynonymFields(String synonymNames, String proteinName, Set<Field> fields) {
        if (synonymNames != null && proteinName != null) {

            Optional<String> synonymName = Optional.ofNullable(synonymNames);
            computeSynonymsAndBuildFields(synonymName, proteinName, fields);

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
                return new Ref(compound.getCompoundId(), compound.getCompoundSource());

            }).forEach(xref -> {
                refs.add(xref);
            });
        
    }

    protected void addCompoundDataFieldsAndXrefs(Set<EnzymePortalCompound> compounds, Set<Field> fields, Set<Ref> refs) {
        String COFACTOR = "COFACTOR";
        String INHIBITOR = "INHIBITOR";
        String ACTIVATOR = "ACTIVATOR";
     
           compounds
                    .stream()
                    .map(compound -> {
                        if (compound.getCompoundRole().equalsIgnoreCase(COFACTOR)) {
                            String chebiId = compound.getCompoundId().replaceAll("CHEBI:", "");
                            Field cofactor = new Field(FieldName.COFACTOR.getName(), chebiId);
                            fields.add(cofactor);
                            Field cofactorName = new Field(FieldName.COFACTOR_NAME.getName(), compound.getCompoundName());
                            fields.add(cofactorName);
                        }
                        return compound;
                    }).map(compound -> {
                if (compound.getCompoundRole().equalsIgnoreCase(INHIBITOR)) {
                    Field inhibitor = new Field(FieldName.INHIBITOR.getName(), compound.getCompoundId());
                    fields.add(inhibitor);
                    Field inhibitorName = new Field(FieldName.INHIBITOR_NAME.getName(), compound.getCompoundName());
                    fields.add(inhibitorName);
                }
                return compound;
            }).map(compound -> {
                if (compound.getCompoundRole().equalsIgnoreCase(ACTIVATOR)) {
                    Field activator = new Field(FieldName.ACTIVATOR.getName(), compound.getCompoundId());
                    fields.add(activator);
                    Field activatorName = new Field(FieldName.ACTIVATOR_NAME.getName(), compound.getCompoundName());
                    fields.add(activatorName);
                }
                return compound;
            }).map(compound -> new Ref(compound.getCompoundId(), compound.getCompoundSource()))
                    .forEach(xref -> {
                        refs.add(xref);
                    });
        
    }

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

    protected void addPathwaysXrefs( Set<EnzymePortalPathways> pathways, Set<Ref> refs) {

    
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

}