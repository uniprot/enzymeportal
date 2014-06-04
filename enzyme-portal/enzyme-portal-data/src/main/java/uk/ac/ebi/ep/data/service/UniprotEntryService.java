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
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.repositories.UniprotEntryRepository;

/**
 *
 * @author joseph
 */
@Transactional
@Service
public class UniprotEntryService {

    @Autowired
    private UniprotEntryRepository repository;

    @Transactional(readOnly = true)
    public UniprotEntry findByAccession(String accession) {

        return repository.findByAccession(accession);
    }



    @Transactional(readOnly = true)
    public List<String> findAllUniprotAccessions() {

        return repository.findAccession();
    }

}
