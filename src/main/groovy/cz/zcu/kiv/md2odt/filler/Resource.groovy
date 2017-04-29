package cz.zcu.kiv.md2odt.filler

import groovy.transform.TupleConstructor

/**
 * Data object for holding resource.
 *
 * @version 2017-04-29
 * @author Patrik Harag
 */
@TupleConstructor
class Resource {

    InputStream inputStream
    long size

}
