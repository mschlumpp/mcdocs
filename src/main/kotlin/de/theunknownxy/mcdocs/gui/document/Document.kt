package de.theunknownxy.mcdocs.gui.document

import de.theunknownxy.mcdocs.docs.Content
import de.theunknownxy.mcdocs.docs.DocumentationBackend
import de.theunknownxy.mcdocs.docs.DocumentationNodeRef
import de.theunknownxy.mcdocs.gui.base.Point
import de.theunknownxy.mcdocs.gui.base.Rectangle
import de.theunknownxy.mcdocs.gui.document.builder.ContentBuilder
import de.theunknownxy.mcdocs.gui.document.segments.Segment
import de.theunknownxy.mcdocs.gui.event.MouseButton
import java.util.ArrayList

class Document(val backend: DocumentationBackend, val content: Content) {
    private data class Link(val rect: Rectangle, val ref: DocumentationNodeRef)

    private val links: MutableList<Link> = ArrayList()

    private val segments: MutableList<Segment> = ArrayList()

    public var width: Float = 0f
        set(v: Float) {
            if ($width != v) {
                $width = v
                rebuild()
            }
        }

    public val height: Float
        get() {
            return segments.lastOrNull()?.rect()?.y2() ?: 0f
        }

    /**
     * Add a segment to the end of the document
     */
    internal fun addSegment(segment: Segment) {
        segment.y = segments.lastOrNull()?.rect()?.y2() ?: 0f
        segments.add(segment)
    }

    /**
     * Add a link rectangle to the document. The rectangle is offseted to the last segment position
     */
    internal fun addLink(rect: Rectangle, ref: DocumentationNodeRef) {
        rect.y += segments.lastOrNull()?.rect()?.y2() ?: 0f
        links.add(Link(rect, ref))
    }

    /**
     * Rebuild the segments from the content
     */
    public fun rebuild() {
        links.clear()
        segments.clear()
        ContentBuilder(this, content).build()
    }

    /**
     * Draw the document at x=0|y=0 to the screen
     */
    public fun draw() {
        for (segment in segments) {
            segment.draw()
        }
    }

    /**
     * Return an action to run when clicked at pos
     * @param[pos] The position of the mouse click in Document local coordinates
     */
    public fun onMouseClick(pos: Point, button: MouseButton): Action? {
        for ((rect, ref) in links) {
            if (rect.contains(pos) && button == MouseButton.LEFT) {
                return NavigateAction(ref)
            }
        }
        return null
    }
}