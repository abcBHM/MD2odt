package cz.zcu.kiv.md2odt.document

/**
 * Interface for list content.
 *
 * @version 2017-03-24
 * @author Patrik Harag
 */
interface ListContent extends BlockContent {

    /**
     * Returns type of this list.
     *
     * @return type
     */
    ListType getType()

    /**
     * Returns list of list items. Every list item can contain one or more
     * blocks - for example another list and a paragraph...
     *
     * @return list
     */
    List<List<BlockContent>> getListItems()

}
