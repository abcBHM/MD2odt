package cz.zcu.kiv.md2odt.document.odt


import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.w3c.dom.Node

/**
 * Created by pepe on 13. 3. 2017.
 */
class OdfSimpleWrapperTest {
    OdfSimpleWrapper odt

    @Before
    void setUp() throws Exception {
        odt = new OdfSimpleWrapper()
    }

    @Ignore
    @Test
    void example() throws Exception {
        String inp = OdfSimpleWrapper.escape("<&>&&<> &bold; text &bold; other") + "<italic<" + OdfSimpleWrapper.escape("<non-Italic<")
        String exp = "<&>&&<> &bold; text &bold; other" + "italic" + "<non-Italic<"
        odt.addHeading("Heading -10",-10)
        odt.addHeading("Heading 100",100)
        odt.addParagraph(inp)
        odt.italicAll()
        odt.reEscapeAll()
        assert odt.getLastNode().getTextContent().equals(exp)
     //   odt.save("test.odt")
    }

    @Ignore
    @Test
    void templateExample() throws Exception {
        odt = new OdfSimpleWrapper("test.odt")

        odt.addHeading("nadpis 1 se stylem sablony",1)
        //odt.save("template_test.odt")
    }

    @Test
    void italicAllTest() throws Exception {
        odt.addParagraph("This <is< not <Sparta<!")
        Node parNode = odt.getLastNode()
        //println(parNode.getTextContent())
        odt.italicAll()
        //println(parNode.getTextContent())
        assert parNode.getTextContent().equals("This is not Sparta!")
        //odt.save("test.odt")
    }

    @Test
    void boldAllTest() throws Exception {
        odt.addParagraph("This >is> not <Sp>art>a<!")
        Node parNode = odt.getLastNode()
        //println(parNode.getTextContent())
        odt.boldAll()
        //println(parNode.getTextContent())
        assert parNode.getTextContent().equals("This is not <Sparta<!")
        //odt.save("test.odt")
    }

    @Test
    void linkAllTest() throws Exception {
        odt.addParagraph("#@"+ OdfSimpleConstants.escape("https://www.seznam.cz/") +"@pokus#")
        odt.linkAll()
        assert odt.getLastNode().getTextContent().equals("pokus")
        odt.save("test.odt")
    }

    @Test
    void linkAllExceptionTest() throws Exception {
        odt.addParagraph("#@"+ OdfSimpleConstants.escape("wwwsezn^amcz") +"@pokus#")
        odt.linkAll()
        assert odt.getLastNode().getTextContent().equals("pokus (wwwsezn^amcz) ")
    }

    @Test
    void inlineCodeAllTest() throws Exception {
        odt.addParagraph("'"+ OdfSimpleConstants.escape("this.add(something)") +"'")
        odt.inlineCodeAll()
        assert odt.getLastNode().getTextContent().equals("this.add(something)") && odt.getLastNode().getChildNodes().item(1).getAttributes().item(0).toString().startsWith("style-name=")
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

    @Test(expected = FileNotFoundException.class)
    void templateNotFound1() throws Exception {
        odt = new OdfSimpleWrapper("wrongtemplate.odt")
    }
    @Test(expected = FileNotFoundException.class)
    void templateNotFound2() throws Exception {
        odt = new OdfSimpleWrapper(new File("wrongtemplate.odt"))
    }

    @Ignore
    @Test
    void bulletListTest() {
        ArrayList<String> list = new ArrayList<>()
        for (int i = 1; i < 4; i++) {
            list.add("Polozka " + i)
        }

        odt.addBulletList("Bullet list", list)
    }

    @Ignore
    @Test
    void numberedListTest() {
        ArrayList<String> list = new ArrayList<>()
        for (int i = 1; i < 4; i++) {
            list.add("Polozka " + i)
        }

        odt.addNumberList("Numbered list", list)
    }
}
