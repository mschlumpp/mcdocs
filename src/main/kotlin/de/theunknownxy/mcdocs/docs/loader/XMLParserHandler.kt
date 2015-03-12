package de.theunknownxy.mcdocs.docs.loader

import org.xml.sax.helpers.DefaultHandler
import org.xml.sax.Attributes
import org.xml.sax.SAXException
import java.util.Stack
import de.theunknownxy.mcdocs.docs.Content
import de.theunknownxy.mcdocs.docs.ImageElement
import de.theunknownxy.mcdocs.docs.ParagraphElement
import de.theunknownxy.mcdocs.docs.TextCommand
import de.theunknownxy.mcdocs.docs.BoldCommand
import de.theunknownxy.mcdocs.docs.UnderlineCommand
import de.theunknownxy.mcdocs.docs.ItalicCommand
import de.theunknownxy.mcdocs.docs.LinkCommand
import de.theunknownxy.mcdocs.docs.HeadingElement

public class ParsedDocument {
    var title: String = ""
    var content: Content? = null
}

private abstract class XMLState(protected val handler: XMLParserHandler) {
    abstract fun startElement(uri: String, localName: String, qName: String, attributes: Attributes)
    abstract fun endElement(uri: String?, localName: String, qName: String)
    abstract fun characters(str: String)
}

/**
 * Top-level state
 */
private class XMLStateStart(handler: XMLParserHandler) : XMLState(handler) {
    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        if (qName.equalsIgnoreCase("document")) {
            handler.xmlstate.push(XMLStateDocument(handler))
        } else {
            throw SAXException("Invalid tag '" + qName + "' in top level")
        }
    }

    override fun endElement(uri: String?, localName: String, qName: String) {
        throw SAXException("Invalid end tag '" + qName + "' at top level")
    }

    override fun characters(str: String) {
    }
}

/**
 * Within document tag
 */
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
            throw SAXException("Invalid end tag '" + qName + "' in document tag")
        }
    }

    override fun characters(str: String) {
    }
}

/**
 * Within title tag
 */
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
            throw SAXException("Invalid end tag '" + qName + "' in title tag")
        }
    }

    override fun characters(str: String) {
        title += str
    }
}

/**
 * Within content tag
 */
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
            throw SAXException("Invalid tag '" + qName + "' in content tag")
        }
    }

    override fun endElement(uri: String?, localName: String, qName: String) {
        if (qName.equalsIgnoreCase("content")) {
            assert(handler.xmlstate.pop() === this)
        } else {
            throw SAXException("Invalid end tag '" + qName + "' in content")
        }
    }

    override fun characters(str: String) {
    }
}

/**
 * Within img tag
 */
private class XMLStateImage(handler: XMLParserHandler, private val src: String, private val width: Int, private val height: Int) : XMLState(handler) {
    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        throw SAXException("Tag '" + qName + "' is not allowed in a image tag")
    }

    override fun endElement(uri: String?, localName: String, qName: String) {
        if (qName.equalsIgnoreCase("img")) {
            assert(handler.xmlstate.pop() === this)
            handler.document.content?.blocks?.add(ImageElement(src, width, height))
        } else {
            throw SAXException("Invalid end tag '" + qName + "' in img")
        }
    }

    override fun characters(str: String) {
        throw SAXException("Text is not allowed in img")
    }
}

/**
 * Within paragraph
 */
private class XMLStateParagraph(handler: XMLParserHandler) : XMLState(handler) {
    private data class FormatState(var bold: Boolean, var underline: Boolean, var italic: Boolean)

    private var formatstack: Stack<FormatState> = Stack()
    private var paragraph = ParagraphElement()

    private fun getCurrentFormat(): FormatState {
        val currentstate = FormatState(false, false, false)
        for (cmd in formatstack) {
            if (cmd.bold) {
                currentstate.bold = true
            }
            if (cmd.italic) {
                currentstate.italic = true
            }
            if (cmd.underline) {
                currentstate.underline = true
            }
        }
        return currentstate
    }

    private fun pushEmitFormat(format: FormatState) {
        formatstack.push(format)
        if (format.bold) {
            paragraph.commands.add(BoldCommand(true))
        }
        if (format.italic) {
            paragraph.commands.add(ItalicCommand(true))
        }
        if (format.underline) {
            paragraph.commands.add(UnderlineCommand(true))
        }
    }

    private fun popEmitFormat() {
        val oldformat = getCurrentFormat()
        formatstack.pop()
        val newformat = getCurrentFormat()

        if (oldformat.italic != newformat.italic) {
            paragraph.commands.add(ItalicCommand(newformat.italic))
        }
        if (oldformat.bold != newformat.bold) {
            paragraph.commands.add(BoldCommand(newformat.bold))
        }
        if (oldformat.underline != newformat.underline) {
            paragraph.commands.add(UnderlineCommand(newformat.underline))
        }
    }

    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        if (qName.equalsIgnoreCase("b")) {
            pushEmitFormat(FormatState(true, false, false))
        } else if (qName.equalsIgnoreCase("i")) {
            pushEmitFormat(FormatState(false, false, true))
        } else if (qName.equalsIgnoreCase("u")) {
            pushEmitFormat(FormatState(false, true, false))
        } else if (qName.equalsIgnoreCase("link")) {
            val ref: String? = attributes.getValue("ref")
            if (ref == null) {
                throw SAXException("A link must have a ref attribute")
            }
            handler.xmlstate.push(XMLStateLink(handler, paragraph, ref))
        }
    }

    override fun endElement(uri: String?, localName: String, qName: String) {
        if (qName.equalsIgnoreCase("p")) {
            assert(handler.xmlstate.pop() === this)
            handler.document.content?.blocks?.add(paragraph)
        } else if (qName.equalsIgnoreCase("b")) {
            popEmitFormat()
        } else if (qName.equalsIgnoreCase("i")) {
            popEmitFormat()
        } else if (qName.equalsIgnoreCase("u")) {
            popEmitFormat()
        } else {
            throw SAXException("Invalid end tag '" + qName + "' in paragraph")
        }
    }

    override fun characters(str: String) {
        paragraph.commands.add(TextCommand(str))
    }
}

private class XMLStateHeading(handler: XMLParserHandler, val level: Int) : XMLState(handler) {
    var text = ""

    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        throw SAXException("Invalid tag '" + qName + "' in heading")
    }

    override fun endElement(uri: String?, localName: String, qName: String) {
        if (qName.equalsIgnoreCase("h" + level.toString())) {
            assert(handler.xmlstate.pop() === this)
            handler.document.content?.blocks?.add(HeadingElement(level, text))
        } else {
            throw SAXException("Invalid end tag '" + qName + " in heading")
        }
    }

    override fun characters(str: String) {
        text += str
    }
}

private class XMLStateLink(handler: XMLParserHandler, val p: ParagraphElement, val ref: String) : XMLState(handler) {
    var text = ""

    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        throw SAXException("Invalid tag '" + qName + "' in link")
    }

    override fun endElement(uri: String?, localName: String, qName: String) {
        if (qName.equalsIgnoreCase("link")) {
            assert(handler.xmlstate.pop() === this)
            p.commands.add(LinkCommand(text, ref))
        } else {
            throw SAXException("Invalid end tag '" + qName + " in link")
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