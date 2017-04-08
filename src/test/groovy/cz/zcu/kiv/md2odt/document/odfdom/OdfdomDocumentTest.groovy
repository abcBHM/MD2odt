package cz.zcu.kiv.md2odt.document.odfdom

import cz.zcu.kiv.md2odt.document.ListContent
import cz.zcu.kiv.md2odt.document.ListContentBuilder
import cz.zcu.kiv.md2odt.document.ListType
import cz.zcu.kiv.md2odt.document.ParagraphContent
import cz.zcu.kiv.md2odt.document.ParagraphContentBuilder
import cz.zcu.kiv.md2odt.document.SpanContent
import cz.zcu.kiv.md2odt.document.SpanContentText
import cz.zcu.kiv.md2odt.document.SpanType
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameter
import org.odftoolkit.odfdom.dom.element.text.TextPElement
import org.odftoolkit.odfdom.dom.element.text.TextParagraphElementBase
import org.w3c.dom.Node

import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by pepe on 5. 4. 2017.
 */
class OdfdomDocumentTest {
    private static final String IMAGE = 'src/test/resources/image.png'
    private static final String TEST = 'test.odt'

    OdfdomDocument doc
    LastNode last

    @Before
    void setUp() throws Exception {
        doc = new OdfdomDocument()
        last = new LastNode(doc)
    }

