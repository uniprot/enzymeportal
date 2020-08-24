package uk.ac.ebi.ep.restapi.service;

import java.util.List;
import reactor.core.publisher.Flux;
import uk.ac.ebi.ep.enzymeservice.reactome.view.PathWay;
import uk.ac.ebi.ep.literatureservice.dto.LabelledCitation;
import uk.ac.ebi.ep.restapi.dto.EnzymeDisease;
import uk.ac.ebi.ep.restapi.dto.PdbInfo;
import uk.ac.ebi.ep.restapi.dto.Reaction;
import uk.ac.ebi.ep.restapi.dto.SmallMolecule;
import uk.ac.ebi.ep.restapi.model.Protein;

/**
 *
 * @author joseph
 */
public interface ProteinApiService {

    Protein proteinByAccession(String accession);

    List<PdbInfo> pdbByAccession(String accession);

    Reaction enzymeReactionByAccession(String accession, int mcsaLimit);

    List<EnzymeDisease> diseasesByAccession(String accession);

    List<LabelledCitation> citationsByAccession(String accession, int limit);

    Flux<PathWay> pathwaysByAccession(String accession);

    SmallMolecule smallMoleculeByAccession(String accession);
}
