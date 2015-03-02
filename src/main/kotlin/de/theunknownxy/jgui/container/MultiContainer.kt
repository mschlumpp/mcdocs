package de.theunknownxy.jgui.container

import de.theunknownxy.jgui.base.Point
import de.theunknownxy.jgui.event.MouseButton
import de.theunknownxy.jgui.base.Widget
import de.theunknownxy.jgui.layout.Constraint
import java.util.HashMap
import java.util.ArrayList

public abstract class MultiContainer : Container() {
    data class Entry(val widget: Widget, val constraint: Constraint)
    public var children: MutableList<Entry> = ArrayList()

    override fun draw() = children.forEach { w -> w.widget.draw() }
    override fun mouseClick(pos: Point, button: MouseButton): Widget? = children.firstOrNull { w -> w.widget.rect.contains(pos) }?.widget?.mouseClick(pos, button)
}