    @Test
    void addParagraphRegularTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addRegular("regular").build()
        doc.addParagraph(pc)
        assert last.nodeName.equals("text:p")
        assert last.textStyleName.equals(StyleNames.BODY_TEXT.getValue())
        assert last.textContent.equals("regular")
    }

    @Test
    void addParagraphBoldTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addBold("bold").build()
        doc.addParagraph(pc)
        last.switchToLastChilde()
        assert last.nodeName.equals("text:span")
        assert !last.textStyleName.equals("")
        assert last.textContent.equals("bold")
    }

    @Test
    void addParagraphItalicTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addItalic("italic").build()
        doc.addParagraph(pc)
        last.switchToLastChilde()
        assert last.nodeName.equals("text:span")
        assert !last.textStyleName.equals("")
        assert last.textContent.equals("italic")
    }

    @Test
    void addParagraphLinkWrongTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addLink("pokus", "www.sez#nam.cz").build()
        doc.addParagraph(pc)
        last.switchToLastChilde()
        assert last.nodeName.equals("text:a")
        assert last.getXLinkHref().equals("www.sez#nam.cz")
        assert last.getXLinkType().equals("simple")
        assert last.textContent.equals("pokus")
    }

    @Test
    void addParagraphLinkOkTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addLink("pokus", "http://www.seznam.cz").build()
        doc.addParagraph(pc)
        last.switchToLastChilde()
        assert last.nodeName.equals("text:a")
        assert last.getXLinkHref().equals("http://www.seznam.cz")
        assert last.getXLinkType().equals("simple")
        assert last.textContent.equals("pokus")
    }

    @Test
    void addParagraphInlineCodeTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addCode("n();").build()
        doc.addParagraph(pc)
        last.switchToLastChilde()
        assert last.nodeName.equals("text:span")
        assert last.textContent.equals("n();")
        assert !last.textStyleName.equals("")
    }

    @Test
    void addParagraphImageOkTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addImage("text", IMAGE, "alt").build()
        doc.addParagraph(pc)
        last.switchToLastChilde()
        assert last.nodeName.equals("draw:frame")
        assert last.textContent.equals("")
        assert !last.drawStyleName.equals("")
        assert last.textAnchorType.equals("as-char")
        last.switchToLastChilde()
        assert last.nodeName.equals("draw:image")
        assert last.textContent.equals("")
        assert last.getXLinkHref().contains("image.png")
        assert last.getXLinkType().equals("simple")
    }

    @Test
    void addParagraphImageExceptionTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addImage("text", "url", "alt").build()
        doc.addParagraph(pc)
        last.switchToLastChilde()
        assert last.nodeName.equals("#text")
        assert last.textContent.equals("alt")
    }

    @Test
    void addCodeBlockTest() throws Exception {
        doc.addCodeBlock("code")
        assert last.nodeName.equals("text:p")
        assert last.textStyleName.equals(StyleNames.CODE.getValue())
        assert last.textContent.equals("code")
    }

    @Test
    void addQuoteBlockTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addRegular("regular").build()
        doc.addQuoteBlock([pc])
        assert last.nodeName.equals("text:p")
        assert last.textStyleName.equals(StyleNames.QUOTE.getValue())
        assert last.textContent.equals("regular")
    }

    @Test
    void addHorizontalRuleTest() throws Exception {
        doc.addHorizontalRule()
        assert last.nodeName.equals("text:p")
        assert last.textStyleName.equals(StyleNames.HORIZONTAL_RULE.getValue())
        assert last.textContent.equals("")
    }

    @Test
    void addListOrderedTest() throws Exception {
        ListContent content = ListContentBuilder.builder(ListType.ORDERED)
                .addListItem(ParagraphContentBuilder.builder().addRegular("bla1").build())
                .build()
        doc.addList(content)
        assert last.nodeName.equals("text:list")
        assert !last.textStyleName.equals("")
        last.switchToLastChilde()
        assert last.nodeName.equals("text:list-item")
        last.switchToLastChilde()
        assert last.nodeName.equals("text:p")
        assert last.textStyleName.equals(StyleNames.LIST.getValue())
        assert last.textContent.equals("bla1")
    }

    @Test
    void addListBulletTest() throws Exception {
        ListContent content = ListContentBuilder.builder(ListType.BULLET)
                .addListItem(ParagraphContentBuilder.builder().addRegular("bla1").build())
                .build()
        doc.addList(content)
        assert last.nodeName.equals("text:list")
        assert !last.textStyleName.equals("")
        last.switchToLastChilde()
        assert last.nodeName.equals("text:list-item")
        last.switchToLastChilde()
        assert last.nodeName.equals("text:p")
        assert last.textStyleName.equals(StyleNames.LIST.getValue())
        assert last.textContent.equals("bla1")
    }

    @Test
    void addMultiListTest() throws Exception {
        ListContent content = ListContentBuilder.builder(ListType.ORDERED)
                .addListItem(ParagraphContentBuilder.builder().addRegular("bla1").build())
                .addListItem(ListContentBuilder.builder(ListType.ORDERED)
                    .addListItem(ParagraphContentBuilder.builder().addRegular("bla2").build()).build())
                .build()
        doc.addList(content)
        assert last.nodeName.equals("text:list")
        last.switchToLastChilde()
        assert last.nodeName.equals("text:list-item")
        last.switchToLastChilde()
        assert last.nodeName.equals("text:list")
        last.switchToLastChilde()
        assert last.nodeName.equals("text:list-item")
        last.switchToLastChilde()
        assert last.nodeName.equals("text:p")
        assert last.textStyleName.equals(StyleNames.LIST.getValue())
        assert last.textContent.equals("bla2")
    }

    ParagraphContent getParagraphContent() {
        new ParagraphContent() {
            List<SpanContent> list = new ArrayList<SpanContent>(1)

            @Override
            List<SpanContent> getList() {
                return list
            }
        }
    }

    @Test
    void fillWithParagraphContentLinkNotLinkInstanceTest() throws Exception {
        ParagraphContent pc = getParagraphContent()
        pc.getList().add(new SpanContentText("text", SpanType.LINK))
        TextPElement par = new TextPElement(doc.odt.getContentDom())
        doc.fillWithParagraphContent(par, pc)
        assert par.textContent.equals("")
    }

    @Test
    void fillWithParagraphContentImageNotImageInstanceTest() throws Exception {
        ParagraphContent pc = getParagraphContent()
        pc.getList().add(new SpanContentText("text", SpanType.IMAGE))
        TextPElement par = new TextPElement(doc.odt.getContentDom())
        doc.fillWithParagraphContent(par, pc)
        assert par.textContent.equals("")
    }

    @Test
    void fillWithParagraphContentNotImplemented() throws Exception {
        ParagraphContent pc = getParagraphContent()
        pc.getList().add(new SpanContentText("text", null))
        TextPElement par = new TextPElement(doc.odt.getContentDom())
        doc.fillWithParagraphContent(par, pc)
        assert par.textContent.equals("")
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
}
