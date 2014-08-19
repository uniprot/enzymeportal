/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.base.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.biobabel.blast.NcbiBlastClient;
import uk.ac.ebi.biobabel.blast.NcbiBlastClientException;
import uk.ac.ebi.biobabel.lucene.LuceneParser;
import uk.ac.ebi.ep.adapter.intenz.IntenzAdapter;
import uk.ac.ebi.ep.base.exceptions.EnzymeFinderException;
import uk.ac.ebi.ep.base.exceptions.MultiThreadingException;
import uk.ac.ebi.ep.data.common.CommonSpecies;
import uk.ac.ebi.ep.data.domain.EnzymePortalSummary;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.enzyme.model.Enzyme;
import uk.ac.ebi.ep.data.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.data.enzyme.model.Sequence;
import uk.ac.ebi.ep.data.search.model.Compound;
import uk.ac.ebi.ep.data.search.model.Disease;
import uk.ac.ebi.ep.data.search.model.EnzymeAccession;
import uk.ac.ebi.ep.data.search.model.EnzymeSummary;
import uk.ac.ebi.ep.data.search.model.SearchFilters;
import uk.ac.ebi.ep.data.search.model.SearchParams;
import uk.ac.ebi.ep.data.search.model.SearchResults;
import uk.ac.ebi.ep.data.search.model.Species;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
import uk.ac.ebi.ep.ebeye.EbeyeSearchResult;
import uk.ac.ebi.ep.ebeye.EbeyeService;
import uk.ac.ebi.ep.ebeye.UniProtDomain;

/**
 *
 * @author joseph
 */
public class EnzymeFinder {

    private final Logger LOGGER = Logger.getLogger(EnzymeFinder.class);
    protected SearchParams searchParams;
    SearchResults enzymeSearchResults;
    List<String> uniprotEnzymeIds;
    boolean newSearch;
    Set<String> uniprotIdPrefixSet;
    List<String> speciesFilter;
    List<String> compoundFilter;
    List<EnzymeSummary> enzymeSummaryList;

    // private UniprotEntryService service;
    private final EnzymePortalService service;

    @Autowired
    private final IntenzAdapter intenzAdapter;
    @Autowired
    private final EbeyeService ebeyeService;        
    
     Set<Species> uniqueSpecies = new TreeSet<>();
    List<Disease> diseaseFilters = new LinkedList<>();
    List<Compound> compoundFilters = new ArrayList<>();
    Set<Compound> uniquecompounds = new HashSet<>();
    Set<Disease> uniqueDiseases = new HashSet<>();

    public EnzymeFinder(EnzymePortalService service,EbeyeService eService) {
        this.service = service;
        this.ebeyeService = eService;

        enzymeSearchResults = new SearchResults();
        //ebeyeAdapter = new EbeyeAdapter();
        uniprotEnzymeIds = new ArrayList<>();
        uniprotIdPrefixSet = new LinkedHashSet<>();
        enzymeSummaryList = new ArrayList<>();
        intenzAdapter = new IntenzAdapter();
    }

    /**
     * Escapes the keywords, validates the filters and sets the global variables
     * to be used in other methods.
     *
     * @param searchParams
     */
    private void processInputs(SearchParams searchParams) {
        this.searchParams = searchParams;
        speciesFilter = searchParams.getSpecies();
        LuceneParser luceneParser = new LuceneParser();
        String keyword = luceneParser.escapeLuceneSpecialChars(this.searchParams.getText());
        String cleanKeyword = HtmlUtility.cleanText(keyword);

        this.searchParams.setText(cleanKeyword);
        String previousText = searchParams.getPrevioustext();
        String currentText = searchParams.getText();
        compoundFilter = searchParams.getCompounds();


        /*
         * There are 2 cases to treat the search as new search: case 1 - the new
         * text is different from the previous text case 2 - all filters are
         * empty
         */
        if (!previousText.equalsIgnoreCase(currentText)
                || (compoundFilter.size() == 0 && speciesFilter.size() == 0)) {
            newSearch = true;
            searchParams.getSpecies().clear();
            searchParams.getCompounds().clear();
        }
    }

    private void queryEbeyeRest() {

    }

