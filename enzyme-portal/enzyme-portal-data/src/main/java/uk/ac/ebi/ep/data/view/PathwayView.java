/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.view;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
//@Projection(name = "pathway", types = EnzymePortalPathways.class)
public interface PathwayView {

    String getPathwayId();

    String getPathwayGroupId();

    String getPathwayUrl();

    String getPathwayName();

//    String getStatus();
//
//    String getSpecies();
}
