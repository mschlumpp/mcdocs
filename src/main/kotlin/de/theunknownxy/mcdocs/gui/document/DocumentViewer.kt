package de.theunknownxy.mcdocs.gui.document

import de.theunknownxy.mcdocs.docs.DocumentationBackend
import de.theunknownxy.mcdocs.gui.base.Point
import de.theunknownxy.mcdocs.gui.event.MouseButton
import de.theunknownxy.mcdocs.gui.widget.ScrollChild

public class DocumentViewer() : ScrollChild() {
    companion object {
        private val PADDING_TOP = 9f
        private val PADDING_INNER = 4f
        private val PADDING_RIGHT = 2f
    }

    var backend: DocumentationBackend? = null

    var current_document: Document? = null

    override fun onWidthChanged() {
        current_document?.width = width + PADDING_RIGHT
    }

    override fun getHeight(): Float {
        return current_document?.height ?: 0f
    }

    override fun draw() {
        val backend = backend
        if (backend != null) {
            // Update document if the page changed
            val new_content = backend.getContent(backend.current_page)
            if (new_content != current_document?.content) {
                current_document = Document(backend, new_content)
                onWidthChanged() // Trigger manually a rebuild
            }

            current_document?.draw()
        }
    }

    override fun onMouseClick(pos: Point, button: MouseButton) {
        val backend = backend
        if (backend != null) {
            current_document?.onMouseClick(pos, button)?.run(backend)
        }
    }
}