package uk.ac.ebi.ep.web.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author joseph
 */

@Data
@EqualsAndHashCode
public class WebStat {

    long uniprot;

    long intenz;

    long omim;

    long pdb;

    long metabolights;

    long kegg;

    long rhea;

    long reactome;

    long chembl;

    long chebi;

    //components
    long proteinStructure;

    long disease;

    long pathways;

    long catalyticActivities;

    long rheaReaction;

    long cofactors;

    long inhibitors;

    long activators;

    long metabolites;

    long expEvidence;

    long reviewed;

    long unreviewed;
    


    
    
}
