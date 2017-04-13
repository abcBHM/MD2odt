package cz.zcu.kiv.md2odt.document

/**
 *
 * @version 2017-04-13
 * @author Patrik Harag
 */
class SpanContentLink implements SpanContent {

    final ParagraphContent content
    final String url
    final SpanType type = SpanType.LINK

    SpanContentLink(ParagraphContent content, String url) {
        this.content = content
        this.url = url
    }

    @Override
    String getText() {
        return content.list*.text.join('')
    }
}
