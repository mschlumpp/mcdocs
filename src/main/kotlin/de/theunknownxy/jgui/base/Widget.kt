package de.theunknownxy.jgui.base

import de.theunknownxy.jgui.container.Container
import de.theunknownxy.jgui.event.MouseButton

public abstract class Widget {
    public var area: Rectangle = Rectangle(0f, 0f, 0f, 0f)
    public var parent: Container? = null

    public abstract fun draw()
    public open fun mouseClick(pos: Point, button: MouseButton) : Widget? {
        return null
    }

    open fun areaChanged() {}
}