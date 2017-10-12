package uk.ac.ebi.ep.xml.generator;

import java.math.BigInteger;
import java.time.LocalDate;
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

    //protein group 
    //use primary protein entry type
//    protected void addEntryTypeFields(ProteinGroups proteinGroups, Set<Field> fields) {
//
//        BigInteger type = proteinGroups.getEntryType();
//        if (type != null) {
//
//            String entryType = String.valueOf(type);
//            Field entryTypefield = new Field(FieldName.ENTRY_TYPE.getName(), entryType);
//
//            fields.add(entryTypefield);
//        }
//
//    }
    protected void addPrimaryProtein(ProteinGroups proteinGroups, final Set<Field> fields) {
        PrimaryProtein primaryProtein = proteinGroups.getPrimaryProtein();
        if (primaryProtein != null) {
            addPrimaryProteinField(primaryProtein, fields);
            addPrimaryImage(primaryProtein, fields);
  
            addEntryTypeFields(proteinGroups, primaryProtein, fields);
            addPrimaryFunctionFields(primaryProtein, fields);

            List<UniprotEntry> entries = proteinGroups.getUniprotEntryList();
            entries.stream()
                    .filter(acc -> primaryProtein.getAccession().equals(acc.getAccession()))
                    .map(entry -> {
                        addRelatedSpeciesField(entry, fields);
                        return entry;
                    }).map(entry -> {
                        addPrimarySynonymFields(entry, proteinGroups.getProteinName(), fields);
                        return entry;
                    }).map(entry -> {
                        addPrimaryEc(entry, fields);
                        return entry;
                    }).map(entry -> {
                        addPrimaryCatalyticActivityFields(entry, fields);
                        return entry;
                    }).forEach(entry -> {
                        addPrimaryGeneNameFields(entry, fields);
                    });

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

    private void addEntryTypeFields(ProteinGroups proteinGroups, PrimaryProtein primaryProtein, Set<Field> fields) {

        BigInteger type = primaryProtein.getEntryType();

        if (type != null) {
            String entryType = String.valueOf(type);
            Field entryTypefield = new Field(FieldName.ENTRY_TYPE.getName(), entryType);

            fields.add(entryTypefield);
//            BigInteger gType = proteinGroups.getEntryType();
//
//            if (gType != null && gType != type) {
//                logger.error("Entry Type MisMatch " + type + " vs " + gType + " in " + proteinGroups.getProteinGroupId());
//
//            }
        }

    }

    private void addRelatedSpeciesField(UniprotEntry entry, final Set<Field> fields) {
        List<String> specieList
                = entry.getRelatedProteinsId().getUniprotEntrySet()
                .stream()
                .map(u -> (u.getAccession() + ";" + u.getCommonName() + ";" + u.getScientificName() + ";" + u.getExpEvidenceFlag() + ";" + u.getTaxId()))
                .distinct()
                .collect(Collectors.toList());

        if (!specieList.isEmpty()) {
            String rs = String.join(" | ", specieList);

            String rsField = StringUtils.removeEnd(rs, " | ");

            Field relatedSpeciesField = new Field(FieldName.RELATED_SPECIES.getName(), rsField);
            fields.add(relatedSpeciesField);
        }
    }

    private void addRelatedSpeciesFieldNOTUSED(ProteinGroups proteinGroups, PrimaryProtein primaryProtein, final Set<Field> fields) {

        // List<String> specieList = new ArrayList<>();
//           Set<UniprotEntry> relatedProteins = new HashSet<>();
//        for (UniprotEntry entry : proteinGroups.getUniprotEntryList()) {
//
//            if (entry.getRelatedProteinsId().getRelProtInternalId() == primaryProtein.getRelatedProteinsId()) {
//
//                relatedProteins = new HashSet<>(entry.getRelatedProteinsId().getUniprotEntrySet());
//                List<UniprotEntry> relatedSpecies = entry.getRelatedProteinsId().getUniprotEntrySet();
//                relatedSpecies
//                        .stream()
//                        .map((u) -> (u.getAccession() + ";" + u.getCommonName() + ";" + u.getScientificName() + ";" + u.getExpEvidenceFlag() + ";" + u.getTaxId()))
//                        .forEach((data) -> {
//                    specieList.add(data);
//                });
//            }
//        }
        List<String> specieList = proteinGroups.getUniprotEntryList()
                .stream()
                //.filter(entry -> entry.getRelatedProteinsId().getRelProtInternalId() == primaryProtein.getRelatedProteinsId())
                .map(relatedSpecies -> relatedSpecies.getRelatedProteinsId().getUniprotEntrySet())
                .flatMap(entry -> entry.stream())
                .map((u) -> (u.getAccession() + ";" + u.getCommonName() + ";" + u.getScientificName() + ";" + u.getExpEvidenceFlag() + ";" + u.getTaxId()))
                .distinct()
                .collect(Collectors.toList());

//               Set<RelSpecies> primaries = proteinGroups.getUniprotEntryList()
//                .stream()
//                .filter(entry -> entry.getRelatedProteinsId().getRelProtInternalId() == primaryProtein.getRelatedProteinsId())
//                .map(relatedSpecies -> relatedSpecies.getRelatedspecies())
//                .flatMap(entry -> entry.stream())
//                .map(specie -> new RelSpecies(specie.getAccession(), specie.getCommonName(), specie.getScientificName(), specie.getExpEvidenceFlag()))
//                //.map((u) -> (u.getAccession() + ";" + u.getCommonName() + ";" + u.getScientificName() + ";" + u.getExpEvidenceFlag() + ";" + u.getTaxId()))
//                //.distinct().findFirst().get();
//                
//                .collect(Collectors.toSet());
//               System.out.println("how many primary "+ primaries.size());
//               
//            RelSpecies primary =   sortByModelOrganism(primaries).stream().findFirst().get();
//               
//               System.out.println(primary.getAccession() + " PRIMARY GUY "+ primary.getCommonName() + " "+ primary.getScientificName());
//       
//        for (UniprotEntry entry : proteinGroups.getUniprotEntryList()) {
//
//            if (entry.getRelatedProteinsId().getRelProtInternalId() == primaryProtein.getRelatedProteinsId()) {
//
//                relatedProteins = new HashSet<>(entry.getRelatedProteinsId().getUniprotEntrySet());
//                List<UniprotEntry> relatedSpecies = entry.getRelatedProteinsId().getUniprotEntrySet();
//                for (UniprotEntry u : relatedSpecies) {
//                    String data = (u.getAccession() + ";" + u.getCommonName() + ";" + u.getScientificName() + ";" + u.getExpEvidenceFlag() + ";" + u.getTaxId());
//                    specieList.add(data);
//                }
//            }
//        }
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

//    protected void addPrimaryProteinXXX(ProteinGroups proteinGroups, final Set<Field> fields) {
//        
//        List<UniprotEntry> entries = proteinGroups.getUniprotEntryList();
//        PrimaryProtein primaryProtein = proteinGroups.getPrimaryProtein();
//        Set<PrimaryProtein> proteins = new HashSet<>();
//
////        for (UniprotEntry e : entries) {
////
////            if (e.getProteinGroupId().getProteinGroupId().equals(proteinGroups.getProteinGroupId())) {
////
////                PrimaryProtein primaryProtein = e.getRelatedProteinsId().getPrimaryProtein();
////                proteins.add(primaryProtein);
////                System.out.println("PROIORITY " + primaryProtein.getPriorityCode());
////            }
////
////        }
////        Set<PrimaryProtein> primaryProteins = new HashSet<>();
////        PrimaryProtein primaryProtein = proteins.stream()
////                .filter(p -> p.getPriorityCode().equalsIgnoreCase("EMO")).findAny().orElse(null);
//        //.forEach(p ->  primaryProteins.add(p));
////        PrimaryProtein primaryProtein
////                = // proteinGroups.getUniprotEntryList()
////                entries
////                .stream()
////                .filter(id -> id.getProteinGroupId().getProteinGroupId().equals(proteinGroups.getProteinGroupId()))
////                .map(p -> p.getRelatedProteinsId().getPrimaryProtein())
////                .findAny().orElse(null);
//        if (primaryProtein != null) {
//            
//            Field primaryAccessionfield = new Field(FieldName.PRIMARY_ACCESSION.getName(), primaryProtein.getAccession());
//            fields.add(primaryAccessionfield);
//            String commonName = primaryProtein.getCommonName();
//            if (commonName == null) {
//                commonName = primaryProtein.getScientificName();
//            }
//            Field primaryOganismfield = new Field(FieldName.PRIMARY_ORGANISM.getName(), " " + commonName);
//            fields.add(primaryOganismfield);
//            
//            addPrimaryImage(primaryProtein, fields);
//            addPrimaryFunctionFields(primaryProtein, fields);
//
////            List<UniprotEntry> entries
////                    = //proteinGroups.getUniprotEntryList()
////                    entryList
////                    .stream()
////                    .filter(entry -> primaryProtein.getAccession().equals(entry.getAccession()))
////                    .collect(Collectors.toList());
//            entries.stream().parallel()
//                    .filter(acc -> primaryProtein.getAccession().equals(acc.getAccession()))
//                    .map(entry -> {
//                        addRelatedSpeciesField(entry, fields);
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
//
//
//        }
//        
//    }
    protected void addPrimarySynonymFields(UniprotEntry entry, String proteinName, Set<Field> fields) {

        if (entry.getSynonymNames() != null && proteinName != null) {

            Optional<String> synonymName = Optional.ofNullable(entry.getSynonymNames());
            computeSynonymsAndBuildFields(synonymName, proteinName, fields);

        }

//        String synonymNames
//                = entries
//                .stream()
//                .map(e -> e.getSynonymNames())
//                        .filter(s -> s!= null)
//                        .findAny().orElse("");
//        
//
//                    Stream.of(synonymNames
//                    .split(";"))
//                    .distinct()
//                    .filter(s-> !StringUtils.isEmpty(s))
//                    .filter(otherName -> (!otherName.trim().equalsIgnoreCase(proteinName.trim())))
//                    .map(syn -> {
//                        return new Field(FieldName.SYNONYM.getName(), syn);
//
//                    }).forEach(field -> fields.add(field));
    }

    protected void addPrimaryGeneNameFields(UniprotEntry entry, Set<Field> fields) {
        // entries.stream()
        //.filter(id -> proteinGroups.getProteinGroupId().equals(id.getProteinGroupId().getProteinGroupId()))
        //.filter(p -> primaryProtein.getAccession().equals(p.getAccession()))
        //.map(e -> e.getEntryToGeneMappingSet())
        entry.getEntryToGeneMappingSet()
                .stream()
                //.flatMap(x -> x.stream())
                //.findFirst().orElse(new HashSet<>()).stream()
                .map(geneMapping -> new Field(FieldName.GENE_NAME.getName(), geneMapping.getGeneName()))
                .forEach(field -> fields.add(field));

    }

    protected void addPrimaryCatalyticActivityFields(UniprotEntry entry, Set<Field> fields) {

        Set<EnzymeCatalyticActivity> activities
                = //entries
                //proteinGroups.getUniprotEntryList()
                entry.getEnzymeCatalyticActivitySet();
        //.stream()
        //.filter(id -> proteinGroups.getProteinGroupId().equals(id.getProteinGroupId().getProteinGroupId()))
        //.filter(p -> primaryProtein.getAccession().equals(p.getAccession()))
        //.map(e -> e.getEnzymeCatalyticActivitySet()).findAny().orElse(new HashSet<>());

        if (!activities.isEmpty()) {

            activities.stream().map(activity -> new Field(FieldName.CATALYTIC_ACTIVITY.getName(), activity.getCatalyticActivity()))
                    .forEach((primaryActivityfield) -> fields.add(primaryActivityfield));

        }

    }

    protected void addPrimaryEc(UniprotEntry entry, Set<Field> fields) {

        //Set<EnzymePortalEcNumbers> ecNumbers = proteinGroups.getUniprotEntryList()
        //proteinGroups.getUniprotEntryList()
        //entries.stream()
        //.filter(id -> proteinGroups.getProteinGroupId().equals(id.getProteinGroupId().getProteinGroupId()))
        //.filter(p -> primaryProtein.getAccession().equals(p.getAccession()))
        entry.getEnzymePortalEcNumbersSet()
                .stream()
                //.map(e -> e.getEnzymePortalEcNumbersSet())
                //.flatMap(x -> x.stream())
                //.findFirst().orElse(new HashSet<>()).stream()
                .map(ec -> new Field(FieldName.EC.getName(), ec.getEcNumber()))
                .forEach(ecfield -> fields.add(ecfield));

//        if (!ecNumbers.isEmpty()) {
//            ecNumbers.stream().map(ec -> new Field(FieldName.EC.getName(), ec.getEcNumber()))
//                    .forEach(ecfield ->  fields.add(ecfield));
//        }
    }

    private void addRelatedSpeciesFieldOLD(UniprotEntry entries, final Set<Field> fields) {

        List<String> specieList
                = //proteinGroups.getUniprotEntryList()
                entries.getRelatedProteinsId().getUniprotEntrySet()
                .stream()
                //.filter(id -> id.getProteinGroupId().getProteinGroupId().equals(proteinGroups.getProteinGroupId()))
                //.map(rel -> rel.getRelatedProteinsId().getUniprotEntrySet())
                //.flatMap(entry -> entry.stream())
                .map(u -> (u.getAccession() + ";" + u.getCommonName() + ";" + u.getScientificName() + ";" + u.getExpEvidenceFlag() + ";" + u.getTaxId()))
                .collect(Collectors.toList());

//        List<UniprotEntry> rel = proteinGroups.getUniprotEntryList().stream().findFirst().get()
//                .getRelatedspecies();
//        UniprotEntry primaryProtein = rel.stream().findFirst().get();
//        
        // if (primaryProtein != null) {
        //for (UniprotEntry entry : rel) {
        //if (primaryProtein.getAccession().equalsIgnoreCase(entry.getAccession())) {
        // primaryProtein = rel.stream().findFirst().get();
        // LinkedList<String> relatedSpeciesList = new LinkedList<>();
//        List<String> specieList = rel.stream()
//                .map(u -> (u.getAccession() + ";" + u.getCommonName() + ";" + u.getScientificName() + ";" + u.getExpEvidenceFlag() + ";" + u.getTaxId()))
//                .collect(Collectors.toList());
//        //.forEach(related_species -> relatedSpeciesList.offer(related_species)
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

    private static String removeLastCharRegexOptional(String s) {
        return Optional.ofNullable(s)
                .map(str -> str.replaceAll(".$", ""))
                .orElse(s);
    }

}
