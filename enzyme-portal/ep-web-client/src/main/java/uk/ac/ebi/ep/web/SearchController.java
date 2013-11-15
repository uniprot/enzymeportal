package uk.ac.ebi.ep.web;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.biobabel.blast.NcbiBlastClient;
import uk.ac.ebi.biobabel.blast.NcbiBlastClientException;
import uk.ac.ebi.biobabel.util.collections.ChemicalNameComparator;
import uk.ac.ebi.ep.adapter.bioportal.BioportalConfig;
import uk.ac.ebi.ep.adapter.chebi.ChebiConfig;
import uk.ac.ebi.ep.adapter.ebeye.EbeyeConfig;
import uk.ac.ebi.ep.adapter.intenz.IntenzConfig;
import uk.ac.ebi.ep.adapter.literature.CitexploreWSClientPool;
import uk.ac.ebi.ep.adapter.literature.LiteratureConfig;
import uk.ac.ebi.ep.adapter.reactome.ReactomeConfig;
import uk.ac.ebi.ep.adapter.uniprot.UniprotConfig;
import uk.ac.ebi.ep.core.filter.CompoundsPredicate;
import uk.ac.ebi.ep.core.filter.DiseasesPredicate;
import uk.ac.ebi.ep.core.filter.SpeciesPredicate;
import uk.ac.ebi.ep.core.search.Config;
import uk.ac.ebi.ep.core.search.EnzymeFinder;
import uk.ac.ebi.ep.core.search.EnzymeRetriever;
import uk.ac.ebi.ep.core.search.HtmlUtility;
import uk.ac.ebi.ep.entry.Field;
import uk.ac.ebi.ep.enzyme.model.*;
import uk.ac.ebi.ep.enzyme.model.Disease;
import uk.ac.ebi.ep.mm.Entry;
import uk.ac.ebi.ep.search.exception.EnzymeFinderException;
import uk.ac.ebi.ep.search.model.*;
import uk.ac.ebi.ep.search.result.Pagination;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

/**
 *
 * @since 1.0
 * @version $LastChangedRevision$ <br/> $LastChangedDate: 2012-03-26 11:53:57
 * +0100 (Mon, 26 Mar 2012) $ <br/> $Author$
 * @author $Author$
 */
@Controller
public class SearchController {

    private static final Logger LOGGER = Logger.getLogger(SearchController.class);
    private static final String ENZYME_MODEL = "enzymeModel";
    private static final String ERROR = "error";

    private enum ResponsePage {

        ENTRY, ERROR;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
    @Autowired
    private EbeyeConfig ebeyeConfig;
    @Autowired
    private UniprotConfig uniprotConfig;
    @Autowired
    private Config searchConfig;
    @Autowired
    private IntenzConfig intenzConfig;
    @Autowired
    private ReactomeConfig reactomeConfig;
    @Autowired
    private ChebiConfig chebiConfig;
    @Autowired
    private LiteratureConfig literatureConfig;
    @Autowired
    private BioportalConfig bioportalConfig;
    private Boolean isCustomSearch = false;
    private String pathVariable;
    //private IntenzAdapter intenzAdapter;

    @PostConstruct
    public void init() {
        try {
            CitexploreWSClientPool.setSize(
                    literatureConfig.getCitexploreClientPoolSize());
            LOGGER.info("CiteXplore client pool size set successfuly");
        } catch (Exception e) {
            LOGGER.error("Unable to set CiteXplore client pool size", e);
        }
    }

