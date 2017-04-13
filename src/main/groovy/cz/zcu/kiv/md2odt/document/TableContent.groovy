package cz.zcu.kiv.md2odt.document


/**
 *
 * @version 2017-04-13
 * @author Patrik Harag
 */
interface TableContent extends BlockContent {

    List<List<TableCellContent>> getRows()

}
