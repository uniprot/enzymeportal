package uk.ac.ebi.ep.enzymeservice.uniprot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
/**
 *
 * @author joseph
 */
@Data
public class Comment {

    @JsonProperty("type")
    private String type;
    @JsonProperty("kinetics")
    private Kinetics kinetics;
    @JsonProperty("phDependence")
    private List<PhDependence> phDependence = new ArrayList<>();
    @JsonProperty("temperatureDependence")
    private List<TemperatureDependence> temperatureDependence = new ArrayList<>();
    

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }


    @JsonProperty("kinetics")
    public Kinetics getKinetics() {
        return kinetics;
    }

    @JsonProperty("kinetics")
    public void setKinetics(Kinetics kinetics) {
        this.kinetics = kinetics;
    }

    @JsonProperty("phDependence")
    public List<PhDependence> getPhDependence() {
        return phDependence;
    }

    @JsonProperty("phDependence")
    public void setPhDependence(List<PhDependence> phDependence) {
        this.phDependence = phDependence;
    }

    @JsonProperty("temperatureDependence")
    public List<TemperatureDependence> getTemperatureDependence() {
        return temperatureDependence;
    }

    @JsonProperty("temperatureDependence")
    public void setTemperatureDependence(List<TemperatureDependence> temperatureDependence) {
        this.temperatureDependence = temperatureDependence;
    }


}
