package cz.zcu.kiv.md2odt.document

import org.apache.log4j.Logger

/**
 * Adapter for {@link Document}.
 *
 * @version 2017-04-13
 * @author Patrik Harag
 */
trait DocumentAdapter implements Document {

    private static final Logger LOGGER = Logger.getLogger(DocumentAdapter)

    void addParagraph(ParagraphContent content) {
        LOGGER.error("Unsupported operation: addParagraph")
    }

    void addCodeBlock(String code) {
        addCodeBlock(code, null)
    }

    void addCodeBlock(String code, String lang) {
        LOGGER.error("Unsupported operation: addCodeBlock")
    }

    @Override
    void addQuoteBlock(List<ParagraphContent> paragraphs) {
        LOGGER.error("Unsupported operation: addQuoteBlock")
    }

    @Override
    void addHorizontalRule() {
        LOGGER.error("Unsupported operation: addHorizontalRule")
    }

    @Override
    void addHeading(String text, int level) {
        addHeading(ParagraphContentBuilder.builder().addRegular(text).build(), level)
    }

    @Override
    void addHeading(ParagraphContent content, int level) {
        LOGGER.error("Unsupported operation: addHeading")
    }


    @Override
    void addList(ListContent content) {
        LOGGER.error("Unsupported operation: addList")
    }

    @Override
    void addTable(TableContent content) {
        LOGGER.error("Unsupported operation: addTable")
    }

    @Override
    void save(String documentPath){
        throw new UnsupportedOperationException()
    }

    @Override
    void save(OutputStream outputStream){
        throw new UnsupportedOperationException()
    }

    @Override
    void enableTableOfContent(TableOfContentPosition tableOfContentPosition) {
        LOGGER.error("Unsupported operation: enableTableOfContent")
    }
}
