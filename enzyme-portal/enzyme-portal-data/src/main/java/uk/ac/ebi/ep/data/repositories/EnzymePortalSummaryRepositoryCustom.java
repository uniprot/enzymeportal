/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.data.entry.Summary;

/**
 *
 * @author joseph
 */
@NoRepositoryBean
public interface EnzymePortalSummaryRepositoryCustom {


    List<Summary> findSummariesByCommentType(String commentType);

//    EnzymePortalSummary findEnzymeSummaryByAccession(String accession);
//
//    List<EnzymePortalSummary> findEnzymeSummariesByAccession(String accessions);
//
//    List<EnzymePortalSummary> findEnzymesByNamePrefixes(List<String> namePrefixes);
//
//    List<EnzymePortalSummary> findEnzymeSummariesByAccessions(List<String> accessions);
//
//    Page<EnzymePortalSummary> findEnzymeSummariesByAccessions(List<String> accessions, Pageable pageable);

}
