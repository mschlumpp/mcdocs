package de.theunknownxy.mcdocs.gui.document.segments

import de.theunknownxy.mcdocs.gui.base.Rectangle
import de.theunknownxy.mcdocs.gui.document.Document

/**
 * A single segment of a rendered document.
 */
abstract class Segment(val document: Document) {
    /**
     * x position of the segment in document local coordinates
     */
    public var x: Float = 0f
    /**
     * y position of the segment in document local coordinates
     */
    public var y: Float = 0f

    public open var width: Float = 0f
    public abstract val height: Float

    public fun rect(): Rectangle = Rectangle(x, y, width, height)

    public abstract fun draw()
}