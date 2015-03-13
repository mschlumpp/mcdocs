package de.theunknownxy.mcdocs.gui.widget

import de.theunknownxy.mcdocs.gui.base.Widget
import de.theunknownxy.mcdocs.gui.base.Root
import de.theunknownxy.mcdocs.docs.DocumentationBackend
import de.theunknownxy.mcdocs.gui.base.Point
import de.theunknownxy.mcdocs.gui.event.MouseButton
import de.theunknownxy.mcdocs.gui.base.Rectangle
import de.theunknownxy.mcdocs.docs.DocumentationNodeRef
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.util.ResourceLocation
import net.minecraft.client.Minecraft

public class TreeBar(root: Root?) : Widget(root) {
    var backend: DocumentationBackend? = null
    var hovered_item: DocumentationNodeRef? = null

    override fun draw() {
        val fontrenderer = root?.gui?.mc?.fontRenderer
        val backend = backend!!
        if (fontrenderer != null) {
            var dy = 4

            fun drawEntry(node: DocumentationNodeRef, text: String, color: Int) {
                var ecol = color
                if (Rectangle(this.x, this.y + dy, this.width, 9f - 1f).contains(root!!.mouse_pos)) {
                    // Highlight item on hover and store it
                    ecol = 0xFFFFFF
                    hovered_item = node
                } else if(node == backend.current_page) {
                    ecol = 0x99FF99
                }
                fontrenderer.drawString(text, this.x.toInt(), (this.y + dy).toInt(), ecol)
                dy += 9
            }

            if (backend.current_path != backend.root) {
                // Draw the up entry
                val title = backend.getTitle(backend.current_path)
                drawEntry(backend.current_path.parent(), ".. §o[$title]", 0xDDDDDD)
            } else {
                // Or leave some space
                dy += 9
            }
            val children = backend.getChildren(backend.current_path)
            for (child in children) {
                val title = backend.getTitle(child)
                drawEntry(child, title, 0xDDDDDD)
                val childchildren = backend.getChildren(child)
                for (childchild in childchildren) {
                    val childtitle = backend.getTitle(childchild)
                    val childchildchildren = backend.getChildren(childchild)
                    var prefix = "  "
                    if(childchildchildren.size() > 0) {
                        prefix = " +"
                    }
                    drawEntry(childchild, prefix + childtitle, 0xAAAAAA)
                }
            }
        }
    }

    override fun onMouseClick(pos: Point, button: MouseButton): Widget? {
        if(hovered_item != null) {
            backend?.navigate(hovered_item!!)
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(ResourceLocation("gui.button.press"), 1.0.toFloat()))
        }
        return null
    }
}