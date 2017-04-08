package cz.zcu.kiv.md2odt.filler

/**
 * Wrapper for string input.
 *
 * @version 2017-04-08
 * @author Patrik Harag
 */
class SourceString implements Source {

    final String source

    SourceString(String source) {
        this.source = source
    }

    @Override
    LocalResources getResources() {
        return LocalResourcesImpl.EMPTY
    }
}
