package de.theunknownxy.mcdocs.widgets

import de.theunknownxy.jgui.utils.BWidget
import de.theunknownxy.jgui.utils.BContainer
import net.minecraft.util.ResourceLocation
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.GuiScreen

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

class BTextField : BWidget(TextField()) {
    fun gui(gui: GuiScreen) {
        val field = widget as TextField
        field.gui = gui
    }

    fun content(str: String) {
        val field = widget as TextField
        field.content = str
    }
}

fun BContainer.image(init: BImage.() -> Unit) = initWidget(BImage(), init)
fun BContainer.textfield(init: BTextField.() -> Unit) = initWidget(BTextField(), init)