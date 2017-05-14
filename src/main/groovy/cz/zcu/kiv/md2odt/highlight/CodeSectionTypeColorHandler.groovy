package cz.zcu.kiv.md2odt.highlight

import cz.zcu.kiv.md2odt.highlight.content.CodeSectionType

import java.awt.Color

/**
 * Code section color handler
 *
 * @version 2017-04-14
 * @author Vít Mazín
 */
class CodeSectionTypeColorHandler {

    Color backgroud;

    /**
     * Creates a new instance.
     *
     * @param background color of background
     */
    CodeSectionTypeColorHandler(Color background) {
        this.backgroud = background
    }

    /**
     * Returns color for part of code.
     *
     * @param type code section type object
     * @return AWT Color
     */
    Color handle(CodeSectionType type) {
        return type.getColor()
    }
}
