package de.theunknownxy.mcdocs.docs

import de.theunknownxy.mcdocs.docs.loader.DocumentationLoader
import de.theunknownxy.mcdocs.utils.LRUCache

internal class DocumentationCache(private val documentationLoader: DocumentationLoader) {
    val cache: LRUCache<DocumentationNodeRef, DocumentationNode> = LRUCache(50)

    public fun get(path: DocumentationNodeRef): DocumentationNode {
        if (!cache.containsKey(path)) {
            cache.put(path, documentationLoader.load(path))
        }
        return cache[path]
    }
}