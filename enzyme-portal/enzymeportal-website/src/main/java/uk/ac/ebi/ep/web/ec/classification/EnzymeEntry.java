
package uk.ac.ebi.ep.web.ec.classification;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author joseph
 */
@Data
public class EnzymeEntry implements Serializable{
    private static final long serialVersionUID = 1L;
     private String ec;
    private String name;
    private String description;
    

    
}
