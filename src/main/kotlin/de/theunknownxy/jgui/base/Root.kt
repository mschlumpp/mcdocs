package de.theunknownxy.jgui.base

import de.theunknownxy.jgui.container.SingleContainer
import java.util.HashMap
import de.theunknownxy.jgui.event.MouseButton

public class Root : SingleContainer() {
    public var named_widgets: HashMap<String, Widget> = HashMap()
    public var selected_widget: Widget? = null

    override fun onMouseClick(pos: Point, button: MouseButton): Widget? {
        val new_selected = super.onMouseClick(pos, button)
        if(new_selected != selected_widget) {
            // Unfocus old one
            selected_widget?.onUnfocus()
            selected_widget = new_selected
        }
        return selected_widget
    }

    override fun onKeyTyped(ch: Char, t: Int) {
        selected_widget?.onKeyTyped(ch, t)
    }

    override fun recalculateChildren() {
        child?.rect = rect
    }
}