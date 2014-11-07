/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.data.domain.EnzymePortalSummary;
import uk.ac.ebi.ep.data.search.model.EnzymeSummary;

/**
 *
 * @author joseph
 */
@NoRepositoryBean
public interface EnzymePortalSummaryRepositoryCustom {

    List<EnzymePortalSummary> findByCommentType(String commentType, int limit);

    List<EnzymePortalSummary> findByCommentType(String commentType);

    List<EnzymePortalSummary> findByCommentText(String commentText, int limit);

    EnzymePortalSummary findDiseaseEvidence(String accession);

    EnzymePortalSummary findEnzymeSummaryByAccession(String accession);

    List<EnzymePortalSummary> findEnzymeSummariesByAccession(String accessions);

    List<EnzymePortalSummary> findEnzymesByNamePrefixes(List<String> name_prefixes);
    @Deprecated
    List<EnzymeSummary> findSummariesBYAccessions(List<String> accessions);

   

    List<EnzymePortalSummary> findEnzymeSummariesByAccessions(List<String> accessions);

    Page<EnzymePortalSummary> findEnzymeSummariesByAccessions(List<String> accessions, Pageable pageable);
    


}
