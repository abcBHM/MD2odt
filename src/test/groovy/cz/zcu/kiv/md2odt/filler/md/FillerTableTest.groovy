package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.parser.Parser
import cz.zcu.kiv.md2odt.document.Document
import cz.zcu.kiv.md2odt.document.TableCellContent.Align
import cz.zcu.kiv.md2odt.document.TableContent
import org.junit.Test

/**
 *
 * @version 2017-04-13
 * @author Patrik Harag
 */
class FillerTableTest {

    def parser = Parser.builder().extensions([TablesExtension.create()]).build()
    def filler = new FlexMarkFiller(parser)

    @Test
    void table() {
        def out = []
        def documentMock = [addTable: out.&push, addParagraph: {}] as Document

        String md = '''
            | h1  | h2  | h3  |
            | --- | --- | --- |
            | d1  | d2  | d3  |
        '''

        filler.fill(md.stripIndent(), documentMock)

        assert out.size() == 1
        out[0].with { table ->
            assert table instanceof TableContent
            assert table.rows.size() == 2

            assert table.rows[0]*.align == [Align.CENTER, Align.CENTER, Align.CENTER]
            assert table.rows[1]*.align == [Align.LEFT, Align.LEFT, Align.LEFT]

            assert table.rows[0]*.heading == [true, true, true]
            assert table.rows[1]*.heading == [false, false, false]
        }
    }

    @Test
    void tableAligned() {
        def out = []
        def documentMock = [addTable: out.&push, addParagraph: {}] as Document

        String md = '''
            | h1  | h2  | h3  |
            | :--- | :---: | ---: |
            | d1  | d2  | d3  |
        '''

        filler.fill(md.stripIndent(), documentMock)

        assert out.size() == 1
        out[0].with { table ->
            assert table instanceof TableContent
            assert table.rows.size() == 2

            table.rows.each {
                assert it*.align == [Align.LEFT, Align.CENTER, Align.RIGHT]
            }

            assert table.rows[0]*.heading == [true, true, true]
            assert table.rows[1]*.heading == [false, false, false]
        }
    }

}
