package cz.zcu.kiv.md2odt.filler.md

import cz.zcu.kiv.md2odt.document.Document
import cz.zcu.kiv.md2odt.document.ParagraphContent
import org.junit.Test

/**
 *
 * @version 2017-03-21
 * @author Patrik Harag
 */
class FillerRestTest {

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

    @Test
    void horizontalRule() {
        int i = 0
        def documentMock = [addHorizontalRule: { i++ }] as Document

        filler.fill("___\n---\n***\n", documentMock)

        assert i == 3
    }

}
