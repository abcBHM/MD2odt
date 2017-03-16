package cz.zcu.kiv.md2odt.document.odt

import org.odftoolkit.odfdom.type.Color
import org.odftoolkit.simple.Document
import org.odftoolkit.simple.TextDocument
import org.odftoolkit.simple.common.navigation.TextNavigation
import org.odftoolkit.simple.common.navigation.TextSelection
import org.odftoolkit.simple.style.Border
import org.odftoolkit.simple.style.DefaultStyleHandler
import org.odftoolkit.simple.style.Font
import org.odftoolkit.simple.style.StyleTypeDefinitions
import org.odftoolkit.simple.style.StyleTypeDefinitions.FontStyle
import org.odftoolkit.simple.text.Span
import org.odftoolkit.simple.text.list.BulletDecorator
import org.odftoolkit.simple.text.list.List
import org.odftoolkit.simple.text.list.ListDecorator
import org.odftoolkit.simple.text.list.NumberDecorator
import org.w3c.dom.Node

/**
 * Created by pepe on 13. 3. 2017.
 * cookbook:
 * http://incubator.apache.org/odftoolkit/simple/document/cookbook/index.html
 */
class OdfSimpleWrapper {
    private TextDocument odt
    private Node lastNode

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

    void italicAll() {
        style(OdfSimpleConstants.ITALIC.getMark(), StyleTypeDefinitions.FontStyle.ITALIC)
    }

    void boldAll() {
        style(OdfSimpleConstants.BOLD.getMark(), StyleTypeDefinitions.FontStyle.BOLD)
    }

    private void style(String mark, FontStyle style) {
        TextNavigation nav = new TextNavigation(mark+"[^"+mark+"]*"+mark, odt)
        TextSelection sel = null

        while(nav.hasNext()) {
            sel = nav.nextSelection()
            sel.replaceWith(sel.getText().substring(mark.length(),sel.getText().length()-mark.length()))
            Span sp = Span.newSpan(sel)
            DefaultStyleHandler dsh = sp.getStyleHandler()
            dsh.getTextPropertiesForWrite().setFontStyle(style)
        }
    }

    void linkAll() {
        String mark = OdfSimpleConstants.LINK.getMark()
        TextNavigation nav = new TextNavigation(mark+"[^"+mark+"]*"+mark, odt)
        TextSelection sel = null
        String hrMark = OdfSimpleConstants.LINK_HREF.getMark()

        while(nav.hasNext()) {
            sel = nav.nextSelection()
            String link = sel.getText();
            link = link.substring(mark.length()+hrMark.length(), link.lastIndexOf(hrMark))
            sel.replaceWith(sel.getText().substring(mark.length() + 2*hrMark.length() + link.length(),sel.getText().length()-mark.length()))

            Span sp = Span.newSpan(sel)
            try {
                sp.applyHyperlink(new URI(OdfSimpleConstants.reEscape(link)))
            } catch (Exception e) {
                sel.replaceWith(sel.getText() + OdfSimpleConstants.escape(" (") + link + OdfSimpleConstants.escape(") "))
            }

        }
    }

    void inlineCodeAll() {
        String mark = OdfSimpleConstants.INLINE_CODE.getMark()
        TextNavigation nav = new TextNavigation(mark+"[^"+mark+"]*"+mark, odt)
        TextSelection sel = null

        while(nav.hasNext()) {
            sel = nav.nextSelection()
            sel.replaceWith(sel.getText().substring(mark.length(),sel.getText().length()-mark.length()))
            Span sp = Span.newSpan(sel)
            //println(sp.getTextContent())
            DefaultStyleHandler dsh = sp.getStyleHandler()
           // println(dsh.getStyleElementForWrite().toString())
            //dsh.getStyleElementForWrite().setStyleParentStyleNameAttribute("Text_20_body")
           // println(dsh.getStyleElementForWrite().toString())
            dsh.getTextPropertiesForWrite().setFont(new Font("Courier New", FontStyle.REGULAR, 12))
            //dsh.getTextPropertiesForWrite().setFontName("Courier New")
        }
    }

    void reEscapeAll() {
        for (OdfSimpleConstants osc : OdfSimpleConstants.values()) {
            reEscape(osc.getEscape(), osc.getMark())
        }
        reEscape(OdfSimpleConstants.AMP_ESCAPE, OdfSimpleConstants.AMP_MARK)
    }

    private void reEscape(String what, String with) {
        TextNavigation nav = new TextNavigation(what, odt)
        TextSelection sel = null
        while(nav.hasNext()) {
            sel = nav.nextSelection()
            sel.replaceWith(with)
        }
    }

    private void addList(String listHeading, java.util.List<String> listItems, ListDecorator decorator) {
        List newList = odt.addList(decorator)
        newList.setHeader(listHeading)

        for(String item : listItems) {
            newList.addItem(item)
        }
    }

    void addBulletList(String listHeading, java.util.List<String> listItems) {
        addList(listHeading, listItems, new BulletDecorator(odt))
    }

    void addNumberList(String listHeading, java.util.List<String> listItems) {
        addList(listHeading, listItems, new NumberDecorator(odt))
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
        linkAll()
        inlineCodeAll()
        italicAll()
        boldAll()
        reEscapeAll()
        odt.save(documentPath)
    }
}
