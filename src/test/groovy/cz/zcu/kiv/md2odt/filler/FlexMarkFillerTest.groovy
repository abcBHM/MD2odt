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

    @Test
    void quoteBlock() {
        List<ParagraphContent> out = []
        def documentMock = [addQuoteBlock: out.&push] as Document

        filler.fill("""
            > quote 1
            >
            > *quote* 2.1
            > quote 2.2
        """.stripIndent(), documentMock)

        assert out.size() == 1
        assert out[0].list*.text == [["quote 1"], ["quote", " 2.1\nquote 2.2"]]
    }

}
