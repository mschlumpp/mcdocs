package de.theunknownxy.mcdocs.gui.utils

import de.theunknownxy.mcdocs.docs.FormatStyle
import de.theunknownxy.mcdocs.gui.base.Rectangle
import net.minecraft.client.Minecraft
import java.util.ArrayList
import java.util.Stack

public class TextSplitter(val width: Float) {
    public val lines: MutableList<String> = ArrayList()

    private var format_dirty = true
    private var format_stack = Stack<FormatStyle>()
    private var current_x = 0f
    private var current_line = StringBuilder()

    public fun wrapFormat(style: FormatStyle, f: () -> Unit) {
        pushFormat(style)
        f()
        popFormat()
    }

    public fun pushFormat(style: FormatStyle) {
        format_stack.push(style)
        format_dirty = true
    }

    public fun popFormat() {
        format_stack.pop()
        format_dirty = true
    }

    private fun emitFormat(force: Boolean = false) {
        if (format_dirty || force) {
            // Clear previous formatting
            current_line.append("§r")

            // Merge all Styles on the stack
            var bold = false
            var italic = false
            var underline = false
            format_stack.forEach {
                when (it) {
                    FormatStyle.BOLD -> bold = true
                    FormatStyle.ITALIC -> italic = true
                    FormatStyle.UNDERLINE -> underline = true
                }
            }
            // Append the Minecraft format codes to the current_line
            if (bold) current_line.append("§l")
            if (italic) current_line.append("§o")
            if (underline) current_line.append("§n")

            format_dirty = false
        }
    }

    /**
     * Push the current line to the formatted lines and clear the current line
     */
    private fun breakLine() {
        lines.add(current_line.toString())
        current_line = StringBuilder()
        current_x = 0f
    }

    public fun addText(text: String): List<Rectangle> {
        val fontrenderer = Minecraft.getMinecraft().fontRenderer
        val rects = ArrayList<Rectangle>()

        emitFormat()
        val words = text.split("(?<=[ .])")
        for (word in words) {
            val wordwidth = fontrenderer.getStringWidth(word)

            if (current_x + wordwidth > width) {
                breakLine()
                emitFormat(true)
            }

            rects.add(Rectangle(current_x, (lines.size() * fontrenderer.FONT_HEIGHT).toFloat(), wordwidth.toFloat(), fontrenderer.FONT_HEIGHT.toFloat()))
            current_x += wordwidth
            current_line.append(word)
        }

        return rects
    }

    /**
     * Return the formatted lines
     */
    public fun toStrings(): List<String> {
        if (current_line.length() > 0) {
            breakLine()
        }
        return lines
    }
}