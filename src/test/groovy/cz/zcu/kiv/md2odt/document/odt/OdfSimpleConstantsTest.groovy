package cz.zcu.kiv.md2odt.document.odt

import org.junit.Test

/**
 * Created by pepe on 16. 3. 2017.
 */
class OdfSimpleConstantsTest {
    @Test
    void escapeTest() throws Exception {
        assert OdfSimpleConstants.escape("bla &italic; df&s").equals("bla &amp;italic; df&amp;s")
    }
    @Test
    void reEscapeTest() throws Exception {
        assert OdfSimpleConstants.reEscape("bla &amp;italic; df&amp;s").equals("bla &italic; df&s")
    }
}
