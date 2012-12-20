package uk.ac.ebi.ep.adapter.uniprot;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.ebi.ep.search.model.Species;

/**
 * Callable to get the UniProt IDs and species matching a query.
 * It also provides a method to get only the IDs.
 * @author rafa
 *
 */
public class UniprotWsSearchIdsCallable
extends UniprotWsSearchCallable<Map<String,Species>> {

	public UniprotWsSearchIdsCallable(String query, UniprotConfig config) {
		super(query, config);
	}

	private class UniprotWsSearchIdsAndSpeciesProcessor
	implements IUniprotWsProcessor<Map<String, Species>, BufferedReader> {
	
		public final String getFields() {
			return "entry+name,organism";
		}
	
		public Map<String, Species> process(BufferedReader reader)
		throws IOException {
			Map<String, Species> idsSpecies = null;
			String line = null;
			while ((line = reader.readLine()) != null){
				if (idsSpecies == null){
					idsSpecies = new HashMap<String, Species>();
					continue; // first line is a header
				}
				String[] split = line.split("\t", -1);
				idsSpecies.put(split[0], UniprotWsAdapter.parseSpecies(split[1]));
			}
			return idsSpecies;
		}
	}

	private class UniprotWsSearchIdsProcessor
	implements IUniprotWsProcessor<List<String>, BufferedReader> {
	
		public final String getFields() {
			return "entry+name";
		}
	
		public List<String> process(BufferedReader reader) throws IOException {
			List<String> ids = null;
			String line = null;
			while ((line = reader.readLine()) != null){
				if (ids == null){
					ids = new ArrayList<String>();
					continue; // first line is a header
				}
				ids.add(line);
			}
			return ids;
		}
	}

	/**
	 * Gets the UniProt IDs matching the query, along with the corresponding
	 * species.
	 * @return A Map of UniProt ID to species (scientific name), or
	 * 		<code>null</code> if none found.
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, Species> getIdsAndSpecies() {
		IUniprotWsProcessor<Map<String, Species>, BufferedReader> processor =
				new UniprotWsSearchIdsAndSpeciesProcessor();
		return (Map<String, Species>) get(processor);
	}

	/**
	 * Retrieves UniProt IDs for enzymes matching the query.
	 * @return A list of UniProt IDs (entry names), or
	 * 		<code>null</code> if none found.
	 */
	@SuppressWarnings("unchecked")
	protected List<String> getIds(){
		IUniprotWsProcessor<List<String>, BufferedReader> processor =
				new UniprotWsSearchIdsProcessor();
		return (List<String>) get(processor);
	}

	public Map<String, Species> call() throws Exception {
		return getIdsAndSpecies();
	}

}
