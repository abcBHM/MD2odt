package cz.zcu.kiv.md2odt.highlight.content

import java.awt.Color


/**
 * Types of code section
 *
 * @version 2017-04-06
 * @author Vít Mazín
 */
enum CodeSectionType {
    NONE("None", Color.BLACK),
    KEYWORD("Keyword", new Color(196, 29, 93)),
    TEXT("Text", Color.BLACK),
    WHITESPACCE("Whitespace", Color.WHITE),
    NAME("Name", new Color(0, 0, 102)),
    LITERAL("Literal", Color.BLACK),
    STRING("String", Color.BLUE),
    NUMBER("Number", Color.CYAN),
    OPERATOR("Operator", Color.RED),
    PUNCTUATION("Punctuation", Color.BLACK),
    COMMENT("Comment", new Color(51, 102, 0)),
    GENERIC("Generic", Color.BLACK)

    private Color color
    private String name

    /**
     * Enum constructor
     *
     * @param name of token
     * @param AWT Color
     */
    CodeSectionType(String name, Color color) {
        this.name = name
        this.color = color
    }

    /**
     * Returns token name
     *
     * @return token name
     */
    String getName() {
        return name
    }

    /**
     * Returns color
     *
     * @return AWT Color
     */
    Color getColor() {
        return color
    }
}