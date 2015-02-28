package de.theunknownxy.jgui.container

import de.theunknownxy.jgui.base.Point
import de.theunknownxy.jgui.event.MouseButton
import de.theunknownxy.jgui.base.Widget
import java.util.ArrayList

public abstract class MultiContainer : Container() {
    public var children: MutableList<Widget> = ArrayList()

    override fun draw() {
        for (w in children) {
            w.draw()
        }
    }

    override fun mouseClick(pos: Point, button: MouseButton): Widget? {
        return children.firstOrNull { w -> w.area.contains(pos) }?.mouseClick(pos, button)
    }
}