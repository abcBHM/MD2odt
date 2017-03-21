package cz.zcu.kiv.md2odt.filler.md

import cz.zcu.kiv.md2odt.document.Document
import org.junit.Test

/**
 *
 * @version 2017-03-21
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
            assert out[i] == ["H${i + 1}", i + 1]
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
        assert out[0] == ["H1", 1]
        assert out[1] == ["H2", 2]
    }

    @Test
    void formatted() {
        def out = []
        def documentMock = [addHeading: { s, l -> out.push([s, l]) }] as Document

        def code = """
            # *one* **two**
        """.stripIndent()

        filler.fill(code, documentMock)

        // formatting in heading is not supported but it should print something...
        assert out.size() == 1
        assert out[0] == ["one two", 1]
    }

}
