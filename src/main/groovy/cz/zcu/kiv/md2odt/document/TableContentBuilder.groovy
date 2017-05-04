package cz.zcu.kiv.md2odt.document

import groovy.transform.Immutable

/**
 *
 * @version 2017-04-13
 * @author Patrik Harag
 */
class TableContentBuilder {

    @Immutable
    private static class TableContentImpl implements TableContent {
        List<List<TableCellContent>> rows
    }

    /**
     * Creates an instance of TableContentBuilder to build a TableContent.
     *
     * @return instance of TableContentBuilder to build a TableContent
     */
    static TableContentBuilder builder() {
        new TableContentBuilder()
    }


    private List<List<TableCellContent>> rows = []

    private TableContentBuilder() {
    }

    /** Adds a table row to a builder.
     *
     * @param row row content
     * @return builder
     * */
    TableContentBuilder addRow(List<TableCellContent> row) {
        this.rows.add(row)
        return this
    }

    /**
     * Creates TableContent from builder.
     *
     * @return TableContent of a builded table
     */
    TableContent build() {
        return new TableContentImpl(rows)
    }

}
