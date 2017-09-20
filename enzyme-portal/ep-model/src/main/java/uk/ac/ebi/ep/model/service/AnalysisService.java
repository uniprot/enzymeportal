/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.model.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.repositories.SpEnzymeEvidenceRepository;
import uk.ac.ebi.ep.model.SpEnzymeEvidence;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Transactional
@Service
public class AnalysisService {

    @Autowired
    private SpEnzymeEvidenceRepository enzymeEvidenceRepository;

    public void populateEvidences(List<SpEnzymeEvidence> evidences) {
        enzymeEvidenceRepository.save(evidences);
    }
}
