package cz.zcu.kiv.md2odt.filler

import cz.zcu.kiv.md2odt.filler.LocalResources
import cz.zcu.kiv.md2odt.filler.LocalResourcesImpl
import cz.zcu.kiv.md2odt.filler.Source
import groovy.transform.CompileStatic

import java.nio.charset.Charset
import java.nio.file.Files
import java.util.regex.Pattern

/**
 * Wrapper for folder input. Folder should contain exactly one source (determined
 * by given pattern) and any number of resources.
 *
 * @version 2017-04-29
 * @author Vít Mazín
 */
class SourceFolder implements Source {

    private final File folder
    private final Charset charset
    private final Pattern sourcePattern

    private boolean cached = false
    private String cachedSource
    private Map<String, byte[]> cachedResources = [:]

    /**
     * Creates a new instance.
     *
     * @param folder folder with source file and resources
     * @param charset charset of source
     * @param sourcePattern pattern of source
     */
    SourceFolder(File folder, Charset charset, Pattern pattern) {
        this.folder = folder
        this.charset = charset
        this.sourcePattern = pattern
    }

    private void load() {
        cached = true

        ArrayDeque<File> deque = new ArrayDeque<>()

        deque.push(folder)

        while (!deque.isEmpty()) {
            File file = deque.pop()

            if(file.isDirectory()) {
                for (File f : file.listFiles()) {
                    deque.push(f)
                }
            } else {
                InputStream is = new FileInputStream(file)
                byte[] array = asArray(is, file.size())
                is.close()

                if(file.getName().matches(sourcePattern)) {
                    if(cachedSource)
                        throw new RuntimeException('More source files found!')

                    cachedSource = new String(array, charset)
                } else {
                    cachedResources.put(file.name, array)
                }
            }
        }

        if(cachedSource == null)
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
