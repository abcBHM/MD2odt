package cz.zcu.kiv.md2odt.highlight.content

/**
 * Enumerator of supported langs
 *
 * @version 2017-04-06
 * @author Josef Baloun
 */
interface CodeSection {

    /** Retrieves a text content of a CodeSection
     *
     * @return text content
     */
    String getText()

    /** Retrieves a type of CodeSection
     *
     * @return CodeSectionType type
     */
    CodeSectionType getType()
}