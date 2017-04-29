package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ast.Node as AstNode
import com.vladsch.flexmark.ast.FencedCodeBlock as AstFencedCodeBlock
import cz.zcu.kiv.md2odt.document.Document
import groovy.transform.PackageScope
import org.apache.log4j.Logger

/**
 * Handles {@link com.vladsch.flexmark.ast.FencedCodeBlock} AST node.
 *
 * @version 2017-04-04
 * @author Patrik Harag
 */
@PackageScope
class FencedCodeBlockHandler implements AstNodeHandler {

    private static final Logger LOGGER = Logger.getLogger(FencedCodeBlockHandler)

    @Override
    Class<?> getTarget() {
        return AstFencedCodeBlock
    }

    @Override
    void handle(AstNode node, Context context, Document document) {
        assert node instanceof AstFencedCodeBlock

        def segments = (node as AstFencedCodeBlock).segments

        if (segments.size() == 4) {
            def lang = segments[1].toString()
            def code = segments[2].toString().trim()
            document.addCodeBlock(code, lang)

        } else {
            LOGGER.warn("Segments: ${segments.size()}")
        }
    }

}
