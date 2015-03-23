package de.theunknownxy.mcdocs

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.SidedProxy
import cpw.mods.fml.common.event.FMLInitializationEvent

[Mod(modid = MCDocs.MODID, version = MCDocs.VERSION)]
public class MCDocs {
    companion object {
        val MODID: String = "mcdocs"
        val VERSION: String = "1.0"

        [SidedProxy(clientSide = "de.theunknownxy.mcdocs.ClientProxy", serverSide = "de.theunknownxy.mcdocs.CommonProxy")]
        var proxy: CommonProxy? = null
    }

    [Mod.EventHandler]
    fun init([suppress("UNUSED_PARAMETER")] event: FMLInitializationEvent) {
        proxy?.setupKeys()
    }
}