package cz.zcu.kiv.md2odt

import org.junit.Ignore
import org.junit.Test

import java.nio.file.Files
import java.nio.file.Paths

/**
 *
 * @version 2017-04-12
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
                .enableAllExtensions()
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
                .enableAllExtensions()
                .convert()
    }

    @Test
    @Ignore
    void testFromString() {
        def s = "# MD2odt\n" +
                "\n" +
                "[![Build Status](https://travis-ci.org/abcBHM/MD2odt.svg?branch=master)](https://travis-ci.org/abcBHM/MD2odt)\n" +
                "[![codecov](https://codecov.io/gh/abcBHM/MD2odt/branch/master/graph/badge.svg)](https://codecov.io/gh/abcBHM/MD2odt)\n" +
                "[![](https://jitpack.io/v/abcBHM/MD2odt.svg)](https://jitpack.io/#abcBHM/MD2odt)"

        def out = Files.newOutputStream(Paths.get(OUTPUT))

        MD2odt.converter()
                .setInputString(s)
                .setOutput(out)
                .enableAllExtensions()
                .convert()
    }
}
