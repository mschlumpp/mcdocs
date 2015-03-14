package de.theunknownxy.mcdocs

import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.ScaledResolution
import de.theunknownxy.mcdocs.gui.base.Root
import de.theunknownxy.mcdocs.gui.event.MouseButton
import de.theunknownxy.mcdocs.gui.base.Point
import de.theunknownxy.mcdocs.gui.base.Rectangle
import org.lwjgl.opengl.GL11

public abstract class UnscaledWidgetGui : GuiScreen() {
    protected var root: Root? = null

    private var old_res_width: Int = 0
    private var old_res_height: Int = 0
    private var guiScale = 1f

    override fun mouseClickMove(p_146273_1_: Int, p_146273_2_: Int, p_146273_3_: Int, p_146273_4_: Long) {
        root?.onMouseClickMove(Point(p_146273_1_.toFloat() * (1 / guiScale), p_146273_2_.toFloat() * (1 / guiScale)))
    }

    override fun mouseClicked(x: Int, y: Int, par3: Int) {
        // Send mouse click event to the GUI framework
        val button = when (par3) {
            0 -> MouseButton.LEFT
            1 -> MouseButton.RIGHT
            2 -> MouseButton.MIDDLE
            else -> MouseButton.MIDDLE
        }
        root?.onMouseClick(Point(x.toFloat() * (1 / guiScale), y.toFloat() * (1 / guiScale)), button)
    }

    override fun keyTyped(p_73869_1_: Char, p_73869_2_: Int) {
        root?.onKeyTyped(p_73869_1_, p_73869_2_)
        if (p_73869_2_ == 1) {
            this.mc.displayGuiScreen(null)
        }
    }

    override fun updateScreen() {
        root?.onUpdate()
    }

    override fun drawScreen(par1: Int, par2: Int, par3: Float) {
        this.drawDefaultBackground()

        if (old_res_width != mc.displayWidth || old_res_height != mc.displayHeight) {
            // Recalculate scale
            old_res_width = mc.displayWidth
            old_res_height = mc.displayHeight
            guiScale = ScaledResolution(mc, old_res_width, old_res_height).getScaleFactor().toFloat()
            guiScale = 1 / guiScale
            guiScale *= 2f
            // Set root dimensions
            root?.rect = Rectangle(0f, 0f, this.width.toFloat() * 1 / guiScale, this.height.toFloat() * 1 / guiScale)
        }

        // Update Root
        root?.mouse_pos = Point(par1.toFloat() * (1 / guiScale), par2.toFloat() * (1 / guiScale))

        // Draw the "unscaled" gui
        GL11.glPushMatrix()
        GL11.glScalef(guiScale, guiScale, 1f)
        root?.draw()
        GL11.glPopMatrix()
    }
}