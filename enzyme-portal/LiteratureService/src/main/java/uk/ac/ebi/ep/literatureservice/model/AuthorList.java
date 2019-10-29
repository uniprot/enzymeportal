package uk.ac.ebi.ep.literatureservice.model;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author joseph
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"author"
})
public class AuthorList {

@JsonProperty("author")
private List<Author> author = new ArrayList<>();
@JsonIgnore
private final Map<String, Object> additionalProperties = new HashMap<>();

/**
* 
* @return
* The author
*/
@JsonProperty("author")
public List<Author> getAuthor() {
return author;
}

/**
* 
* @param author
* The author
*/
@JsonProperty("author")
public void setAuthor(List<Author> author) {
this.author = author;
}

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}
