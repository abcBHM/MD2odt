package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ext.emoji.EmojiExtension
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension
import org.junit.Test

import static cz.zcu.kiv.md2odt.document.TextStyle.*
import static cz.zcu.kiv.md2odt.filler.md.ParagraphCollectorTest.paragraph
import static cz.zcu.kiv.md2odt.filler.md.ParagraphCollectorTest.styles

/**
 *
 * @version 2017-04-12
 * @author Patrik Harag
 */
class ParagraphCollectorExtTest {

    static def EXT_STRIKE = [StrikethroughExtension.create()]
    static def EXT_EMOJI = [EmojiExtension.create()]

    @Test
    void strike() {
        def paragraph = paragraph("~~strike~~", EXT_STRIKE)

        assert paragraph.list*.text == ["strike"]
        assert paragraph.list*.styles == styles([STRIKE])
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

}
