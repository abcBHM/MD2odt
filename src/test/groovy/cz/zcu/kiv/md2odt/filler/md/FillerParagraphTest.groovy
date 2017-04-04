package cz.zcu.kiv.md2odt.filler.md

import cz.zcu.kiv.md2odt.document.Document
import cz.zcu.kiv.md2odt.document.ParagraphContent
import org.junit.Test

/**
 *
 * @version 2017-04-04
 * @author Patrik Harag
 */
class FillerParagraphTest {

    def filler = new FlexMarkFiller()

    @Test
    void paragraph() {
        // more detailed tests contains ParagraphCollectorTest

        def out = []
        def documentMock = [addParagraph: out.&push] as Document

        filler.fill("hello *world*!", documentMock)

        assert out.size() == 1
        assert out[0] instanceof ParagraphContent
        assert out[0].list*.text == ["hello ", "world", "!"]
    }

}
