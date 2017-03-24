package cz.zcu.kiv.md2odt.document

/**
 * Describes content of a paragraph.
 *
 * @version 2017-03-24
 * @author Patrik Harag
 */
interface ParagraphContent extends BlockContent {

    List<SpanContent> getList()

}
