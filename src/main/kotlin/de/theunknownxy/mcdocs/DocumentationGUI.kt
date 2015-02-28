package de.theunknownxy.mcdocs

import net.minecraft.client.gui.GuiScreen

public class DocumentationGUI : GuiScreen() {
    override fun drawScreen(par1: Int, par2: Int, par3: Float) {
        this.drawDefaultBackground()
        super.drawScreen(par1, par2, par3)
    }
}