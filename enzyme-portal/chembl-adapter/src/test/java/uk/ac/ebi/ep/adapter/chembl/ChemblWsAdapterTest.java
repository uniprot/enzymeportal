package uk.ac.ebi.ep.adapter.chembl;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * @author rafa
 * @since 1.0.0
 */
public class ChemblWsAdapterTest {

    private IChemblAdapter adapter = new ChemblWsAdapter();

    @Test
    public void testGetTargetBioactivities() throws Exception {
        ChemblBioactivities bioactivities =
                adapter.getCompoundBioactivities("CHEMBL2");
        assertNotNull(bioactivities);
    }

    @Test
    public void testGetCompoundBioactivities() throws Exception {
        ChemblBioactivities bioactivities =
                adapter.getTargetBioactivities("CHEMBL240");
        assertNotNull(bioactivities);
    }

    @Test
    public void testGetPreferredName() throws Exception {
        String name;

        name = adapter.getPreferredName("CHEMBL24828");
        assertNotNull(name);
        assertEquals("VANDETANIB", name);

        name = adapter.getPreferredName("CHEMBL2");
        assertNotNull(name);
        assertEquals("PRAZOSIN", name);

        name = adapter.getPreferredName("CHEMBL1");
        assertNull(name);
    }
}
