package uk.ac.ebi.ep.adapter.uniprot;


/**
 * Exception thrown while requesting data from UniProt web services.
 * @author rafa
 *
 */
public class UniprotWsException extends Exception {

	public UniprotWsException(String msg, Throwable e) {
		super(msg, e);
	}

	private static final long serialVersionUID = -1047343429428021459L;

}
