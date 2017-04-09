package cz.zcu.kiv.md2odt.document.odfdom

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.odftoolkit.odfdom.dom.OdfSchemaDocument
import org.odftoolkit.odfdom.pkg.OdfPackage

/**
 * Created by pepe on 9. 4. 2017.
 */
@RunWith(Parameterized.class)
class OdfdomDocumentGetImagePathTest {
    def url
    def expected

    OdfdomDocumentGetImagePathTest(a, b) {
        url = a
        expected = b
    }

    @Parameterized.Parameters
    static Collection<Object[]> text() {
        [
                ["http://www.xxx.xx/folder/img.png", "www.xxx.xx/folder/img.png"],
                ["random", "random"],
                ["imgěščřžýáíé.png", "img_________.png"],
                ["/x/x", "x/x"],
                ["x/x", "x/x"],
                ["x/x/x/x/x", "x/x/x/x/x"],
                ["/x/", "x"]
        ]*.toArray()
    }

    OdfdomDocument doc
    LastNode last
    OdfSchemaDocument mOdfSchemaDoc

    @Before
    void setUp() throws Exception {
        doc = new OdfdomDocument()
        last = new LastNode(doc)
        mOdfSchemaDoc = (OdfSchemaDocument) doc.odt.getContentDom().getDocument()
    }

    @Test
    void getImagePathTest() throws Exception {
        String im = doc.getImagePath(mOdfSchemaDoc, url)
        String exp = OdfPackage.OdfFile.IMAGE_DIRECTORY.getPath() + "/" + expected
        assert im.equals(exp)
    }
}