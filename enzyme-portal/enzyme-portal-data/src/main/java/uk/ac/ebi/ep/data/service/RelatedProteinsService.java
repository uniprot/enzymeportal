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
import uk.ac.ebi.ep.data.domain.RelatedProteins;
import uk.ac.ebi.ep.data.repositories.RelatedProteinsRepository;


/**
 *
 * @author joseph
 */
@Transactional
@Service
public class RelatedProteinsService {
   
    
        @Autowired
    private RelatedProteinsRepository repository;
    
    @Transactional
    public RelatedProteins addRelatedProteins(RelatedProteins proteins) {
        RelatedProteins relatedProteins = repository.saveAndFlush(proteins);
        return relatedProteins;
    }
    
    @Transactional
    public List<RelatedProteins> addRelatedProteins(List<RelatedProteins> l) {
        List<RelatedProteins> relatedProteinses = repository.save(l);
        return relatedProteinses;
    }
    
    @Transactional(readOnly = true)
    public RelatedProteins findRelatedProteinsById(Long id) {
        
        return repository.findOne(id);
    }
    
    @Transactional(readOnly = true)
    public List<RelatedProteins> findRelatedProteins() {
        
        return repository.findAll();
    }
}
