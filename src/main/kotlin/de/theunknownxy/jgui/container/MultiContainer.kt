package de.theunknownxy.jgui.container

import de.theunknownxy.jgui.base.Point
import de.theunknownxy.jgui.event.MouseButton
import de.theunknownxy.jgui.base.Widget
import de.theunknownxy.jgui.layout.Constraint
import java.util.HashMap

public abstract class MultiContainer : Container() {
    public var children: MutableMap<Widget, Constraint> = HashMap()

    override fun draw() = children.keySet().forEach { w -> w.draw() }
    override fun mouseClick(pos: Point, button: MouseButton): Widget? = children.keySet().firstOrNull { w -> w.area.contains(pos) }?.mouseClick(pos, button)
}