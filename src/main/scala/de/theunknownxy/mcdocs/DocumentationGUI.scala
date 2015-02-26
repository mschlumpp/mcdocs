package de.theunknownxy.mcdocs

import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.GuiButton

/**
 * @author marco
 */
class DocumentationGUI extends GuiScreen {
  override def drawScreen(par1: Int, par2: Int, par3: Float) {
    this.drawDefaultBackground()
    super.drawScreen(par1, par2, par3)
  }
}