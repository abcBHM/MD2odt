package cz.zcu.kiv.md2odt.document.odfdom
/**
 * Created by pepe on 5. 4. 2017.
 */
enum StyleNames {
    BODY_TEXT("Text_20_body"),
    LIST("List"),
    HEADING("Heading", "Heading_20_1", "Heading_20_2", "Heading_20_3", "Heading_20_4", "Heading_20_5", "Heading_20_6", "Heading_20_7", "Heading_20_8", "Heading_20_9", "Heading_20_10"),
    QUOTE("Quotations"),
    CODE("Preformatted_20_Text"),
    HORIZONTAL_RULE("Horizontal_20_Line")




    private String[] vals

    StyleNames(String... values) {
        this.vals = values
    }

    String getValue() {
        return vals[0]
    }

    String getLevel(int level) {
        if(level >= vals.length)
            return vals[vals.length - 1]
        if(level < 0)
            return vals[0]
        return vals[level]
    }
}