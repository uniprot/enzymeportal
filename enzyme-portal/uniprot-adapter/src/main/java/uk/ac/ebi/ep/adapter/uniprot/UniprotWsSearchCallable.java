package uk.ac.ebi.ep.adapter.uniprot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

/**
 * Callable to search UniProt via web services, getting UniProt IDs matching
 * a query.
 * @author rafa
 *
 */
public class UniprotWsSearchCallable implements Callable<List<String>> {
	
	private static final Logger LOGGER = Logger.getLogger(UniprotWsSearchCallable.class);
	
	private String query;
	private UniprotConfig config;
	
	/**
	 * Basic constructor.
	 * @param query a <a href="http://www.uniprot.org/help/text-search">query</a>
	 * 		for uniprot.
	 * @param config configuration for UniProt.
	 */
	public UniprotWsSearchCallable(String query, UniprotConfig config) {
		this.query = query;
		this.config = config;
	}

	public List<String> call() {
		return getUniprotIds();
	}

	/**
	 * Retrieves UniProt IDs for enzymes matching the query.
	 * @return
	 */
	protected List<String> getUniprotIds(){
		List<String> ids = null;
		String fields = "entry+name";
		// Add flags for reviewed and enzymes:
		final String rev = config.isReviewed()? " reviewed:yes" : "";
		final String enzymes = "+ec:*";
		String url = MessageFormat.format(config.getWsUrl(),
				query + rev + enzymes, fields);
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
			String id = null;
			while ((id = br.readLine()) != null){
				if (ids == null){
					ids = new ArrayList<String>();
					continue; // first line is a header
				}
				ids.add(id);
			}
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
		return ids;
	}

}
