package de.theunknownxy.mcdocs.gui.widget

import de.theunknownxy.mcdocs.docs.DocumentationBackend
import de.theunknownxy.mcdocs.gui.base.Point
import de.theunknownxy.mcdocs.gui.event.MouseButton
import de.theunknownxy.mcdocs.gui.base.Rectangle
import de.theunknownxy.mcdocs.docs.DocumentationNodeRef
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import java.util.ArrayList

public class TreeBar() : ScrollChild() {
    class object {
        val COLOR_HIGHLIGHT = 0xFFFFFF
        val COLOR_ACTIVE = 0x99FF99
        val COLOR_TOPLEVEL = 0xDDDDDD
        val COLOR_SECONDLEVEL = 0xAAAAAA
        val PADDING_TOP = 4
    }

    private data class Entry(val level: Int, val text: String, val ref: DocumentationNodeRef)

    var dirty = true
    var backend: DocumentationBackend? = null
    var entries: MutableList<Entry> = ArrayList()

    /**
     * Append 'num' spaces to a StringBuilder
     */
    private fun add_space(sb: StringBuilder, num: Int) = num * { sb.append(' ') }

    /**
     * Add the childs of 'node' to the entries.
     */
    private fun add_children(node: DocumentationNodeRef, max_level: Int, level: Int = 0) {
        val backend = backend
        if (backend != null && level < max_level) {
            val children = backend.getChildren(node)
            for (child in children) {
                val numchilds = backend.getChildren(child).size()

                val sb = StringBuilder()
                if (numchilds > 0) {
                    // Make nodes with children italic
                    sb.append("§o")
                }
                add_space(sb, level)
                sb.append(backend.getTitle(child))

                entries.add(Entry(level, sb.toString(), child))
                add_children(child, max_level, level + 1)
            }
        }
    }

    /**
     * Recreate the entries in the TreeBar after something changed.
     */
    private fun rebuild() {
        val backend = backend
        if (backend != null) {
            entries.clear()
            if (backend.current_path != backend.root) {
                // Draw the up entry
                val title = backend.getTitle(backend.current_path)
                entries.add(Entry(0, ".. §o[$title]", backend.current_path.parent()))
            }

            add_children(backend.current_path, 2)
            dirty = false
        }
    }

    override fun draw() {
        val fontrenderer = Minecraft?.getMinecraft()?.fontRenderer
        val backend = backend
        if (fontrenderer != null && backend != null) {
            if (dirty) rebuild()

            var dy = PADDING_TOP
            for ((level, text, ref) in entries) {
                var ecol = 0x000000
                // Color depending on depth
                if (level == 0) {
                    ecol = COLOR_TOPLEVEL
                } else if (level == 1) {
                    ecol = COLOR_SECONDLEVEL
                }

                // Override color if the node is active or highlighted
                if (Rectangle(0f, dy.toFloat(), this.width, fontrenderer.FONT_HEIGHT - 1f).contains(mouse_pos)) {
                    ecol = COLOR_HIGHLIGHT
                } else if (ref == backend.current_page) {
                    ecol = COLOR_ACTIVE
                }
                fontrenderer.drawString(text, 0, dy.toInt(), ecol)
                dy += fontrenderer.FONT_HEIGHT
            }
        }
    }

    override fun onMouseClick(pos: Point, button: MouseButton) {
        val index = ((pos.y - PADDING_TOP) / Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT.toFloat()).toInt()
        if (index < entries.size()) {
            backend?.navigate(entries[index].ref)
            dirty = true
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(ResourceLocation("gui.button.press"), 1.toFloat()))
        }
    }

    override fun getHeight(): Float {
        return (entries.size() * Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + PADDING_TOP).toFloat()
    }
}
