package cz.zcu.kiv.md2odt.document

/**
 *
 * @version 2017-03-17
 * @author Patrik Harag
 */
interface Document {

    void addParagraph(ParagraphContent content)

    void addCodeBlock(String code)

    void addCodeBlock(String code, String lang)

    void addQuoteBlock(List<ParagraphContent> paragraphs)

}