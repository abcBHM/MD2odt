package cz.zcu.kiv.md2odt.filler

import org.junit.Test

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern

/**
 *
 * @author Patrik Harag
 * @version 2017-04-29
 */
class SourceZipTest {

    static final String EXAMPLE_ZIP = '/example.zip'

    static final Charset CHARSET = StandardCharsets.UTF_8
    static final Pattern SOURCE_FILE_PATTERN = Pattern.compile(".*\\.md");

    @Test
    void testLoadResources() {
        def stream = System.class.getResourceAsStream(EXAMPLE_ZIP)
        def sourceZip = new SourceZip(stream, CHARSET, SOURCE_FILE_PATTERN)

        assert !sourceZip.source.isEmpty()

        def resources = [
                "folder/img.png",
                "img.bmp",
                "img.gif",
                "img.jpg",
                "img.png",
                "img1.svg",
                "img2.svg",
        ]

        resources.each {
            assert sourceZip.resources.get(it)
        }
    }

    @Test(expected = Exception)
    void testNoSource() {
        def stream = System.class.getResourceAsStream(EXAMPLE_ZIP)
        def sourceZip = new SourceZip(stream, CHARSET, Pattern.compile("______"))

        sourceZip.resources.get("img.png")
    }

}