package de.theunknownxy.mcdocs.gui.widget

import de.theunknownxy.mcdocs.gui.base.Widget
import de.theunknownxy.mcdocs.gui.base.Root
import de.theunknownxy.mcdocs.gui.base.Point
import de.theunknownxy.mcdocs.gui.event.MouseButton
import de.theunknownxy.mcdocs.gui.base.Rectangle
import de.theunknownxy.mcdocs.gui.base.Range
import de.theunknownxy.mcdocs.utils.GuiUtils
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import de.theunknownxy.mcdocs.gui.base.BorderImageDescription

public abstract class ScrollWidget(root: Root?) : Widget(root) {
    class object {
        val SCROLLER_IMAGE = ResourceLocation("mcdocs:textures/gui/controls.png")
        val SCROLLER_TIP_HEIGHT = 5
        val SCROLLER_BAR_WIDTH = 9
        val SCROLLER_MIDDLE_HEIGHT = 1
        val SCROLLBUTTON_HEIGHT = 8

        val SCROLLER_DESCRIPTION = BorderImageDescription(
                Rectangle(
                        0f, 0f,
                        SCROLLER_BAR_WIDTH.toFloat(),
                        (SCROLLER_TIP_HEIGHT * 2 + SCROLLER_MIDDLE_HEIGHT).toFloat()),
                SCROLLER_TIP_HEIGHT, 0, SCROLLER_TIP_HEIGHT, 0)
    }


    private fun scrollbarArea(): Rectangle {
        return Rectangle(x + width - SCROLLER_BAR_WIDTH, y, SCROLLER_BAR_WIDTH.toFloat(), height)
    }

    private fun scrollerArea(): Rectangle {
        val scrollerrange = scrollerRangeScreen()
        return Rectangle(x + width - SCROLLER_BAR_WIDTH, y + scrollerrange.start, SCROLLER_BAR_WIDTH.toFloat(), Math.max(scrollerrange.distance(), 2 * SCROLLER_TIP_HEIGHT.toFloat()))
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
        val scrollbararea = scrollbarArea()
        val range = contentRange()
        val startrel = range.start / getContentHeight()
        val stoprel = range.stop / getContentHeight()
        return Range(startrel * scrollbararea.height + SCROLLBUTTON_HEIGHT, stoprel * scrollbararea.height - SCROLLBUTTON_HEIGHT)
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
            GL11.glColor3f(1f, 1f, 1f)
            Minecraft.getMinecraft().getTextureManager().bindTexture(SCROLLER_IMAGE)

            val barleft = scrollbarArea().x.toInt()
            // Draw scroller
            GuiUtils.drawBox(scrollerArea(), 0.toDouble(), SCROLLER_DESCRIPTION)

            // Draw Buttons
            root!!.gui.drawTexturedModalRect(barleft, y.toInt(), 0, 11, SCROLLER_BAR_WIDTH, SCROLLBUTTON_HEIGHT)
            root!!.gui.drawTexturedModalRect(barleft, (y + this.height - SCROLLBUTTON_HEIGHT).toInt(), 0, 19, SCROLLER_BAR_WIDTH, SCROLLBUTTON_HEIGHT)
        }
    }

    private var drag_last: Point? = null
    override final fun onMouseClick(pos: Point, button: MouseButton): Widget? {
        // Calculate area of scroller
        val scrollbar_area = scrollbarArea()
        val scroller_area = scrollbarArea()
        val scroller_range = scrollerRangeScreen()
        scroller_area.y += scroller_range.start
        scroller_area.height = scroller_range.distance()

        drag_last = null
        if (Rectangle(scrollbar_area.x, scrollbar_area.y, scrollbar_area.width, SCROLLBUTTON_HEIGHT.toFloat()).contains(pos)) {
            // Up button
            position -= 50f
        } else if (Rectangle(scrollbar_area.x, scrollbar_area.y2() - SCROLLBUTTON_HEIGHT, scrollbar_area.width, SCROLLBUTTON_HEIGHT.toFloat()).contains(pos)) {
            // Down button
            position += 50f
        } else if (scrollerArea().contains(pos)) {
            drag_last = pos
        } else {
            val mp = pos
            mp.x -= this.x
            mp.y += position
            onContentMouseClick(mp, button)
        }
        fixScrollerPosition()
        return this
    }

    override fun onMouseClickMove(pos: Point) {
        if (drag_last != null) {
            position += (pos.y - drag_last!!.y) / height * getContentHeight()
            drag_last = pos
            fixScrollerPosition()
        }
    }

    override fun onMouseScroll(pos: Point, wheel: Int) {
        position -= wheel * 0.3f

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