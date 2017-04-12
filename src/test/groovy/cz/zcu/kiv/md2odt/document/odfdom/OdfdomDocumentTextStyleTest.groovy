package cz.zcu.kiv.md2odt.document.odfdom

import cz.zcu.kiv.md2odt.document.ParagraphContent
import cz.zcu.kiv.md2odt.document.SpanContent
import cz.zcu.kiv.md2odt.document.SpanContentText
import cz.zcu.kiv.md2odt.document.TextStyle
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

/**
 * Created by pepe on 12. 4. 2017.
 */
@Ignore
class OdfdomDocumentTextStyleTest {
    OdfdomDocument doc
    LastNode last
    ParagraphContent pc

    @Before
    void setUp() throws Exception {
        doc = new OdfdomDocument()
        last = new LastNode(doc)
        pc = new ParagraphContent() {
            List<SpanContent> list = new ArrayList<SpanContent>(1)

            @Override
            List<SpanContent> getList() {
                return list
            }
        }
    }

    @After
    void tearDown() throws Exception {
        doc.save("test.odt")
    }

    @Test
    void regular() throws Exception {
        pc.getList().add(new SpanContentText("text", [] as Set))
        doc.addParagraph(pc)
        println(last.nodeS)
    }

    @Test
    void italic() throws Exception {
        pc.getList().add(new SpanContentText("text", [TextStyle.ITALIC] as Set))
        doc.addParagraph(pc)
        println(last.nodeS)
    }

    @Test
    void bold() throws Exception {
        pc.getList().add(new SpanContentText("text", [TextStyle.BOLD] as Set))
        doc.addParagraph(pc)
        println(last.nodeS)
    }

    @Test
    void code() throws Exception {
        pc.getList().add(new SpanContentText("text", [TextStyle.CODE] as Set))
        doc.addParagraph(pc)
        println(last.nodeS)
    }

    @Test
    void strike() throws Exception {
        pc.getList().add(new SpanContentText("text", [TextStyle.STRIKE] as Set))
        doc.addParagraph(pc)
        println(last.nodeS)
    }

    @Test
    void subScript() throws Exception {
        pc.getList().add(new SpanContentText("text", [TextStyle.SUBSCRIPT] as Set))
        doc.addParagraph(pc)
        println(last.nodeS)
    }

    @Test
    void superScript() throws Exception {
        pc.getList().add(new SpanContentText("text", [TextStyle.SUPERSCRIPT] as Set))
        doc.addParagraph(pc)
        println(last.nodeS)
    }

    @Test
    void multiStyle() throws Exception {
        pc.getList().add(new SpanContentText("text", [TextStyle.SUPERSCRIPT, TextStyle.BOLD, TextStyle.ITALIC, TextStyle.STRIKE, TextStyle.CODE] as Set))
        doc.addParagraph(pc)
        println(last.nodeS)
    }
}
