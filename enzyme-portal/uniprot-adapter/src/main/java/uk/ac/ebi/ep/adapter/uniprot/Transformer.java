package uk.ac.ebi.ep.adapter.uniprot;

import java.text.MessageFormat;
import java.util.*;

import uk.ac.ebi.ep.enzyme.model.Disease;
import uk.ac.ebi.ep.enzyme.model.Molecule;
import uk.ac.ebi.ep.util.EPUtil;
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

    public static String getCommentText(List<Comment> commentList) {
        StringBuilder sb = new StringBuilder();
        for (Comment comment : commentList) {
            FunctionCommentImpl castedComment = (FunctionCommentImpl) comment;
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
        for (Comment comment : commentList) {
            Disease disease = new Disease();
            DiseaseCommentImpl castedComment = (DiseaseCommentImpl) comment;
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
        StringBuilder comments = new StringBuilder();
        for (Comment comment : commentList) {
            EnzymeRegulationCommentImpl castedComment = (EnzymeRegulationCommentImpl) comment;
            String commentText = castedComment.getValue();
            comments.append(commentText);
        }
        return comments.toString();
    }

    /**
     * Parses a free text to get a list of molecules acting as inhibitors.<br/>
     * @return A list of molecules with just a name (no ID).
     * @deprecated Use {@link EPUtil#parseTextForInhibitors(String)} instead.
     */
     @Deprecated
    public static List<Molecule> parseTextForInhibitors(String text){
        return EPUtil.parseTextForInhibitors(text);
    }

    /**
     * Parses a free text to get a list of molecules acting as activators.<br/>
     * @return A list of molecules with just a name (no ID).
     * @deprecated Use {@link EPUtil#parseTextForActivators(String)} instead.
     */
     @Deprecated
    public static List<Molecule> parseTextForActivators(String text) {
        return EPUtil.parseTextForActivators(text);
    }

    public static String getFullName(Name name) {
        List<Field> fields = name.getFieldsByType(FieldType.FULL);
        String nameValue = null;
        for (Field field : fields) {
            nameValue = field.getValue();
            break;
        }
        return nameValue;
    }

    public static List<String> getNames(Name name) {
        List<Field> fields = name.getFields();
        List<String> names = new ArrayList<String>();
        String nameValue = null;
        for (Field field : fields) {
            nameValue = field.getValue();
            names.add(nameValue);
        }
        return names;
    }

    public static List<String> getAltNames(List<Name> altNames) {
        List<String> synonyms = new ArrayList<String>();
        for (Name altName : altNames) {
            synonyms.addAll(getNames(altName));
        }
        return synonyms;
    }

    public static String getMessageTemplate(String tplName, Object[] messageArguments) {
        ResourceBundle messages = ResourceBundle.getBundle("MessageBundle_ognl");
        //Object[] messageArguments = {tplName};
        MessageFormat formatter = new MessageFormat("");
        formatter.applyPattern(messages.getString(tplName));
        String message = formatter.format(messageArguments);
        return message;
    }

    public static String getMessageTemplate(String tplName) {
        ResourceBundle messages = ResourceBundle.getBundle("MessageBundle_ognl");
        return messages.getString(tplName);
    }
}
