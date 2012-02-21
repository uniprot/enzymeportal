package uk.ac.ebi.ep.adapter.das;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import uk.ac.ebi.das.jdas.adapters.features.DasGFFAdapter.SegmentAdapter;

public class SimpleDASFeaturesAdapter implements IDASFeaturesAdapter {
	
	private final Logger LOGGER =
			Logger.getLogger(SimpleDASFeaturesAdapter.class);
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
			/* Check that the URL exists and is valid:
			URL url = new URL(dasURL);
			URLConnection con = url.openConnection();
			con.connect();
			con.getContent();
			// end of checks
			*/
			this.dasURL = dasURL;
		}
	}
	
	public SegmentAdapter getSegment(String segmentId)
	throws MalformedURLException, JAXBException, Exception {
		SimpleDASFeaturesCaller callable =
				new SimpleDASFeaturesCaller(dasURL, segmentId);
		List<SegmentAdapter> adapters = callable.getSegments();
		return adapters == null || adapters.size() == 0?
				null :
				adapters.get(0);
	}

	public Collection<SegmentAdapter> getSegments(List<String> segmentIds)
	throws MalformedURLException, JAXBException {
		// jDAS allows sending the whole list, but the response time is
		// proportional to the size of the list, so we use threads:
		ExecutorService pool = Executors.newCachedThreadPool();
		CompletionService<SegmentAdapter> ecs =
				new ExecutorCompletionService<SegmentAdapter>(pool);
		Map<Future<SegmentAdapter>, SegmentAdapter> future2segment =
				new LinkedHashMap<Future<SegmentAdapter>, SegmentAdapter>();
		try {
			for (String segmentId : segmentIds) {
				Callable<SegmentAdapter> callable = new SimpleDASFeaturesCaller(
						IDASFeaturesAdapter.PDBE_DAS_URL, segmentId);
				future2segment.put(ecs.submit(callable), null);
			}
			for (int i = 0; i < segmentIds.size(); i++) {
				try {
					Future<SegmentAdapter> future = ecs.take(); // TODO: add timeout, from config
					if (future != null){
						future2segment.put(future, future.get());
					} else {
						LOGGER.warn("STRUCTURES job result not retrieved!");
						future2segment.remove(future);
					}
				} catch (Exception e){
	            	// Don't stop the others
	            	LOGGER.error("Callable " + (i+1) + " of " + segmentIds.size()
	            			+ " - " + e.getMessage(), e);
				}
			}
			return new ArrayList<SegmentAdapter>(future2segment.values());
	    } finally {
	    	pool.shutdown();
		}
	}

}
