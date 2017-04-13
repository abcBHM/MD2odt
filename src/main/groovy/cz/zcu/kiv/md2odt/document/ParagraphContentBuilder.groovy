package cz.zcu.kiv.md2odt.document

/**
 * Builder for {@link ParagraphContent}.
 *
 * @version 2017-04-13
 * @author Patrik Harag
 */
class ParagraphContentBuilder {

    private static class ParagraphContentImpl implements ParagraphContent {
        final List<SpanContent> list

        ParagraphContentImpl(List<SpanContent> list) {
            this.list = new ArrayList<>(list)
        }

        @Override
        List<SpanContent> getList() {
            return Collections.unmodifiableList(list)
        }
    }

    static ParagraphContentBuilder builder() {
        new ParagraphContentBuilder()
    }


    private final List<SpanContent> buffer = []

    private ParagraphContentBuilder() { }

    ParagraphContentBuilder add(SpanContent span) {
        buffer.add(span)
        return this
    }

    ParagraphContentBuilder addText(String text, Set<TextStyle> styles) {
        // try append text after the last span
        def last = buffer ? buffer[-1] : null
        if (last && last.type == SpanType.TEXT && last.styles == styles)
            buffer[-1] = new SpanContentText(last.text + text, styles)
        else
            add(new SpanContentText(text, styles))

        return this
    }

    ParagraphContentBuilder addRegular(String text) {
        addText(text, [] as Set)
    }

    ParagraphContentBuilder addItalic(String text) {
        addText(text, [TextStyle.ITALIC] as Set)
    }

    ParagraphContentBuilder addBold(String text) {
        addText(text, [TextStyle.BOLD] as Set)
    }

    ParagraphContentBuilder addCode(String text) {
        addText(text, [TextStyle.CODE] as Set)
    }

    ParagraphContentBuilder addLink(String text, String url) {
        add(new SpanContentLink(builder().addRegular(text).build(), url))
    }

    ParagraphContentBuilder addLink(ParagraphContent content, String url) {
        add(new SpanContentLink(content, url))
    }

    ParagraphContentBuilder addImage(String text, String url, String alt) {
        add(new SpanContentImage(text, url, alt))
    }

    ParagraphContentBuilder addImage(String text, String url, String alt, InputStream is) {
        add(new SpanContentImageLocal(text, url, alt, is))
    }

    ParagraphContent build() {
        new ParagraphContentImpl(buffer)
    }

}
