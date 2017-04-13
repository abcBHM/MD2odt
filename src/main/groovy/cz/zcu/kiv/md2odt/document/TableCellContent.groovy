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

    final ParagraphContent content
    final Align align
    final boolean heading

    TableCellContent(ParagraphContent content, Align align, boolean heading) {
        this.content = content
        this.align = align
        this.heading = heading
    }

}
