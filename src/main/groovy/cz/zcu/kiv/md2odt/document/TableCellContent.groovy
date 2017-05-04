package cz.zcu.kiv.md2odt.document

/**
 *
 * @version 2017-04-13
 * @author Patrik Harag
 */
class TableCellContent {

    static enum Align {
        LEFT, CENTER, RIGHT
    }

    final BlockContent content
    final Align align
    final boolean heading

    /** TableCellContent constructor.
     *
     * @param content content of a table cell
     * @param align alignment of a table cell
     * @param heading if a table cell should be formatted as heading table cell
     * */
    TableCellContent(BlockContent content, Align align, boolean heading) {
        this.content = content
        this.align = align
        this.heading = heading
    }

}
