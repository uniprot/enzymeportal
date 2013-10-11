/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.web;

import java.util.LinkedList;
import java.util.List;

import uk.ac.ebi.ep.core.search.HtmlUtility;
import uk.ac.ebi.ep.enzyme.model.Molecule;

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
    
 
    
    public static boolean startsWithDigit(String data){
        return Character.isDigit(data.charAt(0));
    }
    
    
        /**This function is to enable using capital letter case in checking if a string starts with the letter
     * 
     * @param data the original string
     * @param letter the first letter
     * @return true if the string starts with the first letter
     */
    public static boolean startsWithLowerCase(String data, String letter){
        String current = data;
        if(startsWithDigit(data)){
             current = data.replaceAll("(-)?\\d+(\\-\\d*)?", "").trim();
       
        }
        return current.startsWith(letter.toLowerCase());
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

    /**
     * Retrieves the URL to the source of the molecule, or builds it if it is
     * not set yet.
     * @param molecule
     * @return a URL pointing to the source of the molecule.
     */
    public static String getMoleculeUrl(Molecule molecule){
        String url = (String) molecule.getUrl();
        if (url == null || url.length() == 0){
            if (molecule.getId().startsWith("DB")){
                // DrugBank
                url = "http://www.drugbank.ca/drugs/" + molecule.getId();
            } else if (molecule.getId().startsWith("CHEMBL")){
                // CHEMBL
                url = "https://www.ebi.ac.uk/chembldb/compound/inspect/"
                        + molecule.getId();
            }
        }
        return url;
    }
    
    /**
     * Builds the URL for the image of a molecule, according to its source
     * database. 
     * @param molecule
     * @return a URL for the image.
     */
    public static String getMoleculeImgSrc(Molecule molecule){
        String imgSrc = "";
        if (molecule.getId().startsWith("CHEBI")){
            imgSrc = "http://www.ebi.ac.uk/chebi/displayImage.do?defaultImage=true&imageIndex=0&chebiId=" + molecule.getId();
        } else if (molecule.getId().startsWith("CHEMBL")){
            imgSrc = "https://www.ebi.ac.uk/chembldb/compound/displayimage/"
                    + molecule.getId();
        }
        return imgSrc;
    }
}