    private EbeyeSearchResult getEbeyeSearchResult() {

        RestTemplate restTemplate = new RestTemplate();
        String query = searchParams.getText();
        System.out.println("EBEYE SERVICES "+ ebeyeService);
        EbeyeSearchResult searchResult = ebeyeService.query(query);
        //String url = "http://www.ebi.ac.uk/ebisearch/ws/rest/uniprot?query=" + query + "&format=json&size=100";

        //EbeyeSearchResult searchResult = restTemplate.getForObject(url, EbeyeSearchResult.class);
        System.out.println("RESULTS SIZE FROM EBEYE " + searchResult.getUniProtDomains().size());
        return searchResult;
    }

    private void getResultsFromUniProt() {
        EbeyeSearchResult searchResult = getEbeyeSearchResult();
        List<UniProtDomain> results = searchResult.getUniProtDomains().stream().distinct().collect(Collectors.toList());

        results.stream().forEach((result) -> {
// prefixes.add("ACE");
            //prefixes.add(result.getUniport_name());
            //accessions.add(result.getUniprot_accession());
            uniprotEnzymeIds.add(result.getUniprot_accession());
            //uniprotEnzymeIds.add(result.getUniport_name());
            //System.out.println("names "+ result.getUniport_name());
        });

    }

    /**
     * Queries EB-Eye for UniProt IDs corresponding to enzymes, and adds them to
     * the uniprotEnzymeIds field.
     *
     * @throws EnzymeFinderException
     */
    private void queryEbeyeForUniprotIds() {
        getResultsFromUniProt();
    }

    //building summary -based on uniprot adapter
    public static final String SEQUENCE_URL_BASE = "http://www.uniprot.org/uniprot/";
    public static final String SEQUENCE_URL_SUFFIX = ".html#section_seq";

    /**
     * Retrieves the protein recommended name as well as any synonyms.
     *
     * @param namesColumn the column returned by the web service
     * @return a list of names, the first one of them being the recommended one.
     */
    private List<String> parseNameSynonyms(String namesColumn) {
        List<String> nameSynonyms = new ArrayList<>();
        if(namesColumn != null){
        final int sepIndex = namesColumn.indexOf(" (");

            //System.out.println("syn index "+ sepIndex);
        if (sepIndex == -1) {
            // no synonyms, just recommended name:

            nameSynonyms.add(namesColumn);
        } else {
            // Recommended name:
            nameSynonyms.add(namesColumn.substring(0, sepIndex));
            // take out starting and ending parentheses
            String[] synonyms = namesColumn.substring(sepIndex + 2, namesColumn.length() - 1).split("\\) \\(");
            nameSynonyms.addAll(Arrays.asList(synonyms));
        }
        return nameSynonyms.stream().distinct().collect(Collectors.toList());
        }
        return nameSynonyms;
    }

