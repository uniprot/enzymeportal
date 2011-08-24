package uk.ac.ebi.ep.adapter.literature;

import uk.ac.ebi.cdb.webservice.Citation;

public interface ILiteratureAdapter {

	public Citation getCitations(String uniprotId);
}
