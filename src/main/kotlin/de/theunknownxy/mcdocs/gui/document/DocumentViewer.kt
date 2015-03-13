package de.theunknownxy.mcdocs.gui.document

import java.util.ArrayList
import de.theunknownxy.mcdocs.gui.base.Root
import de.theunknownxy.mcdocs.gui.base.Widget
import de.theunknownxy.mcdocs.docs.DocumentationBackend
import de.theunknownxy.mcdocs.docs.Content
import de.theunknownxy.mcdocs.docs.ImageElement
import de.theunknownxy.mcdocs.docs.ParagraphElement
import de.theunknownxy.mcdocs.docs.HeadingElement

public class DocumentViewer(root: Root?) : Widget(root) {
    var backend: DocumentationBackend? = null

    var current_content: Content? = null
    var render_blocks: MutableList<render.Block> = ArrayList()
    private val padding_top = 9f
    private val padding_inner = 4f

    private fun rebuild_blocks() {
        render_blocks.clear()
        if(current_content != null) {
            for(block in current_content!!.blocks) {
                when(block) {
                    is ImageElement -> {
                        render_blocks.add(render.ImageBlock(block))
                    }
                    is ParagraphElement -> {
                        render_blocks.add(render.ParagraphBlock(block))
                    }
                    is HeadingElement -> {
                        render_blocks.add(render.HeadingBlock(block))
                    }
                }
            }
        }
    }

    private fun update_width() {
        for(block in render_blocks) {
            block.width = this.width
        }
    }

    override fun onAreaChanged() {
        super.onAreaChanged()
        update_width()
    }

    override fun draw() {
        if (backend != null) {
            // Update blocks if the page changed
            val new_content = backend!!.getContent(backend!!.current_page)
            if (new_content != current_content) {
                current_content = new_content
                rebuild_blocks()
                update_width()
            }

            // Draw content
            var cury = this.y + padding_top
            for(block in render_blocks) {
                block.draw(this.x, cury, 10f)
                cury += block.height + padding_inner
            }
        }
    }
}