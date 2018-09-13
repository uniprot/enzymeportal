package uk.ac.ebi.ep.centralservice.helper;

/**
 *
 * @author Joseph
 */
public enum StandardValue {

    FIFTY(50f), ONE_THOUSAND(1000f);

    private final Float standardValue;

    private StandardValue(Float value) {
        this.standardValue = value;
    }

    public Float getStandardValue() {
        return standardValue;
    }
}
