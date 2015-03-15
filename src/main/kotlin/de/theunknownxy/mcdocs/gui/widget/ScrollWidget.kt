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
        val SCROLLER_IMAGE = ResourceLocation("mcdocs:textures/gui/controls.png")
        val SCROLLER_TIP_HEIGHT = 5
        val SCROLLER_BAR_WIDTH = 8
        val SCROLLER_MIDDLE_HEIGHT = 1
    }

    private data class Range(var start: Float, var stop: Float) {
        /**
         * Set the stop position and move the start position accordingly
         */
        public fun moveStop(pos: Float) {
            val d = pos - stop
            stop = pos
            start += d
        }

        /**
         * Set the start position and move the stop position accordingly
         */
        public fun moveStart(pos: Float) {
            val d = pos - start
            start = pos
            stop += d
        }

        /**
         * Return the distance between the start and the end point
         */
        public fun distance(): Float = stop - start
    }

    private fun scrollbarArea(): Rectangle {
        return Rectangle(x + width - SCROLLER_BAR_WIDTH, y, SCROLLER_BAR_WIDTH.toFloat(), height)
    }

    var position = 0f

    /**
     * Returns the position and length of the visible view
     */
    private fun contentRange(): Range {
        return Range(position, position + height)
    }

    /**
     * Returns the position and length of the scrollbar in screen coordinates
     */
    private fun scrollerRangeScreen(): Range {
        val range = contentRange()
        val startrel = range.start / getContentHeight()
        val stoprel = range.stop / getContentHeight()
        return Range(startrel * height, stoprel * height)
    }

    public fun contentWidth(): Float {
        return width - SCROLLER_BAR_WIDTH
    }

    override final fun draw() {
        // Draw content
        GL11.glPushMatrix()
        GL11.glTranslatef(x, y - position, 0f)
        val thisrect = rect
        val clientArea = Rectangle(0f, 0f, thisrect.width, thisrect.height)
        clientArea.width -= SCROLLER_BAR_WIDTH
        drawContent(clientArea)
        GL11.glPopMatrix()

        // Draw scrollbar
        if (getContentHeight() > height) {
            // Draw the scrollbar if the content is larger than the view
            val scrollbar = scrollerRangeScreen()
            GL11.glColor3f(1f, 1f, 1f)

            Minecraft.getMinecraft().getTextureManager().bindTexture(SCROLLER_IMAGE)

            val barleft = scrollbarArea().x.toInt()
            GuiUtils.drawTexturedModalRect(barleft, (y + scrollbar.start + SCROLLER_TIP_HEIGHT).toInt(), 0.toDouble(), 0, 5, SCROLLER_BAR_WIDTH, (scrollbar.distance() - 2 * SCROLLER_TIP_HEIGHT + 1).toInt(), SCROLLER_BAR_WIDTH, SCROLLER_MIDDLE_HEIGHT)
            root!!.gui.drawTexturedModalRect(barleft, (y + scrollbar.start).toInt(), 0, 0, SCROLLER_BAR_WIDTH, SCROLLER_TIP_HEIGHT)
            root!!.gui.drawTexturedModalRect(barleft, (y + scrollbar.stop - SCROLLER_TIP_HEIGHT).toInt(), 0, 6, SCROLLER_BAR_WIDTH, SCROLLER_TIP_HEIGHT)
        }
    }

    private var drag_last: Point? = null
    override final fun onMouseClick(pos: Point, button: MouseButton): Widget? {
        // Calculate area of scroller
        val scroller_area = scrollbarArea()
        val scroller_range = scrollerRangeScreen()
        scroller_area.y += scroller_range.start
        scroller_area.height = scroller_range.distance()

        drag_last = null
        if (scroller_area.contains(pos)) {
            drag_last = pos
        } else {
            val mp = pos
            mp.x -= this.x
            mp.y += position
            onContentMouseClick(mp, button)
        }
        return this
    }

    override fun onMouseClickMove(pos: Point) {
        if(drag_last != null) {
            position += (pos.y - drag_last!!.y) / height * getContentHeight()
            drag_last = pos
        }
    }

    /**
     * Read the Mouse Wheel and scroll the content
     */
    override fun onUpdate() {
        val m = Mouse.getDWheel()
        position -= m * 0.3f

        fixScrollerPosition()
    }

    /**
     * Fix the scroller between the top and the bottom
     */
    private fun fixScrollerPosition() {
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