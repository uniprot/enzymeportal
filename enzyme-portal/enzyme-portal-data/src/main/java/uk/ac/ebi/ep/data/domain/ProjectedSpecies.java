/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.domain;

import org.springframework.data.rest.core.config.Projection;

/**
 *
 * @author joseph
 */
@Projection(name = "projectedSpecies", types = {UniprotEntry.class})
public interface ProjectedSpecies {

//    @Value("#{target.scientificName + ' ' + target.commonName}")
//		String getFullName();
   // Species getSpecies();
    //Set<EnzymePortalCompound> getEnzymePortalCompoundSet();

    Long getTaxId();

    String getScientificName();

    String getCommonName();

}
