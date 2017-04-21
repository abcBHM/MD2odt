package cz.zcu.kiv.md2odt.filler

import java.util.function.Predicate

/**
 *
 * @version 2017-04-21
 * @author Patrik Harag
 */
class ResourceManagerImpl implements ResourceManager {

    public static final ResourceManager NO_RESOURCES = new ResourceManagerImpl(
            LocalResourcesImpl.EMPTY, { url -> false })

    private final LocalResources localResources
    private final Predicate<URL> filter

    ResourceManagerImpl(LocalResources localResources, Predicate<URL> filter) {
        this.filter = filter
        this.localResources = localResources
    }

    @Override
    InputStream getResourceAsStream(String uri) throws IOException {
        def localResourceStream = localResources ? localResources.get(uri) : null

        if (localResourceStream) {
            return localResourceStream

        } else {
            URL resourceUrl = new URL(uri)

            if (filter.test(resourceUrl)) {
                return resourceUrl.openStream()

            } else {
                throw new SecurityException("Cannot access resource")
            }
        }
    }
}
