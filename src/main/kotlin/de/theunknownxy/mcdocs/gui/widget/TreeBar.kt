package de.theunknownxy.mcdocs.gui.widget

import de.theunknownxy.mcdocs.gui.base.Widget
import de.theunknownxy.mcdocs.gui.base.Root
import de.theunknownxy.mcdocs.docs.DocumentationBackend
import de.theunknownxy.mcdocs.gui.base.Point
import de.theunknownxy.mcdocs.gui.event.MouseButton
import de.theunknownxy.mcdocs.gui.base.Rectangle
import de.theunknownxy.mcdocs.docs.DocumentationNodeRef

public class TreeBar(root: Root?) : Widget(root) {
    var backend: DocumentationBackend? = null
    var hovered_item: DocumentationNodeRef? = null

    override fun draw() {
        val fontrenderer = root?.gui?.mc?.fontRenderer
        val backend = backend!!
        if (fontrenderer != null) {
            var dy = 4

            fun drawEntry(node: DocumentationNodeRef, text: String, color: Int) {
                var ecol = color
                if (Rectangle(this.x, this.y + dy, this.width, 9f - 1f).contains(root!!.mouse_pos)) {
                    // Highlight item on hover and store it
                    ecol = 0xFFFFFF
                    hovered_item = node
                }
                fontrenderer.drawString(text, this.x.toInt(), (this.y + dy).toInt(), ecol)
                dy += 9
            }

            if (backend.current_path != backend.root) {
                // Draw the up entry
                drawEntry(backend.current_path.parent(), "...", 0xDDDDDD)
            }
            val children = backend.getChildren(backend.current_path)
            for (child in children) {
                val title = backend.getTitle(child)
                drawEntry(child, title, 0xDDDDDD)
                val childchildren = backend.getChildren(child)
                for (childchild in childchildren) {
                    val title = backend.getTitle(childchild)
                    drawEntry(childchild, "  " + title, 0xAAAAAA)
                }
            }
        }
    }

    override fun onMouseClick(pos: Point, button: MouseButton): Widget? {
        if(hovered_item != null) {
            backend?.navigate(hovered_item!!)
        }
        return null
    }
}