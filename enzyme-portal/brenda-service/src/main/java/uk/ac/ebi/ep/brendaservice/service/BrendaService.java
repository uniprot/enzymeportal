package uk.ac.ebi.ep.brendaservice.service;

import java.util.List;
import uk.ac.ebi.ep.brendaservice.dto.Brenda;
import uk.ac.ebi.ep.brendaservice.dto.BrendaResult;
import uk.ac.ebi.ep.brendaservice.dto.Ph;
import uk.ac.ebi.ep.brendaservice.dto.Temperature;

/**
 *
 * @author joseph
 */
public interface BrendaService {

    Temperature findTemperatureByEcAndOrganism(String ecNumber, String organism);

    List<Temperature> findTemperatureByEcAndOrganism(String ecNumber, String organism, int limit);

    List<Temperature> findTemperatureByEc(String ecNumber, int limit, boolean addAcc);

    Ph findPhByEcAndOrganism(String ecNumber, String organism);

    List<Ph> findPhByEc(String ecNumber, int limit, boolean addAcc);

    List<Brenda> findKineticsByEc(String ecNumber, int limit);

    List<Brenda> findKcatKmValueByEc(String ecNumber, int limit, boolean addAcc);

    List<Brenda> findKmValueByEc(String ecNumber, int limit, boolean addAcc);

    BrendaResult findBrendaResultByEc(String ecNumber, int limit, boolean addAcc);

}
