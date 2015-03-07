package de.theunknownxy.mcdocs.docs

import de.theunknownxy.mcdocs.docs.loader.DocumentationLoader

public class DocumentationBackend(loader: DocumentationLoader) {
    var cache: DocumentationCache = DocumentationCache(loader)

    var root: DocumentationNode? = null
    var current_path: DocumentationNode? = null
    var current_page: DocumentationNode? = null

    public fun getContent(path: DocumentationNodeRef): Content {
        return cache[path].content
    }

    public fun getTitle(path: DocumentationNodeRef) : String {
        return cache[path].title
    }

    public fun getChildren(path: DocumentationNodeRef): List<DocumentationNodeRef> {
        return cache[path].children
    }
}

