package cz.zcu.kiv.md2odt

import com.vladsch.flexmark.ast.Node as ASTNode
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import org.junit.Test

/**
 *
 * @version 2017-03-05
 * @author Patrik Harag
 */
class FlexmarkTest {

    @Test
    void commonMark() {
        def parser = Parser.builder().build()
        def document = parser.parse("This is *Sparta*!")
        def renderer = HtmlRenderer.builder().build()

        assert renderer.render(document).trim() == "<p>This is <em>Sparta</em>!</p>"

        // printAST(document, "")
    }

    void printAST(ASTNode node, String indent) {
        println "${indent} ${node.class} –- \"${node.chars}\""

        node.children.each {
            printAST(it, indent + " ")
        }
    }

}
