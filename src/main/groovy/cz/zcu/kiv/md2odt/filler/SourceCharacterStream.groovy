package cz.zcu.kiv.md2odt.filler

import java.nio.charset.Charset

/**
 * Wrapper for stream input.
 *
 * @version 2017-04-08
 * @author Patrik Harag
 */
class SourceCharacterStream implements Source {

    final InputStream inputStream
    final Charset charset

    private String cache

    SourceCharacterStream(InputStream inputStream, Charset charset) {
        this.inputStream = inputStream
        this.charset = charset
    }

    @Override
    String getSource() {
        if (cache)
            return cache
        else
            return cache = asString(inputStream, charset)
    }

    private static String asString(InputStream i, Charset charset) {
        return new InputStreamReader(i, charset).text
    }

    @Override
    LocalResources getResources() {
        return LocalResourcesImpl.EMPTY
    }
}