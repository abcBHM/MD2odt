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
class ResourceManagerImplTest {

    static final String EXAMPLE_ZIP = '/example.zip'

    static final Charset CHARSET = StandardCharsets.UTF_8
    static final Pattern SOURCE_FILE_PATTERN = Pattern.compile(".*\\.md");

    @Test
    void testLimitBig() {
        tryLimit(999_999)
    }

    @Test(expected = Exception)
    void testLimitToLow() {
        tryLimit(1)
    }

    private void tryLimit(int limit) {
        def stream = System.class.getResourceAsStream(EXAMPLE_ZIP)
        def sourceZip = new SourceZip(stream, CHARSET, SOURCE_FILE_PATTERN)

        def manager = new ResourceManagerImpl(sourceZip.resources, { url -> true }, limit)

        manager.getResourceAsStream("img.png")
    }

}