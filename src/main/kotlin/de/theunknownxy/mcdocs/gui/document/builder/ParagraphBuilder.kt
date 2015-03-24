package de.theunknownxy.mcdocs.gui.document.builder

import de.theunknownxy.mcdocs.docs.*
import de.theunknownxy.mcdocs.gui.document.Document
import de.theunknownxy.mcdocs.gui.document.segments.TextSegment
import de.theunknownxy.mcdocs.gui.utils.TextSplitter

class ParagraphBuilder(val document: Document, val paragraph: ParagraphBlock) : Builder {


    public override fun build() {
        // Split the text
        val splitter = TextSplitter(document.width)

        fun build_rec(element: InlineElement) {
            when(element) {
                is TextElement -> {
                    splitter.addText(element.text)
                }
                is LinkElement -> {
                    splitter.wrapFormat(FormatStyle.ITALIC, {
                        splitter.wrapFormat(FormatStyle.UNDERLINE, {
                            splitter.addText(element.text).forEach {
                                document.addLink(it, DocumentationNodeRef(element.ref))
                            }
                        })
                    })
                }
                is FormatElement -> {
                    splitter.pushFormat(element.style)
                    element.childs.forEach { build_rec(it) }
                    splitter.popFormat()
                }
                is InlineContainerElement -> {
                    element.childs.forEach { build_rec(it) }
                }
            }
        }

        build_rec(paragraph.value)

        // Generate segments
        splitter.toStrings().forEach {
            document.addSegment(TextSegment(document, it))
        }
        // Append a empty line for spacing
        document.addSegment(TextSegment(document, ""))
    }
}