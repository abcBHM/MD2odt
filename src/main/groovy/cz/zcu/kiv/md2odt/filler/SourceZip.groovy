package cz.zcu.kiv.md2odt.filler

import groovy.transform.CompileStatic

import java.nio.charset.Charset
import java.util.regex.Pattern
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

/**
 * Wrapper for zip input. Zip should contain exactly one source (determined
 * by given pattern) and any number of resources.
 *
 * @version 2017-04-29
 * @author Patrik Harag
 */
class SourceZip implements Source {

    private final InputStream stream
    private final Charset charset
    private final Pattern sourcePattern

    private boolean cached
    private String cachedSource
    private Map<String, byte[]> cachedResources = [:]

    /**
     * Creates a new instance.
     *
     * @param stream input stream
     * @param charset charset of source
     * @param sourcePattern pattern of source
     */
    SourceZip(InputStream stream, Charset charset, Pattern sourcePattern) {
        this.sourcePattern = sourcePattern
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
                    // file
                    byte[] array = asArray(zis, entry.size)

                    if (entry.name.matches(sourcePattern)) {
                        // source file
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

    @CompileStatic
    private static byte[] asArray(InputStream is, long size) {
        byte[] array = new byte[size]
        // read(byte b[]) is broken

        int b
        int index = 0
        while ((b = is.read()) != -1)
            array[index++] = (byte) b

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
