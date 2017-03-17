package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ast.Node as AstNode
import cz.zcu.kiv.md2odt.document.Document

/**
 * Interface for handling specific AST node.
 *
 * @version 2017-03-17
 * @author Patrik Harag
 */
interface AstNodeHandler<T extends AstNode> {

    Class<T> getTarget()

    void handle(AstNode node, Document document)

}