package cz.zcu.kiv.md2odt.document.odt

import org.apache.xerces.dom.TextImpl
import org.odftoolkit.odfdom.dom.element.text.TextSpanElement
import org.odftoolkit.odfdom.pkg.OdfFileDom
import org.odftoolkit.odfdom.dom.attribute.fo.FoBackgroundColorAttribute
import org.odftoolkit.odfdom.dom.attribute.text.TextStyleNameAttribute
import org.odftoolkit.odfdom.dom.element.style.StyleParagraphPropertiesElement
import org.odftoolkit.simple.TextDocument
import org.odftoolkit.simple.common.navigation.ImageSelection
import org.odftoolkit.simple.common.navigation.TextNavigation
import org.odftoolkit.simple.common.navigation.TextSelection
import org.odftoolkit.simple.style.DefaultStyleHandler
import org.odftoolkit.simple.style.Font
import org.odftoolkit.simple.style.StyleTypeDefinitions
import org.odftoolkit.simple.style.StyleTypeDefinitions.FontStyle
import org.odftoolkit.simple.text.Span
import org.odftoolkit.simple.text.list.BulletDecorator
import org.odftoolkit.simple.text.list.List
import org.odftoolkit.simple.text.list.ListDecorator
import org.odftoolkit.simple.text.list.NumberDecorator
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import org.w3c.dom.NodeList

/**
 * Created by pepe on 13. 3. 2017.
 * cookbook:
 * http://incubator.apache.org/odftoolkit/simple/document/cookbook/index.html
 */
class OdfSimpleWrapper {
    private TextDocument odt

    OdfSimpleWrapper() {
        odt = TextDocument.newTextDocument()
        Node n = odt.getContentDom().getElementsByTagName("office:text").item(0)
        n.removeChild(n.lastChild)      //delete an empty paragraph
    }

    OdfSimpleWrapper(File file) {
        this.odt = TextDocument.loadDocument(file)
        fillDefaultStyles()
    }

    OdfSimpleWrapper(String documentPath) {
        this.odt = TextDocument.loadDocument(documentPath)
        fillDefaultStyles()
    }

