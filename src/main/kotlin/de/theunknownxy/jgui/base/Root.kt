package de.theunknownxy.jgui.base

import de.theunknownxy.jgui.container.SingleContainer
import java.util.HashMap
import de.theunknownxy.jgui.event.MouseButton

public class Root : SingleContainer() {
    public var named_widgets: HashMap<String, Widget> = HashMap()
    public var selected_widget: Widget? = null

    override fun mouseClick(pos: Point, button: MouseButton): Widget? {
        selected_widget = super.mouseClick(pos, button)
        return selected_widget
    }

    override fun recalculateChildren() {
        child?.area = area
        child?.areaChanged()
    }
}