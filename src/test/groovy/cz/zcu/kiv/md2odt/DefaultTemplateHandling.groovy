package cz.zcu.kiv.md2odt

import org.junit.Ignore
import org.junit.Test
import org.odftoolkit.simple.TextDocument

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


/**
 * Created by pepe on 4. 5. 2017.
 */
@Ignore
class DefaultTemplateHandling {
    String templatePath = "src/main/resources/default-template.odt"

    @Ignore
    @Test
    void removeParagraphsFromTemplate() throws Exception {
        Path from = Paths.get(templatePath)
        String bckup = templatePath.substring(0, templatePath.length()-4) + "_backup.odt"
        Path to = Paths.get(bckup)
        Files.copy(from,to)


        TextDocument odt = TextDocument.loadDocument(templatePath)
        def par = odt.getParagraphByIndex(0,false)
        while (par != null) {
            par.remove()
            par = odt.getParagraphByIndex(0,false)
        }
        odt.save(templatePath)
    }
}
