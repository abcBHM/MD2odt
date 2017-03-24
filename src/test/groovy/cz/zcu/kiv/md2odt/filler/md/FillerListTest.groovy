package cz.zcu.kiv.md2odt.filler.md

import cz.zcu.kiv.md2odt.document.Document
import cz.zcu.kiv.md2odt.document.ListContent
import cz.zcu.kiv.md2odt.document.ListType
import cz.zcu.kiv.md2odt.document.ParagraphContent
import org.junit.Test

/**
 *
 * @version 2017-03-24
 * @author Patrik Harag
 */
class FillerListTest {

    def filler = new FlexMarkFiller()

    @Test
    void bulletList() {
        def out = []
        def documentMock = [addList: { l -> out.push(l) }] as Document

        def code = """
            * 1
            * *2* ab **3**
        """.stripIndent()

        filler.fill(code, documentMock)

        assert out.size() == 1  // only one list
        out[0].with { list ->
            assert list instanceof ListContent
            assert list.type == ListType.BULLET

                                    // only one BlockContent
            assert list.listItems.collect { it[0].list*.text } == [['1'], ['2', ' ab ', '3']]
        }
    }

    @Test
    void orderedList() {
        def out = []
        def documentMock = [addList: { l -> out.push(l) }] as Document

        def code = """
            1. one
            2. *two*
        """.stripIndent()

        filler.fill(code, documentMock)

        assert out.size() == 1  // only one list
        out[0].with { list ->
            assert list instanceof ListContent
            assert list.type == ListType.ORDERED

                                    // only one BlockContent
            assert list.listItems.collect { it[0].list*.text } == [['one'], ['two']]
        }
    }

    @Test
    void nested() {
        def out = []
        def documentMock = [addList: { l -> out.push(l) }] as Document

        def code = """
            1. paragraph
                * 1
                * 2
            2. two
        """.stripIndent()

        filler.fill(code, documentMock)

        assert out.size() == 1  // only one list
        out[0].with { list ->
            assert list instanceof ListContent
            assert list.type == ListType.ORDERED
            assert list.listItems.size() == 2

            list.listItems[0].with {
                assert it.size() == 2

                assert it[0] instanceof ParagraphContent
                assert it[0].list*.text == ['paragraph']

                assert it[1] instanceof ListContent
                assert it[1].type == ListType.BULLET
                // ...
            }

            list.listItems[1].with {
                assert it.size() == 1

                assert it[0] instanceof ParagraphContent
                assert it[0].list*.text == ['two']
            }
        }
    }

}
