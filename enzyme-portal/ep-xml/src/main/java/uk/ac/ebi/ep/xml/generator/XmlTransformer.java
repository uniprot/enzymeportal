package uk.ac.ebi.ep.xml.generator;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import uk.ac.ebi.ep.model.EnzymeCatalyticActivity;
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

    protected static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(XmlTransformer.class);

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
        String commonName = uniprotEntry.getCommonName();
        if (commonName == null || StringUtils.isEmpty(commonName)) {
            commonName = uniprotEntry.getScientificName();
        }
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

    protected void addSynonymFields(UniprotEntry uniprotEntry, Set<Field> fields) {
        if (uniprotEntry.getSynonymNames() != null && uniprotEntry.getProteinName() != null) {

            Optional<String> synonymName = Optional.ofNullable(uniprotEntry.getSynonymNames());
            computeSynonymsAndBuildFields(synonymName, uniprotEntry.getProteinName(), fields);

        }
    }

    protected void addGeneNameFields(UniprotEntry uniprotEntry, Set<Field> fields) {
        if (!uniprotEntry.getEntryToGeneMappingSet().isEmpty()) {

            uniprotEntry.getEntryToGeneMappingSet().stream()
                    .map(geneMapping -> new Field(FieldName.GENE_NAME.getName(), geneMapping.getGeneName()))
                    .forEach(field -> fields.add(field));

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

    //primary protein for each protein group 
    private void addRelatedSpeciesField(List<UniprotEntry> entries, final Set<Field> fields) {

        List<String> specieList
                = entries
                .stream()
                .map(u -> (u.getAccession() + ";" + u.getCommonName() + ";" + u.getScientificName() + ";" + u.getExpEvidenceFlag() + ";" + u.getTaxId()))
                .distinct()
                .collect(Collectors.toList());
//        } else {
//            String rel = (entry.getAccession() + ";" + entry.getCommonName() + ";" + entry.getScientificName() + ";" + entry.getExpEvidenceFlag() + ";" + entry.getTaxId());

            //specieList = Arrays.asList(rel);
        if (!specieList.isEmpty()) {
            String rs = String.join(" | ", specieList);

            String rsField = StringUtils.removeEnd(rs, " | ");

            Field relatedSpeciesField = new Field(FieldName.RELATED_SPECIES.getName(), rsField);
            fields.add(relatedSpeciesField);
        }
    }

    protected void addPrimaryProtein(ProteinGroups proteinGroups, final Set<Field> fields, Set<Ref> refs) {
        PrimaryProtein primaryProtein = proteinGroups.getPrimaryProtein();
        if (primaryProtein != null) {
            addPrimaryProteinField(primaryProtein, fields);
            addPrimaryImage(primaryProtein, fields);

            addEntryTypeFields(primaryProtein, fields);
            addPrimaryFunctionFields(primaryProtein, fields);

            List<UniprotEntry> entries = proteinGroups.getUniprotEntryList();

//            entries.stream().parallel()
//                    .map(uniprotEntry -> {
//                if (uniprotEntry.getAccession().equals(primaryProtein.getAccession())) {
//
//                    addRelatedSpeciesField(uniprotEntry, fields);
//
//                    addPrimarySynonymFields(uniprotEntry, proteinGroups.getProteinName(), fields);
//                    addPrimaryEc(uniprotEntry, fields);
//                    addPrimaryCatalyticActivityFields(uniprotEntry, fields);
//                    addPrimaryGeneNameFields(uniprotEntry, fields);
//                }
//                return uniprotEntry;
//            }).map((uniprotEntry) -> {
//                addScientificNameFields(uniprotEntry, fields);
//                return uniprotEntry;
//            }).map((uniprotEntry) -> {
//                addCommonNameFields(uniprotEntry, fields);
//                return uniprotEntry;
//            }).map((uniprotEntry) -> {
//                addAccessionXrefs(uniprotEntry, refs);
//                return uniprotEntry;
//            }).map((uniprotEntry) -> {
//                addCompoundDataFieldsAndXrefs(uniprotEntry, fields, refs);
//                return uniprotEntry;
//            }).map((uniprotEntry) -> {
//                addDiseaseFieldsAndXrefs(uniprotEntry, fields, refs);
//                return uniprotEntry;
//            }).map((uniprotEntry) -> {
//                addEcXrefs(uniprotEntry, refs);
//                return uniprotEntry;
//            }).map((uniprotEntry) -> {
//                addPathwaysXrefs(uniprotEntry, refs);
//                return uniprotEntry;
//            }).forEach((uniprotEntry) -> {
//                addTaxonomyXrefs(uniprotEntry, refs);
//            });
            
            
            
            List<UniprotEntry> relatedProteins = entries.stream()
                    .filter((uniprotEntry) -> (uniprotEntry.getRelatedProteinsId().getRelProtInternalId() == primaryProtein.getRelatedProteinsId()))
                    .distinct()
                    .collect(Collectors.toList());

      
            addRelatedSpeciesField(relatedProteins, fields);
            //for (UniprotEntry uniprotEntry : entries) {
            for (UniprotEntry uniprotEntry : relatedProteins) {
                if (uniprotEntry.getAccession().equals(primaryProtein.getAccession())) {

//                    if (uniprotEntry.getRelatedProteinsId().getRelProtInternalId() == primaryProtein.getRelatedProteinsId()) {
//                    addRelatedSpeciesField(uniprotEntry, fields);
//                        
//                    }
                    addPrimarySynonymFields(uniprotEntry, proteinGroups.getProteinName(), fields);
                    addPrimaryEc(uniprotEntry, fields);
                    addPrimaryCatalyticActivityFields(uniprotEntry, fields);
                    addPrimaryGeneNameFields(uniprotEntry, fields);
                }

                addScientificNameFields(uniprotEntry, fields);
                addCommonNameFields(uniprotEntry, fields);

                addAccessionXrefs(uniprotEntry, refs);

                addCompoundDataFieldsAndXrefs(uniprotEntry, fields, refs);

                addDiseaseFieldsAndXrefs(uniprotEntry, fields, refs);
                addEcXrefs(uniprotEntry, refs);
                addPathwaysXrefs(uniprotEntry, refs);
                addTaxonomyXrefs(uniprotEntry, refs);
            }
            
            
            
//            entries.stream().parallel()
//                    .filter(acc -> primaryProtein.getAccession().equals(acc.getAccession()))
//                    // .filter(sp->primaryProtein.getRelatedProteinsId()== sp.getRelatedProteinsId().getRelProtInternalId())
//                    .map(entry -> {
//                        StopWatch stopWatchr = new StopWatch();
//                        stopWatchr.start();
//                        addRelatedSpeciesField(entry, fields);
//                        stopWatchr.stop();
//                      
//                        return entry;
//                    }).map(entry -> {
//                        addPrimarySynonymFields(entry, proteinGroups.getProteinName(), fields);
//                        return entry;
//                    }).map(entry -> {
//                        addPrimaryEc(entry, fields);
//                        return entry;
//                    }).map(entry -> {
//                        addPrimaryCatalyticActivityFields(entry, fields);
//                        return entry;
//                    }).forEach(entry -> {
//                        addPrimaryGeneNameFields(entry, fields);
//                    });
        }

    }

    private void addPrimaryProteinField(PrimaryProtein primaryProtein, final Set<Field> fields) {

        if (primaryProtein != null) {
            Field primaryAccessionfield = new Field(FieldName.PRIMARY_ACCESSION.getName(), primaryProtein.getAccession());
            fields.add(primaryAccessionfield);
            String commonName = primaryProtein.getCommonName();
            if (commonName == null) {
                commonName = primaryProtein.getScientificName();
            }
            Field primaryOganismfield = new Field(FieldName.PRIMARY_ORGANISM.getName(), " " + commonName);
            fields.add(primaryOganismfield);

        }

    }

    private void addPrimaryFunctionFields(PrimaryProtein primaryProtein, Set<Field> fields) {

        if (primaryProtein != null) {
            if (primaryProtein.getFunction() != null && !StringUtils.isEmpty(primaryProtein.getFunction())) {
                Field primaryFunctionfield = new Field(FieldName.FUNCTION.getName(), primaryProtein.getFunction());

                fields.add(primaryFunctionfield);
            }
        }

    }

    private void addPrimaryImage(PrimaryProtein primaryProtein, Set<Field> fields) {

        Character hasPdbFlag = 'Y';

        if (primaryProtein.getPdbFlag().equals(hasPdbFlag)) {
            String pdbId = primaryProtein.getPdbId() + "|" + primaryProtein.getPdbSpecies();
            Field pdbfield = new Field(FieldName.PRIMARY_IMAGE.getName(), pdbId);
            fields.add(pdbfield);
        }

    }

    private void addEntryTypeFields(PrimaryProtein primaryProtein, Set<Field> fields) {

        BigInteger type = primaryProtein.getEntryType();

        if (type != null) {
            String entryType = String.valueOf(type);
            Field entryTypefield = new Field(FieldName.ENTRY_TYPE.getName(), entryType);

            fields.add(entryTypefield);
        }

    }

    private void addRelatedSpeciesField(UniprotEntry entry, final Set<Field> fields) {
        List<String> specieList = new ArrayList<>();
        if (entry.getEntryType().intValue() == 0) {
            specieList
                    = entry.getRelatedProteinsId().getUniprotEntrySet()
                    .stream()
                    .map(u -> (u.getAccession() + ";" + u.getCommonName() + ";" + u.getScientificName() + ";" + u.getExpEvidenceFlag() + ";" + u.getTaxId()))
                    .distinct()
                    .collect(Collectors.toList());
        } else {
            String rel = (entry.getAccession() + ";" + entry.getCommonName() + ";" + entry.getScientificName() + ";" + entry.getExpEvidenceFlag() + ";" + entry.getTaxId());

            specieList = Arrays.asList(rel);
        }

        if (!specieList.isEmpty()) {
            String rs = String.join(" | ", specieList);

            String rsField = StringUtils.removeEnd(rs, " | ");

            Field relatedSpeciesField = new Field(FieldName.RELATED_SPECIES.getName(), rsField);
            fields.add(relatedSpeciesField);
        }
    }

    @Deprecated
    private void addRelatedSpeciesField(ProteinGroups proteinGroups, PrimaryProtein primaryProtein, final Set<Field> fields) {

        List<String> specieList = proteinGroups.getUniprotEntryList()
                .stream()
                //.filter(entry -> entry.getRelatedProteinsId().getRelProtInternalId() == primaryProtein.getRelatedProteinsId())
                .map(relatedSpecies -> relatedSpecies.getRelatedProteinsId().getUniprotEntrySet())
                .flatMap(entry -> entry.stream())
                .map((u) -> (u.getAccession() + ";" + u.getCommonName() + ";" + u.getScientificName() + ";" + u.getExpEvidenceFlag() + ";" + u.getTaxId()))
                .distinct()
                .collect(Collectors.toList());

        if (!specieList.isEmpty()) {
            String rs = String.join(" | ", specieList);
//                    String rs = relatedSpeciesList
//                            .stream()
//                            .reduce((k, v) -> k + "" + v).get();
            String rsField = StringUtils.removeEnd(rs, " | ");

            Field relatedSpeciesField = new Field(FieldName.RELATED_SPECIES.getName(), rsField);
            fields.add(relatedSpeciesField);
        }

    }

    protected void addPrimarySynonymFields(UniprotEntry entry, String proteinName, Set<Field> fields) {

        if (entry.getSynonymNames() != null && proteinName != null) {

            Optional<String> synonymName = Optional.ofNullable(entry.getSynonymNames());
            computeSynonymsAndBuildFields(synonymName, proteinName, fields);

        }

    }

    protected void addPrimaryGeneNameFields(UniprotEntry entry, Set<Field> fields) {

        entry.getEntryToGeneMappingSet()
                .stream()
                .map(geneMapping -> new Field(FieldName.GENE_NAME.getName(), geneMapping.getGeneName()))
                .forEach(field -> fields.add(field));

    }

    protected void addPrimaryCatalyticActivityFields(UniprotEntry entry, Set<Field> fields) {

        Set<EnzymeCatalyticActivity> activities
                = entry.getEnzymeCatalyticActivitySet();

        if (!activities.isEmpty()) {

            activities.stream().map(activity -> new Field(FieldName.CATALYTIC_ACTIVITY.getName(), activity.getCatalyticActivity()))
                    .forEach((primaryActivityfield) -> fields.add(primaryActivityfield));

        }

    }

    protected void addPrimaryEc(UniprotEntry entry, Set<Field> fields) {

        entry.getEnzymePortalEcNumbersSet()
                .stream()
                .map(ec -> new Field(FieldName.EC.getName(), ec.getEcNumber()))
                .forEach(ecfield -> fields.add(ecfield));

    }

    private static String removeLastCharRegexOptional(String s) {
        return Optional.ofNullable(s)
                .map(str -> str.replaceAll(".$", ""))
                .orElse(s);
    }

}
