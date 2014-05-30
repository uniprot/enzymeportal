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
import uk.ac.ebi.ep.data.domain.Disease;
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
    public Disease addDisease(Disease d) {
        Disease disease = repository.saveAndFlush(d);
        return disease;
    }

    @Transactional
    public List<Disease> addDisease(List<Disease> d) {
        List<Disease> disease = repository.save(d);
        return disease;
    }

    @Transactional(readOnly = true)
    public Disease findById(Long id) {

     
        return repository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<Disease> findDiseases() {

        return repository.findAll();
    }

    //delete later after testing
//    @Transactional(rollbackFor = Exception.class)
//    public void dropDiseaseDatabase() {
//        repository.dropDiseaseDatabase();
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public void dropDiseaseDatabaseById(Long id) {
//        repository.delete(id);
//    }
//
//    @Transactional(rollbackFor = Exception.class)
//    public void deleteAllDisease() {
//        repository.deleteAll();
//    }

}
