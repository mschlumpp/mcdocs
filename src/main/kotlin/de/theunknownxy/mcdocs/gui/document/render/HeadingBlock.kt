package de.theunknownxy.mcdocs.gui.document.render

import org.lwjgl.opengl.GL11
import de.theunknownxy.mcdocs.docs.HeadingElement
import net.minecraft.client.Minecraft

public class HeadingBlock(val heading: HeadingElement) : Block() {
    private val base_height = 9f
    private fun font_height(): Float = heading_scale() * base_height
    private fun heading_scale(): Float = -0.15f * heading.level + 1.5f

    override var height: Float
        private set(v: Float) {
        }
        get() {
            return font_height()
        }

    override fun draw(x: Float, y: Float, z: Float) {
        GL11.glPushMatrix()
        GL11.glTranslatef(x, y, z)

        // TODO: Scale heading depending on level
        GL11.glScalef(heading_scale(), heading_scale(), 1f)
        val mc = Minecraft.getMinecraft().fontRenderer
        mc.drawString("§n" + heading.text, 0, 0, 0xFFFFFF)

        GL11.glPopMatrix()
    }
}