package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ast.Paragraph as AstParagraph
import com.vladsch.flexmark.ast.Node as AstNode
import cz.zcu.kiv.md2odt.document.Document

/**
 * Handles {@link com.vladsch.flexmark.ast.Paragraph} AST node.
 *
 * @version 2017-03-17
 * @author Patrik Harag
 */
class ParagraphHandler implements AstNodeHandler {

    @Override
    Class<?> getTarget() {
        return AstParagraph
    }

    @Override
    void handle(AstNode node, Context context, Document document) {
        assert node instanceof AstParagraph

        def collector = new ParagraphCollector()
        def paragraph = collector.processParagraph(node as AstParagraph, context)
        document.addParagraph(paragraph)
    }

}
