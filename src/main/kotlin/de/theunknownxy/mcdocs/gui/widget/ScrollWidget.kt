package de.theunknownxy.mcdocs.gui.widget

import de.theunknownxy.mcdocs.gui.base.Widget
import de.theunknownxy.mcdocs.gui.base.Root
import de.theunknownxy.mcdocs.gui.base.Point
import de.theunknownxy.mcdocs.gui.event.MouseButton
import de.theunknownxy.mcdocs.gui.base.Rectangle
import de.theunknownxy.mcdocs.utils.GuiUtils
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation

public abstract class ScrollWidget(root: Root?) : Widget(root) {
    class object {
        val SCROLLBAR_WIDTH = 3f
        val CONTROLS_IMAGE = ResourceLocation("mcdocs:textures/gui/controls.png")
        val CONTROL_TIP_HEIGHT = 5
        val CONTROL_BAR_WIDTH = 8
        val CONTROL_MIDDLE_HEIGHT = 1
    }

    private data class Range(var start: Float, var stop: Float) {
        public fun moveStop(pos: Float) {
            val d = pos - stop
            stop = pos
            start += d
        }

        public fun moveStart(pos: Float) {
            val d = pos - start
            start = pos
            stop += d
        }

        public fun distance(): Float = stop - start
    }

    var position = 0f

    /**
     * Returns the position and length of the visible view
     */
    private fun contentRange(): Range {
        return Range(position, position + height)
    }

    /**
     * Returns the position and length of the scrollbar
     */
    private fun scrollbarRangeScreen(): Range {
        val range = contentRange()
        val startrel = range.start / getContentHeight()
        val stoprel = range.stop / getContentHeight()
        return Range(startrel * height, stoprel * height)
    }

    override final fun draw() {
        // Draw content
        GL11.glPushMatrix()
        GL11.glTranslatef(x, y - position, 0f)
        val thisrect = rect
        val clientArea = Rectangle(0f, 0f, thisrect.width, thisrect.height)
        clientArea.width -= SCROLLBAR_WIDTH
        drawContent(clientArea)
        GL11.glPopMatrix()

        // Draw scrollbars
        if (getContentHeight() > height) {
            // Draw the scrollbar if the content is larger than the view
            val scrollbar = scrollbarRangeScreen()
            GL11.glColor3f(1f, 1f, 1f)

            Minecraft.getMinecraft().getTextureManager().bindTexture(CONTROLS_IMAGE)

            val barleft = (thisrect.x2() - SCROLLBAR_WIDTH).toInt()
            GuiUtils.drawTexturedModalRect(barleft, (this.y + scrollbar.start + 5).toInt(), 0.toDouble(), 0, 5, CONTROL_BAR_WIDTH, (scrollbar.distance() - 2 * CONTROL_TIP_HEIGHT + 1).toInt(), CONTROL_BAR_WIDTH, CONTROL_MIDDLE_HEIGHT)
            root!!.gui.drawTexturedModalRect(barleft, (this.y + scrollbar.start).toInt(), 0, 0, CONTROL_BAR_WIDTH, CONTROL_TIP_HEIGHT)
            root!!.gui.drawTexturedModalRect(barleft, (this.y + scrollbar.stop - 5).toInt(), 0, 6, CONTROL_BAR_WIDTH, CONTROL_TIP_HEIGHT)
        }
    }

    override final fun onMouseClick(pos: Point, button: MouseButton): Widget? {
        val mp = pos
        mp.x -= this.x
        mp.y += position
        onContentMouseClick(mp, button)
        return null
    }

    /**
     * Read the Mouse Wheel and scroll the content
     */
    override fun onUpdate() {
        val m = Mouse.getDWheel()
        position -= m * 0.3f

        fixScrollRange()
    }

    /**
     * Fix the scrollbar between the top and the bottom
     */
    private fun fixScrollRange() {
        var range = contentRange()
        range.moveStop(Math.min(getContentHeight(), range.stop))
        range.moveStart(Math.max(0f, range.start))
        position = range.start
    }

    protected abstract fun getContentHeight(): Float
    protected abstract fun drawContent(childArea: Rectangle)
    protected open fun onContentMouseClick(pos: Point, button: MouseButton) {
    }
}