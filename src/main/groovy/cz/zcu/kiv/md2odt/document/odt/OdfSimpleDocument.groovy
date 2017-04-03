package cz.zcu.kiv.md2odt.document.odt

import cz.zcu.kiv.md2odt.document.BlockContent
import cz.zcu.kiv.md2odt.document.DocumentAdapter
import cz.zcu.kiv.md2odt.document.ListContent
import cz.zcu.kiv.md2odt.document.ListType
import cz.zcu.kiv.md2odt.document.ParagraphContent
import cz.zcu.kiv.md2odt.document.SpanContent
import cz.zcu.kiv.md2odt.document.SpanContentImage
import cz.zcu.kiv.md2odt.document.SpanContentLink
import cz.zcu.kiv.md2odt.document.SpanType
import org.odftoolkit.simple.text.list.List as OdfList
import org.odftoolkit.simple.text.list.ListItem

/**
 * Created by pepe on 15. 3. 2017.
 */
class OdfSimpleDocument implements DocumentAdapter {
    private OdfSimpleWrapper doc

    OdfSimpleDocument() {
        doc = new OdfSimpleWrapper()
    }

    OdfSimpleDocument(File file) {
        doc = new OdfSimpleWrapper(file)
    }

    OdfSimpleDocument(String documentPath) {
        doc = new OdfSimpleWrapper(documentPath)
    }

    OdfSimpleDocument(InputStream inputStream) {
        doc = new OdfSimpleWrapper(inputStream)
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

    @Override
    void save(String documentPath) {
        doc.save(documentPath)
    }

    @Override
    void save(OutputStream outputStream) {
        doc.save(outputStream)
    }

    @Override
    void addParagraph(ParagraphContent content) {
        doc.addParagraph(parToString(content))
    }

    @Override
    void addCodeBlock(String code) {
        addCodeBlock(code, null)
    }

    @Override
    void addCodeBlock(String code, String lang) {
        doc.addCodeBlock(OdfSimpleConstants.escape(code), lang)
    }

    @Override
    void addQuoteBlock(List<ParagraphContent> paragraphs) {
        for (ParagraphContent pc : paragraphs) {
            doc.addQuoteParagraph(parToString(pc))
        }
    }

    @Override
    void addHorizontalRule() {
        doc.addHorizontalRule()
    }

    @Override
    void addHeading(String text, int level) {
        doc.addHeading(OdfSimpleConstants.escape(text), level)
    }

    @Override
    void addList(ListContent content) {
        OdfList list = doc.addList("", switchListTypes(content.getType()))

        addListRec(content, list)
    }

    private void addListRec(ListContent content, OdfList list) {
        List<List<BlockContent>> listListBlockContent = content.getListItems()

        for (List<BlockContent> listBlock : listListBlockContent) {
            def itemInList = null

            for (BlockContent blockContent : listBlock) {
                if(blockContent instanceof ParagraphContent) {
                    if(itemInList == null) {
                        list.addItem("")
                        itemInList = list.getItem(list.size() - 1)
                    }
                    itemInList.textContent += parToString(blockContent) + "\r\n"
                }

                else if(blockContent instanceof ListContent) {
                    OdfList newList = doc.addSubList(list, switchListTypes(blockContent.getType()))
                    addListRec(blockContent, newList)
                }
            }
        }
    }

    private static OdfListEnum switchListTypes(ListType listType) {
        switch (listType) {
            case ListType.BULLET:
                return OdfListEnum.BULLET_LIST
                break
            case ListType.ORDERED:
                return OdfListEnum.NUMBERED_LIST
                break
            default:
                return OdfListEnum.BULLET_LIST
                break
        }
    }
}
