package cz.zcu.kiv.md2odt.document

/**
 *
 * @version 2017-03-24
 * @author Patrik Harag
 */
interface Document {

    void addHeading(String text, int level)

    void addParagraph(ParagraphContent content)

    void addList(ListContent content)

    void addCodeBlock(String code)

    void addCodeBlock(String code, String lang)

    void addQuoteBlock(List<ParagraphContent> paragraphs)

    void addHorizontalRule()

    void save(String documentPath)

    void save(OutputStream outputStream)
}