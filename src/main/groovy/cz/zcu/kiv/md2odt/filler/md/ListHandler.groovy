package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ast.ListBlock
import com.vladsch.flexmark.ast.ListBlock as AstList
import com.vladsch.flexmark.ast.Node as AstNode
import cz.zcu.kiv.md2odt.document.Document

/**
 * Handles {@link com.vladsch.flexmark.ast.ListBlock} AST node.
 *
 * @version 2017-03-24
 * @author Patrik Harag
 */
class ListHandler implements AstNodeHandler {

    @Override
    Class<?> getTarget() {
        return AstList
    }

    @Override
    void handle(AstNode node, Document document) {
        assert node instanceof AstList

        def collector = new ListCollector()
        def listContent = collector.processList(node as ListBlock)

        document.addList(listContent)
    }

}
