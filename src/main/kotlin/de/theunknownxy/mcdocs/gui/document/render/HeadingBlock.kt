package de.theunknownxy.mcdocs.gui.document.render

import org.lwjgl.opengl.GL11
import de.theunknownxy.mcdocs.docs.HeadingElement
import net.minecraft.client.Minecraft

public class HeadingBlock(val heading: HeadingElement) : Block() {
    override var height: Float
        private set(v: Float) {
        }
        get() {
            return 14f
        }

    override fun draw(x: Float, y: Float, z: Float) {
        GL11.glPushMatrix()
        GL11.glTranslatef(x, y, z)

        // TODO: Scale heading depending on level
        GL11.glScalef(1.3f, 1.3f, 1f)
        val mc = Minecraft.getMinecraft().fontRenderer
        mc.drawString("§n" + heading.text, 0, 0, 0xFFFFFF)

        GL11.glPopMatrix()
    }
}