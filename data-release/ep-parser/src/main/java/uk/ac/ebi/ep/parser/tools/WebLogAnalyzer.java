package uk.ac.ebi.ep.parser.tools;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Joseph
 */
public class WebLogAnalyzer {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(WebLogAnalyzer.class);

    private static final String VALID_EC_REGEX = "[1-6](\\.(\\-|\\d{1,4})){3}n*";

    public static boolean validateEc(String ec) {
        boolean isValid = false;
        if (ec.matches(VALID_EC_REGEX)) {

            return true;
        }
        return isValid;

    }

    private static final Comparator<Log> LOG_COUNT_COMPARATOR = (c1, c2) -> c1.getCount().compareTo(c2.getCount());

//    public static void main(String[] args) throws IOException {
//
//        String userHome = System.getProperty("user.home");
//
//        String logDir = userHome + "/ep-logs/2018/";
//
//        String log = logDir + "enzymeportal.log";
//
//        try {
//
//            fullList(log, logDir);
//            searchTermList(log, logDir);
//            idAndXrefList(log, logDir);
//
//        } catch (IOException ex) {
//
//            LOGGER.error(ex.getMessage());
//        }
//
//    }

    public static void analyseLogLocally() {

        String userHome = System.getProperty("user.home");

        String logDir = userHome + "/ep-logs/2018/";

        String log = logDir + "enzymeportal.log";

        try {

            fullList(log, logDir);
            searchTermList(log, logDir);
            idAndXrefList(log, logDir);

        } catch (IOException ex) {

            LOGGER.error(ex.getMessage());
        }

    }

    public static void analyseLog(String inputLogData, String outputLogDir)   {

        try {

            fullList(inputLogData, outputLogDir);
            searchTermList(inputLogData, outputLogDir);
            idAndXrefList(inputLogData, outputLogDir);

        } catch (IOException ex) {

            LOGGER.error(ex.getMessage());
        }

    }

    private static void fullList(String inputLogData, String outputLogDir) throws IOException {

        List<String> data = Files.readAllLines(Paths.get(inputLogData))
                .stream()
                .flatMap(line -> Arrays.stream(line.split(" ")))
                .map(x -> x.replaceAll("\\+", " ").replaceAll("%20", " "))
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()))
                .entrySet()
                .stream()
                .map(map -> new Log(map.getKey(), map.getValue()))
                .sorted(LOG_COUNT_COMPARATOR.reversed())
                .map(d -> d.getCount() + " : " + d.getSearchTerm())
                .collect(Collectors.toList());

        String outputFilename = "enzymeportal.txt";
        String outputFilePath = outputLogDir + outputFilename;

        data.add(0, "Full List - " + "#" + data.size());
        data.add(1, "=============================================================================================");
        data.add(2, "#Hits : Keyword ");
        data.add(3, "=============================================================================================");

        bufferedWrite(data, outputFilePath);

    }

    private static void searchTermList(String inputLogData, String outputLogDir) throws IOException {
        List<String> data = new LinkedList<>();

        Files.readAllLines(Paths.get(inputLogData))
                .stream()
                .flatMap(line -> Arrays.stream(line.split(" ")))
                .map(x -> x.replaceAll("\\+", " ").replaceAll("%20", " "))
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()))
                .entrySet()
                .stream()
                .map(map -> new Log(map.getKey(), map.getValue()))
                .filter(in -> !in.getSearchTerm().contains("INTENZ:"))
                .filter(in -> !in.getSearchTerm().contains("id:"))
                .filter(in -> !in.getSearchTerm().contains("ec:"))
                .filter(in -> !in.getSearchTerm().contains("OMIM:"))
                .filter(in -> !in.getSearchTerm().contains("TAXONOMY:"))
                .filter(in -> !in.getSearchTerm().contains("REACTOME:"))
                .filter(in -> !in.getSearchTerm().contains("REACT_"))
                .filter(in -> !in.getSearchTerm().contains("UNIPROT_PUBS:"))
                .filter(in -> !validateEc(in.getSearchTerm()))
                .sorted(LOG_COUNT_COMPARATOR.reversed())
                .forEachOrdered(d -> data.add(d.getCount() + " : " + d.getSearchTerm()));

        String outputFilename = "enzymeportal_searchterm.txt";
        String outputFilePath = outputLogDir + outputFilename;

        data.add(0, "SearchTerm List - " + "#" + data.size());
        data.add(1, "=============================================================================================");
        data.add(2, "#Hits : Keyword ");
        data.add(3, "=============================================================================================");

        bufferedWrite(data, outputFilePath);

    }

    private static void idAndXrefList(String inputLogData, String outputLogDir) throws IOException {

        List<String> data = new LinkedList<>();

        Files.readAllLines(Paths.get(inputLogData))
                .stream()
                .flatMap(line -> Arrays.stream(line.split(" ")))
                .map(x -> x.replaceAll("\\+", " ").replaceAll("%20", " "))
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()))
                .entrySet()
                .stream()
                .map(map -> new Log(map.getKey(), map.getValue()))
                .filter(in -> (in.getSearchTerm().contains("INTENZ:")
                || in.getSearchTerm().contains("id:")
                || in.getSearchTerm().contains("ec:")
                || in.getSearchTerm().contains("OMIM:")
                || in.getSearchTerm().contains("TAXONOMY:")
                || in.getSearchTerm().contains("REACTOME:")
                || in.getSearchTerm().contains("REACT_:")
                || in.getSearchTerm().contains("UNIPROT_PUBS:")
                || validateEc(in.getSearchTerm())))
                .sorted(LOG_COUNT_COMPARATOR.reversed())
                .forEachOrdered(d -> data.add(d.getCount() + " : " + d.getSearchTerm()));

        String outputFilename = "enzymeportal_ids.txt";
        String outputFilePath = outputLogDir + outputFilename;

        data.add(0, "ID's & XREF List - " + "#" + data.size());
        data.add(1, "=============================================================================================");
        data.add(2, "#Hits : Keyword ");
        data.add(3, "=============================================================================================");

        bufferedWrite(data, outputFilePath);

    }

    /**
     * Write a big list of Strings to a file - Use a BufferedWriter
     *
     * @param content
     * @param filePath
     */
    private static void bufferedWrite(List<String> content, String filePath) {

        Path fileP = Paths.get(filePath);

        Charset charset = Charset.forName("utf-8");

        try (BufferedWriter writer = Files.newBufferedWriter(fileP, charset)) {

            for (String line : content) {

                writer.write(line, 0, line.length());

                writer.newLine();

            }

        } catch (IOException ex) {

            LOGGER.error(ex.getMessage(), ex);

        }

    }
}
