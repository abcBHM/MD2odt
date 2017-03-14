package cz.zcu.kiv.md2odt.document.odt

import org.odftoolkit.simple.TextDocument
import org.odftoolkit.simple.common.navigation.TextNavigation
import org.odftoolkit.simple.common.navigation.TextSelection
import org.odftoolkit.simple.style.DefaultStyleHandler
import org.odftoolkit.simple.style.StyleTypeDefinitions
import org.odftoolkit.simple.text.Span
import org.w3c.dom.Node

/**
 * Created by pepe on 13. 3. 2017.
 * cookbook:
 * http://incubator.apache.org/odftoolkit/simple/document/cookbook/index.html
 */
class OdfSimpleWrapper {
    TextDocument odt
    Node lastNode

    OdfSimpleWrapper() {
        odt = TextDocument.newTextDocument()
        Node n = odt.getContentDom().getElementsByTagName("office:text").item(0)
        n.removeChild(n.lastChild)      //delete an empty paragraph
    }

    OdfSimpleWrapper(File file) {
        this.odt = TextDocument.loadDocument(file)
    }

    OdfSimpleWrapper(String documentPath) {
        this.odt = TextDocument.loadDocument(documentPath)
    }

    void addHeading(String text, int level) {
        if (level < 1 || level > 10)
            throw new IllegalArgumentException("Heading level '"+level+"' is not supported.")

        def paragraph = odt.addParagraph(text)
        paragraph.applyHeading(true, level)

        lastNode = odt.getContentDom().getElementsByTagName("office:text").item(0).lastChild
        lastNode.getAttributes().getNamedItem("text:style-name").setNodeValue("Heading_20_"+level)       //set the style (1-10)
    }

    void addParagraph(String text) {
        def paragraph = odt.addParagraph(text)
        paragraph.setStyleName("Text_20_body")
        lastNode = odt.getContentDom().getElementsByTagName("office:text").item(0).lastChild
        lastNode.getAttributes().getNamedItem("text:style-name").setNodeValue("Text_20_body")
    }

    void emphasiseAll() {
        String mark = OdfSimpleConstants.ITALIC.getMark()
        TextNavigation nav = new TextNavigation(mark+"[^"+mark+"]*"+mark, odt)
        TextSelection sel = null

        while(nav.hasNext()) {
            sel = nav.nextSelection()
            sel.replaceWith(sel.getText().substring(mark.length(),sel.getText().length()-mark.length()))
            Span sp = Span.newSpan(sel)
            DefaultStyleHandler dsh = sp.getStyleHandler()
            dsh.getTextPropertiesForWrite().setFontStyle(StyleTypeDefinitions.FontStyle.ITALIC)
        }
    }

    void reEscapeAll() {
        for (OdfSimpleConstants osc : OdfSimpleConstants.values()) {
            reEscape(osc.getEscape(), osc.getMark())
        }
        reEscape(OdfSimpleConstants.AMP_ESCAPE, OdfSimpleConstants.AMP_MARK)
    }

    void reEscape(String what, String with) {
        TextNavigation nav = new TextNavigation(what, odt)
        TextSelection sel = null
        while(nav.hasNext()) {
            sel = nav.nextSelection()
            sel.replaceWith(with)
        }
    }

    static String escape(String toEscape) {
        String rtn = toEscape.replaceAll(OdfSimpleConstants.AMP_MARK,OdfSimpleConstants.AMP_ESCAPE)   //& has to be replaced first
        for (OdfSimpleConstants osc : OdfSimpleConstants.values()) {
            rtn = rtn.replaceAll(osc.getMark(), osc.getEscape())
        }
        return rtn
    }

    /** Returns the last operated Node. Used for testing.
     *
     * @return the last operated Node
     * */
    @Deprecated
    Node getLastNode() {
        return lastNode
    }

    void save(String documentPath) {
        odt.save(documentPath)
    }
    void save(File file) {
        odt.save(file)
    }
}
