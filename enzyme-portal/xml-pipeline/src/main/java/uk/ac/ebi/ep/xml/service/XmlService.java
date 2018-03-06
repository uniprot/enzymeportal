package uk.ac.ebi.ep.xml.service;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import uk.ac.ebi.ep.xml.entity.EnzymePortalUniqueEc;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
public interface XmlService {
//unique enzymes

    Long countEnzymes();

    Stream<EnzymePortalUniqueEc> streamEnzymesByEc(String ecNumber);
     Stream<EnzymePortalUniqueEc> streamEnzymes();

    CompletableFuture<EnzymePortalUniqueEc> findCompletableFutureEnzymesByEcClass(Short ecFamily);

////proteins
//    // Stream<UniprotEntry> streamEnzymesByEc(String ecNumber);
//    //EC_NUMBER enzymes
//    CompletableFuture<EnzymePortalUniqueEc> findCompletableFutureEnzymesByEcClass(Short ecFamily);
//
//    List<EnzymePortalEcNumbers> findEnzymesByEcClass(Integer ecFamily);
//
//    List<EnzymePortalEcNumbers> findByEcNumber(String ecNumber);
//
//    Long countEnzymePortalEnzymes();
//
//    //INTENZ enzymes
//    List<IntenzEnzymes> findNonTransferredEnzymesWithNoAcc();
//
//    Long countIntenzEnzymes();
//
//    List<IntenzEnzymes> findNonTransferredEnzymesWithNoAcc(Integer limit);
}
