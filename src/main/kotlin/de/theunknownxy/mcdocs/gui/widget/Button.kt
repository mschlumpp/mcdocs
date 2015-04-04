package de.theunknownxy.mcdocs.gui.widget

import de.theunknownxy.mcdocs.gui.base.Point
import de.theunknownxy.mcdocs.gui.base.Root
import de.theunknownxy.mcdocs.gui.base.Widget
import de.theunknownxy.mcdocs.gui.event.MouseButton
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton

public class Button(root: Root?) : Widget(root) {
    private var button: GuiButton? = null
    public var callback: ((Button) -> Unit)? = null
    public var text: String
        set(str: String) {
            lazy_init()
            button?.displayString = str
        }
        get() {
            lazy_init()
            return button!!.displayString
        }

    private fun lazy_init() {
        if (button == null) {
            button = GuiButton(1234, this.x.toInt(), this.y.toInt(), this.width.toInt(), this.height.toInt(), "")
        }
    }

    override fun draw() {
        lazy_init()
        val mouse_pos = root!!.mouse_pos
        button?.drawButton(Minecraft.getMinecraft(), mouse_pos.x.toInt(), mouse_pos.y.toInt())
    }

    override fun onMouseClick(pos: Point, button: MouseButton): Widget? {
        val mc = Minecraft.getMinecraft()
        if (this.button?.mousePressed(mc, pos.x.toInt(), pos.y.toInt())!!) {
            this.button?.func_146113_a(mc.getSoundHandler())
            callback?.invoke(this)
        }
        return null
    }

    override fun onAreaChanged() {
        lazy_init()
        super.onAreaChanged()
        button?.xPosition = this.x.toInt()
        button?.yPosition = this.y.toInt()
        button?.width = this.width.toInt()
        button?.height = this.height.toInt()
    }
}