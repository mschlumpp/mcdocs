package de.theunknownxy.mcdocs;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@Mod(modid = MCDocs.MODID, version = MCDocs.VERSION)
public class MCDocs {
    public static final String MODID = "mcdocs";
    public static final String VERSION = "1.0";
    @SidedProxy(serverSide = "de.theunknownxy.mcdocs.CommonProxy", clientSide = "de.theunknownxy.mcdocs.ClientProxy")
    static CommonProxy proxy;

    @Mod.EventHandler
    void init(FMLInitializationEvent event) {
        proxy.setupKeys();
    }
}
