package cz.zcu.kiv.md2odt

import org.junit.Ignore
import org.junit.Test

import java.nio.file.Files
import java.nio.file.Paths

/**
 *
 * @version 2017-04-01
 * @author Patrik Harag
 */
class MD2odtTest {

    private static final String EXAMPLE = 'src/test/resources/example.md'
    private static final String TEMPLATE = 'src/test/resources/template.odt'

    @Test
    @Ignore
    void test() {
        def md = new FileInputStream(EXAMPLE)
        def out = Files.newOutputStream(Paths.get("result.odt"))

        MD2odt.converter()
                .setInput(md)
                .setOutput(out)

                .enableAutolinks()

                .convert()
    }

    @Test
    @Ignore
    void testWithTemplate() {
        def md = new FileInputStream(EXAMPLE)
        def template = new FileInputStream(TEMPLATE)
        def out = Files.newOutputStream(Paths.get("result.odt"))

        MD2odt.converter()
                .setInput(md)
                .setTemplate(template)
                .setOutput(out)

                .enableAutolinks()

                .convert()
    }

}
