package de.theunknownxy.mcdocs.gui.container

import de.theunknownxy.mcdocs.gui.base.Widget
import de.theunknownxy.mcdocs.gui.base.Point
import de.theunknownxy.mcdocs.gui.base.Root
import de.theunknownxy.mcdocs.gui.event.MouseButton

public abstract class SingleContainer(root: Root?) : Container(root) {
    public var child: Widget? = null

    override fun onMouseClick(pos: Point, button: MouseButton): Widget? {
        val c = child
        if (c != null && c.rect.contains(pos)) {
            return c.onMouseClick(pos, button)
        }
        return null
    }

    override fun onMouseScroll(pos: Point, wheel: Int) {
        val c = child
        if(c != null && c.rect.contains(pos)) {
            c.onMouseScroll(pos, wheel)
        }
    }

    override fun onMouseClickMove(pos: Point) {
        val c = child
        if(c != null && c.rect.contains(pos)) {
            c.onMouseClickMove(pos)
        }
    }

    override fun onUpdate() {
        child?.onUpdate()
    }

    override fun draw() = child?.draw()
}