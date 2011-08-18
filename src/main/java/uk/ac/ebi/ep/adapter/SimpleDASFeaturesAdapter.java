package uk.ac.ebi.ep.adapter;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import javax.xml.bind.JAXBException;

import uk.ac.ebi.das.jdas.adapters.features.DasGFFAdapter.SegmentAdapter;

public class SimpleDASFeaturesAdapter implements IDASFeaturesAdapter {

	public SegmentAdapter getFeatures(String segmentId)
	throws MalformedURLException, JAXBException {
		SimpleDASFeaturesCaller callable =
				new SimpleDASFeaturesCaller(IDASFeaturesAdapter.PDBE_DAS_URL, segmentId);
		List<SegmentAdapter> adapters = callable.getFeatures();
		return adapters == null || adapters.size() == 0?
				null :
				adapters.get(0);
	}

	public Collection<SegmentAdapter> getFeatures(List<String> segmentIds)
	throws MalformedURLException, JAXBException {
		// No need of multithreading, as we can request all of them at once:
		SimpleDASFeaturesCaller callable =
				new SimpleDASFeaturesCaller(IDASFeaturesAdapter.PDBE_DAS_URL, segmentIds);
		return callable.getFeatures();
	}


}
