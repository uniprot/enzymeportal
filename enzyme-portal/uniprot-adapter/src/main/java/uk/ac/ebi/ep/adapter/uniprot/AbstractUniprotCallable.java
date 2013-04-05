package uk.ac.ebi.ep.adapter.uniprot;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.mm.MegaJdbcMapper;
import uk.ac.ebi.ep.mm.MegaMapper;
import uk.ac.ebi.ep.mm.MmDatabase;
import uk.ac.ebi.ep.mm.XRef;
import uk.ac.ebi.ep.search.model.EnzymeAccession;
import uk.ac.ebi.ep.search.model.EnzymeSummary;

/**
 * @author rafa
 * @since 2013-04-04
 */
public abstract class AbstractUniprotCallable
implements Callable<EnzymeSummary> {

    private static final Logger LOGGER =
            Logger.getLogger(AbstractUniprotCallable.class);

    protected Connection mmConnection;

    /**
     * A MegaMapper used to get correct cross-references to PDB.
     */
    protected MegaMapper mm;

    /**
     * Sets safely a database connection to the mega-map, used to clean PDB
     * xrefs from theoretical models coming from UniProt WS.
     * @param mmConnection a database connection (can be <code>null</code>, in
     *      which case no attempt will be made to use it).
     */
    protected void setMmConnection(Connection mmConnection){
        if (mmConnection != null) try {
            this.mmConnection = mmConnection;
            this.mm = new MegaJdbcMapper(mmConnection);
        } catch (IOException e) {
            LOGGER.error("Unable to create a MegaMapper", e);
        }
    }

    /**
     * Fixes any cross references to PDB by querying the mega-map, which only
     * contains PDB entries from empirical methods, no theoretical models.<br/>
     * Note that this method will do nothing if no MegaMapper is set for
     * this UniprotCallable.
     * @param summary a summary to fix.
     * @return the same summary, fixed.
     */
    protected EnzymeModel fixPdbXrefs(EnzymeModel summary) {
        if (mm != null){
            // PDB xrefs for every ortholog in the summary:
            for (EnzymeAccession relSp : summary.getRelatedspecies()) {
                Collection<XRef> pdbXrefs = mm.getXrefs(MmDatabase.UniProt,
                        relSp.getUniprotaccessions().get(0), MmDatabase.PDB);
                if (pdbXrefs != null){
                    relSp.setPdbeaccession(getToIds(pdbXrefs));
                }
            }
            // TODO: fix also the default species?
        }
        return summary;
    }

    /**
     * Converts a collection of objects (cross-references) into a plain list of
     * entry IDs.
     * @param xrefs a collection of cross-references.
     * @return a list of entry IDs.
     */
    private List<String> getToIds(Collection<XRef> xrefs){
        List<String> toIds = new ArrayList<String>();
        for (XRef xRef : xrefs) {
            toIds.add(xRef.getToEntry().getEntryId());
        }
        return toIds;
    }

}
