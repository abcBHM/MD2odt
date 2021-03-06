package cz.zcu.kiv.md2odt.document.odfdom

import cz.zcu.kiv.md2odt.document.*
import cz.zcu.kiv.md2odt.highlight.CodeParser
import cz.zcu.kiv.md2odt.highlight.CodeSectionTypeColorHandler
import cz.zcu.kiv.md2odt.highlight.content.CodeSection
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import org.apache.log4j.Logger
import org.odftoolkit.odfdom.dom.OdfSchemaDocument
import org.odftoolkit.odfdom.dom.attribute.table.TableStyleNameAttribute
import org.odftoolkit.odfdom.dom.attribute.text.TextStyleNameAttribute
import org.odftoolkit.odfdom.dom.element.draw.DrawFrameElement
import org.odftoolkit.odfdom.dom.element.draw.DrawImageElement
import org.odftoolkit.odfdom.dom.element.text.TextAElement
import org.odftoolkit.odfdom.dom.element.text.TextLineBreakElement
import org.odftoolkit.odfdom.dom.element.text.TextPElement
import org.odftoolkit.odfdom.dom.element.text.TextSpanElement
import org.odftoolkit.odfdom.pkg.OdfElement
import org.odftoolkit.odfdom.pkg.OdfPackage
import org.odftoolkit.odfdom.pkg.manifest.OdfFileEntry
import org.odftoolkit.odfdom.type.Color
import org.odftoolkit.simple.TextDocument
import org.odftoolkit.simple.draw.Image
import org.odftoolkit.simple.style.StyleTypeDefinitions
import org.odftoolkit.simple.table.Cell
import org.odftoolkit.simple.table.Table
import org.odftoolkit.simple.table.TableContainer
import org.odftoolkit.simple.text.Paragraph
import org.odftoolkit.simple.text.Span
import org.odftoolkit.simple.text.list.*
import org.odftoolkit.simple.text.list.List as OdfList
import org.w3c.dom.Text

import java.util.List

/**
 * Wrapper for a TextDocument. It provides functionality of Document interface.
 */
class OdfdomDocument implements DocumentAdapter{

    private static final Logger LOGGER = Logger.getLogger(OdfdomDocument)
    protected final TextDocument odt
    protected java.awt.Color codeBlockBackgroundColor

    /** Wrapper for an empty TextDocument constructor.
     * */
    OdfdomDocument() {
        this.odt = TextDocumentHandler.handler().newTextDocument()
        codeBlockBackgroundColor = TextDocumentHandler.getCodeBlockBackgroundColor(odt)
    }

    /** Wrapper for a TextDocument from template constructor.
     *
     * @param file Template file (odt supported).
     * */
    OdfdomDocument(File file) {
        this.odt = TextDocumentHandler.handler().newTextDocumentFromTemplate(file)
        codeBlockBackgroundColor = TextDocumentHandler.getCodeBlockBackgroundColor(odt)
    }

    /** Wrapper for a TextDocument from template constructor.
     *
     * @param documentPath Template path (odt supported).
     * */
    OdfdomDocument(String documentPath) {
        this.odt = TextDocumentHandler.handler().newTextDocumentFromTemplate(documentPath)
        codeBlockBackgroundColor = TextDocumentHandler.getCodeBlockBackgroundColor(odt)
    }

    /** Wrapper for a TextDocument from template constructor.
     *
     * @param inputStream Template inputStream (odt supported).
     * */
    OdfdomDocument(InputStream inputStream) {
        this.odt = TextDocumentHandler.handler().newTextDocumentFromTemplate(inputStream)
        codeBlockBackgroundColor = TextDocumentHandler.getCodeBlockBackgroundColor(odt)
    }

    /** Appends text to an OdfElement.
     *
     * @param element OdfElement where text is appended.
     * @param text Text to append.
     * */
    protected void appendText(OdfElement element, String text) {

        String[] s = text.split("\n")

        if (s == null || s.length < 1) {
            for (int i = 0; i < text.length(); i++) {
                TextLineBreakElement tlbe = new TextLineBreakElement(odt.contentDom)
                element.appendChild(tlbe)
            }
            return
        }

        Text textNode = odt.getContentDom().createTextNode(s[0])
        element.appendChild(textNode)

        for (int i = 1; i < s.length; i++) {
            TextLineBreakElement tlbe = new TextLineBreakElement(odt.contentDom)
            element.appendChild(tlbe)

            textNode = odt.getContentDom().createTextNode(s[i])
            element.appendChild(textNode)
        }

        if (text.endsWith("\n")) {
            TextLineBreakElement tlbe = new TextLineBreakElement(odt.contentDom)
            element.appendChild(tlbe)
        }
    }

