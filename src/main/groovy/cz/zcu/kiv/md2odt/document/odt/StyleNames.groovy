package cz.zcu.kiv.md2odt.document.odt

import com.vladsch.flexmark.ast.Heading

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




    private String[] value

    StyleNames(String... values) {
        this.value = values
    }

    String getValue() {
        return value[0]
    }

    String getLevel(int level) {
        if(level >= value.length)
            return value[value.length - 1]
        if(level < 0)
            return value[0]
        return value[level]
    }
}