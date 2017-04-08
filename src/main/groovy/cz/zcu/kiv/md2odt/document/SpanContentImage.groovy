package cz.zcu.kiv.md2odt.document

/**
 *
 * @version 2017-04-08
 * @author Patrik Harag
 */
class SpanContentImage implements SpanContent {

    // ![<alt>](<url> <text>)

    final String text  // usually shows on hover
    final String url
    final String alt

    final SpanType type = SpanType.IMAGE

    SpanContentImage(String text, String url, String alt) {
        this.text = text
        this.url = url
        this.alt = alt
    }
}
