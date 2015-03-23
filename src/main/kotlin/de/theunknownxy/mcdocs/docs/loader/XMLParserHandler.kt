package de.theunknownxy.mcdocs.docs.loader

import de.theunknownxy.mcdocs.docs.*
import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import java.util.Stack

public class ParsedDocument {
    var title: String = ""
    var content: Content? = null
}

private abstract class XMLState(protected val handler: XMLParserHandler) {
    abstract fun startElement(uri: String, localName: String, qName: String, attributes: Attributes)
    abstract fun endElement(uri: String?, localName: String, qName: String)
    abstract fun characters(str: String)
}

private abstract class XMLStateInline(handler: XMLParserHandler, val parent: XMLStateInlineContainer?) : XMLState(handler) {
    abstract val name: String

    /**
     * Default implementation for non container tags
     */
    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        throw SAXException("Invalid start tag '$qName' in '$name' tag")
    }

    /**
     * Checks the whether the end tag matches the start tag and then runs done()
     */
    final override fun endElement(uri: String?, localName: String, qName: String) {
        if (!qName.equalsIgnoreCase(name)) {
            throw SAXException("Invalid end tag '$qName'; expected a '$name' end tag")
        }
        handler.xmlstate.pop()
        done()
    }

    /**
     * Called when the tag ended
     */
    abstract protected fun done()
}

private abstract class XMLStateInlineContainer(handler: XMLParserHandler, parent: XMLStateInlineContainer?) : XMLStateInline(handler, parent) {
    abstract val container: InlineContainerElement

    final override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        when (qName) {
            "link" -> {
                val ref = attributes.getValue("ref")
                handler.xmlstate.push(XMLStateLink(handler, this, ref))
            }
            "b" -> {
                handler.xmlstate.push(XMLStateBold(handler, this))
            }
            "i" -> {
                handler.xmlstate.push(XMLStateItalic(handler, this))
            }
            "u" -> {
                handler.xmlstate.push(XMLStateUnderline(handler, this))
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

private class XMLStateLink(handler: XMLParserHandler, parent: XMLStateInlineContainer?, val ref: String) : XMLStateInline(handler, parent) {
    override val name = "link"
    private var text = ""

    override fun characters(str: String) {
        text += str
    }

    override fun done() {
        parent?.container?.childs?.add(LinkElement(text, ref))
    }
}

private class XMLStateParagraph(handler: XMLParserHandler) : XMLStateInlineContainer(handler, null) {
    override val container = ParagraphElement()
    override val name = "p"

    override fun done() {
        handler.document.content?.blocks?.add(ParagraphBlock(container))
    }
}

private class XMLStateBold(handler: XMLParserHandler, parent: XMLStateInlineContainer?) : XMLStateInlineContainer(handler, parent) {
    override val container = FormatElement(FormatStyle.BOLD)
    override val name = "b"
}

private class XMLStateItalic(handler: XMLParserHandler, parent: XMLStateInlineContainer?) : XMLStateInlineContainer(handler, parent) {
    override val container = FormatElement(FormatStyle.ITALIC)
    override val name = "i"
}

private class XMLStateUnderline(handler: XMLParserHandler, parent: XMLStateInlineContainer?) : XMLStateInlineContainer(handler, parent) {
    override val container = FormatElement(FormatStyle.UNDERLINE)
    override val name = "u"
}


/**
 * Top-level state
 */
private class XMLStateStart(handler: XMLParserHandler) : XMLState(handler) {
    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        if (qName.equalsIgnoreCase("document")) {
            handler.xmlstate.push(XMLStateDocument(handler))
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

private class XMLStateDocument(handler: XMLParserHandler) : XMLState(handler) {
    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        if (qName.equalsIgnoreCase("title")) {
            handler.xmlstate.push(XMLStateTitle(handler))
        } else if (qName.equalsIgnoreCase("content")) {
            handler.document.content = Content()
            handler.xmlstate.push(XMLStateContent(handler))
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

private class XMLStateTitle(handler: XMLParserHandler) : XMLState(handler) {
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

private class XMLStateContent(handler: XMLParserHandler) : XMLState(handler) {
    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        if (qName.equalsIgnoreCase("img")) {
            val src: String? = attributes.getValue("src")
            if (src == null) {
                throw SAXException("image tag must have a src attribute")
            }
            val width: Int = attributes.getValue("width").toInt()
            val height: Int = attributes.getValue("height").toInt()

            handler.xmlstate.push(XMLStateImage(handler, src, width, height))
        } else if (qName.equalsIgnoreCase("p")) {
            handler.xmlstate.push(XMLStateParagraph(handler))
        } else if (qName.equalsIgnoreCase("h1")) {
            handler.xmlstate.push(XMLStateHeading(handler, 1))
        } else if (qName.equalsIgnoreCase("h2")) {
            handler.xmlstate.push(XMLStateHeading(handler, 2))
        } else if (qName.equalsIgnoreCase("h3")) {
            handler.xmlstate.push(XMLStateHeading(handler, 3))
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

private class XMLStateImage(handler: XMLParserHandler, private val src: String, private val width: Int, private val height: Int) : XMLState(handler) {
    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        throw SAXException("Tag '$qName' is not allowed in a image tag")
    }

    override fun endElement(uri: String?, localName: String, qName: String) {
        if (qName.equalsIgnoreCase("img")) {
            assert(handler.xmlstate.pop() === this)
            handler.document.content?.blocks?.add(ImageBlock(src, width, height))
        } else {
            throw SAXException("Invalid end tag '$qName' in img")
        }
    }

    override fun characters(str: String) {
        throw SAXException("Text is not allowed in img")
    }
}

private class XMLStateHeading(handler: XMLParserHandler, val level: Int) : XMLState(handler) {
    var text = ""

    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        throw SAXException("Invalid tag '$qName' in heading")
    }

    override fun endElement(uri: String?, localName: String, qName: String) {
        if (qName.equalsIgnoreCase("h$level")) {
            assert(handler.xmlstate.pop() === this)
            handler.document.content?.blocks?.add(HeadingBlock(level, text))
        } else {
            throw SAXException("Invalid end tag '$qName' in heading")
        }
    }

    override fun characters(str: String) {
        text += str
    }
}

public class XMLParserHandler : DefaultHandler() {
    var document: ParsedDocument = ParsedDocument()
    val xmlstate: Stack<XMLState> = Stack()

    override fun startDocument() {
        xmlstate.push(XMLStateStart(this))
    }

    override fun endDocument() {
        assert(xmlstate.pop() is XMLStateStart)
    }

    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        xmlstate.peek().startElement(uri, localName, qName, attributes)
    }

    override fun endElement(uri: String?, localName: String, qName: String) {
        xmlstate.peek().endElement(uri, localName, qName)
    }

    override fun characters(ch: CharArray?, start: Int, length: Int) {
        val str: String = java.lang.String(ch, start, length).toString()
        xmlstate.peek().characters(str)
    }
}