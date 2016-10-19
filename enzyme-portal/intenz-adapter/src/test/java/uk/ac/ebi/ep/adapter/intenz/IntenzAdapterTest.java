package uk.ac.ebi.ep.adapter.intenz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.ep.enzyme.model.Molecule;
import uk.ac.ebi.intenz.xml.jaxb.Intenz;

//@Ignore
public class IntenzAdapterTest {

    private IntenzAdapter intenzAdapter;

    @Before
    public void before() {
        intenzAdapter = new IntenzAdapter();
        IntenzConfig config = new IntenzConfig();
        config.setIntenzXmlUrl(
                "http://wwwdev.ebi.ac.uk/intenz/ws/EC/{0}.{1}.{2}.{3}.xml");
        config.setTimeout(30000);
        intenzAdapter.setConfig(config);
    }

    @Test
    public void testGetIntenz() {
        List<String> ecList = new ArrayList<String>();
        List<Intenz> intenz = intenzAdapter.getIntenz(ecList);
        assertEquals(0, intenz.size());
        ecList.add("1.1.-.-");
        intenz = intenzAdapter.getIntenz(ecList);
        assertEquals(0, intenz.size());
        ecList.add("1.1.1.-");
        intenz = intenzAdapter.getIntenz(ecList);
        assertEquals(0, intenz.size());
        ecList.add("1.1.1.1");
        intenz = intenzAdapter.getIntenz(ecList);
        assertEquals(1, intenz.size());
        ecList.add("2.7.10.1");
        intenz = intenzAdapter.getIntenz(ecList);

        assertEquals(2, intenz.size());
    }

    @Test
    public void testGetCofactorsString() {
        String ec = "1.1.-.-";
        Collection<Molecule> cofactors = intenzAdapter.getCofactors(ec);
        assertNull(cofactors);
        ec = "1.1.1.-";
        cofactors = intenzAdapter.getCofactors(ec);
        assertNull(cofactors);
        ec = "1.1.1.1";
        cofactors = intenzAdapter.getCofactors(ec);
        assertNotNull(cofactors);
        assertEquals(2, cofactors.size());

    }

}
