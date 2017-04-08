package cz.zcu.kiv.md2odt

import org.junit.Ignore
import org.junit.Test

import java.nio.file.Files
import java.nio.file.Paths

/**
 *
 * @version 2017-04-08
 * @author Patrik Harag
 */
class MD2odtTest {

    private static final String EXAMPLE_TXT = '/example.md'
    private static final String EXAMPLE_ZIP = '/example.zip'
    private static final String TEMPLATE = '/template.odt'

    private static final String OUTPUT = "result.odt"

    @Test
    @Ignore
    void test() {
        def md = System.class.getResourceAsStream(EXAMPLE_TXT)
        def out = Files.newOutputStream(Paths.get(OUTPUT))

        convert(md, null, out)
    }

    @Test
    @Ignore
    void testWithTemplate() {
        def md = System.class.getResourceAsStream(EXAMPLE_TXT)
        def template = System.class.getResourceAsStream(TEMPLATE)
        def out = Files.newOutputStream(Paths.get(OUTPUT))

        convert(md, template, out)
    }

    private void convert(input, template, out) {
        MD2odt.converter()
                .setInputStream(input)
                .setTemplate(template)
                .setOutput(out)

                .enableAutolinks()
                .enableEmoji()

                .convert()
    }

    @Test
    @Ignore
    void testFromZip() {
        def zip = System.class.getResourceAsStream(EXAMPLE_ZIP)
        def out = Files.newOutputStream(Paths.get(OUTPUT))

        MD2odt.converter()
                .setInputZip(zip)
                .setOutput(out)

                .enableAutolinks()
                .enableEmoji()

                .convert()
    }

}
