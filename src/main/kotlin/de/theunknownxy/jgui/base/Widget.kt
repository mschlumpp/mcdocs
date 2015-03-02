package de.theunknownxy.jgui.base

import de.theunknownxy.jgui.container.Container
import de.theunknownxy.jgui.event.MouseButton

public abstract class Widget {
    public var suspended: Boolean = false
        set(value) {
            $suspended = value
            if(!value) areaChanged()
        }
    public var x: Float = 0f
        set(value) {
            $x = value
            areaChanged()
        }
    public var y: Float = 0f
        set(value) {
            $y = value
            areaChanged()
        }
    public var width: Float = 0f
        set(value) {
            $width = value
            areaChanged()
        }
    public var height: Float = 0f
        set(value) {
            $height = value
            areaChanged()
        }
    public var rect: Rectangle
        get() = Rectangle(x, y, width, height)
        set(value) {
            $x = value.x
            $y = value.y
            $width = value.width
            $height = value.height
            areaChanged()
        }

    protected fun areaChanged() {
        if(!suspended) onAreaChanged()
    }

    public abstract fun draw()
    public open fun onMouseClick(pos: Point, button: MouseButton): Widget? = null
    protected open fun onAreaChanged() {}
}