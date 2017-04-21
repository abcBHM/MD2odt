package cz.zcu.kiv.md2odt.filler

import java.util.function.Predicate

/**
 *
 * @version 2017-04-21
 * @author Patrik Harag
 */
class ResourceManagerImpl implements ResourceManager {

    public static final ResourceManager NO_RESOURCES = new ResourceManagerImpl(
            LocalResourcesImpl.EMPTY, { url -> false }, Long.MAX_VALUE)

    private final LocalResources localResources
    private final Predicate<URL> filter
    private final long totalSizeLimit

    private long totalSize = 0

    ResourceManagerImpl(LocalResources local, Predicate<URL> filter, long totalSizeLimit) {
        this.totalSizeLimit = totalSizeLimit
        this.filter = filter
        this.localResources = local
    }

    @Override
    InputStream getResourceAsStream(String uri) throws IOException {
        if (totalSize > totalSizeLimit)
            return null

        def resource = loadLocal(uri)

        if (!resource)
            return loadFromURL(uri)

        return resource
    }

    private void checkLimit() {
        if (totalSize > totalSizeLimit)
            throw new RuntimeException('Resource limit is reached!')
    }

    private InputStream loadLocal(String uri) {
        if (localResources) {
            def stream = localResources.get(uri)
            def size = localResources.getSize(uri)

            if (stream) {
                totalSize += size
                checkLimit()
            }

            return stream
        }
    }

    private InputStream loadFromURL(String uri) {
        URL resourceUrl = new URL(uri)

        if (filter.test(resourceUrl)) {
            // TODO: something faster

            def buffer = new ByteArrayOutputStream()
            def inputStream = resourceUrl.openStream()

            int b
            while ((b = inputStream.read()) != -1) {
                buffer.write(b)
                totalSize++
                checkLimit()
            }

            return new ByteArrayInputStream(buffer.toByteArray())

        } else {
            throw new SecurityException("Cannot access resource")
        }
    }

}
