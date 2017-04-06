package cz.zcu.kiv.md2odt.document.odfdom

import cz.zcu.kiv.md2odt.document.*
import org.apache.log4j.Logger
import org.odftoolkit.odfdom.dom.OdfSchemaDocument
import org.odftoolkit.odfdom.dom.attribute.text.TextStyleNameAttribute
import org.odftoolkit.odfdom.dom.element.draw.DrawFrameElement
import org.odftoolkit.odfdom.dom.element.draw.DrawImageElement
import org.odftoolkit.odfdom.dom.element.text.TextAElement
import org.odftoolkit.odfdom.dom.element.text.TextListItemElement
import org.odftoolkit.odfdom.dom.element.text.TextPElement
import org.odftoolkit.odfdom.dom.element.text.TextSpanElement
import org.odftoolkit.odfdom.pkg.OdfElement
import org.odftoolkit.odfdom.pkg.manifest.OdfFileEntry
import org.odftoolkit.simple.TextDocument
import org.odftoolkit.simple.draw.Image
import org.odftoolkit.simple.style.Font
import org.odftoolkit.simple.style.StyleTypeDefinitions
import org.odftoolkit.simple.style.StyleTypeDefinitions.FontStyle
import org.odftoolkit.simple.text.Paragraph
import org.odftoolkit.simple.text.Span
import org.odftoolkit.simple.text.list.BulletDecorator
import org.odftoolkit.simple.text.list.List as OdfList
import org.odftoolkit.simple.text.list.ListDecorator
import org.odftoolkit.simple.text.list.NumberDecorator
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.w3c.dom.Text

/**
 * Created by pepe on 5. 4. 2017.
 */
class OdfdomDocument implements DocumentAdapter{

    protected final TextDocument odt
    private static final Logger LOGGER = Logger.getLogger(OdfdomDocument)

    OdfdomDocument() {
        odt = TextDocument.newTextDocument()
        Node n = odt.getContentDom().getElementsByTagName("office:text").item(0)
        n.removeChild(n.lastChild)      //delete an empty paragraph
    }

    OdfdomDocument(File file) {
        this.odt = TextDocument.loadDocument(file)
        fillDefaultStyles()
    }

    OdfdomDocument(String documentPath) {
        this.odt = TextDocument.loadDocument(documentPath)
        fillDefaultStyles()
    }

    OdfdomDocument(InputStream inputStream) {
        this.odt = TextDocument.loadDocument(inputStream)
        fillDefaultStyles()
    }

    protected Set<String> getStyleNames(TextDocument td) {
        Set<String> styleNames = new TreeSet<>()
        NodeList nl = td.getStylesDom().getOfficeStyles().getElementsByTagName("style:style")
        for (int i = 0; i < nl.length; i++) {
            Node n = nl.item(i).getAttributes().getNamedItem("style:name")
            if(n != null)
                styleNames.add(n.getNodeValue())
        }
        return styleNames
    }

    protected void fillDefaultStyles() {
        Set<String> odtStyleNames = getStyleNames(odt)

        TextDocument defaultTextDocument = TextDocument.newTextDocument()

        NodeList nl = defaultTextDocument.getStylesDom().getOfficeStyles().getElementsByTagName("style:style")
        for (int i = 0; i < nl.length; i++) {
            Node node = nl.item(i)
            String styleName = node.getAttributes().getNamedItem("style:name").getNodeValue()

            if(!odtStyleNames.contains(styleName)) {
                Node n = odt.getStylesDom().importNode(node, true)
                odt.getStylesDom().getOfficeStyles().appendChild(n)
            }
        }
    }

    protected void appendText(OdfElement element, String text) {
        Text textNode = odt.getContentDom().createTextNode(text)
        element.appendChild(textNode)
    }

    protected void appendBoldText(OdfElement element, String text) {
        appendStyleText(element, text, StyleTypeDefinitions.FontStyle.BOLD)
    }

    protected void appendItalicText(OdfElement element, String text) {
        appendStyleText(element, text, StyleTypeDefinitions.FontStyle.ITALIC)
    }

    private void appendStyleText(OdfElement element, String text, FontStyle style) {
        Span s = new Span(new TextSpanElement(odt.getContentDom()))
        s.setTextContent(text)
        s.getStyleHandler().getTextPropertiesForWrite().setFontStyle(style)
        element.appendChild(s.getOdfElement())
    }

    protected void appendLink(OdfElement element, String text, String url) {
        TextAElement aElement = (TextAElement) odt.getContentDom().newOdfElement(TextAElement.class)
        aElement.setXlinkTypeAttribute("simple")
        aElement.setXlinkHrefAttribute(url)
        aElement.setTextContent(text)
        element.appendChild(aElement)
    }

    protected void appendCode(OdfElement element, String text) {
        Span s = new Span(new TextSpanElement(odt.getContentDom()))
        s.setTextContent(text)
        s.getStyleHandler().getTextPropertiesForWrite().setFont(new Font("Courier New", StyleTypeDefinitions.FontStyle.REGULAR, 12))
        element.appendChild(s.getOdfElement())
    }

