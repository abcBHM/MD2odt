package cz.zcu.kiv.md2odt.document

/**
 *
 * @version 2017-03-15
 * @author Patrik Harag
 */
class SpanContentLink implements SpanContent {

    String text
    String url
    final SpanType type = SpanType.LINK

    SpanContentLink(String text, String url) {
        this.text = text
        this.url = url
    }
}
