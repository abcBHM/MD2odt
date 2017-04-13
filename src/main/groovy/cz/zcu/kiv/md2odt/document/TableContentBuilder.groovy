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

    static TableContentBuilder builder() {
        new TableContentBuilder()
    }


    private List<List<TableCellContent>> rows = []

    private TableContentBuilder() {
    }

    TableContentBuilder addRow(List<TableCellContent> row) {
        this.rows.add(row)
        return this
    }

    TableContent build() {
        return new TableContentImpl(rows)
    }

}
