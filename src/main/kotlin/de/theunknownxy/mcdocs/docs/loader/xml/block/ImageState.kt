package de.theunknownxy.mcdocs.docs.loader.xml.block

import de.theunknownxy.mcdocs.docs.ImageBlock
import de.theunknownxy.mcdocs.docs.loader.xml.State
import de.theunknownxy.mcdocs.docs.loader.xml.XMLParserHandler
import org.xml.sax.Attributes
import org.xml.sax.SAXException

class ImageState(handler: XMLParserHandler, private val src: String, private val width: Int, private val height: Int) : State(handler) {
    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        throw SAXException("Tag '$qName' is not allowed in a image tag")
    }

    override fun endElement(uri: String?, localName: String, qName: String) {
        if (qName.equalsIgnoreCase("img")) {
            assert(handler.xmlstate.pop() === this)
            handler.document.content.blocks.add(ImageBlock(src, width, height))
        } else {
            throw SAXException("Invalid end tag '$qName' in img")
        }
    }

    override fun characters(str: String) {
        throw SAXException("Text is not allowed in img")
    }
}