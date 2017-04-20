package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ast.Node as AstNode
import com.vladsch.flexmark.ext.toc.TocBlockBase as AstToc
import cz.zcu.kiv.md2odt.document.Document

/**
 * Handles {@link com.vladsch.flexmark.ext.toc.TocBlockBase} AST node.
 *
 * @version 2017-04-20
 * @author Patrik Harag
 */
class TableOfContentsHandler implements AstNodeHandler {

    @Override
    Class<?> getTarget() {
        return AstToc
    }

    @Override
    void handle(AstNode node, Context context, Document document) {
        assert node instanceof AstToc

        document.addTableOfContents()
    }

}
