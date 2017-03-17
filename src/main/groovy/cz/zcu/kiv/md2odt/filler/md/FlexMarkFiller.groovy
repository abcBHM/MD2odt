package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ast.Document as AstDocument
import com.vladsch.flexmark.ast.Node as AstNode
import com.vladsch.flexmark.parser.Parser
import cz.zcu.kiv.md2odt.document.Document
import cz.zcu.kiv.md2odt.filler.Filler

/**
 *
 * @version 2017-03-17
 * @author Patrik Harag
 */
class FlexMarkFiller implements Filler {

    private static final List<AstNodeHandler> converters = [
            new ParagraphHandler(),
            new FencedCodeBlockHandler(),
            new IntendedCodeBlockHandler(),
            new BlockQuoteHandler(),
            new HorizontalRuleHandler(),
    ]


    private final Parser parser

    FlexMarkFiller() {
        this(Parser.builder().build())
    }

    FlexMarkFiller(Parser parser) {
        this.parser = parser
    }

    @Override
    void fill(String md, Document document) {
        def ast = parser.parse(md)
        walk(ast, document)
    }

    private void walk(AstNode node, Document document) {
        if (node.class == AstDocument) {
            // top level node
            node.children.each { walk(it, document) }

        } else {
            def converter = converters.find { it.target == node.class }

            if (converter)
                converter.handle(node, document)
            else
                throw new UnsupportedOperationException(node.class.toString())
        }
    }

}
