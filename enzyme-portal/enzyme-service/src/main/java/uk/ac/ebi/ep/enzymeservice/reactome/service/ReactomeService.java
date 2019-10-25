package uk.ac.ebi.ep.enzymeservice.reactome.service;

import java.util.List;
import reactor.core.publisher.Mono;
import uk.ac.ebi.ep.enzymeservice.reactome.model.ReactomeResult;
import uk.ac.ebi.ep.enzymeservice.reactome.view.PathWay;

/**
 *
 * @author joseph
 */
public interface ReactomeService {

    Mono<ReactomeResult> findReactomeResultById(String reactomeId);

    //ResponseEntity<ReactomeResult> findReactomeResultByPathwayId(String pathwayId);

//    PathWay findPathwayById(String pathwayId);
//
//    List<PathWay> findPathwaysByIds(List<String> pathwayIds);

    Mono<PathWay> findPathwayById(String pathwayId);

    Mono<List<PathWay>> findPathwaysByIds(List<String> pathwayIds);

    //Mono<List<PathWay>> findPathwaysByIdsMono(List<String> pathwayIds);

}
