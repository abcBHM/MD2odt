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

    protected void fillWithParagraphContent(OdfElement element, ParagraphContent paragraphContent) {
        for(SpanContent sc : paragraphContent.list) {
            switch (sc.getType()) {
                case SpanType.REGULAR:
                    Text textNode = odt.getContentDom().createTextNode(sc.getText())
                    element.appendChild(textNode)
                    break
                case SpanType.BOLD:
                    Span s = new Span(new TextSpanElement(odt.getContentDom()))
                    s.setTextContent(sc.getText())
                    s.getStyleHandler().getTextPropertiesForWrite().setFontStyle(StyleTypeDefinitions.FontStyle.BOLD)
                    element.appendChild(s.getOdfElement())
                    break
                case SpanType.ITALIC:
                    Span s = new Span(new TextSpanElement(odt.getContentDom()))
                    s.setTextContent(sc.getText())
                    s.getStyleHandler().getTextPropertiesForWrite().setFontStyle(StyleTypeDefinitions.FontStyle.ITALIC)
                    element.appendChild(s.getOdfElement())
                    break
                case SpanType.LINK:
                    if (sc instanceof SpanContentLink) {
                        TextAElement aElement = (TextAElement) odt.getContentDom().newOdfElement(TextAElement.class)
                        aElement.setXlinkTypeAttribute("simple")
                        aElement.setXlinkHrefAttribute(sc.getUrl())
                        aElement.setTextContent(sc.getText())
                        element.appendChild(aElement)
                    } else {
                        LOGGER.error("SpanContent with a '" + sc.getType() + "' type and instance of '" + sc.class + "'")
                    }
                    break
                case SpanType.CODE:
                    Span s = new Span(new TextSpanElement(odt.getContentDom()))
                    s.setTextContent(sc.getText())
                    s.getStyleHandler().getTextPropertiesForWrite().setFont(new Font("Courier New", StyleTypeDefinitions.FontStyle.REGULAR, 12))
                    element.appendChild(s.getOdfElement())
                    break
                case SpanType.IMAGE:
                    if (sc instanceof SpanContentImage) {
                        try {
                            DrawFrameElement frame = new DrawFrameElement(odt.getContentDom())
                            DrawImageElement e1 = frame.newDrawImageElement()

                            URI imageUri = new URI(sc.getUrl())
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
                        catch (Exception e) {
                            LOGGER.info("Exception while inserting image in OdfdomDocument: " + e.toString())
                            Text textNode = odt.getContentDom().createTextNode("Image (" + sc.getUrl() + ")")
                            element.appendChild(textNode)
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

    @Override
    void addHeading(String text, int level) {
        def paragraph = odt.addParagraph(text)
        paragraph.applyHeading(true, level)

        TextStyleNameAttribute attr = new TextStyleNameAttribute(odt.getContentDom())
        paragraph.odfElement.setOdfAttribute(attr)
        attr.setValue(StyleNames.HEADING.getLevel(level))
    }

    @Override
    void addParagraph(ParagraphContent content) {
        Paragraph paragraph = odt.addParagraph("")
        fillWithParagraphContent(paragraph.getOdfElement(), content)

        TextStyleNameAttribute attr = new TextStyleNameAttribute(odt.getContentDom())
        paragraph.odfElement.setOdfAttribute(attr)
        attr.setValue(StyleNames.BODY_TEXT.getValue())
    }

    @Override
    void addCodeBlock(String code) {
        addCodeBlock(code, null)
    }

    @Override
    void addCodeBlock(String code, String lang) {
        def paragraph = odt.addParagraph(code)

        TextStyleNameAttribute attr = new TextStyleNameAttribute(odt.getContentDom())
        paragraph.odfElement.setOdfAttribute(attr)
        attr.setValue(StyleNames.CODE.getValue())
    }

    @Override
    void addQuoteBlock(List<ParagraphContent> paragraphs) {
        for (ParagraphContent pc : paragraphs) {
            def paragraph = odt.addParagraph("")
            fillWithParagraphContent(paragraph.getOdfElement(), pc)

            TextStyleNameAttribute attr = new TextStyleNameAttribute(odt.getContentDom())
            paragraph.odfElement.setOdfAttribute(attr)
            attr.setValue(StyleNames.QUOTE.getValue())
        }
    }

    @Override
    void addHorizontalRule() {
        def paragraph = odt.addParagraph("")

        TextStyleNameAttribute attr = new TextStyleNameAttribute(odt.getContentDom())
        paragraph.odfElement.setOdfAttribute(attr)
        attr.setValue(StyleNames.HORIZONTAL_RULE.getValue())
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
