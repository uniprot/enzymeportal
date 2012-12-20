package uk.ac.ebi.ep.adapter.bioportal;

import uk.ac.ebi.ep.enzyme.model.Disease;

/**
 * Proxy to get data from BioPortal.
 * @author rafa
 *
 */
public interface IBioportalAdapter {
	
	/**
	 * Retrieves a disease from EFO.
	 * @param name a disease name
	 * @return a Disease object whose name in EFO matches exactly the
	 * 		parameter, or <code>null</code> if not found.
	 * @throws BioportalAdapterException 
	 */
	public Disease getDiseaseByName(String name) throws BioportalAdapterException;

}
