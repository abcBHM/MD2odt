package cz.zcu.kiv.md2odt.document.odt

import cz.zcu.kiv.md2odt.document.ParagraphContent
import cz.zcu.kiv.md2odt.document.ParagraphContentBuilder
import cz.zcu.kiv.md2odt.document.SpanContent
import cz.zcu.kiv.md2odt.document.SpanContentLink
import cz.zcu.kiv.md2odt.document.SpanContentText
import cz.zcu.kiv.md2odt.document.SpanType
import org.junit.Before
import org.junit.Test

/**
 * Created by pepe on 3. 4. 2017.
 */
class OdfSimpleDocumentParToStringTest {
    OdfSimpleDocument doc
    ParagraphContent pc

    @Before
    void setUp() throws Exception {
        pc = new ParagraphContent() {
            List<SpanContent> list = new ArrayList<SpanContent>(1)

            @Override
            List<SpanContent> getList() {
                return list
            }
        }
        doc = new OdfSimpleDocument()
    }
    @Test
    void parToStringTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addRegular("regular ").addBold("bold ").addItalic("italic ").build()
        assert doc.parToString(pc).equals("regular >bold ><italic <")
    }

    @Test
    void parToStringLinkTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addLink("pokus", "www.seznam.cz").build()
        assert doc.parToString(pc).equals("#www.seznam.cz@pokus#")
    }

    @Test
    void parToStringLinkNotLinkInstanceTest() throws Exception {
        pc.getList().add(new SpanContentText("text", SpanType.LINK))
        assert doc.parToString(pc).equals("text")
    }

    @Test
    void parToStringImageTest() throws Exception {
        def pc = ParagraphContentBuilder.builder().addImage("text", "url", "alt").build()
        assert doc.parToString(pc).equals(OdfSimpleConstants.IMAGE.getMark()+"url@alt@text"+OdfSimpleConstants.IMAGE.getMark())
    }

    @Test
    void parToStringImageNotImageInstanceTest() throws Exception {
        pc.getList().add(new SpanContentText("text", SpanType.IMAGE))
        assert doc.parToString(pc).equals("text")
    }

    @Test
    void parToStringUnsupportedOperation() throws Exception {
        pc.getList().add(new SpanContentText("text", null))
        assert doc.parToString(pc).equals("")
    }
}
