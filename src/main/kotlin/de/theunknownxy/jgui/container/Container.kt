package de.theunknownxy.jgui.container

import de.theunknownxy.jgui.base.Widget
import de.theunknownxy.jgui.base.Root

public abstract class Container(root: Root?) : Widget(root) {
    protected abstract fun recalculateChildren()

    override fun onAreaChanged() {
        recalculateChildren()
    }
}