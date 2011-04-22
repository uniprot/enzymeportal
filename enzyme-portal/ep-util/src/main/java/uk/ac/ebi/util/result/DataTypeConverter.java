package uk.ac.ebi.util.result;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class DataTypeConverter {


//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//
/*
    public static String accessionToXLink(String url, String accession) {
        StringBuffer sb = new StringBuffer();
        sb.append(url);
        sb.append(accession);
        return sb.toString();
    }
*/
    public static List<String> accessionsToList (String[] arrayOfAccessions) {
        List<String> list = new ArrayList<String>();
        for (String accession:arrayOfAccessions) {
            list.add(accession);
        }
        return list;
    }

    /*
    public static String StringOfAccessionsToXLinks(Constant.DOMAINS_ENUM domains,
                                                                    String accessions) {
        String accessionXLinks = null;
        switch (domains) {
            case unitprot:{
                accessionXLinks = uniprotAccessionsToXLinks(accessions);
                break;
            }
        }
        return accessionXLinks;
    }
    
    public static String uniprotAccessionsToXLinks(String accessions) {        
        ResourceBundle templateFile = ResourceBundle.getBundle("template");
        String[] arrayOfAccessions = accessions.split(" ");
        String url = templateFile.getString("uniprotUrl");
        StringBuffer sb = new StringBuffer();
        for (String accession:arrayOfAccessions) {
            Object[] messageArguments = {accession,url+accession,accession};
            MessageFormat formatter = new MessageFormat("");
            formatter.applyPattern(templateFile.getString("xlinkTemplate"));
            String xLink = formatter.format(messageArguments);
            sb.append(xLink);
            sb.append(" ");
        }
        return sb.toString();
        
    }
     * */

}
