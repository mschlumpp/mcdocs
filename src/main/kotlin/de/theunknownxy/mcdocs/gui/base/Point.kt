package de.theunknownxy.mcdocs.gui.base

/**
 * Created by marco on 2/28/15.
 */
public class Point(var x: Float, var y: Float) {
    override fun equals(other: Any?): Boolean {
        if (other != null) {
            if (other is Point) {
                return other.x == x && other.y == y
            }
        }
        return false
    }
}