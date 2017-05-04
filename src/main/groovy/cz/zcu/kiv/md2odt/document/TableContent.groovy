package cz.zcu.kiv.md2odt.document


/**
 *
 * @version 2017-04-13
 * @author Patrik Harag
 */
interface TableContent extends BlockContent {

    /** Returns rows of table.
     * @return rows of table
     * */
    List<List<TableCellContent>> getRows()

}
