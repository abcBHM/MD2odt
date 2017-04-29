package cz.zcu.kiv.md2odt.filler

/**
 * Interface for object that manages local resources.
 * Local resources are served with a source - in a same zip or folder.
 *
 * @version 2017-04-29
 * @author Patrik Harag
 */
interface LocalResources {

    /**
     * Returns resource.
     *
     * @param name name of resource (eg. file name)
     * @return resource or null
     */
    Resource get(String name) throws IOException

}
