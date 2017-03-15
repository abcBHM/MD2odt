package cz.zcu.kiv.md2odt.document

/**
 * Builder for {@link ParagraphContent}.
 *
 * @version 2017-03-15
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
        this
    }

    ParagraphContentBuilder addRegular(String text) {
        add(new SpanContentText(text, SpanType.REGULAR))
    }

    ParagraphContentBuilder addItalic(String text) {
        add(new SpanContentText(text, SpanType.ITALIC))
    }

    ParagraphContentBuilder addBold(String text) {
        add(new SpanContentText(text, SpanType.BOLD))
    }

    ParagraphContentBuilder addLink(String text, String url) {
        add(new SpanContentLink(text, url, SpanType.LINK))
    }

    ParagraphContent build() {
        new ParagraphContentImpl(buffer)
    }

}
