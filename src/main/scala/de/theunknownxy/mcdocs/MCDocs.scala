package de.theunknownxy.mcdocs

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.SidedProxy

//TODO: add canBeDeactivated?
@Mod(modid = MCDocs.MODID, version = MCDocs.VERSION, modLanguage = "scala")
object MCDocs {
  final val MODID = "mcdocs"
  final val VERSION = "1.0"
  
  @SidedProxy(serverSide = "de.theunknownxy.mcdocs.CommonProxy", clientSide = "de.theunknownxy.mcdocs.ClientProxy")
  var proxy: CommonProxy = null
  
  @EventHandler
  def init(event: FMLInitializationEvent) {
    proxy.setupKeys()
  }
}