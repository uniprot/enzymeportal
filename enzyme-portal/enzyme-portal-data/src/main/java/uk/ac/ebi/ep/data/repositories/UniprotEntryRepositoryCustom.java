/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.data.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.data.domain.UniprotEntry;

/**
 *
 * @author joseph
 */
@NoRepositoryBean
public interface UniprotEntryRepositoryCustom {
    // @EntityGraph(value = "graph.UniprotEntry", type = EntityGraphType.LOAD)
             //@EntityGraph("UniprotEntry")
    List<UniprotEntry> findEnzymesByNamePrefixes(List<String> nameprefixes);
     //@EntityGraph(value = "graph.UniprotEntry", type = EntityGraphType.LOAD)
      //@EntityGraph("UniprotEntry")       
     List<UniprotEntry> findEnzymesByAccessions(List<String> accessions);
    // List<Species> getSpecies(List<String> name_prefixes);
     
     
      @EntityGraph(value = "graph.UniprotEntry", type = EntityGraph.EntityGraphType.LOAD)
    UniprotEntry findByAccession(String accession);
    
    List<UniprotEntry> findEnzymeByNamePrefixAndProteinName(String namePrefix,String proteinName);
     

}