    /** Appends span to an OdfElement.
     *
     * @param element OdfElement where span is appended.
     * @return Appended Span instance.
     * */
    protected Span appendSpan(OdfElement element) {
        Span s = new Span(new TextSpanElement(odt.getContentDom()))
        element.appendChild(s.getOdfElement())
        return s
    }

    /** Appends code span with font to an OdfElement.
     *
     * @param element OdfElement where span is appended.
     * @return Appended Span instance.
     * */
    protected Span appendCodeSpan(OdfElement element) {
        Span s = appendSpan(element)
        s.getStyleHandler().getTextPropertiesForWrite().setFontName("Courier New")
        return s
    }

    /** Appends span with bold style to an OdfElement.
     *
     * @param element OdfElement where span is appended.
     * @return Appended Span instance.
     * */
    protected Span appendBoldSpan(OdfElement element) {
        Span s = appendSpan(element)
        s.getStyleHandler().getTextPropertiesForWrite().setFontStyle(StyleTypeDefinitions.FontStyle.BOLD)
        return s
    }

    /** Appends span with italic style to an OdfElement.
     *
     * @param element OdfElement where span is appended.
     * @return Appended Span instance.
     * */
    protected Span appendItalicSpan(OdfElement element) {
        Span s = appendSpan(element)
        s.getStyleHandler().getTextPropertiesForWrite().setFontStyle(StyleTypeDefinitions.FontStyle.ITALIC)
        return s
    }

    /** Appends span with strike style to an OdfElement.
     *
     * @param element OdfElement where span is appended.
     * @return Appended Span instance.
     * */
    protected Span appendStrikeSpan(OdfElement element) {
        Span s = appendSpan(element)
        setTextStyleNameAttr(s.odfElement, StyleNames.STRIKE.getValue())
        return s
    }

    /** Appends span with sub-script style to an OdfElement.
     *
     * @param element OdfElement where span is appended.
     * @return Appended Span instance.
     * */
    protected Span appendSubScriptSpan(OdfElement element) {
        Span s = appendSpan(element)
        setTextStyleNameAttr(s.odfElement, StyleNames.SUB_SCRIPT.getValue())
        return s
    }

    /** Appends span with super-script style to an OdfElement.
     *
     * @param element OdfElement where span is appended.
     * @return Appended Span instance.
     * */
    protected Span appendSuperScriptSpan(OdfElement element) {
        Span s = appendSpan(element)
        setTextStyleNameAttr(s.odfElement, StyleNames.SUPER_SCRIPT.getValue())
        return s
    }

    /** Appends link to an OdfElement.
     *
     * @param element OdfElement where link is appended.
     * @param content ParagraphContent which is inside a link. (List of SpanContents)
     * @param url Referring url.
     * */
    protected void appendLink(OdfElement element, ParagraphContent content, String url) {
        TextAElement aElement = (TextAElement) odt.getContentDom().newOdfElement(TextAElement.class)
        aElement.setXlinkTypeAttribute("simple")
        aElement.setXlinkHrefAttribute(url)
        fillWithParagraphContent(aElement, content)
        element.appendChild(aElement)
    }

    /** Converts svg to png image.
     *
     * @param input SVG stream input.
     * @param output PNG stream output.
     * */
    protected void convertSvgToPng(InputStream input, OutputStream output) {
        LOGGER.debug("IMAGE converting SVG to PNG START")
        TranscoderInput input_svg_image = new TranscoderInput(input)
        //Step-2: Define OutputStream to PNG Image and attach to TranscoderOutput
        TranscoderOutput output_png_image = new TranscoderOutput(output)
        // Step-3: Create PNGTranscoder and define hints if required
        PNGTranscoder my_converter = new PNGTranscoder();
        // Step-4: Convert and Write output
        my_converter.transcode(input_svg_image, output_png_image);
        // Step 5- close / flush Output Stream
        output.flush()
        output.close()

        input.close()
        LOGGER.debug("IMAGE converting SVG to PNG DONE")
    }

