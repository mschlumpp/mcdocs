package de.theunknownxy.mcdocs.docs.loader.xml

import org.xml.sax.Attributes

abstract class State(protected val handler: XMLParserHandler) {
    abstract fun startElement(uri: String, localName: String, qName: String, attributes: Attributes)
    abstract fun endElement(uri: String?, localName: String, qName: String)
    abstract fun characters(str: String)
}