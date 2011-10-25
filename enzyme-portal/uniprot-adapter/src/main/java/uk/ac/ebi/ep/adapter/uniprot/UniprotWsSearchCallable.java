package uk.ac.ebi.ep.adapter.uniprot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

/**
 * Superclass for Callables to search UniProt via web services. All queries get
 * automatically a filter for enzymes.
 * @author rafa
 * @param <T>
 *
 */
public abstract class UniprotWsSearchCallable<T>
implements Callable<T> {
	
	private static final Logger LOGGER = Logger.getLogger(UniprotWsSearchCallable.class);
	
	private String query;
	private UniprotConfig config;
	
	/**
	 * Basic constructor.
	 * @param query a <a href="http://www.uniprot.org/help/text-search">query</a>
	 * 		for uniprot.
	 * @param config configuration for UniProt.
	 */
	protected UniprotWsSearchCallable(String query, UniprotConfig config) {
		this.query = query;
		this.config = config;
	}

	/**
	 * Gets results from the web service.
	 * @param processor the processor to use, depending on what we are
	 * 		interested in.
	 * @return results from the web service, or <code>null</code> if none
	 * 		found.
	 */
	protected Object get(IUniprotWsSearchProcessor<?> processor){
		Object result = null;
		// Add flags for reviewed and enzymes:
		final String rev = config.isReviewed()? "+reviewed:yes" : "";
		final String enzymes = "+ec:*";
		String url = MessageFormat.format(config.getWsUrl(),
				query + rev + enzymes, processor.getFields());
		BufferedReader br = null;
		InputStreamReader isr = null;
		InputStream is = null;
		try {
			URLConnection con = new URL(url).openConnection();
			con.setReadTimeout(config.getTimeout());
			con.connect();
			is = con.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			result = processor.process(br);
		} catch (MalformedURLException e) {
			LOGGER.error("Bad URL: " + url, e);
		} catch (IOException e) {
			LOGGER.error("Could not open connection to " + url, e);
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					LOGGER.error("Unable to close stream", e);
				}
			if (isr != null)
				try {
					isr.close();
				} catch (IOException e) {
					LOGGER.error("Unable to close reader", e);
				}
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					LOGGER.error("Unable to close reader", e);
				}
		}
		return result;
	}

	/**
	 * An object which knows what columns to retrieve from the web service
	 * and how to process them.
	 * @author rafa
	 *
	 * @param <T>
	 */
	protected interface IUniprotWsSearchProcessor<T> {
		
		/**
		 * @return The fields (columns) retrieved from the web service.
		 */
		String getFields();
		
		/**
		 * Processes the stream from the web service.
		 * @param reader The object reading the response from the web service.
		 * @return The expected result from the implementation.
		 * @throws IOException
		 */
		T process(BufferedReader reader) throws IOException;
	}
}
