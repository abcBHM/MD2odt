package cz.zcu.kiv.md2odt.document.odt

import cz.zcu.kiv.md2odt.document.ParagraphContent
import cz.zcu.kiv.md2odt.document.ParagraphContentBuilder
import org.junit.Before
import org.junit.Ignore
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

    @Ignore
    @Test
    void example() throws Exception {
        def pc1 = ParagraphContentBuilder.builder().addRegular("regular ").addBold("bold ").addItalic("italic ").addRegular("regular").build()
        def pc2 = ParagraphContentBuilder.builder().addRegular("regular ").addLink("seznam","www.seznam.cz").addRegular("   ").addLink("badlink","gafsgs^sfds").addCode(" inline code ").build()

        doc.addParagraph(pc1)
        doc.addParagraph(pc2)
        doc.save("test.odt")
    }

    @Test
    void parToStringTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addRegular("regular ").addBold("bold ").addItalic("italic ").build()
        assert doc.parToString(pc).equals("regular >bold ><italic <")
    }

    @Test
    void parToStringLinkTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addLink("pokus", "www.seznam.cz").build()
        assert doc.parToString(pc).equals("#@www.seznam.cz@pokus#")
    }

    @Test
    void addParagraphTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addRegular("regular ").addBold("bold ").addItalic("italic ").build()
        doc.addParagraph(pc)
        assert doc.getOdfSimpleWrapper().getLastNode().getTextContent().equals("regular >bold ><italic <")
    }

    @Test
    void addParagraphLinkTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addLink("pokus", "www.sez#nam.cz").build()
        doc.addParagraph(pc)
        assert doc.getOdfSimpleWrapper().getLastNode().getTextContent().equals("#@www.sez&link;nam.cz@pokus#")
    }

    @Test
    void addParagraphInlineCodeTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addCode("new OdfSimpleDocument();").build()
        doc.addParagraph(pc)
        assert doc.getOdfSimpleWrapper().getLastNode().getTextContent().equals("'new OdfSimpleDocument();'")
    }

    @Test
    void addQuoteBlockTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addRegular("regular ").addBold("bold ").addItalic("italic ").build()
        ArrayList<ParagraphContent> list = new ArrayList<>(2)
        list.add(pc)
        list.add(pc)
        doc.addQuoteBlock(list)
        assert doc.getOdfSimpleWrapper().getLastNode().getTextContent().equals("regular >bold ><italic <")
    }

    @Test
    void addHeadingTest() throws Exception {
        doc.addHeading("heading 1", 1)
        assert doc.getOdfSimpleWrapper().getLastNode().toString().equals("<text:h text:outline-level=\"1\" text:style-name=\"Heading_20_1\">heading 1</text:h>")
    }
}
