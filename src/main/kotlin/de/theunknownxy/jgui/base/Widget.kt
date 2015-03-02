package de.theunknownxy.jgui.base

import de.theunknownxy.jgui.container.Container
import de.theunknownxy.jgui.event.MouseButton

public abstract class Widget {
    public var x: Float = 0f
        set(value) {
            $x = value
            onAreaChanged()
        }
    public var y: Float = 0f
        set(value) {
            $y = value
            onAreaChanged()
        }
    public var width: Float = 0f
        set(value) {
            $width = value
            onAreaChanged()
        }
    public var height: Float = 0f
        set(value) {
            $height = value
            onAreaChanged()
        }
    public var rect: Rectangle
        get() = Rectangle(x, y, width, height)
        set(value) {
            $x = value.x
            $y = value.y
            $width = value.width
            $height = value.height
            onAreaChanged()
        }

    public abstract fun draw()
    public open fun onMouseClick(pos: Point, button: MouseButton): Widget? = null
    public open fun onAreaChanged() {}
}