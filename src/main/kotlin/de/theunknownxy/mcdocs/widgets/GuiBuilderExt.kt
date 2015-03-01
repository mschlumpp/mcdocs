package de.theunknownxy.mcdocs.widgets

import de.theunknownxy.jgui.utils.BWidget
import de.theunknownxy.jgui.utils.BContainer
import de.theunknownxy.jgui.utils.root
import net.minecraft.util.ResourceLocation
import net.minecraft.client.gui.Gui

class BImage : BWidget(Image()) {
    fun path(res_path: String) {
        val img = widget as Image
        img.tex = ResourceLocation(res_path)
    }

    fun gui(gui: Gui) {
        val img = widget as Image
        img.gui = gui
    }
}
fun BContainer.image(init: BImage.() -> Unit) = initWidget(BImage(), init)
