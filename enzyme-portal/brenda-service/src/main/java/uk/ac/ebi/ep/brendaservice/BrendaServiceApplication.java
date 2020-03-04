package uk.ac.ebi.ep.brendaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import uk.ac.ebi.ep.brendaservice.dto.BrendaResult;
import uk.ac.ebi.ep.brendaservice.service.BrendaService;

@SpringBootApplication
@EnableCaching
public class BrendaServiceApplication {

    public static void mainX(String[] args) {
        SpringApplication.run(BrendaServiceApplication.class, args);

    }

    public static void main(String[] args) {
        BrendaService service = SpringApplication.run(BrendaServiceApplication.class, args)
                .getBean(BrendaService.class);
        BrendaResult list = service.findBrendaResultByEc("6.1.1.1", 7, true);
        System.out.println("LIST " + list.getBrenda().size());
        System.out.println("OTEHRS " + list.getPh().size() + " Temp " + list.getTemperature().size());
        BrendaResult list1 = service.findBrendaResultByEc("3.1.1.1", 2, true);
        System.out.println("LIST 2 " + list1.getBrenda());
        BrendaResult list2 = service.findBrendaResultByEc("3.1.1.1", 7, true);
        System.out.println("LIST 3 " + list2.getBrenda());

    }

}
