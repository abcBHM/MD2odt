package cz.zcu.kiv.md2odt.filler

import java.util.function.Predicate

/**
 * {@link ResourceManager} implementation.
 *
 * @version 2017-04-29
 * @author Patrik Harag
 */
class ResourceManagerImpl implements ResourceManager {

    public static final ResourceManager NO_RESOURCES = new ResourceManagerImpl(
            LocalResourcesImpl.EMPTY,
            { url -> false },
            Long.MAX_VALUE
    )

    private final LocalResources localResources
    private final Predicate<URL> filter
    private final long totalSizeLimit

    private long totalSize = 0

    /**
     * Creates a new instance.
     *
     * @param local local resources
     * @param filter url filter applied on non-local resources
     * @param totalSizeLimit max size (local + other) of resources loaded through
     *      {@link #getResourceAsStream(java.lang.String)} in bytes
     */
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
            resource = loadFromURL(uri)

        if (resource)
            return resource
        else
            throw new IOException("Resource not found: '$uri'")
    }

    private void checkLimit() {
        if (totalSize > totalSizeLimit)
            throw new RuntimeException('Resource limit is reached!')
    }

    private InputStream loadLocal(String uri) {
        if (localResources) {
            def resource = localResources.get(uri)

            if (resource) {
                totalSize += resource.size
                checkLimit()
            }

            return resource?.inputStream
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
            throw new SecurityException("Cannot access resource: '$uri'")
        }
    }

}
