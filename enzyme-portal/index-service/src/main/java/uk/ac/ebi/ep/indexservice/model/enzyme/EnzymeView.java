package uk.ac.ebi.ep.ebeye.model.enzyme;

import java.util.List;
import java.util.Set;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */

public interface EnzymeView {

  
    String getEc();

    String getEnzymeFamily();

    List<String> getCatalyticActivities();

    Set<String> getIntenzCofactors();

    Set<String> getAltNames();

}