    /**
     * Process the entry page,
     *
     * @param accession The UniProt accession of the enzyme.
     * @param field the requested tab.
     * @param model
     * @return
     */
    @RequestMapping(value = "/search/{accession}/{field}")
    protected String getEnzymeModel(Model model,
            @PathVariable String accession, @PathVariable String field,
            HttpSession session) {
        Field requestedField = Field.valueOf(field);
        EnzymeRetriever retriever = new EnzymeRetriever(searchConfig);
        retriever.getEbeyeAdapter().setConfig(ebeyeConfig);
        retriever.getUniprotAdapter().setConfig(uniprotConfig);
        retriever.getIntenzAdapter().setConfig(intenzConfig);
        EnzymeModel enzymeModel = null;
        String responsePage = ResponsePage.ENTRY.toString();
        try {
            switch (requestedField) {
                case proteinStructure:
                    enzymeModel = retriever.getProteinStructure(accession);
                    break;
                case reactionsPathways:
                    retriever.getReactomeAdapter().setConfig(reactomeConfig);
                    enzymeModel = retriever.getReactionsPathways(accession);
                    break;
                case molecules:
                    retriever.getChebiAdapter().setConfig(chebiConfig);
                    enzymeModel = retriever.getMolecules(accession);
                    break;
                case diseaseDrugs:
                    retriever.getBioportalAdapter().setConfig(bioportalConfig);
                    enzymeModel = retriever.getDiseases(accession);
                    break;
                case literature:
                    retriever.getLiteratureAdapter().setConfig(literatureConfig);
                    enzymeModel = retriever.getLiterature(accession);
                    break;
                default:
                    enzymeModel = retriever.getEnzyme(accession);
                    requestedField = Field.enzyme;
                    break;
            }
            enzymeModel.setRequestedfield(requestedField.name());
            model.addAttribute(ENZYME_MODEL, enzymeModel);
            addToHistory(session, accession);
        } catch (Exception ex) {
            // FIXME: this is an odd job to signal an error for the JSP!
            LOGGER.error("Unable to retrieve the entry!", ex);
            if (requestedField.getName().equalsIgnoreCase(Field.diseaseDrugs.getName())) {
                enzymeModel = new EnzymeModel();
                enzymeModel.setName("Diseases");
                enzymeModel.setRequestedfield(requestedField.name());
                Disease d = new Disease();
                d.setName(ERROR);
                enzymeModel.getDisease().add(0, d);
                model.addAttribute("enzymeModel", enzymeModel);
                LOGGER.fatal("Error in retrieving Disease Information");
            }
            if (requestedField.getName().equalsIgnoreCase(Field.molecules.getName())) {
                enzymeModel = new EnzymeModel();
                enzymeModel.setRequestedfield(requestedField.getName());
                enzymeModel.setName("Small Molecues");
                Molecule molecule = new Molecule();
                molecule.setName(ERROR);
                ChemicalEntity chemicalEntity = new ChemicalEntity();
                chemicalEntity.setDrugs(new CountableMolecules());
                chemicalEntity.getDrugs().setMolecule(new ArrayList<Molecule>());
                chemicalEntity.getDrugs().getMolecule().add(molecule);
                enzymeModel.setMolecule(chemicalEntity);
                model.addAttribute(ENZYME_MODEL, enzymeModel);
                LOGGER.fatal("Error in retrieving Molecules Information");
            }
            if (requestedField.getName().equalsIgnoreCase(Field.enzyme.getName())) {

                enzymeModel = new EnzymeModel();
                enzymeModel.setRequestedfield(requestedField.getName());
                enzymeModel.setName("Enzymes");
                Enzyme enzyme = new Enzyme();
                enzyme.getEnzymetype().add(0, ERROR);
                enzymeModel.setEnzyme(enzyme);

                model.addAttribute(ENZYME_MODEL, enzymeModel);
                LOGGER.fatal("Error in retrieving Enzymes");
            }
            if (requestedField.getName().equalsIgnoreCase(Field.proteinStructure.getName())) {
                enzymeModel = new EnzymeModel();
                enzymeModel.setRequestedfield(requestedField.getName());
                enzymeModel.setName("Protein Structures");
                ProteinStructure structure = new ProteinStructure();
                structure.setName(ERROR);
                enzymeModel.getProteinstructure().add(0, structure);

                model.addAttribute(ENZYME_MODEL, enzymeModel);
                LOGGER.fatal("Error in retrieving ProteinStructure");
            }
            if (requestedField.getName().equalsIgnoreCase(Field.reactionsPathways.getName())) {
                enzymeModel = new EnzymeModel();

                enzymeModel.setRequestedfield(requestedField.getName());
                enzymeModel.setName("Reactions and Pathways");
                ReactionPathway pathway = new ReactionPathway();
                EnzymeReaction reaction = new EnzymeReaction();
                reaction.setName(ERROR);
                pathway.setReaction(reaction);
                enzymeModel.getReactionpathway().add(0, pathway);


                model.addAttribute(ENZYME_MODEL, enzymeModel);
                LOGGER.fatal("Error in retrieving Reaction Pathways");

            }
            if (requestedField.getName().equalsIgnoreCase(Field.literature.getName())) {
                enzymeModel = new EnzymeModel();
                enzymeModel.setRequestedfield(requestedField.getName());
                enzymeModel.setName("Literatures");

                enzymeModel.getLiterature().add(0, ERROR);

                model.addAttribute(ENZYME_MODEL, enzymeModel);
                LOGGER.fatal("Error in retrieving Literature Information");

            }

        } finally {
            retriever.closeResources();
        }
        return responsePage;
    }

