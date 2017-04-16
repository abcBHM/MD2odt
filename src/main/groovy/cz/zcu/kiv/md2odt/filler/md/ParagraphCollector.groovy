package cz.zcu.kiv.md2odt.filler.md

import com.vdurmont.emoji.EmojiManager
import com.vladsch.flexmark.ast.AutoLink as AstAutoLink
import com.vladsch.flexmark.ast.Code as AstCode
import com.vladsch.flexmark.ast.Emphasis as AstEmphasis
import com.vladsch.flexmark.ast.HardLineBreak as AstHardLineBreak
import com.vladsch.flexmark.ast.HtmlEntity as AstHtmlEntity
import com.vladsch.flexmark.ast.HtmlInline as AstHtmlHtmlInline
import com.vladsch.flexmark.ast.HtmlInlineComment as AstHtmlInlineComment
import com.vladsch.flexmark.ast.Image as AstImage
import com.vladsch.flexmark.ast.ImageRef as AstImageRef
import com.vladsch.flexmark.ast.Link as AstLink
import com.vladsch.flexmark.ast.LinkNode as AstLinkNode
import com.vladsch.flexmark.ast.LinkRef as AstLinkRef
import com.vladsch.flexmark.ast.MailLink as AstMailLink
import com.vladsch.flexmark.ast.Node as AstNode
import com.vladsch.flexmark.ast.SoftLineBreak as AstSoftLineBreak
import com.vladsch.flexmark.ast.StrongEmphasis as AstStrongEmphasis
import com.vladsch.flexmark.ast.Text as AstText
import com.vladsch.flexmark.ast.TextBase as AstTextBase
import com.vladsch.flexmark.ext.emoji.Emoji as AstEmoji
import com.vladsch.flexmark.ext.escaped.character.EscapedCharacter as AstEscaped
import com.vladsch.flexmark.ext.gfm.strikethrough.Strikethrough as AstStrike
import com.vladsch.flexmark.ext.gfm.strikethrough.Subscript as AstSub
import com.vladsch.flexmark.superscript.Superscript as AstSup
import cz.zcu.kiv.md2odt.document.ParagraphContent
import cz.zcu.kiv.md2odt.document.ParagraphContentBuilder as Builder
import cz.zcu.kiv.md2odt.document.TextStyle
import org.apache.log4j.Logger
import org.jsoup.Jsoup

/**
 *
 * @version 2017-04-16
 * @author Patrik Harag
 */
class ParagraphCollector {

    private static final Logger LOGGER = Logger.getLogger(ParagraphCollector)

    private final Context context

    ParagraphCollector(Context context) {
        this.context = context
    }

    ParagraphContent processParagraph(AstNode node) {
        processParagraph(node, null)
    }

    ParagraphContent processParagraph(AstNode node, Set<TextStyle> styles) {
        def builder = Builder.builder()

        process(node.children, styles, builder)
        builder.build()
    }

    private void process(Iterable<AstNode> nodes, Set<TextStyle> styles, Builder builder) {
        nodes.each {
            def stylesCopy = styles ? EnumSet.copyOf(styles) : EnumSet.noneOf(TextStyle)
            process(it, stylesCopy, builder)
        }
    }

