package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ext.emoji.EmojiExtension
import com.vladsch.flexmark.ext.escaped.character.EscapedCharacterExtension
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension
import com.vladsch.flexmark.ext.gfm.strikethrough.SubscriptExtension
import com.vladsch.flexmark.superscript.SuperscriptExtension
import org.junit.Test

import static cz.zcu.kiv.md2odt.document.TextStyle.*
import static cz.zcu.kiv.md2odt.filler.md.ParagraphCollectorTest.paragraph
import static cz.zcu.kiv.md2odt.filler.md.ParagraphCollectorTest.styles

/**
 *
 * @version 2017-04-13
 * @author Patrik Harag
 */
class ParagraphCollectorExtTest {

    static def EXT_STRIKE = [StrikethroughExtension.create()]
    static def EXT_SUB = [SubscriptExtension.create()]
    static def EXT_SUP = [SuperscriptExtension.create()]
    static def EXT_EMOJI = [EmojiExtension.create()]
    static def EXT_ESC = [EscapedCharacterExtension.create()]

    @Test
    void strike() {
        def paragraph = paragraph("~~strike~~", EXT_STRIKE)

        assert paragraph.list*.text == ["strike"]
        assert paragraph.list*.styles == styles([STRIKE])
    }

    @Test
    void sub() {
        def paragraph = paragraph("~text~", EXT_SUB)

        assert paragraph.list*.text == ["text"]
        assert paragraph.list*.styles == styles([SUBSCRIPT])
    }

    @Test
    void sup() {
        def paragraph = paragraph("^text^", EXT_SUP)

        assert paragraph.list*.text == ["text"]
        assert paragraph.list*.styles == styles([SUPERSCRIPT])
    }

    @Test
    void emoji() {
        def paragraph = paragraph(":smile:", EXT_EMOJI)

        assert paragraph.list*.text == ["😄"]
        assert paragraph.list*.styles == styles([])
    }

    @Test
    void emojiWithStyle() {
        def paragraph = paragraph("_:smile:_", EXT_EMOJI)

        assert paragraph.list*.text == ["😄"]
        assert paragraph.list*.styles == styles([ITALIC])
    }

    @Test
    void escaping() {
        def paragraph = paragraph("\\\\ \\` \\* \\_ \\{\\} \\[\\] \\(\\) \\#", EXT_ESC)

        assert paragraph.list*.text == ["\\ ` * _ {} [] () #"]
        assert paragraph.list*.styles == styles([])
    }

    @Test
    void escapingStyled() {
        def paragraph = paragraph("*\\\\ \\` \\**", EXT_ESC)

        assert paragraph.list*.text == ["\\ ` *"]
        assert paragraph.list*.styles == styles([ITALIC])
    }

}
