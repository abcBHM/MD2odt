package cz.zcu.kiv.md2odt.filler

import cz.zcu.kiv.md2odt.document.Document
import cz.zcu.kiv.md2odt.document.ParagraphContent
import cz.zcu.kiv.md2odt.filler.md.FlexMarkFiller
import org.junit.Test

/**
 *
 * @version 2017-03-17
 * @author Patrik Harag
 */
class FlexMarkFillerTest {

    def filler = new FlexMarkFiller()

    @Test
    void testParagraph() {
        // more detailed tests contains ParagraphCollectorTest

        def out = []
        def documentMock = [addParagraph: out.&push] as Document

        filler.fill("hello *world*!", documentMock)

        assert out.size() == 1
        assert out[0] instanceof ParagraphContent
        assert out[0].list*.text == ["hello ", "world", "!"]
    }

}
