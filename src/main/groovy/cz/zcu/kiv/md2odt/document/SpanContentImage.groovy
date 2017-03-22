package cz.zcu.kiv.md2odt.document

import groovy.transform.Immutable

/**
 *
 * @version 2017-03-22
 * @author Patrik Harag
 */
@Immutable
class SpanContentImage implements SpanContent {

    // ![<alt>](<url> <text>)

    String text  // usually shows on hover
    String url
    String alt

    SpanType type

}
