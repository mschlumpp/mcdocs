package de.theunknownxy.mcdocs.gui.widget

import de.theunknownxy.mcdocs.gui.base.*
import de.theunknownxy.mcdocs.gui.base.Range
import de.theunknownxy.mcdocs.gui.event.MouseButton
import de.theunknownxy.mcdocs.utils.GuiUtils
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

public class ScrollWidget(root: Root?, val child: ScrollChild) : Widget(root) {
    companion object {
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

        val SCROLLBUTTON_STEP = 50f
    }

    private var position = 0f
    private var drag_last: Point? = null

    /**
     * Returns the area occupied by the scrollbar.
     */
    private fun scrollbarArea(): Rectangle {
        return Rectangle(x + width - SCROLLER_BAR_WIDTH, y, SCROLLER_BAR_WIDTH.toFloat(), height)
    }

    /**
     * Returns the position and length of the visible view
     */
    private fun contentRange(): Range {
        return Range(position, position + height)
    }

    /**
     * Returns the position and length of the scrollbar in screen coordinates.
     *
     * @note child mustn't be null
     */
    private fun scrollerRangeScreen(): Range {
        val scrollbararea = scrollbarArea()
        val range = contentRange()
        val startrel = range.start / child.getHeight()
        val stoprel = range.stop / child.getHeight()
        return Range(startrel * scrollbararea.height + SCROLLBUTTON_HEIGHT, stoprel * scrollbararea.height - SCROLLBUTTON_HEIGHT)
    }

    /**
     * Returns the area occupied by the scroller.
     */
    private fun scrollerArea(): Rectangle {
        val scrollerrange = scrollerRangeScreen()
        return Rectangle(x + width - SCROLLER_BAR_WIDTH, y + scrollerrange.start, SCROLLER_BAR_WIDTH.toFloat(), Math.max(scrollerrange.distance(), 2 * SCROLLER_TIP_HEIGHT.toFloat()))
    }

    /**
     * Fix the scroller between the top and the bottom
     */
    private fun fixScrollerPosition() {
        var range = contentRange()
        range.moveStop(Math.min(child.getHeight(), range.stop))
        range.moveStart(Math.max(0f, range.start))
        position = range.start
    }

    /**
     * Returns the width of the child
     */
    public fun childWidth(): Float {
        return width - SCROLLER_BAR_WIDTH
    }

    override final fun draw() {
        // Update mouse position in child
        val root = root
        if (root != null) {
            child.mouse_pos = Point(root.mouse_pos.x - x, root.mouse_pos.y - y + position)
        }

        val child = child
        // Draw content
        GL11.glPushMatrix()
        GL11.glTranslatef(x, y - position, 0f)
        child.draw()
        GL11.glPopMatrix()

        if (child.getHeight() > height) {
            // Draw the scrollbar if the content is larger than the view
            drawScrollbar()
        }
    }

    private fun drawScrollbar() {
        GL11.glColor3f(1f, 1f, 1f)
        Minecraft.getMinecraft().getTextureManager().bindTexture(SCROLLER_IMAGE)

        val barleft = scrollbarArea().x.toInt()
        // Draw scroller
        GuiUtils.drawBox(scrollerArea(), 0.toDouble(), SCROLLER_DESCRIPTION)

        // Draw Buttons
        root!!.gui.drawTexturedModalRect(barleft, y.toInt(), 0, 11, SCROLLER_BAR_WIDTH, SCROLLBUTTON_HEIGHT)
        root!!.gui.drawTexturedModalRect(barleft, (y + height - SCROLLBUTTON_HEIGHT).toInt(), 0, 19, SCROLLER_BAR_WIDTH, SCROLLBUTTON_HEIGHT)
    }

    override final fun onMouseClick(pos: Point, button: MouseButton): Widget? {
        val scrollbar_area = scrollbarArea()

        drag_last = null
        if (Rectangle(scrollbar_area.x, scrollbar_area.y, scrollbar_area.width, SCROLLBUTTON_HEIGHT.toFloat()).contains(pos)) {
            // Up button
            position -= SCROLLBUTTON_STEP
        } else if (Rectangle(scrollbar_area.x, scrollbar_area.y2() - SCROLLBUTTON_HEIGHT, scrollbar_area.width, SCROLLBUTTON_HEIGHT.toFloat()).contains(pos)) {
            // Down button
            position += SCROLLBUTTON_STEP
        } else if (scrollerArea().contains(pos)) {
            drag_last = pos
        } else {
            val mp = pos
            mp.x -= this.x
            mp.y -= this.y
            mp.y += position
            child.onMouseClick(mp, button)
        }
        fixScrollerPosition()
        return this
    }

    override fun onUpdate() {
        super.onUpdate()
        fixScrollerPosition()
    }

    override fun onMouseClickMove(pos: Point) {
        val drag_last = drag_last
        if (drag_last != null) {
            position += (pos.y - drag_last.y) / height * child.getHeight()
            this.drag_last = pos
            fixScrollerPosition()
        }
    }

    override fun onMouseScroll(pos: Point, wheel: Int) {
        position -= wheel * 0.3f

        fixScrollerPosition()
    }

    override fun onAreaChanged() {
        child.width = childWidth()
    }
}