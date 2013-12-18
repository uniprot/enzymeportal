package uk.ac.ebi.ep.adapter.literature;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import uk.ac.ebi.biobabel.citations.CitexploreWSClient;
import uk.ac.ebi.biobabel.citations.DataSource;
import uk.ac.ebi.cdb.webservice.*;
import uk.ac.ebi.das.jdas.adapters.features.DasGFFAdapter.SegmentAdapter;
import uk.ac.ebi.das.jdas.adapters.features.FeatureAdapter;
import uk.ac.ebi.das.jdas.exceptions.ValidationException;
import uk.ac.ebi.ep.adapter.das.SimpleDASFeaturesCaller;

/**
 * Class to get literature from DAS servers, based on UniProt IDs.
 * <br>
 * <b>NOTE:</b> the citations contain no abstract, and the list of authors
 * returned follows alphabetical order.
 * @author rafa
 *
 */
public class DASLiteratureCaller implements Callable<Set<Result>> {

	private static Logger LOGGER = Logger.getLogger(DASLiteratureCaller.class);
	
	private SimpleDASFeaturesCaller featuresCaller;

	private Integer connectTimeout;
	private Integer readTimeout;
	
	public DASLiteratureCaller(String serverURL, String segment){
		this(serverURL, segment, 0, 0);
	}
	
	public DASLiteratureCaller(String serverURL, List<String> segments){
		this(serverURL, segments, 0, 0);
	}

    /**
     * Complete constructor.
     * @param serverURL the DAS server URL.
     * @param segment the segment (UniProt accession) to get citations for.
     * @param connectTimeout the connection timeout for citeXplore web service.
     * @param readTimeout the read timeout for citeXplore web service.
     * @since 1.0.7
     */
    public DASLiteratureCaller(String serverURL, String segment,
            Integer connectTimeout, Integer readTimeout) {
        featuresCaller = new SimpleDASFeaturesCaller(serverURL, segment);
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }

    public DASLiteratureCaller(String serverURL, List<String> segments,
            Integer connectTimeout, Integer readTimeout) {
        featuresCaller = new SimpleDASFeaturesCaller(serverURL, segments);
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
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
		Map<String, FeatureAdapter> authorsFeatures =
		        new HashMap<String, FeatureAdapter>();
        Map<String, FeatureAdapter> primaryCitationFeatures =
                new HashMap<String, FeatureAdapter>();
        Map<String, String> pubMedIds = new HashMap<String, String>();
		for (SegmentAdapter segment : segments) {
			try {
				for (FeatureAdapter feature : segment.getFeature()) {
				    if (isDepositedBy(feature)){
                        String pdbId = feature.getId().replace("-authors", "");
                        authorsFeatures.put(pdbId, feature);
                    } else if (isPrimaryCitation(feature)){
                        String pdbId = feature.getId().replace("-citation", "");
                        primaryCitationFeatures.put(pdbId, feature);
                        String pubMedId = null;
						if (feature.getLinks().size() == 0){
							LOGGER.warn(feature.getId()
									+ ": link (PMID) not available");
						} else {
                            pubMedId = getPubMedId(
                                    feature.getLinks().get(0).getHref());
                        }
                        pubMedIds.put(pdbId, pubMedId);
					}
				}
				for (String pdbId : primaryCitationFeatures.keySet()){
                    final String pubMedId = pubMedIds.get(pdbId);
                    if (pubMedId == null) continue;
                    Result citation = buildCitation(
                            primaryCitationFeatures.get(pdbId),
                            authorsFeatures.get(pdbId),
                            pubMedId);
                    citations.add(citation);
                }
			} catch (ValidationException e) {
				LOGGER.error("Unable to get citations from DAS for "
						+ segment.getId(), e);
			}
		}
		return citations;
	}

    private boolean isPrimaryCitation(FeatureAdapter feature) {
        return feature.getType().getId().equals("summary")
                && feature.getLabel().equals("Primary Citation");
    }

    private boolean isDepositedBy(FeatureAdapter feature){
        return feature.getType().getId().equals("summary")
                && feature.getLabel().equals("Deposited by");
    }

    private Result buildCitation(FeatureAdapter citationsFeature,
            FeatureAdapter authorsFeature, String pubMedId) {
		Result citation = new Result();
		JournalInfo issue = new JournalInfo();
		Journal journal = new Journal();
		issue.setJournal(journal);
		citation.setJournalInfo(issue);
		citation.setTitle(citationsFeature.getNotes().get(0));
		parseJournal(citationsFeature.getNotes().get(1), citation);
		citation.setAuthorList(new AuthorsList());
		citation.getAuthorList().getAuthor().addAll(
                parseAuthors(authorsFeature.getNotes().get(0)));
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
			citexploreClient.setConnectTimeout(connectTimeout);
			citexploreClient.setReadTimeout(readTimeout);
			citation = citexploreClient.retrieveCitation(DataSource.MED,
			        pubMedId);
		} catch (Exception e) {
			LOGGER.error("Unable to get CiteXplore citation for " + pubMedId,
			        e);
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

	protected List<Authors> parseAuthors(String authors){
	    List<Authors> l = new ArrayList<Authors>();
		for (String sAuthor : authors.split("(?<=\\.), ")) {
            Authors author = new Authors();
            author.setFullName(sAuthor);
			l.add(author);
		}
		return l;
	}

	/**
	 * Populates a Result with journal data from a string.
	 * @param journal String with the standard notation of the publication,
	 * 		which comes from DAS as
	 * 		<code>[journal] vol:[volume] page:[pages] ([year])</code>
	 * @param citation A CiteXplore citation.
	 */
	protected void parseJournal(String journal, Result citation) {
		Pattern p = Pattern.compile("(.+?)(?: vol:(\\d+))?(?: page:(\\d+-\\d+))? \\((\\d{4})\\)");
		Matcher m = p.matcher(journal);
		if (m.matches()){
			citation.getJournalInfo().getJournal().setTitle(m.group(1));
			citation.getJournalInfo().setVolume(m.group(2));
			citation.setPageInfo(m.group(3));
            final String pubYear = m.group(4);
            citation.getJournalInfo().setYearOfPublication(
                    Short.valueOf(pubYear));
			citation.setPubYear(pubYear);
		} else {
			LOGGER.error("Format not recognised for DAS citation: " + journal);
		}
	}

}
