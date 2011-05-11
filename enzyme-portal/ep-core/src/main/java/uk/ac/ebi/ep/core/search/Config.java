package uk.ac.ebi.ep.core.search;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import uk.ac.ebi.ebeye.ParamGetNumberOfResults;
import uk.ac.ebi.ebeye.ResultOfGetNumberOfResults;
import uk.ac.ebi.ebeye.ResultOfGetResultsIds;
import uk.ac.ebi.ep.config.Domain;
import uk.ac.ebi.ep.config.EnzymeRelatedDomains;
import uk.ac.ebi.ep.config.ResultField;
import uk.ac.ebi.ep.core.search.EBeyeWsCallable.GetNumberOfResultsCallable;
import uk.ac.ebi.ep.search.exception.EnzymeFinderException;
import uk.ac.ebi.ep.search.parameter.SearchParams;
import uk.ac.ebi.ep.util.query.LuceneQueryBuilder;
import uk.ac.ebi.util.result.DataTypeConverter;

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

        public static List<String> getResultFields(Domain domain) {
        List<ResultField> fieldList = domain.getResultFieldList().getResultField();
        Iterator it = fieldList.iterator();
        List<String> resultRefFields = new ArrayList<String>();
        while (it.hasNext()) {
            ResultField field = (ResultField) it.next();
            resultRefFields.add(field.getId());
        }
        return resultRefFields;
    }

    public static void main(String[] args) throws EnzymeFinderException {
        String[] queryArray = {
            "EC:(1 OR 1.1* OR 1.2* OR 1.3*)"
            ,"EC:(1.4* OR 1.5* OR 1.6* OR 1.7* OR 1.8*)"
            ,"EC:(1.9*)"
            ,"EC:(2 OR 2.1* OR 2.2* OR 2.3* OR 2.4*)"
            ,"EC:(2.5* OR 2.6* OR 2.8* OR 2.9*)"
            ,"EC:(2.7* NOT 2.7.7*)"
            ,"EC:(2.7.7* OR 3 OR 3.1* OR 3.2*)"
            ,"EC:(3.3* OR 3.4* OR 3.5* OR 3.6* OR 3.7* OR 3.8* OR 3.9*)"
            ,"EC:(4*)"
            ,"EC:(5*)"
            ,"EC:(6* OR 7* OR 8* OR 9*)"
        };
        List<ParamGetNumberOfResults> queryList = new
                ArrayList<ParamGetNumberOfResults>();
        for (String query: queryArray) {
            ParamGetNumberOfResults param = new ParamGetNumberOfResults(
                    EnzymeFinder.UNIPROT_DOMAIN, query);
            queryList.add(param);
        }
        IEnzymeFinder finder = new EnzymeFinder();
        List<ResultOfGetNumberOfResults> nrOfResultsList =
        finder.getNumberOfResults(queryList);
        List<ResultOfGetResultsIds> resultList = finder.getResultsIds(nrOfResultsList);
        List<String> idList = DataTypeConverter.getResultsIds(resultList);
        //System.out.print(idList);
    }


}
