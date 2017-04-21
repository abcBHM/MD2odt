package cz.zcu.kiv.md2odt.filler

/**
 *
 * @version 2017-04-21
 * @author Patrik Harag
 */
interface ResourceManager {

    InputStream getResourceAsStream(String uri) throws IOException

}
