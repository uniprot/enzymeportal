package uk.ac.ebi.ep.dataservice.dto;

import java.util.Date;

/**
 *
 * @author joseph
 */
public interface WebStatXrefView {

    long getUniprot();

    long getIntenz();

    long getOmim();

    long getPdb();

    long getMetabolights();

    long getKegg();

    long getRhea();

    long getReactome();

    long getChembl();

    long getChebi();

    String getReleaseId();

    Date getReleaseDate();


}
