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

    /**
     * Creates an instance of ParagraphContentBuilder to build a ParagraphContent.
     *
     * @return instance of ParagraphContentBuilder to build a ParagraphContent
     */
    static ParagraphContentBuilder builder() {
        new ParagraphContentBuilder()
    }


    private final List<SpanContent> buffer = []

    private ParagraphContentBuilder() { }

    /**
     * Adds SpanContent to builder.
     *
     * @param span Span content
     * @return builder
     */
    ParagraphContentBuilder add(SpanContent span) {
        buffer.add(span)
        return this
    }

    /**
     * Adds styled text to builder.
     *
     * @param text text content
     * @param styles styles to set to a text
     * @return builder
     */
    ParagraphContentBuilder addText(String text, Set<TextStyle> styles) {
        // try append text after the last span
        def last = buffer ? buffer[-1] : null
        if (last && last.type == SpanType.TEXT && last.styles == styles)
            buffer[-1] = new SpanContentText(last.text + text, styles)
        else
            add(new SpanContentText(text, styles))

        return this
    }

    /**
     * Adds regular text to builder.
     *
     * @param text text content
     * @return builder
     */
    ParagraphContentBuilder addRegular(String text) {
        addText(text, [] as Set)
    }

    /**
     * Adds italic text to builder.
     *
     * @param text text content
     * @return builder
     */
    ParagraphContentBuilder addItalic(String text) {
        addText(text, [TextStyle.ITALIC] as Set)
    }

    /**
     * Adds bold text to builder.
     *
     * @param text text content
     * @return builder
     */
    ParagraphContentBuilder addBold(String text) {
        addText(text, [TextStyle.BOLD] as Set)
    }

    /**
     * Adds code text to builder.
     *
     * @param text text content
     * @return builder
     */
    ParagraphContentBuilder addCode(String text) {
        addText(text, [TextStyle.CODE] as Set)
    }

    /**
     * Adds link to builder.
     *
     * @param text text to show
     * @param url url of a link
     * @return builder
     */
    ParagraphContentBuilder addLink(String text, String url) {
        add(new SpanContentLink(builder().addRegular(text).build(), url))
    }

    /**
     * Adds link to builder.
     *
     * @param content content to show
     * @param url url of a link
     * @return builder
     */
    ParagraphContentBuilder addLink(ParagraphContent content, String url) {
        add(new SpanContentLink(content, url))
    }

    /**
     * Adds image to builder.
     *
     * @param text image text
     * @param url image path
     * @param alt alternative text
     * @return builder
     */
    ParagraphContentBuilder addImage(String text, String url, String alt) {
        add(new SpanContentImage(text, url, alt))
    }

    /**
     * Adds image to builder.
     *
     * @param text image text
     * @param url image path
     * @param alt alternative text
     * @param is Stream of an image
     * @return builder
     */
    ParagraphContentBuilder addImage(String text, String url, String alt, InputStream is) {
        add(new SpanContentImageStream(text, url, alt, is))
    }

    /**
     * Creates ParagraphContent from builder.
     *
     * @return ParagraphContent of a builded paragraph
     */
    ParagraphContent build() {
        new ParagraphContentImpl(buffer)
    }

}
