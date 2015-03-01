package de.theunknownxy.mcdocs.widgets

import de.theunknownxy.jgui.base.Widget
import net.minecraft.util.ResourceLocation
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import org.lwjgl.opengl.GL11

class Image : Widget() {
    var gui: Gui? = null
    var tex: ResourceLocation? = null

    override fun draw() {
        if(tex != null && gui != null) {
            GL11.glPushMatrix()
            GL11.glTranslatef(area.x, area.y, 0f)

            val mc = Minecraft.getMinecraft()
            mc.getTextureManager().bindTexture(tex)
            gui!!.drawTexturedModalRect(0, 0, 0, 0, area.width.toInt(), area.height.toInt())

            GL11.glPopMatrix()
        }
    }
}
