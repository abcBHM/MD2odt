package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ast.AutoLink as AstAutoLink
import com.vladsch.flexmark.ast.Code as AstCode
import com.vladsch.flexmark.ast.Emphasis as AstEmphasis
import com.vladsch.flexmark.ast.HtmlEntity as AstHtmlEntity
import com.vladsch.flexmark.ast.Image as AstImage
import com.vladsch.flexmark.ast.Link as AstLink
import com.vladsch.flexmark.ast.SoftLineBreak as AstSoftLineBreak
import com.vladsch.flexmark.ast.StrongEmphasis as AstStrongEmphasis
import com.vladsch.flexmark.ast.Node as AstNode
import com.vladsch.flexmark.ast.MailLink as AstMailLink
import com.vladsch.flexmark.ast.Paragraph as AstParagraph
import com.vladsch.flexmark.ast.Text as AstText
import cz.zcu.kiv.md2odt.document.ParagraphContent
import cz.zcu.kiv.md2odt.document.ParagraphContentBuilder
import org.apache.log4j.Logger
import org.jsoup.Jsoup

/**
 *
 * @version 2017-04-01
 * @author Patrik Harag
 */
class ParagraphCollector {

    private static final Logger LOGGER = Logger.getLogger(ParagraphCollector)

    ParagraphContent processParagraph(AstParagraph paragraphNode) {
        def builder = ParagraphContentBuilder.builder()

        paragraphNode.children.each { node ->
            switch (node) {
                case AstText:
                case AstSoftLineBreak:
                case AstHtmlEntity:
                    builder.addRegular(flatten(node))
                    break

                case AstEmphasis:
                    builder.addItalic(flatten(node))
                    break

                case AstStrongEmphasis:
                    builder.addBold(flatten(node))
                    break

                case AstCode:
                    String code = (node as AstCode).chars.toString()
                    builder.addCode(code[1..-2])  // without `
                    break

                case AstLink:
                    String url = (node as AstLink).url
                    String text = flatten(node)
                    builder.addLink(text, url)
                    break

                case AstAutoLink:
                    String url = (node as AstAutoLink).text.toString()
                    builder.addLink(url, url)
                    break

                case AstMailLink:
                    String address = (node as AstMailLink).text.toString()
                    builder.addLink(address, 'mailto:' + address)
                    break

                case AstImage:
                    (node as AstImage).with {
                        builder.addImage(*[title, url, text]*.toString())
                    }
                    break

                default:
                    LOGGER.warn("Unknown node: " + node.class)
            }
        }

        builder.build()
    }

    static String flatten(AstNode node) {
        switch (node) {
            case AstText:
                return node.chars.toString()
            case AstSoftLineBreak:
                return '\n'
            case AstHtmlEntity:
                return Jsoup.parse(node.chars.toString()).text()
        }

        LOGGER.debug("Flattenize: " + node.class)
        node.children.collect { flatten(it) }.join("")
    }

}
