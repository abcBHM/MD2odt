package cz.zcu.kiv.md2odt.filler.md

import cz.zcu.kiv.md2odt.document.Document
import org.junit.Test

/**
 *
 * @version 2017-04-04
 * @author Patrik Harag
 */
class FillerHorizontalRuleTest {

    def filler = new FlexMarkFiller()

    @Test
    void horizontalRule() {
        int i = 0
        def documentMock = [addHorizontalRule: { i++ }] as Document

        filler.fill("___\n---\n***\n", documentMock)

        assert i == 3
    }

}
