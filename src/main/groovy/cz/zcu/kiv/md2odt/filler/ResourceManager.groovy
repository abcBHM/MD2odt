package cz.zcu.kiv.md2odt.filler

/**
 * Manages all resources.
 *
 * @version 2017-04-21
 * @author Patrik Harag
 */
interface ResourceManager {

    /**
     * Returns resource as an input stream.
     *
     * @param uri resource uri
     * @return input stream
     * @throws IOException
     */
    InputStream getResourceAsStream(String uri) throws IOException

}
