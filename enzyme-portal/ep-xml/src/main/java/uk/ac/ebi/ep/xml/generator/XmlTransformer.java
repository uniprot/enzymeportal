package uk.ac.ebi.ep.xml.generator;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ep.data.domain.PrimaryProtein;
import uk.ac.ebi.ep.data.domain.ProteinGroups;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.search.model.EcNumber;
import uk.ac.ebi.ep.xml.config.XmlConfigParams;
import static uk.ac.ebi.ep.xml.generator.XmlTransformer.ENZYME_PORTAL;
import uk.ac.ebi.ep.xml.model.Database;
import uk.ac.ebi.ep.xml.model.Field;
import uk.ac.ebi.ep.xml.model.Ref;
import uk.ac.ebi.ep.xml.util.DatabaseName;
import uk.ac.ebi.ep.xml.util.FieldName;
import uk.ac.ebi.ep.xml.util.Preconditions;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class XmlTransformer {

    protected static final String REVIEWED = "reviewed";
    protected static final String UNREVIEWED = "unreviewed";

    public static final String ENZYME_PORTAL = "Enzyme Portal";
    public static final String ENZYME_PORTAL_DESCRIPTION = "The Enzyme Portal integrates publicly available information about enzymes, such as small-molecule chemistry, biochemical pathways and drug compounds.";
    private static final String REACTOME_PATHWAY_ID_REGEX = "^R-.*-.*";
    private final XmlConfigParams xmlConfigParams;

    public XmlTransformer(XmlConfigParams xmlConfigParams) {
        Preconditions.checkArgument(xmlConfigParams == null, "XmlConfigParams (XML Configuration) can not be null");

        this.xmlConfigParams = xmlConfigParams;
    }

    protected Database buildDatabaseInfo(long entryCount) {
        Database database = new Database();
        database.setName(ENZYME_PORTAL);
        database.setDescription(ENZYME_PORTAL_DESCRIPTION);
        database.setRelease(xmlConfigParams.getReleaseNumber());
        LocalDate date = LocalDate.now();
        database.setReleaseDate(date);
        database.setEntryCount(entryCount);
        return database;
    }

    protected void addUniprotIdFields(UniprotEntry uniprotEntry, Set<Field> fields) {
        if (!StringUtils.isEmpty(uniprotEntry.getUniprotid())) {
            Field field = new Field(FieldName.UNIPROT_NAME.getName(), uniprotEntry.getUniprotid());
            fields.add(field);

        }
    }

    protected void addStatus(UniprotEntry uniprotEntry, Set<Field> fields) {
        if (uniprotEntry.getEntryType() != null) {
            int entryType = uniprotEntry.getEntryType().intValue();

            if (entryType == 0) {
                Field field = new Field(FieldName.STATUS.getName(), REVIEWED);
                fields.add(field);
            }
            if (entryType == 1) {
                Field field = new Field(FieldName.STATUS.getName(), UNREVIEWED);
                fields.add(field);
            }

        }
    }

    protected void addProteinNameFields(UniprotEntry uniprotEntry, Set<Field> fields) {
        if (!StringUtils.isEmpty(uniprotEntry.getProteinName())) {
            Field field = new Field(FieldName.PROTEIN_NAME.getName(), uniprotEntry.getProteinName());
            fields.add(field);

        }
    }

    protected void addScientificNameFields(UniprotEntry uniprotEntry, Set<Field> fields) {
        if (!StringUtils.isEmpty(uniprotEntry.getScientificName())) {
            Field field = new Field(FieldName.SCIENTIFIC_NAME.getName(), uniprotEntry.getScientificName());
            fields.add(field);

        }
    }

    protected void addCommonNameFields(UniprotEntry uniprotEntry, Set<Field> fields) {
        if (!StringUtils.isEmpty(uniprotEntry.getCommonName())) {
            Field field = new Field(FieldName.COMMON_NAME.getName(), uniprotEntry.getCommonName());
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

    protected void addSynonymFields(UniprotEntry uniprotEntry, Set<Field> fields) {
        if (uniprotEntry.getSynonymNames() != null && uniprotEntry.getProteinName() != null) {

            Optional<String> synonymName = Optional.ofNullable(uniprotEntry.getSynonymNames());
            computeSynonymsAndBuildFields(synonymName, uniprotEntry.getProteinName(), fields);

        }
    }

    protected void addAccessionXrefs(UniprotEntry uniprotEntry, Set<Ref> refs) {
        if (!StringUtils.isEmpty(uniprotEntry.getAccession())) {
            Ref xref = new Ref(uniprotEntry.getAccession(), DatabaseName.UNIPROTKB.getDbName());
            refs.add(xref);

        }
    }

    protected void addTaxonomyXrefs(UniprotEntry uniprotEntry, Set<Ref> refs) {
        String taxId = Long.toString(uniprotEntry.getTaxId());
        if (!StringUtils.isEmpty(taxId)) {
            Ref xref = new Ref(taxId, DatabaseName.TAXONOMY.getDbName());
            refs.add(xref);

        }
    }

    protected void addEcXrefs(UniprotEntry uniprotEntry, Set<Ref> refs) {

        if (!uniprotEntry.getEnzymePortalEcNumbersSet().isEmpty()) {
            uniprotEntry.getEnzymePortalEcNumbersSet()
                    .stream()
                    .map(ecNumbers -> new Ref(ecNumbers.getEcNumber(), DatabaseName.INTENZ.getDbName()))
                    .forEach(xref -> refs.add(xref));

        }
    }

    protected void addCompoundFieldsAndXrefs(UniprotEntry uniprotEntry, Set<Field> fields, Set<Ref> refs) {
        if (!uniprotEntry.getEnzymePortalCompoundSet().isEmpty()) {

            uniprotEntry.getEnzymePortalCompoundSet().stream().map(compound -> {
                Field field = new Field(FieldName.COMPOUND_NAME.getName(), compound.getCompoundName());
                //Field compoundType = new Field(FieldName.COMPOUND_TYPE.getName(), compound.getCompoundRole());
                fields.add(field);
                //fields.add(compoundType);
                return new Ref(compound.getCompoundId(), compound.getCompoundSource());

            }).forEach(xref -> {
                refs.add(xref);
            });
        }
    }

    protected void addDiseaseFieldsAndXrefs(UniprotEntry uniprotEntry, Set<Field> fields, Set<Ref> refs) {
        if (!uniprotEntry.getEnzymePortalDiseaseSet().isEmpty()) {
            uniprotEntry.getEnzymePortalDiseaseSet().stream().map(disease -> {
                Field field = new Field(FieldName.DISEASE_NAME.getName(), disease.getDiseaseName());
                fields.add(field);
                return new Ref(disease.getOmimNumber(), DatabaseName.OMIM.getDbName());

            }).forEach(xref -> refs.add(xref));

        }
    }

    protected void addEnzymeFamilyField(String ec, Set<Field> fields) {

        EcNumber ecNumber = new EcNumber();
        String enzymeFamily = ecNumber.computeFamily(ec);
        Field field = new Field(FieldName.ENZYME_FAMILY.getName(), enzymeFamily);
        fields.add(field);
    }

    protected void addGeneNameFields(UniprotEntry uniprotEntry, Set<Field> fields) {
        if (!uniprotEntry.getEntryToGeneMappingSet().isEmpty()) {

            uniprotEntry.getEntryToGeneMappingSet().stream()
                    .map(geneMapping -> new Field(FieldName.GENE_NAME.getName(), geneMapping.getGeneName()))
                    .forEach(field -> fields.add(field));

        }
    }

    protected void addPathwaysXrefs(UniprotEntry uniprotEntry, Set<Ref> refs) {

        if (!uniprotEntry.getEnzymePortalPathwaysSet().isEmpty()) {
            uniprotEntry.getEnzymePortalPathwaysSet()
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

    protected void addPrimaryProteinField(ProteinGroups proteinGroups, final Set<Field> fields) {

        PrimaryProtein primaryProtein = proteinGroups.getPrimaryProtein();

        if (primaryProtein != null) {

            Field primaryAccessionfield = new Field(FieldName.PRIMARY_ACCESSION.getName(), primaryProtein.getAccession());
            fields.add(primaryAccessionfield);
            String commonName = primaryProtein.getCommonName();
            if (commonName == null) {
                commonName = primaryProtein.getScientificName();
            }
            Field primaryOganismfield = new Field(FieldName.PRIMARY_ORGANISM.getName(), commonName);

            fields.add(primaryOganismfield);
        }

    }

}
