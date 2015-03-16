package de.theunknownxy.mcdocs.gui.document

import java.util.ArrayList
import de.theunknownxy.mcdocs.docs.DocumentationBackend
import de.theunknownxy.mcdocs.docs.Content
import de.theunknownxy.mcdocs.docs.ImageElement
import de.theunknownxy.mcdocs.docs.ParagraphElement
import de.theunknownxy.mcdocs.docs.HeadingElement
import de.theunknownxy.mcdocs.docs.BlockElement
import de.theunknownxy.mcdocs.docs.TextCommand
import de.theunknownxy.mcdocs.gui.base.Rectangle
import de.theunknownxy.mcdocs.gui.base.Root
import de.theunknownxy.mcdocs.gui.widget.ScrollWidget
import de.theunknownxy.mcdocs.gui.document.render.Block
import de.theunknownxy.mcdocs.gui.document.render.ImageBlock
import de.theunknownxy.mcdocs.gui.document.render.ParagraphBlock
import de.theunknownxy.mcdocs.gui.document.render.HeadingBlock

public class DocumentViewer(root: Root?) : ScrollWidget(root) {
    class object {
        private val PADDING_TOP = 9f
        private val PADDING_INNER = 4f
        private val PADDING_RIGHT = 2f
    }

    private data class RenderBlock(var position: Float, val block: render.Block)

    var backend: DocumentationBackend? = null

    var current_content: Content? = null
    var render_blocks: MutableList<RenderBlock> = ArrayList()

    private fun rebuild_blocks() {
        render_blocks.clear()
        if (current_content != null) {
            for (block in current_content!!.blocks) {
                render_blocks.add(RenderBlock(0f, create_render_block(block)))
            }
        }
    }

    /**
     * Create a render.Block from the passed BlockElement
     */
    private fun create_render_block(block: BlockElement): Block = when (block) {
        is ImageElement -> {
            ImageBlock(block)
        }
        is ParagraphElement -> {
            ParagraphBlock(block)
        }
        is HeadingElement -> {
            HeadingBlock(block)
        }
        else -> {
            val p = ParagraphElement()
            p.commands.add(TextCommand("Invalid element ${block.toString()}"))
            ParagraphBlock(p)
        }
    }

    /**
     * Update the width of each block and adjust the positions accordingly
     */
    private fun update_width() {
        var lastposition = PADDING_TOP
        for (pair in render_blocks) {
            pair.position = lastposition
            pair.block.width = contentWidth() - PADDING_RIGHT
            lastposition += pair.block.height + PADDING_INNER
        }
    }

    override fun onAreaChanged() {
        super.onAreaChanged()
        update_width()
    }

    override fun getContentHeight(): Float {
        val last = render_blocks.lastOrNull()
        if (last != null) {
            return last.position + last.block.height
        } else {
            return 0f
        }
    }

    override fun drawContent(childArea: Rectangle) {
        if (backend != null) {
            // Update blocks if the page changed
            val new_content = backend!!.getContent(backend!!.current_page)
            if (new_content != current_content) {
                current_content = new_content
                rebuild_blocks()
                update_width()
            }

            // Draw content
            for ((position, block) in render_blocks) {
                block.draw(childArea.x, position, 10f)
            }
        }
    }
}