package uk.ac.ebi.ep.adapter.literature;

import java.util.List;

import uk.ac.ebi.ep.adapter.literature.SimpleLiteratureAdapter.LabelledCitation;

public interface ILiteratureAdapter {

	public List<LabelledCitation> getCitations(String uniprotId);
	
}
