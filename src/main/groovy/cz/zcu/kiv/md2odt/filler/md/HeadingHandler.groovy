package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ast.Heading as AstHeading
import com.vladsch.flexmark.ast.Node as AstNode
import cz.zcu.kiv.md2odt.document.Document

/**
 * Handles {@link com.vladsch.flexmark.ast.Heading} AST node.
 *
 * @version 2017-04-21
 * @author Patrik Harag
 */
class HeadingHandler implements AstNodeHandler {

    @Override
    Class<?> getTarget() {
        return AstHeading
    }

    @Override
    void handle(AstNode node, Context context, Document document) {
        assert node instanceof AstHeading

        def heading = node as AstHeading
        int level = heading.level

        def collector = new ParagraphCollector(context)
        def paragraph = collector.processParagraph(node)

        document.addHeading(paragraph, level)
    }

}
