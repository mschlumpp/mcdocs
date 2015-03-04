package de.theunknownxy.jgui.container

import de.theunknownxy.jgui.base.Point
import de.theunknownxy.jgui.event.MouseButton
import de.theunknownxy.jgui.base.Widget
import java.util.ArrayList

public abstract class MultiContainer : Container() {
    // TODO: Override width setter so that width == children.max(fixed? -> width)
    public var children: MutableList<Widget> = ArrayList()

    override fun draw() = children.forEach { w -> w.draw() }
    override fun onMouseClick(pos: Point, button: MouseButton): Widget? = children.firstOrNull { w -> w.rect.contains(pos) }?.onMouseClick(pos, button)
    override fun onUpdate() = children.forEach { w -> w.onUpdate() }
}