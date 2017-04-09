package cz.zcu.kiv.md2odt.highlight

import cz.zcu.kiv.md2odt.highlight.content.CodeSection

/**
 * Created by vita on 08.04.2017.
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