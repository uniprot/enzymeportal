package uk.ac.ebi.ep.core.search;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import uk.ac.ebi.ep.config.Domain;
import uk.ac.ebi.ep.config.EnzymeRelatedDomains;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class Config {

//********************************* VARIABLES ********************************//
public static List<Domain> domainList;

//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//
static {
        try {
            EnzymeRelatedDomains enzymeRelatedDomains =
                    //unmarshalConfigFile(new File("/Users/hongcao/Projects/enzymeportal/trunk/enzyme-portal/ep-core/src/main/java/uk/ac/ebi/ep/core/search/config.xml"));
                    unmarshalConfigFile(Config.class.getResourceAsStream("/config.xml"));
            domainList = enzymeRelatedDomains.getDomain();
        } catch (IOException ex) {
           //COnfig file not found
        } catch (JAXBException ex) {
            //Config file provided can be unmarshalled
        }

}

    public static EnzymeRelatedDomains unmarshalConfigFile(InputStream inFile)
             throws IOException, JAXBException
            {
        EnzymeRelatedDomains enzymeRelatedDomains = null;
            JAXBContext jaxbContext = JAXBContext.newInstance("uk.ac.ebi.ep.config");
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            JAXBElement  jaxbElement = (JAXBElement) unmarshaller.unmarshal(inFile);
            enzymeRelatedDomains = (EnzymeRelatedDomains)jaxbElement.getValue();
        return enzymeRelatedDomains;
    }
    
    public static Domain getDomain(String domainId) {
        Iterator it = Config.domainList.iterator();
        Domain domain = null;
        while (it.hasNext()) {
            domain = (Domain)it.next();
            if (domainId.equals(domain.getId())) {
                return domain;
            }
        }
        return domain;        
    }


}
