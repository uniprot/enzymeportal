package uk.ac.ebi.ep.enzymeservice.intenz.dto;

import lombok.Data;

/**
 *
 * @author Joseph
 */
@Data
public class EcClass {

    private String ec;

    private String name;

    public EcClass withEc(String value) {
        setEc(value);
        return this;
    }

    public EcClass withName(String value) {
        setName(value);
        return this;
    }

}
