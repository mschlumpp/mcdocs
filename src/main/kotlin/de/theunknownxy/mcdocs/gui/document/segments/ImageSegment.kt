package de.theunknownxy.mcdocs.gui.document.segments

import de.theunknownxy.mcdocs.docs.ImageBlock
import de.theunknownxy.mcdocs.gui.document.Document
import de.theunknownxy.mcdocs.utils.GuiUtils
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

class ImageSegment(document: Document, val elem: ImageBlock) : Segment(document) {
    companion object {
        private val PADDING_BOTTOM = 3
    }

    val resloc = ResourceLocation(elem.src)

    override val height: Float
        get() {
            val ratio = elem.height / elem.width.toFloat()
            return ratio * width.toFloat() + PADDING_BOTTOM
        }

    override fun draw() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(resloc)
        GL11.glColor3f(1f, 1f, 1f)
        GuiUtils.drawTexturedModalRect(x.toInt(), y.toInt(), 0.toDouble(), 0, 0, width.toInt(), height.toInt() - PADDING_BOTTOM, elem.width, elem.height)
    }
}