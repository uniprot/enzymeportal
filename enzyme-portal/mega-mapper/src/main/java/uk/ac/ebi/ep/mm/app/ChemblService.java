/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.mm.app;

import java.util.concurrent.Callable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uk.ac.ebi.chemblws.domain.Compound;
import uk.ac.ebi.chemblws.exception.ChemblServiceException;
import uk.ac.ebi.chemblws.exception.CompoundNotFoundException;
import uk.ac.ebi.chemblws.exception.InvalidCompoundIdentifierException;
import uk.ac.ebi.chemblws.restclient.ChemblRestClient;

/**
 *
 * @author joseph
 */
public class ChemblService implements Callable<Compound> {

    private final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(ChemblService.class);
    private ChemblRestClient chemblRestClient;
    private String chemblId;
    //private Compound compound;

    public ChemblService(String chemblId) {
        this.chemblId = chemblId;
        init();
    }

    private void init() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        chemblRestClient = applicationContext.getBean("chemblRestClient", ChemblRestClient.class);
    }

    public Compound call() {
        Compound compound = null;
       
        try {
            Compound chemblCompound = chemblRestClient.getCompound(chemblId);
            if (chemblCompound != null ) {
                compound = chemblCompound;
                }

           
        } catch (CompoundNotFoundException e) {
            LOGGER.fatal("CompoundNotFoundException thrown", e);

            //Do something
        } catch (ChemblServiceException e) {
            LOGGER.fatal("ChemblServiceException thrown", e);

            //Do something
        } catch (InvalidCompoundIdentifierException ex) {
            LOGGER.error("InvalidCompoundIdentifierException thrown", ex);

        }




        return compound;
    }
}
