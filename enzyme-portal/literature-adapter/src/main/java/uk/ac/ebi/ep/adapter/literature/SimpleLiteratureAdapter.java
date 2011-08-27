package uk.ac.ebi.ep.adapter.literature;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import uk.ac.ebi.cdb.webservice.Citation;
import uk.ac.ebi.ep.adapter.das.IDASFeaturesAdapter;

public class SimpleLiteratureAdapter implements ILiteratureAdapter {
	
	private static final Logger LOGGER = Logger.getLogger(SimpleLiteratureAdapter.class);
	
	public enum CitationLabel { // FIXME take strings from common properties file
		enzyme("Enzyme"),
		proteinStructure("Protein structure");
		private String label;
		private CitationLabel(String label){
			this.label = label;
		}
		public String toString(){ return label; }
	}
	
	/**
	 * Wrapper around a CiteXplore citation which implements
	 * {@link #equals(Object)} and {@link #hashCode()} methods, and
	 * labels it according to the source it came from.
	 * @author rafa
	 */
	public class LabelledCitation {
		private Citation citation;
		private EnumSet<CitationLabel> labels;
		public LabelledCitation(Citation citation, CitationLabel label){
			this.citation = citation;
			this.labels = EnumSet.of(label);
		}
		public Citation getCitation() {
			return citation;
		}
		public EnumSet<CitationLabel> getLabel() {
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
			return other.citation.getDataSource().equals(this.citation.getDataSource())
					&& other.citation.getExternalId().equals(this.citation.getExternalId());
		}
		@Override
		public int hashCode() {
			int hash = 17;
			hash = hash * 17 + citation.getDataSource().hashCode();
			hash = hash * 17 + citation.getExternalId().hashCode();
			return hash;
		}
		
	}
	
	private ExecutorService threadPool = Executors.newCachedThreadPool();
	
	// Not used, it does not find UniProt IDs easily
	//private CitexploreLiteratureCaller citexploreCaller;
	
	public List<LabelledCitation> getCitations(String uniprotId) {
		List<LabelledCitation> citations = null;
		List<Callable<Set<Citation>>> callables = getCallables(uniprotId);
		List<Future<Set<Citation>>> futures = null;
		try {
			futures = threadPool.invokeAll(callables);
			// Collect results:
			citations = new ArrayList<LabelledCitation>();
			for (Future<Set<Citation>> future : futures) {
				try {
					Set<Citation> cits = future.get();
					if (cits != null){
						CitationLabel label = getLabel(callables.get(futures.indexOf(future)));
						for (Citation cit : cits) {
							LabelledCitation citation = new LabelledCitation(cit, label);
							if (citations.contains(citation)){
								citations.get(citations.indexOf(citation)).addLabel(label);
							} else {
								citations.add(citation);
							}
						}
					}
				} catch (InterruptedException e) {
					LOGGER.error(getLabel(callables.get(futures.indexOf(future))), e);
				} catch (ExecutionException e) {
					LOGGER.error(getLabel(callables.get(futures.indexOf(future))), e);
				}
			}		
		} catch (InterruptedException e) {
			LOGGER.error("Unable to send requests for citations", e);
		} finally {
			List<Runnable> notExecuted = threadPool.shutdownNow();
			if (!notExecuted.isEmpty()){
				LOGGER.error(notExecuted.size() + " jobs not sent!");
			}
		}
//		futures = new ArrayList<Future<Set<Citation>>>();
//		for (Callable<Set<Citation>> callable : callables) {
//			futures.add(threadPool.submit(callable));
//		}
//		List<Future<Set<Citation>>> pending =
//				new ArrayList<Future<Set<Citation>>>(futures);
//		// Collect results:
//		Set<LabelledCitation> citations = new HashSet<LabelledCitation>();
//		while (!pending.isEmpty()){
//			for (Future<Set<Citation>> future : pending) {
//				if (future.isDone()){
//					try {
//						Set<Citation> cits = future.get();
//						CitationLabel label = getLabel(callables.get(futures.indexOf(future)));
//						for (Citation cit : cits) {
//							LabelledCitation citation = new LabelledCitation(cit, label);
//							if (citations.contains(citation)){
//								
//							} else {
//								
//							}
//							citations.add();
//						}
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (ExecutionException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} finally {
//						
//					}
//				}
//			}
//		}
		return citations;
	}

	private List<Callable<Set<Citation>>> getCallables(String uniprotId) {
		List<Callable<Set<Citation>>> callables =
				new ArrayList<Callable<Set<Citation>>>();
		callables.add(new UniprotJapiLiteratureCaller(uniprotId));
		callables.add(new DASLiteratureCaller(IDASFeaturesAdapter.PDBE_DAS_URL, uniprotId));
		return callables;
	}
	
	/* FIXME: odd job! */
	private CitationLabel getLabel(Callable<Set<Citation>> callable){
		CitationLabel label = null;
		if (callable.getClass().equals(UniprotJapiLiteratureCaller.class)){
			label = CitationLabel.enzyme;
		} else if (callable.getClass().equals(DASLiteratureCaller.class)){
			label = CitationLabel.proteinStructure;
		}
		return label;
	}

	@Override
	protected void finalize() throws Throwable {
		if (threadPool != null){
			threadPool.shutdownNow();
			if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)){
				LOGGER.error("Thread pool did not terminate");
			}
		}
	}

}
