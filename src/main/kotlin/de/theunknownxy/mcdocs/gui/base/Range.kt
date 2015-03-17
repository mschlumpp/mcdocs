package de.theunknownxy.mcdocs.gui.base

public data class Range(var start: Float, var stop: Float) {
    /**
     * Set the stop position and move the start position accordingly
     */
    public fun moveStop(pos: Float) {
        val d = pos - stop
        stop = pos
        start += d
    }

    /**
     * Set the start position and move the stop position accordingly
     */
    public fun moveStart(pos: Float) {
        val d = pos - start
        start = pos
        stop += d
    }

    /**
     * Return the distance between the start and the end point
     */
    public fun distance(): Float = stop - start
}