package cz.zcu.kiv.md2odt.document

/**
 *
 * @version 2017-04-13
 * @author Patrik Harag
 */
interface Document {

    void addHeading(String text, int level)

    void addHeading(ParagraphContent content, int level)

    void addParagraph(ParagraphContent content)

    void addList(ListContent content)

    void addTable(TableContent content)

    void addCodeBlock(String code)

    void addCodeBlock(String code, String lang)

    void addQuoteBlock(List<ParagraphContent> paragraphs)

    void addHorizontalRule()

    void save(String documentPath)

    void save(OutputStream outputStream)

    void addTableOfContent()
}