package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ast.Document as AstDocument
import com.vladsch.flexmark.ast.HtmlCommentBlock as AstHtmlCommentBlock
import com.vladsch.flexmark.ast.Node as AstNode
import com.vladsch.flexmark.ast.Reference as AstReference
import com.vladsch.flexmark.parser.Parser
import cz.zcu.kiv.md2odt.document.Document
import cz.zcu.kiv.md2odt.filler.Filler
import cz.zcu.kiv.md2odt.filler.LocalResources
import cz.zcu.kiv.md2odt.filler.LocalResourcesImpl
import org.apache.log4j.Logger

/**
 *
 * @version 2017-04-08
 * @author Patrik Harag
 */
class FlexMarkFiller implements Filler {

    private static final Logger LOGGER = Logger.getLogger(FlexMarkFiller)

    private static final List<AstNodeHandler> handlers = [
            new ParagraphHandler(),
            new FencedCodeBlockHandler(),
            new IntendedCodeBlockHandler(),
            new BlockQuoteHandler(),
            new HorizontalRuleHandler(),
            new HeadingHandler(),
            new ListHandler(),
            BasicHandlers.ignore(AstReference),
            BasicHandlers.ignore(AstHtmlCommentBlock)
    ]


    private final Parser parser

    FlexMarkFiller() {
        this(Parser.builder().build())
    }

    FlexMarkFiller(Parser parser) {
        this.parser = parser
    }

    @Override
    void fill(String md, LocalResources resources, Document document) {
        def ast = parser.parse(md) as AstDocument
        def context = Context.of(ast, resources)

        convert(ast, context, document)
    }

    void fill(String md, Document document) {
        fill(md, LocalResourcesImpl.EMPTY, document)
    }

    private void convert(AstNode node, Context context, Document document) {
        if (node.class == AstDocument) {
            // top level node
            node.children.each { convert(it, context, document) }

        } else {
            def handler = handlers.find { it.target.isInstance(node) }

            if (handler)
                handler.handle(node, context, document)
            else
                LOGGER.warn("Unknown node: " + node.class)
        }
    }

}
