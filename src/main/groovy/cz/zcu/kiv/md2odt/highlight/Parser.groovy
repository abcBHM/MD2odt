package cz.zcu.kiv.md2odt.highlight

import cz.zcu.kiv.md2odt.highlight.content.CodeSection

/**
 * Highlighter interface
 *
 * @version 2017-04-06
 * @author Vít Mazín
 */
interface Parser {

    /** Returns a list of CodeSection, where is specified how to style a text segment.
     *
     * @param code  Code to parse.
     * @param lang  Language used.
     * @return list of CodeSection
     */
    List<CodeSection> parse(String code, String lang)
}