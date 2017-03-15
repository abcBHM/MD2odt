package cz.zcu.kiv.md2odt.filler

import cz.zcu.kiv.md2odt.document.Document

/**
 *
 * @version 2017-03-15
 * @author Patrik Harag
 */
interface Filler {

    void fill(String md, Document document)

}