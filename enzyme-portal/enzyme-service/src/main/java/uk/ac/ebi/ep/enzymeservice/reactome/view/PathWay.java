package uk.ac.ebi.ep.enzymeservice.reactome.view;

import lombok.Data;
import lombok.ToString;

/**
 *
 * @author joseph
 */
@Data
@ToString
public class PathWay {

    private String id;
    private String name;
    private String description;
    private String url;
    private String image;

}
