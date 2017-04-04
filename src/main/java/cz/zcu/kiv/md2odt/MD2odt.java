package cz.zcu.kiv.md2odt;

import java.io.*;

/**
 *
 * @author Patrik Harag
 * @version 2017-04-04
 */
public class MD2odt {

    private MD2odt() {}

    public static void convert(InputStream in, InputStream template, OutputStream out)
            throws IOException {

        converter()
                .setInput(in)
                .setTemplate(template)
                .setOutput(out)
                .convert();
    }

    public static void convert(String md, InputStream template, OutputStream out)
            throws IOException {

        converter()
                .setInput(md)
                .setTemplate(template)
                .setOutput(out)
                .convert();
    }

    public static Converter converter() {
        return new Converter();
    }

}
