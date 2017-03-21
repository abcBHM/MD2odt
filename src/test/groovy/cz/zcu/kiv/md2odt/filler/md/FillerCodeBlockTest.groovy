package cz.zcu.kiv.md2odt.filler.md

import cz.zcu.kiv.md2odt.document.Document
import org.junit.Test

/**
 *
 * @version 2017-03-21
 * @author Patrik Harag
 */
class FillerCodeBlockTest {

    def filler = new FlexMarkFiller()

    @Test
    void codeBlock() {
        def out = []
        def documentMock = [addCodeBlock: { c, j -> out.push(c) }] as Document

        def code = """
            ```
            some
              code
            ```
        """.stripIndent()

        filler.fill(code, documentMock)

        assert out.size() == 1
        assert out[0] == "some\n  code"
    }

    @Test
    void codeBlockEmpty() {
        def out = []
        def documentMock = [addCodeBlock: { c, j -> out.push(c) }] as Document

        filler.fill("```\n```", documentMock)

        assert out.size() == 1
        assert out[0] == ""
    }

    @Test
    void codeBlockJavaScript() {
        def out = []
        def documentMock = [
                addCodeBlock: { c, j -> out.push([code: c, lang: j]) }
        ] as Document

        def code = """
            ```javascript
            some
              code
            ```
        """.stripIndent()

        filler.fill(code, documentMock)

        assert out.size() == 1
        assert out[0].lang == "javascript"
        assert out[0].code == "some\n  code"
    }

    @Test
    void codeBlockIndent() {
        def out = []
        def documentMock = [addCodeBlock: { c -> out.push(c) }] as Document

        filler.fill("    if (true) {\n      pass;\n    }\n", documentMock)

        assert out.size() == 1
        assert out[0] == "if (true) {\n  pass;\n}"
    }

}
