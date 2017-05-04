package cz.zcu.kiv.md2odt.document

/**
 *
 * @version 2017-03-15
 * @author Patrik Harag
 */
interface SpanContent {

    /**
     * Returns text content of this span.
     *
     * @return text content
     */
    String getText()
    /**
     * Returns type of this span.
     *
     * @return type
     */
    SpanType getType()

}
