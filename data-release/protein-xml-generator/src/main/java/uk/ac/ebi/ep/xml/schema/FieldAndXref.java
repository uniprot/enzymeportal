package uk.ac.ebi.ep.xml.schema;

import java.util.Set;

/**
 *
 * @author joseph
 */
public class FieldAndXref {

    private Set<Field> field;
    private Set<Ref> ref;

    public FieldAndXref(Set<Field> field, Set<Ref> ref) {
        this.field = field;
        this.ref = ref;
    }

    public Set<Field> getField() {
        return field;
    }

    public void setField(Set<Field> field) {
        this.field = field;
    }

    public Set<Ref> getRef() {
        return ref;
    }

    public void setRef(Set<Ref> ref) {
        this.ref = ref;
    }

}
