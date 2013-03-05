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
     * @deprecated use the generic method {@link #getDisease(java.lang.String)}
     *      instead, as this one can search also by ID (misleading method name).
	 */
    @Deprecated
	public Disease getDiseaseByName(String name)
    throws BioportalAdapterException;

    /**
     * Retrieves a Disease from BioPortal.
     * @param nameOrId the name or ID of a disease.
     * @return a Disease object whose name or ID matches exactly the parameter,
     *      or <code>null</code> if not found.
     * @throws BioportalAdapterException 
     */
	public Disease getDisease(String nameOrId)
    throws BioportalAdapterException;
    
}
