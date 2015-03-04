package de.theunknownxy.mcdocs.gui.container

import de.theunknownxy.mcdocs.gui.base.Point
import de.theunknownxy.mcdocs.gui.event.MouseButton
import de.theunknownxy.mcdocs.gui.base.Widget
import java.util.ArrayList
import de.theunknownxy.mcdocs.gui.base.Root

public abstract class MultiContainer(root: Root?) : Container(root) {
    // TODO: Override width setter so that width == children.max(fixed? -> width)
    public var children: MutableList<Widget> = ArrayList()

    override fun draw() = children.forEach { w -> w.draw() }
    override fun onMouseClick(pos: Point, button: MouseButton): Widget? = children.firstOrNull { w -> w.rect.contains(pos) }?.onMouseClick(pos, button)
    override fun onUpdate() = children.forEach { w -> w.onUpdate() }
}