/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.mm.app;

/**
 *
 * @author joseph
 */
public class ChebiCompounds {

    public static void main(String... args) {
        //String dbConfig = "ep-mm-db-enzdev";

        if (args == null) {
            System.exit(1);
        }



        ICompoundsDAO dAOImpl = new CompoundsDAOImpl(args[0]);
        dAOImpl.getCHEBI_Compounds();

    }
}
