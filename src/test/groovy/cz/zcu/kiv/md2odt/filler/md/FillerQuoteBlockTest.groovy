package cz.zcu.kiv.md2odt.filler.md

import cz.zcu.kiv.md2odt.document.Document
import org.junit.Test

/**
 *
 * @version 2017-04-04
 * @author Patrik Harag
 */
class FillerQuoteBlockTest {

    def filler = new FlexMarkFiller()

    @Test
    void quoteBlock() {
        def list = []
        def documentMock = [addQuoteBlock: { list.add(it) }] as Document

        filler.fill("""
            > quoted
            > block
        """.stripIndent(), documentMock)

        assert list.size() == 1
        list[0].with {
            assert it.size() == 1  // one paragraph
            assert it[0].list*.text == ['quoted block']
        }
    }

}
