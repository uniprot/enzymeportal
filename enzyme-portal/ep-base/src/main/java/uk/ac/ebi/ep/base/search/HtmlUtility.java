/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.base.search;

import java.util.regex.Pattern;
import org.springframework.web.util.HtmlUtils;

/**
 * This class is for cleaning up invalid input from the browser (particularly to
 * handle viral ? and \\
 *
 * @author joseph
 */
public class HtmlUtility {

    public static String cleanText(String text) {

        String removeBackSlash = text.replaceAll(Pattern.quote("\\"), "");

        String escapeText = HtmlUtils.htmlEscape(removeBackSlash);

        //this method is mainly for ? and \\ when they are repeatedly submitted by a user resulting to ?????? and \\\\\\
        String cleanText = escapeText.replaceAll("&Acirc;", "").replaceAll("&acirc;", "").replaceAll("&#39;", "")
                .replaceAll("&Atilde;", "").replaceAll("&atilde;", "").replaceAll("&cent;", "").replaceAll("&pound;", "");


        return cleanText;
    }
}
