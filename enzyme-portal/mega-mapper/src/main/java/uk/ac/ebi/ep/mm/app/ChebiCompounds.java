/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.mm.app;

/**
 *
 * @author joseph
 * @deprecated Now ChEBI cross-references are imported either by text-mining
 *      the UniProtXML for inhibitors/activators, or from IntEnzXML for
 *      cofactors and reaction participants.
 */
@Deprecated
public class ChebiCompounds {

    public static void main(String... args) {
       // String dbConfig = "ep-mm-db-enzdev";
        //String dbConfig = "ep-mm-db-ezprel";

        if (args == null) {
            System.exit(1);
        }

        ICompoundsDAO dAOImpl = new CompoundsChEBI_Impl(args[0]);
        //ICompoundsDAO dAOImpl = new CompoundsChEBI_Impl(dbConfig);
        dAOImpl.buildCompound();
        


    }
}
