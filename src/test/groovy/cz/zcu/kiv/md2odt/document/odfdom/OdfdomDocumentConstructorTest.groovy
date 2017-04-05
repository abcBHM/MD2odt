package cz.zcu.kiv.md2odt.document.odfdom

import org.junit.Before
import org.junit.Test

/**
 * Created by pepe on 5. 4. 2017.
 */
class OdfdomDocumentConstructorTest {
    private static final String TEMPLATE = 'src/test/resources/template.odt'
    private static final String WRONG_TEMPLATE = 'wrongtemplate.odt'

    OdfdomDocument doc

    @Before
    void setUp() throws Exception {
        doc = null
    }

    @Test
    void OdfdomDocumentConstructor() throws Exception {
        doc = new OdfdomDocument()
        assert doc != null
    }

    @Test
    void OdfdomDocumentFileConstructor() throws Exception {
        doc = new OdfdomDocument(new File(TEMPLATE))
        assert doc != null
    }

    @Test
    void OdfdomDocumentStringConstructor() throws Exception {
        doc = new OdfdomDocument(TEMPLATE)
        assert doc != null
    }

    @Test
    void OdfdomDocumentInputStreamConstructor() throws Exception {
        doc = new OdfdomDocument(new FileInputStream(TEMPLATE))
        assert doc != null
    }

    @Test(expected = FileNotFoundException.class)
    void OdfdomDocumentTemplateNotFound1() throws Exception {
        doc = new OdfdomDocument(WRONG_TEMPLATE)
    }

    @Test(expected = FileNotFoundException.class)
    void OdfdomDocumentTemplateNotFound2() throws Exception {
        doc = new OdfdomDocument(new FileInputStream(WRONG_TEMPLATE))
    }
}
