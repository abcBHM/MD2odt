package cz.zcu.kiv.md2odt.document

import groovy.transform.Immutable

/**
 * Builder for {@link ListContent}.
 *
 * @version 2017-03-24
 * @author Patrik Harag
 */
class ListContentBuilder {

    @Immutable
    private static class ListContentImpl implements ListContent {
        ListType type
        List<List<BlockContent>> listItems
    }

    static ListContentBuilder builder(ListType type) {
        new ListContentBuilder(type)
    }


    private final ListType type
    private final List<List<BlockContent>> buffer = []

    // private constructor
    private ListContentBuilder(ListType type) {
        this.type = type
    }

    ListContentBuilder addListItem(List<BlockContent> list) {
        buffer.add(new ArrayList<BlockContent>(list))  // add copy
        return this
    }

    ListContentBuilder addListItem(BlockContent blockContent) {
        addListItem([blockContent])
    }

    ListContent build() {
        new ListContentImpl(type: this.type, listItems: this.buffer)
    }

}
