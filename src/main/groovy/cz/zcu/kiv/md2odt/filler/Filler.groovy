package cz.zcu.kiv.md2odt.filler

import cz.zcu.kiv.md2odt.document.Document

/**
 * Parses input source and fills output document.
 *
 * @version 2017-04-21
 * @author Patrik Harag
 */
interface Filler {

    /**
     * Parses input source and fills output document.
     *
     * @param source input source
     * @param resources resource manager
     * @param document output document
     */
    void fill(String source, ResourceManager resources, Document document)

}