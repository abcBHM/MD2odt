package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ast.Node as AstNode
import cz.zcu.kiv.md2odt.document.Document

/**
 * Interface for handling specific AST node.
 *
 * @version 2017-04-02
 * @author Patrik Harag
 */
interface AstNodeHandler {

    /**
     * Returns class of target AST node.
     *
     * @return class
     */
    Class<?> getTarget()

    /**
     * Handles AST node.
     *
     * @param node AST node
     * @param context context
     * @param document output document
     */
    void handle(AstNode node, Context context, Document document)

}