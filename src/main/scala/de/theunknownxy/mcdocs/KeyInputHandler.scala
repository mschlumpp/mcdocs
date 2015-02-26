package de.theunknownxy.mcdocs

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.InputEvent
import net.minecraft.client.Minecraft

class KeyInputHandler {
  @SubscribeEvent
  def onKeyInput(event: InputEvent.KeyInputEvent) {
    if(KeyBindings.openDocBinding.isPressed) {
      Minecraft.getMinecraft.displayGuiScreen(new DocumentationGUI())
    }
  }
}