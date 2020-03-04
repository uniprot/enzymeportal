package uk.ac.ebi.ep.enzymeservice.uniprot.service;

import java.util.List;
import uk.ac.ebi.ep.enzymeservice.uniprot.model.Comment;
import uk.ac.ebi.ep.enzymeservice.uniprot.model.Kinetics;
import uk.ac.ebi.ep.enzymeservice.uniprot.model.PhDependence;
import uk.ac.ebi.ep.enzymeservice.uniprot.model.TemperatureDependence;
import uk.ac.ebi.ep.enzymeservice.uniprot.model.UniprotApi;

/**
 *
 * @author joseph
 */
public interface UniprotService {

    UniprotApi uniprotApiByAccession(String accession);

    List<UniprotApi> uniprotApiByEc(String ec);

    Comment findKinectParamsCommentByAccession(String accession);

    Kinetics findKineticsByAccession(String accession);

    List<PhDependence> findPhDependenceByAccession(String accession);

    List<TemperatureDependence> findTemperatureDependenceByAccession(String accession);

}
