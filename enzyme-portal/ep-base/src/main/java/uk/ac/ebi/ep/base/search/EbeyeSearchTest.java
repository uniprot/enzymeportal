/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.base.search;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.data.common.CommonSpecies;
import uk.ac.ebi.ep.data.dataconfig.DataConfig;
import uk.ac.ebi.ep.data.search.model.Species;
import uk.ac.ebi.ep.data.service.DiseaseService;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
import uk.ac.ebi.ep.data.service.UniprotEntryService;
import uk.ac.ebi.ep.ebeye.model.EbeyeSearchResult;
import uk.ac.ebi.ep.ebeye.protein.model.Entry;



/**
 *
 * @author joseph
 */
public class EbeyeSearchTest {

    @Autowired
    private static UniprotEntryService uniprotEntryService;

    public static void main(String args[]) {

        EbeyeSearchTest t = new EbeyeSearchTest();

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("uzpdev");
        //context.getEnvironment().setActiveProfiles("ezprel");
        context.register(DataConfig.class);
        context.scan("uk.ac.ebi.ep.data.dataconfig");
        context.refresh();

        UniprotEntryService service = context.getBean(UniprotEntryService.class);
         EnzymePortalService compoundService = context.getBean(EnzymePortalService.class);
         
         DiseaseService diseaseService = context.getBean(DiseaseService.class);

        RestTemplate restTemplate = new RestTemplate();
         String query = "alzheimer disease";
        String url = "http://www.ebi.ac.uk/ebisearch/ws/rest/uniprot?query="+query+"&format=json&size=100";

        EbeyeSearchResult searchResult = restTemplate.getForObject(url, EbeyeSearchResult.class);

        List<Entry> results = searchResult.getEntries().stream().distinct()
                .collect(Collectors.toList());

        //Collections.sort(results, (Entry n1, Entry n2) -> n1.getUniport_name().compareTo(n2.getUniport_name()));
       
              System.out.println("results " + results.size());
        for(Entry d : results){
            System.out.println("found "+ d.getUniprotAccession());
        }
        
        
//        List<UniprotEntry> enzymes = null;
//        List<EnzymePortalCompound> compounds = null;
//        List<EnzymePortalDisease> diseases = null;
//        List<String> prefixes = new LinkedList<>();
//        List<String> accessions = new ArrayList<>();
//        
//        results.stream().forEach((result) -> {
//           // prefixes.add("ACE");
//             prefixes.add(result.getUniport_name());
//            accessions.add(result.getUniprotAccession());
//            //System.out.println("names "+ result.getUniport_name());
//        });

        //find by prefixes
//        enzymes = service.findByNamePrefixes(prefixes);
//        //compounds = compoundService.findCompoundsByNamePrefix(prefixes);
//        //diseases = diseaseService.findDiseasesByNamePrefix(prefixes);
//        //List<Species> specieList = new LinkedList<>();
//        
//        //find by accessions
//        
//         //enzymes = service.findEnzymesByAccessions(accessions);
//         diseases = diseaseService.findDiseasesByAccessions(accessions);
//
//        Set<Species> uniqueSpecies = new TreeSet<>();
//        Set<EnzymePortalDisease> uniqueDiseases = new TreeSet<>();
//
//        System.out.println("final result " + enzymes.size());
//       // System.out.println("num of dieases "+ enzymes.iterator().next().getEnzymePortalDiseaseList().size());
//        for (UniprotEntry u : enzymes) {
//            //System.out.println("summary "+ u);
//            Species s = new Species();
//            s.setCommonname(u.getCommonName());
//            s.setScientificname(u.getScientificName());
//            s.setSelected(false);
//            uniqueSpecies.add(s);
//            
//            for(EnzymePortalDisease d : u.getEnzymePortalDiseaseSet()){
//            EnzymePortalDisease disease = new EnzymePortalDisease();
//            disease.setDiseaseId(d.getDiseaseId());
//            disease.setDiseaseName(d.getDiseaseName());
//            disease.setUrl(d.getUrl());
//            disease.setDefinition(d.getDefinition());
//            disease.setEvidence(d.getEvidence());
//            disease.setUniprotAccession(d.getUniprotAccession());
//            uniqueDiseases.add(disease);
//            }
//            
//            
//            
////            for(EnzymePortalCompound c : u.getEnzymePortalCompoundSet()){
////                EnzymePortalCompound compound = new EnzymePortalCompound();
////                compound.setCompoundId(c.getCompoundId());
////                compound.setCompoundName(c.getCompoundName());
////                compound.setCompoundRole(c.getCompoundRole());
////                compound.setCompoundSource(c.getCompoundSource());
////                compound.setRelationship(url);
////            }
//            
//           
//        }
        
        //species
        // t.buildSpeciesFilter(uniqueSpecies);
         
         //compounds
//         for(EnzymePortalCompound c : compounds){
//             //System.out.println("compounds found "+ c);
//         }
//         
//         //diseases
//        System.out.println("unique diseases found "+ uniqueDiseases.size());
//        for(EnzymePortalDisease d : uniqueDiseases){
//            //System.out.println("Diseases : "+ d);
//        }
        
        
         //System.out.println("number of EnzymePortalDiseases found "+ diseases.size());
//         for(EnzymePortalDisease d : diseases){
//             System.out.println("EnzymePortalDiseases found : "+ d);
//         }
//         
         

//        RestTemplate restTemplate = new RestTemplate();
//        String url = "http://www.ebi.ac.uk/ebisearch/ws/rest/uniprot?query=cancer&format=json&size=100";
//
//        EbeyeSearchResult searchResult = restTemplate.getForObject(url, EbeyeSearchResult.class);
//
//        List<UniProtDomain> results = searchResult.getUniProtDomains().stream().distinct()
//                .collect(Collectors.toList());
//
//    
//        //Collections.sort(results, (Entry n1, Entry n2) -> n1.getUniport_name().compareTo(n2.getUniport_name()));
//
//  
//        
//
//        for (Entry result : results) {
//
//            //System.out.println("Acc:    " + result.getUniprotAccession());
//            System.out.println("Id:   " + result.getUniport_name());
//            //System.out.println("Source:   " + result.getSource());
//           // Iterable<UniprotEntry> ue = uniprotEntryService.findByNamePrefix(result.getUniport_name());
//            //System.out.println("size "+ ue.iterator().next());
//        }
    }

