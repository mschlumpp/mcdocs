package de.theunknownxy.mcdocs.utils

import net.minecraft.client.renderer.Tessellator
import de.theunknownxy.mcdocs.gui.base.Rectangle
import de.theunknownxy.mcdocs.gui.base.BorderImageDescription

object GuiUtils {
    public fun drawTexturedModalRect(x: Int, y: Int, z: Double, u: Int, v: Int, width: Int, height: Int, texwidth: Int = width, texheight: Int = height) {
        if (width == 0 || height == 0) return
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

    public fun drawBox(box: Rectangle, z: Double, descr: BorderImageDescription) {
        drawBox(box, z, descr.texbox, descr.bordertop, descr.borderright, descr.borderbottom, descr.borderleft)
    }

    public fun drawBox(box: Rectangle, z: Double, texbox: Rectangle, bordertop: Int, borderright: Int, borderbottom: Int, borderleft: Int) {
        // Draw topleft corner
        drawTexturedModalRect(box.x.toInt(), box.y.toInt(), z, texbox.x.toInt(), texbox.y.toInt(), borderleft, bordertop)
        // Draw topright corner
        drawTexturedModalRect((box.x2() - borderright).toInt(), box.y.toInt(), z, (texbox.x2() - borderright).toInt(), texbox.y.toInt(), borderright, bordertop)
        // Draw bottomleft corner
        drawTexturedModalRect((box.x.toInt()), (box.y2() - borderbottom).toInt(), z, texbox.x.toInt(), (texbox.y2() - borderbottom).toInt(), borderleft, borderbottom)
        // Draw bottomright corner
        drawTexturedModalRect((box.x2() - borderright).toInt(), (box.y2() - borderbottom).toInt(), z, (texbox.x2() - borderright).toInt(), (texbox.y2() - borderbottom).toInt(), borderright, borderbottom)

        // Draw top and bottom border
        var xhorizontal = box.x + borderleft
        val x2horizontal = box.x2() - borderright
        val stephorizontal = (texbox.width - borderleft - borderright).toInt()
        while (xhorizontal < x2horizontal) {
            val drawwidth = Math.min(stephorizontal, (x2horizontal - xhorizontal).toInt())
            drawTexturedModalRect(xhorizontal.toInt(), box.y.toInt(), z, (texbox.x + borderright).toInt(), texbox.y.toInt(), drawwidth, bordertop)
            drawTexturedModalRect(xhorizontal.toInt(), (box.y2() - borderbottom).toInt(), z, (texbox.x + borderright).toInt(), (texbox.y2() - borderbottom).toInt(), drawwidth, borderbottom)
            xhorizontal += stephorizontal
        }

        // Draw left and right border
        var yleft = box.y + bordertop
        val y2left = box.y2() - borderbottom
        val stepleft = (texbox.height - bordertop - borderbottom).toInt()
        while(yleft < y2left) {
            val drawheight = Math.min(stepleft, (y2left - yleft).toInt())
            drawTexturedModalRect(box.x.toInt(), yleft.toInt(), z, texbox.x.toInt(), (texbox.y + bordertop).toInt(), borderleft, drawheight)
            drawTexturedModalRect((box.x2() - borderleft).toInt(), yleft.toInt(), z, (texbox.x2() - borderright).toInt(), (texbox.x + bordertop).toInt(), borderright, drawheight)
            yleft += stepleft
        }
        // Draw center
        val y2center = box.y2() - borderbottom
        val stepycenter = (texbox.height - bordertop - borderbottom).toInt()
        val x2center = box.x2() - borderright
        val stepxcenter = (texbox.width - borderleft - borderright).toInt()

        var ycenter = box.y + bordertop
        while(ycenter < y2center) {
            var xcenter = box.x + borderleft
            val drawheight = Math.min(stepycenter, (y2center - ycenter).toInt())
            while(xcenter < x2center) {
                val drawwidth = Math.min(stepxcenter, (x2center - xcenter).toInt())
                drawTexturedModalRect(xcenter.toInt(), ycenter.toInt(), z, (texbox.x + borderleft).toInt(), (texbox.y + bordertop).toInt(), drawwidth, drawheight)
                xcenter += stepxcenter
            }
            ycenter += stepycenter
        }
    }
}