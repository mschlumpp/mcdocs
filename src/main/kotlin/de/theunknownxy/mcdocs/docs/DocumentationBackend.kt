package de.theunknownxy.mcdocs.docs

import de.theunknownxy.mcdocs.docs.loader.DocumentationLoader

public class DocumentationBackend(loader: DocumentationLoader) {
    var cache: DocumentationCache = DocumentationCache(loader)

    var root: DocumentationNodeRef = DocumentationNodeRef("")
    var current_path: DocumentationNodeRef = root
    var current_page: DocumentationNodeRef = root

    public fun navigate(path: DocumentationNodeRef) {
        val node = cache[path]
        if(node.children.size() > 0) {
            current_path = node.path
        }
        if(node.content != null) {
            current_page = path
        }
    }

    public fun getContent(path: DocumentationNodeRef): Content {
        val content = cache[path].content
        if(content != null) {
            return content
        } else {
            return Content()
        }
    }

    public fun getTitle(path: DocumentationNodeRef) : String {
        return cache[path].title
    }

    public fun getChildren(path: DocumentationNodeRef): List<DocumentationNodeRef> {
        return cache[path].children
    }
}

