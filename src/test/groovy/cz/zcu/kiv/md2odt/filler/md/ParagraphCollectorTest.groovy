package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ast.Document
import com.vladsch.flexmark.ast.Paragraph
import com.vladsch.flexmark.parser.Parser
import cz.zcu.kiv.md2odt.document.ParagraphContent
import org.junit.Test

import static cz.zcu.kiv.md2odt.document.SpanType.*

/**
 *
 * @version 2017-04-02
 * @author Patrik Harag
 */
class ParagraphCollectorTest {

    static ParagraphContent paragraph(String md) {
        def parser = Parser.builder().build()
        def document = parser.parse(md)
        def astParagraph = document.children[0] as Paragraph

        def collector = new ParagraphCollector()
        def context = Context.of(document as Document)
        return collector.processParagraph(astParagraph, context)
    }

    @Test
    void simpleFormatting1() {
        def paragraph = paragraph("This is *Sparta*!")

        assert paragraph.list*.text == ["This is ", "Sparta", "!"]
        assert paragraph.list*.type == [REGULAR, ITALIC, REGULAR]
    }

    @Test
    void simpleFormatting2() {
        def paragraph = paragraph("a*b***c**")

        assert paragraph.list*.text == ["a", "b", "c"]
        assert paragraph.list*.type == [REGULAR, ITALIC, BOLD]
    }

    @Test
    void lineBreak() {
        def paragraph = paragraph("a\nb")

        // AST:
        // class com.vladsch.flexmark.ast.Paragraph
        //  class com.vladsch.flexmark.ast.Text
        //  class com.vladsch.flexmark.ast.SoftLineBreak
        //  class com.vladsch.flexmark.ast.Text

        assert paragraph.list*.text == ["a\nb"]
        assert paragraph.list*.type == [REGULAR]
    }

    @Test
    void lineBreakNested() {
        def paragraph = paragraph("*a\nb*")

        assert paragraph.list*.text == ["a\nb"]
        assert paragraph.list*.type == [ITALIC]
    }

    @Test
    void nestedFormatting() {
        def paragraph = paragraph("*a**b***")

        assert paragraph.list*.text == ["ab"]
        assert paragraph.list*.type == [ITALIC]

        // TODO: nested formatting is ignored
        // assert paragraph.list*.text == ["a", "b"]
        // assert paragraph.list*.type == [ITALIC, BOLD]
    }

    @Test
    void inlineCode() {
        def paragraph = paragraph("text`code`text")

        assert paragraph.list*.text == ["text", "code", "text"]
        assert paragraph.list*.type == [REGULAR, CODE, REGULAR]
    }

    @Test
    void link() {
        def paragraph = paragraph("[link](www.example.com)")

        assert paragraph.list*.text == ["link"]
        assert paragraph.list*.type == [LINK]
        assert paragraph.list*.url  == ["www.example.com"]
    }

    @Test
    void linkFormatted() {
        def paragraph = paragraph("[**link**](www.example.com)")

        // TODO: link format is ignored
        assert paragraph.list*.text == ["link"]
        assert paragraph.list*.type == [LINK]
        assert paragraph.list*.url  == ["www.example.com"]
    }

    @Test
    void autoLink() {
        def paragraph = paragraph("<http://www.url.net/>")

        assert paragraph.list*.text == ["http://www.url.net/"]
        assert paragraph.list*.type == [LINK]
        assert paragraph.list*.url  == ["http://www.url.net/"]
    }

    @Test
    void mailLink() {
        def paragraph = paragraph("<address@example.com>")

        assert paragraph.list*.text == ["address@example.com"]
        assert paragraph.list*.type == [LINK]
        assert paragraph.list*.url  == ["mailto:address@example.com"]
    }

    @Test
    void linkRef() {
        def paragraph = paragraph('''
            [Google][1]

            [1]: http://google.com
        '''.stripIndent())

        assert paragraph.list*.text == ["Google"]
        assert paragraph.list*.type == [LINK]
        assert paragraph.list*.url  == ["http://google.com"]
    }

    @Test
    void linkRefWithoutText() {
        def paragraph = paragraph('''
            [1]

            [1]: http://google.com
        '''.stripIndent())

        assert paragraph.list*.text == ["1"]
        assert paragraph.list*.type == [LINK]
        assert paragraph.list*.url  == ["http://google.com"]
    }

    @Test
    void htmlEntity() {
        def paragraph = paragraph("&amp;")

        assert paragraph.list*.text == ["&"]
        assert paragraph.list*.type == [REGULAR]
    }

    @Test
    void htmlComment() {
        def paragraph = paragraph("ab<!-- comment -->*cd*")

        assert paragraph.list*.text == ["ab", "cd"]
        assert paragraph.list*.type == [REGULAR, ITALIC]
    }

    @Test
    void image() {
        def paragraph = paragraph('![alt text](img.png "title")')

        assert paragraph.list*.text == ["title"]
        assert paragraph.list*.url == ["img.png"]
        assert paragraph.list*.alt == ["alt text"]
        assert paragraph.list*.type == [IMAGE]
    }

    @Test
    void imageNoTitle() {
        def paragraph = paragraph('![alt text](img.png)')

        assert paragraph.list*.text == [""]
        assert paragraph.list*.url == ["img.png"]
        assert paragraph.list*.alt == ["alt text"]
        assert paragraph.list*.type == [IMAGE]
    }

    @Test
    void imageRef() {
        def paragraph = paragraph('''
            ![alt text][pic]

            [pic]: https://www.example.com/img.png "title"
        '''.stripIndent())

        assert paragraph.list*.text == ["title"]
        assert paragraph.list*.url == ["https://www.example.com/img.png"]
        assert paragraph.list*.alt == ["alt text"]
        assert paragraph.list*.type == [IMAGE]
    }

}
