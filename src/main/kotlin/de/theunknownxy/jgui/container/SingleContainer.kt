package de.theunknownxy.jgui.container

import de.theunknownxy.jgui.base.Widget
import de.theunknownxy.jgui.base.Point
import de.theunknownxy.jgui.event.MouseButton

public abstract class SingleContainer : Container() {
    public var child: Widget? = null

    override fun mouseClick(pos: Point, button: MouseButton): Widget? {
        val c = child
        if (c != null && c.area.contains(pos)) {
            return c.mouseClick(pos, button)
        }
        return null
    }

    override fun draw() = child?.draw()
}