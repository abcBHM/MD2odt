package cz.zcu.kiv.md2odt;

import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.escaped.character.EscapedCharacterExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughSubscriptExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.SubscriptExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.SimTocExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.superscript.SuperscriptExtension;
import cz.zcu.kiv.md2odt.document.Document;
import cz.zcu.kiv.md2odt.document.odfdom.OdfdomDocument;
import cz.zcu.kiv.md2odt.filler.*;
import cz.zcu.kiv.md2odt.filler.md.FlexMarkFiller;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 *
 * @author Patrik Harag
 * @version 2017-04-21
 */
public class Converter {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final String DEFAULT_TEMPLATE = "/default-template.odt";
    private static final Predicate<URL> DEFAULT_RESOURCE_POLICY = (url) -> true;

    private Source input;
    private InputStream template;
    private OutputStream output;

    private Predicate<URL> resourcesPolicy;

    private boolean enableAutolinks;
    private boolean enableEmoji;
    private boolean enableStrikethrough;
    private boolean enableSubscript;
    private boolean enableSuperscript;
    private boolean enableTables;
    private boolean enableTableOfContents;

    // source

    public Converter setInputString(String md) {
        this.input = new SourceString(md);
        return this;
    }

    public Converter setInputStream(InputStream in) {
        return setInputStream(in, DEFAULT_CHARSET);
    }

    public Converter setInputStream(InputStream in, Charset charset) {
        this.input = new SourceCharacterStream(in, charset);
        return this;
    }

    public Converter setInputZip(InputStream in) {
        return setInputZip(in, DEFAULT_CHARSET);
    }

    public Converter setInputZip(InputStream in, Charset charset) {
        this.input = new SourceZip(in, charset);
        return this;
    }

    // template

    public Converter setTemplate(InputStream template) {
        this.template = template;
        return this;
    }

    // output

    public Converter setOutput(OutputStream out) {
        this.output = out;
        return this;
    }

    // resources

    public Converter setResourcesPolicy(Predicate<URL> predicate) {
        this.resourcesPolicy = predicate;
        return this;
    }

    // extensions

    public Converter enableAutolinks() {
        this.enableAutolinks = true;
        return this;
    }

    public Converter enableEmoji() {
        this.enableEmoji = true;
        return this;
    }

    public Converter enableStrikethrough() {
        this.enableStrikethrough = true;
        return this;
    }

    public Converter enableSubscript() {
        this.enableSubscript = true;
        return this;
    }

    public Converter enableSuperscript() {
        this.enableSuperscript = true;
        return this;
    }

    public Converter enableTables() {
        this.enableTables = true;
        return this;
    }

    public Converter enableTableOfContents() {
        this.enableTableOfContents = true;
        return this;
    }

    public Converter enableAllExtensions() {
        enableAutolinks();
        enableEmoji();
        enableStrikethrough();
        enableSubscript();
        enableSuperscript();
        enableTables();
        enableTableOfContents();
        return this;
    }

    // ---

    public void convert() throws IOException {
        if (input == null)
            throw new IllegalArgumentException("Input not set");

        if (output == null)
            throw new IllegalArgumentException("Output not set");

        Document document = new OdfdomDocument(getTemplate());

        Parser parser = Parser.builder().extensions(getExtensions()).build();
        Filler filler = new FlexMarkFiller(parser);
        filler.fill(input.getSource(), getResourceManager(), document);
        document.save(output);
    }

    private ResourceManager getResourceManager() {
        Predicate<URL> predicate = (resourcesPolicy != null)
                ? resourcesPolicy
                : DEFAULT_RESOURCE_POLICY;

        return new ResourceManagerImpl(input.getResources(), predicate);
    }

    private InputStream getTemplate() {
        if (template != null)
            return template;
        else
            return System.class.getResourceAsStream(DEFAULT_TEMPLATE);
    }

    private List<Extension> getExtensions() {
        List<Extension> extensions = new ArrayList<>();

        extensions.add(EscapedCharacterExtension.create());

        if (enableAutolinks) extensions.add(AutolinkExtension.create());
        if (enableEmoji) extensions.add(EmojiExtension.create());
        if (enableSuperscript) extensions.add(SuperscriptExtension.create());
        if (enableTables) extensions.add(TablesExtension.create());

        if (enableStrikethrough && enableSubscript)
            extensions.add(StrikethroughSubscriptExtension.create());
        else if (enableStrikethrough)
            extensions.add(StrikethroughExtension.create());
        else if (enableSubscript)
            extensions.add(SubscriptExtension.create());

        if (enableTableOfContents) {
            extensions.add(TocExtension.create());
            extensions.add(SimTocExtension.create());
        }

        return extensions;
    }

}
