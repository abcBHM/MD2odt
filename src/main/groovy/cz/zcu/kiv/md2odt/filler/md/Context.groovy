package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ast.Document as AstDocument
import com.vladsch.flexmark.ast.Reference as AstReference
import groovy.transform.Immutable

/**
 * Holds information about processed input.
 *
 * @version 2017-04-02
 * @author Patrik Harag
 */
@Immutable
class Context {

    private Map<String, AstReference> references

    /**
     * Returns reference node by name.
     *
     * @param name reference name
     * @return reference node
     */
    AstReference getReference(String name) {
        references.getOrDefault(name, null)
    }


    static Context of(AstDocument node) {
        def refs = collectRefs(node)
        return new Context(references: refs)
    }

    private static Map<String, AstReference> collectRefs(AstDocument node) {
        node.children
                .findAll { it instanceof AstReference}
                .collect { it as AstReference }
                .collectEntries { [(it.reference.toString()): it] }
    }

}
