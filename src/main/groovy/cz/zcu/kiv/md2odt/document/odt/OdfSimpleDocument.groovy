package cz.zcu.kiv.md2odt.document.odt

import cz.zcu.kiv.md2odt.document.DocumentAdapter
import cz.zcu.kiv.md2odt.document.ListContent
import cz.zcu.kiv.md2odt.document.ParagraphContent
import cz.zcu.kiv.md2odt.document.SpanContent
import cz.zcu.kiv.md2odt.document.SpanContentImage
import cz.zcu.kiv.md2odt.document.SpanContentLink
import cz.zcu.kiv.md2odt.document.SpanType

/**
 * Created by pepe on 15. 3. 2017.
 */
class OdfSimpleDocument implements DocumentAdapter {
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
                        rtn += OdfSimpleConstants.LINK.getMark() + OdfSimpleConstants.escape(sc.getUrl()) + OdfSimpleConstants.PARAM.getMark() + OdfSimpleConstants.escape(sc.getText()) + OdfSimpleConstants.LINK.getMark()
                    } else {
                        rtn += OdfSimpleConstants.escape(sc.getText())
                    }
                    break
                case SpanType.CODE:
                    rtn += OdfSimpleConstants.INLINE_CODE.getMark() + OdfSimpleConstants.escape(sc.getText()) + OdfSimpleConstants.INLINE_CODE.getMark()
                    break
                case SpanType.IMAGE:
                    if(sc instanceof SpanContentImage) {
                        rtn +=  OdfSimpleConstants.IMAGE.getMark() +
                                        OdfSimpleConstants.escape(sc.getUrl()) +
                                    OdfSimpleConstants.PARAM.getMark() +
                                        OdfSimpleConstants.escape(sc.getAlt()) +
                                    OdfSimpleConstants.PARAM.getMark() +
                                        OdfSimpleConstants.escape(sc.getText()) +
                                OdfSimpleConstants.IMAGE.getMark()
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

  /*  void addCodeBlock(String code) {
        addCodeBlock(code, null)
    }

    void addCodeBlock(String code, String lang) {
        throw new UnsupportedOperationException()
    }*/

    @Override
    void addQuoteBlock(List<ParagraphContent> paragraphs) {
        for (ParagraphContent pc : paragraphs) {
            doc.addQuoteParagraph(parToString(pc))
        }
    }

 /*   @Override
    void addHorizontalRule() {
        throw new UnsupportedOperationException()
    }*/

    @Override
    void addHeading(String text, int level) {
        doc.addHeading(text, level)
    }
/*
    @Override
    void addList(ListContent content) {
        throw new UnsupportedOperationException()
    }*/
}
