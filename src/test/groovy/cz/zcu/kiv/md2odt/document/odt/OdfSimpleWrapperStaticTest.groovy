package cz.zcu.kiv.md2odt.document.odt

import org.junit.Test

/**
 * Created by pepe on 14. 3. 2017.
 */
class OdfSimpleWrapperStaticTest {
    @Test
    void escapeTest() throws Exception {
        assert OdfSimpleWrapper.escape("bla &italic; df&s").equals("bla &amp;italic; df&amp;s")
    }
}
