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

/**
 *
 * @author joseph
 */
@NoRepositoryBean
public interface EnzymePortalSummaryRepositoryCustom {

    EnzymePortalSummary findDiseaseEvidence(String accession);

    EnzymePortalSummary findEnzymeSummaryByAccession(String accession);

    List<EnzymePortalSummary> findEnzymeSummariesByAccession(String accessions);

    List<EnzymePortalSummary> findEnzymesByNamePrefixes(List<String> namePrefixes);

    List<EnzymePortalSummary> findEnzymeSummariesByAccessions(List<String> accessions);

    Page<EnzymePortalSummary> findEnzymeSummariesByAccessions(List<String> accessions, Pageable pageable);

}
