package uk.ac.ebi.ep.adapter.literature;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import uk.ac.ebi.biobabel.citations.CitexploreWSClient;
import uk.ac.ebi.biobabel.citations.DataSource;
import uk.ac.ebi.cdb.webservice.Result;
import uk.ac.ebi.cdb.webservice.Journal;
import uk.ac.ebi.cdb.webservice.JournalInfo;
import uk.ac.ebi.das.jdas.adapters.features.DasGFFAdapter.SegmentAdapter;
import uk.ac.ebi.das.jdas.adapters.features.FeatureAdapter;
import uk.ac.ebi.das.jdas.exceptions.ValidationException;
import uk.ac.ebi.ep.adapter.das.SimpleDASFeaturesCaller;

/**
 * Class to get literature from DAS servers, based on UniProt IDs.
 * <br>
 * <b>NOTE:</b> the citations will be retrieved from CiteXplore by PubMed ID.
 * In case of not having a PubMed ID or if there is any problem with CiteXplore,
 * they won't contain information about authors nor abstract, as these are not
 * provided by DAS GFF.
 * @author rafa
 *
 */
public class DASLiteratureCaller implements Callable<Set<Result>> {

	private static Logger LOGGER = Logger.getLogger(DASLiteratureCaller.class);
	
	private SimpleDASFeaturesCaller featuresCaller;
	
	public DASLiteratureCaller(String serverURL, String segment){
		featuresCaller = new SimpleDASFeaturesCaller(serverURL, segment);
	}
	
	public DASLiteratureCaller(String serverURL, List<String> segments){
		featuresCaller = new SimpleDASFeaturesCaller(serverURL, segments);
	}
	
	public Set<Result> call() throws Exception {
		return getLiterature(featuresCaller.getSegments());
	}

	/**
	 * Extracts literature information from the segments returned by DAS.
	 * @param segments
	 * @return
	 */
	protected Set<Result> getLiterature(List<SegmentAdapter> segments) {
		Set<Result> citations = new HashSet<Result>();
		segmentsLoop: for (SegmentAdapter segment : segments) {
			try {
				for (FeatureAdapter feature : segment.getFeature()) {
					if (feature.getType().getId().equals("summary")
							&& feature.getLabel().equals("Primary Citation")){
						if (feature.getLinks().size() == 0){
							LOGGER.warn("Citation is not available for " + segment.getId());
							continue segmentsLoop;
						}
						Result citation = null;
						String pubMedId = getPubMedId(feature.getLinks().get(0).getHref());
						if (pubMedId != null && pubMedId.length() > 0){
							// Try to get from CiteXplore with the PubMed ID:
							citation = getCitationFromCitexplore(pubMedId);
						} else {
							LOGGER.warn("No PubMed ID found for " + segment.getId());
						}
						if (citation == null){
							// Build a citation with data from DAS:
							//citation = buildCitation(feature, pubMedId);
						} else {
							citations.add(citation);
						}
					}
				}
				if (citations == null){
					LOGGER.warn("No citations retrieved for " +  segment.getId());
				}
			} catch (ValidationException e) {
				LOGGER.error("Unable to get citations from DAS for " + segment.getId(), e);
			}
		}
		return citations;
	}

	private Result buildCitation(FeatureAdapter feature, String pubMedId) {
		Result citation;
		citation = new Result();
		JournalInfo issue = new JournalInfo();
		Journal journal = new Journal();
		issue.setJournal(journal);
		citation.setJournalInfo(issue);
		citation.setTitle(feature.getNotes().get(0));
		parseJournal(feature.getNotes().get(1), citation);
		//parseAuthors(feature.getNotes().get(2), citation);
		citation.setSource(DataSource.MED.name());
		citation.setId(pubMedId);
		return citation;
	}

	protected Result getCitationFromCitexplore(String pubMedId) {
		Result citation = null;
		CitexploreWSClient citexploreClient = null;
		try {
			citexploreClient =
					CitexploreWSClientPool.getInstance().borrowObject();
			citation = citexploreClient.retrieveCitation(DataSource.MED, pubMedId);
		} catch (Exception e) {
			LOGGER.error("Unable to get citation from CiteXplore", e);
		} finally {
			if (citexploreClient != null){
				try {
					CitexploreWSClientPool.getInstance().returnObject(citexploreClient);
				} catch (Exception e) {
					LOGGER.error("Unable to return CiteXplore client", e);
				}
			}
		}
		return citation;
	}

	/**
	 * Extracts the PubMed ID from the URL returned by DAS.
	 * @param url
	 * @return a PubMed ID
	 */
	protected String getPubMedId(String url) {
		final String pubMedParam = "Citation_pubmed_id=";
		int index = url.lastIndexOf(pubMedParam);
		return index > -1?
				url.substring(index + pubMedParam.length()):
				null;
	}

	/* No authors from DAS' GFF!!
	protected void parseAuthors(String authors, Result citation){
		for (String sAuthor : authors.split(",")) {
			Author author = new Author();
			author.setFullName(sAuthor);
			citation.getAuthorCollection().add(author);
		}
	}
	*/

	/**
	 * Populates a Result with journal data from a string.
	 * @param journal String with the standard notation of the publication,
	 * 		which comes from DAS as
	 * 		<code>[journal] vol:[volume] page:[pages] ([year])</code>
	 * @param citation A CiteXplore citation.
	 */
	protected void parseJournal(String journal, Result citation) {
		Pattern p = Pattern.compile("(.+) vol:(\\d+) page:(\\d+-\\d+) \\((\\d{4})\\)");
		Matcher m = p.matcher(journal);
		if (m.matches()){
			citation.getJournalInfo().getJournal().setTitle(m.group(1));
			citation.getJournalInfo().setVolume(m.group(2));
			citation.setPageInfo(m.group(3));
			citation.getJournalInfo().setYearOfPublication(
					Short.valueOf(m.group(4)));
		} else {
			LOGGER.error("Format not recognised for DAS citation: " + journal);
		}
	}

}
