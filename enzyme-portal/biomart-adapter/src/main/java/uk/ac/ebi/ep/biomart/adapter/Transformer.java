package uk.ac.ebi.ep.biomart.adapter;

import java.text.MessageFormat;
import java.util.ResourceBundle;

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


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    public static String getMessageTemplate(String tplName, Object[] messageArguments) {
        ResourceBundle messages = ResourceBundle.getBundle("MessageBundle_xml");
        //Object[] messageArguments = {tplName};
        MessageFormat formatter = new MessageFormat("");
        formatter.applyPattern(messages.getString(tplName));
        String message = formatter.format(messageArguments);
        return message;
    }

    public static String getMessageTemplate(String tplName) {
        ResourceBundle messages = ResourceBundle.getBundle("MessageBundle_xml");
        return messages.getString(tplName);
    }
}
