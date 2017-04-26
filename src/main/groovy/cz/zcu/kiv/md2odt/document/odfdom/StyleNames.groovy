package cz.zcu.kiv.md2odt.document.odfdom
/**
 * Contains style names used in OdfdomDocument
 */
enum StyleNames {
    BODY_TEXT("Text_20_body"),
    LIST("List"),
    HEADING("Heading", "Heading_20_1", "Heading_20_2", "Heading_20_3", "Heading_20_4", "Heading_20_5", "Heading_20_6", "Heading_20_7", "Heading_20_8", "Heading_20_9", "Heading_20_10"),
    QUOTE("Quotations"),
    CODE("Preformatted_20_Text"),
    HORIZONTAL_RULE("Horizontal_20_Line"),
    TABLE_HEADING("Table_20_Heading"),
    TABLE_CONTENTS("Table_20_Contents"),
    STRIKE("BHM_MD2odt_Strike"),
    SUB_SCRIPT("BHM_MD2odt_SubScript"),
    SUPER_SCRIPT("BHM_MD2odt_SuperScript")

    private String[] vals

    StyleNames(String... values) {
        this.vals = values
    }

    /** Returns style name String value.
     * @return Style name String value.
     * */
    String getValue() {
        return vals[0]
    }

    /** Returns style name String value of given level. If not found then the closest level is returned.
     * 
     * @param level Level of style name.
     * @return Style name String value.
     * */
    String getLevel(int level) {
        if(level >= vals.length)
            return vals[vals.length - 1]
        if(level < 0)
            return vals[0]
        return vals[level]
    }
}