    /** Handling SVG image.
     *
     * @param packagePath path of SVG image.
     * @param mOdfSchemaDoc1 OdfSchemaDocument to work with.
     * @return packagePath of a PNG image
     * */
    protected String appendImageHandleSvg(String packagePath, OdfSchemaDocument mOdfSchemaDoc1) {
        LOGGER.debug("IMAGE handling SVG START")

        def inp = mOdfSchemaDoc1.getPackage().getInputStream(packagePath)
        packagePath = packagePath.substring(0, packagePath.length() - 3) + "png"

        LOGGER.debug("IMAGE recognized as SVG will be converted to: "+packagePath)

        def out = mOdfSchemaDoc1.getPackage().insertOutputStream(packagePath, "image/png")
        convertSvgToPng(inp, out)

        LOGGER.debug("IMAGE handling SVG DONE")
        return packagePath
    }

    /** Appends image from an URL. !!! Not handling it's size. !!!
     *
     * @param element OdfElement where image is appended.
     * @param url Adress of an image.
     * */
    protected void appendImage(OdfElement element, String url) {
        LOGGER.info("IMAGE START apending from URL")
        LOGGER.debug("URL: " + url)

        DrawFrameElement frame = new DrawFrameElement(odt.getContentDom())
        DrawImageElement e1 = frame.newDrawImageElement()

        URI imageUri = new URI(url)
        String imageRef1 = imageUri.toString()
        String mediaType1 = OdfFileEntry.getMediaTypeString(imageRef1)
        OdfSchemaDocument mOdfSchemaDoc1 = (OdfSchemaDocument) odt.getContentDom().getDocument()
        String packagePath = getImagePath(mOdfSchemaDoc1, imageRef1)
        mOdfSchemaDoc1.getPackage().insert(imageUri, packagePath, mediaType1)

        if (packagePath.endsWith("svg")) {
            packagePath = appendImageHandleSvg(packagePath, mOdfSchemaDoc1)
        }

        packagePath = packagePath.replaceFirst(odt.getContentDom().getDocument().getDocumentPath(), "")
        Image.configureInsertedImage((OdfSchemaDocument) odt.getContentDom().getDocument(), e1, packagePath, false)
        Image mImage = Image.getInstanceof(e1)
        mImage.getStyleHandler().setAchorType(StyleTypeDefinitions.AnchorType.AS_CHARACTER)

        element.appendChild(frame)
        LOGGER.info("IMAGE DONE apending from URL")
    }

    /** Appends image from stream. !!! Not handling it's size. !!!
     *
     * @param element OdfElement where image is appended.
     * @param url Adress of an image.
     * @param inputStream Stream of an image.
     * */
    protected void appendImageFromStream(OdfElement element, String url, InputStream inputStream) {
        LOGGER.info("IMAGE START apending from STREAM")
        LOGGER.debug("URL: " + url)

        DrawFrameElement frame = new DrawFrameElement(odt.getContentDom())
        DrawImageElement e1 = frame.newDrawImageElement()

        String imageRef1 = url
        String mediaType1 = OdfFileEntry.getMediaTypeString(imageRef1)
        OdfSchemaDocument mOdfSchemaDoc1 = (OdfSchemaDocument) odt.getContentDom().getDocument()
        String packagePath = getImagePath(mOdfSchemaDoc1, imageRef1)
        mOdfSchemaDoc1.getPackage().insert(inputStream, packagePath, mediaType1)

        if (packagePath.endsWith("svg")) {
            packagePath = appendImageHandleSvg(packagePath, mOdfSchemaDoc1)
        }

        packagePath = packagePath.replaceFirst(odt.getContentDom().getDocument().getDocumentPath(), "")
        Image.configureInsertedImage((OdfSchemaDocument) odt.getContentDom().getDocument(), e1, packagePath, false)
        Image mImage = Image.getInstanceof(e1)
        mImage.getStyleHandler().setAchorType(StyleTypeDefinitions.AnchorType.AS_CHARACTER)

        element.appendChild(frame)
        LOGGER.info("IMAGE DONE apending from STREAM")
    }

