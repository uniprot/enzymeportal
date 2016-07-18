/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye.testing;

import java.util.List;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uk.ac.ebi.ep.ebeye.AccessionService;
import uk.ac.ebi.ep.ebeye.EbeyeRestService;
import uk.ac.ebi.ep.ebeye.EnzymeCentricService;
import uk.ac.ebi.ep.ebeye.ProteinCentricService;
import uk.ac.ebi.ep.ebeye.model.Protein;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class ProteinCentricTest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        //context.register(EnzymeCentricService.class);// use only with default contructor
        context.scan("uk.ac.ebi.ep.ebeye", "uk.ac.ebi.ep.ebeye.model");
        context.refresh();

        EnzymeCentricService enzymeCentricService = context.getBean(EnzymeCentricService.class);
        ProteinCentricService proteinCentricService = context.getBean(ProteinCentricService.class);
        EbeyeRestService ebeyeRestService = context.getBean(EbeyeRestService.class);
        AccessionService accessionService = context.getBean(AccessionService.class);

        String query = "kinase";
        query = "sildenafil";
        query ="Cathepsin B";
        //query = "cancer";
       // query ="1.1.1.1";
        //query ="5'-nucleotidase";
       // query="mtor";
       // query="Pyruvate kinase";
        int limit = 800;
        String ec ="3.1.4.35";
        //ec="2.7.11.11";
       // ec="2.7.1.40";
        ec="3.4.22.1";
        
                     List<Protein> proteins = ebeyeRestService.queryForUniqueProteins(ec, query, limit);
            System.out.println("proteins size" + proteins.size());
            proteins.stream().limit(5).forEach(p -> System.out.println("proteins " + p));

            if (proteins.isEmpty()) {
               // proteins = proteinCentricService.queryForUniqueProteins(entry.getEc(), limit);
                System.out.println("firrst query is empty ...");
                 proteins = ebeyeRestService.queryForUniqueProteins(ec, limit);
            }
        System.out.println("PROTEINS FOUND "+ proteins.size());
        
//               List<String> accessions = ebeyeRestService.queryForUniqueAccessions(ec, query, limit);
//        if (accessions.isEmpty()) {
//            System.out.println("it is empty "+ accessions.isEmpty());
//            //ec= "kinase";
//            //ec ="cancer";
//            accessions = ebeyeRestService.queryForUniqueAccessions(ec, limit);
//        }
//        System.out.println("ACCESSIONS FOUND " + accessions.size());
    }
}
