/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.data.repositories;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.data.domain.EnzymePortalPathways;
import uk.ac.ebi.ep.data.enzyme.model.Pathway;

/**
 *
 * @author joseph
 */
@NoRepositoryBean
public interface EnzymePortalPathwaysRepositoryCustom {
    
    List<EnzymePortalPathways> findPathwaysByAccession(String accession);
     List<String> findAccessionsByPathwayId(String pathwayId);
    List<Pathway> findPathwaysByName(String pathwayName);
}
