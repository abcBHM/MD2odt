package cz.zcu.kiv.md2odt.document

/**
 *
 * @version 2017-04-08
 * @author Patrik Harag
 */
class SpanContentImageStream extends SpanContentImage {

    final InputStream stream

    SpanContentImageStream(String text, String url, String alt, InputStream is) {
        super(text, url, alt)
        this.stream = is
    }
}
