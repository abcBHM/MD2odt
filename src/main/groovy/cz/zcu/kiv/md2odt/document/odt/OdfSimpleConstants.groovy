package cz.zcu.kiv.md2odt.document.odt

/**
 * Created by pepe on 14. 3. 2017.
 */
enum OdfSimpleConstants {
    ITALIC("<", "&italic;"),
    BOLD(">", "&bold;"),
    LINK("#", "&link;"),
    LINK_HREF("@", "&lhref;"),
    INLINE_CODE("'", "&icode;")


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

    static String escape(String toEscape) {
        String rtn = toEscape.replaceAll(AMP_MARK, AMP_ESCAPE)   //& has to be replaced first
        for (OdfSimpleConstants osc : values()) {
            rtn = rtn.replaceAll(osc.getMark(), osc.getEscape())
        }
        return rtn
    }

    static String reEscape(String toReEscape) {
        String rtn = toReEscape
        for (OdfSimpleConstants osc : values()) {
            rtn = rtn.replaceAll(osc.getEscape(), osc.getMark())
        }
        rtn = rtn.replaceAll(AMP_ESCAPE, AMP_MARK)   //& has to be replaced last
        return rtn
    }
}