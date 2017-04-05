package cz.zcu.kiv.md2odt.document.odfdom

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

/**
 * Created by pepe on 5. 4. 2017.
 */
@RunWith(Parameterized.class)
class OdfdomDocumentHeadingTest {
    def level
    def text

    OdfdomDocumentHeadingTest(a, b) {
        level = a
        text = b
    }

    @Parameters
    static Collection<Object[]> text() {
        Random rn = new Random()
        int x = rn.nextInt()
        [
                [-1, "under"],
                [0, "&@"],
                [1, "!@#%^&*"],
                [rn.nextInt(11), "ěščřžýáí"],
                [9, "norm"],
                [10, "limit"],
                [11, "over"],
                [x, "random "+x]
        ]*.toArray()
    }

    OdfdomDocument doc
    LastNode last

    @Before
    void setUp() throws Exception {
        doc = new OdfdomDocument()
        last = new LastNode(doc)
    }

    @Test
    void addHeadingTest() throws Exception {
        doc.addHeading(text, level)
        assert last.textContent.equals(text)
        assert last.textOutlineLevel.equals(level.toString())
        assert last.textStyleName.equals(StyleNames.HEADING.getLevel(level))
        assert last.nodeName.equals("text:h")
    }
}
