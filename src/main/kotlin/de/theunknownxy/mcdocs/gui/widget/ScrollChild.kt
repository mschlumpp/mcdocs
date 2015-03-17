package de.theunknownxy.mcdocs.gui.widget

import de.theunknownxy.mcdocs.gui.base.Point
import de.theunknownxy.mcdocs.gui.event.MouseButton

public abstract class ScrollChild {
    public var mouse_pos: Point = Point(0f, 0f)

    public var width: Float = 0f
        set(v: Float) {
            $width = v
            onWidthChanged()
        }

    /**
     * Ask the child how tall it is.
     */
    abstract fun getHeight(): Float

    /**
     * Draw the child.
     */
    abstract fun draw()

    /**
     * Called when a mouse button is pressed.
     */
    open fun onMouseClick(pos: Point, button: MouseButton) {
    }

    /**
     * Called when the width was changed.
     */
    open fun onWidthChanged() {
    }
}