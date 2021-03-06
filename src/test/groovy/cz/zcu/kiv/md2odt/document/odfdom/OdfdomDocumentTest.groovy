package cz.zcu.kiv.md2odt.document.odfdom

import cz.zcu.kiv.md2odt.document.ListContent
import cz.zcu.kiv.md2odt.document.ListContentBuilder
import cz.zcu.kiv.md2odt.document.ListType
import cz.zcu.kiv.md2odt.document.ParagraphContent
import cz.zcu.kiv.md2odt.document.ParagraphContentBuilder
import cz.zcu.kiv.md2odt.document.SpanContent
import cz.zcu.kiv.md2odt.document.SpanContentText
import cz.zcu.kiv.md2odt.document.SpanType
import cz.zcu.kiv.md2odt.document.TableCellContent
import cz.zcu.kiv.md2odt.document.TableContentBuilder
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.odftoolkit.odfdom.dom.element.text.TextPElement
import org.odftoolkit.simple.table.Cell
import org.odftoolkit.simple.table.Table
import org.odftoolkit.simple.text.list.List as OdfList
import org.odftoolkit.simple.text.list.ListContainer

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
        last.switchToLastChild()
        assert last.nodeName.equals("text:span")
        assert !last.textStyleName.equals("")
        assert last.textContent.equals("bold")
    }

    @Test
    void addParagraphItalicTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addItalic("italic").build()
        doc.addParagraph(pc)
        last.switchToLastChild()
        assert last.nodeName.equals("text:span")
        assert !last.textStyleName.equals("")
        assert last.textContent.equals("italic")
    }

    @Test
    void addHeadingItalicTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addItalic("italic").build()
        doc.addHeading(pc, 1)
        assert last.nodeName.equals("text:h")
        last.switchToLastChild()
        assert last.nodeName.equals("text:span")
        assert !last.textStyleName.equals("")
        assert last.textContent.equals("italic")
    }

    @Test
    void addParagraphLinkWrongTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addLink("pokus", "www.sez#nam.cz").build()
        doc.addParagraph(pc)
        last.switchToLastChild()
        assert last.nodeName.equals("text:a")
        assert last.getXLinkHref().equals("www.sez#nam.cz")
        assert last.getXLinkType().equals("simple")
        assert last.textContent.equals("pokus")
    }

    @Test
    void addParagraphLinkOkTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addLink("pokus", "http://www.seznam.cz").build()
        doc.addParagraph(pc)
        last.switchToLastChild()
        assert last.nodeName.equals("text:a")
        assert last.getXLinkHref().equals("http://www.seznam.cz")
        assert last.getXLinkType().equals("simple")
        assert last.textContent.equals("pokus")
    }

    @Test
    void addParagraphInlineCodeTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addCode("n();").build()
        doc.addParagraph(pc)
        last.switchToLastChild()
        assert last.nodeName.equals("text:span")
        assert last.textContent.equals("n();")
        assert !last.textStyleName.equals("")
    }

    @Test
    void addParagraphImageOkTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addImage("text", IMAGE, "alt").build()
        doc.addParagraph(pc)
        last.switchToLastChild()
        assert last.nodeName.equals("draw:frame")
        assert last.textContent.equals("")
        assert !last.drawStyleName.equals("")
        assert last.textAnchorType.equals("as-char")
        last.switchToLastChild()
        assert last.nodeName.equals("draw:image")
        assert last.textContent.equals("")
        assert last.getXLinkHref().contains("image.png")
        assert last.getXLinkType().equals("simple")
    }

    @Test
    void addParagraphImageExceptionTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addImage("text", "url", "alt").build()
        doc.addParagraph(pc)
        last.switchToLastChild()
        assert last.nodeName.equals("#text")
        assert last.textContent.equals("alt")
    }

    @Test
    void addParagraphImageFromStreamOkTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addImage("text", IMAGE, "alt", new FileInputStream(IMAGE)).build()
        doc.addParagraph(pc)
        last.switchToLastChild()
        assert last.nodeName.equals("draw:frame")
        assert last.textContent.equals("")
        assert !last.drawStyleName.equals("")
        assert last.textAnchorType.equals("as-char")
        last.switchToLastChild()
        assert last.nodeName.equals("draw:image")
        assert last.textContent.equals("")
        assert last.getXLinkHref().contains("image.png")
        assert last.getXLinkType().equals("simple")
    }

    @Test
    void addParagraphImageFromStreamExceptionTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addImage("text", IMAGE, "alt", new FileInputStream(IMAGE).close()).build()
        doc.addParagraph(pc)
        last.switchToLastChild()
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
    void addCodeBlockNoHighlihtTest() throws Exception {
        doc.addCodeBlock("code", null)
        assert last.nodeName.equals("text:p")
        assert last.textStyleName.equals(StyleNames.CODE.getValue())
        last.switchToLastChild()
        assert last.nodeS.equals("[#text: code]")
    }

    @Test
    void addCodeBlockHighlightTest() throws Exception {
        doc.addCodeBlock("code", "java")
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
        last.switchToLastChild()
        assert last.nodeName.equals("text:list-item")
        last.switchToLastChild()
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
        last.switchToLastChild()
        assert last.nodeName.equals("text:list-item")
        last.switchToLastChild()
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
        last.switchToLastChild()
        assert last.nodeName.equals("text:list-item")
        last.switchToLastChild()
        assert last.nodeName.equals("text:list")
        last.switchToLastChild()
        assert last.nodeName.equals("text:list-item")
        last.switchToLastChild()
        assert last.nodeName.equals("text:p")
        assert last.textStyleName.equals(StyleNames.LIST.getValue())
        assert last.textContent.equals("bla2")
    }

    @Test
    void addTable() {
        def pc = ParagraphContentBuilder.builder().addRegular("text").build()
        List<TableCellContent> ltcc = [
                new TableCellContent(pc, TableCellContent.Align.LEFT, false),
                new TableCellContent(pc, TableCellContent.Align.CENTER, true),
                new TableCellContent(pc, TableCellContent.Align.RIGHT, false)
        ]
        def tc = TableContentBuilder.builder().addRow(ltcc).build()
        doc.addTable(tc)
        assert last.node.textContent.equals("texttexttext")
        assert last.nodeName.equals("table:table")
        last.switchToLastChild()
        assert last.nodeName.equals("table:table-row")
        last.switchToLastChild()
        assert last.nodeName.equals("table:table-cell")
        last.switchToLastChild()
        assert last.nodeName.equals("text:p")
        assert last.textContent.equals("text")
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
