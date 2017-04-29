package cz.zcu.kiv.md2odt.filler.md

import com.vladsch.flexmark.ast.Document as AstDocument
import com.vladsch.flexmark.ast.Reference as AstReference
import cz.zcu.kiv.md2odt.filler.ResourceManager
import cz.zcu.kiv.md2odt.filler.ResourceManagerImpl
import groovy.transform.Immutable
import groovy.transform.PackageScope

/**
 * Holds information about processed input.
 *
 * @version 2017-04-21
 * @author Patrik Harag
 */
@Immutable
@PackageScope
class Context {

    private Map<String, AstReference> references
    private ResourceManager resourceManager

    /**
     * Returns reference node by name.
     *
     * @param name reference name
     * @return reference node
     */
    AstReference getReference(String name) {
        references.getOrDefault(name, null)
    }

    /**
     * Returns resource as an input stream.
     *
     * @param uri resource uri
     * @return input stream
     * @throws IOException
     */
    InputStream getResourceAsStream(String uri) throws IOException {
        resourceManager.getResourceAsStream(uri)
    }


    static Context of(AstDocument node) {
        return of(node, ResourceManagerImpl.NO_RESOURCES)
    }

    static Context of(AstDocument node, ResourceManager resourceManager) {
        def refs = collectRefs(node)
        return new Context(references: refs, resourceManager: resourceManager)
    }

    private static Map<String, AstReference> collectRefs(AstDocument node) {
        node.children
                .findAll { it instanceof AstReference}
                .collect { it as AstReference }
                .collectEntries { [(it.reference.toString()): it] }
    }

}
