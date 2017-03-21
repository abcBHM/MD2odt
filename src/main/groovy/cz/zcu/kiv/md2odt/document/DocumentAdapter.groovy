package cz.zcu.kiv.md2odt.document

/**
 * Adapter for {@link Document}.
 *
 * @version 2017-03-17
 * @author Patrik Harag
 */
trait DocumentAdapter implements Document {

    void addParagraph(ParagraphContent content) {
        throw new UnsupportedOperationException()
    }

    void addCodeBlock(String code) {
        addCodeBlock(code, null)
    }

    void addCodeBlock(String code, String lang) {
        throw new UnsupportedOperationException()
    }

    @Override
    void addQuoteBlock(List<ParagraphContent> paragraphs) {
        throw new UnsupportedOperationException()
    }

    @Override
    void addHorizontalRule() {
        throw new UnsupportedOperationException()
    }

    @Override
    void addHeading(String text, int level) {
        throw new UnsupportedOperationException()
    }
}
