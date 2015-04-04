package de.theunknownxy.mcdocs

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.InputEvent
import de.theunknownxy.mcdocs.docs.DocumentationBackend
import de.theunknownxy.mcdocs.docs.loader.FileDocumentationLoader
import net.minecraft.client.Minecraft
import java.io.File

public class KeyInputHandler {
    var backend: DocumentationBackend? = null

    fun init() {
        val loader = FileDocumentationLoader(File(Minecraft.getMinecraft().mcDataDir, "doc").toPath())
        backend = DocumentationBackend(loader)
    }

    [SubscribeEvent]
    fun onKeyInput([suppress("UNUSED_PARAMETER")] event: InputEvent.KeyInputEvent) {
        if (KeyBindings.openDocBinding.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(DocumentationGUI(backend!!))
        }
    }
}