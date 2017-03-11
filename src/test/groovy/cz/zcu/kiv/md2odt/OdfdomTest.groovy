package cz.zcu.kiv.md2odt

import org.junit.Before
import org.junit.Test
import org.odftoolkit.odfdom.type.Color
import org.odftoolkit.simple.TextDocument
import org.odftoolkit.simple.common.navigation.TextNavigation
import org.odftoolkit.simple.common.navigation.TextSelection
import org.odftoolkit.simple.style.DefaultStyleHandler
import org.odftoolkit.simple.style.Font
import org.odftoolkit.simple.style.StyleTypeDefinitions
import org.odftoolkit.simple.text.Paragraph
import org.odftoolkit.simple.text.Span

import java.nio.file.Files
import java.nio.file.Paths


/**
 * @version 2017-03-05
 * @author Josef Baloun
 *
 * cookbook:
 * http://incubator.apache.org/odftoolkit/simple/document/cookbook/index.html
 */
class OdfdomTest {
    def path = "test.odt"
    def text = "This is ODF test"
    TextDocument odt

    @Before
    void setUp() throws Exception {
        odt = TextDocument.newTextDocument()

    }

    @Test
    void createFile() {
        odt.addParagraph(text)
        odt.save(path)
        TextDocument load = TextDocument.loadDocument(path)
        Paragraph par = load.getParagraphByIndex(0,true)
        assert text.equals(par.getTextContent())
        Files.deleteIfExists(Paths.get(path))
    }

    @Test
    void addHeading() {
        Paragraph par = odt.getParagraphByIndex(0,false)  //in an empty document is 1 empty paragraph
        par.applyHeading()  //is working?
        par.setTextContent(text)
        //odt.save(path)
        assert odt.getParagraphByIndex(0,false).isHeading()
    }

    @Test
    void addNonHeading() {
        Paragraph par = odt.addParagraph(text)
        par.setFont(new Font("Arial", StyleTypeDefinitions.FontStyle.ITALIC, 22, Color.RED))
        //odt.save(path)
        assert !odt.getParagraphByIndex(0,true).isHeading()
    }

    @Test
    void setSpanStyle() {
        Paragraph par = odt.addParagraph("blah neco zvyraznit *neco*")
        TextNavigation nav = new TextNavigation("neco", odt)

        if(nav.hasNext()) {
            TextSelection sel = (TextSelection) nav.nextSelection()
            Span sp = Span.newSpan(sel)
            DefaultStyleHandler dsh = sp.getStyleHandler()
            dsh.getTextPropertiesForWrite().setFont(new Font("Arial", StyleTypeDefinitions.FontStyle.ITALIC, 22, Color.RED))
        }
        //odt.save(path)
        assert nav.hasNext()
    }
}
