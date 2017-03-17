package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ast.ThematicBreak as AstThematicBreak
import com.vladsch.flexmark.ast.Node as AstNode
import cz.zcu.kiv.md2odt.document.Document

/**
 * Handles {@link com.vladsch.flexmark.ast.ThematicBreak} AST node.
 *
 * @version 2017-03-17
 * @author Patrik Harag
 */
class HorizontalRuleHandler implements AstNodeHandler {

    @Override
    Class<?> getTarget() {
        return AstThematicBreak
    }

    @Override
    void handle(AstNode node, Document document) {
        assert node instanceof AstThematicBreak

        document.addHorizontalRule()
    }

}
