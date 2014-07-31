/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.data.repositories;

import java.util.List;
import uk.ac.ebi.ep.data.domain.UniprotEntry;

/**
 *
 * @author joseph
 */
public interface UniprotEntryRepositoryCustom {
     List<UniprotEntry> findEnzymesByNamePrefixes(List<String> name_prefixes);
     List<UniprotEntry> findEnzymesByAccessions(List<String> accessions);
    // List<Species> getSpecies(List<String> name_prefixes);
}
