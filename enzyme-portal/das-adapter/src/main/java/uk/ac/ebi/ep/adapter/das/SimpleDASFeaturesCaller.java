package uk.ac.ebi.ep.adapter.das;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import uk.ac.ebi.das.jdas.adapters.features.DasGFFAdapter;
import uk.ac.ebi.das.jdas.adapters.features.DasGFFAdapter.SegmentAdapter;
import uk.ac.ebi.das.jdas.client.FeaturesClient;

public class SimpleDASFeaturesCaller implements Callable<SegmentAdapter> {

	private final Logger LOGGER = Logger.getLogger(SimpleDASFeaturesCaller.class);
	
	/**
	 * The URL of the DAS server.
	 */
	private String serverURL;
	
	/**
	 * The segments to query to the server.
	 */
	private List<String> segments;
	
	/**
	 * Constructor to call just one segment.
	 * @param serverURL
	 * @param segment
	 */
	public SimpleDASFeaturesCaller(String serverURL, String segment){
		this(serverURL, Collections.singletonList(segment));
	}
	
	/**
	 * Constructor to call several segments.
	 * @param serverURL
	 * @param segments
	 */
	public SimpleDASFeaturesCaller(String serverURL, List<String> segments){
		this.serverURL = serverURL;
		this.segments = segments;
	}

	public SegmentAdapter call() throws Exception {
		return getSegments().get(0);
	}

	public List<SegmentAdapter> getSegments()
	throws MalformedURLException, JAXBException, Exception {
		FeaturesClient featuresClient = null;
		try {
			LOGGER.debug(" -STR- before creating featuresClient");
			featuresClient = FeaturesClientPool.getInstance().borrowObject();
			LOGGER.debug(" -STR- before fetching DAS data");
			DasGFFAdapter dasGFF = featuresClient.fetchData(serverURL, segments);
			LOGGER.debug(" -STR- before returning");
			return dasGFF.getGFF().getSegment();
		} finally {
			if (featuresClient != null){
				try {
					FeaturesClientPool.getInstance().returnObject(featuresClient);
				} catch(Exception e){
					LOGGER.error("Unable to return DAS client", e);
				}
			}
		}
	}
}
