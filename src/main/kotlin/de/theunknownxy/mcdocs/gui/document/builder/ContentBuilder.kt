package de.theunknownxy.mcdocs.gui.document.builder

import de.theunknownxy.mcdocs.docs.Content
import de.theunknownxy.mcdocs.docs.HeadingBlock
import de.theunknownxy.mcdocs.docs.ImageBlock
import de.theunknownxy.mcdocs.docs.ParagraphBlock
import de.theunknownxy.mcdocs.gui.document.Document

class ContentBuilder(val document: Document, val content: Content): Builder {
    override fun build() {
        for(element in content.blocks) {
            when(element) {
                is ParagraphBlock -> {
                    ParagraphBuilder(document, element).build()
                }
                is HeadingBlock -> {
                    HeadingBuilder(document, element).build()
                }
                is ImageBlock -> {
                    ImageBuilder(document, element).build()
                }
                else -> {
                    println("[MCDocs] Unhandled Element in Documentation '$element'")
                }
            }
        }
    }
}