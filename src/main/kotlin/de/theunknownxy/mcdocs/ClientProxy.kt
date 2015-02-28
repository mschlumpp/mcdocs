package de.theunknownxy.mcdocs

import cpw.mods.fml.client.registry.ClientRegistry
import cpw.mods.fml.common.FMLCommonHandler

public class ClientProxy : CommonProxy() {
    override fun setupKeys() {
        ClientRegistry.registerKeyBinding(KeyBindings.openDocBinding)
        FMLCommonHandler.instance().bus().register(KeyInputHandler())
    }
}