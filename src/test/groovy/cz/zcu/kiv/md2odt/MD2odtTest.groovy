package cz.zcu.kiv.md2odt

import org.junit.Ignore
import org.junit.Test

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Paths

/**
 *
 * @version 2017-04-29
 * @author Patrik Harag
 */
class MD2odtTest {

    private static final Charset CHARSET = StandardCharsets.UTF_8

    private static final String EXAMPLE_TXT = '/example.md'
    private static final String EXAMPLE_ZIP = '/example.zip'
    private static final String TEMPLATE = '/template.odt'

    private static final String OUTPUT = "result.odt"

    @Test
    @Ignore
    void test() {
        def md = System.class.getResourceAsStream(EXAMPLE_TXT)
        def out = Paths.get(OUTPUT)

        MD2odt.converter()
                .setInput(md, CHARSET)
                .setOutput(out)
                .enableAllExtensions()
                .convert()
    }

    @Test
    @Ignore
    void testWithTemplate() {
        def md = System.class.getResourceAsStream(EXAMPLE_TXT)
        def template = System.class.getResourceAsStream(TEMPLATE)
        def out = Paths.get(OUTPUT)

        MD2odt.converter()
                .setInput(md, CHARSET)
                .setTemplate(template)
                .setOutput(out)
                .enableAllExtensions()
                .convert()
    }

    @Test
    @Ignore
    void testFromZip() {
        def zip = System.class.getResourceAsStream(EXAMPLE_ZIP)
        def out = Paths.get(OUTPUT)

        MD2odt.converter()
                .setInputZip(zip, CHARSET)
                .setOutput(out)
                .enableAllExtensions()
                .convert()
    }

}