    /**
     * This method is an entry point that accepts the request when the search
     * home page is loaded. It then forwards the request to the search page.
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/")
    public String viewSearchHome(Model model, HttpSession session) {
        SearchModel searchModelForm = new SearchModel();
        SearchParams searchParams = new SearchParams();
        //searchParams.setText("Enter a name to search");
        searchParams.setStart(0);
        searchModelForm.setSearchparams(searchParams);
        model.addAttribute("searchModel", searchModelForm);
        clearHistory(session);
        return "index";
    }

    @ModelAttribute("/about")
    public SearchModel getabout(Model model) {
        SearchModel searchModelForm = searchform();
        model.addAttribute("searchModel", searchModelForm);
        return new SearchModel();
    }

    private SearchModel customSearch(@ModelAttribute("searchModel") SearchModel searchModel, @PathVariable("id") String id, Model model, HttpSession session) {


        EnzymeFinder finder = new EnzymeFinder(searchConfig);
        finder.getUniprotAdapter().setConfig(uniprotConfig);
        finder.getIntenzAdapter().setConfig(intenzConfig);
        System.out.println("custom search called " + id);

        //Entry entry = finder.findEntryById("1.1.1.1");//get the entry (ec)
        Entry entry = finder.findEntryById(id);//get the entry (disease)
        System.out.println("The entry " + entry.getEntryId());

        List<String> ids = finder.getXrefs(entry);//obtain uniprot ids
        System.out.println("uni Ids " + ids.size());

        SearchParams searchParams = new SearchParams();



        searchParams.setText(entry.getEntryName());
        searchParams.setType(SearchParams.SearchType.KEYWORD);
        searchParams.setStart(0);
        searchParams.setPrevioustext(entry.getEntryName());

        addToHistory(session, "searchparams.text=" + entry.getEntryName());




        finder.setSearchParams(searchParams);

        SearchResults results = finder.computeEnzymeSummary(ids, new SearchResults());
        System.out.println("final results " + results.getTotalfound());

        searchModel.setSearchparams(searchParams);

        searchModel.setSearchresults(results);

        return searchModel;
    }
    private Set<uk.ac.ebi.ep.search.model.Disease> diseaseList = new TreeSet<uk.ac.ebi.ep.search.model.Disease>();
    private static final Comparator<String> NAME_COMPARATOR = new ChemicalNameComparator();
    static final Comparator<uk.ac.ebi.ep.search.model.Disease> SORT_DISEASES = new Comparator<uk.ac.ebi.ep.search.model.Disease>() {
        @Override
        public int compare(uk.ac.ebi.ep.search.model.Disease d1, uk.ac.ebi.ep.search.model.Disease d2) {

            if (d1.getName() == null && d2.getName() == null) {

                return NAME_COMPARATOR.compare(d1.getName(), d2.getName());
            }
            int compare = NAME_COMPARATOR.compare(d1.getName(), d2.getName());

            return ((compare == 0) ? NAME_COMPARATOR.compare(d1.getName(), d2.getName()) : compare);

        }
    };

    @RequestMapping(value = "/browse", method = RequestMethod.GET)
    public String showDiseases(Model model) {
        EnzymeFinder finder = new EnzymeFinder(searchConfig);

        diseaseList = finder.findAllDiseases();


        SearchModel searchModelForm = searchform();
        model.addAttribute("searchModel", searchModelForm);
        model.addAttribute("diseaseList", diseaseList);

        return "browse";
    }
    /**
     * Facilitates communication with the Intenz WebService; autowired by Spring
     */
    //@Autowired
    protected RestTemplate restTemplate;
    /**
     * The base URL of the Intenz web service - should be configurable
     */
    //private final static String intenzServiceUrl = "http://www.ebi.ac.uk/intenz/ws/EC/1.1.1.1";
    private final static String intenzServiceUrl = "http://www.ebi.ac.uk/intenz/ws/EC/.json";
    //private final static String intenzServiceUrl  = "http://www.ebi.ac.uk/intenz/ws/EC/?format=json";
    // private final static String intenzServiceUrl = "http://www.ebi.ac.uk/intenz/ws/EC/1.1.1.1?format=json";
    //private final static String intenzServiceUrl = "http://www.ebi.ac.uk/intenz/ws/EC/1.1.1.1.json";

//       @RequestMapping(value = "/browseEcNumber/{id}", method = RequestMethod.GET)
//    public void showEcNumberWithParams(@PathVariable String id ,Model model) throws MalformedURLException, IOException{
//           System.out.println("JSON ID "+ id);
//            URL oracle = new URL("http://www.ebi.ac.uk/intenz/ws/EC/"+id+".json");
//        // URL oracle = new URL("http://www.ebi.ac.uk/intenz/ws/EC/1.1.json");
//        URLConnection yc = oracle.openConnection();
//        yc.setRequestProperty("Content-Type", "application/json");
//        
//        BufferedReader in = new BufferedReader(new InputStreamReader(
//                yc.getInputStream()));
//
//        String inputLine;
//        while ((inputLine = in.readLine()) != null) {
//            System.out.println(inputLine);
//         
//            model.addAttribute("jfile", inputLine);
//
//        }
//           
//          
//       }
//    @RequestMapping(value = "/browseEcNumber", method = RequestMethod.POST)
//    public @ResponseBody
//    JsonArray showEcNumber(Model model, @RequestParam("svc") String svc) throws IOException, JSONException {
//        URL url = new URL("http://www.ebi.ac.uk/intenz/ws/EC/" + svc + ".json");
//        System.out.println("json param " + svc);
//        System.out.println("AJAX CALL POST ");
//// Retrieving byte stream object from URL source
//        InputStream is = url.openStream();
//
//        // Creating JSON object model from stream
//        JsonArray arr = Json.createReader(is).readArray();
//
//        JsonArray jsonData = computeJsonArray(arr, null);
//
//
//        //JsonObject obj = Json.createReader(is).readObject();
//        //s.navigateTree(obj, null);
//        //s.parserApp(is);
//
//        //System.out.println("obj returned " + jsonData);
//        model.addAttribute("jfile", jsonData);
//
//
//        // return "browseEcNumber";
//        //return showEcNumberGET(model);
//        return jsonData;
//    }
    @RequestMapping(value = "/browseEcNumber", method = RequestMethod.GET)
    public String showEcNumberGET(Model model, HttpServletRequest request) throws IOException, JSONException {
        //URL url = new URL("http://www.ebi.ac.uk/intenz/ws/EC/.json");

        String param = request.getParameter("svc");
        System.out.println("PARAMETER " + param);

// Retrieving byte stream object from URL source
        //InputStream is = url.openStream();
        JsonArray jsonData = null;
//        if (param != null) {
//            URL url = new URL("http://www.ebi.ac.uk/intenz/ws/EC/" + param + ".json");
//            InputStream is = url.openStream();
//            JsonObject obj = Json.createReader(is).readObject();
//            jsonData = computeJsonObject(obj, param);
//        } 

        URL url = new URL("http://www.ebi.ac.uk/intenz/ws/EC/.json");
        InputStream is = url.openStream();
        JsonArray arr = Json.createReader(is).readArray();

        jsonData = computeJsonArray(arr, null);


        model.addAttribute("jfile", jsonData);
       

        //delete later
        EnzymeFinder finder = new EnzymeFinder(searchConfig);
        finder.getUniprotAdapter().setConfig(uniprotConfig);
        finder.getIntenzAdapter().setConfig(intenzConfig);
        List<String> ecNumbers = finder.findAllEcNumbers();
        //model.addAttribute("ecNumbers", ecNumbers);

        return "browseEcNumber";
        //return jsonData;
    }

