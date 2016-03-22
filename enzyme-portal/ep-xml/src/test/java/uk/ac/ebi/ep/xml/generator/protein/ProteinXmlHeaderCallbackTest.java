package uk.ac.ebi.ep.xml.generator.protein;

import uk.ac.ebi.ep.xml.generator.XmlTransformer;
import uk.ac.ebi.ep.xml.model.ModelDateUtil;

import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasXPath;
import static uk.ac.ebi.ep.xml.generator.protein.ProteinXmlHeaderCallback.*;

/**
 * Tests the behaviour of the {@link ProteinXmlHeaderCallback} class
 */
public class ProteinXmlHeaderCallbackTest extends AbstractCallbackSetup {
    private static final String ROOT_ELEMENT_NAME = "database";

    private static final String RELEASE_VERSION = "rel1";

    private static final String ELEMENT_OCCURS_N_TIMES_XPATH = "%s[count(%s)=%d]";
    private static final String MESSAGE_OCCURS_IN_ELEMENT_XPATH = "%s/%s[.='%s']";

    private ProteinXmlHeaderCallback headerCallback;

    @Override @Before
    public void setUp() throws Exception {
        super.setUp();

        headerCallback = new ProteinXmlHeaderCallback(RELEASE_VERSION);
    }

    @Test
    public void headerContainsDatabaseNameElement() throws Exception {
        headerCallback.write(writer);

        String xmlOutput = wrapWithRootElement(output.toString());

        Document xmlDoc = parse(xmlOutput);

        assertElementAppearsNTimesInDoc(DB_NAME_ELEMENT, 1, xmlDoc);
        assertElementHasMessageInDoc(DB_NAME_ELEMENT, XmlTransformer.enzymePortal, xmlDoc);
    }

    @Test
    public void headerContainsDatabaseDescriptionElement() throws Exception {
        headerCallback.write(writer);

        Document xmlDoc = createXmlDocument(output.toString());

        assertElementAppearsNTimesInDoc(DB_DESCRIPTION_ELEMENT, 1, xmlDoc);
        assertElementHasMessageInDoc(DB_DESCRIPTION_ELEMENT, XmlTransformer.enzymePortalDescription, xmlDoc);
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
        assertElementHasMessageInDoc(RELEASE_DATE_ELEMENT, ModelDateUtil.convertDateToString(todayDate), xmlDoc);
    }

    @Test
    public void headerContainsEntryCountElement() throws Exception {
        headerCallback.write(writer);

        Document xmlDoc = createXmlDocument(output.toString());

        assertElementAppearsNTimesInDoc(ENTRY_COUNT_ELEMENT, 1, xmlDoc);
        assertElementHasMessageInDoc(ENTRY_COUNT_ELEMENT, "0", xmlDoc);
    }

    private Document createXmlDocument(String toConvert) throws Exception {
        return parse(wrapWithRootElement(output.toString()));
    }

    private String wrapWithRootElement(String xml) {
        return "<" + ROOT_ELEMENT_NAME + ">" + xml + "</" + ROOT_ELEMENT_NAME + ">";
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
