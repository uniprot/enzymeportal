package uk.ac.ebi.ep.metaboliteService.service;

import uk.ac.ebi.ep.metaboliteService.model.Metabolite;

/**
 *
 * @author joseph
 */
public interface MetabolightService {

    boolean isMetabolite(String chebiId);

    Metabolite getMetabolite(String chebiId);
}
