package cz.zcu.kiv.md2odt.filler

import java.nio.charset.Charset
import java.util.regex.Pattern
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

/**
 * Wrapper for zip input.
 *
 * @version 2017-04-08
 * @author Patrik Harag
 */
class SourceZip implements Source {

    public static final Pattern SOURCE_FILE_PATTERN = Pattern.compile('.*\\.md')

    private InputStream stream
    private Charset charset

    private boolean cached
    private String cachedSource
    private Map<String, byte[]> cachedResources = [:]

    SourceZip(InputStream stream, Charset charset) {
        this.charset = charset
        this.stream = stream
    }

    private void load() {
        cached = true

        new ZipInputStream(stream).withStream { ZipInputStream zis ->
            ZipEntry entry

            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    // directory

                } else {
                    byte[] array = asArray(zis, entry.size)

                    if (entry.name.matches(SOURCE_FILE_PATTERN)) {
                        // md
                        if (cachedSource)
                            throw new RuntimeException('More source files found!')

                        cachedSource = new String(array, charset)

                    } else {
                        // resource
                        cachedResources.put(entry.name, array)
                    }
                }
                zis.closeEntry()
            }
        }

        if (cachedSource == null)
            throw new RuntimeException('Source file not found!')
    }

    private static byte[] asArray(InputStream is, long size) {
        byte[] array = new byte[size]

        byte b
        int index = 0
        while ((b = is.read()) != -1)
            array[index++] = b

        return array
    }

    @Override
    String getSource() {
        if (!cached) load()

        return cachedSource
    }

    @Override
    LocalResources getResources() {
        if (!cached) load()

        return new LocalResourcesImpl(cachedResources)
    }
}
