package uk.ac.ebi.ep.ebeye.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import uk.ac.ebi.ep.ebeye.adapter.ResultFactory.Domains;
import uk.ac.ebi.ep.ebeye.result.jaxb.IntenzResult;
import uk.ac.ebi.ep.ebeye.result.jaxb.Result;
import uk.ac.ebi.ep.ebeye.result.jaxb.UniprotResult;
import uk.ac.ebi.webservices.ebeye.ArrayOfString;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ResultFactory {

//********************************* VARIABLES ********************************//
    protected String domain;
    protected List<String> resultFields;
    //Object where result content is saved
    public static enum Domains {intenz,unitprot,rhea,reactome,chebi
                                                ,pdbe,chembl_compound, chembl_target,omim,mesh
        };
    public static enum UniprotFields {
       id, acc
    };



//******************************** CONSTRUCTORS ******************************//

    public ResultFactory(String domain, List<String> resultFields) {
        this.domain = domain;
        this.resultFields = resultFields;        
    }
    

//****************************** GETTER & SETTER *****************************//

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<String> getResultFields() {
        return resultFields;
    }

    public void setResultFields(List<String> resultFields) {
        this.resultFields = resultFields;
    }



//********************************** METHODS *********************************//
    public Result getResult( List<String> resultContent) {
         Result resultObj = null;
        Domains domainSelected = Domains.valueOf(domain);
        switch(domainSelected) {
            case unitprot: {
                resultObj = createUniprotResult(resultContent);
            }
            case intenz: {
                //createIntenzResult(rawResultList);
            }

        }

        return resultObj;
    }

    public List<Result> getResults (List<List<String>> resultContentList) {
        List<Result> results = new ArrayList<Result>();
        Iterator it = resultContentList.iterator();
        while (it.hasNext()) {
            List<String> resultContent =(List<String>)it.next();
            Result resultObj = getResult(resultContent);
            results.add(resultObj);
        }
        return results;
    }

    public Result createUniprotResult(List<String> resultContent) {
        UniprotResult uniprotResult = new UniprotResult();
        Iterator fieldIt = this.resultFields.iterator();
        Iterator resultIt = resultContent.iterator();
        while (fieldIt.hasNext() && resultIt.hasNext()) {
            String fieldId = (String)fieldIt.next();
            String fieldValue = (String)resultIt.next();
            UniprotFields fieldSelected = UniprotFields.valueOf(fieldId);
            switch(fieldSelected) {
                case id: {
                    uniprotResult.setId(fieldValue);
                }
                case acc: {
                    uniprotResult.getAcc().add(fieldValue);
                }
            }
            
        }
        return uniprotResult;

    }

    public Result createIntenzResult(ArrayOfString rawResult) {
        IntenzResult intenzResult = new IntenzResult();
        return intenzResult;
    }


    //TODO
}
