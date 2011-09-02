package uk.ac.ebi.ep.uniprot.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import uk.ac.ebi.ep.enzyme.model.Disease;
import uk.ac.ebi.ep.enzyme.model.Molecule;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.Comment;
import uk.ac.ebi.kraken.interfaces.uniprot.description.Field;
import uk.ac.ebi.kraken.interfaces.uniprot.description.FieldType;
import uk.ac.ebi.kraken.interfaces.uniprot.description.Name;
import uk.ac.ebi.kraken.model.uniprot.comments.DiseaseCommentImpl;
import uk.ac.ebi.kraken.model.uniprot.comments.EnzymeRegulationCommentImpl;
import uk.ac.ebi.kraken.model.uniprot.comments.FunctionCommentImpl;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class Transformer {

//********************************* VARIABLES ********************************//
    public static final String COMMON_REGEX = "|\\b.ompletely\\b|\\b.artially\\b|[\\s]\\band\\b[\\s]|;|\\bor\\b|\\b.otentially\\b|\\balso\\b|\\bby\\b|\\b.pecifically\\b|\\b.compound[s\\s]\\b|\\(|\\)";
    public static final String INHIBITOR_REGEX = ".nhibited by"+COMMON_REGEX;
    public static final String ACTIVATOR_REGEX = ".ctivated by"+COMMON_REGEX;
    //public static final String INHIBITOR_KEY= "inhibitor";
    //public static final String ACTIVATOR_KEY= "activator";

//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    public static String getCommentText(List<Comment> commentList) {
        StringBuffer sb = new StringBuffer();
        for (Comment comment: commentList) {
            FunctionCommentImpl castedComment = (FunctionCommentImpl)comment;
            String commentText = castedComment.getValue();
            sb.append(commentText);
            if (commentList.size() > 1) {
                sb.append("\n");
            }
            //comment.getEvidenceIds();
        }
        return sb.toString();
    }

    public static List<Disease> getDiseases(List<Comment> commentList) {
        List<Disease> diseases = new ArrayList<Disease>();
        for (Comment comment: commentList) {
            Disease disease = new Disease();
            DiseaseCommentImpl castedComment = (DiseaseCommentImpl)comment;
            //Work around to delete Note= from the comment
            String commentText = castedComment.getValue().replace("Note=", "");
            String id = String.valueOf(castedComment.getId());
            disease.setId(id);
            disease.setDescription(commentText);
            diseases.add(disease);
         }
        return diseases;
    }

    /**
     * gets a list of small molecules mentioned in enzyme regulation comments.
     * @param commentList
     * @return
     */
    public static String getMoleculeComments(List<Comment> commentList) {
        StringBuffer comments = new StringBuffer();
        for (Comment comment: commentList) {
            EnzymeRegulationCommentImpl castedComment = (EnzymeRegulationCommentImpl)comment;
            String commentText = castedComment.getValue();
            comments.append(commentText);
         }
        return comments.toString();
    }


    /**
     * parses a free text to get a list of molecules and groups these molecules into
     * inhibitor, activator. <br/>
     * Eg.: "Completely inhibited by ADP and ADP-glucose, and partially inhibited by ATP and NADH"
     * will be grouped as inhibitor->[ADP, ADP-glucose, ATP, NADH]
     */

    public static List<Molecule> parseTextForInhibitors(String text) {
        List<Molecule> inhibitors = parseText(text, Transformer.INHIBITOR_REGEX);
        return inhibitors;
    }

    public static List<Molecule> parseTextForActivators(String text) {
        List<Molecule> inhibitors = parseText(text,Transformer.ACTIVATOR_REGEX);
        return inhibitors;
    }

    public static List<Molecule> parseText(String text, String regex) {
        String cleanedText = new String(text.replaceAll(regex, ","));
        StringTokenizer stringTokenizer = new StringTokenizer(cleanedText, ",");
        List<Molecule> results = new ArrayList<Molecule>();
        while(stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken().trim();
            Molecule molecule = new Molecule();
            if (!token.equals("")) {
                molecule.setName(token);
                results.add(molecule);
            }
        }
        return results;
    }


    public static String getFullName(Name name) {
        List<Field> fields = name.getFieldsByType(FieldType.FULL);
        String nameValue = null;
        for (Field field:fields) {
            nameValue = field.getValue();
            break;
        }
        return nameValue;
    }
    public static  List<String> getNames(Name name) {
        List<Field> fields = name.getFields();
        List<String> names = new ArrayList<String>();
        String nameValue = null;
        for (Field field:fields) {
            nameValue = field.getValue();
            names.add(nameValue);
        }
        return names;
    }
    public static List<String> getAltNames (List<Name> altNames) {
        List<String> synonyms = new ArrayList<String>();
        for (Name altName: altNames) {   
            synonyms.addAll(getNames(altName));
        }
        return synonyms;
    }
    
}
