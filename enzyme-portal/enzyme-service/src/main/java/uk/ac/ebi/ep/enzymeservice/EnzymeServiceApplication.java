package uk.ac.ebi.ep.enzymeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "uk.ac.ebi.ep")
public class EnzymeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnzymeServiceApplication.class, args);
        //runService(args);
    }
//        private static void runServiceWs(String[] args) {
//        ReactomeService service = SpringApplication.run(EnzymeServiceApplication.class, args)
//                .getBean(ReactomeService.class);
//
//        String id ="R-HSA-4086398";
//        id="R-HSA-156581";
//       PathWay pathway = service.findPathwayById(id);
//            System.out.println("PATHWAY "+ pathway);
//        
//}

//    private static void runService(String[] args) {
//        ReactomeService service = SpringApplication.run(EnzymeServiceApplication.class, args)
//                .getBean(ReactomeService.class);
//
//        String id ="R-HSA-4086398";
//        id="R-HSA-156581";
//        ReactomeResult result = service.findReactomeResultById(id);
//      // Mono<ReactomeResult> mono = service.searchReactome(id);
//        System.out.println("RESULT "+ result);
//        
//        PathWay pathway = service.findPathwayById(id);
//        System.out.println("PATHWAY "+ pathway);
//        
//                List<String> pathwayIds = Stream.generate(() -> "R-HSA-2514859").limit(20)
//                .collect(Collectors.toList());
//                
//                Flux<PathWay> pathways = service.findPathwayById(pathwayIds);
//                
//                pathways.subscribe(d ->System.out.println(" Data "+ d));
//       
//
//       
//
//    }
}
