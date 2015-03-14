package de.theunknownxy.mcdocs.gui.document

import java.util.ArrayList
import de.theunknownxy.mcdocs.gui.base.Root
import de.theunknownxy.mcdocs.gui.base.Widget
import de.theunknownxy.mcdocs.docs.DocumentationBackend
import de.theunknownxy.mcdocs.docs.Content
import de.theunknownxy.mcdocs.docs.ImageElement
import de.theunknownxy.mcdocs.docs.ParagraphElement
import de.theunknownxy.mcdocs.docs.HeadingElement
import de.theunknownxy.mcdocs.gui.widget.ScrollWidget
import de.theunknownxy.mcdocs.gui.base.Rectangle

public class DocumentViewer(root: Root?) : ScrollWidget(root) {
    class object {
        private val PADDING_TOP = 9f
        private val PADDING_INNER = 4f
        private val PADDING_RIGHT = 2f
    }

    var backend: DocumentationBackend? = null

    var current_content: Content? = null
    var render_blocks: MutableList<render.Block> = ArrayList()

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
            block.width = this.width - ScrollWidget.SCROLLER_BAR_WIDTH
            block.width -= PADDING_RIGHT
        }
    }

    override fun onAreaChanged() {
        super.onAreaChanged()
        update_width()
    }

    override fun getContentHeight(): Float {
        var sum = PADDING_TOP
        for(block in render_blocks) {
            sum += block.height + PADDING_INNER
        }
        return sum
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
            var cury = childArea.y + PADDING_TOP
            for(block in render_blocks) {
                block.draw(childArea.x, cury, 10f)
                cury += block.height + PADDING_INNER
            }
        }
    }
}