package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ast.Document as AstDocument
import com.vladsch.flexmark.ast.Reference as AstReference
import cz.zcu.kiv.md2odt.filler.LocalResources
import cz.zcu.kiv.md2odt.filler.LocalResourcesImpl
import groovy.transform.Immutable

/**
 * Holds information about processed input.
 *
 * @version 2017-04-08
 * @author Patrik Harag
 */
@Immutable
class Context {

    private Map<String, AstReference> references
    private LocalResources resources

    /**
     * Returns reference node by name.
     *
     * @param name reference name
     * @return reference node
     */
    AstReference getReference(String name) {
        references.getOrDefault(name, null)
    }

    InputStream getResourceAsStream(String name) {
        resources ? resources.get(name) : null
    }


    static Context of(AstDocument node) {
        return of(node, LocalResourcesImpl.EMPTY)
    }

    static Context of(AstDocument node, LocalResources resources) {
        def refs = collectRefs(node)
        return new Context(references: refs, resources: resources)
    }

    private static Map<String, AstReference> collectRefs(AstDocument node) {
        node.children
                .findAll { it instanceof AstReference}
                .collect { it as AstReference }
                .collectEntries { [(it.reference.toString()): it] }
    }

}
