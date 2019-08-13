package uk.ac.ebi.ep.xml.schema;

import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 *
 * @author joseph
 */
@XmlType
public class Field {

    private String fieldName;
    private String value;

    public Field() {
    }
    

    public Field(String name, String value) {
        this.fieldName = name;
        this.value = value;
    }

    public String getFieldName() {
        return fieldName;
    }

    @XmlAttribute(name = "name")
    public void setFieldName(String name) {
        this.fieldName = name;
    }

    @XmlValue
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Field other = (Field) obj;
        return Objects.equals(this.value, other.value);
    }

}
