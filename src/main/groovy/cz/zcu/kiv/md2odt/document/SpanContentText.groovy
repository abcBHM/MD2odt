package cz.zcu.kiv.md2odt.document

import groovy.transform.Immutable

/**
 *
 * @version 2017-03-15
 * @author Patrik Harag
 */
@Immutable
class SpanContentText implements SpanContent {

    String text
    SpanType type

}
