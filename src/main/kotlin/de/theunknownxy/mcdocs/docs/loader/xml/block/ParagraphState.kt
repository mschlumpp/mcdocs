package de.theunknownxy.mcdocs.docs.loader.xml.block

import de.theunknownxy.mcdocs.docs.ParagraphBlock
import de.theunknownxy.mcdocs.docs.ParagraphElement
import de.theunknownxy.mcdocs.docs.loader.xml.XMLParserHandler
import de.theunknownxy.mcdocs.docs.loader.xml.inline.InlineContainerState

class ParagraphState(handler: XMLParserHandler) : InlineContainerState(handler, null) {
    override val container = ParagraphElement()
    override val name = "p"

    override fun done() {
        handler.document.content.blocks.add(ParagraphBlock(container))
    }
}