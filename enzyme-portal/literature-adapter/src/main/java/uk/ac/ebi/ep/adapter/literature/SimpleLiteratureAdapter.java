package uk.ac.ebi.ep.adapter.literature;

import java.util.*;
import java.util.concurrent.*;

import org.apache.log4j.Logger;
import uk.ac.ebi.cdb.webservice.Result;
import uk.ac.ebi.ep.adapter.das.IDASFeaturesAdapter;

public class SimpleLiteratureAdapter implements ILiteratureAdapter {
	
	private static final Logger LOGGER =
			Logger.getLogger(SimpleLiteratureAdapter.class);

    private LiteratureConfig config;

    /**
     * Retrieves citations either from CiteXplore or the individual resources,
     * depending on the
     * {@link uk.ac.ebi.ep.adapter.literature.LiteratureConfig#isUseCitexploreWs()
     * configuration setting}.
     * @param uniprotId the UniProt identifier (accession or name) to search.
     *      This is the only parameter required when using the CiteXplore web
     *      service.
     * @param pdbIds the PDB identifiers to search.
     * @return a list of citations sorted by descending publication date.
     */
	public List<LabelledCitation> getCitations(String uniprotId,
			List<String> pdbIds) {
		List<LabelledCitation> citations = null;
		if (config.isUseCitexploreWs()){
            citations = getCitationsFromCitexplore(uniprotId);
        } else {
            citations = getCitationsFromResources(uniprotId, pdbIds);
        }
		if (citations != null){
            Collections.sort(citations, Collections.reverseOrder());
        }
		return citations;
	}

    /**
     * Get all of the citations from CiteXplore. Please note that the number of
     * citations is limited by a {@link LiteratureConfig#setMaxCitations(int)
     * configuration setting}.
     * @param uniprotId the UniProt accession of the enzyme.
     * @return a list of citations related to the enzyme, or <code>null</code>
     *      if none found.
     */
    private List<LabelledCitation> getCitationsFromCitexplore(String uniprotId){
        List<LabelledCitation> citations = null;
        try {
            CitexploreLiteratureCaller citexploreClient =
                    new CitexploreLiteratureCaller("UNIPROT_PUBS:" + uniprotId,
                            config.getCitexploreConnectTimeout(),
                            config.getCitexploreReadTimeout(),
                            config.getMaxCitations());
            Collection<Result> cits = citexploreClient.searchCitations();
            if (cits != null) for (Result cit : cits) {
                EnumSet<CitationLabel> labels = getLabels(cit);
                LabelledCitation citation = new LabelledCitation(cit, labels);
                if (citations == null){
                    citations = new ArrayList<LabelledCitation>();
                }
                if (citations.contains(citation)){
                    citations.get(citations.indexOf(citation))
                            .addLabels(labels);
                } else {
                    citations.add(citation);
                }
            }
        } catch (Exception e){
            LOGGER.error("Unable to get citations from CiteXplore", e);
        }
        return citations;
    }

    /**
     * Get citations from individual resources, currently:
     * <ul>
     *  <li>UniProt (for enzyme tab)</li>
     *  <li>PDB (for structure tab)</li>
     * </ul>
     * @param uniprotId the UniProt accession of the enzyme.
     * @param pdbIds the PDB IDs for the enzyme.
     * @return a list of citations related to the provided IDs.
     */
    private List<LabelledCitation> getCitationsFromResources(String uniprotId,
            List<String> pdbIds) {
        List<LabelledCitation> citations = null;
        LOGGER.debug("Before getting lit. callables");
        List<Callable<Set<Result>>> callables = getCallables(uniprotId, pdbIds);
        List<Future<Set<Result>>> futures = new ArrayList<Future<Set<Result>>>();
        LOGGER.debug("Before creating thread pool");

        ExecutorService pool =
                Executors.newFixedThreadPool(config.getMaxThreads());
        CompletionService<Set<Result>> ecs =
                new ExecutorCompletionService<Set<Result>>(pool);
        try {
            for (Callable<Set<Result>> callable : callables) {
                futures.add(ecs.submit(callable));
            }
            for (int i = 0; i < callables.size(); i++){
                Future<Set<Result>> future = null;
                try {
                    future = ecs.take();
                    if (future == null){
                        LOGGER.error("No citations retrieved for...");
                    } else {
                        Set<Result> cits = future.get();
                        if (cits != null){
                            CitationLabel label = getLabel(
                                    callables.get(futures.indexOf(future)));
                            for (Result cit : cits) {
                                LabelledCitation citation =
                                        new LabelledCitation(cit, label);
                                if (citations == null){
                                    citations = new ArrayList<LabelledCitation>();
                                } else if (citations.contains(citation)){
                                    citations.get(citations.indexOf(citation))
                                            .addLabel(label);
                                } else {
                                    citations.add(citation);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Unable to send requests for citations", e);
                }
            }
        } finally {
            pool.shutdown();
        }
        LOGGER.debug("After getting citations");
        return citations;
    }

    /**
     * Get independent threads to get literature from UniProt and PDB.
     * @param uniprotId the UniProt identifier to search.
     * @param pdbIds the PDB IDs to search.
     * @return a list of callables to have a parallel search.
     */
	private List<Callable<Set<Result>>> getCallables(String uniprotId,
			@SuppressWarnings("unused") List<String> pdbIds) {
		List<Callable<Set<Result>>> callables =
				new ArrayList<Callable<Set<Result>>>();
		callables.add(new UniprotJapiLiteratureCaller(uniprotId));
		callables.add(new DASLiteratureCaller(
				IDASFeaturesAdapter.PDBE_DAS_URL, uniprotId));
		return callables;
	}

    /**
     * Chooses the proper label according to the object used to retrieve the
     * citation. Ex. {@link CitationLabel.PROTEIN_STRUCTURE} for citations
     * retrieved via a PDB DAS server.
     * @param callable the object used to retrieve the citation.
     * @return the appropriate label for the citation.
     */
	private CitationLabel getLabel(Callable<Set<Result>> callable){
		CitationLabel label = null;
		if (callable.getClass().equals(UniprotJapiLiteratureCaller.class)){
			label = CitationLabel.ENZYME;
		} else if (callable.getClass().equals(DASLiteratureCaller.class)){
			label = CitationLabel.PROTEIN_STRUCTURE;
		}
		return label;
	}

    /**
     * Chooses the proper labels for a citation according to the database
     * cross-references it has got.
     * @param citation a citation from CiteXplore.
     * @return a set of labels appropriate for the citation.
     */
    private EnumSet<CitationLabel> getLabels(Result citation) {
        EnumSet<CitationLabel> labels = EnumSet.noneOf(CitationLabel.class);
        for (String dbName : citation.getDbCrossReferenceList().getDbName()){
            CitationLabel label = CitationLabel.forDatabase(dbName);
            if (label != null) labels.add(label);
        }
        return labels;
    }

	public void setConfig(LiteratureConfig config) {
		this.config = config;
	}

}
