package de.theunknownxy.mcdocs.gui.base

import de.theunknownxy.mcdocs.gui.event.MouseButton
import de.theunknownxy.mcdocs.gui.layout.Constraint
import de.theunknownxy.mcdocs.gui.layout.ExpandingPolicy

public abstract class Widget(public var root: Root?) {
    public var suspended: Boolean = false
        set(value) {
            $suspended = value
            if (!value) areaChanged()
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
    public var constraint: Constraint = Constraint(ExpandingPolicy(1f), ExpandingPolicy(1f))

    protected fun areaChanged() {
        if (!suspended) onAreaChanged()
    }

    public abstract fun draw()
    public open fun onMouseClick(pos: Point, button: MouseButton): Widget? = null
    public open fun onKeyTyped(ch: Char, t: Int) {
    }

    public open fun onUpdate() {
    }

    protected open fun onAreaChanged() {
    }

    protected open fun onUnfocus() {
    }
}