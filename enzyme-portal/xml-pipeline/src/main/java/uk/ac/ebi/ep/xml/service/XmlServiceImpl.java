package uk.ac.ebi.ep.xml.service;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.xml.entity.EnzymePortalUniqueEc;
import uk.ac.ebi.ep.xml.repository.EnzymePortalUniqueEcRepository;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Service("xmlService")
public class XmlServiceImpl implements XmlService {

   // private final IntenzEnzymesRepository intenzEnzymesRepository;
   // private final UniprotEntryRepository uniprotEntryRepository;
    //private final EnzymePortalEcNumbersRepository enzymePortalEcNumbersRepository;
    private final EnzymePortalUniqueEcRepository enzymePortalUniqueEcRepository;


    @Autowired
    public XmlServiceImpl(EnzymePortalUniqueEcRepository enzymePortalUniqueEcRepository) {
        this.enzymePortalUniqueEcRepository = enzymePortalUniqueEcRepository;
    }
    
    

    @Transactional(readOnly = true)
    @Override
    public Long countEnzymes() {
        return enzymePortalUniqueEcRepository.countEnzymes();
    }
    
        @Transactional(readOnly = true)
    @Override
    public Stream<EnzymePortalUniqueEc> streamEnzymes() {
        return enzymePortalUniqueEcRepository.streamEnzymes();
    }

    @Transactional(readOnly = true)
    @Override
    public Stream<EnzymePortalUniqueEc> streamEnzymesByEc(String ecNumber) {
        return enzymePortalUniqueEcRepository.streamEnzymesByEc(ecNumber);
    }
    
    
    @Transactional(readOnly = true)
    @Override
    public Stream<EnzymePortalUniqueEc> streamEnzymesByFamily(Short ecFamily) {
        return enzymePortalUniqueEcRepository.streamEnzymesByFamily(ecFamily);
    }

    @Transactional(readOnly = true)
    @Override
    public CompletableFuture<EnzymePortalUniqueEc> findCompletableFutureEnzymesByEcClass(Short ecFamily) {
        return enzymePortalUniqueEcRepository.findCompletableFutureEnzymesByEcClass(ecFamily);
    }

//    @Transactional(readOnly = true)
//    @Override
//    public List<EnzymePortalEcNumbers> findEnzymesByEcClass(Integer ecFamily) {
//        return enzymePortalEcNumbersRepository.findEnzymesByEcClass(ecFamily);
//    }
//
//    @Transactional(readOnly = true)
//    @Override
//    public List<EnzymePortalEcNumbers> findByEcNumber(String ecNumber) {
//        return enzymePortalEcNumbersRepository.findByEcNumber(ecNumber);
//    }
//
//    @Transactional(readOnly = true)
//    @Override
//    public Long countEnzymePortalEnzymes() {
//        return enzymePortalEcNumbersRepository.countEnzymes();
//    }
//
//    @Transactional(readOnly = true)
//    @Override
//    public List<IntenzEnzymes> findNonTransferredEnzymesWithNoAcc() {
//        return intenzEnzymesRepository.findNonTransferredEnzymesWithNoAcc();
//    }
//
//    @Transactional(readOnly = true)
//    @Override
//    public List<IntenzEnzymes> findNonTransferredEnzymesWithNoAcc(Integer limit) {
//        return intenzEnzymesRepository.findNonTransferredEnzymesWithNoAcc(limit);
//    }
//
//    @Transactional(readOnly = true)
//    @Override
//    public Long countIntenzEnzymes() {
//        return intenzEnzymesRepository.countIntenzEnzymes();
//    }

}
