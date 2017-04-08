package cz.zcu.kiv.md2odt;

import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.parser.Parser;
import cz.zcu.kiv.md2odt.document.Document;
import cz.zcu.kiv.md2odt.document.odfdom.OdfdomDocument;
import cz.zcu.kiv.md2odt.filler.*;
import cz.zcu.kiv.md2odt.filler.md.FlexMarkFiller;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Patrik Harag
 * @version 2017-04-08
 */
public class Converter {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private Source input;
    private InputStream template;
    private OutputStream output;

    private boolean enableAutolinks;
    private boolean enableEmoji;

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

    // extensions

    public Converter enableAutolinks() {
        this.enableAutolinks = true;
        return this;
    }

    public Converter enableEmoji() {
        this.enableEmoji = true;
        return this;
    }

    // ---

    public void convert() throws IOException {
        if (input == null)
            throw new IllegalArgumentException("Input not set");

        if (output == null)
            throw new IllegalArgumentException("Output not set");

        Document document = (template == null)
                ? new OdfdomDocument()
                : new OdfdomDocument(template);

        Parser parser = Parser.builder().extensions(getExtensions()).build();
        Filler filler = new FlexMarkFiller(parser);
        filler.fill(input.getSource(), input.getResources(), document);
        document.save(output);
    }

    private List<Extension> getExtensions() {
        List<Extension> extensions = new ArrayList<>();

        if (enableAutolinks) extensions.add(AutolinkExtension.create());
        if (enableEmoji) extensions.add(EmojiExtension.create());

        return extensions;
    }

}
