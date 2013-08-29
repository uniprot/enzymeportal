/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.web;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.web.util.HtmlUtils;
import uk.ac.ebi.ep.core.search.HtmlUtility;

/**
 * Due to no similar functionality in JSTL, this function was designed to help
 * various operation in a Collection.
 *
 * @author joseph
 */
public final class Functions {

    private Functions() {
        // Hiden constructor.
    }

    /**
     *
     * @param collection
     * @param item
     * @return true if the item is contained in the collection
     */
    public static boolean contains(List collection, Object item) {
        return collection.contains(item);
    }
    
        /**
     *
     * @param alpha the first item
     * @param omega the last item
     * @return true if none of the items is null
     */
    public static boolean alphaOmegaIsNotNull(Object alpha, Object omega) {
        boolean eval = true;
        if (alpha == null | omega == null) {

            eval = false;
        }

        return eval;

    }

    /**
     *
     * @param alpha first item
     * @param omega last item
     * @return true if the last item is null
     */
    public static boolean omegaIsNull(Object alpha, Object omega) {
        boolean eval = false;
        if (alpha == null) {

            eval = false;
        }

        if (alpha != null && omega == null) {

            eval = true;
        }

        if ((alpha != null & !alpha.equals("") & !alpha.equals(" ")) && (omega == null || omega.equals("") || omega.equals(" "))) {

            eval = true;
        }



        return eval;
    }

    /**
     *
     * @param collection list of items
     * @param last the last item in the list
     * @return true if the item is the last in the list
     */
    public static boolean lastInList(List<Object> collection, Object last) {
        boolean eval = false;
        LinkedList<Object> list = new LinkedList<Object>(collection);
        if (last.equals(list.getLast())) {
            eval = true;
        }
        return eval;
    }

    public static String escapeHTML(String value) {

        String text = HtmlUtility.cleanText(value);
        return text;
    }
    
  
}