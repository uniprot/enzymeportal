/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.uniprotservice.citation;

import java.util.List;
import org.springframework.context.annotation.Bean;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.interfaces.uniprot.citationsNew.Citation;
import uk.ac.ebi.uniprot.dataservice.client.Client;
import uk.ac.ebi.uniprot.dataservice.client.ServiceFactory;
import uk.ac.ebi.uniprot.dataservice.client.exception.ServiceException;
import uk.ac.ebi.uniprot.dataservice.client.uniprot.UniProtService;


/**
 *
 * @author joseph
 */
public class UniprotCitationService {
    
    //http://europepmc.org/RestfulWebService
    
        @Bean
    public UniProtService uniProtService() {
        ServiceFactory serviceFactoryInstance = Client.getServiceFactoryInstance();
       UniProtService uniprotService = serviceFactoryInstance.getUniProtQueryService();
       uniprotService.start();
        return uniprotService;
    }
    
    public void computeCitation(String accession) throws ServiceException{
        
     UniProtEntry entry =   uniProtService().getEntry(accession);
     
    List<Citation> citation = entry.getCitationsNew();
    
    citation.stream().forEach((Citation c) -> {
        System.out.println("Citation "+ c.getTitle()+ " "+ c.hasTitle());
     });
    }
}
