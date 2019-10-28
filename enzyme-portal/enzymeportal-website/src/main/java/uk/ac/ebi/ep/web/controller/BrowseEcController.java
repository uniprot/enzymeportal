package uk.ac.ebi.ep.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.ebi.ep.web.ec.classification.EnzymeEntry;
import uk.ac.ebi.ep.web.ec.classification.EnzymeSubSubclass;
import uk.ac.ebi.ep.web.ec.classification.EnzymeSubclass;
import uk.ac.ebi.ep.web.ec.classification.IntenzEnzyme;

/**
 *
 * @author joseph
 */
@Slf4j
@Controller
public class BrowseEcController {

    //concrete jsp's
    private static final String BROWSE_ENZYMES = "/browse-enzymes";
    private static final String EC = "/ec";

    //abtract url
    private static final String BROWSE_ENZYME_CLASSIFICATION = "/browse/enzymes";
    private static final String BROWSE_EC = "/browse/enzyme";

    private static final String EC_NUMBER = "ec";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String SUBCLASSES = "subclasses";
    private static final String SUBSUBCLASSES = "subsubclasses";
    private static final String ENTRIES = "entries";
    private static final String INTENZ_URL = "https://www.ebi.ac.uk/intenz/ws/EC";
    private static final String ROOT = "ROOT";
    private static final String SUBCLASS = "SUBCLASS";
    private static final String SUBSUBCLASS = "SUBSUBCLASS";
    private static final String SELECTED_EC = "selectedEc";

    @GetMapping(value = BROWSE_ENZYME_CLASSIFICATION)
    public String browseEc(Model model, HttpSession session) {
        clearSelectedEc(session);

        return BROWSE_ENZYMES;
    }

    @GetMapping(value = BROWSE_EC + "/{ec}/{ecname}")
    public String showStaticEc(@PathVariable("ec") String ec, @PathVariable("ecname") String ecname,
            Model model, HttpSession session, HttpServletRequest request) throws MalformedURLException, IOException {
        clearSelectedEc(session);
        browseEc(model, session, ecname, null, null, null, ec);
        return EC;

    }

    @GetMapping(value = BROWSE_EC)
    public String browseEcTree(@RequestParam(value = "ec", required = false) String ec, @RequestParam(value = "ecname", required = false) String ecname,
            @RequestParam(value = "subecname", required = false) String subecname,
            @RequestParam(value = "subsubecname", required = false) String subsubecname,
            @RequestParam(value = "entryecname", required = false) String entryecname, Model model, HttpSession session, HttpServletRequest request, Pageable pageable, RedirectAttributes attributes) throws MalformedURLException, IOException {

        browseEc(model, session, ecname, subecname, subsubecname, entryecname, ec);
        return EC;
    }

    private void browseEc(Model model, HttpSession session, String ecname, String sub_ecname, String subsub_ecname, String entry_ecname, String ec) throws MalformedURLException, IOException {

        String intenz_url = String.format("%s/%s.json", INTENZ_URL, ec);
        URL url = new URL(intenz_url);
        try (InputStream is = url.openStream();
                JsonReader rdr = Json.createReader(is)) {

            computeJsonData(rdr, model, session, ecname, sub_ecname, subsub_ecname, entry_ecname, ec);
        }
    }

    /**
     * This method keeps track of the selected enzymes in their hierarchy for
     * the browse enzyme
     *
     * @param session
     * @param s the selected enzyme
     * @param type the position in the hierarchy
     */
    private void addToSelectedEc(HttpSession session, IntenzEnzyme s, String type) {
        @SuppressWarnings("unchecked")
        LinkedList<IntenzEnzyme> history = (LinkedList<IntenzEnzyme>) session.getAttribute(SELECTED_EC);

        if (history == null) {

            history = new LinkedList<>();
            session.setAttribute(SELECTED_EC, history);
        }

        if (!history.isEmpty() && history.contains(s)) {

            if (type.equalsIgnoreCase(ROOT) && history.size() == 2) {
                history.removeLast();

            }
            if (type.equalsIgnoreCase(ROOT) && history.size() == 3) {
                history.removeLast();
                history.removeLast();

            }
            if (type.equalsIgnoreCase(SUBCLASS) && history.size() == 2) {
                history.removeLast();
                history.add(s);

            }
            if (type.equalsIgnoreCase(SUBCLASS) && history.size() == 3) {
                history.removeLast();

            }

        } else if ((history.isEmpty() || !history.contains(s)) && (history.size() < 3)) {
            history.add(s);

        }
    }

