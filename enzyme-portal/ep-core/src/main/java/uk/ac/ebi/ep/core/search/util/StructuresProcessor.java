package uk.ac.ebi.ep.core.search.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.ac.ebi.ep.mm.MegaMapper;
import uk.ac.ebi.ep.mm.MegaMapper.Constraint;
import uk.ac.ebi.ep.mm.MmDatabase;
import uk.ac.ebi.ep.mm.XRef;
import uk.ac.ebi.ep.search.model.EnzymeSummary;

/**
 * Processor for EnzymeSummaries which replaces the existing PDBe accessions -
 * coming from the UniProt web services, including theoretical models - with
 * those existing in the mega-map - including only experimental ones.
 * @author rafa
 *
 */
public class StructuresProcessor implements EnzymeSummaryProcessor {

	private MegaMapper mm;
	
	/**
	 * 
	 * @param mm the mega-mapper required to get (only experimental) PDB IDs.
	 */
	public StructuresProcessor(MegaMapper mm){
		this.mm = mm;
	}
	
	public void process(EnzymeSummary item) {
		String uId = item.getUniprotid();
		String uIdPrefix = uId.substring(0, uId.indexOf('_')+1);
		Collection<XRef> pdbXrefs = mm.getXrefs(MmDatabase.UniProt, uIdPrefix,
				Constraint.STARTS_WITH, MmDatabase.PDB);
		List<String> pdbeaccessions = new ArrayList<String>();
		if (pdbXrefs != null){
			for (XRef xRef : pdbXrefs) {
				pdbeaccessions.add(xRef.getToEntry().getEntryId());
			}
		}
		item.setPdbeaccession(pdbeaccessions );
	}

}
