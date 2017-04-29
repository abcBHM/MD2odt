package cz.zcu.kiv.md2odt;

/**
 * High level API for converting Markdown to odt.
 *
 * @author Patrik Harag
 * @version 2017-04-08
 */
public class MD2odt {

    private MD2odt() {}

    /**
     * Creates converter.
     *
     * @return converter
     */
    public static Converter converter() {
        return new Converter();
    }

}
