package de.theunknownxy.mcdocs.gui.base

import de.theunknownxy.mcdocs.gui.container.SingleContainer
import java.util.HashMap
import de.theunknownxy.mcdocs.gui.event.MouseButton
import net.minecraft.client.gui.GuiScreen

public class Root(public val gui: GuiScreen) : SingleContainer(null) {
    public var mouse_pos: Point = Point(0f, 0f)
    public var named_widgets: HashMap<String, Widget> = HashMap()
    public var selected_widget: Widget? = null

    override fun onMouseClick(pos: Point, button: MouseButton): Widget? {
        val new_selected = super.onMouseClick(pos, button)
        if (new_selected != selected_widget) {
            // Unfocus old one
            selected_widget?.onUnfocus()
            selected_widget = new_selected
        }
        return selected_widget
    }

    override fun onMouseClickMove(pos: Point) {
        // Send the move events to the last clicked widget
        selected_widget?.onMouseClickMove(pos)
    }

    override fun onKeyTyped(ch: Char, t: Int) {
        selected_widget?.onKeyTyped(ch, t)
    }

    override fun recalculateChildren() {
        child?.rect = rect
    }
}