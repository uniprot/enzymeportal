
package uk.ac.ebi.ep.xml.schema;

import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author joseph
 */
@XmlType
public class Ref {

    private String dbKey;
    private String dbName;

    public Ref() {
    }
    public Ref(String dbKey, String dbName) {
        this.dbKey = dbKey;
        this.dbName = dbName;
    }

    @XmlAttribute(name = "dbkey")
    public String getDbKey() {
        return dbKey;
    }

    public void setDbKey(String dbKey) {
        this.dbKey = dbKey;
    }

    @XmlAttribute(name = "dbname")
    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.dbKey);
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
        final Ref other = (Ref) obj;
        return Objects.equals(this.dbKey, other.dbKey);
    }

}
