package de.theunknownxy.mcdocs

import net.minecraft.client.settings.KeyBinding
import org.lwjgl.input.Keyboard

object KeyBindings {
  val openDocBinding: KeyBinding = new KeyBinding("mcdocs.key.opendoc", Keyboard.KEY_O, "mcdocs.keycategory")
}