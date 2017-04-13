package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ast.Node as AstNode
import com.vladsch.flexmark.ext.tables.TableBlock as AstTable
import com.vladsch.flexmark.ext.tables.TableHead as AstTableHead
import com.vladsch.flexmark.ext.tables.TableSeparator as AstTableSeparator
import com.vladsch.flexmark.ext.tables.TableBody as AstTableBody
import com.vladsch.flexmark.ext.tables.TableRow as AstTableRow
import com.vladsch.flexmark.ext.tables.TableCell as AstTableCell
import cz.zcu.kiv.md2odt.document.TableCellContent
import cz.zcu.kiv.md2odt.document.TableContent
import cz.zcu.kiv.md2odt.document.TableContentBuilder as Builder
import org.apache.log4j.Logger

/**
 *
 * @version 2017-04-13
 * @author Patrik Harag
 */
class TableCollector {

    private static final Logger LOGGER = Logger.getLogger(TableCollector)

    private final Context context

    TableCollector(Context context) {
        this.context = context
    }

    TableContent processTable(AstTable node) {
        def builder = Builder.builder()
        node.children.each {
            switch (it) {
                case AstTableHead:
                    processTableHead(it, builder)
                    break

                case AstTableSeparator:
                    processTableSeparator(it, builder)
                    break

                case AstTableBody:
                    processTableBody(it, builder)
                    break

                default:
                    LOGGER.warn('Unknown node inside table: ' + it.class)
            }
        }

        return builder.build()
    }

    private void processTableHead(AstNode head, Builder builder) {
        head.children.each {
            if (it instanceof AstTableRow) {
                builder.addRow(collectRow(it))

            } else {
                LOGGER.warn('Unknown node inside table head: ' + it.class)
            }
        }
    }

    private void processTableSeparator(AstNode separator, Builder builder) {
        // nothing
    }

    private void processTableBody(AstNode body, Builder builder) {
        body.children.each {
            if (it instanceof AstTableRow) {
                builder.addRow(collectRow(it))

            } else {
                LOGGER.warn('Unknown node inside table body: ' + it.class)
            }
        }
    }

    private List<TableCellContent> collectRow(AstTableRow row) {
        def list = []
        row.children.each {
            if (it instanceof AstTableCell) {
                def collector = new ParagraphCollector(context)
                def paragraph = collector.processParagraph(it)

                def heading = it.header
                def align = align(it.alignment, heading)

                list.add(new TableCellContent(paragraph, align, heading))

            } else {
                LOGGER.warn('Unknown node inside table row: ' + it.class)
            }
        }
        return list
    }

    private static TableCellContent.Align align(AstTableCell.Alignment a, boolean heading) {
        if (a == null)
            return (heading) ? TableCellContent.Align.CENTER : TableCellContent.Align.LEFT

        if (a == AstTableCell.Alignment.LEFT)
            return TableCellContent.Align.LEFT
        else if (a == AstTableCell.Alignment.RIGHT)
            return TableCellContent.Align.RIGHT
        else if (a == AstTableCell.Alignment.CENTER)
            return TableCellContent.Align.CENTER
        else
            throw new IllegalArgumentException()
    }

}
