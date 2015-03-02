package de.theunknownxy.jgui.container

import de.theunknownxy.jgui.base.Widget
import de.theunknownxy.jgui.base.Point
import de.theunknownxy.jgui.event.MouseButton
import de.theunknownxy.jgui.layout.Constraint

public abstract class SingleContainer : Container() {
    public var child: Widget? = null
    public var constraint: Constraint? = null

    override fun mouseClick(pos: Point, button: MouseButton): Widget? {
        val c = child
        if (c != null && c.rect.contains(pos)) {
            return c.mouseClick(pos, button)
        }
        return null
    }

    override fun draw() = child?.draw()
}