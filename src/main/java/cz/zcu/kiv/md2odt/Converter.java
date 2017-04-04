package cz.zcu.kiv.md2odt;

import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.parser.Parser;
import cz.zcu.kiv.md2odt.document.Document;
import cz.zcu.kiv.md2odt.document.odt.OdfSimpleDocument;
import cz.zcu.kiv.md2odt.filler.Filler;
import cz.zcu.kiv.md2odt.filler.md.FlexMarkFiller;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Patrik Harag
 * @version 2017-04-04
 */
public class Converter {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private String inString;
    private InputStream inStream;
    private Charset charset;

    private InputStream template;

    private OutputStream out;

    private boolean enableAutolinks;

    // source

    public Converter setInput(InputStream in) {
        return setInput(in, DEFAULT_CHARSET);
    }

    public Converter setInput(InputStream in, Charset charset) {
        this.inString = null;
        this.inStream = in;
        this.charset = charset;
        return this;
    }

    public Converter setInput(String md) {
        this.inString = md;
        this.inStream = null;
        return this;
    }

    // template

    public Converter setTemplate(InputStream template) {
        this.template = template;
        return this;
    }

    // output

    public Converter setOutput(OutputStream out) {
        this.out = out;
        return this;
    }

    // extensions

    public Converter enableAutolinks() {
        this.enableAutolinks = true;
        return this;
    }

    // ---

    public void convert() throws IOException {
        Document document = (template == null)
                ? new OdfSimpleDocument()
                : new OdfSimpleDocument(template);

        String md = (inString == null)
                ? asString(inStream, charset)
                : inString;

        Parser parser = Parser.builder().extensions(getExtensions()).build();
        Filler filler = new FlexMarkFiller(parser);
        filler.fill(md, document);
        document.save(out);
    }

    private List<Extension> getExtensions() {
        List<Extension> extensions = new ArrayList<>();

        if (enableAutolinks) extensions.add(AutolinkExtension.create());

        return extensions;
    }

    private String asString(InputStream in, Charset charset) throws IOException {
        try (Reader reader = new InputStreamReader(in, charset);
             BufferedReader br = new BufferedReader(reader)) {

            return br.lines().collect(Collectors.joining("\n"));
        }
    }

}
