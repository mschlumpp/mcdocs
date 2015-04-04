package de.theunknownxy.mcdocs.gui.document

import de.theunknownxy.mcdocs.docs.DocumentationBackend
import de.theunknownxy.mcdocs.docs.DocumentationNodeRef

trait Action {
    fun run(backend: DocumentationBackend)
}

class NavigateAction(val ref: DocumentationNodeRef) : Action {
    override fun run(backend: DocumentationBackend) {
        backend.navigate(ref)
    }
}