    /**
     * Builds an EnzymeModel and populates it with basic information (enough for
     * an EnzymeSummary).
     *
     * @param accession
     * @param id
     * @param nameSynonymsString
     * @param ecsString
     * @param species
     * @param seqLength
     * @param functionString
     * @param pdbAccessions
     * @param relSpecies
     * @return
     */
    private EnzymeModel buildSummary(String accession, String id,
            String nameSynonymsString, String ecsString, Species species,
            List<Disease> diseases, String seqLength, String functionString,
            List<String> pdbAccessions,
            List<EnzymeAccession> relSpecies) {
        EnzymeModel summary = new EnzymeModel();
        summary.getUniprotaccessions().add(accession);
        summary.setUniprotid(id);
        final List<String> nameSynonyms = parseNameSynonyms(nameSynonymsString);
        summary.setName(nameSynonyms.get(0));
        if (nameSynonyms.size() > 1) {
            summary.getSynonym().addAll(nameSynonyms.subList(0, nameSynonyms.size() - 1));
        }
        if (ecsString != null && ecsString.length() > 0) {
            summary.getEc().addAll(Arrays.asList(ecsString.split("; ")));
        }
        summary.setSpecies(species);
        summary.setRelatedspecies(relSpecies);
        // Optional fields:
        if (diseases != null) {
            summary.setDisease(diseases);
        }
        if (seqLength != null) {
            Sequence seq = new Sequence();
            try {
                seq.setLength(Integer.valueOf(seqLength));
            } catch (NumberFormatException e) {
                LOGGER.error(seqLength + ": wrong length for " + accession);
            }
            // TODO seq.setWeight(value);
            seq.setSequenceurl(SEQUENCE_URL_BASE + accession
                    + SEQUENCE_URL_SUFFIX);
            Enzyme enzyme = new Enzyme();
            enzyme.setSequence(seq);
            summary.setEnzyme(enzyme);
        }
        if (functionString != null && functionString.length() > 0) {
            summary.setFunction(functionString);
        }
        if (pdbAccessions != null) {
            summary.setPdbeaccession(pdbAccessions);
        }
//        if (rpString != null) {
//            summary.getReactionpathway().add(parseReactionPathways(rpString));
//        }
//        if (drugsString != null || regulString != null) {
//            summary.setMolecule(parseChemicalEntity(drugsString, regulString));
//        }
        return summary;
    }

//    protected EnzymeSummary getEnzymeSummary(EnzymePortalSummary enzyme) {
//        String defaultSpecies = CommonSpecies.Human.getScientificName();
//
////        List<String> enzymeInfo = getEnzymeInfo(accOrId, idType, getColumns());
////        if (enzymeInfo == null) {
////            return null;
////        }
////        if (enzymeInfo.size() > 1 && idType.equals(IdType.ACCESSION)) {
////            // More than one entry for one accession? It has been demerged?
////            LOGGER.warn("More than one entry for " + accOrId);
////        }
//        Species species = getSpeciesFromSummary(enzyme);
//        List<String> pdbCodes = getPdbCodes(enzyme);
//        String functionString = null;
//
//        String accession = null, id = null,
//                nameSynonymsString = null, ecsString = null,
//                diseasesString = null, seqLength = null,
//                pdbeAccessions = null, pdbMethods = null, rpString = null, drugsString = null,
//                regulString = null;
//        //Species species = null;
//        List<EnzymeAccession> relSpecies = new LinkedList<EnzymeAccession>();
//
//        int bestSpecies = 0; // the best choice to show
//        String[] colValues = null;
//        for (int i = 0; i < enzymeInfo.size(); i++) {
//            colValues = enzymeInfo.get(i).split("\t", -1);
//
//            // some entries don't return a species, ex. Q5D707
//            if (colValues[4].length() == 0) {
//                continue;
//            }
//
//            EnzymeAccession ea = new EnzymeAccession();
//            // organism is always [4], accession is [0], see getColumns()
//            ea.setSpecies(getSpeciesFromSummary(enzyme));
//            ea.getUniprotaccessions().add(enzyme.getUniprotAccession().getAccession());
////            //List<String> pdbCodes = null;
////            if (field.equals(Field.brief) || field.equals(Field.enzyme)) {
////                // We already have PDB codes in enzymeInfo as [6], see getColumns()
////                if (colValues.length > 6) {
////                    pdbCodes = parsePdbCodes(colValues[6], colValues[7]);
////                }
////            }
//            ea.setPdbeaccession(pdbCodes);
//
//            final boolean isDefSp = ea.getSpecies().getScientificname().equalsIgnoreCase(defaultSpecies);
//            final boolean noDefSp = relSpecies.isEmpty()
//                    || !relSpecies.get(0).getSpecies().getScientificname().equalsIgnoreCase(defaultSpecies);
//
//            if (isDefSp || (noDefSp && pdbCodes != null)) {
//                relSpecies.add(0, ea);
//                bestSpecies = 0;//previously i
//            } else {
//                relSpecies.add(ea);
//            }
//        }
//
//        species = relSpecies.get(0).getSpecies();
//
//        // Take the default species or one with structures.
//        colValues = enzymeInfo.get(bestSpecies).split("\t", -1);
//        accession = enzyme.getUniprotAccession().getAccession();
//        id = enzyme.getUniprotAccession().getName();
//        nameSynonymsString = enzyme.get;
//        ecsString = colValues[3];
//
//        switch (field) {
//            case enzyme:
//                if (colValues.length > 6) {
//                    diseasesString = colValues[8];
//                    seqLength = colValues[9];
//                }
//            case brief:
//                functionString = colValues[5];
//                if (colValues.length > 6) {
//                    pdbeAccessions = colValues[6];
//                    pdbMethods = colValues[7];
//                }
//                break;
//            case proteinStructure:
//                pdbeAccessions = colValues[5];
//                if (colValues.length > 6) {
//                    pdbMethods = colValues[6];
//                }
//                break;
//            case reactionsPathways:
//                rpString = colValues[5];
//                break;
//            case molecules:
//                drugsString = colValues[5];
//                if (colValues.length > 6) {
//                    regulString = colValues[6];
//                }
//                break;
//            case diseaseDrugs:
//                // TODO
//                break;
//            case literature:
//                // TODO
//                break;
//            case full:
//                // TODO
//                break;
//        }
//
//        if (idType.equals(IdType.ACCESSION)) {
//
//            // We'll have to make one more request to get other species:
//            String uniprotIdPrefix = id.split(IUniprotAdapter.ID_SPLIT_SYMBOL)[0];
//            String defSp = species.getScientificname();
//
//            relSpecies = getRelatedSpecies(uniprotIdPrefix, defSp);
//        }
//
//        return buildSummary(accession, id, nameSynonymsString, ecsString,
//                species, diseasesString, seqLength, functionString,
//                pdbeAccessions, pdbMethods, rpString, drugsString, regulString, relSpecies);
//    }

