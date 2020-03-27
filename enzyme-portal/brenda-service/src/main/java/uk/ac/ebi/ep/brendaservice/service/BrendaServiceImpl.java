package uk.ac.ebi.ep.brendaservice.service;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import static java.util.Comparator.nullsLast;
import static java.util.Comparator.reverseOrder;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ep.brendaservice.config.BrendaConfig;
import uk.ac.ebi.ep.brendaservice.config.BrendaProperties;
import uk.ac.ebi.ep.brendaservice.dto.Brenda;
import uk.ac.ebi.ep.brendaservice.dto.BrendaResult;
import uk.ac.ebi.ep.brendaservice.dto.Ph;
import uk.ac.ebi.ep.brendaservice.dto.Sequence;
import uk.ac.ebi.ep.brendaservice.dto.Temperature;

/**
 *
 * @author joseph
 */
@Slf4j
@org.springframework.stereotype.Service
public class BrendaServiceImpl implements BrendaService {

    private final BrendaProperties brendaProperties;
    private static final String SOAP_URL = "http://soapinterop.org/";
    private static final String EC_NUMBER = ",ecNumber*";
    private static final String ORGANISM = "#organism*";
    private static final String MORE = "more";
    private static final String INVALID_NUMBER = "-999";
    private static final String EC = "ecNumber";

    private static final String COMMENTARY = "commentary";
    private static final String ORGANISMS = "organism";

