package de.theunknownxy.mcdocs.gui.document.builder

import de.theunknownxy.mcdocs.docs.ImageBlock
import de.theunknownxy.mcdocs.gui.document.Document
import de.theunknownxy.mcdocs.gui.document.segments.ImageSegment

class ImageBuilder(val document: Document, val image: ImageBlock): Builder {
    override fun build() {

        val segment = ImageSegment(document, image)
        segment.width = document.width
        document.addSegment(segment)
    }
}