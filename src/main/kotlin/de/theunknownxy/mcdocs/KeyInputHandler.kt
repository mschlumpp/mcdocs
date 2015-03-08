package de.theunknownxy.mcdocs

import cpw.mods.fml.common.gameevent.InputEvent
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.client.Minecraft
import de.theunknownxy.mcdocs.docs.DocumentationBackend
import de.theunknownxy.mcdocs.docs.loader.FileDocumentationLoader
import java.io.File

public class KeyInputHandler {
    var backend: DocumentationBackend? = null
    var gui: DocumentationGUI? = null

    fun init() {
        val loader = FileDocumentationLoader(File(Minecraft.getMinecraft().mcDataDir, "doc"))
        backend = DocumentationBackend(loader)
        gui = DocumentationGUI(backend!!)
    }

    [SubscribeEvent]
    fun onKeyInput(event: InputEvent.KeyInputEvent) {
        if (KeyBindings.openDocBinding.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(gui)
        }
    }
}