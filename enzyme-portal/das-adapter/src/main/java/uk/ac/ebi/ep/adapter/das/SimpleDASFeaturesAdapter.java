package uk.ac.ebi.ep.adapter.das;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBException;

import uk.ac.ebi.das.jdas.adapters.features.DasGFFAdapter.SegmentAdapter;

public class SimpleDASFeaturesAdapter implements IDASFeaturesAdapter {
	
	private String dasURL;
	
	@Deprecated
	public SimpleDASFeaturesAdapter(){
		dasURL = IDASFeaturesAdapter.PDBE_DAS_URL;
	}
	
	public SimpleDASFeaturesAdapter(String dasUrl)
	throws MalformedURLException, IOException{
		setDasUrl(dasUrl);
	}
	
	/**
	 * Setter for the dasUrl field.
	 * @param dasURL
	 * @throws MalformedURLException if the URL is bad
	 * @throws IOException in case of problem connecting to that URL
	 */
	public void setDasUrl(String dasURL)
	throws MalformedURLException, IOException{
		if (dasURL == null){
			throw new NullPointerException("dasURL cannot be null");
		} else {
			URL url = new URL(dasURL);
			URLConnection con = url.openConnection();
			con.connect();
			con.getContent();
			// end of checks
			this.dasURL = dasURL;
		}
	}
	
	public SegmentAdapter getSegment(String segmentId)
	throws MalformedURLException, JAXBException {
		SimpleDASFeaturesCaller callable =
				new SimpleDASFeaturesCaller(dasURL, segmentId);
		List<SegmentAdapter> adapters = callable.getSegments();
		return adapters == null || adapters.size() == 0?
				null :
				adapters.get(0);
	}

	public Collection<SegmentAdapter> getSegments(List<String> segmentIds)
	throws MalformedURLException, JAXBException {
		// No need of multithreading, as we can request all of them at once:
		SimpleDASFeaturesCaller callable =
				new SimpleDASFeaturesCaller(dasURL, segmentIds);
		return callable.getSegments();
	}


}
