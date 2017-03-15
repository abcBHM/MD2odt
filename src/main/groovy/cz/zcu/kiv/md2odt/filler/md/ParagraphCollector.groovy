package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ast.Emphasis as AstEmphasis
import com.vladsch.flexmark.ast.Link as AstLink
import com.vladsch.flexmark.ast.StrongEmphasis as AstStrongEmphasis
import com.vladsch.flexmark.ast.Node as AstNode
import com.vladsch.flexmark.ast.Paragraph as AstParagraph
import com.vladsch.flexmark.ast.Text as AstText
import cz.zcu.kiv.md2odt.document.ParagraphContent
import cz.zcu.kiv.md2odt.document.ParagraphContentBuilder

/**
 *
 * @version 2017-03-15
 * @author Patrik Harag
 */
class ParagraphCollector {

    ParagraphContent processParagraph(AstParagraph paragraphNode) {
        def builder = ParagraphContentBuilder.builder()

        paragraphNode.children.each { node ->
            switch (node) {
                case AstText:
                    builder.addRegular(node.chars.toString())
                    break

                case AstEmphasis:
                    builder.addItalic(flatten(node))
                    break

                case AstStrongEmphasis:
                    builder.addBold(flatten(node))
                    break

                case AstLink:
                    String url = (node as AstLink).url
                    String text = flatten(node)
                    builder.addLink(text, url)
                    break

                default:
                    throw new UnsupportedOperationException('Not implemented yet')
            }
        }

        builder.build()
    }

    private String flatten(AstNode node) {
        if (node instanceof AstText)
            return node.chars.toString()

        node.children.collect { flatten(it) }.join("")
    }

}
