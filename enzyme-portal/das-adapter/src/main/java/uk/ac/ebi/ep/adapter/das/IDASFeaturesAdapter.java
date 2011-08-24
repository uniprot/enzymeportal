package uk.ac.ebi.ep.adapter.das;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Collection;

import javax.xml.bind.JAXBException;

import uk.ac.ebi.das.jdas.adapters.features.DasGFFAdapter.SegmentAdapter;

public interface IDASFeaturesAdapter {

	// FIXME: TAKE THIS TO A PROPERTIES FILE, JMX-MANAGEABLE
	String PDBE_DAS_URL = "http://www.ebi.ac.uk/das-srv/proteindas/das/pdbe_summary/features";

	public SegmentAdapter getSegment(String segmentId)
	throws MalformedURLException, JAXBException;
	
	public Collection<SegmentAdapter> getSegments(List<String> segmentIds)
	throws MalformedURLException, JAXBException;
}
