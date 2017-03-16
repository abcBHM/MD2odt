package cz.zcu.kiv.md2odt.document.odt

import cz.zcu.kiv.md2odt.document.Document
import cz.zcu.kiv.md2odt.document.ParagraphContent
import cz.zcu.kiv.md2odt.document.SpanContent
import cz.zcu.kiv.md2odt.document.SpanContentLink
import cz.zcu.kiv.md2odt.document.SpanType

/**
 * Created by pepe on 15. 3. 2017.
 */
class OdfSimpleDocument implements Document{
    private OdfSimpleWrapper doc

    OdfSimpleDocument() {
        doc = new OdfSimpleWrapper()
    }

    protected String parToString(ParagraphContent content) {
        String rtn = ""
        for(SpanContent sc : content.list) {
            switch (sc.getType()) {
                case SpanType.REGULAR:
                    rtn += OdfSimpleConstants.escape(sc.getText())
                    break
                case SpanType.BOLD:
                    rtn += OdfSimpleConstants.BOLD.getMark() + OdfSimpleConstants.escape(sc.getText()) + OdfSimpleConstants.BOLD.getMark()
                    break
                case SpanType.ITALIC:
                    rtn += OdfSimpleConstants.ITALIC.getMark() + OdfSimpleConstants.escape(sc.getText()) + OdfSimpleConstants.ITALIC.getMark()
                    break
                case SpanType.LINK:
                    if(sc instanceof SpanContentLink) {
                        String href = OdfSimpleConstants.LINK_HREF.getMark() + OdfSimpleConstants.escape(sc.url) + OdfSimpleConstants.LINK_HREF.getMark()
                        rtn += OdfSimpleConstants.LINK.getMark() + href + OdfSimpleConstants.escape(sc.getText()) + OdfSimpleConstants.LINK.getMark()
                    } else {
                        rtn += OdfSimpleConstants.escape(sc.getText())
                    }
                    break
                default:
                    throw new UnsupportedOperationException("Not implemented in OdfSimpleDocument yet")
            }

        }
        return rtn
    }

    /** Returns the wrapper instance. Used for testing.
     *
     * @return wrapper instance
     * */
    @Deprecated
    protected OdfSimpleWrapper getOdfSimpleWrapper() {
        return doc
    }

    void save(String documentPath) {
        doc.save(documentPath)
    }

    @Override
    void addParagraph(ParagraphContent content) {
        doc.addParagraph(parToString(content))
    }
}
