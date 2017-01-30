/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.service;

import com.mysema.query.types.Predicate;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.IntenzEnzymes;
import uk.ac.ebi.ep.data.domain.QUniprotEntry;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.repositories.IntenzEnzymesRepository;
import uk.ac.ebi.ep.data.repositories.UniprotEntryRepository;

/**
 *
 * @author joseph
 */
@Service
public class EnzymePortalXmlService {

    @Autowired
    private IntenzEnzymesRepository intenzEnzymesRepository;
    @Autowired
    private UniprotEntryRepository uniprotEntryRepository;

    private static Predicate swissprotEnzymesByEcNumber(String ecNumber) {
        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        return enzyme.entryType.eq(Integer.valueOf(0).shortValue()).and(enzyme.enzymePortalEcNumbersSet.any().ecNumber.equalsIgnoreCase(ecNumber));

    }

    private static Predicate enzymesByEcNumber(String ecNumber) {
        QUniprotEntry enzyme = QUniprotEntry.uniprotEntry;

        return enzyme.enzymePortalEcNumbersSet.any().ecNumber.equalsIgnoreCase(ecNumber);

    }

    /**
     * Note : This method should only used for Unit Test.
     *
     * @param enzyme the Intenz enzyme
     */
    public void addIntenzEnzyme(IntenzEnzymes enzyme) {
        intenzEnzymesRepository.save(enzyme);
    }

    /**
     * Note : This method should only used for Unit Test.
     *
     * @param enzymes the Intenz enzymes
     */
    public void addIntenzEnzymes(List<IntenzEnzymes> enzymes) {
        intenzEnzymesRepository.save(enzymes);
    }

    @Transactional(readOnly = true)
    public List<IntenzEnzymes> findAllIntenzEnzymes() {

        return intenzEnzymesRepository.findIntenzEnzymes();
    }

    @Transactional(readOnly = true)
    public Iterable<UniprotEntry> findSwissprotEnzymesByEcNumber(String ec) {

        return uniprotEntryRepository.findAll(swissprotEnzymesByEcNumber(ec));
    }

    @Transactional(readOnly = true)
    public Iterable<UniprotEntry> findEnzymesByEcNumber(String ec) {

        return uniprotEntryRepository.findAll(enzymesByEcNumber(ec));
    }

    @Transactional(readOnly = true)
    public List<UniprotEntry> findEnzymesByEcNumberNativeQuery(String ec) {

        return uniprotEntryRepository.findEnzymesByEc(ec);
    }

    public Page<UniprotEntry> findPageableEnzymesByEcNumber(Pageable pageable, String ec) {

        return uniprotEntryRepository.findAll(enzymesByEcNumber(ec), pageable);
    }

    @Transactional(readOnly = true)
    public List<UniprotEntry> findUniprotEntries() {

        return uniprotEntryRepository.findUniprotEntries();
    }

    @Transactional(readOnly = true)
    public Long countUniprotEntries() {
        return uniprotEntryRepository.countUniprotEntries();
    }

    //****** TODO ******
    @Transactional(readOnly = true)
    public Page<UniprotEntry> findPageableUniprotEntries(Pageable pageable) {

        return uniprotEntryRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<UniprotEntry> findUniprotEntriesOrderedByEntryType() {

        return uniprotEntryRepository.findUniprotEntriesOrderedByEntryType();
    }

    @Transactional(readOnly = true)
    public List<UniprotEntry> findSwissprotEnzymesByEc(String ec) {

        return uniprotEntryRepository.findSwissprotEnzymesByEc(ec);
    }

    @Transactional(readOnly = true)
    public Stream<List<UniprotEntry>> findStreamedSwissprotEnzymesByEc(String ec) {
        return uniprotEntryRepository.findStreamedSwissprotEnzymesByEc(ec);
    }

    @Deprecated
    @Transactional(readOnly = true)
    public Stream<UniprotEntry> streamEnzymes() {
        return uniprotEntryRepository.streamEnzymes();
    }

    
    public Stream<IntenzEnzymes> streamIntenzEnzymes() {
        return intenzEnzymesRepository.streamAllIntenzEnzymes();
    }

    @Transactional(readOnly = true)
    public List<IntenzEnzymes> findNonTransferredEnzymes() {

        return intenzEnzymesRepository.findNonTransferredEnzymes();
    }

}
