package cz.zcu.kiv.md2odt.document.odt

import cz.zcu.kiv.md2odt.document.ListContent
import cz.zcu.kiv.md2odt.document.ListContentBuilder
import cz.zcu.kiv.md2odt.document.ListType
import cz.zcu.kiv.md2odt.document.ParagraphContentBuilder
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.odftoolkit.simple.text.Paragraph
import org.odftoolkit.simple.text.list.List
import org.w3c.dom.Node

import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by pepe on 13. 3. 2017.
 */
class OdfSimpleWrapperTest {
    OdfSimpleWrapper odt

    private static final String TEMPLATE = 'src/test/resources/template.odt'
    private static final String WRONG_TEMPLATE = 'wrongtemplate.odt'
    private static final String TEST = 'test.odt'

    @Before
    void setUp() throws Exception {
        odt = new OdfSimpleWrapper()
    }

    @Ignore
    @Test
    void example() throws Exception {
        odt.addParagraph("par")
        println(odt.lastNode.toString())
        odt.save(TEST)
    }

    @Ignore
    @Test
    void templateExample() throws Exception {
        odt = new OdfSimpleWrapper(TEST)

        odt.addHeading("nadpis 1 se stylem sablony",1)
        //odt.save("template_test.odt")
    }

    @Test
    void odfSimpleWrapperFileConstructor() throws Exception {
        odt = null
        odt = new OdfSimpleWrapper(new File(TEMPLATE))
        assert odt != null
    }

    @Test
    void odfSimpleWrapperStringConstructor() throws Exception {
        odt = null
        odt = new OdfSimpleWrapper(TEMPLATE)
        assert odt != null
    }

    @Test
    void odfSimpleWrapperFileInputStreamConstructor() throws Exception {
        odt = null
        odt = new OdfSimpleWrapper(new FileInputStream(TEMPLATE))
        assert odt != null
    }

    @Test(expected = FileNotFoundException.class)
    void odfSimpleWrapperTemplateNotFound1() throws Exception {
        odt = new OdfSimpleWrapper(WRONG_TEMPLATE)
    }

    @Test(expected = FileNotFoundException.class)
    void odfSimpleWrapperTemplateNotFound2() throws Exception {
        odt = new OdfSimpleWrapper(new File(WRONG_TEMPLATE))
    }

    @Test
    void save1() throws Exception {
        odt.save(TEST)
        File f = new File(TEST)
        assert f.exists()
        f.delete()
    }
    @Test
    void save2() throws Exception {
        odt.save(Files.newOutputStream(Paths.get(TEST)))
        File f = new File(TEST)
        assert f.exists()
        f.delete()
    }

    @Test
    void italicAllTest() throws Exception {
        odt.addParagraph("This <is< not <Sparta<!")
        odt.italicAll()
        assert odt.getLastNode().getTextContent().equals("This is not Sparta!")
    }

    @Test
    void boldAllTest() throws Exception {
        odt.addParagraph("This >is> not <Sp>art>a<!")
        odt.boldAll()
        assert odt.getLastNode().getTextContent().equals("This is not <Sparta<!")
    }

    @Test
    void linkAllTest() throws Exception {
        odt.addParagraph("#"+ OdfSimpleConstants.escape("https://www.seznam.cz/") +"@pokus#")
        odt.linkAll()
        assert odt.getLastNode().getTextContent().equals("pokus")
    }

    @Test
    void linkAllExceptionTest() throws Exception {
        odt.addParagraph("#"+ OdfSimpleConstants.escape("wwwsezn^amcz") +"@pokus#")
        odt.linkAll()
        assert odt.getLastNode().getTextContent().equals("pokus (wwwsezn^amcz) ")
    }

    @Test
    void inlineCodeAllTest() throws Exception {
        odt.addParagraph("'"+ OdfSimpleConstants.escape("this.add(something)") +"'")
        odt.inlineCodeAll()
        assert odt.getLastNode().getTextContent().equals("this.add(something)") && odt.getLastNode().getChildNodes().item(1).getAttributes().item(0).toString().startsWith("style-name=")
    }