    /** To retrieve a path of an image in a given OdfSchemaDocument.
     *
     * @param mOdfSchemaDoc OdfSchemaDocument of a TextDocument.
     * @param imageRef Adress of an image.
     * */
    protected String getImagePath(OdfSchemaDocument mOdfSchemaDoc, String imageRef) {
        def fullPath

        try {
            URL url = new URL(imageRef)
            fullPath = url.getHost() + url.getPath()
            LOGGER.debug("IMAGE_PATH path is url: " + fullPath)
        }
        catch (Exception e) {
            fullPath = imageRef
            LOGGER.debug("IMAGE_PATH path is not url: " + fullPath)
        }

        if(fullPath.contains("//")) {
            fullPath = fullPath.substring(fullPath.lastIndexOf("//") + 2, fullPath.length())
        }
        if(fullPath.startsWith("/")) {
            fullPath = fullPath.substring(1, fullPath.length())
        }
        if(fullPath.endsWith("/")) {
            fullPath = fullPath.substring(0, fullPath.length()-1)
        }

        String name = fullPath
        String path = ""
        if(fullPath.contains("/")) {
            name = fullPath.substring(fullPath.lastIndexOf("/"), fullPath.length())
            path = fullPath.substring(0, fullPath.lastIndexOf("/")).replaceAll("[^a-zA-Z0-9/.-]", "_")
        }

        String packagePath = mOdfSchemaDoc.getDocumentPath() + OdfPackage.OdfFile.IMAGE_DIRECTORY.getPath() + "/" + path + name

        LOGGER.debug("IMAGE_PATH: '" + imageRef + "' converted to '" + packagePath + "'")

        return packagePath
    }

    /** Fills an element with a ParagraphContent.
     *
     * @param element OdfElement to fill with ParagraphContent.
     * @param paragraphContent ParagraphContent containing list of SpanContents.
     * */
    protected void fillWithParagraphContent(OdfElement element, ParagraphContent paragraphContent) {
        if (paragraphContent == null) {
            LOGGER.debug("ParagraphContent is null in fillWithParagraphContent() method.")
            return
        }
        for(SpanContent sc : paragraphContent.list) {
            if(sc == null) {
                LOGGER.debug("SpanContent is null in fillWithParagraphContent() method.")
                continue
            }

            switch (sc.getType()) {
               case SpanType.TEXT:
                    if (sc instanceof SpanContentText) {
                        if (sc.styles.isEmpty()) {
                            appendText(element, sc.getText())
                        }
                        else {
                            OdfElement e = element
                            Span s

                            for (TextStyle ts : sc.styles ) {
                                switch (ts) {
                                    case TextStyle.ITALIC:
                                        s = appendItalicSpan(e)
                                        e = s.odfElement
                                        break
                                    case TextStyle.BOLD:
                                        s = appendBoldSpan(e)
                                        e = s.odfElement
                                        break
                                    case TextStyle.CODE:
                                        s = appendCodeSpan(e)
                                        e = s.odfElement
                                        break
                                    case TextStyle.STRIKE:
                                        s = appendStrikeSpan(e)
                                        e = s.odfElement
                                        break
                                    case TextStyle.SUBSCRIPT:
                                        s = appendSubScriptSpan(e)
                                        e = s.odfElement
                                        break
                                    case TextStyle.SUPERSCRIPT:
                                        s = appendSuperScriptSpan(e)
                                        e = s.odfElement
                                        break
                                    default:
                                        LOGGER.warn("TextStyle not implemented: " + ts + " in class " + sc.class)
                                }
                            }
                            appendText(e, sc.getText())
                        }
                    } else {
                        LOGGER.error("SpanContent with a '" + sc.getType() + "' type and instance of '" + sc.class + "'")
                    }
                    break

                case SpanType.LINK:
                    if (sc instanceof SpanContentLink) {
                        appendLink(element, sc.content, sc.url)
                    } else {
                        LOGGER.error("SpanContent with a '" + sc.getType() + "' type and instance of '" + sc.class + "'")
                    }
                    break

                case SpanType.IMAGE:
                    LOGGER.debug("IMAGE: START handling SpanType.IMAGE")
                    if (sc instanceof SpanContentImageStream) {
                        try {
                            appendImageFromStream(element, sc.getUrl(), sc.getStream())
                        }
                        catch (Exception e) {
                            LOGGER.warn("Exception while inserting image from stream: " + e.toString())
                            appendText(element, sc.getAlt())
                        }
                        finally{
                            if (sc.getStream())
                                sc.getStream().close()
                        }
                    } else if (sc instanceof SpanContentImage) {
                        try {
                            appendImage(element, sc.getUrl())
                        }
                        catch (Exception e) {
                            LOGGER.warn("Exception while inserting image: " + e.toString())
                            appendText(element, sc.getAlt())
                        }
                    } else {
                        LOGGER.error("SpanContent with a '" + sc.getType() + "' type and instance of '" + sc.class + "'")
                    }
                    LOGGER.debug("IMAGE: DONE handling SpanType.IMAGE")
                    break
                default:
                    LOGGER.warn("SpanType not implemented: " + sc.getType() + " in class " + sc.class)
            }
        }
    }

