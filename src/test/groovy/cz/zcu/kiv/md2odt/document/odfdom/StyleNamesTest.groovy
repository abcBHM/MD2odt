import cz.zcu.kiv.md2odt.document.odfdom.StyleNames
import org.junit.Test

/**
 * Created by pepe on 5. 4. 2017.
 */
class StyleNamesTest {
    @Test
    void getValueTest() throws Exception {
        assert StyleNames.BODY_TEXT.getValue().equals("Text_20_body")
    }

    @Test
    void getLevelTestOk1() throws Exception {
        assert StyleNames.HEADING.getLevel(0).equals("Heading")
    }

    @Test
    void getLevelTestOk2() throws Exception {
        assert StyleNames.HEADING.getLevel(10).equals("Heading_20_10")
    }

    @Test
    void getLevelTestOk3() throws Exception {
        assert StyleNames.HEADING.getLevel(5).equals("Heading_20_5")
    }

    @Test
    void getLevelTestWrong1() throws Exception {
        assert StyleNames.HEADING.getLevel(-1).equals("Heading")
    }

    @Test
    void getLevelTestWrong2() throws Exception {
        assert StyleNames.HEADING.getLevel(11).equals("Heading_20_10")
    }


    @Test
    void getLevelTestWrong3() throws Exception {
        assert StyleNames.BODY_TEXT.getLevel(2).equals("Text_20_body")
    }
}
