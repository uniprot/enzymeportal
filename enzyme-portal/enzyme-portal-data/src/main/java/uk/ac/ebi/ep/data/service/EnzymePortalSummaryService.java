/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.EnzymePortalSummary;
import uk.ac.ebi.ep.data.repositories.EnzymePortalSummaryRepository;

/**
 *
 * @author joseph
 */
@Transactional
@Service
public class EnzymePortalSummaryService {

    @Autowired
    private EnzymePortalSummaryRepository enzymeSummaryRepository;

    @Transactional(readOnly = true)
    public List<EnzymePortalSummary> findEnzymePortalSummaries() {

        return enzymeSummaryRepository.findAll();
        //return enzymeSummaryRepository.findEnzymePortalSummaries();
    }
}
