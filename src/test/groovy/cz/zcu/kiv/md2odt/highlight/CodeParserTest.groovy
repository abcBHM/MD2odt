package cz.zcu.kiv.md2odt.highlight

import org.junit.Ignore
import org.junit.Test
import org.python.util.PythonInterpreter

/**
 * Created by n5ver on 06.04.2017.
 */
class CodeParserTest {

    @Ignore
    @Test
    void trySmth() throws Exception {
        String code = "private static final Logger LOGGER = Logger.getLogger(CodeParser)"
        PythonInterpreter interpreter = new PythonInterpreter()

        // Set a variable with the content you want to work with
        interpreter.set("code", code)

        // Simple use Pygments as in Python
        interpreter.exec("from pygments import highlight\n"
                + "from pygments.lexers import JavaLexer\n"
                + "from pygments.formatters import RawTokenFormatter\n"
                + "\nresult = highlight(code, JavaLexer(), RawTokenFormatter())")

        // Get the result that has been set in a variable
        System.out.println(interpreter.get("result", String.class))
    }
}
