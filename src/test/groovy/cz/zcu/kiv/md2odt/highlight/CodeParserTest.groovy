package cz.zcu.kiv.md2odt.highlight

import cz.zcu.kiv.md2odt.highlight.content.CodeSection
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

/**
 * Created by n5ver on 06.04.2017.
 */

class CodeParserTest {
    CodeParser parser

    @Before
    void setUp() {
        parser = new CodeParser()
    }

    @Ignore
    @Test
    void parserTest() {
        String code = """package cz.zcu.kiv.md2odt.document.odt

                        import org.apache.xerces.dom.TextImpl
                        
                        /**
                         * Created by pepe on 13. 3. 2017.
                         * cookbook:
                         * http://incubator.apache.org/odftoolkit/simple/document/cookbook/index.html
                         */
                        @Deprecated
                        class OdfSimpleWrapper {
                            private TextDocument odt
                        
                            OdfSimpleWrapper() {
                                odt = TextDocument.newTextDocument()
                                Node n = odt.getContentDom().getElementsByTagName("office:text").item(0)
                                n.removeChild(n.lastChild)      //delete an empty paragraph
                            }
                        
                        }""".stripIndent()
        String lang = "groovy"

        List<CodeSection> list = parser.parse(code, lang)


        list.forEach({
            println it.getType().getName() + " - " + it.getText()
        })
    }

    @Test
    void JavaKnownLangTest() {
        def langs = ["Java", "Groovy", "C", "C++", "PHP", "C#"]
        langs.forEach({
            assert parser.isKnownLanguage(it)
        })
    }

    @Ignore
    @Test
    void JavaUnknownLangTest() {
        def langs = ["Fortran"]
        langs.forEach({
            assert !parser.isKnownLanguage(it)
        })
    }
}
