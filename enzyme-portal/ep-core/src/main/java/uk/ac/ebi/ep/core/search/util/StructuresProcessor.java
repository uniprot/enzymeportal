package uk.ac.ebi.ep.core.search.util;

import uk.ac.ebi.ep.mm.MegaMapper;
import uk.ac.ebi.ep.mm.MegaMapper.Constraint;
import uk.ac.ebi.ep.mm.MmDatabase;
import uk.ac.ebi.ep.mm.XRef;
import uk.ac.ebi.ep.search.model.EnzymeAccession;
import uk.ac.ebi.ep.search.model.EnzymeSummary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Processor for EnzymeSummaries which replaces the existing PDBe accessions -
 * coming from the UniProt web services, including theoretical models - with
 * those existing in the mega-map - including only experimental ones.
 * @author rafa
 * @deprecated processing of summaries after their retrieval is very inefficient
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
	
	public void process(EnzymeSummary summary) {
		String uId = summary.getUniprotid();
		String uIdPrefix = uId.substring(0, uId.indexOf('_') + 1);
		Collection<XRef> pdbXrefs = mm.getXrefs(MmDatabase.UniProt, uIdPrefix,
				Constraint.STARTS_WITH, MmDatabase.PDB);
		if (pdbXrefs != null) summary.setPdbeaccession(getToIds(pdbXrefs));

        for (EnzymeAccession relSp : summary.getRelatedspecies()) {
            pdbXrefs = mm.getXrefs(MmDatabase.UniProt,
                    relSp.getUniprotaccessions().get(0), MmDatabase.PDB);
            if (pdbXrefs != null) relSp.setPdbeaccession(getToIds(pdbXrefs));
        }
    }

    private List<String> getToIds(Collection<XRef> xrefs){
        List<String> toIds = new ArrayList<String>();
        for (XRef xRef : xrefs) {
            toIds.add(xRef.getToEntry().getEntryId());
        }
        return toIds;
    }
}
