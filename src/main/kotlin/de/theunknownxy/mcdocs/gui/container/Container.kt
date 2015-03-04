package de.theunknownxy.mcdocs.gui.container

import de.theunknownxy.mcdocs.gui.base.Widget
import de.theunknownxy.mcdocs.gui.base.Root

public abstract class Container(root: Root?) : Widget(root) {
    protected abstract fun recalculateChildren()

    override fun onAreaChanged() {
        recalculateChildren()
    }
}