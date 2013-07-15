package uk.ac.ebi.ep.adapter.chembl;

/**
 * @author rafa
 * @since 1.0.0
 */
public class ChemblAdapterException extends Exception {

    public ChemblAdapterException(String chemblId, Exception e) {
        super(chemblId, e);
    }
}
