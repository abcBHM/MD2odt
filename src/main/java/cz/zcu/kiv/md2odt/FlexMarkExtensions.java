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
import com.vladsch.flexmark.superscript.SuperscriptExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cz.zcu.kiv.md2odt.Extensions.*;

/**
 * FlexMark extensions builder.
 *
 * @author Patrik Harag
 * @version 2017-04-29
 */
class FlexMarkExtensions {

    private final Set<String> selected = new HashSet<>();

    /**
     * Adds an extension.
     *
     * @param extension extension
     */
    void add(String extension) {
        selected.add(extension);
    }

    /**
     * Creates a list of FlexMark extensions from selected extensions.
     *
     * @return list of extensions
     */
    List<Extension> getExtensions() {
        List<Extension> extensions = new ArrayList<>();

        extensions.add(EscapedCharacterExtension.create());

        boolean enableAutolinks = selected.contains(EXT_AUTOLINKS);
        boolean enableEmoji = selected.contains(EXT_EMOJI);
        boolean enableStrikethrough = selected.contains(EXT_STRIKETHROUGH);
        boolean enableSubscript = selected.contains(EXT_SUBSCRIPT);
        boolean enableSuperscript = selected.contains(EXT_SUPERSCRIPT);
        boolean enableTables = selected.contains(EXT_TABLES);
        boolean enableTableOfContents = selected.contains(EXT_TABLE_OF_CONTENTS);

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
