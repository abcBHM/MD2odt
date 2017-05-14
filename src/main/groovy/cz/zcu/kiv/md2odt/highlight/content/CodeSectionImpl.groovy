package cz.zcu.kiv.md2odt.highlight.content


/**
 * Code section implementation
 *
 * @version 2017-04-06
 * @author Vít Mazín
 */
class CodeSectionImpl implements CodeSection {

    String text
    CodeSectionType type

    /**
     * Class constructor
     *
     * @param text of part of code snippet
     * @param CodeSectionType object
     */
    CodeSectionImpl(String text, CodeSectionType type) {
        this.text = text
        this.type = type
    }

    /**
     * Class constructor
     *
     * @param text of part of code snippet
     */
    CodeSectionImpl(String text) {
        this(text, CodeSectionType.TEXT)
    }

    @Override
    /** Retrieves a text content of a CodeSection
     *
     * @return text content
     */
    String getText() {
        return text
    }

    @Override
    /** Retrieves a type of CodeSection
     *
     * @return CodeSectionType type
     */
    CodeSectionType getType() {
        return type
    }
}
