/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.parser.main;

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
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;

/**
 *
 * @author joseph
 */
public class LogAnalyzer {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LogAnalyzer.class);

    public void processLog() {
        Map<String, Long> frequencyMap = null;
        Map<Long, String> printer = new TreeMap<>(reverseKeyJava8);

        List<String> data = new LinkedList<>();
        data.add("views " + "  Keyword");
        data.add(" ");

        String userHome = System.getProperty("user.home");

        String logDir = userHome + "/ep-logs/";
        String log = logDir + "ep-access-logs.log";


        try {
            //frequencyMap = Files.readAllLines(Paths.get("/Users/joseph/ep-logs/ep-ACCESS-LOGS-oy90-2015.txt"))

            frequencyMap = Files.readAllLines(Paths.get(log))
                    .stream()
                    .flatMap(line -> Arrays.stream(line.split(" ")))
                    .collect(Collectors.groupingBy(i -> i, Collectors.counting()));
        } catch (IOException ex) {
            Logger.getLogger(LogAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }

//        List<String> sortedWords = frequencyMap.entrySet()
//                .stream()
//                .sorted(Comparator.comparing((Map.Entry<String, Long> entry) -> entry.getValue()).reversed())
//                .map(Map.Entry::getKey)
//                .collect(Collectors.toList());
        for (Map.Entry<String, Long> map : frequencyMap.entrySet()) {

            //System.out.println("map> "+ map.getKey()+" : "+ map.getValue());
            printer.put(map.getValue(), map.getKey().replaceAll("%20", " "));
        }

        for (Map.Entry<Long, String> epMap : printer.entrySet()) {

            //System.out.println("logs => " + epMap.getKey() + " : " + epMap.getValue());
            //data.add(epMap.getValue() + " : " + epMap.getKey());
            data.add(epMap.getKey() + "   : " + epMap.getValue());

        }

        String filePath = logDir + "sampleLog.txt";
        bufferedWrite(data, filePath);

    }

    public static void main(String args[]) {
        System.out.println("ok");
        LogAnalyzer analyzer = new LogAnalyzer();
        analyzer.processLog();

    }

    /**
     * 048 Write a big list of Strings to a file - Use a BufferedWriter 049
     *
     * @param content
     * @param filePath
     */
    public static void bufferedWrite(List<String> content, String filePath) {

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

    public static Comparator<Long> reverseKeyJava8 = (Long key1, Long key2) -> -key1.compareTo(key2);
    public static Comparator<Long> reverseKey = new Comparator<Long>() {
        @Override
        public int compare(Long key1, Long key2) {

            return -key1.compareTo(key2);
        }
    };

}
