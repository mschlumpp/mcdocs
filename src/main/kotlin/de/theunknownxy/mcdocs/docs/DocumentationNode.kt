package de.theunknownxy.mcdocs.docs

import java.util.ArrayList

public data class DocumentationNodeRef(val path: String) {
    public fun components() : Array<String> {
        return path.split("/")
    }

    public fun parent() : DocumentationNodeRef {
        val components = components()
        return DocumentationNodeRef(components.take(components.count() - 1).join("/"))
    }
}

public class DocumentationNode(val path: DocumentationNodeRef, val title: String, val content: Content?) {
    var parent: DocumentationNodeRef? = null
    var children: MutableList<DocumentationNodeRef> = ArrayList()
}