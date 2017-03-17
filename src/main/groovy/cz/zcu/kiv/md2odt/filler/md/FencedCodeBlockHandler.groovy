package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ast.Node as AstNode
import com.vladsch.flexmark.ast.FencedCodeBlock as AstFencedCodeBlock
import cz.zcu.kiv.md2odt.document.Document

/**
 * Handles {@link com.vladsch.flexmark.ast.FencedCodeBlock} AST node.
 *
 * @version 2017-03-17
 * @author Patrik Harag
 */
class FencedCodeBlockHandler implements AstNodeHandler {

    @Override
    Class<?> getTarget() {
        return AstFencedCodeBlock
    }

    @Override
    void handle(AstNode node, Document document) {
        assert node instanceof AstFencedCodeBlock

        def segments = (node as AstFencedCodeBlock).segments

        if (segments.size() == 3) {
            def code = segments[1].toString().trim()
            document.addCodeBlock(code)

        } else if (segments.size() == 4) {
            def lang = segments[1].toString()
            def code = segments[2].toString().trim()
            document.addCodeBlock(code, lang)

        } else {
            throw new UnsupportedOperationException("segments: ${segments.size()}")
        }
    }

}
