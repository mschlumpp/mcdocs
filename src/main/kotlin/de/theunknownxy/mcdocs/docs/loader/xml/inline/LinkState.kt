package de.theunknownxy.mcdocs.docs.loader.xml.inline

import de.theunknownxy.mcdocs.docs.LinkElement
import de.theunknownxy.mcdocs.docs.loader.xml.XMLParserHandler

class LinkState(handler: XMLParserHandler, parent: InlineContainerState?, val ref: String) : InlineState(handler, parent) {
    override val name = "link"
    private var text = ""

    override fun characters(str: String) {
        text += str
    }

    override fun done() {
        parent?.container?.childs?.add(LinkElement(text, ref))
    }
}