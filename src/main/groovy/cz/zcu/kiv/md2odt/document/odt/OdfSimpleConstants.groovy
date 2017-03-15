package cz.zcu.kiv.md2odt.document.odt

/**
 * Created by pepe on 14. 3. 2017.
 */
enum OdfSimpleConstants {
    ITALIC("<", "&italic;"),
    BOLD(">", "&bold;"),
    LINK("#", "&link;"),
    LINK_HREF("^", "&lhref;")


    static String AMP_MARK = "&"
    static String AMP_ESCAPE = "&amp;"

    private String mark
    private String escape

    private OdfSimpleConstants(String mark, String escape) {
        this.mark = mark
        this.escape = escape
    }

    String getMark() {
        return mark
    }

    String getEscape() {
        return escape
    }
}