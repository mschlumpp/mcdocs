package de.theunknownxy.mcdocs.docs.loader.xml.block

import de.theunknownxy.mcdocs.docs.HeadingBlock
import de.theunknownxy.mcdocs.docs.loader.xml.State
import de.theunknownxy.mcdocs.docs.loader.xml.XMLParserHandler
import org.xml.sax.Attributes
import org.xml.sax.SAXException

class HeadingState(handler: XMLParserHandler, val level: Int) : State(handler) {
    var text = ""

    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        throw SAXException("Invalid tag '$qName' in heading")
    }

    override fun endElement(uri: String?, localName: String, qName: String) {
        if (qName.equalsIgnoreCase("h$level")) {
            assert(handler.xmlstate.pop() === this)
            handler.document.content.blocks.add(HeadingBlock(level, text))
        } else {
            throw SAXException("Invalid end tag '$qName' in heading")
        }
    }

    override fun characters(str: String) {
        text += str
    }
}