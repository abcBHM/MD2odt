package cz.zcu.kiv.md2odt.document.odfdom

import org.apache.log4j.Logger
import org.odftoolkit.odfdom.dom.OdfStylesDom
import org.odftoolkit.odfdom.dom.element.style.StyleStyleElement
import org.odftoolkit.odfdom.dom.element.style.StyleTextPropertiesElement
import org.odftoolkit.odfdom.dom.element.text.TextSpanElement
import org.odftoolkit.simple.TextDocument
import org.odftoolkit.simple.style.Font
import org.odftoolkit.simple.style.StyleTypeDefinitions
import org.odftoolkit.simple.text.Span
import org.w3c.dom.Node
import org.w3c.dom.NodeList

/**
 * Handling the creation of a TextDocument and retrieving informations from TextDocument.
 */
class TextDocumentHandler {
    private static final Logger LOGGER = Logger.getLogger(TextDocumentHandler)

    /** Returns a background color of a style name used for code block or WHITE if not found.
     *
     * @param document TextDocument where the color is searched.
     * @return Value of fo:background-color in StyleNames.CODE.getValue() style name as java.awt.Color
     * */
    static java.awt.Color getCodeBlockBackgroundColor(TextDocument document) {
        try {
            NodeList nl = document.stylesDom.officeStyles.getElementsByTagName("style:style")
            for (int i = 0; i < nl.length; i++) {
                Node n = nl.item(i).getAttributes().getNamedItem("style:name")
                if (n != null && n.textContent.equals(StyleNames.CODE.getValue())) {
                    //style:paragraph-properties fo:background-color="#888a85"
                    for (Node spp : nl.item(i).childNodes) {
                        if (spp != null && spp.nodeName.equals("style:paragraph-properties")) {
                            Node fbc = spp.attributes.getNamedItem("fo:background-color")
                            if (fbc != null) {
                                String s = fbc.textContent.replaceAll("[^a-fA-F0-9]", "")
                                int rgb = Integer.parseInt(s, 16)
                                return new java.awt.Color(rgb)
                            }
                            return java.awt.Color.WHITE
                        }
                    }
                    return java.awt.Color.WHITE
                }
            }
            return java.awt.Color.WHITE
        }
        catch (Exception e) {
            LOGGER.error("getCodeBlockBackgroundColor failed: "+e)
            return java.awt.Color.WHITE
        }
    }

    /** Returns a TextDocumentHandler to handle creation of a new TextDocument.
     *
     * @return TextDocumentHandler to handle creation of a new TextDocument.
     * */
    static TextDocumentHandler handler() {
        return new TextDocumentHandler();
    }

    private TextDocument odt

    private TextDocumentHandler() {
    }

    /** Returns an empty TextDocument, which has support for styles.
     *
     * @return Returns an empty TextDocument.
     * */
    TextDocument newTextDocument() {
        odt = TextDocument.newTextDocument()
        odt.getParagraphByIndex(0,false) .remove()     //removes an empty paragraph
        prepareConstructor()
        return odt
    }

    /** Returns a TextDocument, which is filled with template styles and has support for other non-set styles.
     *
     * @param file Template file.
     * @return Returns a TextDocument from template.
     * */
    TextDocument newTextDocumentFromTemplate(File file) {
        odt = TextDocument.loadDocument(file)
        fillDefaultStyles()
        prepareConstructor()
        return odt
    }

    /** Returns a TextDocument, which is filled with template styles and has support for other non-set styles.
     *
     * @param documentPath Template path.
     * @return Returns a TextDocument from template.
     * */
    TextDocument newTextDocumentFromTemplate(String documentPath) {
        odt = TextDocument.loadDocument(documentPath)
        fillDefaultStyles()
        prepareConstructor()
        return odt
    }

    /** Returns a TextDocument, which is filled with template styles and has support for other non-set styles.
     *
     * @param inputStream Template stream.
     * @return Returns a TextDocument from template.
     * */
    TextDocument newTextDocumentFromTemplate(InputStream inputStream) {
        odt = TextDocument.loadDocument(inputStream)
        fillDefaultStyles()
        prepareConstructor()
        return odt
    }



    private void prepareConstructor() {
        addTextStyles()
    }

    private void addTextStyles() {
        addStrikeStyle()
        addSubScriptStyle()
        addSuperScriptStyle()
        addInlineCodeFont()
    }

    private StyleTextPropertiesElement addStyleStyleElementForSpan(String styleName) {
        def oas = odt.contentDom.getOrCreateAutomaticStyles()
        StyleStyleElement sse = new StyleStyleElement(odt.getContentDom())
        oas.appendChild(sse)
        sse.setStyleNameAttribute(styleName)
        sse.setStyleFamilyAttribute("text")

        StyleTextPropertiesElement stpe = new StyleTextPropertiesElement(odt.getContentDom())
        sse.appendChild(stpe)
        return stpe
    }

    private void addStrikeStyle() {
        def stpe = addStyleStyleElementForSpan(StyleNames.STRIKE.getValue())
        stpe.setStyleTextLineThroughTypeAttribute("single")
        stpe.setStyleTextLineThroughStyleAttribute("solid")
    }

    private void addSubScriptStyle() {
        def stpe = addStyleStyleElementForSpan(StyleNames.SUB_SCRIPT.getValue())
        stpe.setStyleTextPositionAttribute("sub 58%")
    }

    private void addSuperScriptStyle() {
        def stpe = addStyleStyleElementForSpan(StyleNames.SUPER_SCRIPT.getValue())
        stpe.setStyleTextPositionAttribute("super 58%")
    }

    private void addInlineCodeFont() {
        Span s = new Span(new TextSpanElement(odt.getContentDom()))
        s.getStyleHandler().getTextPropertiesForWrite().setFont(new Font("Courier New", StyleTypeDefinitions.FontStyle.REGULAR, 12))
        // span is not added to document it only fills the style and set FONT NAME
    }

    private void fillDefaultStyles() {
        Set<String> odtStyleNames = getStylesStyleNames(odt.getStylesDom())

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

    private Set<String> getNamedItemValues(NodeList nl, String name) {
        Set<String> set = new TreeSet<>()

        for (int i = 0; i < nl.length; i++) {
            Node n = nl.item(i).getAttributes().getNamedItem(name)
            if(n != null)
                set.add(n.getNodeValue())
        }
        return set
    }

    private Set<String> getStylesStyleNames(OdfStylesDom stylesDom) {
        NodeList nl = stylesDom.getOfficeStyles().getElementsByTagName("style:style")
        return getNamedItemValues(nl, "style:name")
    }
}
