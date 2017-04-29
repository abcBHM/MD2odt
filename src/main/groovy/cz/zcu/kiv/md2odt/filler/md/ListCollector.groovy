package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ast.ListBlock as AstList
import com.vladsch.flexmark.ast.ListItem as AstListItem
import com.vladsch.flexmark.ast.OrderedList as AstOrderedList
import com.vladsch.flexmark.ast.Paragraph as AstParagraph
import com.vladsch.flexmark.ast.Node as AstNode
import cz.zcu.kiv.md2odt.document.BlockContent
import cz.zcu.kiv.md2odt.document.ListContent
import cz.zcu.kiv.md2odt.document.ListContentBuilder
import cz.zcu.kiv.md2odt.document.ListType
import cz.zcu.kiv.md2odt.document.ParagraphContentBuilder
import groovy.transform.PackageScope
import org.apache.log4j.Logger

/**
 * Converts AST node into {@link ListContent}.
 *
 * @version 2017-04-29
 * @author Patrik Harag
 */
@PackageScope
class ListCollector {

    private static final Logger LOGGER = Logger.getLogger(ListCollector)

    private final Context context

    ListCollector(Context context) {
        this.context = context
    }

    ListContent processList(AstList list) {
        def builder = ListContentBuilder.builder(type(list))

        list.children.each {
            builder.addListItem(buildListItem(it))
        }

        builder.build()
    }

    private List<BlockContent> buildListItem(AstNode node) {
        assert node instanceof AstListItem

        node.children.collect { build(it) }.toList()
    }

    private BlockContent build(AstNode node) {
        switch (node) {
            case AstList:
                // nested list
                return processList(node as AstList)

            case AstParagraph:
                // paragraph
                def collector = new ParagraphCollector(context)
                return collector.processParagraph(node as AstParagraph)

            default:
                // TODO: headers? code blocks? quote blocks?

                LOGGER.warn("Unknown node: " + node.class)
                return ParagraphContentBuilder.builder()
                        .addRegular(node.chars.toString().trim())
                        .build()
        }
    }

    private ListType type(AstList list) {
        (list instanceof AstOrderedList) ? ListType.ORDERED : ListType.BULLET
    }

}
