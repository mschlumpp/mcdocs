package de.theunknownxy.mcdocs.docs.loader.xml

import de.theunknownxy.mcdocs.docs.loader.xml.block.HeadingState
import de.theunknownxy.mcdocs.docs.loader.xml.block.ImageState
import de.theunknownxy.mcdocs.docs.loader.xml.block.ParagraphState
import org.xml.sax.Attributes
import org.xml.sax.SAXException

class ContentState(handler: XMLParserHandler) : State(handler) {
    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        if (qName.equalsIgnoreCase("img")) {
            val src: String? = attributes.getValue("src")
            if (src == null) {
                throw SAXException("image tag must have a src attribute")
            }
            val width: Int = attributes.getValue("width").toInt()
            val height: Int = attributes.getValue("height").toInt()

            handler.xmlstate.push(ImageState(handler, src, width, height))
        } else if (qName.equalsIgnoreCase("p")) {
            handler.xmlstate.push(ParagraphState(handler))
        } else if (qName.equalsIgnoreCase("h1")) {
            handler.xmlstate.push(HeadingState(handler, 1))
        } else if (qName.equalsIgnoreCase("h2")) {
            handler.xmlstate.push(HeadingState(handler, 2))
        } else if (qName.equalsIgnoreCase("h3")) {
            handler.xmlstate.push(HeadingState(handler, 3))
        } else {
            throw SAXException("Invalid tag '$qName' in content tag")
        }
    }

    override fun endElement(uri: String?, localName: String, qName: String) {
        if (qName.equalsIgnoreCase("content")) {
            assert(handler.xmlstate.pop() === this)
        } else {
            throw SAXException("Invalid end tag '$qName' in content")
        }
    }

    override fun characters(str: String) {
    }
}