package uk.ac.ebi.ep.adapter.bioportal;

/**
 * Exception thrown while querying BioPortal.
 * @author rafa
 *
 */
public class BioportalAdapterException extends Exception {

	private static final long serialVersionUID = -7780365580065755918L;

	public BioportalAdapterException(String msg, Throwable e) {
		super(msg, e);
	}

}
