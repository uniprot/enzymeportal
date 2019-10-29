package uk.ac.ebi.ep.enzymeservice.brenda.service;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ep.enzymeservice.brenda.config.BrendaProperties;
import uk.ac.ebi.ep.enzymeservice.brenda.dto.Brenda;
import uk.ac.ebi.ep.enzymeservice.brenda.dto.BrendaConfig;
import uk.ac.ebi.ep.enzymeservice.brenda.dto.Ph;
import uk.ac.ebi.ep.enzymeservice.brenda.dto.Temperature;

/**
 *
 * @author joseph
 */
@Slf4j
@org.springframework.stereotype.Service
public class BrendaService {

    private final BrendaProperties brendaProperties;
    private final String SOAP_URL = "http://soapinterop.org/";
    private final String EC_NUMBER = ",ecNumber*";
    private final String ORGANISM = "#organism*";

    @Autowired
    public BrendaService(BrendaProperties brendaProperties) {
        this.brendaProperties = brendaProperties;
    }

    private BrendaConfig getBrendaConfig() {
        Call call = null;
        String parameters = null;
        try {
            Service service = new Service();
            call = (Call) service.createCall();
            String endpoint = brendaProperties.getUrl();
            String password = brendaProperties.getPassword();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte byteData[] = md.digest();
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < byteData.length; i++) {
                String hex = Integer.toHexString(0xff & byteData[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            call.setTargetEndpointAddress(new java.net.URL(endpoint));

            parameters = String.format("%s,%s", brendaProperties.getUsername(), hexString);

        } catch (ServiceException | NoSuchAlgorithmException | MalformedURLException ex) {
            log.error(ex.getMessage());
        }
        return BrendaConfig
                .builder()
                .call(call)
                .parameters(parameters)
                .build();
    }

    private String invokeBrendaSoapService(Call call, String parameters) {
        try {
            return (String) call.invoke(new Object[]{parameters});
        } catch (RemoteException ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    //temperature
    private String getTemperatureRange(Call call, String parameters) {
        call.setOperationName(new QName(SOAP_URL, "getTemperatureRange"));
        return invokeBrendaSoapService(call, parameters);
    }

    private String replaceDegree(String data) {
        return data.replace("&deg;C", "'C");
    }

    private String replaceAcirc(String data) {
        return data.replace("&Acirc;", "");
    }

    private Stream<Temperature> processTemperature(String data) {
        return Stream.of(data.split("#!"))
                .map(x -> x.replace("\\*", ""))
                .map(this::replaceDegree)
                .map(this::replaceAcirc)
                .map(s -> s.split("#"))
                .filter(queryString -> queryString.length > 1)
                .map(queryString -> buildTemperature(queryString));

    }

    public Temperature findTemperatureByEcAndOrganism(String ecNumber, String organism) {
        BrendaConfig brendaConfig = getBrendaConfig();

        String parameters = brendaConfig.getParameters() + EC_NUMBER + ecNumber + ORGANISM + organism;
        String result = getTemperatureRange(brendaConfig.getCall(), parameters);
        return processTemperature(result)
                .findFirst()
                .orElse(Temperature.builder().build());
    }

    public List<Temperature> findTemperatureByEcAndOrganism(String ecNumber, String organism, int limit) {
        BrendaConfig brendaConfig = getBrendaConfig();

        String parameters = brendaConfig.getParameters() + EC_NUMBER + ecNumber + ORGANISM + organism;
        String result = getTemperatureRange(brendaConfig.getCall(), parameters);
        return processTemperature(result)
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Temperature> findTemperatureByEc(String ecNumber, int limit) {
        BrendaConfig brendaConfig = getBrendaConfig();

        String parameters = brendaConfig.getParameters() + EC_NUMBER + ecNumber;
        String result = getTemperatureRange(brendaConfig.getCall(), parameters);
        return processTemperature(result)
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Stream<Temperature> findTemperatureByEc(String ecNumber) {
        BrendaConfig brendaConfig = getBrendaConfig();

        String parameters = brendaConfig.getParameters() + EC_NUMBER + ecNumber;
        String result = getTemperatureRange(brendaConfig.getCall(), parameters);
        return processTemperature(result);
    }

    //ph
    private String getPhRange(Call call, String parameters) {
        call.setOperationName(new QName(SOAP_URL, "getPhRange"));
        return invokeBrendaSoapService(call, parameters);
    }

    private Stream<Ph> processPh(String data) {

        return Stream.of(data.split("#!"))
                .map(x -> x.replace("\\*", ""))
                .map(this::replaceDegree)
                .map(this::replaceAcirc)
                .map(s -> s.split("#"))
                .filter(queryString -> queryString.length > 1)
                .map(queryString -> buildPh(queryString));

    }

    public Ph findPhByEcAndOrganism(String ecNumber, String organism) {
        BrendaConfig brendaConfig = getBrendaConfig();

        String parameters = brendaConfig.getParameters() + EC_NUMBER + ecNumber + ORGANISM + organism;
        String result = getPhRange(brendaConfig.getCall(), parameters);
        return processPh(result)
                .findFirst()
                .orElse(Ph.builder().build());
    }

    public List<Ph> findPhByEc(String ecNumber, int limit) {
        BrendaConfig brendaConfig = getBrendaConfig();

        String parameters = brendaConfig.getParameters() + EC_NUMBER + ecNumber;
        String result = getPhRange(brendaConfig.getCall(), parameters);
        return processPh(result)
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Stream<Ph> findPhByEc(String ecNumber) {
        BrendaConfig brendaConfig = getBrendaConfig();

        String parameters = brendaConfig.getParameters() + EC_NUMBER + ecNumber;
        String result = getPhRange(brendaConfig.getCall(), parameters);
        return processPh(result);

    }

    //brenda
    private String getKinetics(Call call, String parameters) {
        call.setOperationName(new QName(SOAP_URL, "getKcatKmValue"));
        String resultString = invokeBrendaSoapService(call, parameters);

        if (StringUtils.isEmpty(resultString)) {
            call.setOperationName(new QName(SOAP_URL, "getKmValue"));

            return invokeBrendaSoapService(call, parameters);
        }
        return resultString;

    }

    private Stream<Brenda> processKinetics(String data) {

        return Stream.of(data.split("#!"))
                .map(x -> x.replace("\\*", ""))
                .map(this::replaceDegree)
                .map(this::replaceAcirc)
                .map(s -> s.split("#"))
                .filter(queryString -> queryString.length > 1)
                .map(queryString -> buildEnzymeInformation(queryString));

    }

    public Stream<Brenda> findKineticsByEc(String ecNumber) {
        BrendaConfig brendaConfig = getBrendaConfig();

        String parameters = brendaConfig.getParameters() + EC_NUMBER + ecNumber;

        String result = getKinetics(brendaConfig.getCall(), parameters);
        return processKinetics(result);

    }

    public List<Brenda> findKineticsByEc(String ecNumber, int limit) {
        BrendaConfig brendaConfig = getBrendaConfig();

        String parameters = brendaConfig.getParameters() + EC_NUMBER + ecNumber;

        String result = getKinetics(brendaConfig.getCall(), parameters);
        return processKinetics(result)
                .limit(limit)
                .collect(Collectors.toList());
    }

    private Brenda buildEnzymeInformation(String[] queryString) {
        String ec = "";
        String kcatKmValue = "";
        String kmValue = "";
        String substrate = "";
        String commentary = "";
        String organism = "";

        if (queryString[0].contains("ecNumber")) {
            ec = queryString[0].replace("ecNumber", "");
        }

        if (queryString[1].contains("kcatKmValue")) {
            kcatKmValue = queryString[1].replace("kcatKmValue", "");
        }
        if (queryString[1].contains("kmValue")) {
            kmValue = queryString[1].replace("kmValue", "");
        }

        if (queryString[3].contains("substrate")) {
            substrate = queryString[3].replace("substrate", "");
        }

        if (queryString[4].contains("commentary")) {
            commentary = queryString[4].replace("commentary", "");
        }

        if (queryString[5].contains("organism")) {
            organism = queryString[5].replace("organism", "").trim();
        }

        return Brenda
                .builder()
                .ecNumber(ec)
                .organism(organism)
                .kcatKmValue(kcatKmValue)
                .kmValue(kmValue)
                .substrate(substrate)
                .comment(commentary)
                .build();
    }

    private Ph buildPh(String[] queryString) {

        String ecNumber = "";
        String phRange = "";
        String phRangeMaximum = "";
        String organism = "";
        String commentary = "";

        if (queryString[0].contains("ecNumber")) {
            ecNumber = queryString[0].replace("ecNumber", "");
        }
        if (queryString[1].contains("phRange")) {
            phRange = queryString[1].replace("phRange", "");
        }
        if (queryString[2].contains("phRangeMaximum")) {
            phRangeMaximum = queryString[2].replace("phRangeMaximum", "");
        }
        if (queryString[3].contains("commentary")) {
            commentary = queryString[3].replace("commentary", "");
        }
        if (queryString[4].contains("organism")) {
            organism = queryString[4].replace("organism", "");
        }
        return Ph.builder()
                .ecNumber(ecNumber)
                .phRange(phRange)
                .phRangeMaximum(phRangeMaximum)
                .organism(organism)
                .comment(commentary)
                .build();

    }

    private Temperature buildTemperature(String[] queryString) {

        String ecNumber = "";
        String temperatureRange = "";
        String temperatureRangeMaximum = "";
        String organism = "";
        String commentary = "";

        if (queryString[0].contains("ecNumber")) {
            ecNumber = queryString[0].replace("ecNumber", "");
        }
        if (queryString[1].contains("temperatureRange")) {
            temperatureRange = queryString[1].replace("temperatureRange", "");
        }
        if (queryString[2].contains("temperatureRangeMaximum")) {
            temperatureRangeMaximum = queryString[2].replace("temperatureRangeMaximum", "");
        }
        if (queryString[3].contains("commentary")) {
            commentary = queryString[3].replace("commentary", "");
        }
        if (queryString[4].contains("organism")) {
            organism = queryString[4].replace("organism", "").trim();
        }

        return Temperature
                .builder()
                .ecNumber(ecNumber)
                .temperatureRange(temperatureRange)
                .temperatureRangeMaximum(temperatureRangeMaximum)
                .organism(organism)
                .comment(commentary)
                .build();
    }

}
