package cz.zcu.kiv.md2odt.filler

/**
 * Implementation of LocalResources using map of byte arrays.
 *
 * @version 2017-04-29
 * @author Patrik Harag
 */
class LocalResourcesImpl implements LocalResources {

    public static final LocalResources EMPTY = new LocalResourcesImpl([:])


    private final Map<String, byte[]> resources

    /**
     * Creates a new instance.
     *
     * @param resources resources
     */
    LocalResourcesImpl(Map<String, byte[]> resources) {
        this.resources = resources
    }

    @Override
    Resource get(String name) {
        def array = resources.getOrDefault(name, null)

        if (array) {
            def stream = new ByteArrayInputStream(array)
            def size = array.length
            return new Resource(inputStream: stream, size: size)
        }

        return null
    }

}
