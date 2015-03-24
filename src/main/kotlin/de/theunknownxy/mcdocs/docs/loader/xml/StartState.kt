package de.theunknownxy.mcdocs.docs.loader.xml

import org.xml.sax.Attributes
import org.xml.sax.SAXException

/**
 * Top-level state
 */
class StartState(handler: XMLParserHandler) : State(handler) {
    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        if (qName.equalsIgnoreCase("document")) {
            handler.xmlstate.push(DocumentState(handler))
        } else {
            throw SAXException("Invalid tag '$qName' in top level")
        }
    }

    override fun endElement(uri: String?, localName: String, qName: String) {
        throw SAXException("Invalid end tag '$qName' at top level")
    }

    override fun characters(str: String) {
    }
}