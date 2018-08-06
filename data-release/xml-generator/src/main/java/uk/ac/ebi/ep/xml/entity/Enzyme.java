package uk.ac.ebi.ep.xml.entity;

import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Data;

/**
 *
 * @author Joseph
 */
@MappedSuperclass
@Data
public class Enzyme {

    @Id
    @Basic(optional = false)
    @Column(name = "EC_INTERNAL_ID")
    protected BigDecimal ecInternalId;
    @Column(name = "EC_FAMILY")
    protected Integer ecFamily;
    @Column(name = "ENZYME_NAME")
    protected String enzymeName;
    @Column(name = "CATALYTIC_ACTIVITY")
    protected String catalyticActivity;
    @Column(name = "TRANSFER_FLAG")
    protected Character transferFlag;
    @Column(name = "COFACTOR")
    protected String cofactor;
}
