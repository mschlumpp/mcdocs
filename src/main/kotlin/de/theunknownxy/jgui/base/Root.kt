package de.theunknownxy.jgui.base

import de.theunknownxy.jgui.container.SingleContainer
import java.util.HashMap

public class Root : SingleContainer() {
    public var named_widgets: HashMap<String, Widget> = HashMap()

    override fun recalculateChildren() {
        child?.area = area
    }
}