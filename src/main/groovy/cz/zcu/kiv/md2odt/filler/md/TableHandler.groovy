package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ast.Node as AstNode
import com.vladsch.flexmark.ext.tables.TableBlock as AstTable
import cz.zcu.kiv.md2odt.document.Document

/**
 * Handles {@link com.vladsch.flexmark.ext.tables.TableBlock} AST node.
 *
 * @version 2017-04-13
 * @author Patrik Harag
 */
class TableHandler implements AstNodeHandler {

    @Override
    Class<?> getTarget() {
        return AstTable
    }

    @Override
    void handle(AstNode node, Context context, Document document) {
        assert node instanceof AstTable

        def collector = new TableCollector(context)
        def table = collector.processTable(node as AstTable)

        document.addTable(table)
    }

}
