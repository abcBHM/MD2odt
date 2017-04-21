package cz.zcu.kiv.md2odt.filler.md

import cz.zcu.kiv.md2odt.document.Document
import cz.zcu.kiv.md2odt.document.ParagraphContent
import org.junit.Test

/**
 *
 * @version 2017-04-21
 * @author Patrik Harag
 */
class FillerHeadingsTest {

    def filler = new FlexMarkFiller()

    @Test
    void standard() {
        def out = []
        def documentMock = [addHeading: { s, l -> out.push([s, l]) }] as Document

        def code = """
            # H1
            ## H2
            ### H3
            #### H4
            ##### H5
            ###### H6
        """.stripIndent()

        filler.fill(code, documentMock)

        assert out.size() == 6

        (0..<6).each { i ->
            assert out[i][0] instanceof ParagraphContent
            assert out[i][1] == i + 1
        }
    }

    @Test
    void underline() {
        def out = []
        def documentMock = [addHeading: { s, l -> out.push([s, l]) }] as Document

        def code = """
            H1
            ======
            
            H2
            ------
        """.stripIndent()

        filler.fill(code, documentMock)

        assert out.size() == 2

        assert out[0][0] instanceof ParagraphContent
        assert out[0][1] == 1

        assert out[1][0] instanceof ParagraphContent
        assert out[1][1] == 2
    }

    @Test
    void formatted() {
        def out = []
        def documentMock = [addHeading: { s, l -> out.push([s, l]) }] as Document

        def code = """
            # *one* **two**
        """.stripIndent()

        filler.fill(code, documentMock)

        assert out.size() == 1

        assert out[0][0] instanceof ParagraphContent
        assert out[0][1] == 1
    }

}
