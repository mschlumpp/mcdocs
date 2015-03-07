package de.theunknownxy.mcdocs.docs.loader

import org.xml.sax.helpers.DefaultHandler
import org.xml.sax.Attributes
import java.util.Stack
import org.xml.sax.SAXException
import de.theunknownxy.mcdocs.docs.Content
import de.theunknownxy.mcdocs.docs.ImageElement
import de.theunknownxy.mcdocs.docs.ParagraphElement
import de.theunknownxy.mcdocs.docs.TextCommand
import de.theunknownxy.mcdocs.docs.BoldCommand
import de.theunknownxy.mcdocs.docs.UnderlineCommand
import de.theunknownxy.mcdocs.docs.ItalicCommand
import de.theunknownxy.mcdocs.docs.LinkCommand
import de.theunknownxy.mcdocs.docs.HeadingElement

private enum class State {
    INVALID
    DOCUMENT
    TITLE
    CONTENT
    PARAGRAPH
    LINK
    HEADING
}

private enum class FormatState {
    BOLD
    ITALIC
    UNDERLINE
}

public class ParsedDocument {
    var title: String = ""
    var content: Content? = Content()
}

public class XMLParserHandler : DefaultHandler() {
    var document: ParsedDocument = ParsedDocument()
    var state: Stack<State> = Stack()

    // For the PARAGRAPH stack
    var paragraph_elem: ParagraphElement? = null
    var paragraph_formatstack: Stack<FormatState> = Stack()

    // For the LINK state
    var link_ref = ""
    var link_text = ""

    // For the HEADING state
    var heading_level = 0
    var heading_text = ""

    override fun startDocument() {
        state.push(State.INVALID)
    }

    override fun endDocument() {
        assert(state.pop() == State.INVALID)
    }

    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        when (state.peek()) {
            State.INVALID -> {
                state.push(State.DOCUMENT)
            }
            State.DOCUMENT -> {
                if (qName.equalsIgnoreCase("title")) {
                    state.push(State.TITLE)
                } else if (qName.equalsIgnoreCase("content")) {
                    state.push(State.CONTENT)
                }
            }
            State.CONTENT -> {
                if (qName.equalsIgnoreCase("img")) {
                    val src: String? = attributes.getValue("src")
                    if (src == null) {
                        throw SAXException("There needs to be a src attribute in img")
                    }
                    document.content?.blocks?.add(ImageElement(src))
                } else if (qName.equalsIgnoreCase("p")) {
                    paragraph_elem = ParagraphElement()
                    state.push(State.PARAGRAPH)
                } else if (qName.equalsIgnoreCase("h1")) {
                    state.push(State.HEADING)
                    heading_level = 1
                    heading_text = ""
                } else if (qName.equalsIgnoreCase("h2")) {
                    state.push(State.HEADING)
                    heading_level = 2
                    heading_text = ""
                } else if (qName.equalsIgnoreCase("h3")) {
                    state.push(State.HEADING)
                    heading_level = 3
                    heading_text = ""
                }

            }
            State.PARAGRAPH -> {
                if (qName.equalsIgnoreCase("b")) {
                    paragraph_formatstack.push(FormatState.BOLD)
                    emitFormatCommands()
                } else if (qName.equalsIgnoreCase("i")) {
                    paragraph_formatstack.push(FormatState.ITALIC)
                    emitFormatCommands()
                } else if (qName.equalsIgnoreCase("u")) {
                    paragraph_formatstack.push(FormatState.UNDERLINE)
                    emitFormatCommands()
                } else if (qName.equalsIgnoreCase("link")) {
                    state.push(State.LINK)
                    link_ref = attributes.getValue("ref")
                    link_text = ""
                }
            }
            else -> {
                throw SAXException("Invalid element '" + qName + "' when in a '" + state.peek().toString() + "'")
            }
        }
    }

    override fun endElement(uri: String?, localName: String, qName: String) {
        if (qName.equalsIgnoreCase("p")) {
            assert(state.pop() == State.PARAGRAPH)
            document.content?.blocks?.add(paragraph_elem!!)
        } else if (qName.equalsIgnoreCase("b") || qName.equalsIgnoreCase("i") || qName.equalsIgnoreCase("u")) {
            assert(state.peek() == State.PARAGRAPH)
            paragraph_formatstack.pop()
            emitFormatCommands()
        } else if (qName.equalsIgnoreCase("link")) {
            assert(state.pop() == State.LINK)
            assert(state.peek() == State.PARAGRAPH)
            paragraph_elem?.commands?.add(LinkCommand(link_text, link_ref))
        } else if (qName.equalsIgnoreCase("document")) {
            assert(state.pop() == State.DOCUMENT)
        } else if (qName.equalsIgnoreCase("content")) {
            assert(state.pop() == State.CONTENT)
        } else if(qName.equalsIgnoreCase("h1") || qName.equalsIgnoreCase("h2") || qName.equalsIgnoreCase("h3")) {
            assert(state.pop() == State.HEADING)
            document.content?.blocks?.add(HeadingElement(heading_level, heading_text))
        }
    }

    override fun characters(ch: CharArray?, start: Int, length: Int) {
        val str: String = java.lang.String(ch, start, length).toString()
        when (state.peek()) {
            State.TITLE -> {
                document.title += str
                state.pop()
            }
            State.PARAGRAPH -> {
                paragraph_elem?.commands?.add(TextCommand(str))
            }
            State.LINK -> {
                link_text = str
            }
            State.HEADING -> {
                heading_text = str
            }
        }
    }

    private fun emitFormatCommands() {
        assert(state.peek() == State.PARAGRAPH)

        var hasBold = false
        var hasItalic = false
        var hasUnderline = false
        for (attr in paragraph_formatstack) {
            when (attr) {
                FormatState.BOLD -> hasBold = true
                FormatState.ITALIC -> hasItalic = true
                FormatState.UNDERLINE -> hasUnderline = true
            }
        }
        val commands = paragraph_elem?.commands
        if (commands != null) {
            commands.add(BoldCommand(hasBold))
            commands.add(UnderlineCommand(hasUnderline))
            commands.add(ItalicCommand(hasItalic))
        }
    }
}