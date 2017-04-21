package cz.zcu.kiv.md2odt.filler

/**
 * Implementation of LocalResources using map.
 *
 * @version 2017-04-21
 * @author Patrik Harag
 */
class LocalResourcesImpl implements LocalResources {

    public static final LocalResources EMPTY = new LocalResourcesImpl([:])


    private final Map<String, byte[]> resources

    LocalResourcesImpl(Map<String, byte[]> resources) {
        this.resources = resources
    }

    @Override
    InputStream get(String name) {
        def value = resources.getOrDefault(name, null)

        if (value)
            return new ByteArrayInputStream(value)

        return null
    }

    @Override
    long getSize(String name) {
        def value = resources.getOrDefault(name, null)

        return value ? value.length : 0
    }
}
