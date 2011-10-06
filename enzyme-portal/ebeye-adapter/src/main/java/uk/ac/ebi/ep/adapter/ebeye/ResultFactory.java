package uk.ac.ebi.ep.adapter.ebeye;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import uk.ac.ebi.ebeye.util.Transformer;
import uk.ac.ebi.ep.adapter.ebeye.IEbeyeAdapter.Domains;
import uk.ac.ebi.ep.adapter.ebeye.IEbeyeAdapter.FieldsOfGetResults;
import uk.ac.ebi.ep.ebeye.result.Result;
import uk.ac.ebi.ep.ebeye.result.Xref;

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
    public List<Result> getResult( List<String> resultContent, boolean convertResultToUniprot) {
        /*
        Result result = new Result();
        this.createResult(result, resultContent);
        */
         List<Result>  results = new ArrayList<Result>();
         
         if (convertResultToUniprot) {
             List<Result> otherDomainsResults = convertResultToUniprot(resultContent);
             if (otherDomainsResults != null) {
                 results.addAll(otherDomainsResults);
             }
             
         } else {
             results.add(createResult(resultContent));
         }
         /*
         if (domain.equals(IEbeyeAdapter.Domains.uniprot.name())) {
             results.add(createResult(resultContent));
         }
         else {
             List<Result> otherDomainsResults = convertResultToUniprot(resultContent);
             if (otherDomainsResults != null) {
                 results.addAll(otherDomainsResults);
             }
             
         }
         */
         /*
            Domains domainSelected = IEbeyeAdapter.Domains.valueOf(domain);
            switch(domainSelected) {
                case uniprot: {
                    result = createUniprotResult(resultContent);
                    break;
                }
                case intenz: {
                    createIntenzResult(resultContent);
                    break;
                }
                case pdbe: {
                    result = createResult(result, resultContent);
                    break;
                }
            }
        */
        return results;
    }

    public List<Result> getResults (List<List<String>> resultContentList
            , boolean convertResultToUniprot ) {
        List<Result> results = new ArrayList<Result>();
        Iterator it = resultContentList.iterator();
        while (it.hasNext()) {
            List<String> resultContent =(List<String>)it.next();
            List<Result> resultObj = getResult(resultContent, convertResultToUniprot);
            results.addAll(resultObj);
        }
        return results;
    }

    public Result createResult(List<String> rawResult) {
        Result result = new Result();
        Iterator fieldIt = this.resultFields.iterator();
        Iterator resultIt = rawResult.iterator();
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
                    Transformer.transformAccessionsString(fieldValue);
                    result.getAcc().addAll(accessions);
                    break;
                }
//                case UNIPROT: {
//                    if (fieldValue!=null) {
//                        if (!fieldValue.equals("")) {
//                            Xref xref = new Xref();
//                            xref.setDomain(Domains.uniprot.name());
//                            List<String> xrefAccs =
//                            Transformer.transformAccessionsString(fieldValue);
//                            xref.getAcc().addAll(xrefAccs);
//                            result.getXrefs().add(xref);
//                        }
//                    }
//
//                }
            }

        }

        return result;
    }
    public List<Result> convertResultToUniprot(List<String> resultFromOtherDomains) {
            List<Result> resultList = new ArrayList<Result>();
            String idField = resultFromOtherDomains.get(0);
            String accsField = resultFromOtherDomains.get(1);
            String uniprotField = resultFromOtherDomains.get(2);
            if (!uniprotField.isEmpty()) {
                //List<String> nonUniprotAccs = Transformer.transform(accsField);
                Xref xref = new Xref();
                xref.setDomain(this.domain);
                if (this.domain.equals(EbeyeAdapter.Domains.chebi.name())) {
                    xref.getAcc().add(idField);
                } else {
                    xref.getAcc().add(accsField);
                }
                

                List<String> uniprotAccs =
                Transformer.transformAccessionsString(uniprotField);
                for (String acc: uniprotAccs) {
                    Result result = new Result();
                result.getAcc().add(acc);
                result.getXrefs().add(xref);
                resultList.add(result);
            }
            return resultList;
        }

        return resultList;
    }

/*
    public Result createUniprotResult(List<String> resultContent) {        
        UniprotResult uniprotResult = new UniprotResult();
        //Result uniprotResult = new Result();
        return createResult(uniprotResult, resultContent);
    }

    public Result createIntenzResult(List<String> resultContent) {
        IntenzResult intenzResult = new IntenzResult();
        //Result uniprotResult = new Result();
        return createResult(intenzResult, resultContent);
    }

    public Result createPdbeResult(List<String> resultContent) {
        PdbeResult bdPdbeResult = new PdbeResult();
        //Result uniprotResult = new Result();
        return createResult(bdPdbeResult, resultContent);
    }
*/
//TODO



    //TODO
}
