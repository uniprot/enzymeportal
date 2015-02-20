/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.parser.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author joseph
 */
public class LogAnalyzer {

    public void processLog() {
        Map<String, Long> occurenceMap = null;
        try {
            occurenceMap = Files.readAllLines(Paths.get("/Users/joseph/ep-logs/ep-ACCESS-LOGS-oy90-2015.txt"))
                    .stream()
                    .flatMap(line -> Arrays.stream(line.split(" ")))
                    .collect(Collectors.groupingBy(i -> i, Collectors.counting()));
        } catch (IOException ex) {
            Logger.getLogger(LogAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         //System.out.println("data : "+ occurenceMap.entrySet().stream().distinct().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey)));
     
        System.out.println("INITIAL DATA "+ occurenceMap);  
        
        
        List<String> sortedWords = occurenceMap.entrySet()
                .stream()
                .sorted(Comparator.comparing((Map.Entry<String, Long> entry) -> entry.getValue()).reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        
//        
//           Map<String, Long> record = occurenceMap.entrySet()
//                   .stream()
//                    .sorted(Comparator.comparing((Map.Entry<String, Long> entry) -> entry.getValue()).reversed())
//                   .collect(Collectors.toMap(null, null));
//       
        
       
       
        System.out.println("SORTED "+ sortedWords.size());
        System.out.println("finall "+ sortedWords);
        //System.out.println("record "+ record);
        
        Map<Long,String> printer = new TreeMap<>();
        
        for(Map.Entry<String, Long> map:occurenceMap.entrySet()){
            
            System.out.println("map> "+ map.getKey()+" : "+ map.getValue());
            printer.put(map.getValue(), map.getKey());
        }
        
 Map<Long,String> p =   printer.entrySet().stream().sorted(Comparator.comparing((Map.Entry<Long, String> entry) -> entry.getValue()).reversed())
         .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        
        System.out.println("PRINTER "+ printer);
    }
    
    
    public static void main(String args[]){
        System.out.println("ok");
       LogAnalyzer analyzer = new LogAnalyzer();
       analyzer.processLog();
        
    }
}
