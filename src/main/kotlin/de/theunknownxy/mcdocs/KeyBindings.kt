package de.theunknownxy.mcdocs

import net.minecraft.client.settings.KeyBinding
import org.lwjgl.input.Keyboard

public object KeyBindings {
    val openDocBinding: KeyBinding = KeyBinding("mcdocs.key.opendoc", Keyboard.KEY_O, "mcdocs.keycategory")
}