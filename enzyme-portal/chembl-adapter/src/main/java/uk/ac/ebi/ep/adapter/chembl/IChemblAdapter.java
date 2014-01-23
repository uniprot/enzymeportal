package uk.ac.ebi.ep.adapter.chembl;

import java.util.List;

/**
 * @author rafa
 * @since 2013-06-13
 */
public interface IChemblAdapter {


    /**
     * Gets bioactivities for a given target.
     * @param targetId the ChEMBL ID of the target.
     * @return the reported bioactivities for the target.
     * @throws ChemblAdapterException
     */
    public ChemblBioactivities getTargetBioactivities(String targetId)
    throws ChemblAdapterException;

    /**
     * Gets bioactivities for a given compound.
     * @param compoundId the ChEMBL ID of the compound.
     * @return the reported bioactivities for the compound.
     * @throws ChemblAdapterException
     */
    public ChemblBioactivities getCompoundBioactivities(String compoundId)
    throws ChemblAdapterException;

    /**
     * Gets the preferred name for a given compound, if available.
     * @param compoundId the ChEMBL ID of the compound.
     * @return a name, or <code>null</code> if none available.
     * @throws ChemblAdapterException
     */
    public String getPreferredName(String compoundId)
    throws ChemblAdapterException;
    
    /**
     * Gets the existing ChEMBL targets for a given UniProt accession.
     * @param uniprotAcc the UniProt accession.
     * @return a list of ChEMBL target IDs.
     * @throws ChemblAdapterException
     * @since 1.0.2
     */
    public List<String> getTargets(String uniprotAcc)
    throws ChemblAdapterException;

    ChemblConfig getConfig();

    /**
     * Changes the configuration.
     * @param config the new configuration
     */
    void setConfig(ChemblConfig config);
}
