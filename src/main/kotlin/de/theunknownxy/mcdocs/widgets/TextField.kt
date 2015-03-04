package de.theunknownxy.mcdocs.widgets

import de.theunknownxy.jgui.base.Widget
import net.minecraft.client.gui.GuiTextField
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.Minecraft
import de.theunknownxy.jgui.base.Point
import de.theunknownxy.jgui.event.MouseButton

public class TextField : Widget() {
    var gui: GuiScreen? = null
    private var textfield: GuiTextField? = null // Initialize it lazy
    public var content: String
        set(str: String) {
            lazy_init()
            textfield!!.setText(str)
        }
        get() {
            lazy_init()
            return textfield!!.getText()
        }

    private fun lazy_init() {
        if (textfield == null) {
            textfield = GuiTextField(Minecraft.getMinecraft().fontRenderer, this.x.toInt(), this.y.toInt(), this.width.toInt(), this.height.toInt())
        }
    }

    override fun onMouseClick(pos: Point, button: MouseButton): Widget? {
        textfield?.setFocused(true)
        textfield?.mouseClicked(pos.x.toInt(), pos.y.toInt(), 0)
        return this
    }

    override fun onUnfocus() {
        textfield?.setFocused(false)
    }

    override fun onAreaChanged() {
        lazy_init()
        super.onAreaChanged()
        textfield?.xPosition = this.x.toInt()
        textfield?.yPosition = this.y.toInt()
        textfield?.width = this.width.toInt()
        textfield?.height = this.height.toInt()
    }

    override fun onKeyTyped(ch: Char, t: Int) {
        textfield?.textboxKeyTyped(ch, t)
    }

    override fun onUpdate() {
        textfield?.updateCursorCounter()
    }

    override fun draw() {
        lazy_init()
        textfield?.drawTextBox()
    }
}