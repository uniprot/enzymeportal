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
        Entity entity = null;
        if (chebiWebServiceClient == null) {
            log.error("chebiWebServiceClient seem unavailable making requesting using ID " + chebiId);
            return entity;
        }

        try {
            if (chebiId != null || !StringUtils.isEmpty(chebiId)) {
                entity = chebiWebServiceClient.getCompleteEntity(chebiId);
            }
             } catch (Exception ex){
        //} catch (ChebiWebServiceFault_Exception | UnsupportedMediaException | ClientTransportException | IllegalArgumentException ex) {

            log.error("ChEBI webservice error for ID " + chebiId + " Reason :: " + ex.getMessage());
        }
        return entity;
    }

    @Override
    public List<String> getChebiSynonyms(String chebiId) {
        Entity chebiEntity = getCompleteChebiEntityInformation(chebiId);
        if ((chebiEntity != null)) {

            return chebiEntity
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
