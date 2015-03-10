package de.theunknownxy.mcdocs.gui.base

public class Rectangle(var x: Float, var y: Float, var width: Float, var height: Float) {
    public fun topleft(): Point {
        return Point(x, y)
    }

    public fun bottomright(): Point {
        return Point(x2(), y2())
    }

    public fun x2(): Float {
        return x + width
    }

    public fun y2(): Float {
        return y + height
    }

    public fun contains(point: Point): Boolean {
        return x <= point.x && y <= point.y &&
                point.x <= x2() && point.y <= y2()
    }

    override fun toString(): String {
        return "[${topleft()}-${bottomright()}]"
    }
}