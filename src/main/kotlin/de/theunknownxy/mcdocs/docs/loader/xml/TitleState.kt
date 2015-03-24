package de.theunknownxy.mcdocs.docs.loader.xml

import org.xml.sax.Attributes
import org.xml.sax.SAXException

class TitleState(handler: XMLParserHandler) : State(handler) {
    private var title: String = ""

    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        throw SAXException("No tags are allowed within the title tag")
    }

    override fun endElement(uri: String?, localName: String, qName: String) {
        if (qName.equalsIgnoreCase("title")) {
            assert(handler.xmlstate.pop() === this)
            handler.document.title = title
        } else {
            throw SAXException("Invalid end tag '$qName' in title tag")
        }
    }

    override fun characters(str: String) {
        title += str
    }
}