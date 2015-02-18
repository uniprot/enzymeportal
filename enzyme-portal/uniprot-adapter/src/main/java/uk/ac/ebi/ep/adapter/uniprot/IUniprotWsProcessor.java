package uk.ac.ebi.ep.adapter.uniprot;

import java.io.IOException;

/**
 * An object which knows what columns to retrieve from the web service
 * and how to process them.
 * @author rafa
 *
 * @param <T> the type returned after processing the response from the web
 * 		service.
 * @param <R> the type retrieved from the web service.
 */
interface IUniprotWsProcessor<T, R> {
	
	/**
	 * @return The fields (columns) retrieved from the web service.
	 */
	String getFields();
	
	/**
	 * Processes the stream from the web service.
	 * @param r The object read from the web service.
	 * @return The expected result from the implementation.
	 * @throws IOException
	 */
	T process(R r) throws IOException;
}