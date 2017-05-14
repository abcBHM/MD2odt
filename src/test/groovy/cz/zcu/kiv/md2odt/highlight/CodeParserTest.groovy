package cz.zcu.kiv.md2odt.highlight

import cz.zcu.kiv.md2odt.highlight.content.CodeSection
import cz.zcu.kiv.md2odt.highlight.content.CodeSectionType
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

    @Test
    void knownLangTest() {
        def langs = ["Java", "Groovy", "C", "C++", "PHP", "C#"]
        langs.forEach({
            assert parser.isKnownLanguage(it)
        })
    }

    @Test
    void unknownLangTest() {
        def langs = ["unknown"]
        langs.forEach({
            assert !parser.isKnownLanguage(it)
        })
    }

    @Test
    void switchTypeTest() {
        def types = ["None", "Keyword", "Text", "Whitespace", "Name", "Literal", "String", "Number", "Operator", "Punctuation", "Comment", "Generic"]

        types.forEach({
            def type = parser.switchType(it)
            assert type.getName().equals(it)
        })
    }
}