    @RequestMapping(value = "/browseEcNumberJson", method = RequestMethod.GET)
    //public String showEcNumberGET(Model model, HttpServletRequest request) throws IOException, JSONException {
    public @ResponseBody
    Object ajaxJsonResponse(@ModelAttribute("searchModel") SearchModel searchModel,Model model, HttpServletRequest request,HttpSession session) throws IOException, JSONException, ParseException {
        //URL url = new URL("http://www.ebi.ac.uk/intenz/ws/EC/.json");

        String param = request.getParameter("svc");
 

        if (param != null && param.length() < 7) {
            System.out.println("ajax call to intenz  "+ param);
            URL url = new URL("http://www.ebi.ac.uk/intenz/ws/EC/" + param + ".json");
            InputStream is = url.openStream();
            JsonObject obj = Json.createReader(is).readObject();


            JsonArray jsonData = computeJsonObject(obj, param);
            //jsonData = computeJsonArray(obj, null);
            JSONParser parser = new JSONParser();
            Object o = parser.parse(jsonData.toString());

              //return jsonData;
            return o;
        }else{
            customSearch(searchModel, param, model, session);
        }
        
//        else {
//            URL url = new URL("http://www.ebi.ac.uk/intenz/ws/EC/.json");
//            InputStream is = url.openStream();
//            JsonArray arr = Json.createReader(is).readArray();
//
//            JsonArray jsonData = computeJsonArray(arr, null);
//            System.out.println("Main json AJAX" + jsonData);
//            //return jsonData;
//            return null;
//        }



        //return jsonData;
        return null;
    }

    private JsonArray computeJsonObject(JsonValue tree, String key) throws JSONException {

        JsonArrayBuilder root_array = Json.createArrayBuilder();

        //JsonArray rootArray = (JsonArray) tree;
        //List<JsonObject> obj = rootArray.getValuesAs(JsonObject.class);
        JsonObject obj = (JsonObject) tree;

        JsonObjectBuilder root = Json.createObjectBuilder();
        //JSONObject jroot = new JSONObject();
        //JSONArray jList = new JSONArray();

        String ec = "ec";
        String name = "name";
        String desc = "description";


        System.out.println("child objects called "+ key);

//        if (obj.containsKey(ec)) {
//            String _ec = obj.getString(ec);
//            String _name = obj.getString(name);
//            //root.add("data", _ec);//jsTree
//             
//            root.add("label", "ec " + _ec +" "+_name);//jqTree
//
//            root.add("id", _ec);//jqTree
//            //jroot.put("label", ec);
//
//
//
//        }
        if (obj.containsKey(name)) {
            String _name = obj.getString(name);
            //root.add("data", _ec);//jsTree
            root.add("label", _name);//jqTree


        }

        //JsonObjectBuilder rootObj = editJsonData(obj, ec, name, desc);
        //root_array.add(rootObj);


        if (obj.containsKey("subsubclasses")) {
            JsonArrayBuilder childArray = Json.createArrayBuilder();
            

            JsonArray arr = obj.getJsonArray("subsubclasses");
            List<JsonObject> childList = arr.getValuesAs(JsonObject.class);
            for (JsonObject children : childList) {


                JsonObjectBuilder childObj = editJsonData(children, ec, name, desc);
                childArray.add(childObj);
                
            }
        
            root.add("children", childArray);
            //jroot.put("children", childArray);


        } else if (obj.containsKey("entries")) {
           
            JsonArrayBuilder childArray = Json.createArrayBuilder();
            JsonArray arr = obj.getJsonArray("entries");
            List<JsonObject> childList = arr.getValuesAs(JsonObject.class);
            for (JsonObject children : childList) {

                JsonObjectBuilder childObj = editJsonData(children, ec, name, desc);
                childArray.add(childObj);
            }

            root.add("children", childArray);


        } else if (obj.containsKey("entries") && obj.containsKey("subsubclasses")) {
            //System.out.println("make a request to mega mapper");
        }

        root_array.add(root);

        //System.out.println(" " + root_array.build());
        return root_array.build();
        //return jroot;
    }