    private void process(AstNode node, Set<TextStyle> styles, Builder builder) {
        switch (node) {
            // styles
            case AstEmphasis:
                styles.add(TextStyle.ITALIC)
                process(node.children, styles, builder)
                break

            case AstStrongEmphasis:
                styles.add(TextStyle.BOLD)
                process(node.children, styles, builder)
                break

            case AstStrike:
                styles.add(TextStyle.STRIKE)
                process(node.children, styles, builder)
                break

            case AstSub:
                styles.add(TextStyle.SUBSCRIPT)
                process(node.children, styles, builder)
                break

            case AstSup:
                styles.add(TextStyle.SUPERSCRIPT)
                process(node.children, styles, builder)
                break

            case AstCode:  // leaf node!
                styles.add(TextStyle.CODE)
                String code = (node as AstCode).text.toString()
                builder.addText(code, styles)
                break

            // text
            case AstText:
            case AstSoftLineBreak:
            case AstHardLineBreak:
            case AstEscaped:
            case AstHtmlEntity:
            case AstHtmlHtmlInline:
            case AstEmoji:
                builder.addText(flatten(node), styles)
                break

            // images (image nodes extends link node!)
            case AstImage:
            case AstImageRef:
                processImage(node, styles, builder)
                break

            // links
            case AstLinkNode:
                processLink(node as AstLinkNode, styles, builder)
                break

            // ignored
            case AstHtmlInlineComment:
                break

            case AstTextBase:
                process(node.children, styles, builder)
                break

            default:
                LOGGER.warn("Unknown node: " + node.class)
        }
    }

    private void processImage(AstNode node, Set<TextStyle> styles, Builder builder) {
        if (node instanceof AstImage) {
            (node as AstImage).with {
                processImage(*[title, url, text]*.toString(), builder)
            }

        } else if (node instanceof AstImageRef) {
            def imageRef = node as AstImageRef
            def refName = imageRef.reference.toString()
            def ref = context.getReference(refName)

            if (ref) {
                String alt = imageRef.text.toString()
                String url = ref.url.toString()
                String title = ref.title.toString()

                processImage(title, url, alt, builder)
            } else {
                LOGGER.warn("Reference not found: \"$refName\"")
                builder.addText(node.chars.toString(), styles)
            }

        } else {
            assert false
        }
    }

    private void processImage(String text, String url, String alt, Builder builder) {
        def stream = context.getResourceAsStream(url)

        if (stream)
            builder.addImage(text, url, alt, stream)
        else
            builder.addImage(text, url, alt)
    }

    private void processLink(AstLinkNode node, Set<TextStyle> styles, Builder builder) {
        switch (node) {
            case AstLink:
                String url = (node as AstLink).url
                def collector = new ParagraphCollector(context)
                def content = collector.processParagraph(node, styles)

                if (url && content.list)
                    builder.addLink(content, url)
                else if (content.list)
                    builder.addLink(content, content.list*.text.join(''))

                break

            case AstAutoLink:
                String url = (node as AstAutoLink).text.toString()
                def content = Builder.builder().addText(url, styles).build()
                builder.addLink(content, url)
                break

            case AstMailLink:
                String address = (node as AstMailLink).text.toString()
                def content = Builder.builder().addText(address, styles).build()
                builder.addLink(content, 'mailto:' + address)
                break

            case AstLinkRef:
                def linkRef = node as AstLinkRef
                String refName = linkRef.reference.toString()
                String linkText = linkRef.text.toString()
                def ref = context.getReference(refName)

                if (ref) {
                    String text = linkRef.text.blank ? refName : linkText
                    def content = Builder.builder().addText(text, styles).build()
                    builder.addLink(content, ref.url.toString())
                } else {
                    LOGGER.warn("Reference not found: \"$refName\"")
                    builder.addText(linkRef.chars.toString(), styles)
                }
                break

            default:
                LOGGER.warn("Unknown link node: " + node.class)
        }
    }

    static String flatten(AstNode node) {
        switch (node) {
            case AstText:
                return node.chars.toString()
            case AstSoftLineBreak:
                return ' '
            case AstHardLineBreak:
                return '\n'
            case AstEscaped:
                return (node as AstEscaped).text.toString()
            case AstHtmlEntity:
                return Jsoup.parse(node.chars.toString()).text()
            case AstHtmlHtmlInline:
                return node.chars.toString().trim()
            case AstEmoji:
                def alias = (node as AstEmoji).getText().toString()
                def emoji = EmojiManager.getForAlias(alias)
                return (emoji) ? emoji.getUnicode() : node.chars.toString()
        }

        LOGGER.debug("Flattenize: " + node.class)
        node.children.collect { flatten(it) }.join("")
    }

}
