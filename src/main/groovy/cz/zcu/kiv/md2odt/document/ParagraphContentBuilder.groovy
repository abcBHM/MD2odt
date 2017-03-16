package cz.zcu.kiv.md2odt.document

/**
 * Builder for {@link ParagraphContent}.
 *
 * @version 2017-03-16
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

    ParagraphContentBuilder addText(String text, SpanType type) {
        // try append text after the last span
        if (buffer && buffer[-1].type == type)
            buffer[-1] = new SpanContentText(buffer[-1].text + text, type)
        else
            add(new SpanContentText(text, type))

        return this
    }

    ParagraphContentBuilder addRegular(String text) {
        addText(text, SpanType.REGULAR)
    }

    ParagraphContentBuilder addItalic(String text) {
        addText(text, SpanType.ITALIC)
    }

    ParagraphContentBuilder addBold(String text) {
        addText(text, SpanType.BOLD)
    }

    ParagraphContentBuilder addCode(String text) {
        addText(text, SpanType.CODE)
    }

    ParagraphContentBuilder addLink(String text, String url) {
        add(new SpanContentLink(text, url, SpanType.LINK))
    }

    ParagraphContent build() {
        new ParagraphContentImpl(buffer)
    }

}