    private List<EnzymeAccession> getRelatedSPecies(EnzymePortalSummary enzymePortalSummary) {
        String defaultSpecies = CommonSpecies.Human.getScientificName();

        List<EnzymeAccession> relatedPecies = new LinkedList<>();

        String nameprefix = enzymePortalSummary.getUniprotAccession().getName().substring(0, enzymePortalSummary.getUniprotAccession().getName().indexOf("_"));
        List<UniprotEntry> enzyme = service.findEnzymeByNamePrefixAndProteinName(nameprefix, enzymePortalSummary.getUniprotAccession().getProteinName());
        //System.out.println("num species found "+ enzyme.size());
        for (UniprotEntry entry : enzyme) {
           // Species sp = getSpecies(entry);
            EnzymeAccession ea = new EnzymeAccession();
            ea.setPdbeaccession(getPdbCodes(entry));
            ea.getUniprotaccessions().add(entry.getAccession());
            ea.setSpecies(entry);
            ea.setCompounds(entry.getEnzymePortalCompoundSet().stream().distinct().collect(Collectors.toList()));
            ea.setDiseases(entry.getEnzymePortalDiseaseSet().stream().distinct().collect(Collectors.toList()));
           diseaseFilters.addAll(entry.getEnzymePortalDiseaseSet().stream().distinct().collect(Collectors.toList()));
            //System.out.println("NUM DISEASE PER ENZYME "+ ea.getDiseases().size());
//            if (sp.getScientificname() != null && sp.getScientificname().equalsIgnoreCase(defaultSpecies)) {
//                relatedPecies.add(0, ea);
//            }else if(sp.getScientificname() != null && !sp.getScientificname().equalsIgnoreCase(defaultSpecies)){
//                relatedPecies.add(ea);
//            }
            
                      if (entry.getScientificname() != null && entry.getScientificname().equalsIgnoreCase(defaultSpecies)) {
                relatedPecies.add(0, ea);
            }else if(entry.getScientificname() != null && !entry.getScientificname().equalsIgnoreCase(defaultSpecies)){
                relatedPecies.add(ea);
            }
           uniqueSpecies.add(entry);  
            
        }

     
        return relatedPecies;

    }

    private Species getSpecies(UniprotEntry entry) {
        Species specie = new Species();
        specie.setCommonname(entry.getCommonName());
        specie.setScientificname(entry.getScientificName());
        specie.setSelected(false);
        return specie;
    }

    private List<String> getPdbCodes(UniprotEntry e) {
        List<String> pdbcodes = new ArrayList<>();
        e.getUniprotXrefSet().stream().filter((xref) -> (xref.getSource().equalsIgnoreCase("PDB"))).forEach((xref) -> {
            pdbcodes.add(xref.getSourceId());
        });
        return pdbcodes;
    }

    private String getFunctionFromSummary(EnzymePortalSummary enzymePortalSummary) {
        String function = null;
        if (enzymePortalSummary.getCommentType().equalsIgnoreCase("FUNCTION")) {

            function = enzymePortalSummary.getCommentText();
        }

        return function;
    }