    @Autowired
    public BrendaServiceImpl(BrendaProperties brendaProperties) {
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
            MessageDigest md = MessageDigest.getInstance(brendaProperties.getAlgorithm());
            md.update(password.getBytes());
            byte[] byteData = md.digest();
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
            call.setTimeout(30_000);
            return (String) call.invoke(new Object[]{parameters});
        } catch (RemoteException ex) {
            log.error(ex.getMessage(), ex);
        }
        return "";
    }

    @Override
    @Cacheable(cacheNames = "findBrendaResultByEc", key = "#ecNumber")
    public BrendaResult findBrendaResultByEc(String ecNumber, int limit, boolean addAcc) {

        List<Brenda> kmKcat = findKcatKmValueByEc(ecNumber, limit, addAcc);

        List<Ph> phList = findPhByEc(ecNumber, limit, addAcc);
        List<Temperature> tempList = findTemperatureByEc(ecNumber, limit, addAcc);

        if (kmKcat.isEmpty()) {
            List<Brenda> kmValue = findKmValueByEc(ecNumber, limit, addAcc);
            return new BrendaResult(kmValue, phList, tempList, true);

        }

        return new BrendaResult(kmKcat, phList, tempList, false);
    }

    //temperature
    private String getTemperatureRange(Call call, String parameters) {
        call.setOperationName(new QName(SOAP_URL, "getTemperatureRange"));
        return invokeBrendaSoapService(call, parameters);
    }

    private String replaceDegree(String data) {
        return data;
    }

    private String replaceAcirc(String data) {
        return data.replace("&Acirc;", "");
    }

    private Stream<Temperature> processTemperature(@NonNull String data) {
        return Stream.of(data.split("#!"))
                .map(x -> x.replaceAll("\\*", ""))
                .map(this::replaceDegree)
                .map(this::replaceAcirc)
                .map(s -> s.split("#"))
                .filter(queryString -> queryString.length > 1)
                .map(this::buildTemperature);

    }

    @Override
    public Temperature findTemperatureByEcAndOrganism(String ecNumber, String organism) {
        BrendaConfig brendaConfig = getBrendaConfig();

        String parameters = brendaConfig.getParameters() + EC_NUMBER + ecNumber + ORGANISM + organism;
        String result = getTemperatureRange(brendaConfig.getCall(), parameters);
        return processTemperature(result)
                .findFirst()
                .orElse(Temperature.builder().build());
    }

    @Override
    public List<Temperature> findTemperatureByEcAndOrganism(String ecNumber, String organism, int limit) {
        BrendaConfig brendaConfig = getBrendaConfig();

        String parameters = brendaConfig.getParameters() + EC_NUMBER + ecNumber + ORGANISM + organism;
        String result = getTemperatureRange(brendaConfig.getCall(), parameters);
        return processTemperature(result)
                .distinct()
                .map(this::addAccession)
                .sorted(Comparator.comparing(Temperature::getAccession, nullsLast(reverseOrder())))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Temperature> findTemperatureByEc(String ecNumber, int limit, boolean addAcc) {
        BrendaConfig brendaConfig = getBrendaConfig();

        String parameters = brendaConfig.getParameters() + EC_NUMBER + ecNumber;
        String result = getTemperatureRange(brendaConfig.getCall(), parameters);

        if (addAcc) {
            return processTemperature(result)
                    .distinct()
                    .filter(n -> !n.getTemperatureRange().trim().equalsIgnoreCase(INVALID_NUMBER))
                    .map(this::addAccession)
                    .sorted(Comparator.comparing(Temperature::getAccession, nullsLast(reverseOrder())))
                    .limit(limit)
                    .collect(Collectors.toList());
        }
        return processTemperature(result)
                .distinct()
                .filter(n -> !n.getTemperatureRange().trim().equalsIgnoreCase(INVALID_NUMBER))
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

    private Stream<Ph> processPh(@NonNull String data) {

        return Stream.of(data.split("#!"))
                .map(x -> x.replaceAll("\\*", ""))
                .map(this::replaceDegree)
                .map(this::replaceAcirc)
                .map(s -> s.split("#"))
                .filter(queryString -> queryString.length > 1)
                .map(this::buildPh);

    }

    @Override
    public Ph findPhByEcAndOrganism(String ecNumber, String organism) {
        BrendaConfig brendaConfig = getBrendaConfig();

        String parameters = brendaConfig.getParameters() + EC_NUMBER + ecNumber + ORGANISM + organism;
        String result = getPhRange(brendaConfig.getCall(), parameters);
        return processPh(result)
                .findFirst()
                .orElse(Ph.builder().build());
    }

    @Override
    public List<Ph> findPhByEc(String ecNumber, int limit, boolean addAcc) {
        BrendaConfig brendaConfig = getBrendaConfig();

        String parameters = brendaConfig.getParameters() + EC_NUMBER + ecNumber;
        String result = getPhRange(brendaConfig.getCall(), parameters);
        if (addAcc) {
            return processPh(result)
                    .distinct()
                    .filter(n -> !n.getPhRange().trim().equalsIgnoreCase(INVALID_NUMBER))
                    .map(this::addAccession)
                    .sorted(Comparator.comparing(Ph::getAccession, nullsLast(reverseOrder())))
                    .limit(limit)
                    .collect(Collectors.toList());
        }
        return processPh(result)
                .distinct()
                .filter(n -> !n.getPhRange().trim().equalsIgnoreCase(INVALID_NUMBER))
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

    private String getKcatKmValue(Call call, String parameters) {
        call.setOperationName(new QName(SOAP_URL, "getKcatKmValue"));
        return invokeBrendaSoapService(call, parameters);

    }

    private String getKmValue(Call call, String parameters) {

        call.setOperationName(new QName(SOAP_URL, "getKmValue"));

        return invokeBrendaSoapService(call, parameters);

    }

    private Stream<Brenda> processKinetics(@NonNull String data) {

        return Stream.of(data.split("#!"))
                .map(x -> x.replaceAll("\\*", ""))
                .map(this::replaceDegree)
                .map(this::replaceAcirc)
                .map(s -> s.split("#"))
                .filter(queryString -> queryString.length > 1)
                .map(this::buildEnzymeInformation);

    }

    public Stream<Brenda> findKineticsByEc(String ecNumber) {
        BrendaConfig brendaConfig = getBrendaConfig();

        String parameters = brendaConfig.getParameters() + EC_NUMBER + ecNumber;

        String result = getKinetics(brendaConfig.getCall(), parameters);
        return processKinetics(result);

    }

    @Override
    public List<Brenda> findKineticsByEc(String ecNumber, int limit) {
        BrendaConfig brendaConfig = getBrendaConfig();

        String parameters = brendaConfig.getParameters() + EC_NUMBER + ecNumber;

        String result = getKinetics(brendaConfig.getCall(), parameters);
        return processKinetics(result)
                .distinct()
                .filter(more -> !more.getSubstrate().trim().equalsIgnoreCase(MORE))
                .filter(n -> !n.getKcatKmValue().trim().equalsIgnoreCase(INVALID_NUMBER))
                .filter(n -> !n.getKmValue().trim().equalsIgnoreCase(INVALID_NUMBER))
                //.parallel()
                .map(this::addAccession)
                .sorted(Comparator.comparing(Brenda::getAccession, nullsLast(reverseOrder())))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Brenda> findKcatKmValueByEc(String ecNumber, int limit, boolean addAcc) {
        BrendaConfig brendaConfig = getBrendaConfig();

        String parameters = brendaConfig.getParameters() + EC_NUMBER + ecNumber;

        String result = getKcatKmValue(brendaConfig.getCall(), parameters);
        if (addAcc) {

            return processKinetics(result)
                    .distinct()
                    //.parallel()
                    .filter(more -> !more.getSubstrate().trim().equalsIgnoreCase(MORE))
                    .filter(n -> !n.getKcatKmValue().trim().equalsIgnoreCase(INVALID_NUMBER))
                    .filter(n -> !n.getKmValue().trim().equalsIgnoreCase(INVALID_NUMBER))
                    .map(this::addAccession)
                    .sorted(Comparator.comparing(Brenda::getAccession, nullsLast(reverseOrder())))
                    .limit(limit)
                    .collect(Collectors.toList());
        }

        return processKinetics(result)
                .distinct()
                .filter(more -> !more.getSubstrate().trim().equalsIgnoreCase(MORE))
                .filter(n -> !n.getKcatKmValue().trim().equalsIgnoreCase(INVALID_NUMBER))
                .filter(n -> !n.getKmValue().trim().equalsIgnoreCase(INVALID_NUMBER))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Brenda> findKmValueByEc(String ecNumber, int limit, boolean addAcc) {
        BrendaConfig brendaConfig = getBrendaConfig();

        String parameters = brendaConfig.getParameters() + EC_NUMBER + ecNumber;

        String result = getKmValue(brendaConfig.getCall(), parameters);

        if (addAcc) {
            return processKinetics(result)
                    .distinct()
                    //.parallel()
                    .filter(more -> !more.getSubstrate().trim().equalsIgnoreCase(MORE))
                    .filter(n -> !n.getKcatKmValue().trim().equalsIgnoreCase(INVALID_NUMBER))
                    .filter(n -> !n.getKmValue().trim().equalsIgnoreCase(INVALID_NUMBER))
                    .map(this::addAccession)
                    .sorted(Comparator.comparing(Brenda::getAccession, nullsLast(reverseOrder())))
                    .limit(limit)
                    .collect(Collectors.toList());
        }
        return processKinetics(result)
                .distinct()
                .filter(more -> !more.getSubstrate().trim().equalsIgnoreCase(MORE))
                .filter(n -> !n.getKcatKmValue().trim().equalsIgnoreCase(INVALID_NUMBER))
                .filter(n -> !n.getKmValue().trim().equalsIgnoreCase(INVALID_NUMBER))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private Stream<Sequence> processSequence(String data) {
        if (data == null) {
            return Stream.empty();
        }
        return Stream.of(data.split("#!"))
                .map(x -> x.replaceAll("\\*", ""))
                .map(this::replaceDegree)
                .map(this::replaceAcirc)
                .map(s -> s.split("#"))
                .filter(queryString -> queryString.length > 1)
                .map(this::buildSequence);

    }

    private Brenda addAccession(@NonNull Brenda brenda) {
        Sequence sequence = findSequenceByEcAndOrganism(brenda.getEcNumber(), brenda.getOrganism());
        return Brenda
                .builder()
                .ecNumber(brenda.getEcNumber())
                .organism(brenda.getOrganism())
                .kcatKmValue(brenda.getKcatKmValue())
                .kmValue(brenda.getKmValue())
                .substrate(brenda.getSubstrate())
                .comment(brenda.getComment())
                .kmv(brenda.isKmv())
                .accession(sequence.getAccession())
                .build();
    }

    private Ph addAccession(@NonNull Ph ph) {
        Sequence sequence = findSequenceByEcAndOrganism(ph.getEcNumber(), ph.getOrganism());
        return Ph
                .builder()
                .ecNumber(ph.getEcNumber())
                .organism(ph.getOrganism())
                .phRange(ph.getPhRange())
                .comment(ph.getComment())
                .accession(sequence.getAccession())
                .build();
    }

    private Temperature addAccession(@NonNull Temperature temp) {
        Sequence sequence = findSequenceByEcAndOrganism(temp.getEcNumber(), temp.getOrganism());
        return Temperature
                .builder()
                .ecNumber(temp.getEcNumber())
                .organism(temp.getOrganism())
                .temperatureRange(temp.getTemperatureRange())
                .comment(temp.getComment())
                .accession(sequence.getAccession())
                .build();
    }

    private Brenda buildEnzymeInformation(String[] queryString) {
        String ec = "";
        String kcatKmValue = "";
        String kmValue = "";
        String substrate = "";
        String commentary = "";
        String organism = "";
        boolean isKmV = false;

        if (queryString[0].contains(EC)) {
            ec = queryString[0].replace(EC, "");
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

        if (queryString[4].contains(COMMENTARY)) {
            commentary = queryString[4].replace(COMMENTARY, "");
        }

        if (queryString[5].contains(ORGANISMS)) {
            organism = queryString[5].replace(ORGANISMS, "").trim();
        }
        if (StringUtils.isEmpty(kcatKmValue) && !StringUtils.isEmpty(kmValue)) {
            isKmV = true;
        }

        return Brenda
                .builder()
                .ecNumber(ec)
                .organism(organism)
                .kcatKmValue(kcatKmValue)
                .kmValue(kmValue)
                .substrate(substrate)
                .comment(commentary)
                .kmv(isKmV)
                .build();
    }

    private Ph buildPh(String[] queryString) {

        String ecNumber = "";
        String phRange = "";
        String phRangeMaximum = "";
        String organism = "";
        String commentary = "";

        if (queryString[0].contains(EC)) {
            ecNumber = queryString[0].replace(EC, "");
        }
        if (queryString[1].contains("phRange")) {
            phRange = queryString[1].replace("phRange", "");
        }
        if (queryString[2].contains("phRangeMaximum")) {
            phRangeMaximum = queryString[2].replace("phRangeMaximum", "");
        }
        if (queryString[3].contains(COMMENTARY)) {
            commentary = queryString[3].replace(COMMENTARY, "");
        }
        if (queryString[4].contains(ORGANISMS)) {
            organism = queryString[4].replace(ORGANISMS, "");
        }

        if (!StringUtils.isEmpty(phRange) && !StringUtils.isEmpty(phRangeMaximum)) {
            phRange = phRange + " - " + phRangeMaximum;
        } else {
            phRange = phRange + " " + phRangeMaximum;
        }

        return Ph.builder()
                .ecNumber(ecNumber)
                .phRange(phRange)
                //.phRangeMaximum(phRangeMaximum)
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

        if (queryString[0].contains(EC)) {
            ecNumber = queryString[0].replace(EC, "");
        }
        if (queryString[1].contains("temperatureRange")) {
            temperatureRange = queryString[1].replace("temperatureRange", "");
        }
        if (queryString[2].contains("temperatureRangeMaximum")) {
            temperatureRangeMaximum = queryString[2].replace("temperatureRangeMaximum", "");
        }
        if (queryString[3].contains(COMMENTARY)) {
            commentary = queryString[3].replace(COMMENTARY, "");
        }
        if (queryString[4].contains(ORGANISMS)) {
            organism = queryString[4].replace(ORGANISMS, "").trim();
        }

        if (!StringUtils.isEmpty(temperatureRange) && !StringUtils.isEmpty(temperatureRangeMaximum)) {
            temperatureRange = temperatureRange + " - " + temperatureRangeMaximum;
        } else {
            temperatureRange = temperatureRange + " " + temperatureRangeMaximum;
        }

        return Temperature
                .builder()
                .ecNumber(ecNumber)
                .temperatureRange(temperatureRange)
                //.temperatureRangeMaximum(temperatureRangeMaximum)
                .organism(organism)
                .comment(commentary)
                .build();
    }

    //sequence
    private String getSequence(Call call, String parameters) {
        call.setOperationName(new QName(SOAP_URL, "getSequence"));

        return invokeBrendaSoapService(call, parameters);
    }

    private Sequence findSequenceByEcAndOrganism(String ecNumber, String organism) {
        BrendaConfig brendaConfig = getBrendaConfig();

        String parameters = brendaConfig.getParameters() + EC_NUMBER + ecNumber + ORGANISM + organism;
        String result = getSequence(brendaConfig.getCall(), parameters);
        return processSequence(result)
                .findFirst().orElse(Sequence.builder().build());

    }

    private Sequence buildSequence(String[] queryString) {

        String ecNumber = "";
        String firstAccessionCode = "";

        String organism = "";

        if (queryString[0].contains(EC)) {
            ecNumber = queryString[0].replace(EC, "");
        }

        if (queryString[3].contains("firstAccessionCode")) {
            firstAccessionCode = queryString[3].replace("firstAccessionCode", "");
        }

        if (queryString[6].contains(ORGANISMS)) {
            organism = queryString[6].replace(ORGANISMS, "");
        }
        return Sequence.builder()
                .ecNumber(ecNumber)
                .accession(firstAccessionCode)
                .organism(organism)
                .build();

    }

}