    private JsonArray computeJsonArray(JsonValue tree, String key) {
        JsonArrayBuilder root_array = Json.createArrayBuilder();

        if (key != null) {
            //System.out.print("Key " + key + ": ");

        }
        JsonArray rootArray = (JsonArray) tree;

        List<JsonObject> jsonObject = rootArray.getValuesAs(JsonObject.class);

        JsonObjectBuilder root = Json.createObjectBuilder();
        String ec = "ec";
        String name = "name";
        String desc = "description";

        for (JsonObject obj : jsonObject) {

//            JsonObject rootObj = editJsonData(obj, ec, name, desc);
//            root_array.add(rootObj);

            if (obj.containsKey(ec)) {
                String _ec = obj.getString(ec);
                //root.add("data", _ec);//jsTree
                
                root.add("label", "ec "+ _ec +" - "+obj.getString(name));//jqTree
                root.add("id", _ec);//jqTree
                //System.out.println("first object "+ obj);

            }

            if (obj.containsKey("subclasses")) {
                
                JsonArrayBuilder childArray = Json.createArrayBuilder();

                JsonArray arr = obj.getJsonArray("subclasses");
                List<JsonObject> childList = arr.getValuesAs(JsonObject.class);
                for (JsonObject children : childList) {


                    JsonObjectBuilder childObj = editJsonData(children, ec, name, desc);
                    childArray.add(childObj);
                }

                root.add("children", childArray);

                //root.add("load_on_demand", true);//jqTree

            }


            root_array.add(root);

        }
        //System.out.println(" " + root_array.build());

        return root_array.build();


    }

    private JsonObjectBuilder editJsonData(JsonObject jsonObject, String ec, String name, String desc) {
        JsonObjectBuilder childObj = Json.createObjectBuilder();

        if (jsonObject.containsKey(ec)) {
            String _ec = jsonObject.getString(ec);
            //childObj.add("data", _ec);//jsTree
            childObj.add("label", "ec " + _ec);//jqTree
            //childObj.add("label", "ec " + _ec +" - "+jsonObject.getString(name) );//jqTree
            //+" - "+ jsonObject.getString(name)

        }
        //for JqTree, add an id
        if (jsonObject.containsKey(ec)) {
            String _ec = jsonObject.getString(ec);
            //childObj.add("data", _ec);//jsTree
            childObj.add("id", _ec);//jqTree

        }
        if (jsonObject.containsKey(name)) {
            String title = jsonObject.getString(name);
            //childObj.add("title", title);//jsTree
            childObj.add("title", title);//jqTree

        }
        if (jsonObject.containsKey(desc)) {
            String description = jsonObject.getString(desc);
            childObj.add("desc", description);

        }

        //return childObj.build();
        return childObj;
    }

    private boolean startsWithDigit(String data) {
        return Character.isDigit(data.charAt(0));
    }

    @RequestMapping(value = "/browse/{startsWith}", method = RequestMethod.GET)
    public String showDiseasesLike(@PathVariable("startsWith") String startsWith, Model model) {

        Set<uk.ac.ebi.ep.search.model.Disease> selectedDiseases = new TreeSet<>(SORT_DISEASES);

        for (uk.ac.ebi.ep.search.model.Disease disease : diseaseList) {
            if (disease.getName().startsWith(startsWith.toLowerCase())) {
                selectedDiseases.add(disease);
            }
            if (startsWithDigit(disease.getName())) {
                String current = disease.getName().replaceAll("(-)?\\d+(\\-\\d*)?", "").trim();
                if (current.startsWith(startsWith.toLowerCase())) {
                    selectedDiseases.add(disease);
                }
            }
        }


        SearchModel searchModelForm = searchform();
        model.addAttribute("searchModel", searchModelForm);

        model.addAttribute("alldiseaseList", selectedDiseases);
        model.addAttribute("startsWith", startsWith.toUpperCase());

        return "browse";
    }

    @RequestMapping(value = "/faq")
    public SearchModel getfaq(Model model) {

        SearchModel searchModelForm = searchform();
        model.addAttribute("searchModel", searchModelForm);
        return searchModelForm;

    }

    @RequestMapping(value = "/search")
    public SearchModel getSearch(Model model) {
        SearchModel searchModelForm = searchform();
        model.addAttribute("searchModel", searchModelForm);
        return searchModelForm;
    }

    @ModelAttribute("searchModelForm")
    public SearchModel searchform() {
        SearchModel searchModelForm = new SearchModel();
        SearchParams searchParams = new SearchParams();
        //searchParams.setText("Enter a name to search");
        searchParams.setStart(0);
        searchModelForm.setSearchparams(searchParams);
        return searchModelForm;
    }

    private void resetSelectedSpecies(List<Species> speciesList) {
        for (Species sp : speciesList) {

            sp.setSelected(false);

        }
    }

    private void resetSelectedCompounds(List<Compound> compounds) {
        for (Compound compound : compounds) {
            compound.setSelected(false);
        }
    }

    private void resetSelectedDisease(List<uk.ac.ebi.ep.search.model.Disease> diseases) {
        for (uk.ac.ebi.ep.search.model.Disease disease : diseases) {
            disease.setSelected(false);
        }
    }

//    @RequestMapping(value = "/underconstruction", method = RequestMethod.GET)
//    public String getSearchResult(Model model) {
//        return "underconstruction";
//    }
//
    private void addToHistory(HttpSession session, String s) {
        @SuppressWarnings("unchecked")
        //List<String> history = (List<String>) session.getAttribute("history");
        LinkedList<String> history = (LinkedList<String>) session.getAttribute("history");
        if (history == null) {
            //history = new ArrayList<String>();
            history = new LinkedList<String>();
            session.setAttribute("history", history);
        }
        if (history.isEmpty() || !history.get(history.size() - 1).equals(s)) {
            String cleanedText = HtmlUtility.cleanText(s);
            history.add(cleanedText);
        }
    }

