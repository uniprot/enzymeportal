package uk.ac.ebi.ep.functions;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import uk.ac.ebi.ep.adapter.chembl.ChemblConfig;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.enzyme.model.Molecule;
import uk.ac.ebi.ep.ebeye.model.proteinGroup.ProteinGroupEntry;
import uk.ac.ebi.ep.enzymeservices.chebi.ChebiConfig;

/**
 * Due to no similar functionality in JSTL, this function was designed to help
 * various operation in a Collection.
 *
 * @author joseph
 */
public final class Functions {

    private static ChebiConfig chebiConfig;
    private static ChemblConfig chemblConfig;
    private static Map<String, String> drugbankConfig;

    public Functions() {
    }

    public static void setChebiConfig(ChebiConfig chebiConfig) {
        Functions.chebiConfig = chebiConfig;
    }

    public static void setChemblConfig(ChemblConfig chemblConfig) {
        Functions.chemblConfig = chemblConfig;
    }

    public static void setDrugbankConfig(Map<String, String> config) {
        Functions.drugbankConfig = config;
    }

    public static boolean startsWithDigit(String data) {
        return Character.isDigit(data.charAt(0));
    }

    /**
     * This function is to enable using capital letter case in checking if a
     * string starts with the letter
     *
     * @param data the original string
     * @param letter the first letter
     * @return true if the string starts with the first letter
     */
    public static boolean startsWithLowerCase(String data, String letter) {
        String current = data;
        if (startsWithDigit(data)) {
            current = data.replaceAll("(-)?\\d+(\\-\\d*)?", "").trim();

        }
        //return current.startsWith(letter.toLowerCase());
        return current.startsWith(letter);
    }

    /**
     *
     * @param collection
     * @param item
     * @return true if the item is contained in the collection
     */
    public static boolean contains(Collection collection, Object item) {
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

        if ((alpha != null && !alpha.equals("") && !alpha.equals(" ")) && (omega == null || omega.equals("") || omega.equals(" "))) {

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
        LinkedList<Object> list = new LinkedList<>(collection);
        if (last.equals(list.getLast())) {
            eval = true;
        }
        return eval;
    }

    public static String escapeHTML(String value) {

        String text = HtmlUtility.cleanText(value);
        return text;
    }

    public static String removeSlash(String url) {
        String link = url;
        if (url.contains("/")) {
            link = url.replace("/", "");

        }

        return "#omim" + link;
    }

    public static String replaceSpacesWithUnderScore(String text) {

        if (text.contains(" ")) {
            String refinedText = text.replaceAll(" ", "_");
            return refinedText;
        }

        return text;
    }

    /**
     * split with = and - to return only the search term
     *
     * @param text of this format searchparams.text=cathepsin-1.1.1.1 for
     * example
     * @return index[1] - cathepsin
     */
    public static String splitAndGetValue(String text) {
        String result = "";
        String data[] = text.split("searchKey=");

        if (data[1] != null) {
            String keyword[] = data[1].split("-");
            result = keyword[0];
        }
        return result;
    }

    /**
     * 
     * @param text text with hyphen e.g human-1.1.1.1
     * @return the text before the hyphen e.g human
     */
    public static String splitHyphen(String text) {

        String data[] = text.split("-");

        return data[0];
    }

    /**
     * Retrieves the URL to the source of the molecule, or builds it if it is
     * not set yet.
     *
     * @param molecule
     * @return a URL pointing to the source of the molecule.
     */
    public static String getMoleculeUrl(Molecule molecule) {
        String url = (String) molecule.getUrl();
        if (url == null || url.length() == 0) {
//            if (molecule.getId().startsWith("DB")){
//                // DrugBank
//                url = Functions.drugbankConfig.get("compound.base.url")
//                        + molecule.getId();
//            } else if (molecule.getId().startsWith("CHEMBL")){
//                // CHEMBL
//                url = Functions.chemblConfig.getCompoundBaseUrl()
//                        + molecule.getId();
//            } else if (molecule.getId().startsWith("CHEBI")){
//                // ChEBI
//                url = Functions.chebiConfig.getCompoundBaseUrl()
//                        + molecule.getId();
//            }

            if (molecule.getId().startsWith("CHEMBL")) {
                url = Functions.chemblConfig.getCompoundBaseUrl()
                        + molecule.getId();
            } else if (molecule.getId().startsWith("CHEBI")) {
                url = Functions.chebiConfig.getCompoundBaseUrl()
                        + molecule.getId();
            }

        }
        return url;
    }

    /**
     * Builds the URL for the image of a molecule, according to its source
     * database.
     *
     * @param molecule
     * @return a URL for the image.
     */
    public static String getMoleculeImgSrc(Molecule molecule) {
        String imgSrc = "";
        if (molecule.getId().startsWith("CHEBI")) {
            imgSrc = Functions.chebiConfig.getCompoundImgBaseUrl()
                    + molecule.getId();
        } else if (molecule.getId().startsWith("CHEMBL")) {
            imgSrc = Functions.chemblConfig.getCompoundImgBaseUrl()
                    + molecule.getId();
        } else if (molecule.getId().startsWith("DB")) {
            imgSrc = MessageFormat.format(
                    drugbankConfig.get("compound.img.base.url"),
                    molecule.getId());
        }
        return imgSrc;
    }
    
       public static String getBasketId(ProteinGroupEntry proteinGroupEntry) {

        return proteinGroupEntry.getPrimaryAccession();
        

    }

    public static String getSummaryBasketId(UniprotEntry summary) {
        List<String> accs = new ArrayList<>();
       // accs.add(summary.getAccession());
//        summary.getRelatedspecies().stream().forEach((acc) -> {
//            //accs.add(acc.getUniprotaccessions().get(0));
//        Stream.of(acc.getUniprotaccessions().stream().findFirst().get());
//        });

        //return accs.toString();
        return summary.getAccession();
        
//        System.out.println("FUNCTION 1 "+ summary.getRelatedspecies().stream().findFirst().get().getUniprotaccession());  
//        System.out.println("FUNCTION 2 "+ summary.getRelatedspecies().stream().findFirst().get().getUniprotaccessions());
//        
//        return summary.getRelatedspecies().stream().distinct().findFirst().get().getUniprotaccession();
//    
    }

    public static List<UniprotEntry> sortBlastResult(List<UniprotEntry> enzymes) {

        List<UniprotEntry> enzymeSummaries = enzymes.stream().sorted((id1, id2) -> -(Float.compare(id1.getIdentity(), id2.getIdentity()))).collect(Collectors.toList());
        Collections.sort(enzymeSummaries, (id1, id2) -> -(Float.compare(id1.getIdentity(), id2.getIdentity())));
        return enzymeSummaries.stream().sorted((id1, id2) -> -(Float.compare(id1.getIdentity(), id2.getIdentity()))).collect(Collectors.toList());
    }
}