    //this is the part that takes ages.....
//    private List<EnzymeSummary> getEnzymesFromUniprotAPI_OLD(
//            List<String> prefixes, List<String> paramList) {
//
//        List<EnzymeSummary> enzymeList = new ArrayList<>();//make this unique set
//        //List<UniprotEntry> enzymes = service.findByNamePrefixes(prefixes);
//         List<UniprotEntry> enzymes = service.findEnzymesByAccessions(prefixes);
//
//       
//         
//        for (UniprotEntry enzymePortalSummary : enzymes) {
//
//         
//            Species s = new Species();
//            s.setCommonname(enzymePortalSummary.getCommonName());
//            s.setScientificname(enzymePortalSummary.getScientificName());
//            s.setSelected(false);
//            uniqueSpecies.add(s);
//
//            for (EnzymePortalDisease d : enzymePortalSummary.getEnzymePortalDiseaseSet()) {
//                
//                
////                EnzymePortalDisease disease = new EnzymePortalDisease();
////                disease.setDiseaseId(d.getDiseaseId());
////                disease.setDiseaseName(d.getDiseaseName());
////                disease.setUrl(d.getUrl());
////                disease.setDefinition(d.getDefinition());
////                disease.setEvidence(d.getEvidence());
////                disease.setUniprotAccession(d.getUniprotAccession());
//                
//                           Disease disease = new Disease();
//               // EnzymePortalDisease disease = new EnzymePortalDisease();
//                disease.setId(Long.toString(d.getDiseaseId()));
//                disease.setName(d.getDiseaseName());
//                disease.setUrl(d.getUrl());
//                disease.setDescription(d.getDefinition());
//                //disease.setEvidence(d.getEvidence());
//                //disease.setUniprotAccession(d.getUniprotAccession());
//                uniqueDiseases.add(disease);
//            }
//
//            EnzymeSummary summary = new EnzymeSummary();
//            summary.setName(enzymePortalSummary.getProteinName());
//            summary.getSynonym().add(enzymePortalSummary.getSynonymName());
//            summary.setCompounds(enzymePortalSummary.getEnzymePortalCompoundSet());
//            summary.setDiseases(uniqueDiseases);
//            summary.setUniprotid(enzymePortalSummary.getName());
//            summary.getUniprotaccessions().add(enzymePortalSummary.getAccession());
//            summary.setSpecies(s);
//            
//            
//
//            enzymeList.add(summary);
//
//        }
//
//        return enzymeList;
//    }
   

