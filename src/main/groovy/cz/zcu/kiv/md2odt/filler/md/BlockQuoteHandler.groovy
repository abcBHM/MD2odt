package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ast.BlockQuote as AstBlockQuote
import com.vladsch.flexmark.ast.Paragraph as AstParagraph
import com.vladsch.flexmark.ast.Node as AstNode
import cz.zcu.kiv.md2odt.document.Document
import groovy.transform.PackageScope

/**
 * Handles {@link com.vladsch.flexmark.ast.BlockQuote} AST node.
 *
 * @version 2017-03-17
 * @author Patrik Harag
 */
@PackageScope
class BlockQuoteHandler implements AstNodeHandler {

    @Override
    Class<?> getTarget() {
        return AstBlockQuote
    }

    @Override
    void handle(AstNode node, Context context, Document document) {
        assert node instanceof AstBlockQuote

        // TODO: add support for other nested blocks

        def collector = new ParagraphCollector(context)
        def paragraphs = node.children
                .findAll { it instanceof AstParagraph }
                .collect { collector.processParagraph(it as AstParagraph) }

        document.addQuoteBlock(paragraphs)
    }

}
