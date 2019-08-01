
package uk.ac.ebi.ep.xml.dao;

import lombok.Data;

/**
 *
 * @author joseph
 */
@Data
public class ProteinGroup {
    

    private String proteinName;

    private String proteinGroupId;

    private Integer entryType;
    

    //private Set<UniprotEntry> uniprotEntrySet;// = Collections.emptySet();

    //private PrimaryProtein primaryProtein;
}
