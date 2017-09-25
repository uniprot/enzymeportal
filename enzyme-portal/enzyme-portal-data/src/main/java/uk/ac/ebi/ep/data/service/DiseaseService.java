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
import uk.ac.ebi.ep.data.search.model.Disease;

/**
 *
 * @author joseph
 */
@Transactional
@Service
public class DiseaseService {
    
    @Autowired
    private DiseaseRepository diseaseRepository;
    
    @Transactional
    public EnzymePortalDisease addDisease(EnzymePortalDisease d) {
        return diseaseRepository.saveAndFlush(d);
        
    }
    
    @Transactional
    public List<EnzymePortalDisease> addDiseases(List<EnzymePortalDisease> d) {
       return diseaseRepository.save(d);
        
    }
    
    @Transactional(readOnly = true)
    public EnzymePortalDisease findById(Long id) {
        
        return diseaseRepository.findOne(id);
    }
    
    @Transactional(readOnly = true)
    public List<EnzymePortalDisease> findDiseases() {
        
        return diseaseRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<EnzymePortalDisease> findDiseasesByNamePrefix(List<String> namePrefixes) {
        
        return diseaseRepository.findDiseasesByNamePrefixes(namePrefixes);
    }
    
    @Transactional(readOnly = true)
    public List<EnzymePortalDisease> findDiseasesByAccessions(List<String> accessions) {
        
        return diseaseRepository.findDiseasesByAccessions(accessions);
    }
    
    @Transactional(readOnly = true)
    public List<Disease> findDiseasesByAccession(String accession) {
        
        return diseaseRepository.findDiseasesByAccession(accession);
    }

    
     /**
      * 
      * @param diseaseName formated name e.g name = String.format(%%%s%%,diseaseName)
      * @return matched diseases
      */
          @Transactional(readOnly = true)
     public List<Disease> findDiseasesLike(String diseaseName){
       
         return diseaseRepository.findDiseasesNameLike(diseaseName);
     }
    
    
}
