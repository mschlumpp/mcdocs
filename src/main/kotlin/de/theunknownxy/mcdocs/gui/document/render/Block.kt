package de.theunknownxy.mcdocs.gui.document.render

import de.theunknownxy.mcdocs.gui.base.Point
import de.theunknownxy.mcdocs.gui.event.MouseButton

public abstract class Block {
    public open var width: Float = 1f
    public abstract var height: Float

    /**
     * Draw the block at the specified coordinates.
     */
    abstract fun draw(x: Float, y: Float, z: Float)

    /**
     * Called when the mouse was clicked within the block
     */
    open fun onMouseClick(pos: Point, button: MouseButton) {
    }
}