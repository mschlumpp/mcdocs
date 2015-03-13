package de.theunknownxy.mcdocs.gui.document.render

import net.minecraft.util.ResourceLocation
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.Minecraft
import de.theunknownxy.mcdocs.docs.ImageElement
import org.lwjgl.opengl.GL11

public class ImageBlock(val elem: ImageElement) : Block() {
    val resloc = ResourceLocation(elem.src)

    override var width: Float = elem.width.toFloat()
    override var height: Float
        private set(v: Float) {
        }
        get() {
            val ratio = elem.height / elem.width.toFloat()
            return ratio * width.toFloat()
        }

    override fun draw(x: Float, y: Float, z: Float) {
        val v: Float = 0f
        val u: Float = 0f
        val width: Int = width.toInt()
        val height: Int = height.toInt()
        val yi: Int = y.toInt()
        val xi: Int = x.toInt()
        val zLevel: Double = z.toDouble()

        val f = 0.00390625.toFloat()
        val f1 = 0.00390625.toFloat()
        val tessellator = Tessellator.instance

        GL11.glColor3f(1f, 1f, 1f)

        Minecraft.getMinecraft().getTextureManager().bindTexture(resloc)

        tessellator.startDrawingQuads()
        tessellator.addVertexWithUV((xi + 0).toDouble(), (yi + height).toDouble(), zLevel, ((u + 0).toDouble() * f), ((v + elem.height) * f1).toDouble())
        tessellator.addVertexWithUV((xi + width).toDouble(), (yi + height).toDouble(), zLevel, ((u + elem.width).toDouble() * f), ((v + elem.height).toDouble() * f1))
        tessellator.addVertexWithUV((xi + width).toDouble(), (yi + 0).toDouble(), zLevel, ((u + elem.width).toDouble() * f), ((v + 0).toDouble() * f1))
        tessellator.addVertexWithUV((xi + 0).toDouble(), (yi + 0).toDouble(), zLevel, ((u + 0).toDouble() * f), ((v + 0).toDouble() * f1))
        tessellator.draw()
    }
}