package cz.zcu.kiv.md2odt.filler

/**
 * Wrapper for various forms of input.
 *
 * @version 2017-04-08
 * @author Patrik Harag
 */
interface Source {

    /**
     * Returns source as string.
     *
     * @return source
     */
    String getSource()

    /**
     * Returns object that manages local resources.
     *
     * @return resources
     */
    LocalResources getResources()

}
