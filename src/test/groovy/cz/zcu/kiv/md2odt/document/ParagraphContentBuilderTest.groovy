package cz.zcu.kiv.md2odt.document

import org.junit.Test

/**
 *
 * @version 2017-03-15
 * @author Patrik Harag
 */
class ParagraphContentBuilderTest {

    def par = ParagraphContentBuilder.builder()
            .addRegular("regular")
            .addItalic("italic")
            .addBold("bold")
            .build()

    @Test
    void testClasses() {
        assert par.list[0].type == SpanType.REGULAR
        assert par.list[1].type == SpanType.ITALIC
        assert par.list[2].type == SpanType.BOLD
    }

    @Test
    void testContent() {
        assert par.list[0].text == "regular"
        assert par.list[1].text == "italic"
        assert par.list[2].text == "bold"
    }

}
