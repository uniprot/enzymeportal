/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.eptools.logging;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.log4j.FileAppender;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class CustomFileAppender extends FileAppender {

    public CustomFileAppender() {
    }

    @Override
    public void setFile(String file) {

        try {
            Path path = Paths.get(file);
            Files.deleteIfExists(path);
        } catch (IOException ex) {
            Logger.getLogger(CustomFileAppender.class.getName()).log(Level.SEVERE, null, ex);
        }

        super.setFile(prependDate(file));
    }

    private static String prependDate(String filename) {
        //return System.currentTimeMillis() + "_" + filename;
        return filename;

    }
}
