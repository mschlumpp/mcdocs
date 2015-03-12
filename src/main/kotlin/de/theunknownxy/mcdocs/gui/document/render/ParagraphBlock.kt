package de.theunknownxy.mcdocs.gui.document.render

import de.theunknownxy.mcdocs.docs.ParagraphElement
import net.minecraft.client.Minecraft
import java.util.ArrayList
import de.theunknownxy.mcdocs.docs.TextCommand
import de.theunknownxy.mcdocs.docs.LinkCommand
import de.theunknownxy.mcdocs.docs.BoldCommand
import de.theunknownxy.mcdocs.docs.ItalicCommand
import de.theunknownxy.mcdocs.docs.UnderlineCommand

public class ParagraphBlock(val paragraph: ParagraphElement) : Block() {
    private class FormatState(var bold: Boolean = false, var underline: Boolean = false, var italic: Boolean = false)

    override var width: Float = 1f
    set(v: Float) {
        if(v != $width) {
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

    private fun rebuild() {
        val fontrender = Minecraft.getMinecraft().fontRenderer

        var currentformat = FormatState()
        var curx = 0

        var line = TextCommand("")
        for (command in paragraph.commands) {
            when (command) {
                is TextCommand -> {
                    // Emit format
                    emitFormatCodes(currentformat, line)
                    // Fill line with characters
                    val splitted = command.text.split("(?<=[ .])")

                    for (word in splitted) {
                        val wordwidth = fontrender.getStringWidth(word)
                        if (curx + wordwidth < width) {
                            curx += wordwidth
                            line.text += word
                        } else {
                            // Create new line
                            lines.add(line)
                            line = TextCommand("")
                            emitFormatCodes(currentformat, line)
                            line.text += word
                            curx = wordwidth
                        }
                    }
                }
                is LinkCommand -> {
                    // TODO: Implement me
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
}