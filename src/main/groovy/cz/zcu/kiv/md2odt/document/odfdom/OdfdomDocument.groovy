package cz.zcu.kiv.md2odt.document.odfdom

import cz.zcu.kiv.md2odt.document.Document
import cz.zcu.kiv.md2odt.document.DocumentAdapter
import cz.zcu.kiv.md2odt.document.ListContent
import cz.zcu.kiv.md2odt.document.ParagraphContent
import cz.zcu.kiv.md2odt.document.odt.StyleNames
import org.apache.log4j.Logger
import org.odftoolkit.odfdom.dom.attribute.text.TextStyleNameAttribute
import org.odftoolkit.odfdom.pkg.OdfFileDom
import org.odftoolkit.simple.TextDocument
import org.w3c.dom.Node
import org.w3c.dom.NodeList

/**
 * Created by pepe on 5. 4. 2017.
 */
class OdfdomDocument implements DocumentAdapter{

    private TextDocument odt
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

    @Override
    void addHeading(String text, int level) {
        def paragraph = odt.addParagraph(text)
        paragraph.applyHeading(true, level)

        TextStyleNameAttribute attr = new TextStyleNameAttribute((OdfFileDom) paragraph.odfElement.ownerDocument)
        paragraph.odfElement.setOdfAttribute(attr)
        attr.setValue(StyleNames.HEADING.getLevel(level))
    }

    @Override
    void addParagraph(ParagraphContent content) {
       /* def paragraph = odt.addParagraph(text)

        TextStyleNameAttribute attr = new TextStyleNameAttribute((OdfFileDom) paragraph.odfElement.ownerDocument)
        paragraph.odfElement.setOdfAttribute(attr)
        attr.setValue(StyleNames.BODY_TEXT.getValue())*/
        LOGGER.warn("addParagraph not implemented in class " + this.class)
    }

    @Override
    void addList(ListContent content) {
        LOGGER.warn("addList not implemented in class " + this.class)
    }

    @Override
    void addCodeBlock(String code) {
        addCodeBlock(code, null)
    }

    @Override
    void addCodeBlock(String code, String lang) {
        def paragraph = odt.addParagraph(code)

        TextStyleNameAttribute attr = new TextStyleNameAttribute((OdfFileDom) paragraph.odfElement.ownerDocument)
        paragraph.odfElement.setOdfAttribute(attr)
        attr.setValue(StyleNames.CODE.getValue())
    }

    @Override
    void addQuoteBlock(List<ParagraphContent> paragraphs) {
        /*def paragraph = odt.addParagraph(text)

        TextStyleNameAttribute attr = new TextStyleNameAttribute((OdfFileDom) paragraph.odfElement.ownerDocument)
        paragraph.odfElement.setOdfAttribute(attr)
        attr.setValue(StyleNames.QUOTE.getValue())*/
        LOGGER.warn("addQuoteBlock not implemented in class " + this.class)
    }

    @Override
    void addHorizontalRule() {
        def paragraph = odt.addParagraph("")

        TextStyleNameAttribute attr = new TextStyleNameAttribute((OdfFileDom) paragraph.odfElement.ownerDocument)
        paragraph.odfElement.setOdfAttribute(attr)
        attr.setValue(StyleNames.HORIZONTAL_RULE.getValue())
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
