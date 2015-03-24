package de.theunknownxy.mcdocs.docs.loader.xml.inline

import de.theunknownxy.mcdocs.docs.InlineContainerElement
import de.theunknownxy.mcdocs.docs.TextElement
import de.theunknownxy.mcdocs.docs.loader.xml.XMLParserHandler
import org.xml.sax.Attributes

abstract class InlineContainerState(handler: XMLParserHandler, parent: InlineContainerState?) : InlineState(handler, parent) {
    abstract val container: InlineContainerElement

    final override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        when (qName) {
            "link" -> {
                val ref = attributes.getValue("ref")
                handler.xmlstate.push(LinkState(handler, this, ref))
            }
            "b" -> {
                handler.xmlstate.push(BoldState(handler, this))
            }
            "i" -> {
                handler.xmlstate.push(ItalicState(handler, this))
            }
            "u" -> {
                handler.xmlstate.push(UnderlineState(handler, this))
            }
        }
    }

    override fun characters(str: String) {
        container.childs.add(TextElement(str))
    }

    override fun done() {
        parent?.container?.childs?.add(container)
    }
}