    OdfSimpleWrapper(InputStream inputStream) {
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

    void addHeading(String text, int level) {
        def paragraph = odt.addParagraph(text)
        paragraph.applyHeading(true, level)

        TextStyleNameAttribute attr = new TextStyleNameAttribute((OdfFileDom) paragraph.odfElement.ownerDocument)
        paragraph.odfElement.setOdfAttribute(attr)
        attr.setValue("Heading_20_"+level)
    }

    void addParagraph(String text) {
        def paragraph = odt.addParagraph(text)

        TextStyleNameAttribute attr = new TextStyleNameAttribute((OdfFileDom) paragraph.odfElement.ownerDocument)
        paragraph.odfElement.setOdfAttribute(attr)
        attr.setValue("Text_20_body")
    }

    void addQuoteParagraph(String text) {
        def paragraph = odt.addParagraph(text)

        TextStyleNameAttribute attr = new TextStyleNameAttribute((OdfFileDom) paragraph.odfElement.ownerDocument)
        paragraph.odfElement.setOdfAttribute(attr)
        attr.setValue("Quotations")
    }

    void addCodeBlock(String code, String lang) {
        def paragraph = odt.addParagraph(code)

        TextStyleNameAttribute attr = new TextStyleNameAttribute((OdfFileDom) paragraph.odfElement.ownerDocument)
        paragraph.odfElement.setOdfAttribute(attr)
        attr.setValue("Preformatted_20_Text")
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

        while(nav.hasNext()) {
            sel = nav.nextSelection()
            String linkData = sel.getText();
            linkData = linkData.substring(mark.length(), linkData.length()-mark.length())

            String[] atr = linkData.split(OdfSimpleConstants.PARAM.getMark())   //url 0, text 1

            sel.replaceWith(atr[1])

            Span sp = Span.newSpan(sel)
            try {
                sp.applyHyperlink(new URI(OdfSimpleConstants.reEscape(atr[0])))
            } catch (Exception e) {
                sel.replaceWith(atr[1] + OdfSimpleConstants.escape(" (") + atr[0] + OdfSimpleConstants.escape(") "))
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

    void imageAll() {
        String mark = OdfSimpleConstants.IMAGE.getMark()
        TextNavigation nav = new TextNavigation(mark+"[^"+mark+"]*"+mark, odt)
        TextSelection sel = null

        while(nav.hasNext()) {
            sel = nav.nextSelection()
            String imageData = sel.getText()
            imageData = imageData.substring(mark.length(), imageData.length()-mark.length())

            String[] atr = imageData.split(OdfSimpleConstants.PARAM.getMark())  //url, alt, text

            def imSel = new ImageSelection(sel)
            def im = imSel.replaceWithImage(new URI(OdfSimpleConstants.reEscape(atr[0])))

            if(im == null) {
                def nl = sel.element.getElementsByTagName("draw:frame")
                def imNode = nl.item(nl.length-1)   //node of a last image which was not added successfully

                TextSpanElement textSpan = new TextSpanElement((OdfFileDom) sel.element.ownerDocument)
                textSpan.newTextNode(OdfSimpleConstants.escape(" Image (") + atr[0] + OdfSimpleConstants.escape(") "))
                sel.element.insertBefore(textSpan, imNode)
            }
            else {
                  /*  im.setDescription("desc")
                    im.setName("name")
                    im.setTitle("title")*/
            }
        }
    }

    void reEscapeAll() {
        Node root = odt.getContentDom().getElementsByTagName("office:text").item(0)
        Node n = null
        Queue<Node> q = new LinkedList<>()
        q.add(root)
        while(!q.isEmpty()) {
            n = q.poll()
            if(n.hasChildNodes()) {
                for(Node child : n.childNodes) {
                    q.add(child)
                }
            }else if(n instanceof TextImpl) {
                n.setTextContent(OdfSimpleConstants.reEscape(n.getTextContent()))
            }
        }
    }

    private ListDecorator switchDecorator(OdfListEnum e) {
        ListDecorator decorator = null

        switch (e) {
            case OdfListEnum.BULLET_LIST:
                decorator = new BulletDecorator(odt)
                break

            case OdfListEnum.NUMBERED_LIST:
                decorator = new NumberDecorator(odt)
                break
        }

        return decorator
    }

    List addList(String listHeading, OdfListEnum e) {
        ListDecorator decorator = switchDecorator(e)

        List newList = odt.addList(decorator)
        newList.setHeader(listHeading)

        return newList
    }

    List addSubList(List parentList, OdfListEnum e) {
        ListDecorator decorator = switchDecorator(e)

        return parentList.getItem(parentList.size() - 1).addList(decorator)
    }

    void addItemsToList(List list, java.util.List<Object> listItems) {

        for(Object item : listItems) {
            list.addItem((String)item)
        }
    }

    void addHorizontalRule() {
        def paragraph = odt.addParagraph("")

        TextStyleNameAttribute attr = new TextStyleNameAttribute((OdfFileDom) paragraph.odfElement.ownerDocument)
        paragraph.odfElement.setOdfAttribute(attr)
        attr.setValue("Horizontal_20_Line")
    }

    /** Returns the last operated Node. Used for testing.
     *
     * @return the last operated Node
     * */
    @Deprecated
    protected Node getLastNode() {
        return odt.getContentDom().getElementsByTagName("office:text").item(0).lastChild
    }

    void beforeSave() {
        linkAll()
        inlineCodeAll()
        italicAll()
        boldAll()
        imageAll()
        reEscapeAll()
    }

    void save(String documentPath) {
        beforeSave()
        odt.save(documentPath)
    }

    void save(OutputStream outputStream) {
        beforeSave()
        odt.save(outputStream)
    }
}
