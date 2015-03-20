package de.theunknownxy.mcdocs.gui.document.render

import de.theunknownxy.mcdocs.docs.*
import de.theunknownxy.mcdocs.gui.base.Point
import de.theunknownxy.mcdocs.gui.base.Rectangle
import de.theunknownxy.mcdocs.gui.event.MouseButton
import net.minecraft.client.Minecraft
import java.util.ArrayList

public class ParagraphBlock(val paragraph: ParagraphElement, val backend: DocumentationBackend) : Block() {
    private class FormatState(var bold: Boolean = false, var underline: Boolean = false, var italic: Boolean = false)
    private data class LinkPair(var rect: Rectangle, var ref: DocumentationNodeRef)


    override var width: Float = 1f
        set(v: Float) {
            if (v != $width) {
                $width = v
                dirty = true
            }
        }
    override var height: Float
        private set(v: Float) {
        }
        get() {
            if (dirty) rebuild()
            return lines.size() * 9f
        }

    private var dirty = true
    private var lines = ArrayList<TextCommand>()
    private var links = ArrayList<LinkPair>()

    private fun rebuild() {
        lines.clear()
        links.clear()
        val fontrender = Minecraft.getMinecraft().fontRenderer

        var currentformat = FormatState()
        var curx = 0

        var line = TextCommand("")

        fun addText(text: String, ref: DocumentationNodeRef? = null) {
            // Emit format
            emitFormatCodes(currentformat, line)
            // Fill line with characters
            val splitted = text.split("(?<=[ .])")

            for (word in splitted) {
                val wordwidth = fontrender.getStringWidth(word)
                if (curx + wordwidth < width) {
                    if (ref != null) {
                        links.add(LinkPair(Rectangle(curx.toFloat(), (lines.size() * fontrender.FONT_HEIGHT).toFloat(), wordwidth.toFloat(), (fontrender.FONT_HEIGHT).toFloat()), ref))
                    }
                    curx += wordwidth
                    line.text += word
                } else {
                    // Create new line
                    lines.add(line)
                    line = TextCommand("")
                    emitFormatCodes(currentformat, line)
                    if (ref != null) {
                        links.add(LinkPair(Rectangle(0f, (lines.size() * fontrender.FONT_HEIGHT).toFloat(), wordwidth.toFloat(), (fontrender.FONT_HEIGHT).toFloat()), ref))
                    }
                    line.text += word
                    curx = wordwidth
                }
            }
        }

        for (command in paragraph.commands) {
            when (command) {
                is TextCommand -> {
                    addText(command.text)
                }
                is LinkCommand -> {
                    addText(command.text, DocumentationNodeRef(command.ref))
                }
                is BoldCommand -> {
                    currentformat.bold = command.enable
                }
                is ItalicCommand -> {
                    currentformat.italic = command.enable
                }
                is UnderlineCommand -> {
                    currentformat.underline = command.enable
                }
            }
        }
        if (line.text.length() > 0) {
            lines.add(line)
        }

        dirty = false
    }

    private fun emitFormatCodes(format: FormatState, line: TextCommand) {
        line.text += "§r"
        if (format.bold) {
            line.text += "§l"
        }
        if (format.underline) {
            line.text += "§n"
        }
        if (format.italic) {
            line.text += "§o"
        }
    }

    override fun draw(x: Float, y: Float, z: Float) {
        if (dirty) rebuild()
        val fontrender = Minecraft.getMinecraft().fontRenderer

        var cury = y
        for (line in lines) {
            fontrender.drawString(line.text, x.toInt(), cury.toInt(), 0xFFFFFF)
            cury += 9f
        }
    }

    override fun onMouseClick(pos: Point, button: MouseButton) {
        for ((rect, ref) in links) {
            if(rect.contains(pos)) {
                backend.navigate(ref)
                return
            }
        }
    }
}