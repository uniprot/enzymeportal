/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.domain.UniprotXref;
import uk.ac.ebi.ep.data.repositories.UniprotEntryRepository;
import uk.ac.ebi.ep.data.repositories.UniprotXrefRepository;

/**
 *
 * @author joseph
 */
@Transactional
@Service
public class EnzymePortalParserService {
    
    @Autowired
    private UniprotXrefRepository xrefRepository;
     @Autowired
    private UniprotEntryRepository uniprotEntryRepository;
     
         @Transactional(readOnly = true)
    public Optional<UniprotEntry> findByAccession(String accession) {

        return Optional.ofNullable(uniprotEntryRepository.findByAccession(accession));
    }
    
    @Transactional(readOnly = true)
    public UniprotXref findByPdbId(String pdbId) {
        
        return xrefRepository.findPdbById(pdbId);
    }
    
    
    
        @Transactional(readOnly = true)
    public List<UniprotXref> findPDBcodes() {
        return xrefRepository.findPDBcodes();
    }
    @Transactional(readOnly = true)
    public List<String> findPdbIdsWithNoNames() {
        return xrefRepository.findPdbCodesWithNoNames();
    }
    
    @Transactional(readOnly = true)
    public List<UniprotXref> updatePDB(List<UniprotXref> pdb) {
        
        return xrefRepository.save(pdb);
    }
    
}