    private void clearHistory(HttpSession session) {
        @SuppressWarnings("unchecked")
        // List<String> history = (List<String>) session.getAttribute("history");
        LinkedList<String> history = (LinkedList<String>) session.getAttribute("history");
        if (history == null) {
            //history = new ArrayList<String>();
            history = new LinkedList<String>();
            session.setAttribute("history", history);
        } else {
            history.clear();
        }
    }

    /**
     * Processes the search request. When user enters a search text and presses
     * the submit button the request is processed here.
     *
     * @param searchModel
     * @param model
     * @param session
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String postSearchResult(SearchModel searchModel, Model model,
            HttpSession session) {
        String view = "error";

        String searchKey = null;
        SearchResults results = null;

        boolean custom = getIsCustomSearch();
        try {

            if (custom == true) {
                results = searchModel.getSearchresults();


                if (results == null) {



                    String id = getPathVariable();

                    clearHistory(session);
                    searchModel = customSearch(searchModel, id, model, session);
                    results = searchModel.getSearchresults();
                    model.addAttribute("searchModel", searchModel);
                    model.addAttribute("pagination", getPagination(searchModel));



                    searchKey = getSearchKey(searchModel.getSearchparams());
                    cacheSearch(session.getServletContext(), searchKey, results);
                    addToHistory(session, "searchparams.text=" + searchKey);

                    setIsCustomSearch(false);
                    view = "search";
                }
                if (results != null) {
                    searchModel.setSearchresults(results);

                    applyFilters(searchModel);
                    model.addAttribute("searchModel", searchModel);
                    model.addAttribute("pagination", getPagination(searchModel));
                    clearHistory(session);
                    if (searchModel.getSearchparams().getType().equals(SearchParams.SearchType.KEYWORD)) {
                        addToHistory(session, "searchparams.text=" + searchKey);
                    }
                    if (searchModel.getSearchparams().getType().equals(SearchParams.SearchType.SEQUENCE)) {
                        addToHistory(session, "searchparams.sequence=" + searchKey);
                    }
                    //addToHistory(session, "searchparams.text=" + searchKey);
                    view = "search";
                }
            } else if (custom == false) {
                

                // See if it is already there, perhaps we are paginating:
                Map<String, SearchResults> prevSearches =
                        getPreviousSearches(session.getServletContext());

                searchKey = getSearchKey(searchModel.getSearchparams());


                results = prevSearches.get(searchKey);

                if (results == null) {
                    // New search:
                    clearHistory(session);

                    switch (searchModel.getSearchparams().getType()) {

                        case KEYWORD:


                            results = searchKeyword(searchModel.getSearchparams());
                            cacheSearch(session.getServletContext(), searchKey, results);
                            addToHistory(session, "searchparams.text=" + searchKey);
                            break;
                        case SEQUENCE:
                            view = searchSequence(model, searchModel);
                            // cacheSearch(session.getServletContext(), searchKey, results);
                            addToHistory(session, "searchparams.sequence=" + searchKey);
                            break;
                        case COMPOUND:
                            //            	view = postCompoundSearch(model, searchModel);
                            break;
                        default:
                    }
                }
            }
            if (results != null) { // something to show
                searchModel.setSearchresults(results);
                applyFilters(searchModel);
                model.addAttribute("searchModel", searchModel);
                model.addAttribute("pagination", getPagination(searchModel));
                clearHistory(session);
                if (searchModel.getSearchparams().getType().equals(SearchParams.SearchType.KEYWORD)) {
                    addToHistory(session, "searchparams.text=" + searchKey);
                }
                if (searchModel.getSearchparams().getType().equals(SearchParams.SearchType.SEQUENCE)) {
                    addToHistory(session, "searchparams.sequence=" + searchKey);
                }
                //addToHistory(session, "searchparams.text=" + searchKey);
                view = "search";
            }
        } catch (Throwable t) {
            LOGGER.error("one of the search params (Text or Sequence is :", t);
        }

        return view;
    }

    /**
     * Searches by keyword.
     *
     * @param searchParameters the search parameters.
     * @return the search results.
     */
    private SearchResults searchKeyword(SearchParams searchParameters) {
        SearchResults results = null;
        EnzymeFinder finder = new EnzymeFinder(searchConfig);
        finder.getEbeyeAdapter().setConfig(ebeyeConfig);
        finder.getUniprotAdapter().setConfig(uniprotConfig);
        finder.getIntenzAdapter().setConfig(intenzConfig);
        try {
            results = finder.getEnzymes(searchParameters);
        } catch (EnzymeFinderException ex) {
            LOGGER.error("Unable to create the result list because an error "
                    + "has occurred in the find method! \n", ex);
        } finally {
            finder.closeResources();
        }
        return results;
    }

