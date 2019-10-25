package uk.ac.ebi.ep.parser.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;


/**
 *
 * @author joseph
 */
@Slf4j
public class LogParser {

//    public static void main(String[] args) {
//        String userHome = System.getProperty("user.home");
//
//        String output = userHome + "/ep-logs/2019/";
//        // String jsonFile = "/Users/joseph/ep-logs/enzymeportal-json.log";
//        //String jsonFile = "/Users/joseph/ep-logs/enzymeportal-jsonPG.json";
//        String jsonFile = "/Users/joseph/ep-logs/enzymeportal.log";
//
//        LogParser logParser = new LogParser();
//        logParser.processLog(jsonFile, output);
//        logParser.processLogWithCount(jsonFile, output);
//    }
    
    public void logReport(String jsonFile, String output){
      processLog(jsonFile, output);
      processLogWithCount(jsonFile, output);
    }

    private void processLog(String file, String outputLogDir) {
        List<String> data = new LinkedList<>();
        try {
            BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            Stream<String> stream = buff.lines();

            stream
                    //.filter(m->m.contains("\"message\""))
                    .filter(m -> m.contains("\"message\" : \"SearchType="))
                    .map(s -> buildSearchResult(s))
                    .filter(obj -> Objects.nonNull(obj))
                    //.forEach(x -> System.out.println(x));
                    .forEachOrdered(d -> data.add(d.toString()));

            String outputFilename = "enzymeportal2-log.txt";
            String outputFilePath = outputLogDir + outputFilename;

            data.add(0, "Enzyme Portal Log - " + "#" + data.size());
            data.add(1, "=============================================================================================");
            //data.add(2, "#Hits : Keyword ");
            data.add(2, "SearchType || SearchCategory  || SearchTerm  || Date ");
            data.add(3, "=============================================================================================");

            bufferedWrite(data, outputFilePath);

        } catch (FileNotFoundException ex) {
            log.error(ex.getLocalizedMessage());
        }
    }

    private SearchResult buildSearchResult(String query) {
        String queryString = query.split(":")[1];

        if (queryString.contains("SearchType=") && queryString.contains("SearchCategory=") && queryString.contains("searchTerm=") && queryString.contains("date=")) {
            String[] result = queryString.split(",");

            String type = result[0].replace("SearchType=", "");
            String category = result[1].replace("SearchCategory=", "");
            String term = result[2].replace("searchTerm=", "");

            String date = "";
            if (result.length > 3) {
                date = result[3].replace("date=", "");
            }

            return new SearchResult(type, category, term, date);
        }
        return null;
    }

    private static final Comparator<Log> LOG_COUNT_COMPARATOR = (c1, c2) -> c1.getCount().compareTo(c2.getCount());

    private void processLogWithCount(String file, String outputLogDir) {
        List<String> data = new LinkedList<>();
        try {
            BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            Stream<String> stream = buff.lines();

            stream
                    .filter(m -> m.contains("\"message\" : \"SearchType="))
                    .flatMap(line -> Arrays.stream(line.split(":")))
                    .filter(queryString ->queryString.contains("SearchType=") && queryString.contains("SearchCategory=") && queryString.contains("searchTerm=") && queryString.contains("date="))
                    .map(input ->  input.split(",date=")[0])//remove date
                    .map(x -> x.replaceAll("SearchType=", "").replaceAll("SearchCategory=", "").replace("searchTerm=", "").replace("date=", "").replaceAll("\"", "").replaceAll(",", " | "))
                    .collect(Collectors.groupingBy(i -> i, Collectors.counting()))
                    .entrySet()
                    .stream()
                    .map(map -> new Log(map.getKey(), map.getValue()))
                    .sorted(LOG_COUNT_COMPARATOR.reversed())
                    .forEachOrdered(d -> data.add(d.getCount() + " : " + d.getSearchTerm()));

            String outputFilename = "enzymeportal-count2.txt";
            String outputFilePath = outputLogDir + outputFilename;

            data.add(0, "Enzyme Portal Log - " + "#" + data.size());
            data.add(1, "=============================================================================================");
            data.add(2, "#Hits :    SearchType | SearchCategory | SearchTerm ");
            data.add(3, "=============================================================================================");

            bufferedWrite(data, outputFilePath);

        } catch (FileNotFoundException ex) {
            log.error(ex.getLocalizedMessage());
        }
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

            log.error(ex.getMessage(), ex);

        }

    }
}
