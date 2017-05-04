package cz.zcu.kiv.md2odt.document

/**
 * Describes content of a paragraph.
 *
 * @version 2017-03-24
 * @author Patrik Harag
 */
interface ParagraphContent extends BlockContent {

    /**
     * Returns content of a paragraph created as SpanContents.
     *
     * @return content of a paragraph as a list of SpanContents
     */
    List<SpanContent> getList()

}
