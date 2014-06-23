/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import uk.ac.ebi.ep.data.domain.EnzymePortalSummary;

/**
 *
 * @author joseph
 */
@NoRepositoryBean
public interface EnzymeSummaryRepositoryCustom {

    List<EnzymePortalSummary> findByCommentType(String commentType, int limit);

    List<EnzymePortalSummary> findByCommentType(String commentType);

    List<EnzymePortalSummary> findByCommentText(String commentText, int limit);

    EnzymePortalSummary findDiseaseEvidence(String accession);
    EnzymePortalSummary findByAccession(String accession);

}
