package uk.ac.ebi.ep.adapter;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import javax.xml.bind.JAXBException;

import uk.ac.ebi.das.jdas.adapters.features.DasGFFAdapter;
import uk.ac.ebi.das.jdas.adapters.features.DasGFFAdapter.SegmentAdapter;
import uk.ac.ebi.das.jdas.client.FeaturesClient;

public class SimpleDASFeaturesCaller implements Callable<List<SegmentAdapter>> {

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
	SimpleDASFeaturesCaller(String serverURL, String segment){
		this(serverURL, Arrays.asList(new String[]{ segment }));
	}
	
	/**
	 * Constructor to call several segments.
	 * @param serverURL
	 * @param segments
	 */
	SimpleDASFeaturesCaller(String serverURL, List<String> segments){
		this.serverURL = serverURL;
		this.segments = segments;
	}

	public List<SegmentAdapter> call() throws Exception {
		return getFeatures();
	}

	List<SegmentAdapter> getFeatures()
	throws MalformedURLException, JAXBException {
		FeaturesClient featuresClient = new FeaturesClient();
		DasGFFAdapter dasGFF = featuresClient.fetchData(serverURL, segments);
		return dasGFF.getGFF().getSegment();
	}
}
