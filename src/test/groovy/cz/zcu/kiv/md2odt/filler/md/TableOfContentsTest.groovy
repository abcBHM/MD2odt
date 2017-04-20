package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ext.toc.SimTocExtension
import com.vladsch.flexmark.ext.toc.TocExtension
import com.vladsch.flexmark.parser.Parser
import cz.zcu.kiv.md2odt.document.Document
import org.junit.Test

/**
 *
 * @version 2017-04-20
 * @author Patrik Harag
 */
class TableOfContentsTest {

    static EXTENSIONS = [TocExtension.create(), SimTocExtension.create()]

    @Test
    void tableOfContents() {
        int i = 0
        def documentMock = [addTableOfContents: { i++ }] as Document

        def parser = Parser.builder().extensions(EXTENSIONS).build()
        def filler = new FlexMarkFiller(parser)

        def md = '''
            [TOC]
            [TOC style]
            
            [TOC]:#
            [TOC style]: # "Title"
        '''
        // we don't support styles and a title

        filler.fill(md.stripIndent(), documentMock)

        assert i == 4
    }

}
