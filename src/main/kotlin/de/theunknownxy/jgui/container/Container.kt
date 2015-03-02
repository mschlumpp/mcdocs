package de.theunknownxy.jgui.container

import de.theunknownxy.jgui.base.Widget

public abstract class Container : Widget() {
    protected abstract fun recalculateChildren()

    override fun onAreaChanged() {
        recalculateChildren()
    }
}