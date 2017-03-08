package cz.zcu.kiv.md2odt

import org.junit.Test
import org.odftoolkit.odfdom.type.Color
import org.odftoolkit.simple.TextDocument
import org.odftoolkit.simple.style.Font
import org.odftoolkit.simple.style.StyleTypeDefinitions
import org.odftoolkit.simple.text.Paragraph

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

    @Test
    void createFile() {
        def text = "This is ODF creation test"
        TextDocument odt = TextDocument.newTextDocument()
        odt.addParagraph(text)
        odt.save(path)

        TextDocument load = TextDocument.loadDocument(path)
        Paragraph par = load.getParagraphByIndex(0,true)

        assert text.equals(par.getTextContent())
        Files.deleteIfExists(Paths.get(path))
    }

    @Test
    void addHeading() {
        def text = "This is HEADING test"
        TextDocument odt = TextDocument.newTextDocument()
        Paragraph par = odt.getParagraphByIndex(0,false)  //in an empty document is 1 empty paragraph

        par.applyHeading()  //is working?
        par.setTextContent(text)

        par = odt.addParagraph(text + " 2")

        par.setFont(new Font("Arial", StyleTypeDefinitions.FontStyle.ITALIC, 22, Color.RED))

        odt.save(path)

       // println(par.getStyleName())
        assert odt.getParagraphByIndex(0,false).isHeading()
        Files.deleteIfExists(Paths.get(path))
    }
}
