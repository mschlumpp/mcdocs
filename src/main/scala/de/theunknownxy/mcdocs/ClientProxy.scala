package de.theunknownxy.mcdocs

import net.minecraft.client.settings.KeyBinding
import org.lwjgl.input.Keyboard
import cpw.mods.fml.client.registry.ClientRegistry
import cpw.mods.fml.common.FMLCommonHandler

class ClientProxy extends CommonProxy {
  
  override def setupKeys() {
     ClientRegistry.registerKeyBinding(KeyBindings.openDocBinding)
     FMLCommonHandler.instance().bus().register(new KeyInputHandler())
  }
}