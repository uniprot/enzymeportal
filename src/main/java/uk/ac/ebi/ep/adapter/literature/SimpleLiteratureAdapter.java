package uk.ac.ebi.ep.adapter.literature;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import uk.ac.ebi.cdb.webservice.Result;
import uk.ac.ebi.ep.adapter.das.IDASFeaturesAdapter;

public class SimpleLiteratureAdapter implements ILiteratureAdapter {
	
	private static final Logger LOGGER =
			Logger.getLogger(SimpleLiteratureAdapter.class);
	
	public enum CitationLabel { // FIXME take strings from common properties file
		enzyme("Enzyme"),
		proteinStructure("Protein structure");
		private String label;
		private CitationLabel(String label){
			this.label = label;
		}
		public String toString(){ return label; }
		public String getCode(){ return name(); }
		public String getDisplayText(){ return label; }
	}
	
	/**
	 * Wrapper around a CiteXplore citation which implements
	 * {@link #equals(Object)} and {@link #hashCode()} methods, and
	 * labels it according to the source it came from.
	 * @author rafa
	 */
	public class LabelledCitation {
		private Result citation;
		private EnumSet<CitationLabel> labels;
		public LabelledCitation(Result citation, CitationLabel label){
			this.citation = citation;
			this.labels = EnumSet.of(label);
		}
		public Result getCitation() {
			return citation;
		}
		public EnumSet<CitationLabel> getLabels() {
			return labels;
		}
		public void addLabel(CitationLabel label) {
			this.labels.add(label);
		}
		/**
		 * Equals method based on bibliography identifier, namely PubMed ID.
		 * It does not take labels into account.
		 */
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof LabelledCitation)) return false;
			if (o == this) return true;
			LabelledCitation other = (LabelledCitation) o;
			String tcei = this.citation.getId();
			String ocei = other.citation.getId();
			if (tcei == null ^ ocei == null) return false;
			if (tcei != null && ocei != null){
				String tcds = this.citation.getSource();
				String ocds = other.citation.getSource();
				return tcei.equals(ocei) && tcds.equals(ocds);
			}
			return this.citation.getTitle().equals(other.citation.getTitle());
		}
		@Override
		public int hashCode() {
			int hash = 17;
			hash = hash * 17;
			if (citation.getSource() != null){
				hash += citation.getSource().hashCode();
			}
			hash = hash * 17;
			if (citation.getId() != null){
				hash += citation.getId().hashCode();
			}
			hash = hash * 17 + citation.getTitle().hashCode();
			return hash;
		}
		
	}

	private LiteratureConfig config;
	
	public List<LabelledCitation> getCitations(String uniprotId, List<String> pdbIds) {
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

	private List<Callable<Set<Result>>> getCallables(String uniprotId,
			List<String> pdbIds) {
		List<Callable<Set<Result>>> callables =
				new ArrayList<Callable<Set<Result>>>();
		callables.add(new UniprotJapiLiteratureCaller(uniprotId));
		for (String pdbId : pdbIds) {
			callables.add(new DASLiteratureCaller(
					IDASFeaturesAdapter.PDBE_DAS_URL, pdbId));
		}
		return callables;
	}
	
	/* FIXME: odd job! */
	private CitationLabel getLabel(Callable<Set<Result>> callable){
		CitationLabel label = null;
		if (callable.getClass().equals(UniprotJapiLiteratureCaller.class)){
			label = CitationLabel.enzyme;
		} else if (callable.getClass().equals(DASLiteratureCaller.class)){
			label = CitationLabel.proteinStructure;
		}
		return label;
	}

	public void setConfig(LiteratureConfig config) {
		this.config = config;
	}

}