    /**
     * A wrapper of {@code postSearchResult} method, created to accept the
     * search request using GET.
     *
     * @param searchModel
     * @param result
     * @param model
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String getSearchResults(SearchModel searchModel, BindingResult result,
            Model model, HttpSession session) {
        return postSearchResult(searchModel, model, session);
    }

    @RequestMapping(value = "/search/{id}", method = RequestMethod.GET)
    public String getSearchResults(SearchModel searchModel, BindingResult result, @PathVariable("id") String id,
            Model model, HttpSession session) {

        setIsCustomSearch(true);
        setPathVariable(id);


        return "redirect:/search";

    }

    @RequestMapping(value = "/search/{id}/num", method = RequestMethod.GET)
    public String getSearchResultsEc(SearchModel searchModel, BindingResult result, @PathVariable("id") String id,
            Model model, HttpSession session) {

        setIsCustomSearch(true);
        setPathVariable(id);


        return "redirect:/search";

    }

    @RequestMapping(value = "/advanceSearch", method = RequestMethod.GET)
    public String getAdvanceSearch(Model model) {
        return "advanceSearch";
    }

    public String resolveSpecialCharacters(String data) {

        SpecialCharacters xchars = SpecialCharacters.getInstance(null);
        EncodingType[] encodings = {
            EncodingType.CHEBI_CODE,
            EncodingType.COMPOSED,
            EncodingType.EXTENDED_HTML,
            EncodingType.GIF,
            EncodingType.HTML,
            EncodingType.HTML_CODE,
            EncodingType.JPG,
            EncodingType.SWISSPROT_CODE,
            EncodingType.UNICODE
        };

        if (!xchars.validate(data)) {
            LOGGER.warn("SPECIAL CHARACTER PARSING ERROR : This is not a valid xchars string!" + data);

        }


        return xchars.xml2Display(data, EncodingType.CHEBI_CODE);
    }

    /**
     * Applies filters taken from the search parameters to the search results.
     *
     * @param searchModel
     */
    private void applyFilters(SearchModel searchModel) {


        if (searchModel != null) {

            SearchParams searchParameters = searchModel.getSearchparams();
            searchParameters.setSize(searchConfig.getResultsPerPage());
            SearchResults resultSet = searchModel.getSearchresults();


            final int numOfResults = resultSet.getSummaryentries().size();
            Pagination pagination = new Pagination(
                    numOfResults, searchParameters.getSize());
            pagination.setFirstResult(searchParameters.getStart());

            // Filter:
            List<String> speciesFilter = searchParameters.getSpecies();
            List<String> compoundsFilter = searchParameters.getCompounds();
            List<String> diseasesFilter = searchParameters.getDiseases();

//both from auto complete and normal selection. selected items are displayed on top the list and returns back to the orignial list when not selected.
            SearchResults searchResults = resultSet;
            List<Species> defaultSpeciesList = searchResults.getSearchfilters().getSpecies();
            resetSelectedSpecies(defaultSpeciesList);

            for (String selectedItems : searchParameters.getSpecies()) {

                for (Species theSpecies : defaultSpeciesList) {
                    if (selectedItems.equals(theSpecies.getScientificname())) {
                        theSpecies.setSelected(true);
                    }

                }
            }

            List<Compound> defaultCompoundList = searchResults.getSearchfilters().getCompounds();
            resetSelectedCompounds(defaultCompoundList);

            for (String SelectedCompounds : searchParameters.getCompounds()) {

                for (Compound theCompound : defaultCompoundList) {

                    if (SelectedCompounds.equals(theCompound.getName())) {
                        theCompound.setSelected(true);

                    }
                }
            }

            List<uk.ac.ebi.ep.search.model.Disease> defaultDiseaseList = searchResults.getSearchfilters().getDiseases();
            resetSelectedDisease(defaultDiseaseList);

            for (String selectedDisease : searchParameters.getDiseases()) {
                for (uk.ac.ebi.ep.search.model.Disease disease : defaultDiseaseList) {
                    if (selectedDisease.equals(disease.getName())) {
                        disease.setSelected(true);
                    }
                }
            }



            //if an item is seleted, then filter the list
            if (!speciesFilter.isEmpty() || !compoundsFilter.isEmpty() || !diseasesFilter.isEmpty()) {
                List<EnzymeSummary> filteredResults =
                        new LinkedList<EnzymeSummary>(resultSet.getSummaryentries());


                CollectionUtils.filter(filteredResults,
                        new SpeciesPredicate(speciesFilter));
                CollectionUtils.filter(filteredResults,
                        new CompoundsPredicate(compoundsFilter));
                CollectionUtils.filter(filteredResults,
                        new DiseasesPredicate(diseasesFilter));





                //adapting the sequece code

                // Create a new SearchResults, don't modify the one in session
                SearchResults sr = new SearchResults();

                // Update the number of results to paginate:
                pagination.setNumberOfResults(filteredResults.size());

                //model.addAttribute("pagination", pagination);
                sr.setSearchfilters(resultSet.getSearchfilters());
                sr.setSummaryentries(filteredResults);
                // show the total number of hits (w/o filtering):
                sr.setTotalfound(resultSet.getTotalfound());
                searchModel.setSearchresults(sr);

//filtering ends here

            }


        }

    }

    /**
     * Retrieves any previous searches stored in the application context.
     *
     * @param servletContext the application context.
     * @return a map of searches to results.
     */
    @SuppressWarnings("unchecked")
    private Map<String, SearchResults> getPreviousSearches(
            ServletContext servletContext) {
        Map<String, SearchResults> prevSearches = (Map<String, SearchResults>) servletContext.getAttribute("searches");
        if (prevSearches == null) {
            // Map implementation which maintains the order of access:
            prevSearches = Collections.synchronizedMap(
                    new LinkedHashMap<String, SearchResults>(
                    searchConfig.getSearchCacheSize(), 1, true));
            servletContext.setAttribute("searches", prevSearches);
        }
        return prevSearches;
    }

