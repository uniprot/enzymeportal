package uk.ac.ebi.ep.adapter.literature;

import java.util.List;

public interface ILiteratureAdapter {

	public List<LabelledCitation> getCitations(String uniprotId,
			List<String> pdbIds);
	
	public void setConfig(LiteratureConfig config);
}
