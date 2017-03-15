package cz.zcu.kiv.md2odt.document.odt

import cz.zcu.kiv.md2odt.document.ParagraphContentBuilder
import org.junit.Before
import org.junit.Test

/**
 * Created by pepe on 15. 3. 2017.
 */
class OdfSimpleDocumentTest {
    OdfSimpleDocument doc

    @Before
    void setUp() throws Exception {
        doc = new OdfSimpleDocument()
    }

    @Test
    void parToStringTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addRegular("regular ").addBold("bold ").addItalic("italic ").build()
        assert doc.parToString(pc).equals("regular >bold ><italic <")
    }

    @Test
    void addParagraphTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addRegular("regular ").addBold("bold ").addItalic("italic ").build()
        doc.addParagraph(pc)
        assert doc.getOdfSimpleWrapper().getLastNode().getTextContent().equals("regular >bold ><italic <")
        //doc.save("test.odt")
    }
}
