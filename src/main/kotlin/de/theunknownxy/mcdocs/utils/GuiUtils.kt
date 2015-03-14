package de.theunknownxy.mcdocs.utils

import net.minecraft.client.renderer.Tessellator

object GuiUtils {
    public fun drawTexturedModalRect(x: Int, y: Int, z: Double, u: Int, v: Int, width: Int, height: Int, texwidth: Int, texheight: Int) {
        val f = 0.00390625.toFloat()
        val f1 = 0.00390625.toFloat()
        val tessellator = Tessellator.instance
        tessellator.startDrawingQuads()
        tessellator.addVertexWithUV((x + 0).toDouble(), (y + height).toDouble(), z, ((u + 0).toFloat() * f).toDouble(), ((v + texheight).toFloat() * f1).toDouble())
        tessellator.addVertexWithUV((x + width).toDouble(), (y + height).toDouble(), z, ((u + texwidth).toFloat() * f).toDouble(), ((v + texheight).toFloat() * f1).toDouble())
        tessellator.addVertexWithUV((x + width).toDouble(), (y + 0).toDouble(), z, ((u + texwidth).toFloat() * f).toDouble(), ((v + 0).toFloat() * f1).toDouble())
        tessellator.addVertexWithUV((x + 0).toDouble(), (y + 0).toDouble(), z, ((u + 0).toFloat() * f).toDouble(), ((v + 0).toFloat() * f1).toDouble())
        tessellator.draw()
    }
}