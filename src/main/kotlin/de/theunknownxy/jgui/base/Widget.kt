package de.theunknownxy.jgui.base

import de.theunknownxy.jgui.container.Container
import de.theunknownxy.jgui.event.MouseButton

public abstract class Widget {
    public var x: Float = 0f
    public var y: Float = 0f
    public var width: Float = 0f
    public var height: Float = 0f
    public var rect: Rectangle
        get() = Rectangle(x, y, width, height)
        set(value) {
            x = value.x
            y = value.y
            width = value.width
            height = value.height
        }

    public var parent: Container? = null

    public abstract fun draw()
    public open fun mouseClick(pos: Point, button: MouseButton): Widget? {
        return null
    }

    open fun areaChanged() {}
}