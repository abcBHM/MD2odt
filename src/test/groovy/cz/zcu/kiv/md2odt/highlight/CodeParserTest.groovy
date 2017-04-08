package cz.zcu.kiv.md2odt.highlight

import org.junit.Ignore
import org.junit.Test
import org.python.core.PyList
import org.python.core.PyTuple
import org.python.core.PyTupleDerived
import org.python.util.PythonInterpreter

/**
 * Created by n5ver on 06.04.2017.
 */
class CodeParserTest {

    @Ignore
    @Test
    void trySmth() throws Exception {
        String code = "package cz.zcu.kiv.md2odt.document.odt\n" +
                "\n" +
                "import org.apache.xerces.dom.TextImpl\n" +
                "\n" +
                "/**\n" +
                " * Created by pepe on 13. 3. 2017.\n" +
                " * cookbook:\n" +
                " * http://incubator.apache.org/odftoolkit/simple/document/cookbook/index.html\n" +
                " */\n" +
                "@Deprecated\n" +
                "class OdfSimpleWrapper {\n" +
                "    private TextDocument odt\n" +
                "\n" +
                "    OdfSimpleWrapper() {\n" +
                "        odt = TextDocument.newTextDocument()\n" +
                "        Node n = odt.getContentDom().getElementsByTagName(\"office:text\").item(0)\n" +
                "        n.removeChild(n.lastChild)      //delete an empty paragraph\n" +
                "    }\n" +
                "\n" +
                "}"
        PythonInterpreter interpreter = new PythonInterpreter()

        // Set a variable with the content you want to work with
        interpreter.set("code", code)

        // Simple use Pygments as in Python
        interpreter.exec("""
        from pygments import highlight
        from pygments.lexers import GroovyLexer
        from pygments.formatter import Formatter
        ret = None
        class OwnFormatter(Formatter):
                
            def format(self, tokensource, outfile):
                enc = self.encoding
                global ret
                ret = list(tokensource)
        
        highlight(code, GroovyLexer(), OwnFormatter())""".stripIndent())

        // Get the result that has been set in a variable
        PyList result = interpreter.get("ret", PyList.class)
        for(int i = 0; i < result.size(); i++) {
            PyTuple tuple = result.get(i)
            PyTupleDerived tuple2 = tuple.get(0)
            String value = tuple.get(1)
            print tuple2.get(0) + " : "
            print value
            println ""
        }
        //System.out.println(result)
    }
}