    @Ignore
    @Test
    void imageAllTest() throws Exception {
        odt.addParagraph(OdfSimpleConstants.IMAGE.getMark() + OdfSimpleConstants.escape("obrazek.png")+"@alt@text" +OdfSimpleConstants.IMAGE.getMark()+
                OdfSimpleConstants.IMAGE.getMark() + OdfSimpleConstants.escape("https://www.seznam.cz/media/img/logo_v2.png")+"@alt@text" +OdfSimpleConstants.IMAGE.getMark()+
                OdfSimpleConstants.IMAGE.getMark() + OdfSimpleConstants.escape("https://image.ibb.co/inwVYv/uml.png")+"@alt@text" +OdfSimpleConstants.IMAGE.getMark()+
                OdfSimpleConstants.IMAGE.getMark() + OdfSimpleConstants.escape("https://image.ibb.co/inwVYv/uml.png")+"@alt@text" +OdfSimpleConstants.IMAGE.getMark()+
                OdfSimpleConstants.IMAGE.getMark() + OdfSimpleConstants.escape("https://image.ibb.co/inwVYv/uml.png")+"@alt@text" +OdfSimpleConstants.IMAGE.getMark()+
                OdfSimpleConstants.IMAGE.getMark() + OdfSimpleConstants.escape("https://www.seznam.cz/media/img/logo_v2.png")+"@alt@text" +OdfSimpleConstants.IMAGE.getMark()+
                OdfSimpleConstants.IMAGE.getMark() + OdfSimpleConstants.escape("https://www.seznam.cz/media/img/logo_v2.png")+"@alt@text" +OdfSimpleConstants.IMAGE.getMark()+
                OdfSimpleConstants.IMAGE.getMark() + OdfSimpleConstants.escape("https://image.ibb.co/inwVYv/uml.png")+"@alt@text" +OdfSimpleConstants.IMAGE.getMark()+
                OdfSimpleConstants.IMAGE.getMark() + OdfSimpleConstants.escape("https://www.seznam.cz/media/img/logo_v2.png")+"@alt@text" +OdfSimpleConstants.IMAGE.getMark()
        )
        odt.save(TEST)
    }

    @Test
    void imageAllOkTest() throws Exception {
        odt.addParagraph(OdfSimpleConstants.IMAGE.getMark() + OdfSimpleConstants.escape("https://www.seznam.cz/media/img/logo_v2.png")+"@alt@text" +OdfSimpleConstants.IMAGE.getMark())
        odt.imageAll()
        assert odt.getLastNode().getTextContent().equals("")
    }

    @Test
    void imageAllExceptionTest() throws Exception {
        odt.addParagraph(OdfSimpleConstants.IMAGE.getMark() + OdfSimpleConstants.escape("https://image.ibb.co/inwVYv/uml.png")+"@alt@text" +OdfSimpleConstants.IMAGE.getMark())
        odt.imageAll()
        assert odt.getLastNode().getTextContent().equals(OdfSimpleConstants.escape(" Image (https://image.ibb.co/inwVYv/uml.png) "))
    }

    @Test
    void reEscapeAllTest() throws Exception {
        String inp = OdfSimpleConstants.escape("<&>&&<> &bold; text &bold; other") + "<italic<" + OdfSimpleConstants.escape("<non-Italic<")
        String exp = "<&>&&<> &bold; text &bold; other" + "italic" + "<non-Italic<"
        odt.addParagraph(inp)
        odt.italicAll()
        odt.reEscapeAll()
        assert odt.getLastNode().getTextContent().equals(exp)
    }

    @Ignore
    @Test
    void horizontalRuleTest() {
        odt.addHorizontalRule()
        odt.save("test.odt")
    }

    @Ignore
    @Test
    void bulletListTest() {
        ArrayList<Object> list = new ArrayList<>()
        for (int i = 1; i < 4; i++) {
            list.add("Polozka " + i)
        }

        List odfList = odt.addList("Bullet list", OdfListEnum.BULLET_LIST)
        odt.addItemsToList(odfList, list)
        odt.save("test.odt")
    }

    @Ignore
    @Test
    void numberedListTest() {
        ArrayList<String> list = new ArrayList<>()
        for (int i = 1; i < 4; i++) {
            list.add("Polozka " + i)
        }

        List odfList = odt.addList("Numbered list", OdfListEnum.NUMBERED_LIST)
        odt.addItemsToList(odfList, list)
        odt.save("test.odt")
    }

    @Ignore
    @Test
    void subListTest() {
        ArrayList<String> list = new ArrayList<>()
        for (int i = 1; i < 4; i++) {
            list.add("Polozka " + i)
        }

        List odfList = odt.addList("Nested list", OdfListEnum.NUMBERED_LIST)
        odt.addItemsToList(odfList, list)
        List nestedList = odt.addSubList(odfList, OdfListEnum.BULLET_LIST)
        odt.addItemsToList(nestedList, list)
        odt.save("test.odt")
    }
}