    protected void appendImage(OdfElement element, String url) {
        DrawFrameElement frame = new DrawFrameElement(odt.getContentDom())
        DrawImageElement e1 = frame.newDrawImageElement()

        URI imageUri = new URI(url)
        String imageRef1 = imageUri.toString()
        String mediaType1 = OdfFileEntry.getMediaTypeString(imageRef1)
        OdfSchemaDocument mOdfSchemaDoc1 = (OdfSchemaDocument) odt.getContentDom().getDocument()
        String packagePath = Image.getPackagePath(mOdfSchemaDoc1, imageRef1)
        mOdfSchemaDoc1.getPackage().insert(imageUri, packagePath, mediaType1)
        packagePath = packagePath.replaceFirst(odt.getContentDom().getDocument().getDocumentPath(), "")
        Image.configureInsertedImage((OdfSchemaDocument) odt.getContentDom().getDocument(), e1, packagePath, false)
        Image mImage = Image.getInstanceof(e1)
        mImage.getStyleHandler().setAchorType(StyleTypeDefinitions.AnchorType.AS_CHARACTER)

        element.appendChild(frame)
    }

    protected void fillWithParagraphContent(OdfElement element, ParagraphContent paragraphContent) {
        for(SpanContent sc : paragraphContent.list) {
            switch (sc.getType()) {
                case SpanType.REGULAR:
                    appendText(element, sc.getText())
                    break
                case SpanType.BOLD:
                    appendBoldText(element, sc.getText())
                    break
                case SpanType.ITALIC:
                    appendItalicText(element, sc.getText())
                    break
                case SpanType.LINK:
                    if (sc instanceof SpanContentLink) {
                        appendLink(element, sc.getText(), sc.getUrl())
                    } else {
                        LOGGER.error("SpanContent with a '" + sc.getType() + "' type and instance of '" + sc.class + "'")
                    }
                    break
                case SpanType.CODE:
                    appendCode(element, sc.getText())
                    break
                case SpanType.IMAGE:
                    if (sc instanceof SpanContentImage) {
                        try {
                            appendImage(element, sc.getUrl())
                        }
                        catch (Exception e) {
                            LOGGER.info("Exception while inserting image in OdfdomDocument: " + e.toString())
                            appendText(element, "Image (" + sc.getUrl() + ")")
                        }
                    } else {
                        LOGGER.error("SpanContent with a '" + sc.getType() + "' type and instance of '" + sc.class + "'")
                    }
                    break
                default:
                    LOGGER.warn("SpanType not implemented: " + sc.getType() + " in class " + sc.class)
            }
        }
    }

    protected Paragraph addParagraph(String styleName) {
        return addParagraph("", styleName)
    }

    protected Paragraph addParagraph(String text, String styleName) {
        Paragraph paragraph = odt.addParagraph(text)
        TextStyleNameAttribute attr = new TextStyleNameAttribute(odt.getContentDom())
        paragraph.odfElement.setOdfAttribute(attr)
        attr.setValue(styleName)
        return paragraph
    }

    @Override
    void addHeading(String text, int level) {
        def paragraph = addParagraph(text, StyleNames.HEADING.getLevel(level))
        paragraph.applyHeading(true, level)
    }

    @Override
    void addParagraph(ParagraphContent content) {
        Paragraph paragraph = addParagraph(StyleNames.BODY_TEXT.getValue())
        fillWithParagraphContent(paragraph.getOdfElement(), content)
    }

    @Override
    void addCodeBlock(String code) {
        addCodeBlock(code, null)
    }

    @Override
    void addCodeBlock(String code, String lang) {
        addParagraph(code, StyleNames.CODE.getValue())
    }

    @Override
    void addQuoteBlock(List<ParagraphContent> paragraphs) {
        for (ParagraphContent pc : paragraphs) {
            def paragraph = addParagraph(StyleNames.QUOTE.getValue())
            fillWithParagraphContent(paragraph.getOdfElement(), pc)
        }
    }

    @Override
    void addHorizontalRule() {
        addParagraph(StyleNames.HORIZONTAL_RULE.getValue())
    }

    private ListDecorator switchDecorator(ListType e) {
        ListDecorator decorator = null
        switch (e) {
            case ListType.BULLET:
                decorator = new BulletDecorator(odt)
                break
            case ListType.ORDERED:
                decorator = new NumberDecorator(odt)
                break
        }
        return decorator
    }

    @Override
    void addList(ListContent content) {
        OdfList list = odt.addList(switchDecorator(content.getType()))
        //newList.setHeader(listHeading)
        addListRec(content, list)
    }

    private void addListRec(ListContent content, OdfList list) {
        List<List<BlockContent>> listListBlockContent = content.getListItems()

        for (List<BlockContent> listBlock : listListBlockContent) {
            TextListItemElement textItem = null

            for (BlockContent blockContent : listBlock) {
                if(blockContent instanceof ParagraphContent) {
                    if(textItem == null) {
                        textItem = list.getOdfElement().newTextListItemElement()
                    }
                    TextPElement par = textItem.newTextPElement()
                    TextStyleNameAttribute attr = new TextStyleNameAttribute(odt.getContentDom())
                    par.setOdfAttribute(attr)
                    attr.setValue(StyleNames.LIST.getValue())
                    fillWithParagraphContent(par, blockContent)

                }
                else if(blockContent instanceof ListContent) {
                    OdfList newList = addSubList(list, blockContent.getType())
                    addListRec(blockContent, newList)
                }
            }
        }
    }

    OdfList addSubList(OdfList parentList, ListType e) {
        ListDecorator decorator = switchDecorator(e)
        return parentList.getItem(parentList.size() - 1).addList(decorator)
    }

    @Override
    void save(String documentPath) {
        odt.save(documentPath)
    }

    @Override
    void save(OutputStream outputStream) {
        odt.save(outputStream)
    }
}
