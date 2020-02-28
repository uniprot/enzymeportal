package uk.ac.ebi.ep.model.dao;

/**
 *
 * @author joseph
 */
//@Builder
public class WebStatXrefDao {

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

    String month;
    String releaseId;
    //String year;
    //Date releaseDate;

    public long getUniprot() {
        return uniprot;
    }

    public long getIntenz() {
        return intenz;
    }

    public long getOmim() {
        return omim;
    }

    public long getPdb() {
        return pdb;
    }

    public long getMetabolights() {
        return metabolights;
    }

    public long getKegg() {
        return kegg;
    }

    public long getRhea() {
        return rhea;
    }

    public long getReactome() {
        return reactome;
    }

    public long getChembl() {
        return chembl;
    }

    public long getChebi() {
        return chebi;
    }

    public String getMonth() {
        return month;
    }

    public String getReleaseId() {
        return releaseId;
    }
    
    

}