    /** Sets TextStyleNameAttribute of element.
     *
     * @param element OdfElement to set TextStyleNameAttribute.
     * @param styleName Style name to set.
     * */
    protected void setTextStyleNameAttr(OdfElement element, String styleName) {
        TextStyleNameAttribute attr = new TextStyleNameAttribute(odt.getContentDom())
        element.setOdfAttribute(attr)
        attr.setValue(styleName)
    }

    /** Adds an empty paragraph to a document.
     *
     * @param styleName Style name to set to a paragraph.
     * @return Instance of added paragraph.
     * */
    protected Paragraph addParagraph(String styleName) {
        return addParagraph("", styleName)
    }

    /** Adds a paragraph to a document.
     *
     * @param text Paragraph will contain this text.
     * @param styleName Style name to set to a paragraph.
     * @return Instance of added paragraph.
     * */
    protected Paragraph addParagraph(String text, String styleName) {
        Paragraph paragraph = odt.addParagraph(text)
        setTextStyleNameAttr(paragraph.getOdfElement(), styleName)
        return paragraph
    }

    @Override
    void addHeading(String text, int level) {
        def paragraph = addParagraph(text, StyleNames.HEADING.getLevel(level))
        paragraph.applyHeading(true, level)
    }

    @Override
    void addHeading(ParagraphContent content, int level) {
        Paragraph paragraph = addParagraph(StyleNames.HEADING.getLevel(level))
        fillWithParagraphContent(paragraph.getOdfElement(), content)
        paragraph.applyHeading(true, level)
    }

    @Override
    void addParagraph(ParagraphContent content) {
        Paragraph paragraph = addParagraph(StyleNames.BODY_TEXT.getValue())
        fillWithParagraphContent(paragraph.getOdfElement(), content)
    }

    @Override
    void addCodeBlock(String code) {
        addParagraph(code, StyleNames.CODE.getValue())
    }

    /** Appends span with given font color to an OdfElement.
     *
     * @param element OdfElement where span is appended.
     * @param text Text contained by a span.
     * @param color Font color to set.
     * */
    protected void appendColorSpan(OdfElement element, String text, java.awt.Color color) {
        Span s = new Span(new TextSpanElement(odt.getContentDom()))
        appendText(s.odfElement, text)
        s.getStyleHandler().getTextPropertiesForWrite().setFontColor(new Color(color))
        element.appendChild(s.getOdfElement())
    }

