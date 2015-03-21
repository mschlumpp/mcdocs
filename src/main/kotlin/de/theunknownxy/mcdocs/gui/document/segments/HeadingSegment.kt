package de.theunknownxy.mcdocs.gui.document.segments

import de.theunknownxy.mcdocs.docs.HeadingElement
import de.theunknownxy.mcdocs.gui.document.Document
import net.minecraft.client.Minecraft
import org.lwjgl.opengl.GL11

class HeadingSegment(document: Document, val heading: HeadingElement): Segment(document) {
    private val base_height = 9f
    private fun heading_scale(): Float = -0.15f * heading.level + 1.5f
    private fun font_height(): Float = heading_scale() * base_height

    override val height: Float = font_height() + 2

    override fun draw() {
        GL11.glPushMatrix()
        GL11.glTranslatef(x, y, 0f)

        GL11.glScalef(heading_scale(), heading_scale(), 1f)
        val mc = Minecraft.getMinecraft().fontRenderer
        mc.drawString("§n" + heading.text, 0, 0, 0xFFFFFF)

        GL11.glPopMatrix()
    }
}