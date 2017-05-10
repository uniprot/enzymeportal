package uk.ac.ebi.ep.ebeye.model.proteinGroup;

import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Data
@Builder(builderMethodName = "proteinBuilder")
public class ProteinGroup  {

    private int hitCount;
    private String proteinGroupId;
    private String proteinName;
    private List<String> scientificName;
    

    
    
    
}
