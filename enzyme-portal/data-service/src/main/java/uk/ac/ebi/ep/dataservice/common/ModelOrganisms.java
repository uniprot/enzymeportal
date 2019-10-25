
package uk.ac.ebi.ep.data.common;

import java.util.LinkedList;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public enum ModelOrganisms {

    //Human,Mouse, Mouse-ear cress, fruit fly, yeast, e.coli,Rat,//worm
    HUMAN(9606),
    MOUSE(10090),
    MOUSE_EAR_CRESS(3702),
    FRUIT_FLY(7227),
    BAKER_YEAST(4932),
    ECOLI(83333),
    RAT(10116);

    private static final List<Long> taxIds = new LinkedList<>();

    static {
        for (ModelOrganisms cs : ModelOrganisms.values()) {
            taxIds.add(cs.taxId);
        }
    }

    private long taxId;

    private ModelOrganisms(@NotNull long taxId) {
        this.taxId = taxId;
    }

    public long getTaxId() {
        return taxId;
    }

    public static List<Long> getTaxIds() {
        return taxIds;
    }

}
