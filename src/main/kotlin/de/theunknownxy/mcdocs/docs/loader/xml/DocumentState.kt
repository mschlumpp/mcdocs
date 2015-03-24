package de.theunknownxy.mcdocs.docs.loader.xml

import de.theunknownxy.mcdocs.docs.Content
import org.xml.sax.Attributes
import org.xml.sax.SAXException

class DocumentState(handler: XMLParserHandler) : State(handler) {
    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        if (qName.equalsIgnoreCase("title")) {
            handler.xmlstate.push(TitleState(handler))
        } else if (qName.equalsIgnoreCase("content")) {
            handler.document.content = Content()
            handler.xmlstate.push(ContentState(handler))
        }
    }

    override fun endElement(uri: String?, localName: String, qName: String) {
        if (qName.equalsIgnoreCase("document")) {
            assert(handler.xmlstate.pop() === this)
        } else {
            throw SAXException("Invalid end tag '$qName' in document tag")
        }
    }

    override fun characters(str: String) {
    }
}