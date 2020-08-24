package uk.ac.ebi.ep.restapi.service;

import java.util.List;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import uk.ac.ebi.ep.indexservice.model.enzyme.EnzymeEntry;
import uk.ac.ebi.ep.indexservice.model.protein.ProteinGroupEntry;
import uk.ac.ebi.ep.literatureservice.model.Result;
import uk.ac.ebi.ep.reaction.mechanism.model.MechanismResult;
import uk.ac.ebi.ep.restapi.dto.BrendaParameter;
import uk.ac.ebi.ep.restapi.model.EnzymeModel;

/**
 *
 * @author joseph
 */
public interface EnzymeApiService {

    Flux<EnzymeModel> findEnzymes(int page, int pageSize);

    Page<EnzymeEntry> getEnzymeEntries(Pageable pageable);

    MechanismResult findReactionMechanism(String ec);

    List<Result> findCitations(String enzymeName, int limit);

    EnzymeEntry enzymeByEcNumber(@Valid String ecNumber);

    Flux<EnzymeEntry> getEnzyme(@Valid String ecNumber);

    BrendaParameter reactionParameterByEcNumber(String ec, int limit);

    Flux<ProteinGroupEntry> associatedProteinByEc(String ec, int limit);

    List<ProteinGroupEntry> associatedProteinSummaryByEc(String ec, int limit);

    Flux<ProteinGroupEntry> findProteinSummary(String searchTerm, String ec, int start, int pageSize);

    Page<ProteinGroupEntry> findProteinSummary(String query, String ec, Pageable pageable);

    int getEnzymesHitCount(String query);

    Page<EnzymeEntry> findEnzymesBySearchTerm(String searchTerm,String indexQuery, Pageable pageable);

}
