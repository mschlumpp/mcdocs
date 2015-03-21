package de.theunknownxy.mcdocs.gui.document.segments

import de.theunknownxy.mcdocs.gui.document.Document
import net.minecraft.client.Minecraft

class TextSegment(document: Document, val text: String): Segment(document) {
    override val height: Float = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT.toFloat()

    override fun draw() {
        val fontrenderer = Minecraft.getMinecraft().fontRenderer
        fontrenderer.drawString(text, x.toInt(), y.toInt(), 0xFFFFFF)
    }
}