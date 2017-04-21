package cz.zcu.kiv.md2odt.filler

/**
 * Interface for object that manages local resources.
 *
 * @version 2017-04-21
 * @author Patrik Harag
 */
interface LocalResources {

    /**
     * Returns input stream.
     *
     * @param name name of resource (eg. file name)
     * @return input stream or null
     */
    InputStream get(String name)

    /**
     * Returns size of a resource.
     *
     * @param name name of resource (eg. file name)
     * @return size or 0
     */
    long getSize(String name)

}
