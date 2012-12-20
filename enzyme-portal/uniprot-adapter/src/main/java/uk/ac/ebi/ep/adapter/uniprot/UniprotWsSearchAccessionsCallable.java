package uk.ac.ebi.ep.adapter.uniprot;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.ebi.ep.search.model.Species;

public class UniprotWsSearchAccessionsCallable
extends UniprotWsSearchCallable<Map<String, Species>> {

	public UniprotWsSearchAccessionsCallable(String query, UniprotConfig config) {
		super(query, config);
	}

	private class UniprotWsSearchAccessionsAndSpeciesProcessor
	implements IUniprotWsProcessor<Map<String, Species>, BufferedReader> {
	
		public final String getFields() {
			return "id,organism";
		}
	
		public Map<String, Species> process(BufferedReader reader)
		throws IOException {
			Map<String, Species> accsSpecies = null;
			String line = null;
			while ((line = reader.readLine()) != null){
				if (accsSpecies == null){
					accsSpecies = new HashMap<String, Species>();
					continue; // first line is a header
				}
				String[] split = line.split("\t", -1);
				accsSpecies.put(split[0], UniprotWsAdapter.parseSpecies(split[1]));
			}
			return accsSpecies;
		}
	}

	private class UniprotWsSearchAccessionsProcessor
	implements IUniprotWsProcessor<List<String>, BufferedReader> {
	
		public String getFields() {
			return "id";
		}
	
		public List<String> process(BufferedReader reader) throws IOException {
			List<String> accessions = null;
			String line = null;
			while ((line = reader.readLine()) != null){
				if (accessions == null){
					accessions = new ArrayList<String>();
					continue; // first line is a header
				}
				accessions.add(line);
			}
			return accessions;
		}
	}

	/**
	 * Retrieves UniProt accessions for enzymes matching the query.
	 * @return A list of UniProt accessions, or
	 * 		<code>null</code> if none found.
	 */
	@SuppressWarnings("unchecked")
	protected List<String> getAccessions() {
		IUniprotWsProcessor<List<String>, BufferedReader> processor =
				new UniprotWsSearchAccessionsProcessor();
		return (List<String>) get(processor);
	}
	
	/**
	 * Retrieves UniProt accessions for enzymes matching the query
	 * along with their corresponding species.
	 * @return A Map of UniProt accessions to Species, or
	 * 		<code>null</code> if none found.
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, Species> getAccessionsAndSpecies(){
		IUniprotWsProcessor<Map<String, Species>, BufferedReader> processor =
				new UniprotWsSearchAccessionsAndSpeciesProcessor();
		return (Map<String, Species>) get(processor);
	}

	public Map<String, Species> call() throws Exception {
		return getAccessionsAndSpecies();
	}

}
