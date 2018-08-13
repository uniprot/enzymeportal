package uk.ac.ebi.ep.pdbeadapter;

import java.util.ArrayList;
import java.util.List;
import uk.ac.ebi.ep.pdbeadapter.molecule.Molecule;

/**
 *
 * @author joseph
 */
public class SmallMoleculeLigand {

    private String chainId;
    private String shortName;
    private String label;
    private List<Molecule> molecules;

    public String getChainId() {
        return chainId.replaceAll("\\[", "").replaceAll("]", "");
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Molecule> getMolecules() {
        if (molecules == null) {
            molecules = new ArrayList<>();
        }
        return molecules;
    }

    public void setMolecules(List<Molecule> molecules) {
        this.molecules = molecules;
    }

    @Override
    public String toString() {
        return "SmallMoleculeLigand{" + "shortName=" + shortName + ", label=" + label + ", molecules=" + molecules + '}';
    }

}