    /**
     * Stores a search result in the application context.
     *
     * @param servletContext the application context.
     * @param searchKey the key to use for the search results in the table.
     * @param searchResult the search results.
     */
    private void cacheSearch(ServletContext servletContext, String searchKey,
            SearchResults searchResult) {
        Map<String, SearchResults> prevSearches =
                getPreviousSearches(servletContext);
        synchronized (prevSearches) {
            while (prevSearches.size() >= searchConfig.getSearchCacheSize()) {
                // remove the eldest:
                prevSearches.remove(prevSearches.keySet().iterator().next());
            }
            prevSearches.put(searchKey, searchResult);
        }
    }

//    static final Comparator<Species> SORT_SPECIES = new Comparator<Species>() {
//
//        public int compare(Species sp1, Species sp2) {
//            
//            if (sp1.getCommonname() == null && sp2.getCommonname() == null) {
//
//                return sp1.getScientificname().compareTo(sp2.getScientificname());
//            }
//            int compare = sp1.getScientificname().compareTo(sp2.getScientificname());
//
//            return ((compare == 0) ? sp1.getScientificname().compareTo(sp2.getScientificname()) : compare);
//
//        }
//        
//    };
    @RequestMapping(value = "/underconstruction", method = RequestMethod.GET)
    public String getSearchResult(Model model) {
        return "underconstruction";
    }

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String getAbout(Model model) {
        return "about";
    }

    /**
     * Sends a search to a BLAST server. The job ID returned is stored in the
     * model as
     * <code>jobId</code> attribute.
     *
     * @param model the Spring model.
     * @param searchModel the EP search model.
     * @return the view according to the search: <code>running</code> if
     * everything went ok and the search job is running, <code>error</code>
     * otherwise.
     */
    private String searchSequence(Model model, SearchModel searchModel) {
        String view = "error";
        EnzymeFinder finder = new EnzymeFinder(searchConfig);
        try {
            String sequence = searchModel.getSearchparams().getSequence()
                    .trim().toUpperCase();
            String jobId = finder.blast(sequence);
            searchModel.getSearchparams().setPrevioustext(sequence);
            model.addAttribute("jobId", jobId);
            LOGGER.debug("BLAST job running: " + jobId);
            view = "running";
        } catch (NcbiBlastClientException e) {
            LOGGER.error(e);
        } finally {
            finder.closeResources();
        }
        return view;
    }

    @RequestMapping(value = "/checkJob", method = RequestMethod.POST)
    public String checkJob(@RequestParam String jobId, Model model,
            SearchModel searchModel, HttpSession session) {
        String view = null;
        try {
            EnzymeFinder enzymeFinder = new EnzymeFinder(searchConfig);
            enzymeFinder.getEbeyeAdapter().setConfig(ebeyeConfig);
            enzymeFinder.getUniprotAdapter().setConfig(uniprotConfig);
            enzymeFinder.getIntenzAdapter().setConfig(intenzConfig);
            NcbiBlastClient.Status status = enzymeFinder.getBlastStatus(jobId);
            switch (status) {
                case ERROR:
                case FAILURE:
                case NOT_FOUND:
                    LOGGER.error("Blast job returned status " + status);
                    view = "error";
                    break;
                case FINISHED:
                    LOGGER.debug("BLAST job finished");
                    SearchResults results = enzymeFinder.getBlastResult(jobId);
                    SearchParams searchParams = searchModel.getSearchparams();
                    String resultKey = getSearchKey(searchParams);
                    cacheSearch(session.getServletContext(), resultKey, results);
                    addToHistory(session, "searchparams.sequence=" + resultKey);
                    searchParams.setSize(searchConfig.getResultsPerPage());
                    searchModel.setSearchresults(results);
                    model.addAttribute("searchModel", searchModel);
                    model.addAttribute("pagination", getPagination(searchModel));
                    view = "search";
                    break;
                case RUNNING:
                    model.addAttribute("jobId", jobId);
                    view = "running";
                    break;
                default:
            }

        } catch (Throwable t) {
            LOGGER.error("While checking BLAST job", t);
            view = "error";
        }
        return view;
    }

    /**
     * Processes a string to normalise it to use as a key in the application
     * cache.
     *
     * @param searchParams the search parameters, including the original search
     * text from the user.
     * @return A normalised string.
     */
    private String getSearchKey(SearchParams searchParams) {

        String key = null;

        switch (searchParams.getType()) {
            case KEYWORD:
                key = searchParams.getText().trim().toLowerCase();
                break;
            case SEQUENCE:
                key = searchParams.getSequence().trim().toUpperCase()
                        .replaceAll("[\n\r]", "");
                break;
            case COMPOUND:
                throw new NotImplementedException("Compound structure search");
            default:
                key = searchParams.getText().trim().toLowerCase();
        }
        return key;
    }

    /**
     * Adds a pagination object to the model, suitable to the search results and
     * search parameters.
     *
     * @param searchModel the search model including the search parameters
     * (including pagination start).
     * @return a pagination.
     */
    private Pagination getPagination(SearchModel searchModel) {
        Pagination pagination = new Pagination(
                searchModel.getSearchresults().getSummaryentries().size(),
                searchConfig.getResultsPerPage());
        pagination.setFirstResult(searchModel.getSearchparams().getStart());
        return pagination;
    }

    public Boolean getIsCustomSearch() {
        return isCustomSearch;
    }

    public void setIsCustomSearch(Boolean isCustomSearch) {
        this.isCustomSearch = isCustomSearch;
    }

    public String getPathVariable() {
        return pathVariable;
    }

    public void setPathVariable(String pathVariable) {
        this.pathVariable = pathVariable;
    }
}
