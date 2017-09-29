package uk.ac.ebi.ep.xml.generator;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import uk.ac.ebi.ep.model.PrimaryProtein;
import uk.ac.ebi.ep.model.ProteinGroups;
import uk.ac.ebi.ep.model.UniprotEntry;
import uk.ac.ebi.ep.model.search.model.EcNumber;
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

    protected void addCompoundDataFieldsAndXrefs(UniprotEntry uniprotEntry, Set<Field> fields, Set<Ref> refs) {
        String COFACTOR = "COFACTOR";
        String INHIBITOR = "INHIBITOR";
        String ACTIVATOR = "ACTIVATOR";
        if (!uniprotEntry.getEnzymePortalCompoundSet().isEmpty()) {
            uniprotEntry.getEnzymePortalCompoundSet()
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

    protected void addFunctionFields(ProteinGroups proteinGroups, Set<Field> fields) {

        PrimaryProtein primaryProtein = proteinGroups.getPrimaryProtein();

        if (primaryProtein != null && !StringUtils.isEmpty(primaryProtein.getFunction())) {

            Field primaryFunctionfield = new Field(FieldName.FUNCTION.getName(), primaryProtein.getFunction());

            fields.add(primaryFunctionfield);
        }

//        proteinGroups.getUniprotEntryList()
//                .stream()
//                .filter(entry -> (!StringUtils.isEmpty(entry.getFunction())))
//                .map(entry -> new Field(FieldName.FUNCTION.getName(), entry.getFunction()))
//                .limit(1)
//                .forEach(field -> fields.add(field));
    }

    protected void addEntryTypeFields(ProteinGroups proteinGroups, Set<Field> fields) {

        String entryType = proteinGroups.getEntryType().toString();
        Field entryTypefield = new Field(FieldName.ENTRY_TYPE.getName(), entryType);

        fields.add(entryTypefield);
    }

    private static String removeLastCharRegexOptional(String s) {
        return Optional.ofNullable(s)
                .map(str -> str.replaceAll(".$", ""))
                .orElse(s);
    }

    protected void addRelatedSpeciesField(ProteinGroups proteinGroups, final Set<Field> fields) {

        PrimaryProtein primaryProtein = proteinGroups.getPrimaryProtein();

        if (primaryProtein != null) {

            for (UniprotEntry entry : proteinGroups.getUniprotEntryList()) {

                if (primaryProtein.getAccession().equalsIgnoreCase(entry.getAccession())) {

                    List<UniprotEntry> rel = entry.getRelatedspecies();
                    LinkedList<String> relatedSpeciesList = new LinkedList<>();

//                    if (rel.size() > 1) {
//                        rel.stream()
//                                .map(u -> (u.getAccession() + ";" + u.getCommonName() + ";" + u.getScientificName()).concat("|"))
//                                .forEach(related_species -> relatedSpeciesList.offer(related_species)
//                                );
//                    } else {
//                        rel.stream()
//                                .map(u -> (u.getAccession() + ";" + u.getCommonName() + ";" + u.getScientificName()))
//                                .forEach(related_species -> relatedSpeciesList.offer(related_species)
//                                );
//                    }
//                    rel.stream()
//                            .map(u -> (u.getAccession() + ";" + u.getCommonName() + ";" + u.getScientificName()).concat("|"))
//                            .forEach(related_species -> relatedSpeciesList.offer(related_species)
//                            );
                    List<String> specieList = rel.stream()
                            .map(u -> (u.getAccession() + ";" + u.getCommonName() + ";" + u.getScientificName()))
                            .collect(Collectors.toList());
                    //.forEach(related_species -> relatedSpeciesList.offer(related_species)

                    String rs = String.join(" | ", specieList);
//                    String rs = relatedSpeciesList
//                            .stream()
//                            .reduce((k, v) -> k + "" + v).get();
                    String rsField = StringUtils.removeEnd(rs, " | ");

                    Field relatedSpeciesField = new Field(FieldName.RELATED_SPECIES.getName(), rsField);
                    fields.add(relatedSpeciesField);

                }
            }
        }

    }

    protected void addPrimaryImage(ProteinGroups proteinGroups, Set<Field> fields) {
        PrimaryProtein primaryProtein = proteinGroups.getPrimaryProtein();

        if (primaryProtein != null) {
            String specieWithImage = primaryProtein.getScientificName();

            if (primaryProtein.getCommonName() != null) {
                specieWithImage = primaryProtein.getCommonName();
            }

            Character hasPdbFlag = 'Y';
            String pdbId = primaryProtein.getPdbId() + "|" + specieWithImage;
            if (primaryProtein.getPdbFlag().equals(hasPdbFlag)) {
                Field pdbfield = new Field(FieldName.PRIMARY_IMAGE.getName(), pdbId);
                fields.add(pdbfield);
            }
        }
    }

    protected void addPrimaryEc(ProteinGroups proteinGroups, Set<Field> fields) {
        PrimaryProtein primaryProtein = proteinGroups.getPrimaryProtein();
        if (primaryProtein != null) {

            proteinGroups.getUniprotEntryList()
                    .stream()
                    .filter(entry -> (entry.getAccession().equals(primaryProtein.getAccession())))
                    .forEach(entry -> {
                        entry.getEnzymePortalEcNumbersSet()
                        .stream()
                        .map(ec -> new Field(FieldName.EC.getName(), ec.getEcNumber()))
                        .forEach(ecfield -> fields.add(ecfield));
                    });
        }
    }

    protected void addPrimaryImageSpecie(ProteinGroups proteinGroups, Set<Field> fields) {
        PrimaryProtein primaryProtein = proteinGroups.getPrimaryProtein();

        if (primaryProtein != null) {

            Character hasPdbFlag = 'Y';

            String specieWithImage = primaryProtein.getScientificName();

            if (primaryProtein.getCommonName() != null) {
                specieWithImage = primaryProtein.getCommonName();
            }

            if (primaryProtein.getPdbFlag().equals(hasPdbFlag)) {

                Field specieWithImagefield = new Field(FieldName.PRIMARY_IMAGE_SPECIE.getName(), specieWithImage);
                fields.add(specieWithImagefield);
            }
        }
    }
//
//    protected void addPDBFields(ProteinGroups proteinGroups, Set<Field> fields) {
//        PrimaryProtein primaryProtein = proteinGroups.getPrimaryProtein();
//        //String PDB_SOURCE = "PDB";
//        if (primaryProtein != null) {
//            Character hasPdbFlag = 'Y';
//            String pdbId = primaryProtein.getPdbId();
//            String specieWithImage = primaryProtein.getCommonName();
//            if (specieWithImage == null) {
//                specieWithImage = primaryProtein.getScientificName();
//            }
//            if (primaryProtein.getPdbFlag().equals(hasPdbFlag)) {
//                Field pdbfield = new Field(FieldName.PDB.getName(), pdbId);
//                fields.add(pdbfield);
//
//                Field specieWithImagefield = new Field(FieldName.PDB_SPECIE.getName(), specieWithImage);
//                fields.add(specieWithImagefield);
//
//            }
//
//        }
//
//    }

}
