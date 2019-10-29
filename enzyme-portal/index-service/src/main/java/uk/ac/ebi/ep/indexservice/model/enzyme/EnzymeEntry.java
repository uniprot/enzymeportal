package uk.ac.ebi.ep.indexservice.model.enzyme;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "source",
    "fields"
})
public class EnzymeEntry extends AbstractPageView implements EnzymeView {

    @JsonProperty("id")
    private String id;
    @JsonProperty("source")
    private String source;
    @JsonProperty("fields")
    private EnzymeFields fields;


    /**
     *
     * @param id ec number
     * @param fields
     */
    public EnzymeEntry(String id, EnzymeFields fields) {
        this.id = id;
        this.fields = fields;
    }

    /**
     *
     * @return The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     *
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return The source
     */
    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    /**
     *
     * @param source The source
     */
    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    /**
     *
     * @return The fields
     */
    @JsonProperty("fields")
    public EnzymeFields getFields() {
        return fields;
    }

    /**
     *
     * @param fields The fields
     */
    @JsonProperty("fields")
    public void setFields(EnzymeFields fields) {
        this.fields = fields;
    }

    @Override
    public String getEc() {
        return getId();
    }

    @Override
    public String getEnzymeName() {
        return fields.getName().stream().findFirst().orElse("");

    }

    @Override
    public String getEnzymeFamily() {
        if (fields.getEnzymeFamily().isEmpty()) {
            return computeFamily(getEc());
        }

        return fields.getEnzymeFamily().stream().findFirst().orElse("");

    }

    @Override
    public List<String> getCatalyticActivities() {

        return fields.getCatalyticActivity();

    }

    @Override
    public Set<String> getIntenzCofactors() {
        return fields.getIntenzCofactors();
    }

    @Override
    public Set<String> getAltNames() {
        return fields.getAltNames();
    }


    /**
     *
     * @param ec
     * @return enzyme family
     */
    public String computeFamily(String ec) {

        if (ec.startsWith("1")) {

            return EcNumber.EnzymeFamily.OXIDOREDUCTASES.getName();
        }
        if (ec.startsWith("2")) {
            return EcNumber.EnzymeFamily.TRANSFERASES.getName();
        }
        if (ec.startsWith("3")) {
            return EcNumber.EnzymeFamily.HYDROLASES.getName();
        }
        if (ec.startsWith("4")) {
            return EcNumber.EnzymeFamily.LYASES.getName();
        }
        if (ec.startsWith("5")) {
            return EcNumber.EnzymeFamily.ISOMERASES.getName();
        }
        if (ec.startsWith("6")) {
            return EcNumber.EnzymeFamily.LIGASES.getName();
        }

        if (ec.startsWith("7")) {
            return EcNumber.EnzymeFamily.TRANSLOCASES.getName();
        }

        return "Invalid Ec Number";
    }
}
