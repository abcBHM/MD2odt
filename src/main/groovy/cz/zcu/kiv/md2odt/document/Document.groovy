package cz.zcu.kiv.md2odt.document

/**
 *
 * @version 2017-04-20
 * @author Patrik Harag
 */
interface Document {

    /** Appends heading to a document.
     *
     * @param text Text that will be contained in heading.
     * @param level Level of heading.
     * */
    void addHeading(String text, int level)

    /** Appends heading to a document.
     *
     * @param content ParagraphContent that will be contained in heading.
     * @param level Level of heading.
     * */
    void addHeading(ParagraphContent content, int level)

    /** Appends paragraph to a document.
     *
     * @param content ParagraphContent that will be contained in paragraph.
     * */
    void addParagraph(ParagraphContent content)

    /** Appends list to a document.
     *
     * @param content ListContent that will be contained in list.
     * */
    void addList(ListContent content)

    /** Appends table to a document.
     *
     * @param content TableContent that will be contained in table.
     * */
    void addTable(TableContent content)

    /** Appends code block without highlighting to a document.
     *
     * @param code Text that will be contained in code block.
     * */
    void addCodeBlock(String code)

    /** Appends code block with highlighting to a document.
     *
     * @param code Text that will be contained and highlighted in code block.
     * @param lang Programming language used in code for proper syntax highlighting.
     * */
    void addCodeBlock(String code, String lang)

    /** Appends quote block to a document.
     *
     * @param paragraphs Content that will be contained in quote block.
     * */
    void addQuoteBlock(List<ParagraphContent> paragraphs)

    /** Appends horizontal rule to a document.
     * */
    void addHorizontalRule()

    /** Appends table of content to a document.
     * */
    void addTableOfContents()

    /** Saves a document into given path.
     *
     * @param documentPath Path where a document should be saved into.
     * */
    void save(String documentPath)

    /** Saves a document into given stream.
     *
     * @param outputStream Stream where a document should be saved into.
     * */
    void save(OutputStream outputStream)

}