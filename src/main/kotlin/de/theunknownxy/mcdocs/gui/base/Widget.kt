package de.theunknownxy.mcdocs.gui.base

import de.theunknownxy.mcdocs.gui.event.MouseButton
import de.theunknownxy.mcdocs.gui.layout.Constraint
import de.theunknownxy.mcdocs.gui.layout.ExpandingPolicy

public abstract class Widget(public var root: Root?) {
    /**
     * Whether on onAreaChanged is called when the dimensions are changed.
     * Calls onAreaChanged() when it is unsuspended
     */
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

    /**
     * Position and Size of the widget as Rectangle
     */
    public var rect: Rectangle
        get() = Rectangle(x, y, width, height)
        set(value) {
            $x = value.x
            $y = value.y
            $width = value.width
            $height = value.height
            areaChanged()
        }

    /**
     * Constraints requested by the widget
     */
    public var constraint: Constraint = Constraint(ExpandingPolicy(1f), ExpandingPolicy(1f))

    private fun areaChanged() {
        if (!suspended) onAreaChanged()
    }

    /**
     * Called when the widget should be drawed.
     */
    public abstract fun draw()

    /**
     * Called when the mouse is clicked within the area of the widget.
     * Returns the widget which was clicked
     */
    public open fun onMouseClick(pos: Point, button: MouseButton): Widget? = this

    /**
     * Called when the mouse wheel is scrolled
     */
    public open fun onMouseScroll(pos: Point, wheel: Int) {
    }

    /**
     * Called when the mouse is moved after it was clicked
     */
    public open fun onMouseClickMove(pos: Point) {
    }

    /**
     * Called when the widget is focused and a key is typed.
     */
    public open fun onKeyTyped(ch: Char, t: Int) {
    }

    /**
     * Called once per tick.
     */
    public open fun onUpdate() {
    }

    /**
     * Called when the was resized or moved.
     */
    protected open fun onAreaChanged() {
    }

    /**
     * Called when the widget is unfocused.
     */
    protected open fun onUnfocus() {
    }
}