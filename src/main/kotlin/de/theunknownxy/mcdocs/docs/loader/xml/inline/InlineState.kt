package de.theunknownxy.mcdocs.docs.loader.xml.inline

import de.theunknownxy.mcdocs.docs.loader.xml.State
import de.theunknownxy.mcdocs.docs.loader.xml.XMLParserHandler
import org.xml.sax.Attributes
import org.xml.sax.SAXException

abstract class InlineState(handler: XMLParserHandler, val parent: InlineContainerState?) : State(handler) {
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