    private List<EnzymeSummary> getEnzymesFromUniprotAPI(
            List<String> prefixes, List<String> paramList) {

       // Set<String> uniqueEc = new HashSet<>();
        //Set<EnzymeAccession> uniqueEnzymeAccessions = new HashSet<>();
        //Set<EnzymeSummary> uniqueEnzymeList = new TreeSet<>();
        List<EnzymeSummary> enzymeList = new ArrayList<>();
        //List<EnzymePortalSummary> enzymes = service.findEnzymeSummariesByNamePrefixes(prefixes);
        List<EnzymePortalSummary> enzymes = service.findEnzymeSumariesByAccessions(prefixes);

        System.out.println("num of enzymes found " + enzymes.size());
        for (EnzymePortalSummary enzymePortalSummary : enzymes) {
            //try {
            List<Compound> compoundsInEnzyme = new ArrayList<>();
            List<Disease> diseaseInEnzyme = new ArrayList<>();
 //System.out.println("SPECIES FOUND "+ enzymePortalSummary.getUniprotAccession().getCommonName());
            //System.out.println("FIRST SUMMARY " + enzymePortalSummary.getUniprotAccession());
            //Species specie = new Species();
//            specie.setCommonname(enzymePortalSummary.getUniprotAccession().getCommonName());
//            specie.setScientificname(enzymePortalSummary.getUniprotAccession().getScientificName());
//
//            specie.setSelected(false);

            //uniqueSpecies.add(enzymePortalSummary.getUniprotAccession());
            //diseaseFilters.addAll(enzymePortalSummary.getUniprotAccession().getEnzymePortalDiseaseSet());
           
//uniqueDiseases.addAll(enzymePortalSummary.getUniprotAccession().getEnzymePortalDiseaseSet());
            diseaseInEnzyme.addAll(enzymePortalSummary.getUniprotAccession().getEnzymePortalDiseaseSet());
            compoundFilters.addAll(enzymePortalSummary.getUniprotAccession().getEnzymePortalCompoundSet());
            //uniquecompounds.addAll(enzymePortalSummary.getUniprotAccession().getEnzymePortalCompoundSet());
            compoundsInEnzyme.addAll(enzymePortalSummary.getUniprotAccession().getEnzymePortalCompoundSet());

            //System.out.println(" Num Disease found : " + enzymePortalSummary.getUniprotAccession().getEnzymePortalDiseaseSet().size());
           // for (EnzymePortalDisease d : enzymePortalSummary.getUniprotAccession().getEnzymePortalDiseaseSet()) {

//                Disease disease = new Disease();
//                // EnzymePortalDisease disease = new EnzymePortalDisease();
//                disease.setId(d.getMeshId());//change disease_id to string at DB level
//                disease.setName(d.getDiseaseName().replaceAll(",", ""));//remove replace all later. fixed in parser
//                disease.setUrl(d.getUrl());
//                disease.setDescription(d.getDefinition());
//                disease.getEvidence().add(d.getEvidence());
//                disease.setUniprotAccession(d.getUniprotAccession().getAccession());
//
//                disease.setSelected(false);
//                uniqueDiseases.add(disease);
//                diseaseInEnzyme.add(disease);

                //specie.getDiseases().add(disease);//4-2-0-1
                // System.out.println("diseases found "+ disease.getName());
            //}
            //specie.setDiseases(diseaseInEnzyme);
            // System.out.println("diseases added "+ diseaseInEnzyme.size());
            
            

//            for (EnzymePortalCompound c : enzymePortalSummary.getUniprotAccession().getEnzymePortalCompoundSet()) {
//
//                Compound compound = new Compound();
//                compound.setId(c.getCompoundId());
//                compound.setName(c.getCompoundName());
//                compound.setRole(Compound.Role.valueOf(c.getCompoundRole()));
//                compound.setUrl(c.getUrl());
//                compound.setSelected(false);
//
//                uniquecompounds.add(compound);
//                compoundsInEnzyme.add(compound);
//
//            }

//            EnzymeAccession ea = new EnzymeAccession();
//            ea.setCompounds(compoundsInEnzyme);
//            ea.setDiseases(diseaseInEnzyme);
//            ea.setUniprotName(enzymePortalSummary.getUniprotAccession().getName());
//
//            ea.setSpecies(specie);
//            //specie.setDiseases(diseaseInEnzyme);
//            //ea.getSpecies().setDiseases(diseaseInEnzyme);
//
//            ea.getUniprotaccessions().add(enzymePortalSummary.getUniprotAccession().getAccession());
//
//            for (UniprotXref xref : enzymePortalSummary.getUniprotAccession().getUniprotXrefSet()) {
//                if (xref.getSource().equalsIgnoreCase("PDB")) {
//                    ea.getPdbeaccession().add(xref.getSourceId());
//                }
//            }
//            // ea.setPdbeaccession("TODO");
//
//            uniqueEnzymeAccessions.add(ea);
//            

            EnzymeSummary summary = new EnzymeSummary();
            summary.setAccession(enzymePortalSummary.getUniprotAccession().getAccession());
            summary.setName(enzymePortalSummary.getUniprotAccession().getProteinName());
            //System.out.println("syn size "+ enzymePortalSummary.getUniprotAccession().getSynonymNames());
            //parseNameSynonyms(enzymePortalSummary.getUniprotAccession().getSynonymNames());
            
            //23.315s
//            if(enzymePortalSummary.getUniprotAccession().getSynonymNames() != null){
//            summary.setSynonym(parseNameSynonyms(enzymePortalSummary.getUniprotAccession().getSynonymNames()));
//            }

            //System.out.println("synomns found : "+ enzymePortalSummary.getUniprotAccession().getSynonymNames());
  //parseNameSynonyms(enzymePortalSummary.getUniprotAccession().getSynonymNames());

            List<String> synonyms = new LinkedList<>();
            //syn = parseNameSynonyms(enzymePortalSummary.getUniprotAccession().getSynonymNames());
String namesColumn = enzymePortalSummary.getUniprotAccession().getSynonymNames();
            //System.out.println("SYMS "+ namesColumn);
            if(namesColumn != null && namesColumn.contains(";")){
                String [] syn = namesColumn.split(";");
                for(String x : syn) {
                    //synonyms.add(x);
                    synonyms.addAll(parseNameSynonyms(x));
                }
            }

//            if(enzymePortalSummary.getUniprotAccession().getSynonymNames() != null) {
//                syn.add(enzymePortalSummary.getUniprotAccession().getSynonymNames().replaceAll(";",";").split(";")[0]);
//            }


                  //summary.getSynonym().add(enzymePortalSummary.getUniprotAccession().getSynonymNames());
            //summary.setSynonym(s.stream().distinct().collect(Collectors.toList()));
           // System.out.println("number of syn "+ syns.size());
            summary.setSynonym(synonyms.stream().distinct().collect(Collectors.toList()));
                            //summary.setSynonyms(enzymePortalSummary.getUniprotAccession().getSynonymNames());
            summary.setCompounds(compoundsInEnzyme);
            summary.setDiseases(diseaseInEnzyme);
            summary.setUniprotid(enzymePortalSummary.getUniprotAccession().getName());
                                //summary.getUniprotaccessions().add(enzymePortalSummary.getUniprotAccession().getAccession());
            //specie.setDiseases(diseaseInEnzyme);//verify
            summary.setSpecies(enzymePortalSummary.getUniprotAccession());
            if (enzymePortalSummary.getCommentType().equalsIgnoreCase("FUNCTION")) {
                summary.setFunction(enzymePortalSummary.getCommentText());
            }
            if (enzymePortalSummary.getCommentType().equalsIgnoreCase("EC_NUMBER")) {
                //if(!StringUtils.isEmpty(enzymePortalSummary.getCommentText()) && !enzymePortalSummary.getCommentText().contains("-")){
                //System.out.println("EC "+ enzymePortalSummary.getCommentText());
                summary.getEc().add(enzymePortalSummary.getCommentText());
                //uniqueEc.add(enzymePortalSummary.getCommentText());
                //}

            }

            //summary.getRelatedspecies().add(ea);
            //summary.getRelatedspecies().addAll(uniqueEnzymeAccessions);
            summary.setRelatedspecies(getRelatedSPecies(enzymePortalSummary));

//                EnzymeSummaryProcessor[] processors = {
//                    new SynonymsProcessor(uniqueEc, intenzAdapter)
//                };
//
//                for (EnzymeSummaryProcessor processor : processors) {
//                summary =    processor.process(summary);
//                uniqueEnzymeList.add(summary);
//                }
            //ignore unique for now
             enzymeList.add(summary);
            //uniqueEnzymeList.add(summary);

//            } catch (uk.ac.ebi.ep.search.exception.MultiThreadingException ex) {
//                //java.util.logging.Logger.getLogger(EnzymeFinder.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }

        //System.out.println("unique enzyme list " + uniqueEnzymeList.size());
        //enzymeList.addAll(uniqueEnzymeList);
       // System.out.println("enzyme List size " + enzymeList.size());
        return enzymeList.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Retrieves full enzyme summaries.
     *
     * @param uniprotIdPrefixes a list of UniProt ID prefixes.
     * @return a list of enzyme summaries ready to show in a result list.
     * @throws MultiThreadingException problem getting the original summaries,
     * or creating a processor to add synonyms to them.
     */
    private List<EnzymeSummary> getEnzymeSummaries(
            List<String> uniprotIdPrefixes, List<String> paramList) {
        List<EnzymeSummary> summaries = getEnzymesFromUniprotAPI(uniprotIdPrefixes, paramList);

//        if (summaries != null && !summaries.isEmpty()) {
//            EnzymeSummaryProcessor[] processors = {
//                new SynonymsProcessor(summaries, intenzAdapter)
//            };
////            for (EnzymeSummary summary : summaries) {
////                for (EnzymeSummaryProcessor processor : processors) {
////                    processor.process(summary);
////        }
////            }
//        }
        return summaries;
    }

    public SearchResults getEnzymes(SearchParams searchParams) {

        processInputs(searchParams);

        /*
         * First time search or when user inserts a new keyword, the filter is
         * reset then the search is performed across all domains without
         * considering the filter.
         */
        if (newSearch) {
            // Search in EBEye for Uniprot ids, the search is filtered by ec:*
            LOGGER.debug("Starting new search");
            // try {
            queryEbeyeForUniprotIds();
            //LOGGER.debug("UniProt IDs from UniProt: " + uniprotEnzymeIds.size());
            //} catch (EnzymeFinderException enzymePortalSummary){
            //LOGGER.error("Unable to search EB-Eye uniprot domain", enzymePortalSummary);
            //}
            // Search in Intenz, Rhea, Reactome, PDBe etc. for Uniprot ids.
            //try {
            //queryEbeyeOtherDomainForIds();
            LOGGER.debug("UniProt IDs from UniProt+ChEBI+others: "
                    + uniprotEnzymeIds.size());
            //} catch (EnzymeFinderException enzymePortalSummary){
            // LOGGER.error("Unable to search EB-Eye other domains", enzymePortalSummary);
            //}

            uniprotIdPrefixSet.addAll(uniprotEnzymeIds);
        }

        List<String> idPrefixesList
                = new ArrayList<>(uniprotIdPrefixSet);

        LOGGER.debug("Getting enzyme summaries...");
        enzymeSummaryList = getEnzymeSummaries(idPrefixesList, searchParams.getSpecies());
        enzymeSearchResults.setSummaryentries(enzymeSummaryList);
        enzymeSearchResults.setTotalfound(enzymeSummaryList.size());
        if (uniprotIdPrefixSet.size() != enzymeSummaryList.size()) {
            LOGGER.warn((uniprotIdPrefixSet.size() - enzymeSummaryList.size())
                    + " UniProt ID prefixes have been lost");
        }
        LOGGER.debug("Building filters...");
        buildFilters(enzymeSearchResults);
        LOGGER.debug("Finished search");
        //closeResources();
        return enzymeSearchResults;
    }

    /**
     * Builds filters - species, compounds, diseases - from a result list.
     *
     * @param searchResults the result list, which will be modified by setting
     * the relevant filters.
     */
    private void buildFilters(SearchResults searchResults) {
        //  String[] commonSpecie = {"Human", "Mouse", "Rat", "Fruit fly", "Worm", "Yeast", "Ecoli"};
        // CommonSpecies [] commonSpecie = {"Homo sapiens","Mus musculus","Rattus norvegicus", "Drosophila melanogaster","Saccharomyces cerevisiae"};
        // List<String> commonSpecieList = Arrays.asList(commonSpecie);
        List<String> commonSpecieList = new ArrayList<>();
        for (CommonSpecies commonSpecies : CommonSpecies.values()) {
            commonSpecieList.add(commonSpecies.getScientificName());
        }

        Map<Integer, Species> priorityMapper = new TreeMap<>();

        // Set<Species> uniqueSpecies = new TreeSet<>();
        //Set<EnzymePortalDisease> uniqueDiseases = new TreeSet<>();
        AtomicInteger key = new AtomicInteger(50);
        AtomicInteger customKey = new AtomicInteger(6);
        System.out.println("Build unique species " + uniqueSpecies.size());
        for (Species sp : uniqueSpecies) {
            //Species sp = wrapper.getSpecies();

            if (commonSpecieList.contains(sp.getScientificname().split("\\(")[0].trim())) {
                // Human, Mouse, Rat, Fly, Worm, Yeast, Ecoli 
                // "Homo sapiens","Mus musculus","Rattus norvegicus", "Drosophila melanogaster","Worm","Saccharomyces cerevisiae","Ecoli"
                if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.Human.getScientificName())) {
                    priorityMapper.put(1, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.Mouse.getScientificName())) {
                    priorityMapper.put(2, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.Rat.getScientificName())) {
                    priorityMapper.put(3, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.Fruit_fly.getScientificName())) {
                    priorityMapper.put(4, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.Worm.getScientificName())) {
                    priorityMapper.put(5, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.Ecoli.getScientificName())) {
                    priorityMapper.put(6, sp);
                } else if (sp.getScientificname().split("\\(")[0].trim().equalsIgnoreCase(CommonSpecies.Baker_Yeast.getScientificName())) {
                    priorityMapper.put(customKey.getAndIncrement(), sp);

                }
            } else {

                priorityMapper.put(key.getAndIncrement(), sp);

            }
        }

        List<Species> speciesFilters = new LinkedList<>();
        for (Map.Entry<Integer, Species> map : priorityMapper.entrySet()) {
            speciesFilters.add(map.getValue());

        }

        System.out.println("TOTAL : Species : " + speciesFilters.size());
        System.out.println("TOTAL : Compounds : " + compoundFilters.size());
        System.out.println("TOTAL : Diseases : " + diseaseFilters.size());

        //compoundFilters.addAll(uniquecompounds);
        //diseaseFilters.addAll(uniqueDiseases);

        SearchFilters filters = new SearchFilters();
        filters.setSpecies(speciesFilters);
        filters.setCompounds(compoundFilters.stream().distinct().collect(Collectors.toList()));

        filters.setDiseases(diseaseFilters.stream().distinct().collect(Collectors.toList()));
        searchResults.setSearchfilters(filters);
    }

    public SearchResults getEnzymesByCompound(SearchParams searchParams) throws EnzymeFinderException {
        return null;
    }

    public NcbiBlastClient.Status getBlastStatus(String jobId) throws NcbiBlastClientException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        return null;
    }

    public SearchResults getBlastResult(String jobId) throws NcbiBlastClientException, MultiThreadingException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String blast(String sequence) throws NcbiBlastClientException {
        //return getBlastClient().run(sequence);
        return null;
    }
}
