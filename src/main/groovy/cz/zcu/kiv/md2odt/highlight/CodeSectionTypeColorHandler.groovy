package cz.zcu.kiv.md2odt.highlight

import cz.zcu.kiv.md2odt.highlight.content.CodeSectionType

import java.awt.Color

/**
 * Created by n5ver on 14.04.2017.
 */
class CodeSectionTypeColorHandler {

    Color backgroud;

    CodeSectionTypeColorHandler(Color background) {
        this.backgroud = background
    }

    Color handle(CodeSectionType type) {
        return type.getColor()
    }
}
