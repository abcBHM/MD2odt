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
        odt.addHeading("Nadpis 1",1)
        odt.addHeading("Nadpis 10",10)

        odt.addParagraph("\"   &quot;\n" +
                "'   &apos;\n" +
                "<   &lt;\n" +
                ">   &gt;\n" +
                "&   &amp;")

        odt.save("test.odt")
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
        odt.addParagraph("This <em>is</em> not <em>Sparta</em>!")
        Node parNode = odt.getLastNode()
        //println(parNode.getTextContent())
        odt.emphasiseAll()
        //println(parNode.getTextContent())
        assert parNode.getTextContent().equals("This is not Sparta!")
        //odt.save("test.odt")
    }

    @Test
    void escapeTest() throws Exception {
        odt.addParagraph("This <em>is</em> not <em>Sparta</em>!")
        Node parNode = odt.getLastNode()
        //println(parNode.getTextContent())
        odt.emphasiseAll()
        //println(parNode.getTextContent())
        assert parNode.getTextContent().equals("This is not Sparta!")
        //odt.save("test.odt")
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
