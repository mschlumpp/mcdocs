package de.theunknownxy.mcdocs.gui.document.builder

import de.theunknownxy.mcdocs.docs.*
import de.theunknownxy.mcdocs.gui.document.Document
import de.theunknownxy.mcdocs.gui.document.segments.TextSegment
import de.theunknownxy.mcdocs.gui.utils.TextSplitter

class ParagraphBuilder(val document: Document, val paragraph: ParagraphElement) : Builder {
    public override fun build() {
        // Split the text
        val splitter = TextSplitter(document.width)

        for (command in paragraph.commands) {
            when (command) {
                is TextCommand -> {
                    splitter.addText(command.text)
                }
                is LinkCommand -> {
                    splitter.addText(command.text).forEach {
                        document.addLink(it, DocumentationNodeRef(command.ref))
                    }
                }
                is BoldCommand -> {
                    //FIXME: Are they really symetric after XMLParserHandler?
                    if (command.enable) splitter.pushBold() else splitter.popFormat()
                }
                is ItalicCommand -> {
                    if (command.enable) splitter.pushItalic() else splitter.popFormat()
                }
                is UnderlineCommand -> {
                    if (command.enable) splitter.pushUnderline() else splitter.popFormat()
                }
            }
        }

        // Generate segments
        splitter.toStrings().forEach {
            document.addSegment(TextSegment(document, it))
        }
        // Append a empty line for spacing
        document.addSegment(TextSegment(document, ""))
    }
}