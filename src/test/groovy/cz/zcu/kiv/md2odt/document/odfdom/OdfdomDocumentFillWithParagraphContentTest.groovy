package cz.zcu.kiv.md2odt.document.odfdom

import cz.zcu.kiv.md2odt.document.ParagraphContent
import cz.zcu.kiv.md2odt.document.SpanContent
import cz.zcu.kiv.md2odt.document.SpanContentText
import cz.zcu.kiv.md2odt.document.SpanType
import org.junit.Before
import org.junit.Test
import org.odftoolkit.odfdom.dom.element.text.TextPElement

/**
 * Created by pepe on 12. 4. 2017.
 */
class OdfdomDocumentFillWithParagraphContentTest {
    OdfdomDocument doc
    LastNode last
    ParagraphContent pc
    TextPElement par

    @Before
    void setUp() throws Exception {
        doc = new OdfdomDocument()
        last = new LastNode(doc)
        par = new TextPElement(doc.odt.getContentDom())
        pc = new ParagraphContent() {
            List<SpanContent> list = new ArrayList<SpanContent>(1)

            @Override
            List<SpanContent> getList() {
                return list
            }
        }
    }

    SpanContent getSpanContent(String text, SpanType type) {
        new SpanContent() {
            @Override
            String getText() {
                return text
            }

            @Override
            SpanType getType() {
                return type
            }
        }
    }

    void fill(SpanType type) {
        pc.getList().add(getSpanContent("text", type))
        doc.fillWithParagraphContent(par, pc)
    }

    @Test
    void linkNotLinkInstanceTest() throws Exception {
        fill(SpanType.LINK)
        assert par.textContent.equals("")
    }

    @Test
    void imageNotImageInstanceTest() throws Exception {
        fill(SpanType.IMAGE)
        assert par.textContent.equals("")
    }

    @Test
    void textNotTextInstanceTest() throws Exception {
        fill(SpanType.TEXT)
        assert par.textContent.equals("")
    }

    @Test
    void notImplementedTest() throws Exception {
        fill(null)
        assert par.textContent.equals("")
    }

    @Test
    void nullParagraphContentTest() throws Exception {
        doc.fillWithParagraphContent(par, null)
        assert par.textContent.equals("")
    }

    @Test
    void nullSpanContentTest() throws Exception {
        pc.getList().add(null)
        doc.fillWithParagraphContent(par, pc)
        assert par.textContent.equals("")
    }

    @Test
    void nullTextStyleTest() throws Exception {
        pc.getList().add(new SpanContentText("text", [null] as Set))
        doc.fillWithParagraphContent(par, pc)
        assert par.textContent.equals("text")
    }
}
