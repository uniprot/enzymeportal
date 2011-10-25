package uk.ac.ebi.ep.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class EPUtil {

    /**
     * Extracts the prefix from a UniProt ID (i.e. strips the species suffix).
     * @param id a UniProt ID.
     * @return an ID without the species suffix.
     */
    public static String getIdPrefix(String id) {
    	return id.split("_")[0];
    }

    /**
     * Extracts the prefixes from UniProt IDs (i.e. strips the species suffixes).
     * @param ids a collection of UniProt IDs.
     * @return a list of distinct UniProt IDs without the species suffix.
     */
	public static List<String> getIdPrefixes(Collection<String> ids) {
		Set<String> prefixes = new LinkedHashSet<String>();
		for (String id : ids) {
			prefixes.add(getIdPrefix(id));
		}
		return new ArrayList<String>(prefixes);
	}

	/**
	 * Converts the UniProt IDs to prefixes with a wildcard (<code>_*</code>)
	 * at the end, which should match homologs.
	 * @param ids a collection of UniProt IDs, either complete (ex. ALR1_YEAST)
	 * 		or just prefixes without wildcard (ex. ALR1).
	 * @return a list of distinct UniProt IDs with the species suffix replaced
	 * 		with a wildcard.
	 */
	public static List<String> getWildcardIds(Collection<String> ids){
		Set<String> wIds = new LinkedHashSet<String>();
		for (String id : ids) {
			String idPrefix = id.indexOf('_') > -1? getIdPrefix(id) : id;
			wIds.add(idPrefix+"_*");
		}
		return new ArrayList<String>(wIds);
	}
}
