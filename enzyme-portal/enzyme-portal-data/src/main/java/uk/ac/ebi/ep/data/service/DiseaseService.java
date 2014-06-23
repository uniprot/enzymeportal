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
import uk.ac.ebi.ep.data.domain.EnzymePortalDisease;
import uk.ac.ebi.ep.data.repositories.DiseaseRepository;

/**
 *
 * @author joseph
 */
@Transactional
@Service
public class DiseaseService {

    @Autowired
    private DiseaseRepository repository;

    @Transactional
    public EnzymePortalDisease addDisease(EnzymePortalDisease d) {
        EnzymePortalDisease disease = repository.saveAndFlush(d);
        return disease;
    }

    @Transactional
    public List<EnzymePortalDisease> addDisease(List<EnzymePortalDisease> d) {
        List<EnzymePortalDisease> disease = repository.save(d);
        return disease;
    }

    @Transactional(readOnly = true)
    public EnzymePortalDisease findById(Long id) {

     
        return repository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<EnzymePortalDisease> findDiseases() {

        return repository.findAll();
    }



}
