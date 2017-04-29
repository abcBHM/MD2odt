package cz.zcu.kiv.md2odt;

import java.util.Arrays;
import java.util.List;

/**
 * Available extensions.
 *
 * @author Patrik Harag
 * @version 2017-04-29
 */
public class Extensions {

    private Extensions() { }

    public static final String EXT_AUTOLINKS = "AUTOLINKS";
    public static final String EXT_EMOJI = "EMOJI";
    public static final String EXT_STRIKETHROUGH = "STRIKETHROUGH";
    public static final String EXT_SUBSCRIPT = "SUBSCRIPT";
    public static final String EXT_SUPERSCRIPT = "SUPERSCRIPT";
    public static final String EXT_TABLES = "TABLES";
    public static final String EXT_TABLE_OF_CONTENTS = "TABLE OF CONTENTS";

    public static final List<String> LIST = Arrays.asList(
            EXT_AUTOLINKS,
            EXT_EMOJI,
            EXT_STRIKETHROUGH,
            EXT_SUBSCRIPT,
            EXT_SUPERSCRIPT,
            EXT_TABLES,
            EXT_TABLE_OF_CONTENTS
    );

}