    private void clearSelectedEc(HttpSession session) {
        @SuppressWarnings("unchecked")
        LinkedList<IntenzEnzyme> history = (LinkedList<IntenzEnzyme>) session.getAttribute(SELECTED_EC);
        if (history == null) {
            history = new LinkedList<>();
            session.setAttribute(SELECTED_EC, history);
        } else {
            history.clear();
        }
    }

    private void computeJsonData(JsonReader jsonReader, Model model, HttpSession session, String... ecname) {
        JsonObject jsonObject = jsonReader.readObject();

        IntenzEnzyme root = new IntenzEnzyme();

        String ec = jsonObject.getString(EC_NUMBER);
        //String name = jsonObject.getString(NAME);
        String description = null;

        if (jsonObject.containsKey(DESCRIPTION)) {
            description = jsonObject.getString(DESCRIPTION);

            root.setDescription(description);
        }
        root.setEc(ec);
        root.setName(ecname[0]);
        root.setSubclassName(ecname[1]);
        root.setSubsubclassName(ecname[2]);
        root.setEntryName(ecname[3]);

        //compute the childObject
        if (jsonObject.containsKey(SUBCLASSES)) {

            JsonArray jsonArray = jsonObject.getJsonArray(SUBCLASSES);

            for (JsonObject childObject : jsonArray.getValuesAs(JsonObject.class)) {
                String _ec = null;
                String _name = null;
                String _desc = null;
                _ec = childObject.getString(EC_NUMBER);
                _name = childObject.getString(NAME);

                EnzymeSubclass subclass = new EnzymeSubclass();

                if (childObject.containsKey(DESCRIPTION)) {
                    _desc = childObject.getString(DESCRIPTION);
                    subclass.setDescription(_desc);
                }

                subclass.setEc(_ec);
                subclass.setName(_name);
                root.getChildren().add(subclass);

            }
            addToSelectedEc(session, root, ROOT);
            model.addAttribute("json", root);
        }
        if (jsonObject.containsKey(SUBSUBCLASSES)) {

            JsonArray jsonArray = jsonObject.getJsonArray(SUBSUBCLASSES);

            for (JsonObject childObject : jsonArray.getValuesAs(JsonObject.class)) {
                String _ec = null;
                String _name = null;
                String _desc = null;
                _ec = childObject.getString(EC_NUMBER);
                _name = childObject.getString(NAME);

                EnzymeSubSubclass subsubclass = new EnzymeSubSubclass();

                if (childObject.containsKey(DESCRIPTION)) {
                    _desc = childObject.getString(DESCRIPTION);

                    subsubclass.setDescription(_desc);
                }

                subsubclass.setEc(_ec);
                subsubclass.setName(_name);

                root.getSubSubclasses().add(subsubclass);

            }

            model.addAttribute("json", root);
            addToSelectedEc(session, root, SUBCLASS);
        }
        if (jsonObject.containsKey(ENTRIES)) {

            JsonArray jsonArray = jsonObject.getJsonArray(ENTRIES);

            for (JsonObject childObject : jsonArray.getValuesAs(JsonObject.class)) {
                String _ec = null;
                String _name = null;
                String _desc = null;
                _ec = childObject.getString(EC_NUMBER);
                _name = childObject.getString(NAME);

                EnzymeEntry entries = new EnzymeEntry();
                if (childObject.containsKey(DESCRIPTION)) {
                    _desc = childObject.getString(DESCRIPTION);

                    entries.setDescription(_desc);
                }

                entries.setEc(_ec);
                entries.setName(_name);
                root.setEc(ecname[4]);
                root.getEntries().add(entries);

            }

            model.addAttribute("json", root);
            addToSelectedEc(session, root, SUBSUBCLASS);
        }

    }

}
