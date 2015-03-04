package de.theunknownxy.mcdocs.gui.widget

import de.theunknownxy.mcdocs.gui.base.Widget
import de.theunknownxy.mcdocs.gui.base.Root
import net.minecraft.util.ResourceLocation
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL11

class Image(root: Root?) : Widget(root) {
    var tex: ResourceLocation? = null

    override fun draw() {
        if (tex != null) {
            GL11.glPushMatrix()
            GL11.glTranslatef(x, y, 0f)

            val mc = Minecraft.getMinecraft()
            mc.getTextureManager().bindTexture(tex)
            root!!.gui.drawTexturedModalRect(0, 0, 0, 0, width.toInt(), height.toInt())

            GL11.glPopMatrix()
        }
    }
}