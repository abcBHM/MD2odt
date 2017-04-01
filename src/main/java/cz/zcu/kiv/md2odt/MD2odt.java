package cz.zcu.kiv.md2odt;

import cz.zcu.kiv.md2odt.document.Document;
import cz.zcu.kiv.md2odt.document.odt.OdfSimpleDocument;
import cz.zcu.kiv.md2odt.filler.Filler;
import cz.zcu.kiv.md2odt.filler.md.FlexMarkFiller;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 *
 * @author Patrik Harag
 * @version 2017-04-01
 */
public class MD2odt {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private MD2odt() {}

    public static void convert(InputStream in, InputStream template, OutputStream out)
            throws IOException {

        convert(asString(in), template, out);
    }

    public static void convert(String md, InputStream template, OutputStream out) {
        Document document = (template == null)
                ? new OdfSimpleDocument()
                : new OdfSimpleDocument(template);

        Filler filler = new FlexMarkFiller();

        filler.fill(md, document);
        document.save(out);
    }

    private static String asString(InputStream in) throws IOException {
        try (Reader reader = new InputStreamReader(in, DEFAULT_CHARSET);
             BufferedReader br = new BufferedReader(reader)) {

            return br.lines().collect(Collectors.joining("\n"));
        }
    }

}
