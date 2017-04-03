package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ast.IndentedCodeBlock as AstIndentedCodeBlock
import com.vladsch.flexmark.ast.Node as AstNode
import cz.zcu.kiv.md2odt.document.Document

/**
 * Handles {@link com.vladsch.flexmark.ast.IndentedCodeBlock} AST node.
 *
 * @version 2017-03-17
 * @author Patrik Harag
 */
class IntendedCodeBlockHandler implements AstNodeHandler {

    // TODO: tabs?
    private static final int INDENT = 4
    private static final String INDENT_STR = " " * INDENT

    @Override
    Class<?> getTarget() {
        return AstIndentedCodeBlock
    }

    @Override
    void handle(AstNode node, Context context, Document document) {
        assert node instanceof AstIndentedCodeBlock

        def code = node.chars.toString()
        def lines = code.split("\\n")

        // the first line is trimmed
        def trimmedLines = lines.drop(1).collect {
            if (it.size() > INDENT && it[0..<INDENT] == INDENT_STR)
                // trim indentation
                it[INDENT..-1]
            else
                it
        }

        trimmedLines.add(0, lines[0])

        document.addCodeBlock(trimmedLines.join("\n"))
    }

}
