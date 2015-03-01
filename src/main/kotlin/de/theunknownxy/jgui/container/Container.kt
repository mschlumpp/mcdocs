package de.theunknownxy.jgui.container

import de.theunknownxy.jgui.base.Widget

public abstract class Container : Widget() {
    public var suspend: Boolean = false

    protected abstract fun recalculateChildren()

    override fun areaChanged() {
        recalculateChildren()
    }
}