package cz.zcu.kiv.md2odt.highlight

import org.junit.Ignore
import org.junit.Test
import org.python.core.PyList
import org.python.core.PyTuple
import org.python.core.PyTupleDerived
import org.python.util.PythonInterpreter

/**
 * Created by n5ver on 06.04.2017.
 */
class CodeParserTest {

    @Ignore
    @Test
    void trySmth() throws Exception {
        String code = "package cz.zcu.kiv.md2odt.document.odt\n" +
                "\n" +
                "import org.apache.xerces.dom.TextImpl\n" +
                "import org.odftoolkit.odfdom.dom.element.text.TextSpanElement\n" +
                "import org.odftoolkit.odfdom.pkg.OdfFileDom\n" +
                "import org.odftoolkit.odfdom.dom.attribute.fo.FoBackgroundColorAttribute\n" +
                "import org.odftoolkit.odfdom.dom.attribute.text.TextStyleNameAttribute\n" +
                "import org.odftoolkit.odfdom.dom.element.style.StyleParagraphPropertiesElement\n" +
                "import org.odftoolkit.simple.TextDocument\n" +
                "import org.odftoolkit.simple.common.navigation.ImageSelection\n" +
                "import org.odftoolkit.simple.common.navigation.TextNavigation\n" +
                "import org.odftoolkit.simple.common.navigation.TextSelection\n" +
                "import org.odftoolkit.simple.style.DefaultStyleHandler\n" +
                "import org.odftoolkit.simple.style.Font\n" +
                "import org.odftoolkit.simple.style.StyleTypeDefinitions\n" +
                "import org.odftoolkit.simple.style.StyleTypeDefinitions.FontStyle\n" +
                "import org.odftoolkit.simple.text.Span\n" +
                "import org.odftoolkit.simple.text.list.BulletDecorator\n" +
                "import org.odftoolkit.simple.text.list.List\n" +
                "import org.odftoolkit.simple.text.list.ListDecorator\n" +
                "import org.odftoolkit.simple.text.list.NumberDecorator\n" +
                "import org.w3c.dom.NamedNodeMap\n" +
                "import org.w3c.dom.Node\n" +
                "import org.w3c.dom.NodeList\n" +
                "import cz.zcu.kiv.md2odt.document.odfdom.StyleNames\n" +
                "\n" +
                "/**\n" +
                " * Created by pepe on 13. 3. 2017.\n" +
                " * cookbook:\n" +
                " * http://incubator.apache.org/odftoolkit/simple/document/cookbook/index.html\n" +
                " */\n" +
                "@Deprecated\n" +
                "class OdfSimpleWrapper {\n" +
                "    private TextDocument odt\n" +
                "\n" +
                "    OdfSimpleWrapper() {\n" +
                "        odt = TextDocument.newTextDocument()\n" +
                "        Node n = odt.getContentDom().getElementsByTagName(\"office:text\").item(0)\n" +
                "        n.removeChild(n.lastChild)      //delete an empty paragraph\n" +
                "    }\n" +
                "\n" +
                "    OdfSimpleWrapper(File file) {\n" +
                "        this.odt = TextDocument.loadDocument(file)\n" +
                "        fillDefaultStyles()\n" +
                "        escapeAll()\n" +
                "    }\n" +
                "\n" +
                "    OdfSimpleWrapper(String documentPath) {\n" +
                "        this.odt = TextDocument.loadDocument(documentPath)\n" +
                "        fillDefaultStyles()\n" +
                "        escapeAll()\n" +
                "    }\n" +
                "\n" +
                "    OdfSimpleWrapper(InputStream inputStream) {\n" +
                "        this.odt = TextDocument.loadDocument(inputStream)\n" +
                "        fillDefaultStyles()\n" +
                "        escapeAll()\n" +
                "    }\n" +
                "\n" +
                "    protected Set<String> getStyleNames(TextDocument td) {\n" +
                "        Set<String> styleNames = new TreeSet<>()\n" +
                "        NodeList nl = td.getStylesDom().getOfficeStyles().getElementsByTagName(\"style:style\")\n" +
                "        for (int i = 0; i < nl.length; i++) {\n" +
                "            Node n = nl.item(i).getAttributes().getNamedItem(\"style:name\")\n" +
                "            if(n != null)\n" +
                "                styleNames.add(n.getNodeValue())\n" +
                "        }\n" +
                "        return styleNames\n" +
                "    }\n" +
                "\n" +
                "    protected void fillDefaultStyles() {\n" +
                "        Set<String> odtStyleNames = getStyleNames(odt)\n" +
                "\n" +
                "        TextDocument defaultTextDocument = TextDocument.newTextDocument()\n" +
                "\n" +
                "        NodeList nl = defaultTextDocument.getStylesDom().getOfficeStyles().getElementsByTagName(\"style:style\")\n" +
                "        for (int i = 0; i < nl.length; i++) {\n" +
                "            Node node = nl.item(i)\n" +
                "            String styleName = node.getAttributes().getNamedItem(\"style:name\").getNodeValue()\n" +
                "\n" +
                "            if(!odtStyleNames.contains(styleName)) {\n" +
                "                Node n = odt.getStylesDom().importNode(node, true)\n" +
                "                odt.getStylesDom().getOfficeStyles().appendChild(n)\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    void addHeading(String text, int level) {\n" +
                "        def paragraph = odt.addParagraph(text)\n" +
                "        paragraph.applyHeading(true, level)\n" +
                "\n" +
                "        TextStyleNameAttribute attr = new TextStyleNameAttribute((OdfFileDom) paragraph.odfElement.ownerDocument)\n" +
                "        paragraph.odfElement.setOdfAttribute(attr)\n" +
                "        attr.setValue(StyleNames.HEADING.getLevel(level))\n" +
                "    }\n" +
                "\n" +
                "    void addParagraph(String text) {\n" +
                "        def paragraph = odt.addParagraph(text)\n" +
                "\n" +
                "        TextStyleNameAttribute attr = new TextStyleNameAttribute((OdfFileDom) paragraph.odfElement.ownerDocument)\n" +
                "        paragraph.odfElement.setOdfAttribute(attr)\n" +
                "        attr.setValue(StyleNames.BODY_TEXT.getValue())\n" +
                "    }\n" +
                "\n" +
                "    void addQuoteParagraph(String text) {\n" +
                "        def paragraph = odt.addParagraph(text)\n" +
                "\n" +
                "        TextStyleNameAttribute attr = new TextStyleNameAttribute((OdfFileDom) paragraph.odfElement.ownerDocument)\n" +
                "        paragraph.odfElement.setOdfAttribute(attr)\n" +
                "        attr.setValue(StyleNames.QUOTE.getValue())\n" +
                "    }\n" +
                "\n" +
                "    void addCodeBlock(String code, String lang) {\n" +
                "        def paragraph = odt.addParagraph(code)\n" +
                "\n" +
                "        TextStyleNameAttribute attr = new TextStyleNameAttribute((OdfFileDom) paragraph.odfElement.ownerDocument)\n" +
                "        paragraph.odfElement.setOdfAttribute(attr)\n" +
                "        attr.setValue(StyleNames.CODE.getValue())\n" +
                "    }\n" +
                "\n" +
                "    void italicAll() {\n" +
                "        style(OdfSimpleConstants.ITALIC.getMark(), StyleTypeDefinitions.FontStyle.ITALIC)\n" +
                "    }\n" +
                "\n" +
                "    void boldAll() {\n" +
                "        style(OdfSimpleConstants.BOLD.getMark(), StyleTypeDefinitions.FontStyle.BOLD)\n" +
                "    }\n" +
                "\n" +
                "    private void style(String mark, FontStyle style) {\n" +
                "        TextNavigation nav = new TextNavigation(mark+\"[^\"+mark+\"]*\"+mark, odt)\n" +
                "        TextSelection sel = null\n" +
                "\n" +
                "        while(nav.hasNext()) {\n" +
                "            sel = nav.nextSelection()\n" +
                "            sel.replaceWith(sel.getText().substring(mark.length(),sel.getText().length()-mark.length()))\n" +
                "            Span sp = Span.newSpan(sel)\n" +
                "            DefaultStyleHandler dsh = sp.getStyleHandler()\n" +
                "            dsh.getTextPropertiesForWrite().setFontStyle(style)\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    void linkAll() {\n" +
                "        String mark = OdfSimpleConstants.LINK.getMark()\n" +
                "        TextNavigation nav = new TextNavigation(mark+\"[^\"+mark+\"]*\"+mark, odt)\n" +
                "        TextSelection sel = null\n" +
                "\n" +
                "        while(nav.hasNext()) {\n" +
                "            sel = nav.nextSelection()\n" +
                "            String linkData = sel.getText();\n" +
                "            linkData = linkData.substring(mark.length(), linkData.length()-mark.length())\n" +
                "\n" +
                "            String[] atr = linkData.split(OdfSimpleConstants.PARAM.getMark())   //url 0, text 1\n" +
                "\n" +
                "            sel.replaceWith(atr[1])\n" +
                "\n" +
                "            Span sp = Span.newSpan(sel)\n" +
                "            try {\n" +
                "                sp.applyHyperlink(new URI(OdfSimpleConstants.reEscape(atr[0])))\n" +
                "            } catch (Exception e) {\n" +
                "                sel.replaceWith(atr[1] + OdfSimpleConstants.escape(\" (\") + atr[0] + OdfSimpleConstants.escape(\") \"))\n" +
                "            }\n" +
                "\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    void inlineCodeAll() {\n" +
                "        String mark = OdfSimpleConstants.INLINE_CODE.getMark()\n" +
                "        TextNavigation nav = new TextNavigation(mark+\"[^\"+mark+\"]*\"+mark, odt)\n" +
                "        TextSelection sel = null\n" +
                "\n" +
                "        while(nav.hasNext()) {\n" +
                "            sel = nav.nextSelection()\n" +
                "            sel.replaceWith(sel.getText().substring(mark.length(),sel.getText().length()-mark.length()))\n" +
                "            Span sp = Span.newSpan(sel)\n" +
                "            //println(sp.getTextContent())\n" +
                "            DefaultStyleHandler dsh = sp.getStyleHandler()\n" +
                "           // println(dsh.getStyleElementForWrite().toString())\n" +
                "            //dsh.getStyleElementForWrite().setStyleParentStyleNameAttribute(\"Text_20_body\")\n" +
                "           // println(dsh.getStyleElementForWrite().toString())\n" +
                "            dsh.getTextPropertiesForWrite().setFont(new Font(\"Courier New\", FontStyle.REGULAR, 12))\n" +
                "            //dsh.getTextPropertiesForWrite().setFontName(\"Courier New\")\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    void imageAll() {\n" +
                "        String mark = OdfSimpleConstants.IMAGE.getMark()\n" +
                "        TextNavigation nav = new TextNavigation(mark+\"[^\"+mark+\"]*\"+mark, odt)\n" +
                "        TextSelection sel = null\n" +
                "\n" +
                "        while(nav.hasNext()) {\n" +
                "            sel = nav.nextSelection()\n" +
                "            String imageData = sel.getText()\n" +
                "            imageData = imageData.substring(mark.length(), imageData.length()-mark.length())\n" +
                "\n" +
                "            String[] atr = imageData.split(OdfSimpleConstants.PARAM.getMark())  //url, alt, text\n" +
                "\n" +
                "            def imSel = new ImageSelection(sel)\n" +
                "            def im = imSel.replaceWithImage(new URI(OdfSimpleConstants.reEscape(atr[0])))\n" +
                "\n" +
                "            if(im == null) {\n" +
                "                def nl = sel.element.getElementsByTagName(\"draw:frame\")\n" +
                "                def imNode = nl.item(nl.length-1)   //node of a last image which was not added successfully\n" +
                "\n" +
                "                TextSpanElement textSpan = new TextSpanElement((OdfFileDom) sel.element.ownerDocument)\n" +
                "                textSpan.newTextNode(OdfSimpleConstants.escape(\" Image (\") + atr[0] + OdfSimpleConstants.escape(\") \"))\n" +
                "                sel.element.insertBefore(textSpan, imNode)\n" +
                "            }\n" +
                "            else {\n" +
                "                  /*  im.setDescription(\"desc\")\n" +
                "                    im.setName(\"name\")\n" +
                "                    im.setTitle(\"title\")*/\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    void escapeAll() {\n" +
                "        Node root = odt.getContentDom().getElementsByTagName(\"office:text\").item(0)\n" +
                "        Node n = null\n" +
                "        Queue<Node> q = new LinkedList<>()\n" +
                "        q.add(root)\n" +
                "        while(!q.isEmpty()) {\n" +
                "            n = q.poll()\n" +
                "            if(n.hasChildNodes()) {\n" +
                "                for(Node child : n.childNodes) {\n" +
                "                    q.add(child)\n" +
                "                }\n" +
                "            }else if(n instanceof TextImpl) {\n" +
                "                n.setTextContent(OdfSimpleConstants.escape(n.getTextContent()))\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    void reEscapeAll() {\n" +
                "        Node root = odt.getContentDom().getElementsByTagName(\"office:text\").item(0)\n" +
                "        Node n = null\n" +
                "        Queue<Node> q = new LinkedList<>()\n" +
                "        q.add(root)\n" +
                "        while(!q.isEmpty()) {\n" +
                "            n = q.poll()\n" +
                "            if(n.hasChildNodes()) {\n" +
                "                for(Node child : n.childNodes) {\n" +
                "                    q.add(child)\n" +
                "                }\n" +
                "            }else if(n instanceof TextImpl) {\n" +
                "                n.setTextContent(OdfSimpleConstants.reEscape(n.getTextContent()))\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    private ListDecorator switchDecorator(OdfListEnum e) {\n" +
                "        ListDecorator decorator = null\n" +
                "\n" +
                "        switch (e) {\n" +
                "            case OdfListEnum.BULLET_LIST:\n" +
                "                decorator = new BulletDecorator(odt)\n" +
                "                break\n" +
                "\n" +
                "            case OdfListEnum.NUMBERED_LIST:\n" +
                "                decorator = new NumberDecorator(odt)\n" +
                "                break\n" +
                "        }\n" +
                "\n" +
                "        return decorator\n" +
                "    }\n" +
                "\n" +
                "    List addList(String listHeading, OdfListEnum e) {\n" +
                "        ListDecorator decorator = switchDecorator(e)\n" +
                "\n" +
                "        List newList = odt.addList(decorator)\n" +
                "        newList.setHeader(listHeading)\n" +
                "\n" +
                "        return newList\n" +
                "    }\n" +
                "\n" +
                "    List addSubList(List parentList, OdfListEnum e) {\n" +
                "        ListDecorator decorator = switchDecorator(e)\n" +
                "\n" +
                "        return parentList.getItem(parentList.size() - 1).addList(decorator)\n" +
                "    }\n" +
                "\n" +
                "    void addItemsToList(List list, java.util.List<Object> listItems) {\n" +
                "\n" +
                "        for(Object item : listItems) {\n" +
                "            list.addItem((String)item)\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    void addHorizontalRule() {\n" +
                "        def paragraph = odt.addParagraph(\"\")\n" +
                "\n" +
                "        TextStyleNameAttribute attr = new TextStyleNameAttribute((OdfFileDom) paragraph.odfElement.ownerDocument)\n" +
                "        paragraph.odfElement.setOdfAttribute(attr)\n" +
                "        attr.setValue(StyleNames.HORIZONTAL_RULE.getValue())\n" +
                "    }\n" +
                "\n" +
                "    /** Returns the last operated Node. Used for testing.\n" +
                "     *\n" +
                "     * @return the last operated Node\n" +
                "     * */\n" +
                "    @Deprecated\n" +
                "    protected Node getLastNode() {\n" +
                "        return odt.getContentDom().getElementsByTagName(\"office:text\").item(0).lastChild\n" +
                "    }\n" +
                "\n" +
                "    void beforeSave() {\n" +
                "        linkAll()\n" +
                "        inlineCodeAll()\n" +
                "        italicAll()\n" +
                "        boldAll()\n" +
                "        imageAll()\n" +
                "        reEscapeAll()\n" +
                "    }\n" +
                "\n" +
                "    void save(String documentPath) {\n" +
                "        beforeSave()\n" +
                "        odt.save(documentPath)\n" +
                "    }\n" +
                "\n" +
                "    void save(OutputStream outputStream) {\n" +
                "        beforeSave()\n" +
                "        odt.save(outputStream)\n" +
                "    }\n" +
                "}"
        PythonInterpreter interpreter = new PythonInterpreter()

        // Set a variable with the content you want to work with
        interpreter.set("code", code)

        // Simple use Pygments as in Python
        interpreter.exec("""
        from pygments import highlight
        from pygments.lexers import GroovyLexer
        from pygments.formatter import Formatter
        ret = None
        class OwnFormatter(Formatter):
                
            def format(self, tokensource, outfile):
                enc = self.encoding
                global ret
                ret = list(tokensource)
        
        highlight(code, GroovyLexer(), OwnFormatter())""".stripIndent())

        // Get the result that has been set in a variable
        PyList result = interpreter.get("ret", PyList.class)
        for(int i = 0; i < result.size(); i++) {
            PyTuple tuple = result.get(i)
            PyTupleDerived tuple2 = tuple.get(0)
            String value = tuple.get(1)
            print tuple2.get(0) + " : "
            print value
            println ""
        }
        //System.out.println(result)
    }
}
