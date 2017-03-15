package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ast.Document as AstDocument
import com.vladsch.flexmark.ast.Node as AstNode
import com.vladsch.flexmark.ast.Paragraph as AstParagraph
import com.vladsch.flexmark.parser.Parser
import cz.zcu.kiv.md2odt.document.Document
import cz.zcu.kiv.md2odt.filler.Filler

/**
 *
 * @version 2017-03-15
 * @author Patrik Harag
 */
class FlexMarkFiller implements Filler {

    private Document document

    @Override
    void fill(String md, Document document) {
        this.document = document

        def ast = getParser().parse(md)
        walk(ast)
    }

    private void walk(AstNode node) {
        switch (node) {
            case AstDocument:
                // top level node
                node.children.each { walk(it) }
                break

            case AstParagraph:
                processParagraph(node as AstParagraph)
                break

            default:
                throw new UnsupportedOperationException('Not implemented yet')
        }
    }

    private void processParagraph(AstParagraph node) {
        def collector = new ParagraphCollector()
        def paragraph = collector.processParagraph(node)
        document.addParagraph(paragraph)
    }

    private static Parser getParser() {
        Parser.builder().build()
    }

}
