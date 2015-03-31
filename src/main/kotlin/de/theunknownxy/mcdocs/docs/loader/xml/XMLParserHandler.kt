package de.theunknownxy.mcdocs.docs.loader.xml

import de.theunknownxy.mcdocs.docs.Content
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import java.lang
import java.util.Stack

public class XMLParserHandler : DefaultHandler() {
    public class ParsedDocument {
        var title: String = ""
        var content: Content = Content()
    }

    var document: ParsedDocument = ParsedDocument()
    val xmlstate: Stack<State> = Stack()

    override fun startDocument() {
        xmlstate.push(StartState(this))
    }

    override fun endDocument() {
        assert(xmlstate.pop() is StartState)
    }

    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        xmlstate.peek().startElement(uri, localName, qName, attributes)
    }

    override fun endElement(uri: String?, localName: String, qName: String) {
        xmlstate.peek().endElement(uri, localName, qName)
    }

    override fun characters(ch: CharArray?, start: Int, length: Int) {
        val str: String = lang.String(ch, start, length).toString()
        xmlstate.peek().characters(str)
    }
}