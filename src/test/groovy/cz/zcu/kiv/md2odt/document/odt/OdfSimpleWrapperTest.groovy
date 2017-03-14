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
        odt.addParagraph(inp)
        odt.emphasiseAll()
        odt.reEscapeAll()
        assert odt.getLastNode().getTextContent().equals(exp)
    }

    @Ignore
    @Test
    void templateExample() throws Exception {
        odt = new OdfSimpleWrapper("test.odt")

        odt.addHeading("nadpis 1 se stylem sablony",1)
        //odt.save("template_test.odt")
    }

    @Test
    void emphasiseAllTest() throws Exception {
        odt.addParagraph("This <is< not <Sparta<!")
        Node parNode = odt.getLastNode()
        //println(parNode.getTextContent())
        odt.emphasiseAll()
        //println(parNode.getTextContent())
        assert parNode.getTextContent().equals("This is not Sparta!")
        //odt.save("test.odt")
    }

    @Test
    void reEscapeAllTest() throws Exception {
        String inp = OdfSimpleWrapper.escape("<&>&&<> &bold; text &bold; other") + "<italic<" + OdfSimpleWrapper.escape("<non-Italic<")
        String exp = "<&>&&<> &bold; text &bold; other" + "italic" + "<non-Italic<"
        odt.addParagraph(inp)
        odt.emphasiseAll()
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

    @Test(expected = IllegalArgumentException.class)
    void addHeadingIllegalArgument1() throws Exception {
        odt.addHeading("Nadpis",0)
    }
    @Test(expected = IllegalArgumentException.class)
    void addHeadingIllegalArgument2() throws Exception {
        odt.addHeading("Nadpis",11)
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
