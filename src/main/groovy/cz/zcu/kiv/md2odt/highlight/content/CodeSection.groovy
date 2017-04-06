package cz.zcu.kiv.md2odt.highlight.content

/**
 * Created by pepe on 6. 4. 2017.
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