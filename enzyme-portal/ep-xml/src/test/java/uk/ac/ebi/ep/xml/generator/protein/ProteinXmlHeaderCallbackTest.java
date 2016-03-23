package uk.ac.ebi.ep.xml.generator.protein;

import uk.ac.ebi.ep.data.service.EnzymePortalXmlService;
import uk.ac.ebi.ep.xml.generator.XmlTransformer;
import uk.ac.ebi.ep.xml.util.DateUtil;

import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.w3c.dom.Document;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasXPath;
import static org.mockito.Mockito.when;
import static uk.ac.ebi.ep.xml.generator.protein.ProteinXmlHeaderCallback.*;

/**
 * Tests the behaviour of the {@link ProteinXmlHeaderCallback} class
 */
@RunWith(MockitoJUnitRunner.class)
public class ProteinXmlHeaderCallbackTest extends AbstractCallbackSetup {
    private static final String ROOT_ELEMENT_NAME = "database";

    private static final String RELEASE_VERSION = "rel1";
    private static final String DB_NAME = XmlTransformer.ENZYME_PORTAL;
    private static final String DB_DESCRIPTION = XmlTransformer.ENZYME_PORTAL_DESCRIPTION;

    private static final String ELEMENT_OCCURS_N_TIMES_XPATH = "%s[count(%s)=%d]";
    private static final String MESSAGE_OCCURS_IN_ELEMENT_XPATH = "%s/%s[.='%s']";

    private ProteinXmlHeaderCallback headerCallback;

    @Mock
    private EnzymePortalXmlService enzymeService;

    @Override @Before
    public void setUp() throws Exception {
        super.setUp();

        headerCallback = new ProteinXmlHeaderCallback(RELEASE_VERSION, enzymeService);
    }

    @Test
    public void headerContainsDatabaseNameElement() throws Exception {
        headerCallback.write(writer);

        String xmlOutput = wrapWithRootElement(output.toString());

        Document xmlDoc = parse(xmlOutput);

        assertElementAppearsNTimesInDoc(DB_NAME_ELEMENT, 1, xmlDoc);
        assertElementHasMessageInDoc(DB_NAME_ELEMENT, DB_NAME, xmlDoc);
    }

    @Test
    public void headerContainsDatabaseDescriptionElement() throws Exception {
        headerCallback.write(writer);

        Document xmlDoc = createXmlDocument(output.toString());

        assertElementAppearsNTimesInDoc(DB_DESCRIPTION_ELEMENT, 1, xmlDoc);
        assertElementHasMessageInDoc(DB_DESCRIPTION_ELEMENT, DB_DESCRIPTION, xmlDoc);
    }

    @Test
    public void headerContainsReleaseVersionElement() throws Exception {
        headerCallback.write(writer);

        Document xmlDoc = createXmlDocument(output.toString());

        assertElementAppearsNTimesInDoc(RELEASE_VERSION_ELEMENT, 1, xmlDoc);
        assertElementHasMessageInDoc(RELEASE_VERSION_ELEMENT, RELEASE_VERSION, xmlDoc);
    }

    @Test
    public void headerContainsReleaseDateElement() throws Exception {
        headerCallback.write(writer);

        Document xmlDoc = createXmlDocument(output.toString());

        LocalDate todayDate = LocalDate.now();

        assertElementAppearsNTimesInDoc(RELEASE_DATE_ELEMENT, 1, xmlDoc);
        assertElementHasMessageInDoc(RELEASE_DATE_ELEMENT, DateUtil.convertDateToString(todayDate), xmlDoc);
    }

    @Test
    public void headerContainsEntryCountElement() throws Exception {
        long totalCount = 17;

        when(enzymeService.countUniprotEntries()).thenReturn(totalCount);

        headerCallback.write(writer);

        Document xmlDoc = createXmlDocument(output.toString());

        assertElementAppearsNTimesInDoc(ENTRY_COUNT_ELEMENT, 1, xmlDoc);
        assertElementHasMessageInDoc(ENTRY_COUNT_ELEMENT, String.valueOf(totalCount), xmlDoc);
    }

    @Test
    public void headerContainsEntriesStartElement() throws Exception {
        headerCallback.write(writer);

        Document xmlDoc = createXmlDocument(output.toString());

        assertElementAppearsNTimesInDoc(ENTRY_COUNT_ELEMENT, 1, xmlDoc);
    }

    private Document createXmlDocument(String toConvert) throws Exception {
        return parse(wrapWithRootElement(output.toString()));
    }

    private String wrapWithRootElement(String xml) {
        //entries element is left open by xmlEventWriter, so it is necessary to close the element.
        String entriesClosingElement = "/>";
        return "<" + ROOT_ELEMENT_NAME + ">" + xml + entriesClosingElement + "</" + ROOT_ELEMENT_NAME + ">";
    }

    private void assertElementAppearsNTimesInDoc(String elementName, int occurrence, Document xmlDoc) {
        String occursNTimes = String.format(ELEMENT_OCCURS_N_TIMES_XPATH, ROOT_ELEMENT_NAME, elementName, occurrence);
        assertThat(xmlDoc, hasXPath(occursNTimes));
    }

    private void assertElementHasMessageInDoc(String elementName, String message, Document xmlDoc) {
        String messageOccursInElement = String.format(MESSAGE_OCCURS_IN_ELEMENT_XPATH, ROOT_ELEMENT_NAME,
                elementName, message);

        assertThat(xmlDoc, hasXPath(messageOccursInElement));
    }
}
