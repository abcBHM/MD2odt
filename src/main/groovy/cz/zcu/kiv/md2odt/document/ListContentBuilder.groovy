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

    /**
     * Creates an instance of ListContentBuilder to build a ListContent.
     *
     * @param type ListType of a list
     * @return instance of ListContentBuilder to build a ListContent
     */
    static ListContentBuilder builder(ListType type) {
        new ListContentBuilder(type)
    }


    private final ListType type
    private final List<List<BlockContent>> buffer = []

    // private constructor
    private ListContentBuilder(ListType type) {
        this.type = type
    }

    /**
     * Adds list item to builder.
     *
     * @param list list item contents
     * @return builder
     */
    ListContentBuilder addListItem(List<BlockContent> list) {
        buffer.add(new ArrayList<BlockContent>(list))  // add copy
        return this
    }

    /**
     * Adds list item to builder.
     *
     * @param list list item content
     * @return builder
     */
    ListContentBuilder addListItem(BlockContent blockContent) {
        addListItem([blockContent])
    }

    /**
     * Creates ListContent from builder.
     *
     * @return ListContent of a builded list
     */
    ListContent build() {
        new ListContentImpl(type: this.type, listItems: this.buffer)
    }

}
