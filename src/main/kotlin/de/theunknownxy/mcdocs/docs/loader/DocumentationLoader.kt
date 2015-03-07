package de.theunknownxy.mcdocs.docs.loader

import de.theunknownxy.mcdocs.docs.DocumentationNodeRef
import de.theunknownxy.mcdocs.docs.DocumentationNode

public trait DocumentationLoader {
    public fun load(ref: DocumentationNodeRef) : DocumentationNode
}