package de.theunknownxy.mcdocs.gui.widget

import de.theunknownxy.mcdocs.gui.base.Point
import de.theunknownxy.mcdocs.gui.base.Root
import de.theunknownxy.mcdocs.gui.base.Widget
import de.theunknownxy.mcdocs.gui.event.MouseButton
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiTextField

public class TextField(root: Root?) : Widget(root) {
    private var textfield: net.minecraft.client.gui.GuiTextField? = null // Initialize it lazy
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