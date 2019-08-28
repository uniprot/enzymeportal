package uk.ac.ebi.ep.metaboliteService.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uk.ac.ebi.chebi.webapps.chebiWS.client.ChebiWebServiceClient;
import uk.ac.ebi.chebi.webapps.chebiWS.model.ChebiWebServiceFault_Exception;
import uk.ac.ebi.chebi.webapps.chebiWS.model.Entity;

/**
 *
 * @author joseph
 */
@Service
@Slf4j
public class ChebiServiceImpl implements ChebiService {

    private final ChebiWebServiceClient chebiWebServiceClient;

    @Autowired
    public ChebiServiceImpl(ChebiWebServiceClient chebiWebServiceClient) {
        this.chebiWebServiceClient = chebiWebServiceClient;
    }

    @Override
    public Entity getCompleteChebiEntityInformation(String chebiId) {
        try {

            return chebiWebServiceClient.getCompleteEntity(chebiId);
        } catch (ChebiWebServiceFault_Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public List<String> getChebiSynonyms(String chebiId) {
        Entity chebiEntity = getCompleteChebiEntityInformation(chebiId);
        if ((chebiEntity != null)) {

            return getCompleteChebiEntityInformation(chebiId)
                    .getSynonyms()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(s -> StringUtils.capitalize(s.getData().toLowerCase()))
                    .distinct()
                    .collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }

}
