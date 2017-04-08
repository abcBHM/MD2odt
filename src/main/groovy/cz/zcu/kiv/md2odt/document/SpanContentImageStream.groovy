package cz.zcu.kiv.md2odt.document

/**
 *
 * @version 2017-04-08
 * @author Patrik Harag
 */
class SpanContentImageStream implements SpanContent {

    final String text
    final InputStream stream
    final String alt

    final SpanType type = SpanType.IMAGE

    SpanContentImageStream(String text, InputStream stream, String alt) {
        this.text = text
        this.stream = stream
        this.alt = alt
    }
}
