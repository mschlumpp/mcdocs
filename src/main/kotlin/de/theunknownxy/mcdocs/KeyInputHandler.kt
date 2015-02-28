package de.theunknownxy.mcdocs

import cpw.mods.fml.common.gameevent.InputEvent
import net.minecraft.client.Minecraft
import cpw.mods.fml.common.eventhandler.SubscribeEvent

public class KeyInputHandler {
    [SubscribeEvent]
    fun onKeyInput(event: InputEvent.KeyInputEvent) {
        if(KeyBindings.openDocBinding.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(DocumentationGUI())
        }
    }
}