package de.theunknownxy.jgui.container

import de.theunknownxy.jgui.base.Point
import de.theunknownxy.jgui.event.MouseButton
import de.theunknownxy.jgui.base.Widget
import java.util.ArrayList
import de.theunknownxy.jgui.layout.Constraint
import java.util.HashMap

public abstract class MultiContainer : Container() {
    public var children: MutableMap<Widget, Constraint> = HashMap()

    override fun draw() {
        for (w in children.keySet()) {
            w.draw()
        }
    }

    override fun mouseClick(pos: Point, button: MouseButton): Widget? {
        return children.keySet().firstOrNull { w -> w.area.contains(pos) }?.mouseClick(pos, button)
    }
}