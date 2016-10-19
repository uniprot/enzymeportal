/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.base.search;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.base.config.EnzymesConfig;
import uk.ac.ebi.ep.data.dataconfig.DataConfig;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.exceptions.EnzymeFinderException;
import uk.ac.ebi.ep.data.search.model.SearchParams;
import uk.ac.ebi.ep.data.search.model.SearchResults;
import uk.ac.ebi.ep.data.service.DiseaseService;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
import uk.ac.ebi.ep.data.service.UniprotEntryService;
import uk.ac.ebi.ep.ebeye.EbeyeRestService;


/**
 *
 * @author joseph
 */
public class EPCoreTest {
    
    
    
       public static void main(String args[]) throws EnzymeFinderException {

        EbeyeSearchTest t = new EbeyeSearchTest();

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("uzpdev");
        //context.getEnvironment().setActiveProfiles("ezprel");
      context.register(DataConfig.class, EbeyeRestService.class, EnzymesConfig.class);
        context.scan("uk.ac.ebi.ep.data.dataconfig", "uk.ac.ebi.ep.base.config.EnzymesConfig", "uk.ac.ebi.ep.ebeye");
        context.refresh();

        EnzymePortalService service = context.getBean(EnzymePortalService.class);
        EbeyeRestService ebeyeService = context.getBean(EbeyeRestService.class);

        UniprotEntryService service1 = context.getBean(UniprotEntryService.class);
         EnzymePortalService compoundService = context.getBean(EnzymePortalService.class);
         
         DiseaseService diseaseService = context.getBean(DiseaseService.class);
         

        // List<UniprotXref> pdbcodes = service.findPDBcodesByAccession("O76074").stream().sorted().collect(Collectors.toList());
          // System.out.println("PDBCODES "+ pdbcodes);
         
         //EnzymePortalSummary su = service.findEnzymeSummaryByAccession("P51160");
         
           System.out.println("search family");
//                List<EcNumber> enzymeFamilies = service.findEnzymeFamiliesByTaxId(9606L);
//         System.out.println("families "+ enzymeFamilies.size());
//           System.out.println("DISTINCT FAMILIES " + enzymeFamilies);
//         
//         List<EnzymeCatalyticActivity> activities = service.findEnzymeCatalyticActivities();
//           System.out.println("ACTIVITIES CAT "+ activities.size());
//         
         
         
//           System.out.println("FINAL E.. "+ su.getUniprotAccession().getEnzymePortalPathwaysSet());
//          System.out.println("FINAL REACT "+ su.getUniprotAccession().getEnzymePortalReactionSet());
//           System.out.println(" summ comp "+ su.getUniprotAccession().getEnzymePortalCompoundSet());
//           System.out.println("summ dis "+ su.getUniprotAccession().getEnzymePortalDiseaseSet());
//           //System.out.println("pdbe "+ su.getUniprotAccession().getUniprotXrefSet());
//           //System.out.println("summ summ : "+ su.getUniprotAccession().getEnzymePortalSummarySet());
////         UniprotEntry e = service.findByAccession("Q9NP56");
////         
//           System.out.println("found entry "+ e.getName() + " pathways "+ e.getEnzymePortalPathwaysSet());
//  
           System.out.println("about to search ...");
//            List<EnzymePortalDisease> ds = service.findAllDiseases();
//            System.out.println("service find disease "+ ds.size());
//           
//            System.out.println("searching old method .....");
//         List<EnzymePortalDisease> di = diseaseService.findDiseases();
//         
//           System.out.println("num disease found "+ di.size());
           
           ///AUTOCOMPLETE
           String name = "parkinson disease";
           name = "parkin";
          name = String.format("%%%s%%", name);
           System.out.println("name search "+ name);
          // List<Disease> fuzz = diseaseService.findDiseaseNamesLike(name);
           
           //System.out.println("FUZZ "+ fuzz);
           
//           List<Disease> di = diseaseService.findDiseasesLike(name);
//           for(Disease ds : di){
//               System.out.println(ds.getName() +"TESTIGN ENTITY GRAPH "+ ds.getId());
//           }
//           System.out.println("fuzz obj "+ di);
//           String accession = "P51160";
//           Optional<UniprotEntry> ent = service1.findByAccession(accession);//.orElseThrow(() -> new EnzymeFinderException(String.format("User=%s not found", accession)));
//                   
//                  
//           
//           System.out.println("OPTIONAL TESTING "+ ent);
//           if(ent.isPresent()){
//               System.out.println("VALUE "+ ent.get());
//           }else{
//               System.out.println("NO VALUE "+ ent);
//           }
           
           //TAXONOMY
//           List<Taxonomy> tax = service.findModelOrganisms();
//           
//           //System.out.println("TAXONOMY FOUND "+ tax);
//           
//           for(Taxonomy tx : tax){
//               System.out.println(tx.getTaxId()+": "+tx.getCommonName()+" : "+tx.getScientificName()+ ": "+tx.getNum_enzymes());  
//           }
          
           
           
           
           ///////////////&&&&&&&&&&&&&&&&&&&&&&
           
          RestTemplate restTemplate = new RestTemplate();
         String query = "alzheimer disease";
         //query ="sildenafil";
         //query = " cancer";
         query = "kinase";
  //12.651s      
         //String url = "http://www.ebi.ac.uk/ebisearch/ws/rest/uniprot?query="+query+"&format=json&size=100";
        
               String url = "http://wwwdev.ebi.ac.uk/ebisearch/ws/rest/enzymeportal?format=json&size=100&query="+query+"";
        
      

//        EbeyeSearchResult searchResult = restTemplate.getForObject(url, EbeyeSearchResult.class);
//
//        List<UniProtDomain> result = searchResult.getUniProtDomains().stream().distinct()
//                .collect(Collectors.toList());

//        //Collections.sort(results, (UniProtDomain n1, UniProtDomain n2) -> n1.getUniport_name().compareTo(n2.getUniport_name()));
//       List<String> accesions = new ArrayList<>();
//              System.out.println("results from ebeye " + result.size());
//        for(UniProtDomain d : result){
//            //System.out.println("found "+ d.getUniprot_accession());
//            accesions.add(d.getUniprot_accession());
//            //accesions.add(d.getUniport_name());
//        }         
//           
//           
//         accesions.stream().distinct().collect(Collectors.toList());
//           System.out.println("how many accessions "+ accesions.size());
//           
//    
//     Pageable  pageable = new PageRequest(0, 10);
//          Page<UniprotEntry> ex = service.findEnzymesByTaxonomy(9606L, pageable);
//          
//       //Page<UniprotEntry> ex = service.findEnzymesBySpecial(accesions, pageable);
//       
//       // Page<EnzymeSummary> ex = service.findEnzymesByAccessions(accesions, pageable);
//          
//           System.out.println("num "+ ex.getContent().size());
//           System.out.println("total element "+ ex.getTotalElements());
//           System.out.println(" num element "+ ex.getNumberOfElements());
//           System.out.println("total pages  "+ex.getTotalPages());
//           
//           for(UniprotEntry e : ex){
//               
////for(EnzymeSummary e : ex){
////System.out.println(" "+ e.getUniprotEntry().getProteinName() +" "+ e.getUniprotEntry().getRelatedspecies().size()); 
//                 //System.out.println("eeee "+ e.getProteinName() +" REL "+ e.getRelatedspecies().size());
//               //alz = 14 -15 sec
//               //kinase 15.215 - 15.750
//           }
//           
           
//           List<Species> species = service.findSpeciesByTaxId(9606L);
//           System.out.println(" species filters "+ species.size());
//           
//           List<Disease> dix = service.findDiseasesByTaxId(9606L);
//           System.out.println("DIX FILTER "+ dix.size());
//           
//           List<Compound> comps = service.findCompoundsByTaxId(9606L);
//           System.out.println("COMPOUDS "+ comps.size());
           
           ////////////%%%%%%%%%%%%%%%%%%%%%%%%%%
           
           
           
           
//            for(Summary e : ex){
//       //System.out.println("ec "+ e.getEc() +"pdb "+ e.getPdbeaccession() +" func "+ e.getFunction() );
//       
//               // System.out.println("dis "+ e.getDiseases().size() +" comp "+ e.getCompounds().size() + " symn "+ e.getSynonym());
//                
//                //System.out.println("rel "+e.getRelatedspecies().size());
//                //System.out.println("species "+ e.getSpecies());
//                System.out.println("eeee "+ e.getUniprotEntry().getProteinName() +" REL "+ e.getUniprotEntry().getRelatedspecies().size());
//            }
           
           
//            List<UniprotEntry> ez = service.findEnzymesByTaxId(9606L);
//            System.out.println("how many enzyes "+ ez.size());
//            
//            for(UniprotEntry e : ez){
//                System.out.println("enzymes "+ e.getProteinName());
//            }
           
           
//        List<String> accs =   service.findAccessionsByTaxId(9600L);
//        
//         //List<String> accs =   service.findAccessionsByTaxId(9606L);
//        
//           System.out.println("num accs "+ accs.size());
//           
//           List<EnzymeSummary> sums = service.findSummariesBYAccessions(accs).stream().distinct().collect(Collectors.toList());
//           System.out.println("TOTAL SUMS "+ sums.size());
//           
//           for(EnzymeSummary w : sums){
//            
//                 System.out.println("ESUMMARIES "+ w);
//           }
           
         
         
         EnzymeFinder finder= null;// new EnzymeFinder(service);
           SearchParams params = new SearchParams();
           params.setCompound("");
           params.setPrevioustext("");
           params.setStart(0);
           params.setType(SearchParams.SearchType.KEYWORD);
           params.setText("alzheimer disease");
           //params.setText("kinase");
           params.setText("sildenafil");
           //params.setText("CFTR");
           //params.setText("cancer");
           //params.setText("REACT_1400.4");//not in ebeye index uniprot
            params.setText("dna ligase");
            //params.setText("'human'");
            //params.setText("ligase");
            //params.setText("kinase");
           
           
//           List<String> dd = new ArrayList<>();
//           dd.add("P04062");
//            dd.add("Q13126");
//            //SearchResults results = finder.computeEnzymeSummariesByAccessions(dd);
//            //System.out.println("found "+ results);
            
            
//            List<EnzymePortalReaction> rxn = finder.findAllReactions();
//            System.out.println("RXN "+ rxn.size());
//           List<EnzymePortalPathways> path = finder.findAllPathways();
//           System.out.println("PATH "+ path.size());
//            
//           
           
      SearchResults results = finder.getEnzymes(params);// finder.computeEnzymeSummariesByPathwayId("REACT_9527");  //finder.getEnzymes(params);
     // finder.computeEnzymeSummariesByPathwayId("REACT_217977"); //217977
           System.out.println("Results "+ results.getTotalfound());
           System.out.println("number of Species in filter "+  results.getSearchfilters().getSpecies().size());
           System.out.println("number of diseases in filter "+  results.getSearchfilters().getDiseases().size());
           System.out.println("number of compounds in filter "+ results.getSearchfilters().getCompounds().size());
           
           System.out.println("ENZYME FAMILY FILTER "+ results.getSearchfilters().getEcNumbers());
          // System.out.println("FAMILY "+ results.getSearchfilters().getEcNumbers().stream().findAny().get().getFamily());
//           List<EcNumber> family = results.getSearchfilters().getEcNumbers().stream().distinct().collect(Collectors.toList());
//           for(EcNumber fam : family){
//           System.out.println("FAMILY "+ fam.getFamily());
//           }
           
           for(UniprotEntry entr : results.getSummaryentries()){
           //System.out.println("CATALYTIC "+ entr.getEnzymeCatalyticActivitySet());
           }
           System.out.println("Number of enzyme summaries "+ results.getSummaryentries().size());
           
//           for(Disease d : results.getSearchfilters().getDiseases()){
//               System.out.println("numEnzyme "+ d.getNumEnzyme());
//           }
         
           
//           for(Compound c : results.getSearchfilters().getCompounds()){
//               System.out.println("compounds "+ c);
//           }

           
           
           for(UniprotEntry s : results.getSummaryentries()){
               //System.out.println("PROTEIN "+ s.getProteinName() + " rel "+ s.getRelatedspecies().size());
               //System.out.println("Accessions "+ s.getAccession());
//System.out.println("synomns  "+ s.getSynonym().size());
               
               //System.out.println("PDB "+ s.getPdbeaccession().size());
               //System.out.println("ACCESSION "+ s.getAccession() + " UNIPROT_NAME "+ s.getUniprotid());
            //System.out.println("protein name "+ s.getName() +" Related species :"+ s.getRelatedspecies().size());
               //System.out.println(" acc "+ s.getAccession() +" dis"+ s.getDiseases());
//               for(UniprotEntry e : s.getRelatedspecies()){
//                   //System.out.println("related "+ e.getPdbeaccession());
//                   System.out.println("other related "+ e.getCommonName());
//               }
//            for(Disease x : s.getDiseases()){
//                System.out.println("synonms "+ x);
//            }

//            for(Compound c : s.getCompounds()){
//                //System.out.println("comp found "+ c.getId() +" : "+ c.getName());
//               //System.out.println("final compouns "+ c.getRole());
//            }
//            for(EnzymeAccession e : s.getRelatedspecies()){
//                System.out.println("Ea "+ e);
//                for(Disease d : e.getSpecies().getDiseases()){
//                System.out.println("final disease "+ d.getName());
//               }
//            }
               //System.out.println("syn "+ s.getSynonym().size());
               //System.out.println("UniPort Name : "+ s.getUniprotid()+" Access: "+ s.getUniprotaccessions()+ " enzyme name :  "+ s.getName() +"species "+ s.getSpecies());
               //System.out.println("more "+ s.getFunction() + " "+ s.getEc() +" : "+ s.getDiseases().size() +" :"+ s.getCompounds().size() );
           }
           
           
  //results = finder.getEnzymesByCompound(null);
    
          // System.out.println("for compounds "+ results.getTotalfound());
           
           
         
       }
       
      //320 IN Total time: 20.223s  parallel
       //353 Total time: 20.358s normal
       
       
       //query
      // impl  = Total time: 4:23.765s and totla of 530 enzymes
       
       //query = Total time: 21.324s total 530 enzymes
}
