/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.mm.app;

import java.io.IOException;
import java.util.Collection;
import uk.ac.ebi.ep.mm.Entry;
import uk.ac.ebi.ep.mm.XRef;

/**
 *
 * @author joseph
 */
public interface ICompoundsDAO {
    
     void writeEntry(Entry entry) throws IOException;

     void writeEntries(Collection<Entry> entries) throws IOException;

    
    void writeXref(XRef ref) throws IOException;


     void writeXrefs(Collection<XRef> xRefs) throws IOException ;
   

     void writeEntriesAndXrefs(Collection<Entry> entries, Collection<XRef> xRefs) throws IOException;
     
     void buildCompound();

}
