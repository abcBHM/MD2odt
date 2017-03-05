package cz.zcu.kiv.md2odt

import org.junit.Test
import org.odftoolkit.simple.TextDocument
import org.odftoolkit.simple.text.Paragraph

import java.nio.file.Files
import java.nio.file.Paths

/**
 * @version 2017-03-05
 * @author Josef Baloun
 */
class OdfdomTest {

    @Test
    void createFile() {
        try {
            def text = "This is ODF creation test"
            def path = "test.odt"
            def odt = TextDocument.newTextDocument()
            odt.addParagraph(text)
            odt.save(path)

            def load = TextDocument.loadDocument(path)
            Paragraph par = load.getParagraphByIndex(0,true)

            assert text.equals(par.getTextContent())

            Files.deleteIfExists(Paths.get(path))
        } catch (Exception e) {
            e.printStackTrace()
        }
    }
}
