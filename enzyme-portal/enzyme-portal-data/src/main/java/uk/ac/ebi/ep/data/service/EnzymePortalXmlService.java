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
import uk.ac.ebi.ep.data.domain.IntenzEnzymes;
import uk.ac.ebi.ep.data.repositories.IntenzEnzymesRepository;

/**
 *
 * @author joseph
 */
@Transactional
@Service
public class EnzymePortalXmlService {

    @Autowired
    private IntenzEnzymesRepository intenzEnzymesRepository;

    /**
     * Note : This method should only used for Unit Test.
     * @param enzyme the Intenz enzyme 
     */
    public void addIntenzEnzyme(IntenzEnzymes enzyme) {
        intenzEnzymesRepository.save(enzyme);
    }
    
        /**
     * Note : This method should only used for Unit Test.
     * @param enzymes the Intenz enzymes
     */
    public void addIntenzEnzymes(List<IntenzEnzymes> enzymes) {
        intenzEnzymesRepository.save(enzymes);
    }


    @Transactional(readOnly = true)
    public List<IntenzEnzymes> findAllIntenzEnzymes() {

        return intenzEnzymesRepository.findAll();
    }

}