    @Override
    void addCodeBlock(String code, String lang) {
        if (lang == null || lang.equals("")) {
            addCodeBlock(code)
            return
        }
        CodeParser codeParser = new CodeParser()
        if (!codeParser.isKnownLanguage(lang)) {
            LOGGER.debug("Language '$lang' is not known, code is not formatted")
            addCodeBlock(code)
            return
        }

        LOGGER.debug("Adding highlighted code block: $lang")
        CodeSectionTypeColorHandler colorHandler = new CodeSectionTypeColorHandler(codeBlockBackgroundColor)
        def parElement = addParagraph(StyleNames.CODE.getValue()).getOdfElement()
        List<CodeSection> codeSections = codeParser.parse(code, lang)
        for (CodeSection cs : codeSections) {
            appendColorSpan(parElement, cs.getText(), colorHandler.handle(cs.getType()))
        }
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

    /** Returns a list decorator corresponding to given ListType.
     *
     * @param e ListType that should be used.
     * @return List decorator corresponding to given ListType.
     * */
    protected ListDecorator switchDecorator(ListType e) {
        ListDecorator decorator = null
        switch (e) {
            case ListType.BULLET:
                decorator = new BulletDecorator(odt)
                break
            case ListType.ORDERED:
                decorator = new NumberDecorator(odt)
                break
            default:
                decorator = new BulletDecorator(odt)
                LOGGER.warn("ListType not implemented: " + e)
        }
        return decorator
    }

    @Override
    void addList(ListContent content) {
        addList(odt, content)
    }

    /** Appends list to container.
     *
     * @param container ListContainer where a list is added.
     * @param content ListContent that will be contained in list.
     * */
    protected void addList(ListContainer container, ListContent content) {
        OdfList list = container.addList(switchDecorator(content.getType()))
        List<List<BlockContent>> listListBlockContent = content.getListItems()

        for (List<BlockContent> listBlock : listListBlockContent) {
            ListItem listItem = new ListItem(list.getOdfElement().newTextListItemElement())

            for (BlockContent blockContent : listBlock) {
                if(blockContent instanceof ParagraphContent) {
                    TextPElement par = listItem.odfElement.newTextPElement()
                    setTextStyleNameAttr(par, StyleNames.LIST.getValue())
                    fillWithParagraphContent(par, blockContent)
                }
                else if(blockContent instanceof ListContent) {
                    addList(listItem, blockContent)
                }
                else if(blockContent instanceof TableContent) {
                    LOGGER.warn("Table in list is not supported. Table is placed after the list.")
                    addTable(blockContent)
                }
                else {
                    LOGGER.warn("BlockContent not implemented in addList(): " + blockContent.class)
                }
            }
        }
    }

    @Override
    void addTable(TableContent content) {
        addTable(odt, content)
    }

    /** Appends table to container.
     *
     * @param container TableContainer where a table is added.
     * @param content TableContent that will be contained in table.
     * @return added table
     * */
    protected Table addTable(TableContainer container, TableContent content) {

        List<List<TableCellContent>> rows = content.getRows()
        int rowCount = rows.size()
        int colCount = 0
        for (List<TableCellContent> cols : rows) {
            if (cols.size()>colCount) {
                colCount = cols.size()
            }
        }

        Table table = container.addTable(rowCount, colCount)
        table.setVerticalMargin(0.1, 0.1)

        int r = 0
        for (List<TableCellContent> row : rows) {
            int c = 0
            for (TableCellContent tableCellContent : row) {
                Cell cell = table.getCellByPosition(c, r)

                BlockContent tccc = tableCellContent.content
                if (tccc instanceof ParagraphContent) {
                    Paragraph par = cell.addParagraph("")
                    fillWithParagraphContent(par.odfElement, tableCellContent.content)

                    switch (tableCellContent.align) {
                        case TableCellContent.Align.LEFT:
                            par.setHorizontalAlignment(StyleTypeDefinitions.HorizontalAlignmentType.LEFT)
                            break
                        case TableCellContent.Align.CENTER:
                            par.setHorizontalAlignment(StyleTypeDefinitions.HorizontalAlignmentType.CENTER)
                            break
                        case TableCellContent.Align.RIGHT:
                            par.setHorizontalAlignment(StyleTypeDefinitions.HorizontalAlignmentType.RIGHT)
                            break
                    }

                    if (tableCellContent.heading) {
                        par.styleHandler.styleElementForWrite.setStyleParentStyleNameAttribute(StyleNames.TABLE_HEADING.getValue())
                    }
                    else {
                        par.styleHandler.styleElementForWrite.setStyleParentStyleNameAttribute(StyleNames.TABLE_CONTENTS.getValue())
                    }
                }
                else if (tccc instanceof ListContent) {
                    addList(cell, tccc)
                }
                else if (tccc instanceof TableContent) {
                    addTable(cell, tccc)
                }
                else {
                    LOGGER.warn("BlockContent not implemented in addTable(): " + tccc.class)
                }

                c++
            }
            r++
        }
        return table
    }

    /** Appends table to cell.
     *
     * @param container TableContainer where a table is added.
     * @param content TableContent that will be contained in table.
     * */
    protected addTable(Cell cell, TableContent content) {
        Table table = addTable(odt, content)
        table.remove()
        cell.odfElement.appendChild(table.odfElement)
    }

    @Override
    void addBlockContent(BlockContent content) {
        if (content instanceof ParagraphContent) {
            addParagraph(content)
        }
        else if (content instanceof ListContent) {
            addList(odt, content)
        }
        else if (content instanceof TableContent) {
            addTable(odt, content)
        }
        else {
            LOGGER.warn("BlockContent not known in addBlockContent: " + content.class)
        }
    }

    @Override
    void save(String documentPath) {
        odt.save(documentPath)
    }

    @Override
    void save(OutputStream outputStream) {
        odt.save(outputStream)
    }

    @Override
    void addTableOfContents() {
        def par = odt.addParagraph("")
        odt.createDefaultTOC(par, false)
        par.remove()
    }
}
