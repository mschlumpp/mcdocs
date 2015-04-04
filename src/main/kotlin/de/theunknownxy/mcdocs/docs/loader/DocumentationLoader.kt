package de.theunknownxy.mcdocs.docs.loader

import de.theunknownxy.mcdocs.docs.DocumentationNode
import de.theunknownxy.mcdocs.docs.DocumentationNodeRef

public trait DocumentationLoader {
    public fun load(ref: DocumentationNodeRef): DocumentationNode
}