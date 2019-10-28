package uk.ac.ebi.ep.web.controller;

import java.util.LinkedList;
import javax.servlet.http.HttpSession;
import uk.ac.ebi.ep.web.tools.Attribute;
import uk.ac.ebi.ep.web.utils.HtmlUtility;

/**
 *
 * @author joseph
 */
public class LegacyMethods {

    protected void clearHistory(HttpSession session) {
        @SuppressWarnings("unchecked")
        LinkedList<String> history = (LinkedList<String>) session.getAttribute(Attribute.history.getName());
        if (history == null) {
            history = new LinkedList<>();
            session.setAttribute(Attribute.history.getName(), history);
        } else {
            history.clear();
        }
    }

    protected void addToHistory(HttpSession session, String s) {
        @SuppressWarnings("unchecked")
        LinkedList<String> history = (LinkedList<String>) session.getAttribute(Attribute.history.getName());
        if (history == null) {
            history = new LinkedList<>();
            session.setAttribute(Attribute.history.getName(), history);
        }
        if (history.isEmpty() || !history.get(history.size() - 1).equals(s)) {
            String cleanedText = HtmlUtility.cleanText(s);
            history.add(cleanedText);
        }

        if (history.size() > 10) {
            history.clear();
        }
    }

}
