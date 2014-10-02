/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.data.domain.RelatedProteins;

/**
 *
 * @author joseph
 */
@NoRepositoryBean
public interface RelatedProteinsRepositoryCustom {
    List<RelatedProteins> findRelatedProteinsByNamePrefix(String nameprefix);
    List<RelatedProteins> findRelatedProteinsByNamePrefixes(List<String> nameprefixes);
}
