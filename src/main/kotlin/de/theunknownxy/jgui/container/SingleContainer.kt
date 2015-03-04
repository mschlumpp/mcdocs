package de.theunknownxy.jgui.container

import de.theunknownxy.jgui.base.Widget
import de.theunknownxy.jgui.base.Point
import de.theunknownxy.jgui.event.MouseButton

public abstract class SingleContainer : Container() {
    public var child: Widget? = null

    override fun onMouseClick(pos: Point, button: MouseButton): Widget? {
        val c = child
        if (c != null && c.rect.contains(pos)) {
            return c.onMouseClick(pos, button)
        }
        return null
    }

    override fun onUpdate() {
        child?.onUpdate()
    }

    override fun draw() = child?.draw()
}