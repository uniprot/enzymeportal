package uk.ac.ebi.ep.web.utils;

import java.util.regex.Pattern;
import org.springframework.web.util.HtmlUtils;

/**
 * This class is for cleaning up invalid input from the browser (particularly to
 * handle viral ? and \\ e.g when a user query results in viral ?????? and
 * \\\\\\
 *
 * @author joseph
 */
public class HtmlUtility {

    private HtmlUtility() {
    }

    public static String cleanText(String text) {

        String removeBackSlash = text.replaceAll(Pattern.quote("\\"), "");

        String escapeText = HtmlUtils.htmlEscape(removeBackSlash);

        return escapeText.replace("&Acirc;", "").replace("&acirc;", "").replace("&#39;", "")
                .replace("&Atilde;", "").replace("&atilde;", "").replace("&cent;", "").replace("&pound;", "");

    }
}
