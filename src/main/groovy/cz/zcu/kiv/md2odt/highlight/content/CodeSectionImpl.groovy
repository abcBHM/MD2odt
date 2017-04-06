package cz.zcu.kiv.md2odt.highlight.content


/**
 * Created by vita on 06.04.2017.
 */
class CodeSectionImpl implements CodeSection {

    String text
    CodeSectionType type

    CodeSectionImpl(String text, CodeSectionType type) {
        this.text = text
        this.type = type
    }

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