    public List<Species> buildSpeciesFilter(Set<Species> uniqueSpecies) {

        List<String> commonSpecieList = new ArrayList<>();
        for (CommonSpecies commonSpecies : CommonSpecies.values()) {
            commonSpecieList.add(commonSpecies.getScientificName());
        }

        Map<Integer, Species> priorityMapper = new TreeMap<>();

        AtomicInteger key = new AtomicInteger(50);
        AtomicInteger customKey = new AtomicInteger(6);

        for (Species sp : uniqueSpecies) {

            if (commonSpecieList.contains(sp.getScientificname().split("\\(")[0].trim())) {
                // HUMAN, MOUSE, RAT, Fly, WORM, Yeast, ECOLI 
                // "Homo sapiens","Mus musculus","Rattus norvegicus", "Drosophila melanogaster","WORM","Saccharomyces cerevisiae","ECOLI"
                if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.HUMAN.getScientificName())) {
                    priorityMapper.put(1, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.MOUSE.getScientificName())) {
                    priorityMapper.put(2, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.RAT.getScientificName())) {
                    priorityMapper.put(3, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.FRUIT_FLY.getScientificName())) {
                    priorityMapper.put(4, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.WORM.getScientificName())) {
                    priorityMapper.put(5, sp);
                } else if (sp.getScientificname().equalsIgnoreCase(CommonSpecies.ECOLI.getScientificName())) {
                    priorityMapper.put(6, sp);
                } else if (sp.getScientificname().split("\\(")[0].trim().equalsIgnoreCase(CommonSpecies.BAKER_YEAST.getScientificName())) {
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

//        for (Species s : speciesFilters) {
//            System.out.println("Name " + s.getCommonname() + " " + "Sciencetific " + s.getScientificname());
//        }

        return speciesFilters;

        //building search filters
//          SearchFilters filters = new SearchFilters();
//        filters.setSpecies(speciesFilters);
//        //filters.setCompounds(compoundFilters);
//        //filters.setDiseases(diseaseFilter);
//       // searchResults.setSearchfilters(filters);
//        
//        
    }

}
