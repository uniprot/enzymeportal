
package uk.ac.ebi.ep.xml.dao;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import uk.ac.ebi.ep.xml.schema.Field;
import uk.ac.ebi.ep.xml.schema.Ref;

/**
 *
 * @author joseph
 */
@Data
public class FieldAndRef {
    
    Set<Field> field = new HashSet<>();
    Set<Ref> refs = new HashSet<>();

    public FieldAndRef() {
    }
    
    
}
