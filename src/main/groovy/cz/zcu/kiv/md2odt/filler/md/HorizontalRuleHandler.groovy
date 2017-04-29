package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ast.ThematicBreak as AstThematicBreak
import com.vladsch.flexmark.ast.Node as AstNode
import cz.zcu.kiv.md2odt.document.Document
import groovy.transform.PackageScope

/**
 * Handles {@link com.vladsch.flexmark.ast.ThematicBreak} AST node.
 *
 * @version 2017-03-17
 * @author Patrik Harag
 */
@PackageScope
class HorizontalRuleHandler implements AstNodeHandler {

    @Override
    Class<?> getTarget() {
        return AstThematicBreak
    }

    @Override
    void handle(AstNode node, Context context, Document document) {
        assert node instanceof AstThematicBreak

        document.addHorizontalRule()
    }

}
