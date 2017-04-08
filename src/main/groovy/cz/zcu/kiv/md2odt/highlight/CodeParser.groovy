package cz.zcu.kiv.md2odt.highlight

import cz.zcu.kiv.md2odt.highlight.content.CodeLang
import cz.zcu.kiv.md2odt.highlight.content.CodeSection
import cz.zcu.kiv.md2odt.highlight.content.CodeSectionImpl
import org.apache.log4j.Logger

/**
 * Created by pepe on 6. 4. 2017.
 */
class CodeParser implements Parser {
    private static final Logger LOGGER = Logger.getLogger(CodeParser)

    @Override
    /** Returns a list of CodeSection, where is specified how to style a text segment.
     *
     * @param code  Code to parse.
     * @param lang  Language used.
     * @return list of CodeSection
     */
    List<CodeSection> parse(String code, CodeLang lang) {
        LOGGER.warn("parse(String code, CodeLang lang) not implemented")
        [new CodeSectionImpl(code)]
    }
}
