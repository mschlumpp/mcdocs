package de.theunknownxy.mcdocs.gui.document.builder

import de.theunknownxy.mcdocs.docs.HeadingElement
import de.theunknownxy.mcdocs.gui.document.Document
import de.theunknownxy.mcdocs.gui.document.segments.HeadingSegment

internal class HeadingBuilder(val document: Document, val heading: HeadingElement) : Builder {
    override fun build() {
        document.addSegment(HeadingSegment(document, heading))
    }
}