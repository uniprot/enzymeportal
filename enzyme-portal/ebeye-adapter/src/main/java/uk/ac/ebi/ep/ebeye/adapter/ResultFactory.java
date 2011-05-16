package uk.ac.ebi.ep.ebeye.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import uk.ac.ebi.ebeye.util.Transformer;
import uk.ac.ebi.ep.ebeye.adapter.IEbeyeAdapter.Domains;
import uk.ac.ebi.ep.ebeye.adapter.IEbeyeAdapter.FieldsOfGetResults;
import uk.ac.ebi.ep.ebeye.result.jaxb.IntenzResult;
import uk.ac.ebi.ep.ebeye.result.jaxb.Result;
import uk.ac.ebi.ep.ebeye.result.jaxb.UniprotResult;
import uk.ac.ebi.ep.ebeye.result.jaxb.Xref;
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



//******************************** CONSTRUCTORS ******************************//
    public ResultFactory(List<String> resultFields) {        
        this.resultFields = resultFields;        
    }

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
        Result result = new Result();
        this.createResult(result, resultContent);

         /*
            Domains domainSelected = IEbeyeAdapter.Domains.valueOf(domain);
            switch(domainSelected) {
                case uniprot: {
                    resultObj = createUniprotResult(resultContent);
                    break;
                }
                case intenz: {
                    //createIntenzResult(rawResultList);
                    break;
                }
                default: {
                    Result result = new Result();
                    this.createResult(result, resultContent);
                    break;
                }
            }
          *
          */
        return result;
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

    public Result createResult(Result result, List<String> resultContent) {
        Iterator fieldIt = this.resultFields.iterator();
        Iterator resultIt = resultContent.iterator();
        while (fieldIt.hasNext() && resultIt.hasNext()) {
            String fieldId = (String)fieldIt.next();
            String fieldValue = (String)resultIt.next();
            FieldsOfGetResults fieldSelected = FieldsOfGetResults.valueOf(fieldId);
            switch(fieldSelected) {
                case id: {
                    result.setId(fieldValue);
                    break;
                }
                case acc: {
                    List<String> accessions =
                    Transformer.transform(fieldValue);
                    result.getAcc().addAll(accessions);
                    break;
                }
                case UNIPROT: {
                    Xref xref = new Xref();
                    xref.setDomain(Domains.uniprot.name());
                    List<String> xrefAccs =
                    Transformer.transform(fieldValue);
                    xref.getAcc().addAll(xrefAccs);
                    result.getXrefs().add(xref);
                }
            }

        }

        return result;
    }

    public Result createUniprotResult(List<String> resultContent) {        
        UniprotResult uniprotResult = new UniprotResult();
        //Result uniprotResult = new Result();
        return createResult(uniprotResult, resultContent);

    }

    public Result createIntenzResult(ArrayOfString rawResult) {
        IntenzResult intenzResult = new IntenzResult();
        return intenzResult;
    }


    //TODO
}
