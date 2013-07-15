package uk.ac.ebi.ep.adapter.chembl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Bioactivities for a given ChEMBL ID (may be target or compound).
 * @author rafa
 * @since 1.0.0
 */
public class ChemblBioactivities {

    /**
     * Map of ChEMBL IDs to their bioactivities. If this object is for a
     * target/compound then the keys of this map are compound/target IDs
     * respectively.
     */
    private Map<String, ChemblBioactivity> m =
            new HashMap<String, ChemblBioactivity>();

    public void addBioactivity(String chemblId, int confidence,
            String assayType){
        if (!m.containsKey(chemblId)){
            m.put(chemblId, new ChemblBioactivity());
        }
        m.get(chemblId).addAssay();
        if (confidence > 4) m.get(chemblId).addConf4();
        if (confidence == 9) m.get(chemblId).addConf9();
        if ("F".equals(assayType)) m.get(chemblId).addFunct();
    }

    /**
     * Retrieves the map of ChEMBL IDs to their bioactivities
     * @return
     */
    public Map<String, ChemblBioactivity> getMap() {
        return m;
    }

    /**
     * Filters the bioactivities in order to keep only those matching minimum
     * requirements.
     * @param minAssays minimum number of assays performed.
     * @param minConf4 minimum fraction of assays with confidence 4 or higher.
     * @param minConf9 minimum fraction of assays with confidence 9.
     * @param minFunc minimum fraction of functional assays.
     * @return
     */
    public Collection<String> filter(int minAssays, double minConf4,
            double minConf9, double minFunc){
        Collection<String> filtered = new ArrayList<String>();
        for (String chemblId : m.keySet()) {
            ChemblBioactivity bioact = m.get(chemblId);
            if (bioact.getAssays() >= minAssays
                    && bioact.getConf4() >= minConf4
                    && bioact.getConf9() >= minConf9
                    && bioact.getFunct() >= minFunc){
                filtered.add(chemblId);
            }
        }
        return filtered;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[BIOACTIVITIES]\n");
        for (String chemblId : m.keySet()) {
            sb.append('\t').append(chemblId).append(": ")
                    .append(m.get(chemblId)).append('\n');
        }
        return sb.toString();
    }
}
