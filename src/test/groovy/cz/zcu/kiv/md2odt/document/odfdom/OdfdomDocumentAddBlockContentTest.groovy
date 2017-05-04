package cz.zcu.kiv.md2odt.document.odfdom

import cz.zcu.kiv.md2odt.document.*
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by pepe on 4. 5. 2017.
 */
class OdfdomDocumentAddBlockContentTest {
    private static final String TEST = 'test.odt'

    OdfdomDocument doc
    LastNode last

    @Before
    void setUp() throws Exception {
        doc = new OdfdomDocument()
        last = new LastNode(doc)
    }

    @After
    public void tearDown() throws Exception {
     //   doc.save(TEST)
    }

    @Test
    public void addListInList() throws Exception {
        ParagraphContent pc = ParagraphContentBuilder.builder().addRegular("Paragraph").build()
        ListContent content = ListContentBuilder.builder(ListType.BULLET)
                .addListItem(pc)
                .addListItem(ListContentBuilder.builder(ListType.ORDERED)
                                    .addListItem(pc)
                                    .addListItem(pc)
                                    .addListItem(pc)
                                    .build())
                .addListItem(pc)
                .build()
        doc.addBlockContent(content)
    }

    @Test
    public void addListInTable() throws Exception {
        def pc = ParagraphContentBuilder.builder().addRegular("Paragraph").build()

        ListContent listContent = ListContentBuilder.builder(ListType.BULLET)
                .addListItem(pc)
                .addListItem(ListContentBuilder.builder(ListType.ORDERED)
                .addListItem(pc)
                .addListItem(pc)
                .addListItem(pc)
                .build())
                .addListItem(pc)
                .build()

        List<TableCellContent> ltcc = [
                new TableCellContent(pc, TableCellContent.Align.LEFT, false),
                new TableCellContent(listContent, TableCellContent.Align.LEFT, false),
                new TableCellContent(pc, TableCellContent.Align.CENTER, true),
                new TableCellContent(pc, TableCellContent.Align.RIGHT, false)
        ]
        def content = TableContentBuilder.builder().addRow(ltcc).build()
        doc.addBlockContent(content)
    }

    @Test
    public void addTable() throws Exception {
        def pc = ParagraphContentBuilder.builder().addRegular("Paragraph").build()
        List<TableCellContent> ltcc = [
                new TableCellContent(pc, TableCellContent.Align.LEFT, false),
                new TableCellContent(pc, TableCellContent.Align.CENTER, true),
                new TableCellContent(pc, TableCellContent.Align.RIGHT, false)
        ]
        def content = TableContentBuilder.builder().addRow(ltcc).build()
        doc.addBlockContent(content)
    }

    @Test
    public void addTableInList() throws Exception {
        def pc = ParagraphContentBuilder.builder().addRegular("Paragraph").build()

        List<TableCellContent> ltcc = [
                new TableCellContent(pc, TableCellContent.Align.LEFT, false),
                new TableCellContent(pc, TableCellContent.Align.CENTER, true),
                new TableCellContent(pc, TableCellContent.Align.RIGHT, false)
        ]
        def tc = TableContentBuilder.builder().addRow(ltcc).build()

        def lc = ListContentBuilder.builder(ListType.BULLET)
                .addListItem(pc)
                .addListItem(tc)
                .addListItem(pc)
                .build()

        doc.addBlockContent(lc)
    }

    @Test
    public void addTableInTable() throws Exception {
        def pc = ParagraphContentBuilder.builder().addRegular("Paragraph").build()

        List<TableCellContent> ltcc = [
                new TableCellContent(pc, TableCellContent.Align.LEFT, false),
                new TableCellContent(pc, TableCellContent.Align.CENTER, true),
                new TableCellContent(pc, TableCellContent.Align.RIGHT, false)
        ]
        def tc = TableContentBuilder.builder().addRow(ltcc).build()

        List<TableCellContent> ltcc2 = [
                new TableCellContent(pc, TableCellContent.Align.LEFT, false),
        ]
        List<TableCellContent> ltcc3 = [
                new TableCellContent(tc, TableCellContent.Align.LEFT, false),
        ]

        def tc2 = TableContentBuilder.builder()
                .addRow(ltcc2)
                .addRow(ltcc3)
                .build()

        doc.addBlockContent(tc2)
    }
}
