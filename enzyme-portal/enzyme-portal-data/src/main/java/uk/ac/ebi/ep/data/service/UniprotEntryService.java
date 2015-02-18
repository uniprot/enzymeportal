/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.service;

import com.mysema.query.types.Predicate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.QUniprotEntry;
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
    public Optional<UniprotEntry> findByAccession(String accession) {

        //return repository.findByAccession(accession);
        return Optional.ofNullable(repository.findByAccession(accession));
    }

    @Transactional(readOnly = true)
    public UniprotEntry findByUniProtAccession(String accession) {

        return repository.findOne(isAccession(accession));
    }

    @Transactional(readOnly = true)
    public List<String> findAllUniprotAccessions() {

        return repository.findAccessions();
    }

    @Transactional(readOnly = true)
    public List<UniprotEntry> findUniprotEntries() {

       
        return repository.findUniprotEntries();
    }

    @Transactional(readOnly = true)
    public List<UniprotEntry> findByNamePrefixes(List<String> namePrefixes) {

        return repository.findEnzymesByNamePrefixes(namePrefixes);
    }

    @Transactional(readOnly = true)
    public List<UniprotEntry> findEnzymesByAccessions(List<String> accessions) {

        return repository.findEnzymesByAccessions(accessions);
    }

    private static Predicate isAccession(String accession) {

        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        Predicate predicate = enzyme.accession.equalsIgnoreCase(accession);

        return predicate;
    }

    private static Predicate byNamePrefix(String namePrefix) {

        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        Predicate predicate = enzyme.name.eq(namePrefix);

        return predicate;
    }
      
}
