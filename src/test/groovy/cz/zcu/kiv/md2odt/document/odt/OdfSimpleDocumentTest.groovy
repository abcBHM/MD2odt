package cz.zcu.kiv.md2odt.document.odt

import cz.zcu.kiv.md2odt.document.ListContent
import cz.zcu.kiv.md2odt.document.ListContentBuilder
import cz.zcu.kiv.md2odt.document.ListType
import cz.zcu.kiv.md2odt.document.ParagraphContent
import cz.zcu.kiv.md2odt.document.ParagraphContentBuilder
import cz.zcu.kiv.md2odt.document.SpanContentImage
import cz.zcu.kiv.md2odt.document.SpanContentText
import cz.zcu.kiv.md2odt.document.SpanType
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by pepe on 15. 3. 2017.
 */
class OdfSimpleDocumentTest {
    OdfSimpleDocument doc

    private static final String TEMPLATE = 'src/test/resources/template.odt'
    private static final String WRONG_TEMPLATE = 'wrongtemplate.odt'
    private static final String TEST = 'test.odt'

    @Before
    void setUp() throws Exception {
        doc = new OdfSimpleDocument()
    }

    @Ignore
    @Test
    void example() throws Exception {
        def pc1 = ParagraphContentBuilder.builder().addRegular("regular ").addBold("bold ").addItalic("italic ").addRegular("regular ").addImage("text", "https://www.seznam.cz/media/img/logo_v2.png", "alt").addRegular(" regular ").build()
        def pc2 = ParagraphContentBuilder.builder().addRegular("regular ").addLink("seznam","http://www.seznam.cz").addRegular("   ").addLink("badlink","gafsgs^sfds").addCode(" inline code ").build()

        doc.addParagraph(pc1)
        doc.addParagraph(pc2)
        doc.addHeading("Code block", 1)
        doc.addCodeBlock("List<> x = new ArrayList<>();\n"+"List<> x = new ArrayList<>();\n"+"List<> x = new ArrayList<>();\n")

        doc.addHeading("Quote block", 3)
        def pc = ParagraphContentBuilder.builder().addRegular("regular ").addBold("bold ").addItalic("italic ").build()
        ArrayList<ParagraphContent> list = new ArrayList<>(2)
        list.add(pc)
        list.add(pc)
        doc.addQuoteBlock(list)
        doc.save(TEST)
    }

    @Test
    void odfSimpleDocumentFileInputStreamConstructor() throws Exception {
        doc = null
        doc = new OdfSimpleDocument(new FileInputStream(TEMPLATE))
        assert doc != null
    }

    @Test(expected = FileNotFoundException.class)
    void odfSimpleDocumentTemplateNotFound1() throws Exception {
        doc = new OdfSimpleDocument(WRONG_TEMPLATE)
    }

    @Test(expected = FileNotFoundException.class)
    void odfSimpleDocumentTemplateNotFound2() throws Exception {
        doc = new OdfSimpleDocument(new File(WRONG_TEMPLATE))
    }

    @Test
    void save1() throws Exception {
        doc.save(TEST)
        File f = new File(TEST)
        assert f.exists()
        f.delete()
    }
    @Test
    void save2() throws Exception {
        doc.save(Files.newOutputStream(Paths.get(TEST)))
        File f = new File(TEST)
        assert f.exists()
        f.delete()
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
        assert doc.getOdfSimpleWrapper().getLastNode().getTextContent().equals("#www.sez&link;nam.cz@pokus#")
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
    void addCodeBlockTest() throws Exception {
        doc.addCodeBlock("List<> x = new ArrayList<>()")
        assert doc.getOdfSimpleWrapper().getLastNode().getTextContent().equals(OdfSimpleConstants.escape("List<> x = new ArrayList<>()"))
    }

    @Test
    void addHeadingTest() throws Exception {
        doc.addHeading("heading 1", 1)
        assert doc.getOdfSimpleWrapper().getLastNode().toString().equals("<text:h text:outline-level=\"1\" text:style-name=\"Heading_20_1\">heading 1</text:h>")
    }

    @Ignore
    @Test
    void testListContent() {
        ListContent content = ListContentBuilder.builder(ListType.ORDERED)
                .addListItem(ParagraphContentBuilder.builder().addBold("bla1").build())
                .addListItem(ListContentBuilder.builder(ListType.ORDERED)
                        .addListItem(ParagraphContentBuilder.builder().addBold("bla2").build()).addListItem(ParagraphContentBuilder.builder().addBold("bla21").build()).build())
                .addListItem(ParagraphContentBuilder.builder().addBold("bla3").build())
                .build()
        doc.addList(content)
        doc.save("test.odt")
    }

    @Test
    void addHorizontalRuleTest() throws Exception {
        doc.addHorizontalRule()
        assert doc.odfSimpleWrapper.lastNode.nodeName.equals("text:p")
        assert doc.odfSimpleWrapper.lastNode.textContent.equals("")
    }

    @Test
    void addMultiListTest() throws Exception {
        ListContent content = ListContentBuilder.builder(ListType.ORDERED)
                .addListItem(ParagraphContentBuilder.builder().addRegular("bla1").build())
                .addListItem(ListContentBuilder.builder(ListType.BULLET)
                .addListItem(ParagraphContentBuilder.builder().addRegular("bla2").build()).build())
                .addListItem(ListContentBuilder.builder(null)
                .addListItem(ParagraphContentBuilder.builder().addRegular("bla2").build()).build())
                .build()
        doc.addList(content)
        assert doc.odfSimpleWrapper.lastNode.nodeName.equals("text:list")
    }
}
