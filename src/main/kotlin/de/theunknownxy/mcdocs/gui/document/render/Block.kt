package de.theunknownxy.mcdocs.gui.document.render

public abstract class Block {
    public open var width: Float = 1f
    public abstract var height: Float

    /**
     * Draw the block at the specified coordinates.
     */
    abstract fun draw(x: Float, y: Float, z: Float)
}