package cz.zcu.kiv.md2odt.highlight.content

import java.awt.Color


/**
 * Created by vita on 06.04.2017.
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

    CodeSectionType(String name, Color color) {
        this.name = name
        this.color = color
    }

    String getName() {
        return name
    }

    Color getColor() {
        return color
    }
}