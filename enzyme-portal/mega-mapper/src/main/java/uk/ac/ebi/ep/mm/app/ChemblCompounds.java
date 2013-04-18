/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.mm.app;

/**
 *
 * @author joseph
 */
public class ChemblCompounds {
    
        public static void main(String[] args) {
       // String dbConfig = "ep-mm-db-enzdev";
       // String dbConfig = "ep-mm-db-ezprel";

        if (args == null) {
            System.exit(1);
        }

        ICompoundsDAO dAOImpl = new CompoundsChEMBL_Impl(args[0]);
        //CompoundsChEMBL_Impl dAOImpl = new CompoundsChEMBL_Impl(dbConfig);
        dAOImpl.buildCompound();

    }
    
}
