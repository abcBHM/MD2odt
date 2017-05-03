package cz.zcu.kiv.md2odt.filler

import groovy.transform.CompileStatic
import org.apache.commons.io.IOUtils

import java.nio.charset.Charset
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
                byte[] array = IOUtils.toByteArray(is)
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
